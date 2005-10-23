package com.medigy.persist.model.insurance;

import com.medigy.persist.model.common.AbstractTopLevelEntity;
import com.medigy.persist.reference.custom.insurance.CoverageLevelBasisType;
import com.medigy.persist.reference.custom.insurance.CoverageLevelType;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.HashSet;
import java.util.Set;

/**
 * Class for coverage levels defined for an insurance coverage. Each level describes the terms and conditions for the coverage.
 */
@Entity
public class CoverageLevel extends AbstractTopLevelEntity
{
    public static final String PK_COLUMN_NAME = "coverage_level_id";

    private Long coverageLevelId;
    private CoverageLevelType type;
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

    /**
     * Gets the coverage level type
     * @return
     */
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
    @Column(name = "min_value")
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
    @Column(name = "max_value")
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

    /**
     * Gets the coverage this level belongs to
     * @return
     */
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
        insPlanCoverageLevel.setCoverageLevel(this);
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
        insPolicyCoverageLevel.setCoverageLevel(this);
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
