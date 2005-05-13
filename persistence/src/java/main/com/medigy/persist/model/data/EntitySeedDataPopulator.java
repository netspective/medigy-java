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

import com.medigy.persist.model.party.Party;
import com.medigy.persist.model.session.ProcessSession;
import com.medigy.persist.model.session.SessionManager;
import com.medigy.persist.reference.custom.CachedCustomHierarchyReferenceEntity;
import com.medigy.persist.reference.custom.CachedCustomReferenceEntity;
import com.medigy.persist.util.HibernateConfiguration;
import com.medigy.persist.util.HibernateUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Map;
import java.util.HashMap;

public class EntitySeedDataPopulator
{
    private final Log log = LogFactory.getLog(EntitySeedDataPopulator.class);

    private Session session;
    private HibernateConfiguration configuration;
    private Party globalParty;

    public EntitySeedDataPopulator(final Session session, final HibernateConfiguration configuration)
    {
        this.session = session;
        this.configuration = configuration;
    }

    public void populateSeedData() throws HibernateException
    {
        com.medigy.persist.model.session.Session session = new ProcessSession();
        session.setProcessName(EntitySeedDataPopulator.class.getName());

        HibernateUtil.beginTransaction();        
        HibernateUtil.getSession().save(session);
        SessionManager.getInstance().pushActiveSession(session);

        if (log.isInfoEnabled())
            log.info("Initializing with seed data");
        globalParty = new Party(Party.SYS_GLOBAL_PARTY_NAME);
        HibernateUtil.getSession().save(globalParty);

        for(final Map.Entry<Class, Class> entry : configuration.getCustomReferenceEntitiesAndCachesMap().entrySet())
        {
            final Class aClass = entry.getKey();
            CachedCustomReferenceEntity[] cachedEntities = (CachedCustomReferenceEntity[]) entry.getValue().getEnumConstants();
            Object[][] data = new Object[cachedEntities.length][4];
            int i=0;
            for(final CachedCustomReferenceEntity c : cachedEntities)
            {
                data[i][0] = c.getCode();
                data[i][1] = c.getCode(); // LABEL
                data[i][2] = globalParty;

                if (c instanceof CachedCustomHierarchyReferenceEntity)
                    data[i][3] = ((CachedCustomHierarchyReferenceEntity)c).getParent();
                else
                    data[i][3] = null;
                i++;
            }
            if (log.isInfoEnabled())
                log.info(aClass.getCanonicalName() + " cached enums addded.");
            populateEntity(HibernateUtil.getSession(), aClass, cachedEntities, new String[] {"code", "label", "party", "parentEntity"}, data);
        }
        HibernateUtil.commitTransaction();
        SessionManager.getInstance().popActiveSession();
    }

    protected void  populateEntity(final Session session,
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
            }
        }
        catch (Exception e)
        {
            log.error(e);
            throw new HibernateException(e);
        }
    }
}
