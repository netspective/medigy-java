/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.reference.custom.insurance;

import com.medigy.persist.reference.custom.AbstractCustomReferenceEntity;
import com.medigy.persist.reference.custom.CachedCustomReferenceEntity;
import com.medigy.persist.reference.custom.CustomReferenceEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;

/**
 * Various attributes that could be associated with an insurance plan
 */
@Entity
public class InsurancePlanAttributeType extends AbstractCustomReferenceEntity
{
    public static final String PK_COLUMN_NAME = "ins_plan_attr_type_id";

    public enum Cache implements CachedCustomReferenceEntity
    {
        CHAMPUS_STATUS("CHAMPUS_STATUS", "Champus Status"),
        CHAMPUS_GRADE("CHAMPUS_GRADE", "Champus Grade"),
        CHAMPUS_BRANCH("CHAMPUS_BRANCH", "Champus Branch"),
        BCBS_PLAN_CODE("BCBS_CODE", "BCBS Plan Code");

        private final String code;
        private final String label;
        private InsurancePlanAttributeType entity;

        Cache(final String code, final String label)
        {
            this.code = code;
            this.label = label;
        }

        public String getCode()
        {
            return code;
        }

        public InsurancePlanAttributeType getEntity()
        {
            return entity;
        }

        public void setEntity(final CustomReferenceEntity entity)
        {
            this.entity = (InsurancePlanAttributeType) entity;
        }

        public String getLabel()
        {
            return label;
        }
    }

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getInsurancePlanAttributeTypeId()
    {
        return getSystemId();
    }

    public void setInsurancePlanAttributeTypeId(final Long id)
    {
        setSystemId(id);
    }
}
