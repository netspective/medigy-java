package com.medigy.persist.model.invoice.attribute;

import com.medigy.persist.model.common.attribute.LongAttributeValue;
import com.medigy.persist.reference.custom.attribute.AttributeType;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * Invoice attribute for a long number
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class InvoiceLongAttribute extends InvoiceAttribute
{
    private LongAttributeValue longValue;

    public InvoiceLongAttribute()
    {
        setType(AttributeType.Cache.INTEGER.getEntity());
    }

    @Embedded
    public LongAttributeValue getLongValue()
    {
        return longValue;
    }

    public void setLongValue(final LongAttributeValue longValue)
    {
        this.longValue = longValue;
    }
}
