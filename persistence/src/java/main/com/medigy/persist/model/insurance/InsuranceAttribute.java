/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.model.insurance;

import com.medigy.persist.model.common.AbstractTopLevelEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Attribute table used to describe more information about the insurance related entity
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class InsuranceAttribute extends AbstractTopLevelEntity
{
    public static final String PK_COLUMN_NAME = "ins_attr_id";

    private Long attributeId;
    private InsurancePolicy insurancePolicy;

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getInsuranceAttributeId()
    {
        return attributeId;
    }

    public void setInsuranceAttributeId(final Long insuranceAttributeId)
    {
        this.attributeId = insuranceAttributeId;
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
