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

import com.medigy.persist.model.common.AbstractEntity;
import com.medigy.persist.model.person.Person;
import com.medigy.persist.model.person.PersonRole;
import com.medigy.persist.reference.custom.health.HealthCareReferralType;
import com.medigy.persist.reference.custom.person.PersonRoleType;
import org.hibernate.validator.NotNull;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.Date;

@Entity
@Table(name = HealthCareReferral.TABLE_NAME)
public class HealthCareReferral extends AbstractEntity
{
    public static final String TABLE_NAME = "Health_Care_Referral";
    public static final String PK_COLUMN_NAME = "health_care_referral_id";

    public Long healthCareReferralId;
    private String referralReason;
    private String notes;
    private String authorizationNumber;
    private Date referralDate;
    private Long allowedVisits;

    private PersonRole patientRole;
    private PersonRole requesterRole;
    private PersonRole providerRole;
    private Diagnosis diagnosis;
    private HealthCareEpisode episode;
    private HealthCareReferralType type;

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getHealthCareReferralId()
    {
        return healthCareReferralId;
    }

    public void setHealthCareReferralId(final Long healthCareReferralId)
    {
        this.healthCareReferralId = healthCareReferralId;
    }

    /**
     * Gets the referral authorization number
     * @return
     */
    @Column(name = "authorization_number", length = 32)
    public String getAuthorizationNumber()
    {
        return authorizationNumber;
    }

    public void setAuthorizationNumber(final String authorizationNumber)
    {
        this.authorizationNumber = authorizationNumber;
    }

    /**
     * Gets the number of allowed visits
     * @return
     */
    @Column(name = "allowed_visits")
    public Long getAllowedVisits()
    {
        return allowedVisits;
    }

    public void setAllowedVisits(final Long allowedVisits)
    {
        this.allowedVisits = allowedVisits;
    }

    @Column(name = "referral_reason", length = 128)
    public String getReferralReason()
    {
        return referralReason;
    }

    public void setReferralReason(final String referralReason)
    {
        this.referralReason = referralReason;
    }

    public String getNotes()
    {
        return notes;
    }

    public void setNotes(final String notes)
    {
        this.notes = notes;
    }

    @Basic(temporalType = TemporalType.DATE)
    @Column(name = "referral_date")
    public Date getReferralDate()
    {
        return referralDate;
    }

    public void setReferralDate(final Date referralDate)
    {
        this.referralDate = referralDate;
    }

    /**
     * Gets the patient role object of the person
     * @return
     */
    @ManyToOne
    @JoinColumn(name="patient_role_id", referencedColumnName = PersonRole.PK_COLUMN_NAME, nullable = false)
    @NotNull
    public PersonRole getPatientRole()
    {
        return patientRole;
    }

    public void setPatientRole(final PersonRole patientRole)
    {
        this.patientRole = patientRole;
    }

    @Transient
    public void setPatient(final Person person)
    {
        PersonRole role = person.getRole(PersonRoleType.Cache.PATIENT.getEntity());
        if (role == null)
            person.addRole(PersonRoleType.Cache.PATIENT.getEntity());
        setPatientRole(role);
    }

    /**
     * Gets the role of the requesting  physician
     * @return
     */
    @ManyToOne
    @JoinColumn(name = "requester_role_id", referencedColumnName = PersonRole.PK_COLUMN_NAME, nullable = false)
    @NotNull
    public PersonRole getRequesterRole()
    {
        return requesterRole;
    }

    public void setRequesterRole(final PersonRole requesterRole)
    {
        this.requesterRole = requesterRole;
    }

    @Transient
    public void setRequestor(final Person person)
    {
        PersonRole role = person.getRole(PersonRoleType.Cache.INDIVIDUAL_HEALTH_CARE_PRACTITIONER.getEntity());
        if (role == null)
            person.addRole(PersonRoleType.Cache.INDIVIDUAL_HEALTH_CARE_PRACTITIONER.getEntity());
        setRequesterRole(role);
    }

    /**
     * Gets the role of the physician the patient is being referred to
     * @return
     */
    @ManyToOne
    @JoinColumn(name = "provider_role_id", referencedColumnName = PersonRole.PK_COLUMN_NAME, nullable = false)
    public PersonRole getProviderRole()
    {
        return providerRole;
    }

    public void setProviderRole(final PersonRole providerRole)
    {
        this.providerRole = providerRole;
    }

    @Transient
    public void setProvider(final Person person)
    {
        PersonRole role = person.getRole(PersonRoleType.Cache.INDIVIDUAL_HEALTH_CARE_PRACTITIONER.getEntity());
        if (role == null)
            person.addRole(PersonRoleType.Cache.INDIVIDUAL_HEALTH_CARE_PRACTITIONER.getEntity());
        setProviderRole(role);
    }

   /**
     * Gets the diagnosis basis for the referral
     * @return diagnosis
     */
    @ManyToOne
    @JoinColumn(name = Diagnosis.PK_COLUMN_NAME)
    public Diagnosis getDiagnosis()
    {
        return diagnosis;
    }

    public void setDiagnosis(final Diagnosis diagnosis)
    {
        this.diagnosis = diagnosis;
    }

    @ManyToOne
    @JoinColumn(name = "episode_id")
    public HealthCareEpisode getEpisode()
    {
        return episode;
    }

    public void setEpisode(final HealthCareEpisode episode)
    {
        this.episode = episode;
    }

    @ManyToOne
    @JoinColumn(name = HealthCareReferralType.PK_COLUMN_NAME, nullable = false)
    public HealthCareReferralType getType()
    {
        return type;
    }

    public void setType(final HealthCareReferralType type)
    {
        this.type = type;
    }
}
