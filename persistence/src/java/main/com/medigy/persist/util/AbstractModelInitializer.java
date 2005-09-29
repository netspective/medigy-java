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
package com.medigy.persist.util;

import com.medigy.persist.model.data.EntitySeedDataPopulator;
import com.medigy.persist.model.party.Party;
import com.medigy.persist.reference.CachedReferenceEntity;
import com.medigy.persist.reference.ReferenceEntity;
import com.medigy.persist.reference.custom.CachedCustomReferenceEntity;
import com.medigy.persist.reference.custom.CustomReferenceEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

public abstract class AbstractModelInitializer
{
    public enum SeedDataPopulationType
    {
        AUTO, YES, NO
    }

    private final Log log = LogFactory.getLog(AbstractModelInitializer.class);
    private SeedDataPopulationType seedDataPopulationType;
    private boolean isInitialized = false;

    public SeedDataPopulationType getSeedDataPopulationType()
    {
        return seedDataPopulationType;
    }

    public void setSeedDataPopulationType(final SeedDataPopulationType seedDataPopulationType)
    {
        this.seedDataPopulationType = seedDataPopulationType;
    }

    public boolean isInitialized()
    {
        return isInitialized;
    }

    public void setInitialized(final boolean initialized)
    {
        isInitialized = initialized;
    }

    protected abstract void initReferenceEntityCaches();
    protected abstract void initCustomReferenceEntityCaches();

    protected void initReferenceEntityList(final Class aClass, final CachedReferenceEntity[] cache, final List list)
    {
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

    protected void initCustomReferenceEntityList(final Class aClass, final CachedCustomReferenceEntity[] cache, final List list)
    {
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

    protected void initSystemGlobalPartyEntity()
    {
        final Party entity = readSystemGlobalPropertyEntity();
        if (entity == null)
             throw new RuntimeException("The " + Party.SYS_GLOBAL_PARTY_NAME + " entity MUST exist before trying to " +
                    "access related built-in custom reference entities.");

        Party.Cache.SYS_GLOBAL_PARTY.setEntity(entity);
    }

    protected void initialize(final SeedDataPopulationType seedDataPopulationType)
    {
        boolean populate = false;
        if (log.isInfoEnabled())
            log.info("Initialize model setting: " + seedDataPopulationType);
        switch (seedDataPopulationType)
        {
            case AUTO:
                if(readSystemGlobalPropertyEntity() == null)
                {
                    createEntitySeedDataPopulator().populateSeedData();
                    populate = true;
                }
                break;

            case YES:
                createEntitySeedDataPopulator().populateSeedData();
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
        setInitialized(true);
    }

    protected abstract EntitySeedDataPopulator createEntitySeedDataPopulator();
    protected abstract Party readSystemGlobalPropertyEntity();

}
