/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.model.health.lab;

import com.medigy.persist.model.common.AbstractTopLevelEntity;
import org.hibernate.validator.NotNull;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Person_Lab_Order_Entry")
public class LabOrderEntry extends AbstractTopLevelEntity
{
    public static final String PK_COLUMN_NAME = "lab_order_entry_id";

    private Long labOrderEntryId;
    private LabOrder labOrder;
    private LabOrderEntry parentEntry;

    private String caption;
    private String labCode;
    private String chargeCode;

    private Float physicianCost;
    private Float patientCost;
    private String panelTestNames;

    private List<LabOrderEntry> childEntries = new ArrayList<LabOrderEntry>();

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getLabOrderEntryId()
    {
        return labOrderEntryId;
    }

    public void setLabOrderEntryId(final Long labOrderEntryId)
    {
        this.labOrderEntryId = labOrderEntryId;
    }

    @ManyToOne
    @JoinColumn(name = LabOrder.PK_COLUMN_NAME, nullable = false)
    @NotNull
    public LabOrder getLabOrder()
    {
        return labOrder;
    }

    public void setLabOrder(final LabOrder labOrder)
    {
        this.labOrder = labOrder;
    }

    @ManyToOne
    @JoinColumn(name = "parent_entry_id", referencedColumnName = PK_COLUMN_NAME)
    public LabOrderEntry getParentEntry()
    {
        return parentEntry;
    }

    public void setParentEntry(final LabOrderEntry parentEntry)
    {
        this.parentEntry = parentEntry;
    }

    @Column(length = 64)
    public String getCaption()
    {
        return caption;
    }

    public void setCaption(final String caption)
    {
        this.caption = caption;
    }

    @Column(length = 64)
    public String getLabCode()
    {
        return labCode;
    }

    public void setLabCode(final String labCode)
    {
        this.labCode = labCode;
    }

    @Column(length = 64)
    public String getChargeCode()
    {
        return chargeCode;
    }

    public void setChargeCode(final String chargeCode)
    {
        this.chargeCode = chargeCode;
    }

    public Float getPhysicianCost()
    {
        return physicianCost;
    }

    public void setPhysicianCost(final Float physicianCost)
    {
        this.physicianCost = physicianCost;
    }

    public Float getPatientCost()
    {
        return patientCost;
    }

    public void setPatientCost(final Float patientCost)
    {
        this.patientCost = patientCost;
    }

    /**
     * Gets comma separated list of panel test names
     * @return comma separated list of panel test names
     */
    @Column(length = 512)
    public String getPanelTestNames()
    {
        return panelTestNames;
    }

    public void setPanelTestNames(final String panelTestNames)
    {
        this.panelTestNames = panelTestNames;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parentEntry")    
    public List<LabOrderEntry> getChildEntries()
    {
        return childEntries;
    }

    public void setChildEntries(final List<LabOrderEntry> childEntries)
    {
        this.childEntries = childEntries;
    }
}
