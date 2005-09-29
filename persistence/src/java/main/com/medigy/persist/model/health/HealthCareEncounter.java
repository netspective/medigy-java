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

import com.medigy.persist.model.common.AbstractDateDurationEntity;
import com.medigy.persist.model.order.Perscription;
import com.medigy.persist.model.party.Facility;
import com.medigy.persist.model.person.Person;
import com.medigy.persist.reference.custom.health.HealthCareEncounterDiscardType;
import com.medigy.persist.reference.custom.health.HealthCareEncounterStatusType;
import com.medigy.persist.reference.custom.health.HealthCareVisitRoleType;
import com.medigy.persist.reference.custom.person.PatientType;

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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A health care visit (also known as Appointment) may have been scheduled for several visit reasons: because of a
 * health care episode or because of various symptoms.
 *
 */
@Entity
public class HealthCareEncounter  extends AbstractDateDurationEntity
{
    public static final String PK_COLUMN_NAME = "visit_id";

    private Long healthCareEncounterId;
    private Facility facility;
    private Person patient;
    private PatientType patientType;

    private Date scheduledTime;
    private Date startTime;
    private Date checkoutTime;
    private Date discardTime;

    private String discardRemarks;
    private HealthCareEncounterDiscardType  discardType;

    private List<HealthCareVisitStatus> statuses = new ArrayList<HealthCareVisitStatus>();
    private Set<HealthCareVisitRole> roles = new HashSet<HealthCareVisitRole>();    // scheduler, patient, doctor
    private Set<VisitReason> reasons = new HashSet<VisitReason>();
    private List<HealthCareDelivery> healthCareDeliveries = new ArrayList<HealthCareDelivery>();

    private List<Perscription> perscription = new ArrayList<Perscription>();

    public HealthCareEncounter()
    {
    }

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getHealthCareEncounterId()
    {
        return healthCareEncounterId;
    }

    protected void setHealthCareEncounterId(final Long healthCareEncounterId)
    {
        this.healthCareEncounterId = healthCareEncounterId;
    }

    @ManyToOne
    @JoinColumn(name = HealthCareEncounterDiscardType.PK_COLUMN_NAME)
    public HealthCareEncounterDiscardType getDiscardType()
    {
        return discardType;
    }

    public void setDiscardType(final HealthCareEncounterDiscardType discardType)
    {
        this.discardType = discardType;
    }

    @Column(length = 512, name = "discard_remarks")
    public String getDiscardRemarks()
    {
        return discardRemarks;
    }

    public void setDiscardRemarks(final String discardRemarks)
    {
        this.discardRemarks = discardRemarks;
    }

    /**
     * The date/time when the appointment was cancelled/no-showed/re-scheduled
     * @return
     */
    @Basic(temporalType = TemporalType.TIMESTAMP)
    @Column(name = "discard_time")
    public Date getDiscardTime()
    {
        return discardTime;
    }

    public void setDiscardTime(final Date discardTime)
    {
        this.discardTime = discardTime;
    }

    @Basic(temporalType = TemporalType.TIMESTAMP)
    public Date getCheckoutTime()
    {
        return checkoutTime;
    }

    public void setCheckoutTime(final Date checkoutTime)
    {
        this.checkoutTime = checkoutTime;
    }

    /**
     * Gets the start time of the visit
     * @return
     */
    @Basic(temporalType = TemporalType.TIMESTAMP)
    @Column(name = "start_time")
    public Date getStartTime()
    {
        return startTime;
    }

    public void setStartTime(final Date startTime)
    {
        this.startTime = startTime;
    }

    /**
     * Gets the time the appointment was made
     * @return
     */
    @Basic(temporalType = TemporalType.TIMESTAMP)
    @Column(name = "scheduled_time")
    public Date getScheduledTime()
    {
        return scheduledTime;
    }

    public void setScheduledTime(final Date scheduledTime)
    {
        this.scheduledTime = scheduledTime;
    }

    /**
     * Gets the facility at which the visit occurred
     * @return
     */
    @ManyToOne
    @JoinColumn(name = Facility.PK_COLUMN_NAME)
    public Facility getFacility()
    {
        return facility;
    }

    public void setFacility(final Facility facility)
    {
        this.facility = facility;
    }

    @ManyToOne
    @JoinColumn(name = Person.PK_COLUMN_NAME, nullable = false)
    public Person getPatient()
    {
        return patient;
    }

    public void setPatient(final Person patient)
    {
        this.patient = patient;
    }

    /**
     * Gets all the statuses associated with the visit. The list is sorted in descending order by date.
     * @return
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "visit")
    @OrderBy("statusDate desc")
    public List<HealthCareVisitStatus> getStatuses()
    {
        return statuses;
    }

    public void setStatuses(final List<HealthCareVisitStatus> statuses)
    {
        this.statuses = statuses;
    }

    @Transient
    public void addStatus(final HealthCareVisitStatus status)
    {
        this.statuses.add(status);
        if (status.getType().getHealthCareEncounterStatusTypeId().equals(HealthCareEncounterStatusType.Cache.DISCARD.getEntity().getHealthCareEncounterStatusTypeId()))
            setDiscardTime(new Date());
    }

    @Transient
    public void addStatus(final HealthCareEncounterStatusType type)
    {
        addStatus(type, new Date());
    }

    @Transient
    public void addStatus(final HealthCareEncounterStatusType type, final Date date)
    {
        HealthCareVisitStatus status = new HealthCareVisitStatus();
        status.setType(type);
        status.setVisit(this);
        status.setStatusDate(date);
        addStatus(status);
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "visit")
    public Set<HealthCareVisitRole> getRoles()
    {
        return roles;
    }

    public void setRoles(final Set<HealthCareVisitRole> roles)
    {
        this.roles = roles;
    }

    @Transient
    public void setRequestedPhysician(final Person person)
    {
        addRole(person, HealthCareVisitRoleType.Cache.REQ_PHYSICIAN.getEntity());
    }

    @Transient
    public void setVisitPhysician(final Person person)
    {
        addRole(person, HealthCareVisitRoleType.Cache.VISIT_PHYSICIAN.getEntity());
    }

    @Transient
    public void setAppointmentTaker(final Person person)
    {
        addRole(person, HealthCareVisitRoleType.Cache.APPT_TAKER.getEntity());
    }

    @Transient
    public void setAppointmentConfirmPerson(final Person person)
    {
        addRole(person, HealthCareVisitRoleType.Cache.APPT_CONFIRMER.getEntity());
    }

    @Transient
    protected void addRole(final Person person, final HealthCareVisitRoleType type)
    {
        final HealthCareVisitRole role = new HealthCareVisitRole();
        role.setPerson(person);
        role.setType(type);
        role.setVisit(this);
        roles.add(role);
    }

    /**
     * Gets all the reasons associated with the visit
     * @return
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "visit")
    public Set<VisitReason> getReasons()
    {
        return reasons;
    }

    public void setReasons(final Set<VisitReason> reasons)
    {
        this.reasons = reasons;
    }

    @Transient
    public void addReason(final VisitReason reason)
    {
        this.reasons.add(reason);
    }

    @Transient
    public void addReason(final String reason)
    {
        final VisitReason visitReason = new VisitReason();
        visitReason.setDescription(reason);
        visitReason.setVisit(this);
        reasons.add(visitReason);
    }

    /**
     * Gets all the halth care deliveries (services) performed during the visit
     * @return
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "healthCareEncounter")
    public List<HealthCareDelivery> getHealthCareDeliveries()
    {
        return healthCareDeliveries;
    }

    public void setHealthCareDeliveries(final List<HealthCareDelivery> healthCareDeliveries)
    {
        this.healthCareDeliveries = healthCareDeliveries;
    }

    @Transient
    public void addHealthCareDelivery(final HealthCareDelivery delivery)
    {
        delivery.setHealthCareEncounter(this);
        this.healthCareDeliveries.add(delivery);
    }

    @ManyToOne
    @JoinColumn(name = PatientType.PK_COLUMN_NAME)
    public PatientType getPatientType()
    {
        return patientType;
    }

    public void setPatientType(final PatientType patientType)
    {
        this.patientType = patientType;
    }

    /*
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "healthCareEncounter")
    public List<Perscription> getPerscription()
    {
        return perscription;
    }

    public void setPerscription(final List<Perscription> perscription)
    {
        this.perscription = perscription;
    }
    */
}
