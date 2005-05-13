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

import com.medigy.persist.model.common.AbstractTopLevelEntity;
import com.medigy.persist.model.person.Person;
import com.medigy.persist.reference.custom.insurance.InsurancePolicyRoleType;

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
 * Relationship table between a party and related insurance policies. The party can have several insurance
 * policy roles: INSURED DEPENDENT, INSURANCE  and INSURED POLICY HOLDER.
 *
 */
@Entity
@Table(name = "Ins_Policy_Role")
public class InsurancePolicyRole extends AbstractTopLevelEntity
{
    private Long insurancePolicyRoleId;
    private Person person;
    private InsurancePolicyRoleType type;
    private Set<InsurancePolicy> insurancePolicies = new HashSet<InsurancePolicy>();

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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "insurancePolicyRole")
    public Set<InsurancePolicy> getInsurancePolicies()
    {
        return insurancePolicies;
    }

    public void setInsurancePolicies(Set<InsurancePolicy> policies)
    {
        this.insurancePolicies = policies;
    }

    @ManyToOne
    @JoinColumn(name = "party_id")
    public Person getPerson()
    {
        return person;
    }

    public void setPerson(final Person person)
    {
        this.person = person;
    }

    @ManyToOne
    @JoinColumn(name = "ins_policy_role_type_id")
    public InsurancePolicyRoleType getType()
    {
        return type;
    }

    public void setType(final InsurancePolicyRoleType type)
    {
        this.type = (InsurancePolicyRoleType) type;
    }

    /**
     * Checks to see if this role represents an insured dependent
     * @return
     */
    @Transient
    public boolean isInsuredDependent()
    {
        return (getType().equals(InsurancePolicyRoleType.Cache.INSURED_DEPENDENT.getEntity()));
    }

    /**
     * Checks to see if this role represents an insured contract holder
     * @return
     */
    @Transient
    public boolean isInsuredContractHolder()
    {
        return (getType().equals(InsurancePolicyRoleType.Cache.INSURED_CONTRACT_HOLDER.getEntity()));
    }

    @Transient
    public void addInsurancePolicy(final InsurancePolicy policy)
    {
        insurancePolicies.add(policy);
    }
}
