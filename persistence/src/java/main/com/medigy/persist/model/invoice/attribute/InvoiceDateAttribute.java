package com.medigy.persist.model.invoice.attribute;

import com.medigy.persist.model.common.attribute.DateAttributeValue;
import com.medigy.persist.reference.custom.attribute.AttributeType;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * Invoice attribute for date values
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class InvoiceDateAttribute extends InvoiceAttribute
{
    private DateAttributeValue dateValue;

    public InvoiceDateAttribute()
    {
        setType(AttributeType.Cache.DATE.getEntity());
    }

    @Embedded
    public DateAttributeValue getDateValue()
    {
        return dateValue;
    }

    public void setDateValue(final DateAttributeValue dateValue)
    {
        this.dateValue = dateValue;
    }
}
