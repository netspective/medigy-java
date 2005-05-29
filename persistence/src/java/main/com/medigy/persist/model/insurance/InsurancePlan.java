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
import com.medigy.persist.reference.custom.insurance.CoverageLevelType;
import com.medigy.persist.reference.custom.invoice.BillRemittanceType;
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

@Entity
@Table(name = "Ins_Plan")
public class InsurancePlan extends AbstractDateDurationEntity
{
    public static final String PK_COLUMN_NAME = "ins_plan_id";

    private Long insurancePlanId;
    private InsuranceProduct insuranceProduct;
    private Organization organization;
    private String name;
    private String remittancePayerId;
    private String remittancePayerName;
    private String medigapId;
    private BillRemittanceType billRemittanceType;

    private Set<InsurancePolicy> insurancePolicies = new HashSet<InsurancePolicy>();
    private Set<InsurancePlanContactMechanism> insurancePlanContactMechanisms = new HashSet<InsurancePlanContactMechanism>();
    private Set<InsurancePlanCoverageLevel> coverageLevelRelationships = new HashSet<InsurancePlanCoverageLevel>();

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getInsurancePlanId()
    {
        return insurancePlanId;
    }

    public void setInsurancePlanId(final Long insurancePlanId)
    {
        this.insurancePlanId = insurancePlanId;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "insurancePlan")
    public Set<InsurancePlanCoverageLevel> getCoverageLevelRelationships()
    {
        return coverageLevelRelationships;
    }

    public void setCoverageLevelRelationships(final Set<InsurancePlanCoverageLevel> coverageLevelRelationships)
    {
        this.coverageLevelRelationships = coverageLevelRelationships;
    }

    @Transient
    public void addCoverageLevelRelationship(final InsurancePlanCoverageLevel rel)
    {
        this.coverageLevelRelationships.add(rel);
    }

    @Transient
    public InsurancePlanCoverageLevel getCoverageLevelRelationship(final CoverageLevelType entity)
    {
        for (InsurancePlanCoverageLevel rel : coverageLevelRelationships)
        {
            if (rel.getCoverageLevel().getType().equals(entity))
                return rel;
        }
        return null;
    }

    @ManyToOne
    @JoinColumn(name = InsuranceProduct.PK_COLUMN_NAME)
    public InsuranceProduct getInsuranceProduct()
    {
        return insuranceProduct;
    }

    public void setInsuranceProduct(final InsuranceProduct insuranceProduct)
    {
        this.insuranceProduct = insuranceProduct;
    }

    /**
     * Gets the bill remittance type.  (NEFS-SAMPLE-MEDIGY)
     * @return
     */
    @ManyToOne
    @JoinColumn(name = BillRemittanceType.PK_COLUMN_NAME)
    public BillRemittanceType getBillRemittanceType()
    {
        return billRemittanceType;
    }

    public void setBillRemittanceType(final BillRemittanceType billRemittanceType)
    {
        this.billRemittanceType = billRemittanceType;
    }

    @Column(length = 100, nullable = false)
    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    /**
     * Gets the remittance payer name. The name given to the primary remittance id record (NEFS-SAMPLE-MEDIGY)
     * @return
     */
    @Column(length = 256)
    public String getRemittancePayerName()
    {
        return remittancePayerName;
    }

    public void setRemittancePayerName(final String remittancePayerName)
    {
        this.remittancePayerName = remittancePayerName;
    }

    @Column(length = 64)
    public String getMedigapId()
    {
        return medigapId;
    }

    public void setMedigapId(final String medigapId)
    {
        this.medigapId = medigapId;
    }

    /**
     * Gets the remittance payer name. The id (if required) for this insurance plan/group for electronic remittance (NEFS-SAMPLE-MEDIGY)
     * @return
     */
    public String getRemittancePayerId()
    {
        return remittancePayerId;
    }

    public void setRemittancePayerId(final String remittancePayerId)
    {
        this.remittancePayerId = remittancePayerId;
    }

    @ManyToOne
    @JoinColumn(name = Organization.PK_COLUMN_NAME, nullable = false)
    @NotNull
    public Organization getOrganization()
    {
        return organization;
    }

    public void setOrganization(final Organization organization)
    {
        this.organization = organization;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "insurancePlan")
    public Set<InsurancePolicy> getInsurancePolicies()
    {
        return insurancePolicies;
    }

    public void setInsurancePolicies(final Set<InsurancePolicy> insurancePolicies)
    {
        this.insurancePolicies = insurancePolicies;
    }

    @OneToMany(mappedBy = "insurancePlan")
    public Set<InsurancePlanContactMechanism> getInsurancePlanContactMechanisms()
    {
        return insurancePlanContactMechanisms;
    }

    public void setInsurancePlanContactMechanisms(final Set<InsurancePlanContactMechanism> insurancePlanContactMechanisms)
    {
        this.insurancePlanContactMechanisms = insurancePlanContactMechanisms;
    }

    @Transient
    public void addInsurancePolicy(final InsurancePolicy insPolicy)
    {
        insurancePolicies.add(insPolicy);
    }
}
