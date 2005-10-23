package com.medigy.persist.model.insurance;

import com.medigy.persist.model.common.AbstractEntity;
import com.medigy.persist.reference.custom.insurance.CoverageLevelBasisType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Class for defining the basis for each coverage level
 */
@Entity
public class CoverageLevelBasis extends AbstractEntity
{
    public static final String PK_COLUMN_NAME = "level_basis_id";

    private Long coverageLevelBasisId;
    private CoverageLevelBasisType type;
    private CoverageLevel coverageLevel;

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getCoverageLevelBasisId()
    {
        return coverageLevelBasisId;
    }

    public void setCoverageLevelBasisId(final Long coverageLevelBasisId)
    {
        this.coverageLevelBasisId = coverageLevelBasisId;
    }

    @ManyToOne
    @JoinColumn(name = "coverage_level_basis_type_id", nullable = false)
    public CoverageLevelBasisType getType()
    {
        return type;
    }

    public void setType(final CoverageLevelBasisType type)
    {
        this.type = type;
    }

    @ManyToOne
    @JoinColumn(name = CoverageLevel.PK_COLUMN_NAME, nullable = false)
    public CoverageLevel getCoverageLevel()
    {
        return coverageLevel;
    }

    public void setCoverageLevel(final CoverageLevel coverageLevel)
    {
        this.coverageLevel = coverageLevel;
    }
}
