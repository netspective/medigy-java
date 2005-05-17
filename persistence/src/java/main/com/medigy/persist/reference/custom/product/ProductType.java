/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.reference.custom.product;

import com.medigy.persist.reference.custom.AbstractCustomReferenceEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratorType;
import javax.persistence.Column;

@Entity
public class ProductType  extends AbstractCustomReferenceEntity
{
    public static final String PK_COLUMN_NAME = "product_type_id";

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getProductTypeId()
    {
        return getSystemId();
    }

    public void setProductTypeId(final Long productTypeId)
    {
        setSystemId(productTypeId);
    }
}
