/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.reference.custom.health;

import com.medigy.persist.reference.custom.AbstractCustomReferenceEntity;
import com.medigy.persist.reference.custom.CachedCustomReferenceEntity;
import com.medigy.persist.reference.custom.CustomReferenceEntity;

import javax.persistence.Id;
import javax.persistence.GeneratorType;
import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.Table;

@Entity
@Table(name = "Lab_Order_Status_Type")
public class LabOrderStatusType extends AbstractCustomReferenceEntity
{
    public static final String PK_COLUMN_NAME = "status_type_id";

    public enum Cache implements CachedCustomReferenceEntity
    {
        APPROVED("APPROV", "Approved"),
        PENDING("PEND", "Pending"),
        FAXED_TO_LAB("FAXED", "Faxed to Lab"),
        EMAILED_TO_LAB("EMAILED", "Emailed to Lab"),
        TRANSMITTED_TO_LAB("SENT", "Transmitted to Lab");

        private String code;
        private String label;
        private LabOrderStatusType entity;

        Cache(final String code, final String label)
        {
            this.code = code;
            this.label = label;
        }

        public String getCode()
        {
            return code;
        }

        public LabOrderStatusType getEntity()
        {
            return entity;
        }

        public void setEntity(final CustomReferenceEntity entity)
        {
            this.entity = (LabOrderStatusType) entity;
        }

        public String getLabel()
        {
            return label;
        }
    }

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getLabOrderStatusTypeId()
    {
        return super.getSystemId();
    }

    public void setLabOrderStatusTypeId(final Long id)
    {
        super.setSystemId(id);
    }
}

