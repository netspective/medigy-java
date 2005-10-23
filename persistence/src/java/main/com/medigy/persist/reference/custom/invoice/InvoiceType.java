package com.medigy.persist.reference.custom.invoice;

import com.medigy.persist.reference.custom.AbstractCustomReferenceEntity;
import com.medigy.persist.reference.custom.CachedCustomReferenceEntity;
import com.medigy.persist.reference.custom.CustomReferenceEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;

/**
 * Reference entity representing the Invoice type
 */
@Entity
public class InvoiceType extends AbstractCustomReferenceEntity
{
    public static final String PK_COLUMN_NAME = "invoice_type_id";

    public enum Cache implements CachedCustomReferenceEntity
    {
        HCFA_1500("HCFA", "HCFA 1500 Claim"),
        SERVICES("SERVICE", "Services Rendered");

        private final String label;
        private final String code;
        private InvoiceType entity;

        Cache(final String code, final String label)
        {
            this.code = code;
            this.label = label;
        }

        public String getCode()
        {
            return code;
        }
                                                                                                
        public InvoiceType getEntity()
        {
            return entity;
        }

        public void setEntity(final CustomReferenceEntity entity)
        {
            this.entity = (InvoiceType) entity;
        }

        public String getLabel()
        {
            return label;
        }
    }

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getInvoiceTypeId()
    {
        return super.getSystemId();
    }

    protected void setInvoiceTypeId(final Long id)
    {
        super.setSystemId(id);
    }
}

