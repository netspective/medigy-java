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
 * @author Shahid N. Shah
 */

/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.model.person;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.medigy.persist.model.common.AbstractEntity;
import com.medigy.persist.model.common.EffectiveDates;
import com.medigy.persist.reference.type.MaritalStatusType;

@Entity
@Table(name = "Person_Mar_Stat")
public class MaritalStatus extends AbstractEntity implements Comparable
{
    public static final String PK_COLUMN_NAME = "person_mar_stat_id";

    private Long identifier;

    private Person person;
    private MaritalStatusType type;
    private EffectiveDates effectiveDates = new EffectiveDates();

    public MaritalStatus()
    {
    }

    @Id(generate=GeneratorType.AUTO)
    @Column(name = MaritalStatus.PK_COLUMN_NAME)
    public Long getIdentifier()
    {
        return identifier;
    }

    protected void setIdentifier(final Long identifier)
    {
        this.identifier = identifier;
    }

    @ManyToOne
    @JoinColumn(name =Person.PK_COLUMN_NAME, nullable = false)
    public Person getPerson()
    {
        return person;
    }

    public void setPerson(final Person person)
    {
        this.person = person;
    }

    @ManyToOne
    @JoinColumn(name = "mar_stat_type_id", nullable = false)
    public MaritalStatusType getType()
    {
        return type;
    }

    public void setType(final MaritalStatusType type)
    {
        this.type = type;
    }

    public Date getFromDate()
    {
        return effectiveDates.getFromDate();
    }

    public void setFromDate(final Date fromDate)
    {
        effectiveDates.setFromDate(fromDate);
    }

    public Date getThroughDate()
    {
        return effectiveDates.getThroughDate();
    }

    public void setThroughDate(final Date throughDate)
    {
        effectiveDates.setThroughDate(throughDate);
    }

    @Transient
    public EffectiveDates getEffectiveDates()
    {
        return effectiveDates;
    }

    public int compareTo(Object o)
    {
        if(o == this)
            return 0;

        final MaritalStatus otherStatus = (MaritalStatus) o;
        final int effDatesResult = getEffectiveDates().compareTo(otherStatus.getEffectiveDates());
        if(effDatesResult == 0)
            return ((MaritalStatusType) getType()).compareTo(otherStatus.getType());
        else
            return effDatesResult;
    }

    public String toString()
    {
        return "MaritalStatus{" +
                "identifier=" + identifier +
                ", person_id=" + (person != null ? person.getPersonId() : null) +
                ", type=" + type +
                ", effectiveDates=" + effectiveDates +
                "}";
    }
}
