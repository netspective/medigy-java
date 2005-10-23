package com.medigy.persist.model.invoice.attribute;

import com.medigy.persist.model.common.attribute.EntityAttribute;
import com.medigy.persist.model.invoice.Invoice;
import com.medigy.persist.reference.custom.attribute.AttributeType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Describes an attribute associated with an Invoice. This table DOES NOT contain the actual attribute value
 * itself.
 */
@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames={Invoice.PK_COLUMN_NAME, "name"})})
@Inheritance(strategy=InheritanceType.JOINED)
public class InvoiceAttribute extends EntityAttribute
{
    public static final String PK_COLUMN_NAME = "inv_attr_id";

    private Invoice invoice;
    private String name;
    private AttributeType type;

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getInvoiceAttributeId()
    {
        return getAttributeId();
    }

    protected void setInvoiceAttributeId(final Long id)
    {
        setAttributeId(id);
    }

    /**
     * Gets the invoice associated with this attribute
     * @return Invoice
     */
    @ManyToOne
    @JoinColumn(name = Invoice.PK_COLUMN_NAME, nullable = false)
    public Invoice getInvoice()
    {
        return invoice;
    }

    public void setInvoice(final Invoice invoice)
    {
        this.invoice = invoice;
    }

    /**
     * Gets the name of the attribute
     * @return attribute name
     */
    @Column(length = 128, nullable = false)
    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    /**
     * Gets the attribute type
     * @return attribute type
     */
    @ManyToOne
    @JoinColumn(name = AttributeType.PK_COLUMN_NAME, nullable = false)
    public AttributeType getType()
    {
        return type;
    }

    public void setType(final AttributeType type)
    {
        this.type = type;
    }
}
