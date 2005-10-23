package com.medigy.persist.model.common.attribute;

import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Attribute class for saving a STRING (Text) type attribute
 */
@Embeddable(access = AccessType.PROPERTY)
public class StringAttributeValue implements Serializable
{
    private String value;

    public StringAttributeValue(final String value)
    {
        this.value = value;
    }

    @Column(length = 64, nullable = false)
    public String getValue()
    {
        return value;
    }

    public void setValue(final String value)
    {
        this.value = value;
    }
}
