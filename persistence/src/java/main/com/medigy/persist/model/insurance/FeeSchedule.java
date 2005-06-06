/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.model.insurance;

import com.medigy.persist.model.common.AbstractDateDurationEntity;
import com.medigy.persist.model.org.Organization;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import java.util.Set;
import java.util.HashSet;

@Entity
public class FeeSchedule extends AbstractDateDurationEntity
{
    public static final String PK_COLUMN_NAME = "fee_schedule_id";

    private Long feeScheduleId;
    private String name;
    private Organization organization;
    private String description;
    private String caption;
    private Long sequenceNumber;

    private Set<FeeScheduleItem> feeScheduleItems = new HashSet<FeeScheduleItem>();

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getFeeScheduleId()
    {
        return feeScheduleId;
    }

    public void setFeeScheduleId(final Long feeScheduleId)
    {
        this.feeScheduleId = feeScheduleId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public Organization getOrganization()
    {
        return organization;
    }

    public void setOrganization(final Organization organization)
    {
        this.organization = organization;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(final String description)
    {
        this.description = description;
    }

    public String getCaption()
    {
        return caption;
    }

    public void setCaption(final String caption)
    {
        this.caption = caption;
    }

    public Long getSequenceNumber()
    {
        return sequenceNumber;
    }

    public void setSequenceNumber(final Long sequenceNumber)
    {
        this.sequenceNumber = sequenceNumber;
    }

}
