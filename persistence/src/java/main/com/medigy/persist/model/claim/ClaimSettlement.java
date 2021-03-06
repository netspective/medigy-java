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
package com.medigy.persist.model.claim;

import com.medigy.persist.model.common.AbstractTopLevelEntity;
import com.medigy.persist.model.invoice.Payment;
import org.hibernate.exception.NestableRuntimeException;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class ClaimSettlement extends AbstractTopLevelEntity
{
    public static final String PK_COLUMN_NAME = "claim_settlement_id";

    private Long claimSettlementId;
    private Date settledDate;
    private ClaimItem claimItem;

    private List<ClaimSettlementAmount> settlementAmounts = new ArrayList<ClaimSettlementAmount>();

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getClaimSettlementId()
    {
        return claimSettlementId;
    }

    protected void setClaimSettlementId(final Long claimSettlementId)
    {
        this.claimSettlementId = claimSettlementId;
    }

    @Basic(temporalType = TemporalType.DATE)
    @Column(name = "settled_date")
    public Date getSettledDate()
    {
        return settledDate;
    }

    public void setSettledDate(final Date settledDate)
    {
        this.settledDate = settledDate;
    }

    @ManyToOne
    @JoinColumn(name = ClaimItem.PK_COLUMN_NAME, nullable = false)
    public ClaimItem getClaimItem()
    {
        return claimItem;
    }

    public void setClaimItem(final ClaimItem claimItem)
    {
        this.claimItem = claimItem;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "claimSettlement")
    @OrderBy("")
    public List<ClaimSettlementAmount> getSettlementAmounts()
    {
        return settlementAmounts;
    }

    public void setSettlementAmounts(final List<ClaimSettlementAmount> settlementAmounts)
    {
        this.settlementAmounts = settlementAmounts;
    }

    /**
     * Apply a certain amount of the payment amount towards this claim item
     * @param payment   Payment object representing one physical payment like a check or cash
     * @param value     the amount to use from the Payment towards this claim item
     */
    @Transient
    public void addSettlementAmount(final Payment payment, final Float value)
    {
        if (value > payment.getAmount())
            throw new NestableRuntimeException("A Claim settlement amount CANNOT be larger than the amount of the associated Payment entity.");
        final ClaimSettlementAmount amount = new ClaimSettlementAmount();
        amount.setPayment(payment);
        amount.setAmount(value);
        amount.setClaimSettlement(this);
        this.settlementAmounts.add(amount);
    }

    /**
     * Apply all the amount of the payment towards this claim item
     * @param payment
     */
    @Transient
    public void addSettlementAmount(final Payment payment)
    {
        final ClaimSettlementAmount amount = new ClaimSettlementAmount();
        amount.setPayment(payment);
        amount.setAmount(payment.getAmount());
        amount.setClaimSettlement(this);
        this.settlementAmounts.add(amount);
    }
}
