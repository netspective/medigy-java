/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.model.product;

import com.medigy.persist.model.common.AbstractDateDurationEntity;
import com.medigy.persist.model.org.Organization;
import com.medigy.persist.reference.custom.product.ProductType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE, discriminatorValue = "General")
public class Product extends AbstractDateDurationEntity
{
    public static final String PK_COLUMN_NAME = "product_id";

    private Long productId;
    private String name;
    private String comment;
    private Organization organization;
    private ProductType type;

    private ProductCategory productCategory;
    //private Set<InvoiceItem> invoiceItems = new HashSet<InvoiceItem>();

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

    public String getName()
    {
        return name;
    }

    @Column(length = 100)
    public void setName(final String name)
    {
        this.name = name;
    }

    @Column(length = 100)
    public String getComment()
    {
        return comment;
    }

    public void setComment(final String comment)
    {
        this.comment = comment;
    }

    @ManyToOne
    @JoinColumn(name = "party_id")
    public Organization getOrganization()
    {
        return organization;
    }

    public void setOrganization(final Organization party)
    {
        this.organization = party;
    }

    @ManyToOne
    @JoinColumn(name = "product_category_id")
    public ProductCategory getProductCategory()
    {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory)
    {
        this.productCategory = productCategory;
    }

    @ManyToOne
    @JoinColumn(name = ProductType.PK_COLUMN_NAME)
    public ProductType getType()
    {
        return type;
    }

    public void setType(final ProductType type)
    {
        this.type = type;
    }

    /*
    @OneToMany(mappedBy = "product")
    public Set<InvoiceItem> getInvoiceItems()
    {
        return invoiceItems;
    }

    public void setInvoiceItems(final Set<InvoiceItem> invoiceItems)
    {
        this.invoiceItems = invoiceItems;
    }
    */

}
