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
 */
package com.medigy.persist.util;

import com.medigy.persist.model.data.EntitySeedDataPopulator;
import com.medigy.persist.model.party.Party;
import com.medigy.persist.reference.CachedReferenceEntity;
import com.medigy.persist.reference.ReferenceEntity;
import com.medigy.persist.reference.custom.CachedCustomReferenceEntity;
import com.medigy.persist.reference.custom.CustomReferenceEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import java.util.Map;

public class ModelInitializer
{
    public enum SeedDataPopulationType
    {
        AUTO, YES, NO
    }

    private final Log log = LogFactory.getLog(ModelInitializer.class);
    private SeedDataPopulationType seedDataPopulationType;
    private Session session;
    private Configuration hibernateConfiguration;
    private boolean isInitialized = false;

    private Map<Class,Class<? extends CachedReferenceEntity>> referenceEntitiesAndCachesMap;
    private Map<Class,Class<? extends CachedCustomReferenceEntity>> customReferenceEntitiesAndCachesMap;

    private static ModelInitializer initializer = new ModelInitializer();

    public static final ModelInitializer getInstance()
    {
        return initializer;
    }

    public boolean isInitialized()
    {
        return isInitialized;
    }

    private ModelInitializer()
    {

    }

    /**
     * There are two ways to initialize the data model: one is to use the hibernate data model to create/insert reference entities
     * and then cache these values and the other is to only read from the data model and cache the reference entities.
     * If you already know that the database instance is "clean", then you should use the latter method and if not, you should use
     * the former method.
     *
     * @param session
     * @param seedDataPopulationType
     * @param hibernateConfiguration
     */
    public void initialize(final Session session, final SeedDataPopulationType seedDataPopulationType, final Configuration hibernateConfiguration)
    {
        this.session = session;
        this.seedDataPopulationType = seedDataPopulationType;
        this.hibernateConfiguration = hibernateConfiguration;

        boolean populate = false;
        log.info("Initialize model setting: " + seedDataPopulationType);
        switch(seedDataPopulationType)
        {
            case AUTO:
                if(readSystemGlobalPropertyEntity() == null)
                {
                    populateSeedData();
                    populate = true;
                }
                break;

            case YES:
                populateSeedData();
                populate = true;
                break;
        }
        initSystemGlobalPartyEntity();
        if (!populate)
        {
            // the EntitySeedDataPopulator also registers the cache entities so no need to do this if it was called
            initReferenceEntityCaches();
            initCustomReferenceEntityCaches();
        }
        isInitialized = true;
    }

    private void populateSeedData()
    {
        EntitySeedDataPopulator populator = new EntitySeedDataPopulator(session, hibernateConfiguration);
        populator.populateSeedData();
    }

    private Party readSystemGlobalPropertyEntity()
    {
        final Criteria criteria = session.createCriteria(Party.class);
        criteria.add(Restrictions.eq("partyName", Party.SYS_GLOBAL_PARTY_NAME));
        return (Party) criteria.uniqueResult();
    }

    private void initSystemGlobalPartyEntity()
    {
        final Party entity = readSystemGlobalPropertyEntity();
        if (entity == null)
             throw new RuntimeException("The " + Party.SYS_GLOBAL_PARTY_NAME + " entity MUST exist before trying to " +
                    "access related built-in custom reference entities.");

        Party.Cache.SYS_GLOBAL_PARTY.setEntity(entity);
    }

    protected void initReferenceEntityCaches()
    {
        referenceEntitiesAndCachesMap = HibernateUtil.getReferenceEntitiesAndRespectiveEnums(hibernateConfiguration);
        for(final Map.Entry<Class,Class<? extends CachedReferenceEntity>> entry : referenceEntitiesAndCachesMap.entrySet())
            initReferenceEntityCache(entry.getKey(), (CachedReferenceEntity[]) entry.getValue().getEnumConstants());
    }

    protected void initCustomReferenceEntityCaches()
    {
        customReferenceEntitiesAndCachesMap = HibernateUtil.getCustomReferenceEntitiesAndRespectiveEnums(hibernateConfiguration);
        for(final Map.Entry<Class,Class<? extends CachedCustomReferenceEntity>> entry : customReferenceEntitiesAndCachesMap.entrySet())
            initCustomReferenceEntityCache(entry.getKey(), (CachedCustomReferenceEntity[]) entry.getValue().getEnumConstants());
    }

    protected void initReferenceEntityCache(final Class aClass, final CachedReferenceEntity[] cache)
    {
        final List list = session.createCriteria(aClass).list();
        for(final Object i : list)
        {
            final ReferenceEntity entity = (ReferenceEntity) i;
            final Object id = entity.getCode();
            if(id == null)
            {
                log.error(entity + " id is NULL: unable to map to one of " + cache);
                continue;
            }

            for(final CachedReferenceEntity c : cache)
            {
                if(id.equals(c.getCode()))
                {
                    final ReferenceEntity record = c.getEntity();
                    if(record != null)
                        log.error(c.getClass().getName() + " enum '" + c + "' is bound to multiple rows.");
                    else
                        c.setEntity(entity);
                    break;
                }
            }
        }

        for(final CachedReferenceEntity c : cache)
        {
            if(c.getEntity() == null)
                log.error(c.getClass().getName() + " enum '" + c + "' was not bound to a database row.");
        }
    }

    protected void initCustomReferenceEntityCache(final Class aClass, final CachedCustomReferenceEntity[] cache)
    {
        final Criteria criteria = session.createCriteria(aClass);
        criteria.add(Restrictions.eq("party.id", Party.Cache.SYS_GLOBAL_PARTY.getEntity().getPartyId()));

        final List list = criteria.list();
        if (list == null || list.size() == 0)
        {
            log.error("Failed to initialize CustomReferenceEntity caches. There were NO records of CustomReferenceEntity type.");
            throw new RuntimeException("There were NO records of CustomReferenceEntity type: " + aClass.getName());
        }
        for(final Object i : list)
        {
            final CustomReferenceEntity entity = (CustomReferenceEntity) i;
            final Object code = entity.getCode();
            if(code == null)
            {
                log.error(entity + " code is NULL: unable to map to one of " + cache);
                continue;
            }

            for(final CachedCustomReferenceEntity c : cache)
            {
                if(code.equals(c.getCode()))
                {
                    final CustomReferenceEntity record = c.getEntity();
                    if(record == null)
                        c.setEntity(entity);
                    break;
                }
            }
        }

        for(final CachedCustomReferenceEntity c : cache)
        {
            if(c.getEntity() == null)
                log.error(c.getClass().getName() + " enum '" + c + "' was not bound to a database row.");
        }

    }
}
