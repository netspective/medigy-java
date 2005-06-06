/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.model.insurance;

import com.medigy.persist.model.common.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Transient;

/**
 * Attribute table used to describe more information about the insurance related entity
 */
public abstract class InsuranceAttribute extends AbstractEntity
{
    private Long insuranceAttributeId;
    private String name;
    private String value;

    @Transient
    public Long getInsuranceAttributeId()
    {
        return insuranceAttributeId;
    }

    public void setInsuranceAttributeId(final Long insuranceAttributeId)
    {
        this.insuranceAttributeId = insuranceAttributeId;
    }

    @Column(length = 50)
    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(final String value)
    {
        this.value = value;
    }


}
