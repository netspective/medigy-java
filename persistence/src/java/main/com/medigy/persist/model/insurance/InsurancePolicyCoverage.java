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

@Entity
public class InsurancePolicyCoverage extends CoverageRelationship
{
    public static final String  PK_COLUMN_NAME = "ins_policy_coverage_id";

    private Long insurancePolicyCoverageId;
    private InsurancePolicy insurancePolicy;

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getInsurancePolicyCoverageId()
    {
        return insurancePolicyCoverageId;
    }

    public void setInsurancePolicyCoverageId(final Long insurancePolicyCoverageId)
    {
        this.insurancePolicyCoverageId = insurancePolicyCoverageId;
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
