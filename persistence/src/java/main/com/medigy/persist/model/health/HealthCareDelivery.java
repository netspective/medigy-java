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
package com.medigy.persist.model.health;

import com.medigy.persist.model.claim.Claim;
import com.medigy.persist.model.common.AbstractDateDurationEntity;
import com.medigy.persist.reference.custom.health.DiagnosisType;
import com.medigy.persist.reference.custom.health.HealthCareDeliveryType;
import com.medigy.persist.reference.type.clincial.CPT;
import com.medigy.persist.reference.type.clincial.Icd;

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

/**
 * Class for representing the health care services that was performed during a visit. Each visit will have
 * multiple health care deliveries that was performed and each delivery will have a CPT (current Procedural Terminology)
 * code associated with it. Each delivery can also have multiple ICD codes associated with it to relate with the CPT
 * service that was performed.
 */
@Entity
public class HealthCareDelivery extends AbstractDateDurationEntity
{
    public static final String PK_COLUMN_NAME = "delivery_id";

    private Long healthCareDeliveryId;
    private String deliveryNotes;
    private HealthCareEpisode healthCareEpisode;
    private HealthCareEncounter healthCareEncounter;
    private Claim claim;
    private HealthCareDeliveryType type;

    private Set<HealthCareDeliveryBilling> billings = new HashSet<HealthCareDeliveryBilling>();
    private Set<HealthCareDeliveryRole> healthCareDeliveryRoles = new HashSet<HealthCareDeliveryRole>();
    private Set<DeliveryOutcome> outcomes = new HashSet<DeliveryOutcome>();

    private CPT cpt;
    private List<Diagnosis> diagnoses = new ArrayList<Diagnosis>();

    @ManyToOne
    @JoinColumn(name = "cpt_code", referencedColumnName = CPT.PK_COLUMN_NAME)
    public CPT getCpt()
    {
        return cpt;
    }

    public void setCpt(final CPT cpt)
    {
        this.cpt = cpt;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "healthCareDelivery")
    public List<Diagnosis> getDiagnoses()
    {
        return diagnoses;
    }

    public void setDiagnoses(final List<Diagnosis> diagnoses)
    {
        this.diagnoses = diagnoses;
    }

    @Transient
    public void addDiagnosis(final DiagnosisType type, final Icd icd)
    {
        final Diagnosis diagnosis = new Diagnosis();
        diagnosis.setDiagnosisDate(new Date());
        diagnosis.setType(type);
        diagnosis.setIcd(icd);
        diagnosis.setHealthCareDelivery(this);
        this.diagnoses.add(diagnosis);
    }

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getHealthCareDeliveryId()
    {
        return healthCareDeliveryId;
    }

    protected void setHealthCareDeliveryId(final Long healthCareDeliveryId)
    {
        this.healthCareDeliveryId = healthCareDeliveryId;
    }

    @Column(name = "delivery_notes")
    public String getDeliveryNotes()
    {
        return deliveryNotes;
    }

    public void setDeliveryNotes(final String deliveryNotes)
    {
        this.deliveryNotes = deliveryNotes;
    }

    @ManyToOne
    @JoinColumn(name = HealthCareDeliveryType.PK_COLUMN_NAME)
    public HealthCareDeliveryType getType()
    {
        return type;
    }

    public void setType(final HealthCareDeliveryType type)
    {
        this.type = type;
    }

    /**
     * Gets the claim this health care delivery is associated with
     * @return
     */
    @ManyToOne
    @JoinColumn(name = Claim.PK_COLUMN_NAME)
    public Claim getClaim()
    {
        return claim;
    }

    public void setClaim(final Claim claim)
    {
        this.claim = claim;
    }

    @ManyToOne
    @JoinColumn(name = HealthCareEpisode.PK_COLUMN_NAME)
    public HealthCareEpisode getHealthCareEpisode()
    {
        return healthCareEpisode;
    }

    public void setHealthCareEpisode(final HealthCareEpisode healthCareEpisode)
    {
        this.healthCareEpisode = healthCareEpisode;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "healthCareDelivery")
    public Set<HealthCareDeliveryRole> getHealthCareDeliveryRoles()
    {
        return healthCareDeliveryRoles;
    }

    public void setHealthCareDeliveryRoles(final Set<HealthCareDeliveryRole> healthCareDeliveryRoles)
    {
        this.healthCareDeliveryRoles = healthCareDeliveryRoles;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "healthCareDelivery")
    public Set<DeliveryOutcome> getOutcomes()
    {
        return outcomes;
    }

    public void setOutcomes(final Set<DeliveryOutcome> outcomes)
    {
        this.outcomes = outcomes;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "healthCareDelivery")
    public Set<HealthCareDeliveryBilling> getBillings()
    {
        return billings;
    }

    public void setBillings(final Set<HealthCareDeliveryBilling> billings)
    {
        this.billings = billings;
    }

    @ManyToOne
    @JoinColumn(name = HealthCareEncounter.PK_COLUMN_NAME)
    public HealthCareEncounter getHealthCareEncounter()
    {
        return healthCareEncounter;
    }

    public void setHealthCareEncounter(final HealthCareEncounter healthCareEncounter)
    {
        this.healthCareEncounter = healthCareEncounter;
    }
}
