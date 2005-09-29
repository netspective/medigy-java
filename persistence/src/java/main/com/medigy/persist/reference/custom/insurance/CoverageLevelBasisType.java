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

@Entity
public class CoverageLevelBasisType extends AbstractCustomReferenceEntity
{
    public static final String PK_COLUMN_NAME =  "coverage_level_basis_id";

    public enum Cache implements CachedCustomReferenceEntity
    {
        PER_INCIDENT("PER_INC", "Per Incident"),
        PER_YEAR("PER_YR", "Per Year"),
        PER_PERSON("PER_PERSON", "Per Person");

        private final String label;
        private final String code;
        private CoverageLevelBasisType entity;

        Cache(final String code, final String label)
        {
            this.code = code;
            this.label = label;
        }

        public String getCode()
        {
            return code;
        }

        public CoverageLevelBasisType getEntity()
        {
            return entity;
        }

        public void setEntity(final CustomReferenceEntity entity)
        {
            this.entity = (CoverageLevelBasisType) entity;
        }

        public String getLabel()
        {
            return label;
        }
    }

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME )
    public Long getCoverageLevelBasisId()
    {
        return super.getSystemId();
    }

    public void setCoverageLevelBasisId(final Long id)
    {
        super.setSystemId(id);
    }
}
