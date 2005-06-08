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
package com.medigy.persist.model.insurance;

import com.medigy.persist.model.common.AbstractTopLevelEntity;
import com.medigy.persist.reference.custom.insurance.FeeScheduleItemCostType;
import org.hibernate.validator.Size;
import org.hibernate.validator.Length;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.HashSet;
import java.util.Set;

@Entity
public class FeeScheduleItem extends AbstractTopLevelEntity
{
    public static final String PK_COLUMN_NAME = "fee_schedule_item_id";

    private Long feeScheduleItemId;
    private FeeSchedule feeSchedule;
    private String code; // CPT code???
    private String codeModifier;
    private String name;
    private String description;
    private Long sequenceNumber;
    private FeeScheduleItem parentFeeScheduleItem;
    private FeeScheduleItemCostType costType;
    private Long unitsAvailable;
    private Boolean isTaxable;
    private Float unitCost;
    private Long defaultUnits;

    private Set<FeeScheduleItem> childFeeSchduleItems = new HashSet<FeeScheduleItem>();

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getFeeScheduleItemId()
    {
        return feeScheduleItemId;
    }

    public void setFeeScheduleItemId(final Long feeScheduleItemId)
    {
        this.feeScheduleItemId = feeScheduleItemId;
    }

    @ManyToOne
    @JoinColumn(name = FeeSchedule.PK_COLUMN_NAME, nullable = false)
    public FeeSchedule getFeeSchedule()
    {
        return feeSchedule;
    }

    public void setFeeSchedule(final FeeSchedule feeSchedule)
    {
        this.feeSchedule = feeSchedule;
    }


    public String getCode()
    {
        return code;
    }

    public void setCode(final String code)
    {
        this.code = code;
    }

    @Column(length = 64)
    public String getCodeModifier()
    {
        return codeModifier;
    }

    public void setCodeModifier(final String codeModifier)
    {
        this.codeModifier = codeModifier;
    }

    @Column(length = 64)
    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public Long getSequenceNumber()
    {
        return sequenceNumber;
    }

    public void setSequenceNumber(final Long sequenceNumber)
    {
        this.sequenceNumber = sequenceNumber;
    }

    @ManyToOne
    @JoinColumn(name = "parent_item_id", referencedColumnName = PK_COLUMN_NAME)
    public FeeScheduleItem getParentFeeScheduleItem()
    {
        return parentFeeScheduleItem;
    }

    public void setParentFeeScheduleItem(final FeeScheduleItem parentFeeScheduleItem)
    {
        this.parentFeeScheduleItem = parentFeeScheduleItem;
    }

    @OneToMany(mappedBy = "parentFeeScheduleItem", cascade = CascadeType.ALL)
    public Set<FeeScheduleItem> getChildFeeSchduleItems()
    {
        return childFeeSchduleItems;
    }

    public void setChildFeeSchduleItems(final Set<FeeScheduleItem> childFeeSchduleItems)
    {
        this.childFeeSchduleItems = childFeeSchduleItems;
    }

    @Transient
    public void addChildFeeScheduleItem(final FeeScheduleItem childItem)
    {
        childItem.setParentFeeScheduleItem(this);
        childFeeSchduleItems.add(childItem);
    }

    @ManyToOne
    @JoinColumn(name = FeeScheduleItemCostType.PK_COLUMN_NAME)
    public FeeScheduleItemCostType getCostType()
    {
        return costType;
    }

    public void setCostType(final FeeScheduleItemCostType costType)
    {
        this.costType = costType;
    }

    @Size(max = 99999999)
    public Long getUnitsAvailable()
    {
        return unitsAvailable;
    }

    public void setUnitsAvailable(final Long unitsAvailable)
    {
        this.unitsAvailable = unitsAvailable;
    }

    /**
     * Gets the description of the item (overrides REF_CPT, REF_ICD, REF_HCPCS if not null)
     * @return
     */
    @Column(length = 1024)
    @Length(max = 1024)
    public String getDescription()
    {
        return description;
    }

    public void setDescription(final String description)
    {
        this.description = description;
    }

    public Boolean getTaxable()
    {
        return isTaxable;
    }

    public void setTaxable(final Boolean taxable)
    {
        isTaxable = taxable;
    }

    @Column(precision = 12, scale = 2)
    public Float getUnitCost()
    {
        return unitCost;
    }

    public void setUnitCost(final Float unitCost)
    {
        this.unitCost = unitCost;
    }

    @Column(columnDefinition = "NUMBER DEFAULT 1")
    public Long getDefaultUnits()
    {
        return defaultUnits;
    }

    public void setDefaultUnits(final Long defaultUnits)
    {
        this.defaultUnits = defaultUnits;
    }
}
