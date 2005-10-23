package com.medigy.persist.model.common.attribute;

import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable(access = AccessType.PROPERTY)
public class DoubleAttributeValue implements Serializable
{
    private Double value;

    public DoubleAttributeValue(final Double value)
    {
        this.value = value;
    }

    @Column(nullable = false)
    public Double getValue()
    {
        return value;
    }

    public void setValue(final Double value)
    {
        this.value = value;
    }
}
