/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.model.insurance;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Relationship class between insurance plans (belonging to the same insurance product) and
 * the insurance coverage level.  Multiple plans can point to the same coverage level (e.g. inheriting the
 * coverage level from the product)
 */
@Entity
public class InsurancePlanCoverageLevel extends CoverageLevelRelationship
{
    public static final String  PK_COLUMN_NAME = "ins_plan_coverage_level_id";

    private Long insurancePlanCoverageId;
    private InsurancePlan insurancePlan;
    private CoverageLevel coverageLevel;

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getInsurancePlanCoverageId()
    {
        return insurancePlanCoverageId;
    }

    public void setInsurancePlanCoverageId(final Long insurancePlanCoverageId)
    {
        this.insurancePlanCoverageId = insurancePlanCoverageId;
    }

    @ManyToOne
    @JoinColumn(name = InsurancePlan.PK_COLUMN_NAME)
    public InsurancePlan getInsurancePlan()
    {
        return insurancePlan;
    }

    public void setInsurancePlan(final InsurancePlan insurancePlan)
    {
        this.insurancePlan = insurancePlan;
    }

    @ManyToOne
    @JoinColumn(name = CoverageLevel.PK_COLUMN_NAME, nullable = false)
    public CoverageLevel getCoverageLevel()
    {
        return coverageLevel;
    }

    public void setCoverageLevel(final CoverageLevel coverageLevel)
    {
        this.coverageLevel = coverageLevel;
    }
}
