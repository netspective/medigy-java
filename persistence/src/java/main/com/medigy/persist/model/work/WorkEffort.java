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
package com.medigy.persist.model.work;

import com.medigy.persist.model.common.AbstractTopLevelEntity;
import com.medigy.persist.model.common.EffectiveDates;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.Date;

@Entity
public class WorkEffort extends AbstractTopLevelEntity
{
    public static final String PK_COLUMN_NAME = "work_effort_id";

    private Long workId;
    private String workName;
    private String description;
    private Float totalDollarsAllowed;
    private Float totalHoursAllowed;
    private Float estimatedHours;
    private Float actualHours;

    private EffectiveDates scheduledDates = new EffectiveDates();
    private EffectiveDates actualDates = new EffectiveDates();

    public WorkEffort()
    {
    }

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getWorkId()
    {
        return workId;
    }

    protected void setWorkId(final Long workId)
    {
        this.workId = workId;
    }

    @Column(length = 100, nullable = false, name = "work_name")
    public String getWorkName()
    {
        return workName;
    }

    public void setWorkName(final String workName)
    {
        this.workName = workName;
    }

    @Column(length = 500)
    public String getDescription()
    {
        return description;
    }

    public void setDescription(final String description)
    {
        this.description = description;
    }

    @Column(name = "total_dollars_allowed")
    public Float getTotalDollarsAllowed()
    {
        return totalDollarsAllowed;
    }

    public void setTotalDollarsAllowed(final Float totalDollarsAllowed)
    {
        this.totalDollarsAllowed = totalDollarsAllowed;
    }

    @Column(name = "total_hours_allowed")
    public Float getTotalHoursAllowed()
    {
        return totalHoursAllowed;
    }

    public void setTotalHoursAllowed(final Float totalHoursAllowed)
    {
        this.totalHoursAllowed = totalHoursAllowed;
    }

    @Column(name = "estimated_hours")
    public Float getEstimatedHours()
    {
        return estimatedHours;
    }

    public void setEstimatedHours(final Float estimatedHours)
    {
        this.estimatedHours = estimatedHours;
    }

    @Basic(temporalType = TemporalType.DATE)
    @Column(name = "scheduled_start_date")
    public Date getScheduledStartDate()
    {
        return scheduledDates.getFromDate();
    }

    public void setScheduledStartDate(final Date scheduledStartDate)
    {
        this.scheduledDates.setFromDate(scheduledStartDate);
    }

    @Basic(temporalType = TemporalType.DATE)
    @Column(name = "scheduled_end_date")
    public Date getScheduledEndDate()
    {
        return scheduledDates.getThroughDate();
    }

    public void setScheduledEndDate(final Date scheduledEndDate)
    {
        this.scheduledDates.setThroughDate(scheduledEndDate);
    }

    @Transient
    public EffectiveDates getScheduledDates()
    {
        return scheduledDates;
    }

    @Basic(temporalType = TemporalType.DATE)
    @Column(name = "actual_start_date")
    public Date getActualStartDate()
    {
        return actualDates.getFromDate();
    }

    public void setActualStartDate(final Date startDate)
    {
        this.actualDates.setFromDate(startDate);
    }

    @Basic(temporalType = TemporalType.DATE)
    @Column(name = "actual_end_date")
    public Date getActualEndDate()
    {
        return actualDates.getThroughDate();
    }

    public void setActualEndDate(final Date endDate)
    {
        this.actualDates.setThroughDate(endDate);
    }

    @Transient
    public EffectiveDates getActualDates()
    {
        return actualDates;
    }

    @Column(name = "actual_hours")
    public Float getActualHours()
    {
        return actualHours;
    }

    public void setActualHours(final Float actualHours)
    {
        this.actualHours = actualHours;
    }
}
