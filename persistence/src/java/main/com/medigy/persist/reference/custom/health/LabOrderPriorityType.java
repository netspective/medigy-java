/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.reference.custom.health;

import com.medigy.persist.reference.custom.AbstractCustomReferenceEntity;
import com.medigy.persist.reference.custom.CachedCustomReferenceEntity;
import com.medigy.persist.reference.custom.CustomReferenceEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Lab_Order_Priority_Type")
public class LabOrderPriorityType extends AbstractCustomReferenceEntity
{
    public static final String PK_COLUMN_NAME = "priority_type_id";

    public enum Cache implements CachedCustomReferenceEntity
    {
        NORMAL("NORM", "Normal"),
        STAT("STAT", "STAT"),
        ASAP("ASAP", "ASAP"),
        TODAY("TODAY", "Today");

        private String code;
        private String label;
        private LabOrderPriorityType entity;

        Cache(final String code, final String label)
        {
            this.code = code;
            this.label = label;
        }

        public String getCode()
        {
            return code;
        }

        public LabOrderPriorityType getEntity()
        {
            return entity;
        }

        public void setEntity(final CustomReferenceEntity entity)
        {
            this.entity = (LabOrderPriorityType) entity;
        }

        public String getLabel()
        {
            return label;
        }
    }

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getLabOrderPriorityTypeId()
    {
        return super.getSystemId();
    }

    public void setLabOrderPriorityTypeId(final Long id)
    {
        super.setSystemId(id);
    }
}
