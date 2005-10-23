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

import com.medigy.persist.model.common.AbstractDateDurationEntity;
import com.medigy.persist.model.health.VisitType;
import com.medigy.persist.model.party.Facility;
import com.medigy.persist.model.person.Person;

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

/**
 * Class for describing fees for various medical services
 */
@Entity
public class FeeSchedule extends AbstractDateDurationEntity
{
    public static final String PK_COLUMN_NAME = "fee_schedule_id";

    private Long feeScheduleId;
    private String name;
    private String description;
    private String caption;
    private Long sequenceNumber;
    private Float rbrvsMultiplier;

    private Person person;
    private Facility facility;

    private Set<FeeScheduleItem> feeScheduleItems = new HashSet<FeeScheduleItem>();
    private Set<VisitType> visitTypes = new HashSet<VisitType>();

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getFeeScheduleId()
    {
        return feeScheduleId;
    }

    public void setFeeScheduleId(final Long feeScheduleId)
    {
        this.feeScheduleId = feeScheduleId;
    }

    /**
     * Gets the name of fee schedule
     * @return
     */
    @Column(length = 64, nullable = false)
    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    /**
     * Gets the description
     * @return
     */
    @Column(length = 128)
    public String getDescription()
    {
        return description;
    }

    public void setDescription(final String description)
    {
        this.description = description;
    }

    @Column(length =128)
    public String getCaption()
    {
        return caption;
    }

    public void setCaption(final String caption)
    {
        this.caption = caption;
    }

    @Column(name = "seq_number")
    public Long getSequenceNumber()
    {
        return sequenceNumber;
    }

    public void setSequenceNumber(final Long sequenceNumber)
    {
        this.sequenceNumber = sequenceNumber;
    }

    /**
     * gets the resource-based relative value scale (RBRVS) multiplier
     * @return
     */
    @Column(name = "rvrbs_multiplier")
    public Float getRbrvsMultiplier()
    {
        return rbrvsMultiplier;
    }

    public void setRbrvsMultiplier(final Float rbrvsMultiplier)
    {
        this.rbrvsMultiplier = rbrvsMultiplier;
    }

    /**
     * Gets the physician associated with this fee schedule
     * @return
     */
    @ManyToOne
    @JoinColumn(name = Person.PK_COLUMN_NAME)
    public Person getPerson()
    {
        return person;
    }

    public void setPerson(final Person person)
    {
        this.person = person;
    }

    /**
     * gets the facility associated with the fee schedule
     * @return
     */
    @ManyToOne
    @JoinColumn(name = Facility.PK_COLUMN_NAME)
    public Facility getFacility()
    {
        return facility;
    }

    public void setFacility(final Facility facility)
    {
        this.facility = facility;
    }

    @OneToMany(mappedBy = "feeSchedule", cascade = CascadeType.ALL)
    public Set<FeeScheduleItem> getFeeScheduleItems()
    {
        return feeScheduleItems;
    }

    public void setFeeScheduleItems(final Set<FeeScheduleItem> feeScheduleItems)
    {
        this.feeScheduleItems = feeScheduleItems;
    }

    @Transient
    public void addFeeScheduleItem(final FeeScheduleItem item)
    {
        item.setFeeSchedule(this);
        this.feeScheduleItems.add(item);
    }

    /**
     * Gets the associated encounter/visit type
     * @return
     */
    @OneToMany(mappedBy = "feeSchedule", cascade = CascadeType.ALL)
    public Set<VisitType> getVisitTypes()
    {
        return visitTypes;
    }

    public void setVisitTypes(final Set<VisitType> visitTypes)
    {
        this.visitTypes = visitTypes;
    }

}
