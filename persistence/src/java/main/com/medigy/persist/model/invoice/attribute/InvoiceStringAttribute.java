package com.medigy.persist.model.invoice.attribute;

import com.medigy.persist.model.common.attribute.StringAttributeValue;
import com.medigy.persist.model.invoice.Invoice;
import com.medigy.persist.reference.custom.attribute.AttributeType;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * Invoice attribute for a string text
 */
@Entity
@Inheritance(strategy= InheritanceType.JOINED)
public class InvoiceStringAttribute extends InvoiceAttribute
{
    private Invoice invoice;
    private StringAttributeValue stringValue;

    public InvoiceStringAttribute()
    {
        setType(AttributeType.Cache.TEXT.getEntity());
    }

    @Embedded
    public StringAttributeValue getStringValue()
    {
        return stringValue;
    }

    public void setStringValue(final StringAttributeValue stringValue)
    {
        this.stringValue = stringValue;
    }


}
