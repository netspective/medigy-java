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
package com.medigy.service.impl.party;

import com.medigy.persist.model.party.Party;
import com.medigy.persist.model.party.ValidResponsiblePartyRole;
import com.medigy.persist.model.person.Person;
import com.medigy.persist.reference.custom.party.PartyRoleType;
import com.medigy.persist.util.HibernateUtil;
import com.medigy.service.ServiceVersion;
import com.medigy.service.dto.ServiceParameters;
import com.medigy.service.dto.person.SelectFinancialResponsiblePartyParameters;
import com.medigy.service.party.PartyRelationshipFacade;
import com.medigy.service.party.SelectFinancialResponsiblePartyService;
import com.medigy.service.util.ReferenceEntityFacade;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;

/**
 * Service class for handling request for choosing a financial responsible party for the patient
 *
 */
public class SelectFinancialResponsiblePartyServiceImpl implements SelectFinancialResponsiblePartyService
{
    private ReferenceEntityFacade referenceEntityFacade;
    private PartyRelationshipFacade partyRelationshipFacade;

    public PartyRelationshipFacade getPartyRelationshipFacade()
    {
        return partyRelationshipFacade;
    }

    public void setPartyRelationshipFacade(final PartyRelationshipFacade partyRelationshipFacade)
    {
        this.partyRelationshipFacade = partyRelationshipFacade;
    }

    public ReferenceEntityFacade getReferenceEntityFacade()
    {
        return referenceEntityFacade;
    }

    public void setReferenceEntityFacade(final ReferenceEntityFacade referenceEntityFacade)
    {
        this.referenceEntityFacade = referenceEntityFacade;
    }

    public void select(final SelectFinancialResponsiblePartyParameters params)
    {
        final Serializable respPartyId = params.getResponsiblePartyId();
        final Party respParty = (Party) HibernateUtil.getSession().load(Party.class, respPartyId);
        final String roleCode = params.getResponsiblePartyRoleCode();

        PartyRoleType roleType = null;
        if (respParty.isPerson())
            roleType = referenceEntityFacade.getPersonRoleType(roleCode);
        else
            roleType = referenceEntityFacade.getOrganizationRoleType(roleCode);
        // check to see if the respParty alread has this role
        if (!respParty.hasPartyRole(roleType))
            respParty.addPartyRole(roleType);

        // TODO: now need to create/get the patient's role with respect to the relationship
        Criteria criteria = HibernateUtil.getSession().createCriteria(ValidResponsiblePartyRole.class);
        criteria.createCriteria("responsiblePartyRoleType").add(Restrictions.eq("code", roleCode));
        final ValidResponsiblePartyRole respPartyRel = (ValidResponsiblePartyRole) criteria.uniqueResult();
        

        final Person patient = (Person) HibernateUtil.getSession().load(Person.class, params.getPartyId());
        partyRelationshipFacade.addFinancialResposiblePartyRelationship(patient, respParty, roleType);



    }

    public ServiceVersion[] getSupportedServiceVersions()
    {
        return new ServiceVersion[0];
    }

    public boolean isValid(ServiceParameters parameters)
    {
        return false;
    }
}