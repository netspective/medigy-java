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
        SELF_PAY("SELF"),
        INSURANCE("INSURANCE"),
        HMO_CAP("HMO_CAP"),
        HMO_NON_CAP("HMO_NONCAP"),
        PPO("PPO"),
        MEDICARE("MEDICARE"),
        MEDICAID("MEDICAID"),
        WORKERS_COMPENSATION("WORKERS_COMP"),
        THIRD_PARTY_PAYER("THIRD_PARTY"),
        CHAMPUS("CHAMPUS"),
        CHAMPVA("CHAMPVA"),
        FECA_BLK_LUNG("FECA_BLK_LUNG"),
        BCBS("BCBS"),
        MC("MC"),
        POS("POS"),
        RAILROAD_MEDICARE("RAILROAD"),
        OTHER("OTHER");

        private final String code;
        private InsuranceProductType entity;

        Cache(final String code)
        {
            this.code = code;
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
