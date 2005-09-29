/*
 * Copyright (c) 2000-2003 Netspective Communications LLC. All rights reserved.
 *
 * Netspective Communications LLC ("Netspective") permits redistribution, modification and use of this file in source
 * and binary form ("The Software") under the Netspective Source License ("NSL" or "The License"). The following
 * conditions are provided as a summary of the NSL but the NSL remains the canonical license and must be accepted
 * before using The Software. Any use of The Software indicates agreement with the NSL.
 *
 * 1. Each copy or derived work of The Software must preserve the copyright notice and this notice unmodified.
 *
 * 2. Redistribution of The Software is allowed in object code form only (as Java .class files or a .jar file
 *    containing the .class files) and only as part of an application that uses The Software as part of its primary
 *    functionality. No distribution of the package is allowed as part of a software development kit, other library,
 *    or development tool without written consent of Netspective. Any modified form of The Software is bound by these
 *    same restrictions.
 *
 * 3. Redistributions of The Software in any form must include an unmodified copy of The License, normally in a plain
 *    ASCII text file unless otherwise agreed to, in writing, by Netspective.
 *
 * 4. The names "Netspective", "Axiom", "Commons", "Junxion", and "Sparx" are trademarks of Netspective and may not be
 *    used to endorse products derived from The Software without without written consent of Netspective. "Netspective",
 *    "Axiom", "Commons", "Junxion", and "Sparx" may not appear in the names of products derived from The Software
 *    without written consent of Netspective.
 *
 * 5. Please attribute functionality where possible. We suggest using the "powered by Netspective" button or creating
 *    a "powered by Netspective(tm)" link to http://www.netspective.com for each application using The Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT,
 * ARE HEREBY DISCLAIMED.
 *
 * NETSPECTIVE AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE OR ANY THIRD PARTY AS A
 * RESULT OF USING OR DISTRIBUTING THE SOFTWARE. IN NO EVENT WILL NETSPECTIVE OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THE SOFTWARE, EVEN
 * IF HE HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * @author Shahid N. Shah
 */

/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.util;

import com.medigy.persist.reference.CachedReferenceEntity;
import com.medigy.persist.reference.ReferenceEntity;
import com.medigy.persist.reference.custom.CachedCustomReferenceEntity;
import com.medigy.persist.reference.custom.CustomReferenceEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.connection.ConnectionProvider;
import org.hibernate.connection.ConnectionProviderFactory;
import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.exception.NestableRuntimeException;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.stat.EntityStatistics;
import org.hibernate.stat.Statistics;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HibernateUtil
{
    private static final Log log = LogFactory.getLog(HibernateUtil.class);
    private static SessionFactory sessionFactory;
    private static ConnectionProvider connectionProvider;
    private static final ThreadLocal<Session> threadSession = new ThreadLocal<Session>();
    private static final ThreadLocal<Transaction> threadTransaction = new ThreadLocal<Transaction>();

    public static void setConfiguration(Configuration cfg) throws HibernateException
    {
        try
        {
            if (sessionFactory != null)
            {
                connectionProvider.close();
                sessionFactory.close();
            }
            sessionFactory = cfg.buildSessionFactory();
            connectionProvider = ConnectionProviderFactory.newConnectionProvider(cfg.getProperties());
        }
        catch (MappingException e)
        {
            throw e;
        }
    }

    public static Connection getNewConnection()
    {
        try
        {
            return connectionProvider.getConnection();
        }
        catch (SQLException e)
        {
            log.error(e);
            throw new NestableRuntimeException(e);
        }
    }

    public static void closeSessionFactory()
    {
        if (sessionFactory != null)
            sessionFactory.close();
    }

    public static Session getSession()
    {
        Session session = threadSession.get();
        if(session == null)
        {
            try
            {
                session = sessionFactory.openSession();
                threadSession.set(session);
            }
            catch (HibernateException e)
            {
                log.error(e);
                throw new NestableRuntimeException(e);
            }
        }

        return session;
    }

    public static void closeSession()
    {
        Session session = threadSession.get();
        if(session != null)
        {
            threadSession.set(null);
            try
            {
                if(session.isOpen())
                    session.close();
            }
            catch (HibernateException e)
            {
                log.error(e);
                throw new NestableRuntimeException(e);
            }
        }
    }

    public static void beginTransaction()
    {
        Transaction tx = threadTransaction.get();
        if(tx == null)
        {
            try
            {
                tx = getSession().beginTransaction();
                threadTransaction.set(tx);
            }
            catch (HibernateException e)
            {
                log.error(e);
                throw new NestableRuntimeException(e);
            }
        }
    }

    public static void commitTransaction()
    {
        Transaction tx = threadTransaction.get();
        try
        {
            threadTransaction.set(null);
            if(tx != null && !tx.wasCommitted() && ! tx.wasRolledBack())
                tx.commit();
        }
        catch (HibernateException e)
        {
            log.error(e);
            throw new NestableRuntimeException(e);
        }
    }

    public static void rollbackTransaction()
    {
        Transaction tx = threadTransaction.get();
        try
        {
            threadTransaction.set(null);
            if(tx != null && !tx.wasCommitted() && ! tx.wasRolledBack())
                tx.rollback();
        }
        catch (HibernateException e)
        {
            log.error(e);
            throw new NestableRuntimeException(e);
        }
    }

    public static void enableStatistics()
    {
        sessionFactory.getStatistics().setStatisticsEnabled(true);
    }

    public static void logStatistics()
    {

        Statistics stats = HibernateUtil.sessionFactory.getStatistics();
        if (!stats.isStatisticsEnabled())
        {
            log.warn("Statistics are not enabled.");
            return;
        }
        log.info("Connection count: " + stats.getConnectCount());
        log.info("Session open count: " + stats.getSessionOpenCount());
        log.info("Session close count: " + stats.getSessionCloseCount());

        log.info("Entities insert count: " + stats.getEntityInsertCount());
        log.info("Entities update count: " + stats.getEntityUpdateCount());
        log.info("Entities fetch count: " + stats.getEntityFetchCount());

        log.info("Collection fetch count: " + stats.getCollectionFetchCount());
        log.info("Collection load count: " + stats.getCollectionLoadCount());
        double queryCacheHitCount  = stats.getQueryCacheHitCount();
        double queryCacheMissCount = stats.getQueryCacheMissCount();
        double queryCacheHitRatio = queryCacheHitCount / (queryCacheHitCount + queryCacheMissCount);
        log.info("Query Hit ratio:" + queryCacheHitRatio);
    }

    public static void logEntityStatistics(final Class entityClass)
    {
        Statistics stats = HibernateUtil.sessionFactory.getStatistics();
        EntityStatistics entityStats = stats.getEntityStatistics( entityClass.getName() );
        long changes =
                entityStats.getInsertCount()
                + entityStats.getUpdateCount()
                + entityStats.getDeleteCount();
        log.info(entityClass.getName() + " changed " + changes + "times"  );
    }

    protected static Map<Class, Class<? extends CachedReferenceEntity>> getReferenceEntitiesAndRespectiveEnums(final Iterator classMappings)
    {
        Map<Class, Class<? extends CachedReferenceEntity>> referenceEntitiesAndCachesMap = new HashMap<Class, Class<? extends CachedReferenceEntity>>();
        while (classMappings.hasNext())
        {
            Class aClass = ((PersistentClass) classMappings.next()).getMappedClass(); //(Class) classMappings.next();
            if (ReferenceEntity.class.isAssignableFrom(aClass))
            {
                boolean foundCache = false;
                for (final Class ic : aClass.getClasses())
                {
                    if (CachedReferenceEntity.class.isAssignableFrom(ic))
                    {
                        if (ic.isEnum())
                        {
                            referenceEntitiesAndCachesMap.put(aClass, ic);
                            foundCache = true;
                        }
                        else
                            throw new HibernateException(ic + " must be an enum since " + aClass + " is a " + ReferenceEntity.class.getName());

                        break;
                    }

                }

                if (!foundCache)
                    log.warn(aClass + " is marked as a ReferenceEntity but does not contain a ReferenceEntityCache enum.");

                // TODO: find out how to ensure the new mapping for reference type is immutable and read only
                // final PersistentClass pClass = getClassMapping(aClass.getLabel());
            }
        }
        return referenceEntitiesAndCachesMap;

    }

    public static Map<Class, Class<? extends CachedReferenceEntity>> getReferenceEntitiesAndRespectiveEnums(final Ejb3Configuration configuration)
    {
        final Iterator classMappings = configuration.getClassMappings();
        return getReferenceEntitiesAndRespectiveEnums(classMappings);
    }

    /**
     * Creates a map of all classes implementing the {@link com.medigy.persist.reference.ReferenceEntity}
     *  interface and the respective cached enums
     *
     * @param hibernateConfiguration    the hibernate configuration
     * @return
     */
    public static Map<Class, Class<? extends CachedReferenceEntity>> getReferenceEntitiesAndRespectiveEnums(final Configuration hibernateConfiguration)
    {
        final Iterator classMappings = hibernateConfiguration.getClassMappings();
        return getReferenceEntitiesAndRespectiveEnums(classMappings);
    }

    /**
     * Creates a map of all classes implementing the {@link com.medigy.persist.reference.custom.CustomReferenceEntity}
     * interface and the respective cached enums
     *
     * @return
     */
    protected static Map<Class, Class<? extends CachedCustomReferenceEntity>> getCustomReferenceEntitiesAndRespectiveEnums(final Iterator classMappings)
    {
        final Map<Class, Class<? extends CachedCustomReferenceEntity>> customReferenceEntitiesAndCachesMap = new HashMap<Class, Class<? extends CachedCustomReferenceEntity>>();
        while (classMappings.hasNext())
        {
            Class aClass = ((PersistentClass) classMappings.next()).getMappedClass(); //(Class) classMappings.next();
            if (CustomReferenceEntity.class.isAssignableFrom(aClass))
            {
                for (final Class ic : aClass.getClasses())
                {
                    if (CachedCustomReferenceEntity.class.isAssignableFrom(ic))
                    {
                        if (ic.isEnum())
                        {
                            customReferenceEntitiesAndCachesMap.put(aClass, ic);
                        }
                        else
                            throw new HibernateException(ic + " must be an enum since " + aClass + " is a " +
                                    CachedCustomReferenceEntity.class.getName());

                        break;
                    }
                }
                // if no cache is found, its ok since these are custom
            }
        }
        return customReferenceEntitiesAndCachesMap;
    }

    public static Map<Class, Class<? extends CachedCustomReferenceEntity>> getCustomReferenceEntitiesAndRespectiveEnums(final Configuration configuration)
    {
        final Iterator classMappings = configuration.getClassMappings();
        return getCustomReferenceEntitiesAndRespectiveEnums(classMappings);
    }

    public static Map<Class, Class<? extends CachedCustomReferenceEntity>> getCustomReferenceEntitiesAndRespectiveEnums(final Ejb3Configuration configuration)
    {
        final Iterator classMappings = configuration.getClassMappings();
        return getCustomReferenceEntitiesAndRespectiveEnums(classMappings);
    }
}
