package com.medigy.persist.model.insurance;

import com.medigy.persist.model.common.AbstractTopLevelEntity;
import com.medigy.persist.reference.custom.insurance.CoverageLevelType;
import com.medigy.persist.reference.custom.insurance.CoverageType;

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
 * Class for insurance coverage that can be defined at product, plan or policy level.
 */
@Entity
public class Coverage extends AbstractTopLevelEntity
{
    public static final String PK_COLUMN_NAME = "coverage_id";

    private Long coverageId;
    private CoverageType type;
    private Set<CoverageLevel> coverageLevels = new HashSet<CoverageLevel>();

    private Set<InsuranceProductCoverage> insuranceProductRelationships = new HashSet<InsuranceProductCoverage>();
    private Set<InsurancePlanCoverage> insurancePlanRelationships = new HashSet<InsurancePlanCoverage>();
    private Set<InsurancePolicyCoverage> insurancePolicyRelationships = new HashSet<InsurancePolicyCoverage>();

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getCoverageId()
    {
        return coverageId;
    }

    protected void setCoverageId(final Long coverageId)
    {
        this.coverageId = coverageId;
    }

    /**
     * Gets the type of coverage
     * @return
     */
    @ManyToOne
    @JoinColumn(name = "coverage_type_id")
    public CoverageType getType()
    {
        return type;
    }

    public void setType(final CoverageType type)
    {
        this.type = type;
    }

    /**
     * Gets the coverage levels
     * @return
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "coverage")
    public Set<CoverageLevel> getCoverageLevels()
    {
        return coverageLevels;
    }

    public void setCoverageLevels(final Set<CoverageLevel> coverageLevels)
    {
        this.coverageLevels = coverageLevels;
    }

    @Transient
    public void addCoverageLevel(final CoverageLevel level)
    {
        level.setCoverage(this);
        getCoverageLevels().add(level);
    }

    @Transient
    public CoverageLevel getCoverageLevel(final CoverageLevelType type)
    {
        for (CoverageLevel level : coverageLevels)
        {
            if (level.getType().equals(type))
                return level;
        }
        return null;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "coverage")
    public Set<InsuranceProductCoverage> getInsuranceProductRelationships()
    {
        return insuranceProductRelationships;
    }

    public void setInsuranceProductRelationships(final Set<InsuranceProductCoverage> insuranceProductRelationships)
    {
        this.insuranceProductRelationships = insuranceProductRelationships;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "coverage")
    public Set<InsurancePlanCoverage> getInsurancePlanRelationships()
    {
        return insurancePlanRelationships;
    }

    public void setInsurancePlanRelationships(final Set<InsurancePlanCoverage> insurancePlanRelationships)
    {
        this.insurancePlanRelationships = insurancePlanRelationships;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "coverage")
    public Set<InsurancePolicyCoverage> getInsurancePolicyRelationships()
    {
        return insurancePolicyRelationships;
    }

    public void setInsurancePolicyRelationships(final Set<InsurancePolicyCoverage> insurancePolicyRelationships)
    {
        this.insurancePolicyRelationships = insurancePolicyRelationships;
    }

    @Transient
    public void addInsuranceProductRelationship(final InsuranceProductCoverage rel)
    {
        this.insuranceProductRelationships.add(rel);
    }
}
