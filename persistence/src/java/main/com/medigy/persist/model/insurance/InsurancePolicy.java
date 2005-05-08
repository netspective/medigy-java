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
package com.medigy.persist.model.insurance;

import com.medigy.persist.model.org.Organization;
import com.medigy.persist.model.party.Agreement;
import com.medigy.persist.model.party.AgreementItem;
import com.medigy.persist.model.party.AgreementRole;
import com.medigy.persist.model.party.Party;
import com.medigy.persist.model.person.Person;
import com.medigy.persist.reference.custom.insurance.InsurancePolicyRoleType;
import com.medigy.persist.reference.custom.insurance.InsurancePolicyType;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Ins_Policy")
public class InsurancePolicy implements Agreement
{
    private Long policyId;
    private String policyNumber;
    private String description;
    private Date policyDate;
    private InsurancePolicyType type;
    private InsuranceProduct insuranceProduct;

    private Set<InsurancePolicyRole> insurancePolicyRoles = new HashSet<InsurancePolicyRole>();
    private Set<InsurancePolicyItem> insurancePolicyItems = new HashSet<InsurancePolicyItem>();

    @Id(generate = GeneratorType.AUTO)
    @Column(name = "ins_policy_id")
    public Long getAgreementId()
    {
        return policyId;
    }

    public void setAgreementId(final Long id)
    {
        this.policyId = id;
    }

    @Column(name = "policy_effective_date")
    public Date getAgreementDate()
    {
        return policyDate;
    }

    public void setAgreementDate(Date agreementDate)
    {
        this.policyDate = agreementDate;
    }

    @Transient
    public Long getPolicyId()
    {
        return getAgreementId();
    }

    protected void setPolicyId(final Long policyId)
    {
        setAgreementId(policyId);
    }

    @Column(length = 15, nullable = false)
    public String getPolicyNumber()
    {
        return policyNumber;
    }

    public void setPolicyNumber(final String policyNumber)
    {
        this.policyNumber = policyNumber;
    }

    @Column(length = 100)
    public String getDescription()
    {
        return description;
    }

    public void setDescription(final String description)
    {
        this.description = description;
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "ins_policy_id")
    public Set<InsurancePolicyRole> getAgreementRoles()
    {
        return insurancePolicyRoles;
    }

    public void setAgreementRoles(final Set<? extends AgreementRole> agreementRoles)
    {
        this.insurancePolicyRoles = new HashSet<InsurancePolicyRole>();
        for (AgreementRole role : agreementRoles)
        {
            if (role instanceof InsurancePolicyRole)
                this.insurancePolicyRoles.add((InsurancePolicyRole) role);
        }
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "ins_policy_id")
    public Set<InsurancePolicyItem> getAgreementItems()
    {
        return insurancePolicyItems;
    }

    public void setAgreementItems(final Set<? extends AgreementItem> agreementItems)
    {
        this.insurancePolicyItems = new HashSet<InsurancePolicyItem>();
        for (AgreementItem item : agreementItems)
        {
            if (item instanceof InsurancePolicyItem)
                this.insurancePolicyItems.add((InsurancePolicyItem) item);
        }
    }

    @ManyToOne
    @JoinColumn(name = "ins_policy_type_id", nullable = false)
    public InsurancePolicyType getType()
    {
        return type;
    }

    public void setType(final InsurancePolicyType type)
    {
        this.type = type;
    }

    @Transient
    public void setInsuranceProvider(final Organization providerParty)
    {
        addPartyByRole(providerParty, InsurancePolicyRoleType.Cache.INSURANCE_PROVIDER.getEntity());
    }

    @Transient
    public void setPolicyHolder(final Person individualParty)
    {
        addPartyByRole(individualParty, InsurancePolicyRoleType.Cache.INSURED_CONTRACT_HOLDER.getEntity());
    }

    @Transient
    public void addInsuredDependent(final Person dependent)
    {
        addPartyByRole(dependent, InsurancePolicyRoleType.Cache.INSURED_DEPENDENT.getEntity());
    }

    @Transient
    protected void addPartyByRole(final Party party, final InsurancePolicyRoleType roleType)
    {
        InsurancePolicyRole role = new InsurancePolicyRole();
        role.setAgreement(this);
        role.setType(roleType);
        role.setParty(party);
        party.getInsurancePolicyRoles().add(role);

        getAgreementRoles().add(role);
    }

    @Transient
    public Organization getInsuranceProvider()
    {
        return (Organization) getPartyByRole(InsurancePolicyRoleType.Cache.INSURANCE_PROVIDER.getEntity());
    }

    @Transient
    public Person getInsuredContractHolder()
    {
        return (Person) getPartyByRole(InsurancePolicyRoleType.Cache.INSURED_CONTRACT_HOLDER.getEntity());
    }

    @Transient
    protected Party getPartyByRole(final InsurancePolicyRoleType roleType)
    {
        final Object[] objects = (Object[]) getAgreementRoles().toArray();
        for (int i = 0; i < objects.length; i++)
        {
            AgreementRole role = (AgreementRole) objects[i];
            if (role.getType().equals(roleType))
            {
                return role.getParty();
            }
        }
        return null;
    }

    @Transient
    public Set<Person> getInsuredDependents()
    {
        final InsurancePolicyRoleType roleType = InsurancePolicyRoleType.Cache.INSURED_DEPENDENT.getEntity();
        final Set<Person> partyList = new HashSet<Person>();
        final Object[] objects = (Object[]) getAgreementRoles().toArray();
        for (int i = 0; i < objects.length; i++)
        {
            AgreementRole role = (AgreementRole) objects[i];
            if (role.getType().equals(roleType) && role.getParty() instanceof Person)
            {
                partyList.add((Person) role.getParty());
            }
        }
        return partyList;
    }

    @ManyToOne
    @JoinColumn(name = "product_id")        
    public InsuranceProduct getInsuranceProduct()
    {
        return insuranceProduct;
    }

    public void setInsuranceProduct(final InsuranceProduct insuranceProduct)
    {
        this.insuranceProduct = insuranceProduct;
    }
}
