package com.medigy.persist.model.common.attribute;

import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Embeddable(access = AccessType.PROPERTY)
public class DateAttributeValue implements Serializable
{
    private Date value;

    public DateAttributeValue(final Date value)
    {
        this.value = value;
    }

    @Basic(temporalType = TemporalType.DATE)
    @Column(nullable = false)
    public Date getValue()
    {
        return value;
    }

    public void setValue(final Date value)
    {
        this.value = value;
    }
}
