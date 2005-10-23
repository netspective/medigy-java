package com.medigy.persist.reference.custom.attribute;

import com.medigy.persist.reference.custom.AbstractCustomReferenceEntity;
import com.medigy.persist.reference.custom.CachedCustomReferenceEntity;
import com.medigy.persist.reference.custom.CustomReferenceEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;

@Entity
public class AttributeType  extends AbstractCustomReferenceEntity
{
    public static final String PK_COLUMN_NAME = "attr_type_id";

    public enum Cache implements CachedCustomReferenceEntity
    {
        DATE("DATE", "Date"),
        DATE_AND_TIME("DATETIME", "Date and Time"),
        TIME("TIME", "Time"),
        TEXT("TEXT", "Text"),
        INTEGER("INT", "Integer Number"),
        DECIMAL("FLOAT", "Decimal Number"),
        INTEGER_RANGE("INT_RANGE", "Range of Integers"),
        DECIMAL_RANGE("FLOAT_RANGE", "Range of Decimals");

        private final String label;
        private final String code;
        private AttributeType entity;

        Cache(final String code, final String label)
        {
            this.code = code;
            this.label = label;
        }

        public String getLabel()
        {
            return label;
        }

        public String getCode()
        {
            return code;
        }

        public void setEntity(final CustomReferenceEntity entity)
        {
            this.entity = (AttributeType) entity;
        }

        public AttributeType getEntity()
        {
            return entity;
        }
    }

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getAttributeTypeId()
    {
        return getSystemId();
    }

    public void setAttributeTypeId(final Long id)
    {
        setSystemId(id);
    }
}
