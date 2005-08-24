/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.model.insurance;

import com.medigy.persist.model.common.AbstractEntity;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public abstract class CoverageLevelRelationship extends AbstractEntity
{
    private CoverageLevel coverageLevel;

    @ManyToOne
    @JoinColumn(name = CoverageLevel.PK_COLUMN_NAME)
    public CoverageLevel getCoverageLevel()
    {
        return coverageLevel;
    }

    public void setCoverageLevel(final CoverageLevel coverageLevel)
    {
        this.coverageLevel = coverageLevel;
    }
}
