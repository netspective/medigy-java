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
import com.medigy.persist.model.health.HealthCareVisit;
import com.medigy.persist.model.party.Party;
import com.medigy.persist.model.claim.Claim;
import com.medigy.persist.reference.custom.invoice.InvoiceRoleType;
import com.medigy.persist.reference.custom.invoice.InvoiceType;
import com.medigy.persist.reference.custom.invoice.InvoiceStatusType;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.persistence.Column;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

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

    private BillingAccount billingAccount;
    private HealthCareVisit visit;

    private Set<InvoiceItem> items = new HashSet<InvoiceItem>();
    private Set<InvoiceRole> invoiceRoles = new HashSet<InvoiceRole>();
    private Set<InvoiceStatus> invoiceStatuses = new HashSet<InvoiceStatus>();
    private Set<InvoiceTerm> invoiceTerms = new HashSet<InvoiceTerm>();

    private Set<Claim> claims = new HashSet<Claim>();

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

    /**
     * Gets the date the invoice was submitted
     * @return
     */
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
    public Date getInvoiceDate()
    {
        return invoiceDate;
    }

    public void setInvoiceDate(final Date invoiceDate)
    {
        this.invoiceDate = invoiceDate;
    }

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

    /**
     * Gets the party that is issuing the invoice
     * @return
     */
    @Transient
    public Party getIssuingParty()
    {
        for (InvoiceRole role : invoiceRoles)
        {
            if (role.getType().equals(InvoiceRoleType.Cache.ISSUE_PARTY))
                return role.getParty();
        }
        return null;
    }

    /**
     * Gets the party the invoice is being issued for
     * @return
     */
    @Transient
    public Party getTargetParty()
    {
        for (InvoiceRole role : invoiceRoles)
        {
            if (role.getType().equals(InvoiceRoleType.Cache.TARGET_PARTY))
                return role.getParty();
        }
        return null;
    }

    /**
     * Gets the person who is creating/submitting the invoice
     * @return
     */
    @Transient
    public Party getCreateParty()
    {
        for (InvoiceRole role : invoiceRoles)
        {
            if (role.getType().equals(InvoiceRoleType.Cache.CREATE_PARTY))
                return role.getParty();
        }
        return null;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "invoice")
    public Set<InvoiceStatus> getInvoiceStatuses()
    {
        return invoiceStatuses;
    }

    public void setInvoiceStatuses(final Set<InvoiceStatus> invoiceStatuses)
    {
        this.invoiceStatuses = invoiceStatuses;
    }

    @Transient
    public void addInvoiceStatus(final InvoiceStatusType type)
    {
        final InvoiceStatus status = new InvoiceStatus();
        status.setType(type);
        status.setInvoiceStatusDate(new Date());
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
        final Set<InvoiceStatus> invoiceStatuses = getInvoiceStatuses();
        if(invoiceStatuses.size() == 0)
            return null;

        TreeSet<InvoiceStatus> inverseSorted = new TreeSet<InvoiceStatus>(Collections.reverseOrder());
        inverseSorted.addAll(invoiceStatuses);
        return inverseSorted.first();
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
    @JoinColumn(name = HealthCareVisit.PK_COLUMN_NAME)
    public HealthCareVisit getVisit()
    {
        return visit;
    }

    public void setVisit(final HealthCareVisit visit)
    {
        this.visit = visit;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "invoice")
    public Set<Claim> getClaims()
    {
        return claims;
    }

    public void setClaims(final Set<Claim> claims)
    {
        this.claims = claims;
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
}
