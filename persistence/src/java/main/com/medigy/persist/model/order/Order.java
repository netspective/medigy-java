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

import com.medigy.persist.model.common.AbstractTopLevelEntity;
import com.medigy.persist.reference.custom.order.OrderType;

import javax.persistence.Basic;
import javax.persistence.TemporalType;
import javax.persistence.Id;
import javax.persistence.GeneratorType;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.Transient;
import javax.persistence.Entity;
import javax.persistence.EmbeddableSuperclass;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import org.hibernate.validator.NotNull;

@Entity
@Inheritance(discriminatorValue = "Order", strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "Product_Order")
public class Order extends AbstractTopLevelEntity
{
    public static final String PK_COLUMN_NAME = "order_id";

    private Long orderId;
    private String description;
    private Date orderDate;
    private Date entryDate;
    private Date filledDate;
    private OrderType type;

    private List<OrderRole> roles = new ArrayList<OrderRole>();
    private List<OrderItem> items = new ArrayList<OrderItem>();

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getOrderId()
    {
        return orderId;
    }

    public void setOrderId(final Long orderId)
    {
        this.orderId = orderId;
    }

    @ManyToOne
    @JoinColumn(name = OrderType.PK_COLUMN_NAME, nullable = false)
    @NotNull
    public OrderType getType()
    {
        return type;
    }

    public void setType(final OrderType type)
    {
        this.type = type;
    }

    /**
     * Date the order was received
     * @return
     */
    @Basic(temporalType = TemporalType.TIMESTAMP)
    public Date getOrderDate()
    {
        return orderDate;
    }

    public void setOrderDate(final Date orderDate)
    {
        this.orderDate = orderDate;
    }

    /**
     * Date the order was entered into the system
     * @return
     */
    @Basic(temporalType = TemporalType.TIMESTAMP)
    public Date getEntryDate()
    {
        return entryDate;
    }

    public void setEntryDate(final Date entryDate)
    {
        this.entryDate = entryDate;
    }

    /**
     * Date the order was &quot;filled&quot;
     * @return
     */
    @Basic(temporalType = TemporalType.TIMESTAMP)
    public Date getFilledDate()
    {
        return filledDate;
    }

    public void setFilledDate(final Date filledDate)
    {
        this.filledDate = filledDate;
    }

    @Column(length = 512)
    public String getDescription()
    {
        return description;
    }

    public void setDescription(final String description)
    {
        this.description = description;
    }

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    public List<OrderItem> getItems()
    {
        return items;
    }

    public void setItems(final List<OrderItem> items)
    {
        this.items = items;
    }

    @Transient
    public void addItem(final OrderItem item)
    {
        item.setOrder(this);
        items.add(item);
    }

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    public List<OrderRole> getRoles()
    {
        return roles;
    }

    public void setRoles(final List<OrderRole> roles)
    {
        this.roles = roles;
    }
}