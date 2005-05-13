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

import com.medigy.persist.model.common.AbstractDateDurationEntity;
import com.medigy.persist.model.org.Organization;
import com.medigy.persist.reference.custom.insurance.InsurancePolicyType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import java.util.Set;
import java.util.HashSet;

/**
 * Class representing the agreement between the Insurance Carrier and  Insured Policy Holder.
 */
@Entity
@Table(name = "Ins_Policy")
public class InsurancePolicy extends AbstractDateDurationEntity
{
    private Long policyId;
    private String policyNumber;    // not unique across same household
    private String groupNumber;
    private String description;

    private InsurancePolicyType type;               // individual or group
    private InsurancePolicy parentPolicy;           // the policy holder's policy (null if self)
    private InsurancePlan insurancePlan;            // the plan to which this policy belongs to
    private InsurancePolicyRole insurancePolicyRole;// the relating role to the person
    private Enrollment enrollment;                  // the enrollment to which this policy belongs to (optional)

    private Set<CareProviderSelection> careProviderSelections = new HashSet<CareProviderSelection>();
    private Set<InsurancePolicy> childPolicies = new HashSet<InsurancePolicy>();

    @ManyToOne
    @JoinColumn(name = "enrollment_id")
    public Enrollment getEnrollment()
    {
        return enrollment;
    }

    public void setEnrollment(final Enrollment enrollment)
    {
        this.enrollment = enrollment;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "insurancePolicy")
    public Set<CareProviderSelection> getCareProviderSelections()
    {
        return careProviderSelections;
    }

    public void setCareProviderSelections(final Set<CareProviderSelection> careProviderSelections)
    {
        this.careProviderSelections = careProviderSelections;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parentPolicy")
    public Set<InsurancePolicy> getChildPolicies()
    {
        return childPolicies;
    }

    public void setChildPolicies(final Set<InsurancePolicy> childPolicies)
    {
        this.childPolicies = childPolicies;
    }

    @ManyToOne
    @JoinColumn(name = "parent_ins_policy_id", referencedColumnName = "ins_policy_id")
    public InsurancePolicy getParentPolicy()
    {
        return parentPolicy;
    }

    public void setParentPolicy(final InsurancePolicy parentPolicy)
    {
        this.parentPolicy = parentPolicy;
    }

    @Id(generate = GeneratorType.AUTO)
    @Column(name = "ins_policy_id")
    public Long getInsurancePolicyId()
    {
        return policyId;
    }

    protected void setInsurancePolicyId(final Long id)
    {
        this.policyId = id;
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
    @JoinColumn(name = "ins_policy_role_id")
    public InsurancePolicyRole getInsurancePolicyRole()
    {
        return insurancePolicyRole;
    }

    public void setInsurancePolicyRole(final InsurancePolicyRole role)
    {
        this.insurancePolicyRole = role;
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
        return getInsurancePlan().getInsuranceProvider();
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

}
