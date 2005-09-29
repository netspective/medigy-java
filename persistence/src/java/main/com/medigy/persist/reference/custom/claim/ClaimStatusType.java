package com.medigy.persist.reference.custom.claim;

import com.medigy.persist.reference.custom.AbstractCustomReferenceEntity;
import com.medigy.persist.reference.custom.CachedCustomReferenceEntity;
import com.medigy.persist.reference.custom.CustomReferenceEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;

@Entity
public class ClaimStatusType extends AbstractCustomReferenceEntity
{
    public static final String PK_COLUMN_NAME = "claim_status_type_id";

    public enum Cache implements CachedCustomReferenceEntity
    {
        SUBMITTED("SUBMITTED", "Submitted"),
        PENDING("PENDING", "Pending"),
        DENIED("DENIED", "Denied"),
        SENT_BACK_FOR_CORRECTION("CORRECTION", "Sent back for correction"),
        SETTLED("SETTLED", "Settled"),
        OTHER("OTHER", "Other");

        private final String label;
        private final String code;
        private ClaimStatusType entity;

        Cache(final String code, final String label)
        {
            this.code = code;
            this.label = label;
        }

        public String getCode()
        {
            return code;
        }

        public ClaimStatusType getEntity()
        {
            return entity;
        }

        public void setEntity(final CustomReferenceEntity entity)
        {
            this.entity = (ClaimStatusType) entity;
        }

        public String getLabel()
        {
            return label;
        }
    }

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getClaimStatusTypeId()
    {
        return super.getSystemId();
    }

    protected void setClaimStatusTypeId(final Long id)
    {
        super.setSystemId(id);
    }
}
