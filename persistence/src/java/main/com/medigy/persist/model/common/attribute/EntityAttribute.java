package com.medigy.persist.model.common.attribute;

import com.medigy.persist.model.common.AbstractTopLevelEntity;

import javax.persistence.Column;
import javax.persistence.EmbeddableSuperclass;
import javax.persistence.Transient;

/**
 * 
 */
@EmbeddableSuperclass
public abstract class EntityAttribute extends AbstractTopLevelEntity
{
    private Long attributeId;
    private String label;

    @Transient
    public Long getAttributeId()
    {
        return attributeId;
    }

    public void setAttributeId(final Long attributeId)
    {
        this.attributeId = attributeId;
    }

    @Column(length = 128)
    public String getLabel()
    {
        return label;
    }

    public void setLabel(final String label)
    {
        this.label = label;
    }
}
