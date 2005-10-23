package com.medigy.persist.model.invoice;

import com.medigy.persist.model.claim.Claim;
import com.medigy.persist.model.common.AbstractTopLevelEntity;
import com.medigy.persist.model.common.attribute.BooleanAttributeValue;
import com.medigy.persist.model.common.attribute.DateAttributeValue;
import com.medigy.persist.model.common.attribute.LongAttributeValue;
import com.medigy.persist.model.common.attribute.StringAttributeValue;
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
import com.medigy.persist.reference.type.CurrencyType;

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

/**
 * Main entity for representing a healthcare Invoice. Usually an Invoice is issued for a health care encounter or visit.
 *
 */
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
    private CurrencyType currencyType;

    private List<InvoiceItem> invoiceItems = new ArrayList<InvoiceItem>();
    private Set<InvoiceRole> invoiceRoles = new HashSet<InvoiceRole>();
    private List<InvoiceStatus> invoiceStatuses = new ArrayList<InvoiceStatus>();
    private Set<InvoiceTerm> invoiceTerms = new HashSet<InvoiceTerm>();
    private List<InvoiceAttribute> attributes = new ArrayList<InvoiceAttribute>();

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
     * Gets the total cost minus the total adjustment
     * @return the balance for the invoice
     */
    public Float getBalance()
    {
        return balance;
    }

    public void setBalance(final Float balance)
    {
        this.balance = balance;
    }

    /**
     * Gets the parent invoice associated with this invoice.
     * @return Null if there is no parent invoice
     */
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

    /**
     * Gets a list of child invoices
     * @return Child invoices
     */
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
     * @return submit date
     */
    @Basic(temporalType = TemporalType.DATE)
    @Column(name = "submit_date")
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
     * @return  invoice number
     */
    @Column(length = 32, name = "invoice_number")
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
    @Column(name = "invoice_date")
    public Date getInvoiceDate()
    {
        return invoiceDate;
    }

    public void setInvoiceDate(final Date invoiceDate)
    {
        this.invoiceDate = invoiceDate;
    }

    /**
     * Gets the description associated with the invoice.
     * @return the description text
     */
    @Column(length = 256)
    public String getDescription()
    {
        return description;
    }

    public void setDescription(final String description)
    {
        this.description = description;
    }

    /**
     * Gets the message associated with the invoice
     * @return the message text
     */
    @Column(length = 512)
    public String getMessage()
    {
        return message;
    }

    public void setMessage(final String message)
    {
        this.message = message;
    }

    /**
     * Gets the list of invoice items associated with the invoice
     * @return list of invoice items
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "invoice")
    @OrderBy("invoiceItemId")
    public List<InvoiceItem> getInvoiceItems()
    {
        return invoiceItems;
    }

    public void setInvoiceItems(final List<InvoiceItem> invoiceItems)
    {
        this.invoiceItems = invoiceItems;
    }

    /**
     * Gets an invoice item by the index
     * @param index
     * @return an invoice item
     */
    @Transient
    public InvoiceItem getInvoiceItem(final int index)
    {
        return getInvoiceItems().get(index);
    }

    /**
     * Gets the roles associated with the invoice. Each role represents a relationship between the invoice and
     * a party. This one-to-many relationship allows for a flexible association between invoice and parties.
     * @return list of invoice roles
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "invoice")
    public Set<InvoiceRole> getInvoiceRoles()
    {
        return invoiceRoles;
    }

    public void setInvoiceRoles(final Set<InvoiceRole> invoiceRoles)
    {
        this.invoiceRoles = invoiceRoles;
    }

    /**
     * Adds a new invoice role to the invoice
     * @param role
     */
    @Transient
    public void addInvoiceRole(final InvoiceRole role)
    {
        role.setInvoice(this);
        invoiceRoles.add(role);
    }

    /**
     * Adds a new invoice role to the invoice
     * @param party
     * @param type
     */
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
     * Gets the organization which is tracking the billing. This is a convience method that searches
     * the invoice roles for a type: {@link InvoiceRoleType.Cache.BILLING_PARTY}
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
     * Gets the organization where the payment should be sent. This is a convience method that searches the
     * invoice roles for a role with type {@link InvoiceRoleType.Cache.RECEIVING_PARTY}
     * @return  the pay-to organization party
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
     * Gets the organization who performed services. This is a convience method that searches the
     * invoice roles for a role with type {@link InvoiceRoleType.Cache.SERVICE_PARTY}
     * @return the service organization
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

    /**
     * Gets all the invoice statuses associated with the invoice. The statuses are sorted by date in descending order.
     * @return  list of invoice statuses
     */
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

    /**
     * Gets the current invoice status
     *
     * @return invoice status
     * @see com.medigy.persist.model.invoice.Invoice#getInvoiceStatuses()
     */
    @Transient
    public InvoiceStatus getCurrentInvoiceStatus()
    {
        final List<InvoiceStatus> invoiceStatuses = getInvoiceStatuses();
        if(invoiceStatuses.size() == 0)
            return null;
        return invoiceStatuses.get(0);
    }

    /**
     * Gets a list of terms associated with the invoice.
     * @return list of terms
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "invoice")
    public Set<InvoiceTerm> getInvoiceTerms()
    {
        return invoiceTerms;
    }

    public void setInvoiceTerms(final Set<InvoiceTerm> invoiceTerms)
    {
        this.invoiceTerms = invoiceTerms;
    }

    /**
     * Gets the billing account associated with the invoice
     * @return
     */
    @ManyToOne
    @JoinColumn(name = BillingAccount.PK_COLUMN_NAME)
    public BillingAccount getBillingAccount()
    {
        return billingAccount;
    }

    public void setBillingAccount(final BillingAccount billingAccount)
    {
        this.billingAccount = billingAccount;
    }

    /**
     * Gets the health care encounter (visit) associated with this invoice
     * @return the health care encounter
     */
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
        invoiceItems.add(item);
        totalCost = new Float(totalCost  + item.getAmount());
    }

    /**
     * Gets the type of invoice.
     * @return the invoice type
     */
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

    /**
     * Gets the total cost
     * @return the total cost
     */
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

    /**
     * Gets the total adjustments
     * @return the total adjustment
     */
    public Float getTotalAdjustments()
    {
        return totalAdjustments;
    }

    public void setTotalAdjustments(final Float totalAdjustments)
    {
        this.totalAdjustments = totalAdjustments;
        this.balance = new Float(totalCost - totalAdjustments);
    }

    /**
     * Gets the list of attributes defined for this invoice.
     * @return  list of attributes
     */
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    public List<InvoiceAttribute> getAttributes()
    {
        return attributes;
    }

    public void setAttributes(final List<InvoiceAttribute> attributes)
    {
        this.attributes = attributes;
    }

    @Transient
    public void addInvoiceAttribute(final String label, final Long id)
    {
        final InvoiceLongAttribute attr = new InvoiceLongAttribute();
        attr.setLongValue(new LongAttributeValue(id));
        attr.setInvoice(this);
        attr.setLabel(label);
        this.attributes.add(attr);
    }

    @Transient
    public void addInvoiceAttribute(final String label, final Date date)
    {
        final InvoiceDateAttribute attr = new InvoiceDateAttribute();
        attr.setDateValue(new DateAttributeValue(date));
        attr.setInvoice(this);
        attr.setLabel(label);
        this.attributes.add(attr);
    }

    @Transient
    public void addInvoiceAttribute(final String label, final String value)
    {
        final InvoiceStringAttribute attr = new InvoiceStringAttribute();
        attr.setStringValue(new StringAttributeValue(value));
        attr.setInvoice(this);
        attr.setLabel(label);
        this.attributes.add(attr);
    }

    @Transient
    public void addInvoiceAttribute(final String label, final boolean value)
    {
        final InvoiceBooleanAttribute attr = new InvoiceBooleanAttribute();
        attr.setBooleanValue(new BooleanAttributeValue(value));
        attr.setInvoice(this);
        attr.setLabel(label);
        this.attributes.add(attr);
    }

    /**
     * Gets the currency type associated with this invoice
     * @return   the currency type
     */
    @ManyToOne
    @JoinColumn(name = "currency_type_id", referencedColumnName = CurrencyType.PK_COLUMN_NAME)
    public CurrencyType getCurrencyType()
    {
        return currencyType;
    }

    public void setCurrencyType(final CurrencyType currencyType)
    {
        this.currencyType = currencyType;
    }
}
