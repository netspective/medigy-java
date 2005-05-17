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
import com.medigy.persist.reference.custom.insurance.CoverageLevelType;
import com.medigy.persist.reference.custom.insurance.CoverageLevelBasisType;

import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import java.util.HashSet;
import java.util.Set;

@Entity
public class CoverageLevel extends AbstractTopLevelEntity
{
    public static final String PK_COLUMN_NAME = "coverage_level_id";

    private Long coverageLevelId;
    private CoverageLevelType type;
    private Set<EnrollmentElection> enrollmentElections = new HashSet<EnrollmentElection>();
    private Float value;
    private Float minValue;
    private Float maxValue;
    private Set<CoverageLevelBasis> coverageLevelBasises = new HashSet<CoverageLevelBasis>();
    private Coverage coverage;

    private Set<InsurancePlanCoverageLevel> insurancePlanRelationships = new HashSet<InsurancePlanCoverageLevel>();
    private Set<InsurancePolicyCoverageLevel> insurancePolicyRelationships = new HashSet<InsurancePolicyCoverageLevel>();
    private Set<InsuranceProductCoverageLevel> insuranceProductRelationships = new HashSet<InsuranceProductCoverageLevel>();

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getCoverageLevelId()
    {
        return coverageLevelId;
    }

    protected void setCoverageLevelId(final Long coverageLevelId)
    {
        this.coverageLevelId = coverageLevelId;
    }

    @ManyToOne
    @JoinColumn(name = "coverage_level_type_id")
    public CoverageLevelType getType()
    {
        return type;
    }

    public void setType(CoverageLevelType type)
    {
        this.type = type;
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "enrollment_election_id")
    public Set<EnrollmentElection> getEnrollmentElections()
    {
        return enrollmentElections;
    }

    public void setEnrollmentElections(final Set<EnrollmentElection> enrollmentElections)
    {
        this.enrollmentElections = enrollmentElections;
    }

    /**
     * Gets the coverage level value. This could be the co-pay amount, the coinsurance percentage,
     * deductible amount, etc.
     * @return  coverage value
     */
    public Float getValue()
    {
        return value;
    }

    public void setValue(final Float value)
    {
        this.value = value;
    }

    /**
     * Gets the minimum coverage value, This is for coverage level types that need to have a min and max value
     * instead of one value.
     * @return minimum coverage value
     */
    public Float getMinValue()
    {
        return minValue;
    }

    public void setMinValue(final Float minValue)
    {
        this.minValue = minValue;
    }

    /**
     * Gets the maximum coverage value. This is for coverage level types that need to have a min and max value
     * instead of one value.
     * @return  maximum coverage value
     */
    public Float getMaxValue()
    {
        return maxValue;
    }

    public void setMaxValue(final Float maxValue)
    {
        this.maxValue = maxValue;
    }

    /**
     * Gets the coevrage level basis. This is to indicate whether this coverage level value is for
     * per incident, per year, or per person.
     * @return
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "coverageLevel")    
    public Set<CoverageLevelBasis> getCoverageLevelBasises()
    {
        return coverageLevelBasises;
    }

    public void setCoverageLevelBasises(final Set<CoverageLevelBasis> coverageLevelBasis)
    {
        this.coverageLevelBasises = coverageLevelBasis;
    }

    @Transient
    public void addCoverageLevelBasis(final CoverageLevelBasis basis)
    {
        getCoverageLevelBasises().add(basis);
    }

    @Transient
    public void addCoverageLevelBasis(final CoverageLevelBasisType type)
    {
        final CoverageLevelBasis basis = new CoverageLevelBasis();
        basis.setType(type);
        basis.setCoverageLevel(this);
        getCoverageLevelBasises().add(basis);
    }

    @ManyToOne
    @JoinColumn(name = "coverage_id")
    public Coverage getCoverage()
    {
        return coverage;
    }

    public void setCoverage(final Coverage coverage)
    {
        this.coverage = coverage;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "coverageLevel")
    public Set<InsurancePlanCoverageLevel> getInsurancePlanRelationships()
    {
        return insurancePlanRelationships;
    }

    public void setInsurancePlanRelationships(final Set<InsurancePlanCoverageLevel> insurancePlanRelationships)
    {
        this.insurancePlanRelationships = insurancePlanRelationships;
    }

    @Transient
    public void addInsurancePlanCoverageLevel(final InsurancePlanCoverageLevel insPlanCoverageLevel)
    {
        this.insurancePlanRelationships.add(insPlanCoverageLevel);
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "coverageLevel")
    public Set<InsurancePolicyCoverageLevel> getInsurancePolicyRelationships()
    {
        return insurancePolicyRelationships;
    }

    public void setInsurancePolicyRelationships(final Set<InsurancePolicyCoverageLevel> insurancePolicyRelationships)
    {
        this.insurancePolicyRelationships = insurancePolicyRelationships;
    }

    @Transient
    public void addInsurancePolicyCoverageLevel(final InsurancePolicyCoverageLevel insPolicyCoverageLevel)
    {
        this.insurancePolicyRelationships.add(insPolicyCoverageLevel);
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "coverageLevel")
    public Set<InsuranceProductCoverageLevel> getInsuranceProductRelationships()
    {
        return insuranceProductRelationships;
    }

    public void setInsuranceProductRelationships(final Set<InsuranceProductCoverageLevel> insuranceProductRelationships)
    {
        this.insuranceProductRelationships = insuranceProductRelationships;
    }

    @Transient
    public void addInsuranceProductRelationship(final InsuranceProductCoverageLevel rel)
    {
        this.insuranceProductRelationships.add(rel);
    }

}
