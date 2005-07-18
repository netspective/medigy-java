/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.model.health.lab;

import com.medigy.persist.model.common.AbstractTopLevelEntity;
import com.medigy.persist.model.person.Person;
import com.medigy.persist.reference.type.clincial.Icd;
import com.medigy.persist.reference.custom.health.LabOrderStatusType;
import com.medigy.persist.reference.custom.health.LabOrderPriorityType;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratorType;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import org.hibernate.validator.NotNull;

@Entity
@Table(name = "Person_Lab_Order")
public class LabOrder extends AbstractTopLevelEntity
{
    public static final String PK_COLUMN_NAME = "lab_order_id";

    private Long labOrderId;
    private Person patient;
    private Date orderDate;
    private Date doneDate;
    private Person physician;
    private Person resultContact;
    private String labComments;
    private String patientComments;
    private String patientInstructions;

    private Icd icd;
    private LabOrderStatusType status;
    private LabOrderPriorityType priority;

    private List<LabOrderEntry> entries = new ArrayList<LabOrderEntry>();

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getLabOrderId()
    {
        return labOrderId;
    }

    public void setLabOrderId(final Long labOrderId)
    {
        this.labOrderId = labOrderId;
    }

    @ManyToOne
    @JoinColumn(name = "patient_id", referencedColumnName = Person.PK_COLUMN_NAME, nullable = false)
    @NotNull
    public Person getPatient()
    {
        return patient;
    }

    public void setPatient(final Person patient)
    {
        this.patient = patient;
    }

    public Date getOrderDate()
    {
        return orderDate;
    }

    public void setOrderDate(final Date orderDate)
    {
        this.orderDate = orderDate;
    }

    public Date getDoneDate()
    {
        return doneDate;
    }

    public void setDoneDate(final Date doneDate)
    {
        this.doneDate = doneDate;
    }

    @ManyToOne
    @JoinColumn(name = "physician_id", referencedColumnName = Person.PK_COLUMN_NAME, nullable = false)
    @NotNull
    public Person getPhysician()
    {
        return physician;
    }

    public void setPhysician(final Person physician)
    {
        this.physician = physician;
    }

    @ManyToOne
    @JoinColumn(name = "result_contact_id", referencedColumnName = Person.PK_COLUMN_NAME, nullable = false)
    @NotNull
    public Person getResultContact()
    {
        return resultContact;
    }

    public void setResultContact(final Person resultContact)
    {
        this.resultContact = resultContact;
    }

    @Column(length = 512)
    public String getLabComments()
    {
        return labComments;
    }

    public void setLabComments(final String labComments)
    {
        this.labComments = labComments;
    }

    @Column(length = 512)
    public String getPatientComments()
    {
        return patientComments;
    }

    public void setPatientComments(final String patientComments)
    {
        this.patientComments = patientComments;
    }

    @Column(length = 512)
    public String getPatientInstructions()
    {
        return patientInstructions;
    }

    public void setPatientInstructions(final String patientInstructions)
    {
        this.patientInstructions = patientInstructions;
    }

    @ManyToOne
    @JoinColumn(referencedColumnName = Icd.PK_COLUMN_NAME, name = "icd_code")
    public Icd getIcd()
    {
        return icd;
    }

    public void setIcd(final Icd icd)
    {
        this.icd = icd;
    }

    @ManyToOne
    @JoinColumn(name = LabOrderStatusType.PK_COLUMN_NAME)
    public LabOrderStatusType getStatus()
    {
        return status;
    }

    public void setStatus(final LabOrderStatusType status)
    {
        this.status = status;
    }

    @ManyToOne
    @JoinColumn(name = LabOrderPriorityType.PK_COLUMN_NAME)
    public LabOrderPriorityType getPriority()
    {
        return priority;
    }

    public void setPriority(final LabOrderPriorityType priority)
    {
        this.priority = priority;
    }

    @OneToMany(mappedBy = "labOrder", cascade = CascadeType.ALL)
    public List<LabOrderEntry> getEntries()
    {
        return entries;
    }

    public void setEntries(final List<LabOrderEntry> entries)
    {
        this.entries = entries;
    }
}
