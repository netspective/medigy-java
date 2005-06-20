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
 */
package com.medigy.persist.model.data;

import com.medigy.persist.model.common.Entity;
import com.medigy.persist.model.party.Party;
import com.medigy.persist.model.session.ProcessSession;
import com.medigy.persist.model.session.SessionManager;
import com.medigy.persist.reference.AbstractReferenceEntity;
import com.medigy.persist.reference.CachedReferenceEntity;
import com.medigy.persist.reference.ReferenceEntity;
import com.medigy.persist.reference.custom.CachedCustomHierarchyReferenceEntity;
import com.medigy.persist.reference.custom.CachedCustomReferenceEntity;
import com.medigy.persist.reference.custom.CustomReferenceEntity;
import com.medigy.persist.util.HibernateUtil;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.cfg.Environment;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.PersistentClass;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.MatchResult;

public class EntitySeedDataPopulator
{
    private final Log log = LogFactory.getLog(EntitySeedDataPopulator.class);

    public static final String EXTERNAL_DATA_PROPERTY_FILE = "data-mappings.properties";

    private Session session;
    private Configuration configuration;
    private Party globalParty;

    public EntitySeedDataPopulator(final Session session, final Configuration configuration)
    {
        this.session = session;
        this.configuration = configuration;
    }

     /**
     * Loads the reference data contained in separate data files
     */
    protected void loadExternalReferenceData()
    {
        try
        {
            InputStream propertyFileStream = Environment.class.getResourceAsStream(EXTERNAL_DATA_PROPERTY_FILE);
            if (propertyFileStream == null)
                propertyFileStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(EXTERNAL_DATA_PROPERTY_FILE);
            if (propertyFileStream == null)
            {
                log.warn("'" + EXTERNAL_DATA_PROPERTY_FILE + "' property file not found for loading reference data contained " +
                        "in external files.");
                return;
            }
            Properties props = new java.util.Properties();
            props.load(propertyFileStream);

            final Enumeration keys = props.keys();
            while (keys.hasMoreElements())
            {
                final String className = (String)keys.nextElement();
                final String dataFileName = props.getProperty(className);
                InputStream stream = Environment.class.getResourceAsStream(dataFileName);
                if (stream == null)
                    stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(dataFileName);
                Scanner sc = new Scanner(stream);
                int rowsAdded = 0;
                if (sc.hasNextLine())
                {
                    sc.findInLine("\\\"([ \\.0-9a-zA-Z]*)\\\"\\s*\\\"([ \\.0-9a-zA-Z]*)\\\"\\s*\\\"([ \\.0-9a-zA-Z]*)\\\"\\s*\\\"([ \\.0-9a-zA-Z]*)\\\"");
                    try
                    {
                        MatchResult result = sc.match();
                        final Class refClass = Class.forName(className);
                        final Object refObject = refClass.newInstance();
                        if (refObject instanceof AbstractReferenceEntity)
                        {
                            final AbstractReferenceEntity refEntity = (AbstractReferenceEntity) refObject;
                            refEntity.setCode(result.group(0));
                            refEntity.setLabel(result.group(1));
                            refEntity.setDescription(result.group(2));
                            HibernateUtil.getSession().save(refEntity);
                            rowsAdded++;
                        }
                    }
                    catch (Exception e)
                    {
                         log.error(className + ": Error at data row count = " + rowsAdded + " \n" + ExceptionUtils.getStackTrace(e));
                    }
                }
                sc.close();
                log.info(className + ", Rows Added = " + rowsAdded);
            }
            //InputStream stream = new FileInputStream("e:\\netspective\\medigy\\persistence\\database\\refdata\\icd9-codes.txt");
        }
        catch (Exception e)
        {
            log.error(ExceptionUtils.getStackTrace(e));
        }
    }

    public void populateEntityCacheData() throws HibernateException
    {
        final Iterator itr = configuration.getClassMappings();
        while (itr.hasNext())
        {
            Class entityClass = ((PersistentClass) itr.next()).getMappedClass(); //(Class) classMappings.next();
            log.warn(entityClass.getName());
            if (!Entity.class.isAssignableFrom(entityClass))
                continue;

            Class[] innerClasses = entityClass.getDeclaredClasses();
            for (Class innerClass : innerClasses)
            {
                // TODO: assume that this is the inner CACHE class !???!!! maybe make Cache extend an interface to indicate this??
                if (innerClass.isEnum() && !entityClass.equals(Party.class))
                {
                    try
                    {
                        final BeanInfo beanInfo = Introspector.getBeanInfo(entityClass);
                        final PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
                        final Hashtable pdsByName = new Hashtable();
                        for (int i = 0; i < descriptors.length; i++)
                        {
                            final PropertyDescriptor descriptor = descriptors[i];
                            if (descriptor.getWriteMethod() != null)
                                pdsByName.put(descriptor.getReadMethod().getName(), descriptor.getWriteMethod());
                        }

                        Object[] enumObjects = innerClass.getEnumConstants();
                        // now match the enum methods with the enclosing class' methods
                        for (Object enumObj : enumObjects)
                        {
                            Object entityObj = entityClass.newInstance();
                            final Method[] enumMethods = enumObj.getClass().getMethods();
                            for (Method enumMethod : enumMethods)
                            {
                                final Method writeMethod = (Method) pdsByName.get(enumMethod.getName());
                                if (writeMethod != null)
                                {
                                    writeMethod.invoke(entityObj, enumMethod.invoke(enumObj));
                                }
                            }
                            HibernateUtil.getSession().save(entityObj);
                        }
                    }
                    catch (IntrospectionException e)
                    {
                        log.error(e);
                    }
                    catch (IllegalAccessException e)
                    {
                        log.error(e);
                    }
                    catch (InstantiationException e)
                    {
                        log.error(e);
                    }
                    catch (InvocationTargetException e)
                    {
                        log.error(e);
                    }
                    catch (HibernateException e)
                    {
                        log.error(e);
                    }
                }
            }
        }
    }

    public void populateSeedData() throws HibernateException
    {
        com.medigy.persist.model.session.Session processSession = new ProcessSession();
        processSession.setProcessName(EntitySeedDataPopulator.class.getName());

        session.save(processSession);
        SessionManager.getInstance().pushActiveSession(processSession);

        if (log.isInfoEnabled())
            log.info("Initializing with seed data");
        globalParty = new Party(Party.SYS_GLOBAL_PARTY_NAME);
        session.save(globalParty);

        final Map<Class, Class> referenceEntitiesAndCachesMap = HibernateUtil.getReferenceEntitiesAndRespectiveEnums(configuration);
        for(final Map.Entry<Class, Class> entry : referenceEntitiesAndCachesMap.entrySet())
        {
            final Class aClass = entry.getKey();
            CachedReferenceEntity[] cachedEntities = (CachedReferenceEntity[]) entry.getValue().getEnumConstants();
            Object[][] data = new Object[cachedEntities.length][2];
            int i=0;
            for(final CachedReferenceEntity c : cachedEntities)
            {
                data[i][0] = c.getCode();
                data[i][1] = c.getLabel(); // LABEL
                i++;
            }
            if (log.isInfoEnabled())
                log.info(aClass.getCanonicalName() + " cached enums addded.");
            populateCachedReferenceEntities(session, aClass, cachedEntities, new String[] {"code", "label"}, data);
        }

        final Map<Class, Class> customReferenceEntitiesAndCachesMap = HibernateUtil.getCustomReferenceEntitiesAndRespectiveEnums(configuration);
        for(final Map.Entry<Class, Class> entry :customReferenceEntitiesAndCachesMap.entrySet())
        {
            final Class aClass = entry.getKey();
            CachedCustomReferenceEntity[] cachedEntities = (CachedCustomReferenceEntity[]) entry.getValue().getEnumConstants();
            Object[][] data = new Object[cachedEntities.length][4];
            int i=0;
            for(final CachedCustomReferenceEntity c : cachedEntities)
            {
                data[i][0] = c.getCode();
                data[i][1] = c.getLabel(); // LABEL
                data[i][2] = globalParty;

                if (c instanceof CachedCustomHierarchyReferenceEntity)
                    data[i][3] = ((CachedCustomHierarchyReferenceEntity)c).getParent();
                else
                    data[i][3] = null;
                i++;
            }
            if (log.isInfoEnabled())
                log.info(aClass.getCanonicalName() + " cached custom enums addded.");
            populateCachedCustomReferenceEntities(session, aClass, cachedEntities, new String[] {"code", "label", "party", "parentEntity"}, data);
        }
        //loadExternalReferenceData();
        //populateEntityCacheData();
        //HibernateUtil.commitTransaction();
        SessionManager.getInstance().popActiveSession();
    }

    protected void  populateCachedCustomReferenceEntities(final Session session,
                                                          final Class entityClass,
                                                          final CachedCustomReferenceEntity[] cachedEntities,
                                                          final String[] propertyList,
                                                          final Object[][] data)  throws HibernateException
    {
        try
        {
            final Hashtable pdsByName = new Hashtable();
            final BeanInfo beanInfo = Introspector.getBeanInfo(entityClass);
            final PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < descriptors.length; i++)
            {
                final PropertyDescriptor descriptor = descriptors[i];
                if (descriptor.getWriteMethod() != null)
                    pdsByName.put(descriptor.getName(), descriptor.getWriteMethod());
            }

            Map entityMapByCache = new HashMap<CachedCustomReferenceEntity, Object>();
            for (int i = 0; i < data.length; i++)
            {
                final Object entityObject = entityClass.newInstance();
                for (int j = 0; j < propertyList.length; j++)
                {
                    final Method setter = (Method) pdsByName.get(propertyList[j]);
                    if (setter != null && data[i][j] != null)
                    {
                        if (data[i][j] instanceof CachedCustomHierarchyReferenceEntity)
                        {
                            setter.invoke(entityObject, new Object[] {entityMapByCache.get(data[i][j])});
                        }
                        else
                            setter.invoke(entityObject, new Object[] {data[i][j]});
                    }
                }
                entityMapByCache.put(cachedEntities[i], entityObject);
                session.save(entityObject);
                cachedEntities[i].setEntity((CustomReferenceEntity) entityObject);
            }
        }
        catch (Exception e)
        {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new HibernateException(e);
        }
    }

    protected void  populateCachedReferenceEntities(final Session session,
                                                    final Class entityClass,
                                                    final CachedReferenceEntity[] cachedEntities,
                                                    final String[] propertyList,
                                                    final Object[][] data)  throws HibernateException
    {
        try
        {
            final Hashtable pdsByName = new Hashtable();
            final BeanInfo beanInfo = Introspector.getBeanInfo(entityClass);
            final PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < descriptors.length; i++)
            {
                final PropertyDescriptor descriptor = descriptors[i];
                if (descriptor.getWriteMethod() != null)
                    pdsByName.put(descriptor.getName(), descriptor.getWriteMethod());
            }

            Map entityMapByCache = new HashMap<CachedReferenceEntity, Object>();
            for (int i = 0; i < data.length; i++)
            {
                final Object entityObject = entityClass.newInstance();
                for (int j = 0; j < propertyList.length; j++)
                {
                    final Method setter = (Method) pdsByName.get(propertyList[j]);
                    if (setter != null && data[i][j] != null)
                    {
                        setter.invoke(entityObject, new Object[] {data[i][j]});
                    }
                }
                entityMapByCache.put(cachedEntities[i], entityObject);
                session.save(entityObject);
                cachedEntities[i].setEntity((ReferenceEntity) entityObject);
            }
        }
        catch (Exception e)
        {
            log.error(e);
            throw new HibernateException(e);
        }
    }
}
