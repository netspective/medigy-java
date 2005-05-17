/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.model.insurance;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Id;
import javax.persistence.GeneratorType;

@Entity
public class InsuranceProductCoverageLevel extends CoverageLevelRelationship
{
    public static final String PK_COLUMN_NAME = "ins_product_coverage_level_id";

    private Long insuranceProductCoverageLevelId;
    private InsuranceProduct insuranceProduct;

    @Id(generate = GeneratorType.AUTO)
    public Long getInsuranceProductCoverageLevelId()
    {
        return insuranceProductCoverageLevelId;
    }

    public void setInsuranceProductCoverageLevelId(final Long insuranceProductCoverageLevelId)
    {
        this.insuranceProductCoverageLevelId = insuranceProductCoverageLevelId;
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
}
