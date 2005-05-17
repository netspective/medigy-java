/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.model.insurance;

import com.medigy.persist.model.common.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class InsuranceProductCoverage  extends AbstractEntity
{
    public static final String PK_COLUMN_NAME = "ins_product_coverage_id";

    private Long insuranceProductCoverageId;
    private InsuranceProduct insuranceProduct;
    private Coverage coverage;

    @Id(generate = GeneratorType.AUTO)
    public Long getInsuranceProductCoverageId()
    {
        return insuranceProductCoverageId;
    }

    public void setInsuranceProductCoverageId(final Long insuranceProductCoverageId)
    {
        this.insuranceProductCoverageId = insuranceProductCoverageId;
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
