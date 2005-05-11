/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.reference.custom.insurance;

import com.medigy.persist.reference.custom.AbstractCustomReferenceEntity;
import com.medigy.persist.reference.custom.CachedCustomReferenceEntity;
import com.medigy.persist.reference.custom.CustomReferenceEntity;

import javax.persistence.Id;
import javax.persistence.GeneratorType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Coverage_Level_Type")
public class CoverageLevelType extends AbstractCustomReferenceEntity
{
    public enum Cache implements CachedCustomReferenceEntity
    {
        INDIVIDUAL_DEDUCTIBLE("IND_DEDUCT"),
        FAMILY_DEDUCTIBLE("FAMILY_DEDUCT"),
        CONINSURANCE("COINS"), /* Percent amount */
        COPAY("COPAY"),
        COVERAGE_RANGE("RANGE");

        private final String code;
        private CoverageLevelType entity;

        Cache(final String code)
        {
            this.code = code;
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
    }

    @Id(generate = GeneratorType.AUTO)
    @Column(name = "coverage_level_type_id")
    public Long getCoverageLevelTypeId()
    {
        return super.getSystemId();
    }

    public void setCoverageLevelTypeId(final Long id)
    {
        super.setSystemId(id);
    }
}
