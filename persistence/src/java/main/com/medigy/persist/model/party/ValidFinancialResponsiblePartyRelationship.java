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
package com.medigy.persist.model.party;

import com.medigy.persist.model.common.AbstractTopLevelEntity;
import com.medigy.persist.reference.custom.party.PartyRoleType;
import com.medigy.persist.reference.custom.person.PersonRoleType;

import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Describes the valid roles involved for a relationship between a patient and a financial responsible party.
 */
@Entity
public class ValidFinancialResponsiblePartyRelationship  extends AbstractTopLevelEntity
{
    /**
     * The population of these cached entries can only be executed after the party role type tables
     * have been populated.
     */
    /*
    public enum Cache
    {
        SELF(PersonRoleType.Cache.SELF.getEntity(), PersonRoleType.Cache.SELF.getEntity()),
        EMPLOYER(PersonRoleType.Cache.EMPLOYEE.getEntity(), OrganizationRoleType.Cache.EMPLOYER.getEntity()),
        PARENT(PersonRoleType.Cache.CHILD.getEntity(), PersonRoleType.Cache.PARENT.getEntity()),
        SPOUSE(PersonRoleType.Cache.SPOUSE.getEntity(), PersonRoleType.Cache.SPOUSE.getEntity());

        private PersonRoleType patientRoleType;
        private PartyRoleType responsiblePartyRoleType;
        private ValidFinancialResponsiblePartyRelationship entity;

        Cache(PersonRoleType patientRoleType, PartyRoleType roleType)
        {
            this.responsiblePartyRoleType = roleType;
            this.patientRoleType = patientRoleType;
        }

        public void setEntity(ValidFinancialResponsiblePartyRelationship entity)
        {
            this.entity = entity;
        }

        public void getEntity()
        {
            if (entity == null)
                throw new RuntimeException(getClass() + " " + name() + "has not been initialized.");
        }

        public PersonRoleType getPatientRoleType()
        {
            return patientRoleType;
        }

        public PartyRoleType getResponsiblePartyRoleType()
        {
            return responsiblePartyRoleType;
        }
    }
    */
    public ValidFinancialResponsiblePartyRelationship()
    {
        
    }

    public ValidFinancialResponsiblePartyRelationship(final Long responsiblePartyRoleId, final PartyRoleType role)
    {
        this.responsiblePartyRoleId = responsiblePartyRoleId;
        this.responsiblePartyRoleType = role;
    }

    private Long responsiblePartyRoleId;
    private PartyRoleType responsiblePartyRoleType;
    private PersonRoleType patientRoleType;

    @Id(generate = GeneratorType.AUTO)
    public Long getResponsiblePartyRoleId()
    {
        return responsiblePartyRoleId;
    }

    public void setResponsiblePartyRoleId(final Long responsiblePartyRoleId)
    {
        this.responsiblePartyRoleId = responsiblePartyRoleId;
    }

    /**
     * Gets the corresponding responsible party's role
     * @return
     */
    @ManyToOne
    @JoinColumn(name = "resp_party_role_type_id", referencedColumnName = PartyRoleType.PK_COLUMN_NAME, nullable = false)
    public PartyRoleType getResponsiblePartyRoleType()
    {
        return responsiblePartyRoleType;
    }

    public void setResponsiblePartyRoleType(final PartyRoleType role)
    {
        this.responsiblePartyRoleType = role;
    }

    /**
     * Gets the corresponding patient's role
     * @return
     */
    @ManyToOne
    @JoinColumn(name = "patient_role_type_id", referencedColumnName = PersonRoleType.PK_COLUMN_NAME, nullable = false)
    public PersonRoleType getPatientRoleType()
    {
        return patientRoleType;
    }

    public void setPatientRoleType(final PersonRoleType patientRoleType)
    {
        this.patientRoleType = patientRoleType;
    }
}
