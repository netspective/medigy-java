package com.medigy.persist.model.person;

import com.medigy.persist.model.common.AbstractEntity;
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
import java.util.Date;

@Entity
@Table(name = "Patient_Medication_Refill")
public class PatientMedicationRefill extends AbstractEntity
{
    public static final String PK_COLUMN_NAME = "refill_id";
    private Long refillId;
    private PatientMedication patientMedication;
    private Date refillDate;

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getRefillId()
    {
        return refillId;
    }

    public void setRefillId(final Long refillId)
    {
        this.refillId = refillId;
    }

    @ManyToOne
    @JoinColumn(name = PatientMedication.PK_COLUMN_NAME)
    public PatientMedication getPatientMedication()
    {
        return patientMedication;
    }

    public void setPatientMedication(final PatientMedication patientMedication)
    {
        this.patientMedication = patientMedication;
    }

    @Basic(temporalType = TemporalType.DATE)
    @Column(nullable = false)
    @NotNull
    public Date getRefillDate()
    {
        return refillDate;
    }

    public void setRefillDate(final Date refillDate)
    {
        this.refillDate = refillDate;
    }
}
