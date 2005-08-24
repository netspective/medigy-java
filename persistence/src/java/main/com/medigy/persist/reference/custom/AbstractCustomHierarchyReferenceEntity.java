/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.reference.custom;

import com.medigy.persist.model.common.AbstractEntity;
import com.medigy.persist.model.party.Party;

import javax.persistence.Column;
import javax.persistence.EmbeddableSuperclass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@EmbeddableSuperclass
public abstract class AbstractCustomHierarchyReferenceEntity extends AbstractEntity implements CustomHierarchyReferenceEntity, Comparable
{
    private Long systemId;
    private String code;
    private String label;
    private String description;

    private Party party;

    public AbstractCustomHierarchyReferenceEntity()
    {
    }

    @Transient
    public Long getSystemId()
    {
        return systemId;
    }

    protected void setSystemId(Long systemId)
    {
        this.systemId = systemId;
    }

    @Column(name = "code", length = 15, nullable = false)
    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    @Column(length = 100, nullable = false)
    public String getLabel()
    {
        return label;
    }

    public void setLabel(final String label)
    {
        this.label = label;
    }

    @Column(length = 256)
    public String getDescription()
    {
        return description;
    }

    public void setDescription(final String description)
    {
        this.description = description;
    }

    @ManyToOne
    @JoinColumn(name = Party.PK_COLUMN_NAME, nullable = false)
    public Party getParty()
    {
        return party;
    }

    public void setParty(final Party party)
    {
        this.party = party;
    }

    public int compareTo(Object o)
    {
        if (o == this)
            return 0;

        final CustomReferenceEntity otherType = (CustomReferenceEntity) o;
        if (otherType.getSystemId().longValue() == this.getSystemId().longValue())
            return 0;
        else
            return -1;
    }

    public boolean equals(Object obj)
    {
        if (obj == null)
            return false;
        final CustomReferenceEntity otherType = (CustomReferenceEntity) obj;
        return (otherType.getSystemId().longValue() == this.getSystemId().longValue());
    }

}
