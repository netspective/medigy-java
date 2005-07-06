/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.model.insurance;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Id;
import javax.persistence.GeneratorType;
import javax.persistence.Table;
import javax.persistence.Column;

@Entity
@Table(name = "Ins_Product_Coverage_Lvl")
public class InsuranceProductCoverageLevel extends CoverageLevelRelationship
{
    public static final String PK_COLUMN_NAME = "ins_product_coverage_lvl_id";

    private Long insuranceProductCoverageLevelId;
    private InsuranceProduct insuranceProduct;
    private CoverageLevel coverageLevel;

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
