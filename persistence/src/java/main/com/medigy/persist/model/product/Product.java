/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.model.product;

import com.medigy.persist.model.common.EffectiveDates;
import com.medigy.persist.model.org.Organization;
import com.medigy.persist.reference.custom.CustomReferenceEntity;
import org.hibernate.validator.NotNull;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.Date;

//@EmbeddableSuperclass
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Product implements CustomReferenceEntity
{
    public static final String PK_COLUMN_NAME = "product_id";

    private Long productId;
    private String productName;
    private Organization organization;
    private String code;

    private EffectiveDates effectiveDates = new EffectiveDates();

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getProductId()
    {
        return productId;
    }

    protected void setProductId(final Long productId)
    {
        this.productId = productId;
    }

    @Transient
    public Long getSystemId()
    {
        return productId;
    }

    protected void setSystemId(final Long id)
    {
        productId = id;
    }

    @Column(length = 10)
    public String getCode()
    {
        return code;
    }

    public void setCode(final String code)
    {
        this.code = code;
    }

    @Transient
    public String getLabel()
    {
        return productName;
    }

    @Transient
    public Organization getParty()
    {
        return organization;
    }

    @Column(length = 512, nullable = false)
    @NotNull
    public String getProductName()
    {
        return productName;
    }

    public void setProductName(final String name)
    {
        this.productName = name;
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
    @Column(name = "introduction_date")
    public Date getIntroductionDate()
    {
        return effectiveDates.getFromDate();
    }

    public void setIntroductionDate(final Date introductionDate)
    {
        this.effectiveDates.setFromDate(introductionDate);
    }

    @Basic(temporalType = TemporalType.DATE)
    @Column(name = "discontinued_date")
    public Date getDiscontinuedDate()
    {
        return effectiveDates.getThroughDate();
    }

    public void setDiscontinuedDate(final Date discontinuedDate)
    {
        this.effectiveDates.setThroughDate(discontinuedDate);
    }
}
