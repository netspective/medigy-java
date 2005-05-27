/*
 * Copyright (c) 2000-2003 Netspective Communications LLC. All rights reserved.
 *
 * Netspective Communications LLC ("Netspective") permits redistribution, modification and use of this file in source
 * and binary form ("The Software") under the Netspective Source License ("NSL" or "The License"). The following
 * conditions are provided as a summary of the NSL but the NSL remains the canonical license and must be accepted
 * before using The Software. Any use of The Software indicates agreement with the NSL.
 *
 * 1. Each copy or derived work of The Software must preserve the copyright notice and this notice unmodified.
 *
 * 2. Redistribution of The Software is allowed in object code form only (as Java .class files or a .jar file
 *    containing the .class files) and only as part of an application that uses The Software as part of its primary
 *    functionality. No distribution of the package is allowed as part of a software development kit, other library,
 *    or development tool without written consent of Netspective. Any modified form of The Software is bound by these
 *    same restrictions.
 *
 * 3. Redistributions of The Software in any form must include an unmodified copy of The License, normally in a plain
 *    ASCII text file unless otherwise agreed to, in writing, by Netspective.
 *
 * 4. The names "Netspective", "Axiom", "Commons", "Junxion", and "Sparx" are trademarks of Netspective and may not be
 *    used to endorse products derived from The Software without without written consent of Netspective. "Netspective",
 *    "Axiom", "Commons", "Junxion", and "Sparx" may not appear in the names of products derived from The Software
 *    without written consent of Netspective.
 *
 * 5. Please attribute functionality where possible. We suggest using the "powered by Netspective" button or creating
 *    a "powered by Netspective(tm)" link to http://www.netspective.com for each application using The Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT,
 * ARE HEREBY DISCLAIMED.
 *
 * NETSPECTIVE AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE OR ANY THIRD PARTY AS A
 * RESULT OF USING OR DISTRIBUTING THE SOFTWARE. IN NO EVENT WILL NETSPECTIVE OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THE SOFTWARE, EVEN
 * IF HE HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 */
package com.medigy.persist.model.invoice;

import com.medigy.persist.model.common.AbstractTopLevelEntity;
import com.medigy.persist.reference.custom.invoice.InvoiceItemType;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"invoice_id", "invoice_item_seq_id"})})
public class InvoiceItem extends AbstractTopLevelEntity
{
    public static final String PK_COLUMN_NAME = "invoice_item_id";

    private Long invoiceItemId;
    private Long invoiceItemSeqId;
    private Boolean taxableFlag;
    private String itemDescription;
    private Long quantity;
    private Float unitPrice;
    private Float amount;

    private InvoiceItemType type;
    private Invoice invoice;
    private Set<InvoiceTerm> invoiceTerms = new HashSet<InvoiceTerm>();

    private InvoiceItem parentInvoiceItem;

    public InvoiceItem()
    {
    }

    @Id(generate = GeneratorType.AUTO)
    public Long getInvoiceItemId()
    {
        return invoiceItemId;
    }

    protected void setInvoiceItemId(final Long invoiceItemId)
    {
        this.invoiceItemId = invoiceItemId;
    }

    public Long getInvoiceItemSeqId()
    {
        return invoiceItemSeqId;
    }

    public void setInvoiceItemSeqId(final Long invoiceItemSeqId)
    {
        this.invoiceItemSeqId = invoiceItemSeqId;
    }

    public Boolean getTaxableFlag()
    {
        return taxableFlag;
    }

    public void setTaxableFlag(final Boolean taxableFlag)
    {
        this.taxableFlag = taxableFlag;
    }

    /**
     * Because each invoice item may be for a insuranceProduct, insuranceProduct feature, work effort, or time entry or because it may be
     * described via an ITEM DESCRIPTION for non-standard items, the relationships to PRODUCT and PRODUCT FEATURE are
     * both optional.
     * @return
     */
    public String getItemDescription()
    {
        return itemDescription;
    }

    public void setItemDescription(final String itemDescription)
    {
        this.itemDescription = itemDescription;
    }

    public Long getQuantity()
    {
        return quantity;
    }

    public void setQuantity(final Long quantity)
    {
        this.quantity = quantity;
    }

    public Float getUnitPrice()
    {
        return unitPrice;
    }

    public void setUnitPrice(final Float unitPrice)
    {
        this.unitPrice = unitPrice;
    }

    @ManyToOne
    @JoinColumn(name = "invoice_item_type_id")
    public InvoiceItemType getType()
    {
        return type;
    }

    public void setType(InvoiceItemType type)
    {
        this.type = type;
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
    @JoinColumn(name = "parent_invoice_item_id", referencedColumnName = PK_COLUMN_NAME)
    public InvoiceItem getRelatedInvoiceItems()
    {
        return parentInvoiceItem;
    }

    public void setRelatedInvoiceItems(final InvoiceItem relatedInvoiceItem)
    {
        this.parentInvoiceItem = relatedInvoiceItem;
    }

    public Float getAmount()
    {
        return amount;
    }

    public void setAmount(final Float amount)
    {
        this.amount = amount;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "invoiceItem")
    public Set<InvoiceTerm> getInvoiceTerms()
    {
        return invoiceTerms;
    }

    public void setInvoiceTerms(final Set<InvoiceTerm> invoiceTerm)
    {
        this.invoiceTerms = invoiceTerm;
    }
}
