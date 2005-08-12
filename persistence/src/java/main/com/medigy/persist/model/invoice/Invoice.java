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

import com.medigy.persist.model.claim.Claim;
import com.medigy.persist.model.common.AbstractTopLevelEntity;
import com.medigy.persist.model.health.HealthCareEncounter;
import com.medigy.persist.model.invoice.attribute.InvoiceAttribute;
import com.medigy.persist.model.invoice.attribute.InvoiceBooleanAttribute;
import com.medigy.persist.model.invoice.attribute.InvoiceDateAttribute;
import com.medigy.persist.model.invoice.attribute.InvoiceLongAttribute;
import com.medigy.persist.model.invoice.attribute.InvoiceStringAttribute;
import com.medigy.persist.model.org.Organization;
import com.medigy.persist.model.party.Party;
import com.medigy.persist.reference.custom.invoice.InvoiceRoleType;
import com.medigy.persist.reference.custom.invoice.InvoiceStatusType;
import com.medigy.persist.reference.custom.invoice.InvoiceType;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Invoice  extends AbstractTopLevelEntity
{
    public static final String PK_COLUMN_NAME = "invoice_id";

    private Long invoiceId;
    private Date invoiceDate;
    private Date submitDate;
    private String description;
    private String message;
    private String invoiceNumber;
    private InvoiceType type;
    private Invoice parentInvoice;

    private Float totalCost = new Float(0);            // trigger
    private Float totalAdjustments = new Float(0);    // trigger
    private Float balance = new Float(0);

    private BillingAccount billingAccount;
    private HealthCareEncounter encounter;

    private Set<InvoiceItem> items = new HashSet<InvoiceItem>();
    private Set<InvoiceRole> invoiceRoles = new HashSet<InvoiceRole>();
    private List<InvoiceStatus> invoiceStatuses = new ArrayList<InvoiceStatus>();
    private Set<InvoiceTerm> invoiceTerms = new HashSet<InvoiceTerm>();
    private Set<InvoiceAttribute> attributes = new HashSet<InvoiceAttribute>();

    private Set<Invoice> childInvoices = new HashSet<Invoice>();
    private List<Claim> claims = new ArrayList<Claim>();

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getInvoiceId()
    {
        return invoiceId;
    }

    protected void setInvoiceId(final Long invoiceId)
    {
        this.invoiceId = invoiceId;
    }

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    @OrderBy("claimId")
    public List<Claim> getClaims()
    {
        return claims;
    }

    public void setClaims(final List<Claim> claims)
    {
        this.claims = claims;
    }

    /**
     * Total cose minus the total asjustment
     * @return
     */
    public Float getBalance()
    {
        return balance;
    }

    public void setBalance(final Float balance)
    {
        this.balance = balance;
    }

    @ManyToOne
    @JoinColumn(name = "parent_invoice_id", referencedColumnName = PK_COLUMN_NAME)
    public Invoice getParentInvoice()
    {
        return parentInvoice;
    }

    public void setParentInvoice(final Invoice parentInvoice)
    {
        this.parentInvoice = parentInvoice;
    }

    @OneToMany(mappedBy = "parentInvoice", cascade = CascadeType.ALL)
    public Set<Invoice> getChildInvoices()
    {
        return childInvoices;
    }

    public void setChildInvoices(final Set<Invoice> childInvoices)
    {
        this.childInvoices = childInvoices;
    }

    /**
     * Gets the date the invoice was submitted
     * @return
     */
    @Basic(temporalType = TemporalType.DATE)
    public Date getSubmitDate()
    {
        return submitDate;
    }

    public void setSubmitDate(final Date submitDate)
    {
        this.submitDate = submitDate;
    }

    /**
     * Gets the invoice number specific to the organization that created it.
     * @return
     */
    @Column(length = 32)
    public String getInvoiceNumber()
    {
        return invoiceNumber;
    }

    public void setInvoiceNumber(final String invoiceNumber)
    {
        this.invoiceNumber = invoiceNumber;
    }

    /**
     * The Invoice date.  This could be different from the date it was created.
     * @return
     */
    @Basic(temporalType = TemporalType.DATE)
    public Date getInvoiceDate()
    {
        return invoiceDate;
    }

    public void setInvoiceDate(final Date invoiceDate)
    {
        this.invoiceDate = invoiceDate;
    }

    @Column(length = 256)
    public String getDescription()
    {
        return description;
    }

    public void setDescription(final String description)
    {
        this.description = description;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(final String message)
    {
        this.message = message;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "invoice")
    public Set<InvoiceItem> getItems()
    {
        return items;
    }

    public void setItems(final Set<InvoiceItem> items)
    {
        this.items = items;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "invoice")
    public Set<InvoiceRole> getInvoiceRoles()
    {
        return invoiceRoles;
    }

    public void setInvoiceRoles(final Set<InvoiceRole> invoiceRoles)
    {
        this.invoiceRoles = invoiceRoles;
    }

    @Transient
    public void addInvoiceRole(final InvoiceRole role)
    {
        role.setInvoice(this);
        invoiceRoles.add(role);
    }

    @Transient
    public void addInvoiceRole(final Party party, final InvoiceRoleType type)
    {
        final InvoiceRole newRole = new InvoiceRole();
        newRole.setType(type);
        newRole.setParty(party);
        addInvoiceRole(newRole);
    }

    @Transient
    public Party getPartyByInvoiceRole(final InvoiceRoleType type)
    {
        for (InvoiceRole role : invoiceRoles)
        {
            if (role.getType().equals(type))
                return role.getParty();
        }
        return null;
    }

    /**
     * Gets the organization which is tracking the billing
     * @return
     */
    @Transient
    public Party getBillingOrganization()
    {
        return getPartyByInvoiceRole(InvoiceRoleType.Cache.BILLING_PARTY.getEntity());
    }

    @Transient
    public void setBillingOrganization(final Organization org)
    {
        addInvoiceRole(org, InvoiceRoleType.Cache.BILLING_PARTY.getEntity());
    }

    /**
     * Gets the organization where the payment should be sent
     * @return
     */
    @Transient
    public Party getPayToOrganization()
    {
        return getPartyByInvoiceRole(InvoiceRoleType.Cache.RECEIVING_PARTY.getEntity());
    }

    @Transient
    public void setPayToOrganization(final Organization org)
    {
        addInvoiceRole(org, InvoiceRoleType.Cache.RECEIVING_PARTY.getEntity());
    }

    /**
     * Gets the organization who performed services
     * @return
     */
    @Transient
    public Party getServiceOrganization()
    {
        return getPartyByInvoiceRole(InvoiceRoleType.Cache.SERVICE_PARTY.getEntity());
    }

    @Transient
    public void setServiceOrganization(final Organization org)
    {
        addInvoiceRole(org, InvoiceRoleType.Cache.SERVICE_PARTY.getEntity());
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "invoice")
    @OrderBy("invoiceStatusDate desc")
    public List<InvoiceStatus> getInvoiceStatuses()
    {
        return invoiceStatuses;
    }

    public void setInvoiceStatuses(final List<InvoiceStatus> invoiceStatuses)
    {
        this.invoiceStatuses = invoiceStatuses;
    }

    @Transient
    public void addInvoiceStatus(final InvoiceStatusType type)
    {
        addInvoiceStatus(type, new Date());
    }

    @Transient
    public void addInvoiceStatus(final InvoiceStatusType type, final Date date)
    {
        final InvoiceStatus status = new InvoiceStatus();
        status.setType(type);
        status.setInvoiceStatusDate(date);
        addInvoiceStatus(status);
    }

    @Transient
    public void addInvoiceStatus(final InvoiceStatus status)
    {
        status.setInvoice(this);
        invoiceStatuses.add(status);
    }

    @Transient
    public InvoiceStatus getCurrentInvoiceStatus()
    {
        final List<InvoiceStatus> invoiceStatuses = getInvoiceStatuses();
        if(invoiceStatuses.size() == 0)
            return null;
        return invoiceStatuses.get(0);
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "invoice")
    public Set<InvoiceTerm> getInvoiceTerms()
    {
        return invoiceTerms;
    }

    public void setInvoiceTerms(final Set<InvoiceTerm> invoiceTerms)
    {
        this.invoiceTerms = invoiceTerms;
    }

    @ManyToOne
    @JoinColumn(name = "bill_acct_id")
    public BillingAccount getBillingAccount()
    {
        return billingAccount;
    }

    public void setBillingAccount(final BillingAccount billingAccount)
    {
        this.billingAccount = billingAccount;
    }

    @OneToOne
    @JoinColumn(name = HealthCareEncounter.PK_COLUMN_NAME)
    public HealthCareEncounter getVisit()
    {
        return encounter;
    }

    public void setVisit(final HealthCareEncounter encounter)
    {
        this.encounter = encounter;
    }

    @Transient
    public void addInvoiceItem(final InvoiceItem item)
    {
        item.setInvoice(this);
        items.add(item);
    }

    @ManyToOne
    @JoinColumn(name = InvoiceType.PK_COLUMN_NAME)
    public InvoiceType getType()
    {
        return type;
    }

    public void setType(final InvoiceType type)
    {
        this.type = type;
    }

    public Float getTotalCost()
    {
        return totalCost;
    }

    public void setTotalCost(final Float totalCost)
    {
        this.totalCost = totalCost;
        // recalculate the total
        this.balance = new Float(totalCost - totalAdjustments);
    }

    public Float getTotalAdjustments()
    {
        return totalAdjustments;
    }

    public void setTotalAdjustments(final Float totalAdjustments)
    {
        this.totalAdjustments = totalAdjustments;
        this.balance = new Float(totalCost - totalAdjustments);
    }

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    public Set<InvoiceAttribute> getAttributes()
    {
        return attributes;
    }

    public void setAttributes(final Set<InvoiceAttribute> attributes)
    {
        this.attributes = attributes;
    }

    @Transient
    public void addInvoiceAttribute(final String label, final Long id)
    {
        final InvoiceLongAttribute attr = new InvoiceLongAttribute();
        attr.setValue(id);
        attr.setInvoice(this);
        attr.setLabel(label);
        this.attributes.add(attr);
    }

    @Transient
    public void addInvoiceAttribute(final String label, final Date date)
    {
        final InvoiceDateAttribute attr = new InvoiceDateAttribute();
        attr.setValue(date);
        attr.setInvoice(this);
        attr.setLabel(label);
        this.attributes.add(attr);
    }

    @Transient
    public void addInvoiceAttribute(final String label, final String value)
    {
        final InvoiceStringAttribute attr = new InvoiceStringAttribute();
        attr.setValue(value);
        attr.setInvoice(this);
        attr.setLabel(label);
        this.attributes.add(attr);
    }

    @Transient
    public void addInvoiceAttribute(final String label, final boolean value)
    {
        final InvoiceBooleanAttribute attr = new InvoiceBooleanAttribute();
        attr.setValue(value);
        attr.setInvoice(this);
        attr.setLabel(label);
        this.attributes.add(attr);
    }
}
