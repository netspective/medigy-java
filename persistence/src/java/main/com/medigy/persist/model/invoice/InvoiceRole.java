package com.medigy.persist.model.invoice;

import com.medigy.persist.model.common.AbstractTopLevelEntity;
import com.medigy.persist.model.party.Party;
import com.medigy.persist.reference.custom.invoice.InvoiceRoleType;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import java.util.Date;

/**
 * Relationship entity class for relating an Invoice with parties
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"invoice_id", "party_id", "invoice_role_type_id"})})
public class InvoiceRole extends AbstractTopLevelEntity
{
    public static final String PK_COLUMN_NAME = "invoice_role_id";

    private Long invoiceRoleId;
    private Date invoiceDate;
    private Invoice invoice;
    private Party party;
    private InvoiceRoleType type;

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getInvoiceRoleId()
    {
        return invoiceRoleId;
    }

    protected void setInvoiceRoleId(final Long invoiceRoleId)
    {
        this.invoiceRoleId = invoiceRoleId;
    }

    @ManyToOne
    @JoinColumn(name = "invoice_role_type_id")
    public InvoiceRoleType getType()
    {
        return type;
    }

    public void setType(final InvoiceRoleType type)
    {
        this.type = type;
    }

    /**
     * Gets the party associated with the invoice
     * @return
     */
    @ManyToOne
    @JoinColumn(name = Party.PK_COLUMN_NAME, nullable = false)
    public Party getParty()
    {
        return party;
    }

    public void setParty(final Party party)
    {
        this.party = party;
    }

    /**
     * Gets the invoice associated with this role
     * @return
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

    @Basic(temporalType =  TemporalType.DATE)
    public Date getInvoiceDate()
    {
        return invoiceDate;
    }

    public void setInvoiceDate(final Date invoiceDate)
    {
        this.invoiceDate = invoiceDate;
    }
}
