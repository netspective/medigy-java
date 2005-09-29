/*
 * Copyright (c) 2000-2003 Netspective Communications LLC. All rights reserved.
 *
 * Netspective Communications LLC ("Netspective") permits redistribution, modification and use of this file in source
 * and binary form ("The Software") under the Netspective Source License ("NSL" or "The License"). The following
 * conditions are provided as a summary of the NSL but the NSL remains the canonical license and must be accepted
 * before using The Software. Any use of The Software indicates agreement with the NSL.
 *
 * 1. Each copy or derived work of The Software must preserve the copyright notice and this notice unmodified.
 *
 * 2. Redistribution of The Software is allowed in object code form only (as Java .class files or a .jar file
 *    containing the .class files) and only as part of an application that uses The Software as part of its primary
 *    functionality. No distribution of the package is allowed as part of a software development kit, other library,
 *    or development tool without written consent of Netspective. Any modified form of The Software is bound by these
 *    same restrictions.
 *
 * 3. Redistributions of The Software in any form must include an unmodified copy of The License, normally in a plain
 *    ASCII text file unless otherwise agreed to, in writing, by Netspective.
 *
 * 4. The names "Netspective", "Axiom", "Commons", "Junxion", and "Sparx" are trademarks of Netspective and may not be
 *    used to endorse products derived from The Software without without written consent of Netspective. "Netspective",
 *    "Axiom", "Commons", "Junxion", and "Sparx" may not appear in the names of products derived from The Software
 *    without written consent of Netspective.
 *
 * 5. Please attribute functionality where possible. We suggest using the "powered by Netspective" button or creating
 *    a "powered by Netspective(tm)" link to http://www.netspective.com for each application using The Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT,
 * ARE HEREBY DISCLAIMED.
 *
 * NETSPECTIVE AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE OR ANY THIRD PARTY AS A
 * RESULT OF USING OR DISTRIBUTING THE SOFTWARE. IN NO EVENT WILL NETSPECTIVE OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THE SOFTWARE, EVEN
 * IF HE HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 */
package com.medigy.persist.model.order;

import com.medigy.persist.model.common.AbstractEntity;
import com.medigy.persist.model.product.Product;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class OrderItem extends AbstractEntity
{
    public static final String PK_COLUMN_NAME = "order_item_id";

    private Long orderItemId;
    private Long orderItemSeqId;
    private String itemDescription;
    private String shippingInstructions;
    private String comment;
    private Product product;
    private Long quantity;

    private OrderItem parentItem;
    private Order order;
    private List<OrderItem> childItems = new ArrayList<OrderItem>();

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getOrderItemId()
    {
        return orderItemId;
    }

    protected void setOrderItemId(final Long orderItemId)
    {
        this.orderItemId = orderItemId;
    }

    @Column(name = "order_item_seq_id")
    public Long getOrderItemSeqId()
    {
        return orderItemSeqId;
    }

    public void setOrderItemSeqId(final Long orderItemSeqId)
    {
        this.orderItemSeqId = orderItemSeqId;
    }

    @Column(length = 512, name = "item_description")
    public String getItemDescription()
    {
        return itemDescription;
    }

    public void setItemDescription(final String itemDescription)
    {
        this.itemDescription = itemDescription;
    }

    @Column(length = 512)
    public String getComment()
    {
        return comment;
    }

    public void setComment(final String comment)
    {
        this.comment = comment;
    }

    @ManyToOne
    @JoinColumn(name = Order.PK_COLUMN_NAME)
    public Order getOrder()
    {
        return order;
    }

    public void setOrder(final Order order)
    {
        this.order = order;
    }

    @ManyToOne
    @JoinColumn(name = Product.PK_COLUMN_NAME)
    public Product getProduct()
    {
        return product;
    }

    public void setProduct(final Product product)
    {
        this.product = product;
    }

    @Column(length = 512)
    public String getShippingInstructions()
    {
        return shippingInstructions;
    }

    public void setShippingInstructions(final String shippingInstructions)
    {
        this.shippingInstructions = shippingInstructions;
    }

    @ManyToOne
    @JoinColumn(referencedColumnName = PK_COLUMN_NAME, name = "parent_item_id")
    public OrderItem getParentItem()
    {
        return parentItem;
    }

    public void setParentItem(final OrderItem parentItem)
    {
        this.parentItem = parentItem;
    }

    @OneToMany(mappedBy = "parentItem", cascade = CascadeType.ALL)
    public List<OrderItem> getChildItems()
    {
        return childItems;
    }

    public void setChildItems(final List<OrderItem> childItems)
    {
        this.childItems = childItems;
    }

    @Transient
    public void addChildItem(final OrderItem item)
    {
        item.setParentItem(this);
        childItems.add(item);
    }

    public Long getQuantity()
    {
        return quantity;
    }

    public void setQuantity(final Long quantity)
    {
        this.quantity = quantity;
    }
}
