package com.medigy.persist.model.common.attribute;

import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Embeddable(access = AccessType.PROPERTY)
public class DateTimeAttributeValue implements Serializable
{
    private Date value;

    @Basic(temporalType =  TemporalType.TIMESTAMP)
    @Column(nullable =  false)
    public Date getValue()
    {
        return value;
    }

    public void setValue(final Date value)
    {
        this.value = value;
    }
}
