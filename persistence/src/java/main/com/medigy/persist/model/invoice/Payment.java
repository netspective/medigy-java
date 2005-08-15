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

import com.medigy.persist.model.claim.ClaimSettlementAmount;
import com.medigy.persist.model.common.AbstractTopLevelEntity;
import com.medigy.persist.model.party.Party;
import com.medigy.persist.reference.custom.invoice.PaymentMethodType;
import com.medigy.persist.reference.type.CurrencyType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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

    public Date getEffectiveDate()
    {
        return effectiveDate;
    }

    public void setEffectiveDate(final Date effectiveDate)
    {
        this.effectiveDate = effectiveDate;
    }

    @Column(nullable = false)
    public Float getAmount()
    {
        return amount;
    }

    public void setAmount(final Float amount)
    {
        this.amount = amount;
    }

    @Column(length = 100)
    public String getNotes()
    {
        return notes;
    }

    public void setNotes(final String notes)
    {
        this.notes = notes;
    }

    public String getPaymentRefNumber()
    {
        return paymentRefNumber;
    }

    public void setPaymentRefNumber(final String paymentRefNumber)
    {
        this.paymentRefNumber = paymentRefNumber;
    }


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
    public String getPaymentMethodDescription()
    {
        return paymentMethodDescription;
    }

    public void setPaymentMethodDescription(final String paymentMethodDescription)
    {
        this.paymentMethodDescription = paymentMethodDescription;
    }

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
}
