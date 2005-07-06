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

import com.medigy.persist.model.common.AbstractDateDurationEntity;
import com.medigy.persist.reference.custom.party.EmploymentAgreementRoleType;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.HashSet;
import java.util.Set;

@Entity()
@Table(name = "Emp_Agreement")
public class EmploymentAgreement extends AbstractDateDurationEntity implements Agreement
{
    private Long agreementId;
    private String description;

    private Set<EmploymentAgreementRole> agreementRoles = new HashSet<EmploymentAgreementRole>();
    private Set<EmploymentAgreementItem> agreementItems = new HashSet<EmploymentAgreementItem>();


    @Id(generate = GeneratorType.AUTO)
    @Column(name = "emp_agreement_id")
    public Long getAgreementId()
    {
        return agreementId;
    }

    public void setAgreementId(final Long agreementId)
    {
        this.agreementId = agreementId;
    }

    @OneToMany(targetEntity = com.medigy.persist.model.party.EmploymentAgreementRole.class)
    @JoinColumn(name = "emp_agreement_id")
    public Set<EmploymentAgreementRole> getAgreementRoles()
    {
        return this.agreementRoles;
    }

    public void setAgreementRoles(final Set<? extends AgreementRole> agreementRoles)
    {
        this.agreementRoles = new HashSet<EmploymentAgreementRole>();
        for (AgreementRole role : agreementRoles)
        {
            if (role instanceof EmploymentAgreementRole)
            {
                this.agreementRoles.add((EmploymentAgreementRole) role);
            }
        }
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "emp_agreement_id")
    public Set<EmploymentAgreementItem> getAgreementItems()
    {
        return agreementItems;
    }

    public void setAgreementItems(final Set<? extends AgreementItem> items)
    {
        this.agreementItems = new HashSet<EmploymentAgreementItem>();
        for (AgreementItem item : items)
        {
            if (item instanceof EmploymentAgreementItem)
            {
                this.agreementItems.add((EmploymentAgreementItem) item);
            }
        }
    }

    @Transient
    protected void setPartyByRole(Party party, EmploymentAgreementRoleType type)
    {
        EmploymentAgreementRole role = new EmploymentAgreementRole();
        role.setAgreement(this);
        role.setType(type);
        role.setParty(party);

        // TODO: Replace existing party with same  role if it already exists
        getAgreementRoles().add(role);
    }


    @Transient
    public void setEmployer(Party employerParty)
    {
        setPartyByRole(employerParty, EmploymentAgreementRoleType.Cache.EMPLOYER.getEntity());
    }

    @Transient
    public void setEmployee(Party employeeParty)
    {
        setPartyByRole(employeeParty, EmploymentAgreementRoleType.Cache.EMPLOYEE.getEntity());
    }

    @Transient
    public Party getPartyByRole(EmploymentAgreementRoleType type)
    {
        final Object[] objects = (Object[]) getAgreementRoles().toArray();
        for (int i = 0; i < objects.length; i++)
        {
            AgreementRole role = (AgreementRole) objects[i];
            if (role.getType().equals(type))
            {
                return role.getParty();
            }
        }
        return null;
    }

    @Transient
    public Party getEmployer()
    {
        return getPartyByRole(EmploymentAgreementRoleType.Cache.EMPLOYER.getEntity());
    }

    @Transient
    public Party getEmployee()
    {
        return getPartyByRole(EmploymentAgreementRoleType.Cache.EMPLOYEE.getEntity());
    }

}
