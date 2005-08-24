/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.model.insurance;

import com.medigy.persist.reference.custom.insurance.InsurancePlanAttributeType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class InsurancePlanAttribute extends InsuranceAttribute
{
    public static final String PK_COLUMN_NAME = "ins_plan_attr_id";

    private InsurancePlan insurancePlan;
    private InsurancePlanAttributeType type;

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getInsurancePlanAttributeId()
    {
        return getInsuranceAttributeId();
    }

    public void setInsurancePlanAttributeId(final Long id)
    {
        setInsuranceAttributeId(id);
    }

    @ManyToOne
    @JoinColumn(name = InsurancePlan.PK_COLUMN_NAME)
    public InsurancePlan getInsurancePlan()
    {
        return insurancePlan;
    }

    public void setInsurancePlan(final InsurancePlan insurancePlan)
    {
        this.insurancePlan = insurancePlan;
    }

    @ManyToOne
    @JoinColumn(name = InsurancePlanAttributeType.PK_COLUMN_NAME)
    public InsurancePlanAttributeType getType()
    {
        return type;
    }

    public void setType(final InsurancePlanAttributeType type)
    {
        this.type = type;
    }
}
