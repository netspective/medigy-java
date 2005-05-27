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
import com.medigy.persist.reference.custom.invoice.PaymentType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Payment extends AbstractTopLevelEntity
{
    private Long paymentId;
    private Date effectiveDate;
    private Float amount;
    private String comment;
    private String paymentRefNumber; // e.g paychecks, e-transfer ID

    private Party toParty;
    private Party fromParty;
    private PaymentType type;
    private PaymentMethodType paymentMethodType;
    private String paymentMethodDescription;

    private Set<ClaimSettlementAmount> settlementAmounts = new HashSet<ClaimSettlementAmount>();

    public Payment()
    {
    }

    @Id(generate = GeneratorType.AUTO)
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
    public String getComment()
    {
        return comment;
    }

    public void setComment(final String comment)
    {
        this.comment = comment;
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
    @JoinColumn(name = "to_party_id", referencedColumnName = "party_id", nullable = false)
    public Party getToParty()
    {
        return toParty;
    }

    public void setToParty(final Party toParty)
    {
        this.toParty = toParty;
    }

    @ManyToOne
    @JoinColumn(name = "from_party_id", referencedColumnName = "party_id", nullable = false)
    public Party getFromParty()
    {
        return fromParty;
    }

    public void setFromParty(final Party fromParty)
    {
        this.fromParty = fromParty;
    }
    
    @ManyToOne
    @JoinColumn(name = "payment_type_id", nullable = false)
    public PaymentType getType()
    {
        return type;
    }

    public void setType(final PaymentType type)
    {
        this.type = type;
    }

    @ManyToOne
    @JoinColumn(name = "payment_method_type_id")
    public PaymentMethodType getPaymentMethodType()
    {
        return paymentMethodType;
    }

    public void setPaymentMethodType(final PaymentMethodType paymentMethodType)
    {
        this.paymentMethodType = paymentMethodType;
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
}
