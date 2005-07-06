/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.model.insurance;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Id;
import javax.persistence.GeneratorType;
import javax.persistence.Column;

@Entity
public class InsurancePlanCoverage extends CoverageRelationship
{
    public static final String PK_COLUMN_NAME = "ins_plan_coverage_id";

    private Long insurancePlanCoverageId;
    private InsurancePlan insurancePlan;
    private Coverage coverage;

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
    @JoinColumn(name = Coverage.PK_COLUMN_NAME)
    public Coverage getCoverage()
    {
        return coverage;
    }

    public void setCoverage(final Coverage coverage)
    {
        this.coverage = coverage;
    }
}
