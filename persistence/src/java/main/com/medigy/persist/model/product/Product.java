/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.model.product;

import com.medigy.persist.model.common.AbstractDateDurationEntity;
import com.medigy.persist.model.common.EffectiveDates;
import com.medigy.persist.model.common.AbstractTopLevelEntity;
import com.medigy.persist.model.org.Organization;
import com.medigy.persist.reference.custom.product.ProductType;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratorType;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Basic;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.EmbeddableSuperclass;

import org.hibernate.validator.NotNull;

import java.util.Date;

@EmbeddableSuperclass
public class Product extends AbstractTopLevelEntity
{
    public static final String PK_COLUMN_NAME = "product_id";

    private Long productId;
    private String name;
    private Organization organization;
    private EffectiveDates effectiveDates = new EffectiveDates();

    @Transient
    public Long getProductId()
    {
        return productId;
    }

    protected void setProductId(final Long productId)
    {
        this.productId = productId;
    }

    @Column(length = 512, nullable = false)
    @NotNull
    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    @ManyToOne
    @JoinColumn(name = Organization.PK_COLUMN_NAME)
    public Organization getOrganization()
    {
        return organization;
    }

    public void setOrganization(final Organization party)
    {
        this.organization = party;
    }


    @Basic(temporalType = TemporalType.DATE)
    public Date getIntroductionDate()
    {
        return effectiveDates.getFromDate();
    }

    public void setIntroductionDate(final Date introductionDate)
    {
        this.effectiveDates.setFromDate(introductionDate);
    }

    @Basic(temporalType = TemporalType.DATE)
    public Date getDiscontinuedDate()
    {
        return effectiveDates.getThroughDate();
    }

    public void setDiscontinuedDate(final Date discontinuedDate)
    {
        this.effectiveDates.setThroughDate(discontinuedDate);
    }
}
