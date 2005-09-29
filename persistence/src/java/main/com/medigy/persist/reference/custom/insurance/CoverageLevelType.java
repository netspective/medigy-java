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
import javax.persistence.Table;

@Entity
@Table(name = "Coverage_Level_Type")
public class CoverageLevelType extends AbstractCustomReferenceEntity
{
    public static final String PK_COLUMN_NAME = "coverage_level_type_id";

    public enum Cache implements CachedCustomReferenceEntity
    {
        INDIVIDUAL_DEDUCTIBLE("IND_DEDUCT", "Individual Deductible"),
        FAMILY_DEDUCTIBLE("FAMILY_DEDUCT", "Family Deductible"),
        CONINSURANCE("COINS", "Coinsurance"), /* Percent amount */
        COPAY("COPAY", "Copay"),
        COVERAGE_RANGE("RANGE", "Coverage Range");

        private final String label;
        private final String code;
        private CoverageLevelType entity;

        Cache(final String code, final String label)
        {
            this.code = code;
            this.label = label;
        }

        public String getCode()
        {
            return code;
        }

        public CoverageLevelType getEntity()
        {
            return entity;
        }

        public void setEntity(final CustomReferenceEntity entity)
        {
            this.entity = (CoverageLevelType) entity;
        }

        public String getLabel()
        {
            return label;
        }
    }

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getCoverageLevelTypeId()
    {
        return super.getSystemId();
    }

    public void setCoverageLevelTypeId(final Long id)
    {
        super.setSystemId(id);
    }
}
