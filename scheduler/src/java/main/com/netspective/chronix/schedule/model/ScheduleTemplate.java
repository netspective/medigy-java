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
package com.netspective.chronix.schedule.model;

import java.util.Date;

import com.netspective.chronix.set.DaysOfMonthSet;
import com.netspective.chronix.set.DaysOfWeekSet;
import com.netspective.chronix.set.MonthsOfYearSet;
import com.netspective.chronix.set.YearsSet;

public interface ScheduleTemplate
{
    /**
     * Retrieve the unique identifier for this template instance
     *
     * @return An object which uniquely identifies a specific template
     */
    public Object getTemplateIdentifier();

    /**
     * Retrieve the description (any comments) for this template instance
     *
     * @return A simple description of the template (for the user interface)
     */
    public String getTemplateDescription();

    /**
     * Retrieve the schedule manager that is managing this template
     *
     * @return
     */
    public ScheduleManager getScheduleManager();

    /**
     * Retrieve the list of participants for which the template was defined
     *
     * @return A list of participants that must be part of any event scheduled using this template
     */
    public ScheduleParticipants getTemplateOwners();

    /**
     * Retrieve the list of participants that are also related to this template (may be required or optional, and this
     * list may include the template owners)
     *
     * @param includeOwners Set to true if the owners should be included in the list
     *
     * @return The participants that make up the template
     */
    public ScheduleParticipants getParticipants(boolean includeOwners);

    /**
     * Is this time block Available or Unavailable for scheduling events?
     *
     * @return True if events may be scheduled in the time block or false events should not be scheduled in this block
     */
    public boolean isAvailable();

    /**
     * Ascertain the starting applicability date for this template
     *
     * @return The date on or after which events should be allowed to use this template
     */
    public Date getEffectiveBeginDate();

    /**
     * Ascertain the ending applicability date for this template
     *
     * @return The date on before which events should be allowed to use this template
     */
    public Date getEffectiveEndDate();

    /**
     * Ascertain the starting time for the templates slots
     *
     * @return A date object whose time portion indicates the starting time (date portion should be ignored)
     */
    public Date getStartTime();

    /**
     * Ascertain the ending applicability date for this template
     *
     * @return A date object whose time portion indicates the end time (date portion should be ignored)
     */
    public Date getEndTime();

    /**
     * Get the participant types that this schedule template applies to
     *
     * @return The list of participant types that may be applied to this template (other than the owners)
     */
    public ScheduleParticipantTypes getParticipantTypes();

    /**
     * Get the event types that this schedule template applies to
     *
     * @return The list of events that may be applied to this template
     */
    public ScheduleEventTypes getEventTypes();

    /**
     * Retrieve the actual years that this template is applicable
     *
     * @return A IntSpan integer set with the year numbers that this template is applicable. The year values corresponds
     *         to Calendar.set(Calendar.YEAR).
     */
    public YearsSet getYears();

    /**
     * Retrieve the months of the year that this template is applicable
     *
     * @return A IntSpan integer set with the months of the year that this template is applicable. The first month
     *         begins with 0 and the month indexes correspond to Calendar.set(Calendar.MONTH).
     */
    public MonthsOfYearSet getMonthsOfTheYear();

    /**
     * Retrieve the days of the month that this template is applicable
     *
     * @return A IntSpan integer set with the days of the month that this template is applicable. The first day
     *         begins with 1 and the day indexes correspond to Calendar.set(Calendar.DAY_OF_MONTH).
     */
    public DaysOfMonthSet getDaysOfTheMonth();

    /**
     * Retrieve the days of the week that this template is applicable
     *
     * @return A IntSpan integer set with the day of week of the that this template is applicable. The first day
     *         begins with Calendar.MONDAY and the day indexes correspond to Calendar.set(Calendar.DAY_OF_WEEK).
     */
    public DaysOfWeekSet getDaysOfTheWeek();

    /**
     * Retrieve the slot width for parallel appointments
     *
     * @return
     */
    public int getSlotWidth();

    /**
     * Retrieve all the template slots associated with this template. This method will start with the beginDate and
     * go through the endDate to find all applicable dates for which this template may be applicable and create an
     * appropriate set of schedule templates slots for the start/end times for the given date range.
     *
     * @param beginDate
     * @param endDate
     *
     * @return A list of template slots calculated for the begin/end dates of this template
     */
    public ScheduleTemplateSlots getScheduleTemplateSlots(Date beginDate, Date endDate);
}
