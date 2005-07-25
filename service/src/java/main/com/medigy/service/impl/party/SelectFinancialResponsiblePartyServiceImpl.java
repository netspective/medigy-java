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

import com.medigy.persist.model.org.Organization;
import com.medigy.persist.model.person.Person;
import com.medigy.persist.reference.custom.party.OrganizationRoleType;
import com.medigy.persist.reference.custom.person.PersonRoleType;
import com.medigy.service.AbstractService;
import com.medigy.service.ServiceVersion;
import com.medigy.service.dto.ServiceParameters;
import com.medigy.service.dto.ServiceReturnValues;
import com.medigy.service.dto.person.SelectFinancialResponsiblePartyParameters;
import com.medigy.service.party.PartyRelationshipFacade;
import com.medigy.service.party.SelectFinancialResponsiblePartyService;
import com.medigy.service.util.ReferenceEntityFacade;
import org.hibernate.SessionFactory;

import java.io.Serializable;

/**
 * Service class for handling request for choosing a financial responsible party for the patient
 *
 */
public class SelectFinancialResponsiblePartyServiceImpl extends AbstractService implements SelectFinancialResponsiblePartyService
{
    private ReferenceEntityFacade referenceEntityFacade;
    private PartyRelationshipFacade partyRelationshipFacade;

    public SelectFinancialResponsiblePartyServiceImpl(final SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }

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

    public ServiceReturnValues createErrorResponse(final ServiceParameters params, final String errorMessage)
    {
        return null;
    }

    public void select(final SelectFinancialResponsiblePartyParameters params)
    {
        final Serializable respPartyId = params.getResponsiblePartyId();
        final String roleCode = params.getResponsiblePartyRoleCode();
        final Person patient = (Person) getSession().load(Person.class, params.getPartyId());

        if (params.isResponsiblePartyOrganization())
        {
            final Organization respParty = (Organization) getSession().load(Organization.class, respPartyId);
            OrganizationRoleType roleType = referenceEntityFacade.getOrganizationRoleType(roleCode);
            if (!respParty.hasRole(roleType))
                respParty.addRole(roleType);
        }
        else
        {
            final Person respParty = (Person) getSession().load(Person.class, respPartyId);
            PersonRoleType roleType = referenceEntityFacade.getPersonRoleType(roleCode);
            if (!respParty.hasRole(roleType))
                respParty.addRole(roleType);
            partyRelationshipFacade.addFinancialResposiblePersonRelationship(patient, respParty, roleType);            
        }

    }

    public ServiceVersion[] getSupportedServiceVersions()
    {
        return new ServiceVersion[0];
    }

    public String[] isValid(ServiceParameters parameters)
    {
        return null;
    }
}
