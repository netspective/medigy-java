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
import javax.persistence.Table;

@Entity
@Table(name = "Ins_Policy_Coverage_Lvl")
public class InsurancePolicyCoverageLevel  extends CoverageLevelRelationship
{
    public static final String PK_COLUMN_NAME = "ins_policy_coverage_lvl_id";

    private Long insuranceProductCoverageLevelId;
    private InsurancePolicy insurancePolicy;

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getInsuranceProductCoverageLevelId()
    {
        return insuranceProductCoverageLevelId;
    }

    public void setInsuranceProductCoverageLevelId(final Long insuranceProductCoverageLevelId)
    {
        this.insuranceProductCoverageLevelId = insuranceProductCoverageLevelId;
    }

    @ManyToOne
    @JoinColumn(name = InsurancePolicy.PK_COLUMN_NAME)
    public InsurancePolicy getInsurancePolicy()
    {
        return insurancePolicy;
    }

    public void setInsurancePolicy(final InsurancePolicy insurancePolicy)
    {
        this.insurancePolicy = insurancePolicy;
    }

}
