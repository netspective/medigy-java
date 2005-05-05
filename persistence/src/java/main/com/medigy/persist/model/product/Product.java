/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.model.product;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.medigy.persist.model.common.AbstractTopLevelEntity;
import com.medigy.persist.model.invoice.InvoiceItem;
import com.medigy.persist.model.org.Organization;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE, discriminatorValue = "General")
public class Product extends AbstractTopLevelEntity
{
    private Long productId;
    private String name;
    private Date introductionDate;
    private Date saleDiscontinuationDate;
    private Date supportDiscontinuationDate;
    private String comment;
    private Organization organization;

    private ProductCategory productCategory;
    private Set<InvoiceItem> invoiceItems = new HashSet<InvoiceItem>();
    
    @Id(generate = GeneratorType.AUTO)
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

    public Date getIntroductionDate()
    {
        return introductionDate;
    }

    public void setIntroductionDate(final Date introductionDate)
    {
        this.introductionDate = introductionDate;
    }

    public Date getSaleDiscontinuationDate()
    {
        return saleDiscontinuationDate;
    }

    public void setSaleDiscontinuationDate(final Date saleDiscontinuationDate)
    {
        this.saleDiscontinuationDate = saleDiscontinuationDate;
    }

    public Date getSupportDiscontinuationDate()
    {
        return supportDiscontinuationDate;
    }

    public void setSupportDiscontinuationDate(final Date supportDiscontinuationDate)
    {
        this.supportDiscontinuationDate = supportDiscontinuationDate;
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

    @OneToMany(mappedBy = "product")
    public Set<InvoiceItem> getInvoiceItems()
    {
        return invoiceItems;
    }

    public void setInvoiceItems(final Set<InvoiceItem> invoiceItems)
    {
        this.invoiceItems = invoiceItems;
    }

    
}
