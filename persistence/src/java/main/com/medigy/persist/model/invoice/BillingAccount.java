package com.medigy.persist.model.invoice;

import com.medigy.persist.model.common.AbstractDateDurationEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entity representing a billing account for a party
 */
@Entity
@Table(name = "Bill_Acct")
public class BillingAccount extends AbstractDateDurationEntity
{
    public static final String PK_COLUMN_NAME = "bill_acct_id";
    private Long billingAccountId;
    private String description;
    private List<Invoice> invoices = new ArrayList<Invoice>();

    //TODO: Need to add a contact mechanism that is not party related
    //private PartyContactMechanism contactMechanism;

    private Set<BillingAccountRole> billingAccountRoles = new HashSet<BillingAccountRole>();

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getBillingAccountId()
    {
        return billingAccountId;
    }

    protected void setBillingAccountId(final Long billingAccountId)
    {
        this.billingAccountId = billingAccountId;
    }

    /**
     * Gets the description of the billing account
     * @return  the description text
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
     * Gets the roles associated with the billing account. The Role represents a relationship
     * between the billing account and parties associated with it.
     * @return  list of billing account roles
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "billingAccount")
    public Set<BillingAccountRole> getBillingAccountRoles()
    {
        return billingAccountRoles;
    }

    public void setBillingAccountRoles(final Set<BillingAccountRole> billingAccountRoles)
    {
        this.billingAccountRoles = billingAccountRoles;
    }

    /**
     * Gets the invoices associated with this billing account
     * @return list of invoices
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "billingAccount")
    @OrderBy("invoiceDate")
    public List<Invoice> getInvoices()
    {
        return invoices;
    }

    public void setInvoices(final List<Invoice> invoices)
    {
        this.invoices = invoices;
    }
}
