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
import com.medigy.persist.model.health.HealthCareDelivery;
import com.medigy.persist.model.insurance.InsurancePolicy;
import com.medigy.persist.model.invoice.Invoice;
import com.medigy.persist.reference.custom.claim.ClaimType;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Claim extends AbstractTopLevelEntity
{
    public static final String PK_COLUMN_NAME = "claim_id";

    private Long claimId;
    private String claimBatchId;
    private Date claimSubmissionDate;
    private ClaimType type;
    private InsurancePolicy insurancePolicy;
    private Invoice  invoice;

    private List<ClaimItem> claimItems = new ArrayList<ClaimItem>();
    private Set<ClaimStatus> claimStatuses = new HashSet<ClaimStatus>();

    private Set<ClaimResubmission> resubmittedFor = new HashSet<ClaimResubmission>();
    private Set<ClaimResubmission> resubmittedWith = new HashSet<ClaimResubmission>();
    private Set<ClaimRole> claimRoles = new HashSet<ClaimRole>();
    private Set<HealthCareDelivery> healthCareDeliveries = new HashSet<HealthCareDelivery>();

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getClaimId()
    {
        return claimId;
    }

    protected void setClaimId(final Long claimId)
    {
        this.claimId = claimId;
    }

    /**
     * Gets the batch the claim is associated with
     * @return
     */
    public String getClaimBatchId()
    {
        return claimBatchId;
    }

    public void setClaimBatchId(final String claimBatchId)
    {
        this.claimBatchId = claimBatchId;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "claim")
    public Set<HealthCareDelivery> getHealthCareDeliveries()
    {
        return healthCareDeliveries;
    }

    public void setHealthCareDeliveries(final Set<HealthCareDelivery> healthCareDeliveries)
    {
        this.healthCareDeliveries = healthCareDeliveries;
    }

    @ManyToOne
    @JoinColumn(name = ClaimType.PK_COLUMN_NAME)
    public ClaimType getType()
    {
        return type;
    }

    public void setType(final ClaimType type)
    {
        this.type = type;
    }

    @ManyToOne
    @JoinColumn(name = InsurancePolicy.PK_COLUMN_NAME)
    public InsurancePolicy getInsurancePolicy()
    {
        return insurancePolicy;
    }

    public void setInsurancePolicy(final InsurancePolicy insurancePolicy)
    {
        this.insurancePolicy = insurancePolicy;
    }

    /**
     * Gets the claim submission date
     * @return
     */
    public Date getClaimSubmissionDate()
    {
        return claimSubmissionDate;
    }

    public void setClaimSubmissionDate(final Date claimSubmissionDate)
    {
        this.claimSubmissionDate = claimSubmissionDate;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "claim")
    public Set<ClaimStatus> getClaimStatuses()
    {
        return claimStatuses;
    }

    public void setClaimStatuses(final Set<ClaimStatus> claimStatuses)
    {
        this.claimStatuses = claimStatuses;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "claim")
    public List<ClaimItem> getClaimItems()
    {
        return claimItems;
    }

    public void setClaimItems(final List<ClaimItem> claimItems)
    {
        this.claimItems = claimItems;
    }

    @Transient
    public void addClaimItem(final ClaimItem item)
    {
        item.setClaim(this);
        this.claimItems.add(item);
    }


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "claimFor")
    public Set<ClaimResubmission> getResubmittedFor()
    {
        return resubmittedFor;
    }

    public void setResubmittedFor(final Set<ClaimResubmission> resubmittedFor)
    {
        this.resubmittedFor = resubmittedFor;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "claimWith")
    public Set<ClaimResubmission> getResubmittedWith()
    {
        return resubmittedWith;
    }

    public void setResubmittedWith(final Set<ClaimResubmission> resubmittedWith)
    {
        this.resubmittedWith = resubmittedWith;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "claim")
    public Set<ClaimRole> getClaimRoles()
    {
        return claimRoles;
    }

    public void setClaimRoles(final Set<ClaimRole> claimRoles)
    {
        this.claimRoles = claimRoles;
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
}
