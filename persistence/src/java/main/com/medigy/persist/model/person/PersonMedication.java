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
package com.medigy.persist.model.person;

import com.medigy.persist.model.common.AbstractTopLevelEntity;
import com.medigy.persist.model.person.Person;
import com.medigy.persist.model.product.Medication;
import com.medigy.persist.model.health.HealthCareVisit;
import com.medigy.persist.reference.type.UnitOfMeasureType;
import com.medigy.persist.reference.custom.health.MedicationDurationType;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.GeneratorType;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

import org.hibernate.validator.NotNull;

/**
 * Represents what the patient is taking
 */
@Entity
@Table(name = "Person_Medication")
public class PersonMedication extends AbstractTopLevelEntity
{
    public static final String PK_COLUMN_NAME = "per_medication_id";

    private Long personMedicationId;
    private Person patient;
    private Person prescribedBy;
    private Person approvedBy;
    private HealthCareVisit visit;

    private Medication medication;
    private String dosageForm;
    private Float dose;
    private UnitOfMeasureType doseUnits;
    private boolean allowGeneric;
    private String frequency;
    private String directions;
    private String notes;
    private Long numberOfRefills;
    private Long duration;
    private MedicationDurationType durationType;

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getPersonMedicationId()
    {
        return personMedicationId;
    }

    public void setPersonMedicationId(final Long personMedicationId)
    {
        this.personMedicationId = personMedicationId;
    }

    /**
     * Physician who approved the prescription
     * @return
     */
    @ManyToOne
    @JoinColumn(name = "approved_by_id", referencedColumnName = Person.PK_COLUMN_NAME)
    public Person getApprovedBy()
    {
        return approvedBy;
    }

    public void setApprovedBy(final Person approvedBy)
    {
        this.approvedBy = approvedBy;
    }

    /**
     * Physician who prescribed the prescription
     * @return
     */
    @ManyToOne
    @JoinColumn(name = "prescribed_by_id", referencedColumnName = Person.PK_COLUMN_NAME)
    public Person getPrescribedBy()
    {
        return prescribedBy;
    }

    public void setPrescribedBy(final Person prescribedBy)
    {
        this.prescribedBy = prescribedBy;
    }

    @ManyToOne
    @JoinColumn(name = "patient_id", referencedColumnName = Person.PK_COLUMN_NAME)
    public Person getPatient()
    {
        return patient;
    }

    public void setPatient(final Person patient)
    {
        this.patient = patient;
    }

    public HealthCareVisit getVisit()
    {
        return visit;
    }

    public void setVisit(final HealthCareVisit visit)
    {
        this.visit = visit;
    }

    /**
     * e.g. LIQUID, CREAM, TABLET, etc.
     *
     * @return
     */
    @Column(length = 10)
    public String getDosageForm()
    {
        return dosageForm;
    }

    public void setDosageForm(final String dosageForm)
    {
        this.dosageForm = dosageForm;
    }

    /**
     * free text for patient/pharmacist directions, such as "with food" etc
     *
     * @return
     */
    @Column(length = 512)
    public String getDirections()
    {
        return directions;
    }

    public void setDirections(final String directions)
    {
        this.directions = directions;
    }

    /**
     * Notes which won't be printed in a perscription
     * @return
     */
    @Column(length = 512)
    public String getNotes()
    {
        return notes;
    }

    public void setNotes(final String notes)
    {
        this.notes = notes;
    }

    /**
     * Dosage of the medication
     * @return
     */
    @NotNull
    public Float getDose()
    {
        return dose;
    }

    public void setDose(final Float dose)
    {
        this.dose = dose;
    }

    /**
     * Units the dosage is measured in
     * @return
     */
    @ManyToOne
    @JoinColumn(name = "dose_unit_id", referencedColumnName = UnitOfMeasureType.PK_COLUMN_NAME, nullable = false)
    @NotNull
    public UnitOfMeasureType getDoseUnits()
    {
        return doseUnits;
    }

    public void setDoseUnits(final UnitOfMeasureType doseUnits)
    {
        this.doseUnits = doseUnits;
    }

    /**
     * Allow the use of generic brand?
     * @return
     */
    public boolean isAllowGeneric()
    {
        return allowGeneric;
    }

    public void setAllowGeneric(final boolean allowGeneric)
    {
        this.allowGeneric = allowGeneric;
    }

    @ManyToOne
    @JoinColumn(name = Medication.PK_COLUMN_NAME, nullable = false)
    @NotNull
    public Medication getMedication()
    {
        return medication;
    }

    public void setMedication(final Medication medication)
    {
        this.medication = medication;
    }

    /**
     * How many times the medication can be refilled without another visit
     * @return
     */
    public Long getNumberOfRefills()
    {
        return numberOfRefills;
    }

    public void setNumberOfRefills(final Long numberOfRefills)
    {
        this.numberOfRefills = numberOfRefills;
    }

    public Long getDuration()
    {
        return duration;
    }

    public void setDuration(final Long duration)
    {
        this.duration = duration;
    }

    @ManyToOne
    @JoinColumn(name = "duration_units", referencedColumnName = MedicationDurationType.PK_COLUMN_NAME)
    public MedicationDurationType getDurationType()
    {
        return durationType;
    }

    public void setDurationType(final MedicationDurationType durationType)
    {
        this.durationType = durationType;
    }

    /**
     * Frequency at which the medication should be taken
     * @return
     */
    @Column(length = 32)
    public String getFrequency()
    {
        return frequency;
    }

    public void setFrequency(final String frequency)
    {
        this.frequency = frequency;
    }
}
