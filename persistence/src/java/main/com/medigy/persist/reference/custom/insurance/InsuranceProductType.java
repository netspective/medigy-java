/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.reference.custom.insurance;

import com.medigy.persist.reference.custom.CachedCustomReferenceEntity;
import com.medigy.persist.reference.custom.CustomReferenceEntity;
import com.medigy.persist.reference.custom.product.ProductType;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Transient;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE, discriminatorValue = "Insurance")
public class InsuranceProductType  extends ProductType
{
    public static final String PK_COLUMN_NAME = ProductType.PK_COLUMN_NAME;
    public enum Cache implements CachedCustomReferenceEntity
    {
        SELF_PAY("SELF", "Self Pay"),
        INSURANCE("INSURANCE", "Insurance"),
        HMO_CAP("HMO_CAP", "HMO Cap"),
        HMO_NON_CAP("HMO_NONCAP", "HMO Non-cap"),
        PPO("PPO", "PPO"),
        MEDICARE("MEDICARE", "Medicare"),
        MEDICAID("MEDICAID", "Medicaid"),
        WORKERS_COMPENSATION("WORKERS_COMP", "Workers Compensation"),
        THIRD_PARTY_PAYER("THIRD_PARTY", "Third Party"),
        CHAMPUS("CHAMPUS", "Champus"),
        CHAMPVA("CHAMPVA", "Champva"),
        FECA_BLK_LUNG("FECA_BLK_LUNG", "FECA Black Lung"),
        BCBS("BCBS", "BCBS"),
        MC("MC", "MC"),
        POS("POS", "Point of Service"),
        RAILROAD_MEDICARE("RAILROAD", "Railroad Medicare"),
        OTHER("OTHER", "Other");

        private final String code;
        private final String label;
        private InsuranceProductType entity;

        Cache(final String code, final String label)
        {
            this.code = code;
            this.label = label;
        }

        public String getCode()
        {
            return code;
        }

        public InsuranceProductType getEntity()
        {
            return entity;
        }

        public void setEntity(final CustomReferenceEntity entity)
        {
            this.entity = (InsuranceProductType) entity;
        }

        public String getLabel()
        {
            return label;
        }
    }

    @Transient
    public Long getInsuranceProductTypeId()
    {
        return getProductTypeId();
    }

    public void setInsuranceProductTypeId(final Long id)
    {
        setProductTypeId(id);
    }
}
