/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.model.insurance;

import com.medigy.persist.model.common.AbstractEntity;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public abstract class CoverageRelationship extends AbstractEntity
{
    private Coverage coverage;
    
    @ManyToOne
    @JoinColumn(name = Coverage.PK_COLUMN_NAME)
    public Coverage getCoverage()
    {
        return coverage;
    }

    public void setCoverage(final Coverage coverage)
    {
        this.coverage = coverage;
    }
}
