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
import com.medigy.persist.reference.custom.CachedCustomReferenceEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import java.util.Map;

public class HibernateModelInitializer  extends AbstractModelInitializer
{
    private final Log log = LogFactory.getLog(HibernateModelInitializer.class);

    private Session session;
    private Configuration hibernateConfiguration;
    private Map<Class,Class<? extends CachedReferenceEntity>> referenceEntitiesAndCachesMap;
    private Map<Class,Class<? extends CachedCustomReferenceEntity>> customReferenceEntitiesAndCachesMap;

    private static HibernateModelInitializer initializer = new HibernateModelInitializer();

    public static final HibernateModelInitializer getInstance()
    {
        return initializer;
    }

    private HibernateModelInitializer()
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
        this.hibernateConfiguration = hibernateConfiguration;
        super.initialize(seedDataPopulationType);
    }

    protected EntitySeedDataPopulator createEntitySeedDataPopulator()
    {
        return new EntitySeedDataPopulator(session, hibernateConfiguration);
    }

    protected Party readSystemGlobalPropertyEntity()
    {
        final Criteria criteria = session.createCriteria(Party.class);
        criteria.add(Restrictions.eq("partyName", Party.SYS_GLOBAL_PARTY_NAME));
        return (Party) criteria.uniqueResult();
    }

    protected void initReferenceEntityCaches()
    {
        referenceEntitiesAndCachesMap = HibernateUtil.getReferenceEntitiesAndRespectiveEnums(hibernateConfiguration);
        for(final Map.Entry<Class,Class<? extends CachedReferenceEntity>> entry : referenceEntitiesAndCachesMap.entrySet())
        {
            final Class aClass = entry.getKey();
            final List list = session.createCriteria(aClass).list();
            initReferenceEntityList(aClass, (CachedReferenceEntity[]) entry.getValue().getEnumConstants(), list);
        }
    }

    protected void initCustomReferenceEntityCaches()
    {
        customReferenceEntitiesAndCachesMap = HibernateUtil.getCustomReferenceEntitiesAndRespectiveEnums(hibernateConfiguration);
        for(final Map.Entry<Class,Class<? extends CachedCustomReferenceEntity>> entry : customReferenceEntitiesAndCachesMap.entrySet())
        {
            final Class aClass = entry.getKey();
            final Criteria criteria = session.createCriteria(aClass);
            criteria.add(Restrictions.eq("party.id", Party.Cache.SYS_GLOBAL_PARTY.getEntity().getPartyId()));
            final List list = criteria.list();
            initCustomReferenceEntityList(aClass, (CachedCustomReferenceEntity[]) entry.getValue().getEnumConstants(), list);
        }
    }



}
