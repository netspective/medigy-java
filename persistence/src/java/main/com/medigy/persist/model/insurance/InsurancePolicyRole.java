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

import com.medigy.persist.model.party.Agreement;
import com.medigy.persist.model.party.AgreementRole;
import com.medigy.persist.model.party.Party;
import com.medigy.persist.reference.custom.insurance.InsurancePolicyRoleType;
import com.medigy.persist.reference.custom.party.AgreementRoleType;
import com.medigy.persist.reference.type.party.PartyType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.OneToMany;
import java.util.Set;
import java.util.HashSet;

/**
 * Insurance policy role is used to logically define various policy related entities such an "insured individual",
 * "insured organization", or "insurance provider".
 */
@Entity
@Table(name = "Ins_Policy_Role")
public class InsurancePolicyRole implements AgreementRole
{
    private Long insurancePolicyRoleId;
    private InsurancePolicy agreement;
    private Party party;
    protected InsurancePolicyRoleType type;

    private Set<Enrollment> enrollments = new HashSet<Enrollment>();
    private Set<CareProviderSelection> careProviderSelections = new HashSet<CareProviderSelection>();

    @Id(generate = GeneratorType.AUTO)
    @Column(name = "ins_policy_role_id")
    public Long getInsurancePolicyRoleId()
    {
        return insurancePolicyRoleId;
    }

    public void setInsurancePolicyRoleId(final Long insurancePolicyRoleId)
    {
        this.insurancePolicyRoleId = insurancePolicyRoleId;
    }

    @Transient
    public InsurancePolicy getInsurancePolicy()
    {
        return (InsurancePolicy) getAgreement();
    }

    public void setInsurancePolicy(InsurancePolicy insurancePolicy)
    {
        setInsurancePolicy(insurancePolicy);
    }

    @ManyToOne(targetEntity = "com.medigy.persist.model.insurance.InsurancePolicy")
    @JoinColumn(name = "ins_policy_id")
    public Agreement getAgreement()
    {
        return agreement;
    }

    public void setAgreement(final Agreement agreement)
    {
        this.agreement = ((InsurancePolicy) agreement);
    }

    @ManyToOne
    @JoinColumn(name = "party_id")
    public Party getParty()
    {
        return party;
    }

    public void setParty(final Party party)
    {
        this.party = party;
    }

    @ManyToOne(targetEntity = "com.medigy.persist.reference.custom.insurance.InsurancePolicyRoleType")
    @JoinColumn(name = "ins_policy_role_type_id")
    public AgreementRoleType getType()
    {
        return type;
    }

    public void setType(final AgreementRoleType type)
    {
        this.type = (InsurancePolicyRoleType) type;
    }

    /**
     * Checks to see if this role represents the insurance provider
     * @return
     */
    @Transient
    public boolean isInsuranceProvider()
    {
        return (getType().equals(InsurancePolicyRoleType.Cache.INSURANCE_PROVIDER.getEntity()) &&
                getParty().getPartyType().equals(PartyType.Cache.ORGANIZATION.getEntity()));
    }

    /**
     * Checks to see if this role represents either the contract holder or the dependent
     * @return
     */
    @Transient
    public boolean isInsuredIndividual()
    {
        return (getParty().getPartyType().equals(PartyType.Cache.PERSON.getEntity()) &&
                (getType().equals(InsurancePolicyRoleType.Cache.INSURED_CONTRACT_HOLDER.getEntity()) || getType().equals(InsurancePolicyRoleType.Cache.INSURED_DEPENDENT.getEntity())));
    }

    /**
     * Checks to see if this role represents an insured dependent
     * @return
     */
    @Transient
    public boolean isInsuredDependent()
    {
        return (getParty().getPartyType().equals(PartyType.Cache.PERSON.getEntity()) &&
                getType().equals(InsurancePolicyRoleType.Cache.INSURED_DEPENDENT.getEntity()));
    }

    /**
     * Checks to see if this role represents an insured contract holder
     * @return
     */
    @Transient
    public boolean isInsuredContractHolder()
    {
        return (getParty().getPartyType().equals(PartyType.Cache.PERSON.getEntity()) &&
                getType().equals(InsurancePolicyRoleType.Cache.INSURED_CONTRACT_HOLDER.getEntity()));
    }


    @OneToMany(mappedBy = "insuredContractHolderRole")
    public Set<Enrollment> getEnrollments()
    {
        return enrollments;
    }

    public void setEnrollments(Set<Enrollment> enrollments)
    {
        this.enrollments = enrollments;
    }

    @OneToMany(mappedBy = "insuredIndividualRole")
    public Set<CareProviderSelection> getCareProviderSelections()
    {
        return careProviderSelections;
    }

    public void setCareProviderSelections(final Set<CareProviderSelection> careProviderSelections)
    {
        this.careProviderSelections = careProviderSelections;
    }

    @Transient
    public void addCareProviderSelection(final CareProviderSelection selection)
    {
        getCareProviderSelections().add(selection);
    }
}
