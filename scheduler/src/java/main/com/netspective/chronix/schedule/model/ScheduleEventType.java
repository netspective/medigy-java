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

public interface ScheduleEventType
{
    /**
     * Retrieve the unique identifier for this event type
     *
     * @return An object that can uniquely identify this event type
     */
    public Object getEventTypeIdentifier();

    /**
     * Retrieve the schedule manager that is managing this template
     */
    public ScheduleManager getScheduleManager();

    /**
     * Get the participants of this event type
     *
     * @return the required and optional participants of the event
     */
    public ScheduleParticipants getParticipants();

    /**
     * Get the participants types applicable to this event type
     *
     * @return the required and optional participant types of the event
     */
    public ScheduleParticipantTypes getParticipantsTypes();

    /**
     * Retrieve the number of minutes this event should consume in the schedule
     *
     * @return a non-zero cardinal value representing how many minutes an event of this type should block in the schedule
     */
    public int getEventDurationMinutes();

    /**
     * Retrieve the lead time in minutes required before an event of this type is scheduled
     *
     * @return 0 if no lead time is required, non-zero cardinal value if a lead time is necessary
     */
    public int getEventLeadTimeMinutes();

    /**
     * Retrieve the lag time in minutes required after an event of this type before the next event should occur
     *
     * @return 0 if no lag time is required, non-zero cardinal value if a lag time is necessary
     */
    public int getEventLagTimeMinutes();

    /**
     * Ascertain whether back-to-back events of this particular type are allowed.
     *
     * @return true if events of this type may be adjacent to each other, false if no adjacent events allowed
     */
    public boolean isAllowBacktoBackEvents();

    /**
     * Retrieve the multiple simultaneous events of this type allowed
     *
     * @return 0 if no simultaneous events allow or non-zero cardinal value if simultaneous events of this type are allowed
     */
    public int getSimultaneousEventsLimit();

    /**
     * Retrieve the limit of the number of events of this type in AM hours
     *
     * @return 0 if there is no limit or non-zero cardinal value if there is a limit
     */
    public int getMorningEventsLimit();

    /**
     * Retrieve the limit of the number of events of this type in PM hours
     *
     * @return 0 if there is no limit or non-zero cardinal value if there is a limit
     */
    public int getAfternoonEventsLimit();

    /**
     * Retrieve the limit of number of events of this type for the day
     */
    public int getDayEventsLimit();

    /**
     * Retrieve the event width for parallel events
     *
     * @return The number of minutes
     */
    public int getEventWidth();
}
