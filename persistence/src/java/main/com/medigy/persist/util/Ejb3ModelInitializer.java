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
import com.medigy.persist.reference.custom.CachedCustomReferenceEntity;
import org.hibernate.ejb.Ejb3Configuration;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

public class Ejb3ModelInitializer extends AbstractModelInitializer
{
    private EntityManager entityManager;
    private Ejb3Configuration ejb3Configuration;
    private Map<Class,Class<? extends CachedReferenceEntity>> referenceEntitiesAndCachesMap;
    private Map<Class,Class<? extends CachedCustomReferenceEntity>> customReferenceEntitiesAndCachesMap;

    private static Ejb3ModelInitializer initializer = new Ejb3ModelInitializer();

    public static final Ejb3ModelInitializer getInstance()
    {
        return initializer;
    }

    public void initialize(final EntityManager manager, final SeedDataPopulationType seedDataPopulationType, final Ejb3Configuration ejb3Configuration)
    {
        this.entityManager = manager;
        this.ejb3Configuration = ejb3Configuration;
        super.initialize(seedDataPopulationType);
    }

    protected EntitySeedDataPopulator createEntitySeedDataPopulator()
    {
        return new EntitySeedDataPopulator(entityManager, ejb3Configuration);
    }

    protected Party readSystemGlobalPropertyEntity()
    {
        final Query query = entityManager.createQuery("select party from " + Party.class.getSimpleName() + " as party  where party.partyName = :name");
        return (Party) query.setParameter("name", Party.SYS_GLOBAL_PARTY_NAME).getSingleResult();
    }

    protected void initReferenceEntityCaches()
    {
        referenceEntitiesAndCachesMap = HibernateUtil.getReferenceEntitiesAndRespectiveEnums(ejb3Configuration);
        for(final Map.Entry<Class,Class<? extends CachedReferenceEntity>> entry : referenceEntitiesAndCachesMap.entrySet())
        {
            final Class aClass = entry.getKey();
            final List resultList = entityManager.createQuery("from " + aClass.getSimpleName()).getResultList();
            initReferenceEntityList(aClass, (CachedReferenceEntity[]) entry.getValue().getEnumConstants(), resultList);
        }
    }

    protected void initCustomReferenceEntityCaches()
    {
        customReferenceEntitiesAndCachesMap = HibernateUtil.getCustomReferenceEntitiesAndRespectiveEnums(ejb3Configuration);
        for(final Map.Entry<Class,Class<? extends CachedCustomReferenceEntity>> entry : customReferenceEntitiesAndCachesMap.entrySet())
        {
            final Class aClass = entry.getKey();
            final List resultList = entityManager.createQuery("from " + aClass.getSimpleName() + " where party.id = ?").setParameter(0, Party.Cache.SYS_GLOBAL_PARTY.getEntity().getPartyId()).getResultList();
            initCustomReferenceEntityList(aClass, (CachedCustomReferenceEntity[]) entry.getValue().getEnumConstants(), resultList);
        }
    }

}
