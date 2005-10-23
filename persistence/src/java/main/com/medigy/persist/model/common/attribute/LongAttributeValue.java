package com.medigy.persist.model.common.attribute;

import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable(access = AccessType.PROPERTY)
public class LongAttributeValue implements Serializable
{
    public Long value;

    public LongAttributeValue(final Long value)
    {
        this.value = value;
    }

    @Column(nullable = false)
    public Long getValue()
    {
        return value;
    }

    public void setValue(final Long value)
    {
        this.value = value;
    }
}
