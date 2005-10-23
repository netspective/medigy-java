package com.medigy.persist.model.invoice;

import com.medigy.persist.model.claim.ClaimSettlementAmount;
import com.medigy.persist.model.common.AbstractTopLevelEntity;
import com.medigy.persist.model.party.Party;
import com.medigy.persist.reference.custom.invoice.PaymentMethodType;
import com.medigy.persist.reference.custom.invoice.PaymentType;
import com.medigy.persist.reference.type.CurrencyType;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity class representing a payment made by a party.
 */
@Entity
public class Payment extends AbstractTopLevelEntity
{
    public static final String PK_COLUMN_NAME = "payment_id";

    private Long paymentId;
    private Date effectiveDate;
    private Float amount;
    private String notes;
    private String paymentRefNumber; // e.g paychecks, e-transfer ID

    private Party payee;
    private Party payer;
    private PaymentType type;
    private PaymentMethodType paymentMethodType;
    private String paymentMethodDescription;
    private CurrencyType currencyType;

    private Set<ClaimSettlementAmount> settlementAmounts = new HashSet<ClaimSettlementAmount>();

    public Payment()
    {
    }

    @Id(generate = GeneratorType.AUTO)
    @Column(name = Payment.PK_COLUMN_NAME)
    public Long getPaymentId()
    {
        return paymentId;
    }

    protected void setPaymentId(final Long paymentId)
    {
        this.paymentId = paymentId;
    }

    /**
     * Gets the payment effective date
     * @return payment effective date
     */
    @Basic(temporalType = TemporalType.DATE)
    public Date getEffectiveDate()
    {
        return effectiveDate;
    }

    public void setEffectiveDate(final Date effectiveDate)
    {
        this.effectiveDate = effectiveDate;
    }

    /**
     * Gets the payment amount value
     * @return amount
     */
    @Column(nullable = false)
    public Float getAmount()
    {
        return amount;
    }

    public void setAmount(final Float amount)
    {
        this.amount = amount;
    }

    /**
     * Gets the notes associated with the payment
     * @return
     */
    @Column(length = 100)
    public String getNotes()
    {
        return notes;
    }

    public void setNotes(final String notes)
    {
        this.notes = notes;
    }

    /**
     * Gets the reference number associated with the payment. This is a free form text relative to the payment
     * method type (e.g paychecks, e-transfer ID).
     * @return payment reference number
     */
    @Column(name = "payment_ref_number")
    public String getPaymentRefNumber()
    {
        return paymentRefNumber;
    }

    public void setPaymentRefNumber(final String paymentRefNumber)
    {
        this.paymentRefNumber = paymentRefNumber;
    }


    /**
     * Gets the payee party
     * @return payee
     */
    @ManyToOne
    @JoinColumn(name = "payee_party_id", referencedColumnName = Party.PK_COLUMN_NAME, nullable = false)
    public Party getPayee()
    {
        return payee;
    }

    public void setPayee(final Party payee)
    {
        this.payee = payee;
    }

    /**
     * Gets the payer party
     * @return payor
     */
    @ManyToOne
    @JoinColumn(name = "payer_party_id", referencedColumnName = Party.PK_COLUMN_NAME, nullable = false)
    public Party getPayer()
    {
        return payer;
    }

    public void setPayer(final Party payer)
    {
        this.payer = payer;
    }

    /**
     * Gets the payment method type
     * @return payment method type
     */
    @ManyToOne
    @JoinColumn(name = PaymentMethodType.PK_COLUMN_NAME, nullable = false)
    public PaymentMethodType getPaymentMethodType()
    {
        return paymentMethodType;
    }

    public void setPaymentMethodType(final PaymentMethodType paymentMethodType)
    {
        this.paymentMethodType = paymentMethodType;
    }

    @Transient
    public void setPersonalCheckPayment(final Float amount, final String checkNumber)
    {
        setPaymentMethodType(PaymentMethodType.Cache.PERSONAL_CHECK.getEntity());
        setPaymentRefNumber(checkNumber);
        setAmount(amount);
    }

    @Transient
    public void setCreditCardPayment(final Float amount)
    {
        setPaymentMethodType(PaymentMethodType.Cache.CREDIT_CARD.getEntity());
        setAmount(amount);
    }

    @Transient
    public void setCashPayment(final Float amount)
    {
        setPaymentMethodType(PaymentMethodType.Cache.CASH.getEntity());
        setAmount(amount);
    }

    @OneToMany(mappedBy = "payment")
    public Set<ClaimSettlementAmount> getSettlementAmounts()
    {
        return settlementAmounts;
    }

    public void setSettlementAmounts(final Set<ClaimSettlementAmount> settlementAmounts)
    {
        this.settlementAmounts = settlementAmounts;
    }

    /**
     * Gets the payment method description. Used when the payment method type is OTHER.
     * @return  payment method description
     */
    @Column(name = "payment_method_description")
    public String getPaymentMethodDescription()
    {
        return paymentMethodDescription;
    }

    public void setPaymentMethodDescription(final String paymentMethodDescription)
    {
        this.paymentMethodDescription = paymentMethodDescription;
    }

    /**
     * Gets the currency type the payment was made in
     * @return currency type
     */
    @ManyToOne
    @JoinColumn(name = "curr_type_id", referencedColumnName = CurrencyType.PK_COLUMN_NAME)
    public CurrencyType getCurrencyType()
    {
        return currencyType;
    }

    public void setCurrencyType(final CurrencyType currencyType)
    {
        this.currencyType = currencyType;
    }

    @ManyToOne
    @JoinColumn(name = PaymentType.PK_COLUMN_NAME)
    public PaymentType getType()
    {
        return type;
    }

    public void setType(final PaymentType type)
    {
        this.type = type;
    }
}
