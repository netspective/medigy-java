/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.reference.custom.insurance;

import com.medigy.persist.reference.custom.AbstractCustomReferenceEntity;
import com.medigy.persist.reference.custom.CachedCustomReferenceEntity;
import com.medigy.persist.reference.custom.CustomReferenceEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratorType;
import javax.persistence.Column;

@Entity
public class CoverageLevelBasisType extends AbstractCustomReferenceEntity
{
    public enum Cache implements CachedCustomReferenceEntity
    {
        PER_INCIDENT("PER_INC"),
        PER_YEAR("PER_YR"),
        PER_PERSON("PER_PERSON");

        private final String code;
        private CoverageLevelBasisType entity;

        Cache(final String code)
        {
            this.code = code;
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
    }

    @Id(generate = GeneratorType.AUTO)
    @Column(name = "coverage_level_basis_id")
    public Long getCoverageLevelBasisId()
    {
        return super.getSystemId();
    }

    public void setCoverageLevelBasisId(final Long id)
    {
        super.setSystemId(id);
    }
}
