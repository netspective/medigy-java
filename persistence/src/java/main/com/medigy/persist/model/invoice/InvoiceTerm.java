package com.medigy.persist.model.invoice;

import com.medigy.persist.model.common.AbstractTopLevelEntity;
import com.medigy.persist.reference.custom.invoice.InvoiceTermType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Entity class to record various terms and conditions on invoices, such as payment terms
 */
@Entity
public class InvoiceTerm extends AbstractTopLevelEntity
{
    public static final String PK_COLUMN_NAME = "invoice_term_id";

    private Long invoiceTermId;
    private InvoiceItem invoiceItem;
    private Invoice invoice;
    private Long termValue;
    private InvoiceTermType type;

    /**
     * Some systems to record various terms and conditions on invoices, such as payment terms. Terms may
     * sometimes apply at the item level.
     */
    public InvoiceTerm()
    {
    }

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getInvoiceTermId()
    {
        return invoiceTermId;
    }

    protected void setInvoiceTermId(final Long invoiceTermId)
    {
        this.invoiceTermId = invoiceTermId;
    }


    @ManyToOne
    @JoinColumn(name = InvoiceItem.PK_COLUMN_NAME, updatable = false, insertable = false)
    public InvoiceItem getInvoiceItem()
    {
        return invoiceItem;
    }

    public void setInvoiceItem(final InvoiceItem invoiceItem)
    {
        this.invoiceItem = invoiceItem;
    }

    public Long getTermValue()
    {
        return termValue;
    }

    public void setTermValue(final Long termValue)
    {
        this.termValue = termValue;
    }

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

    @ManyToOne
    @JoinColumn(name = "invoice_term_type_id", unique = false)
    public InvoiceTermType getType()
    {
        return type;
    }

    public void setType(final InvoiceTermType type)
    {
        this.type = type;
    }
}
