/*
 * Copyright (c) 2000-2004 Netspective Communications LLC. All rights reserved.
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
 *    used to endorse or appear in products derived from The Software without written consent of Netspective.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT,
 * ARE HEREBY DISCLAIMED.
 *
 * NETSPECTIVE AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE OR ANY THIRD PARTY AS A
 * RESULT OF USING OR DISTRIBUTING THE SOFTWARE. IN NO EVENT WILL NETSPECTIVE OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THE SOFTWARE, EVEN
 * IF IT HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 */

package com.netspective.chronix.schedule.impl;

import java.util.Calendar;
import java.util.Date;

import com.netspective.chronix.CalendarUtils;
import com.netspective.chronix.schedule.model.ScheduleEventTypes;
import com.netspective.chronix.schedule.model.ScheduleManager;
import com.netspective.chronix.schedule.model.ScheduleParticipantTypes;
import com.netspective.chronix.schedule.model.ScheduleParticipants;
import com.netspective.chronix.schedule.model.ScheduleTemplate;
import com.netspective.chronix.schedule.model.ScheduleTemplateSlots;
import com.netspective.chronix.set.DateRangesSet;
import com.netspective.chronix.set.DaysOfMonthSet;
import com.netspective.chronix.set.DaysOfWeekSet;
import com.netspective.chronix.set.MonthsOfYearSet;
import com.netspective.chronix.set.YearsSet;

public class DefaultScheduleTemplate implements ScheduleTemplate
{
    private Object templateIdentifier;
    private String templateDescription;
    private ScheduleManager scheduleManager;
    private ScheduleParticipants templateOwners, templateParticipants;
    private boolean available;
    private Date effectiveBeginDate, effectiveEndDate;
    private Date startTime, endTime;
    private ScheduleParticipantTypes participantTypes;
    private ScheduleEventTypes eventTypes;
    private YearsSet years;
    private MonthsOfYearSet monthsOfTheYear;
    private DaysOfMonthSet daysOfTheMonth;
    private DaysOfWeekSet daysOfTheWeek;
    private DateRangesSet applicableDateRangesSet;
    private int slotWidth;

    public DefaultScheduleTemplate(Object templateIdentifier,
                                   String templateDescription,
                                   ScheduleManager scheduleManager,
                                   ScheduleParticipants templateOwners,
                                   ScheduleParticipants templateParticipants,
                                   ScheduleParticipantTypes participantTypes,
                                   ScheduleEventTypes eventTypes,
                                   boolean available,
                                   Date effectiveBeginDate, Date effectiveEndDate,
                                   Date startTime, Date endTime,
                                   YearsSet years, MonthsOfYearSet monthsOfTheYear, DaysOfMonthSet daysOfTheMonth, DaysOfWeekSet daysOfTheWeek)
    {
        this.templateIdentifier = templateIdentifier == null ? new Integer(hashCode()) : templateIdentifier;
        this.templateDescription = templateDescription;
        this.scheduleManager = scheduleManager;
        this.templateOwners = templateOwners;
        this.templateParticipants = templateParticipants;
        this.participantTypes = participantTypes;
        this.eventTypes = eventTypes;
        this.available = available;
        this.effectiveBeginDate = effectiveBeginDate;
        this.effectiveEndDate = effectiveEndDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.years = years;
        this.monthsOfTheYear = monthsOfTheYear;
        this.daysOfTheMonth = daysOfTheMonth;
        this.daysOfTheWeek = daysOfTheWeek;
        this.applicableDateRangesSet =
        new DateRangesSet(scheduleManager.getCalendarUtils(),
                          effectiveBeginDate, effectiveEndDate, years, monthsOfTheYear, daysOfTheMonth,
                          daysOfTheWeek);

    }

    public Object getTemplateIdentifier()
    {
        return templateIdentifier;
    }

    public String getTemplateDescription()
    {
        return templateDescription;
    }

    public ScheduleManager getScheduleManager()
    {
        return scheduleManager;
    }

    public ScheduleParticipants getTemplateOwners()
    {
        return templateOwners;
    }

    public boolean isAvailable()
    {
        return available;
    }

    public ScheduleParticipants getParticipants(boolean includeOwners)
    {
        return templateParticipants;
    }

    public Date getEffectiveBeginDate()
    {
        return effectiveBeginDate;
    }

    public Date getEffectiveEndDate()
    {
        return effectiveEndDate;
    }

    public ScheduleParticipantTypes getParticipantTypes()
    {
        return participantTypes;
    }

    public Date getStartTime()
    {
        return startTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }

    public ScheduleEventTypes getEventTypes()
    {
        return eventTypes;
    }

    public YearsSet getYears()
    {
        return years;
    }

    public MonthsOfYearSet getMonthsOfTheYear()
    {
        return monthsOfTheYear;
    }

    public DaysOfMonthSet getDaysOfTheMonth()
    {
        return daysOfTheMonth;
    }

    public DaysOfWeekSet getDaysOfTheWeek()
    {
        return daysOfTheWeek;
    }

    public int getSlotWidth()
    {
        return slotWidth;
    }

    public DateRangesSet getApplicableDateRangesSet()
    {
        return applicableDateRangesSet;
    }

    public String toString()
    {
        CalendarUtils calendarUtils = getScheduleManager().getCalendarUtils();

        StringBuffer sb = new StringBuffer();
        sb.append(getClass().getName() + ": ");
        sb.append("id = " + getTemplateIdentifier() + ", ");
        sb.append("available = " + isAvailable() + ", ");
        sb.append("owners = " + getTemplateOwners() + ", ");
        sb.append("participants = " + getParticipants(false) + ", ");
        sb.append("participant types = " + getParticipantTypes() + ", ");
        sb.append("applicable from " + calendarUtils.formatDateOnly(getEffectiveBeginDate()) + " to " + calendarUtils.formatDateOnly(getEffectiveEndDate()) + ", ");
        sb.append("schedule from " + calendarUtils.formatTimeOnly(getStartTime()) + " to " + calendarUtils.formatTimeOnly(getEndTime()) + ", ");
        sb.append("years = " + getYears() + ", ");
        sb.append("months = " + getMonthsOfTheYear() + ", ");
        sb.append("days = " + getDaysOfTheMonth() + ", ");
        sb.append("days of week = " + getDaysOfTheWeek());

        return sb.toString();
    }

    public ScheduleTemplateSlots getScheduleTemplateSlots(Date beginDate, Date endDate)
    {
        ScheduleManager mgr = getScheduleManager();
        CalendarUtils calendarUtils = mgr.getCalendarUtils();
        Calendar calendar = calendarUtils.getCalendar();
        DateRangesSet applicableDates = getApplicableDateRangesSet();

        DefaultScheduleTemplateSlots result = new DefaultScheduleTemplateSlots(mgr);

        int beginDay = calendarUtils.getJulianDay(beginDate);
        int endDay = calendarUtils.getJulianDay(endDate);

        calendar.setTime(getStartTime());
        int startHours = calendar.get(Calendar.HOUR_OF_DAY), startMinutes = calendar.get(Calendar.MINUTE), startSeconds = calendar.get(Calendar.SECOND);

        calendar.setTime(getEndTime());
        int endHours = calendar.get(Calendar.HOUR_OF_DAY), endMinutes = calendar.get(Calendar.MINUTE), endSeconds = calendar.get(Calendar.SECOND);

        for(int day = beginDay; day <= endDay; day++)
        {
            if(!applicableDates.isMember(day))
                continue;

            // the starting date of the slot is the active date plus the starting time of the template
            Date slotBeginDate = calendarUtils.getDateFromJulianDay(day, startHours, startMinutes, startSeconds);

            // the ending date of the slot is the same date as the slot begin date but the ending time of the template
            Date slotEndDate = calendarUtils.createDate(slotBeginDate, endHours, endMinutes, endSeconds);

            result.addSlot(new DefaultScheduleTemplateSlot(mgr, this, slotBeginDate, slotEndDate));
        }

        return result;
    }
}
