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

import com.medigy.persist.model.claim.Claim;
import com.medigy.persist.model.common.AbstractDateDurationEntity;
import com.medigy.persist.model.org.Organization;
import com.medigy.persist.model.person.Person;
import com.medigy.persist.reference.custom.insurance.CoverageLevelType;
import com.medigy.persist.reference.custom.insurance.InsurancePolicyType;
import com.medigy.persist.reference.custom.invoice.BillSequenceType;
import org.hibernate.validator.NotNull;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * Class representing an agreement between an Insurance Carrier and an Insured Person. The policy will
 * have two insurance policy roles associated with it: one is the insured person role and one is the insurance contract
 * holder person role. It is possible that these two roles are played by the same person.</p>
 * <p>
 * The policy will also have relationships to one or more Care Provider Selections. This is to indicate which physicians have
 * been selected as the primary care provider for what period of time. There will also be relationships to
 * one or more Financial Responsible Party Selections which indicate what person is financially responsible for the policy
 * during what time.
 * </p>
 */
@Entity
@Table(name = "Ins_Policy")
public class InsurancePolicy extends AbstractDateDurationEntity
{
    public static final String PK_COLUMN_NAME = "ins_policy_id";

    private Long policyId;
    private String policyNumber;    // not unique across same household
    private String groupNumber;
    private String description;
    private Long billSequenceId;

    private BillSequenceType billSequenceType;
    private InsurancePolicyType type;               // individual or group
    private InsurancePlan insurancePlan;            // the plan to which this policy belongs to
    private Enrollment enrollment;                  // the enrollment to which this policy belongs to (optional)

    private Set<FinancialResponsiblePartySelection> responsiblePartySelection = new HashSet<FinancialResponsiblePartySelection>();
    private Set<CareProviderSelection> careProviderSelections = new HashSet<CareProviderSelection>();
    private Set<InsurancePolicyCoverageLevel> coverageLevelRelationships = new HashSet<InsurancePolicyCoverageLevel>();
    private Set<Claim> claims = new HashSet<Claim>();

    private Person insuredPerson;
    private Person contractHolderPerson;

    @ManyToOne
    @JoinColumn(name = "insured_person_id", referencedColumnName = Person.PK_COLUMN_NAME)
    public Person getInsuredPerson()
    {
        return insuredPerson;
    }

    public void setInsuredPerson(final Person insuredPerson)
    {
        this.insuredPerson = insuredPerson;
    }

    @ManyToOne
    @JoinColumn(name = "contract_holder_person_id", referencedColumnName = Person.PK_COLUMN_NAME)
    public Person getContractHolderPerson()
    {
        return contractHolderPerson;
    }

    public void setContractHolderPerson(final Person contractHolderPerson)
    {
        this.contractHolderPerson = contractHolderPerson;
    }

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getInsurancePolicyId()
    {
       return policyId;
    }

    protected void setInsurancePolicyId(final Long id)
    {
       this.policyId = id;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "insurancePolicy")
    public Set<Claim> getClaims()
    {
        return claims;
    }

    public void setClaims(final Set<Claim> claims)
    {
        this.claims = claims;
    }

    @ManyToOne
    @JoinColumn(name = BillSequenceType.PK_COLUMN_NAME)
    public BillSequenceType getBillSequenceType()
    {
        return billSequenceType;
    }

    public void setBillSequenceType(final BillSequenceType billSequenceType)
    {
        this.billSequenceType = billSequenceType;
    }

    /**
     * Gets the bill sequence.
     * @return
     */
    public Long getBillSequenceId()
    {
        return billSequenceId;
    }

    public void setBillSequenceId(final Long billSequenceId)
    {
        this.billSequenceId = billSequenceId;
    }

    /*
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = Enrollment.PK_COLUMN_NAME)
    public Enrollment getEnrollment()
    {
        return enrollment;
    }

    public void setEnrollment(final Enrollment enrollment)
    {
        this.enrollment = enrollment;
    }
    */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "insurancePolicy")
    public Set<CareProviderSelection> getCareProviderSelections()
    {
        return careProviderSelections;
    }

    public void setCareProviderSelections(final Set<CareProviderSelection> careProviderSelections)
    {
        this.careProviderSelections = careProviderSelections;
    }

    @Column(length = 10)    
    public String getGroupNumber()
    {
        return groupNumber;
    }

    public void setGroupNumber(final String groupNumber)
    {
        this.groupNumber = groupNumber;
    }

    @Column(length = 15, nullable = false)
    @NotNull
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
    public Organization getInsuranceProvider()
    {
        return getInsurancePlan().getOrganization();
    }

    @ManyToOne
    @JoinColumn(name = "ins_plan_id")
    public InsurancePlan getInsurancePlan()
    {
        return insurancePlan;
    }

    public void setInsurancePlan(final InsurancePlan insurancePlan)
    {
        this.insurancePlan = insurancePlan;
    }


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "insurancePolicy")
    public Set<FinancialResponsiblePartySelection> getResponsiblePartySelection()
    {
        return responsiblePartySelection;
    }

    public void setResponsiblePartySelection(final Set<FinancialResponsiblePartySelection> responsiblePartySelection)
    {
        this.responsiblePartySelection = responsiblePartySelection;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "insurancePolicy")
    public Set<InsurancePolicyCoverageLevel> getCoverageLevelRelationships()
    {
        return coverageLevelRelationships;
    }

    public void setCoverageLevelRelationships(final Set<InsurancePolicyCoverageLevel> coverageLevelRelationships)
    {
        this.coverageLevelRelationships = coverageLevelRelationships;
    }

    @Transient
    public void addCoverageLevelRelationship(final InsurancePolicyCoverageLevel rel)
    {
        this.coverageLevelRelationships.add(rel);
    }

    /**
     * Gets the policy to coverage level relationship entity based on the type of the coverage level
     * @param entity
     * @return
     */
    @Transient
    public InsurancePolicyCoverageLevel getCoverageLevelRelationship(final CoverageLevelType entity)
    {
        for (InsurancePolicyCoverageLevel rel : coverageLevelRelationships)
        {
            if (rel.getCoverageLevel().getType().equals(entity))
                return rel;
        }
        return null;
    }



}
