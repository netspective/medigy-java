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

/**
 * $Id: ScheduleManagerTest.java,v 1.4 2004-04-14 20:55:41 shahid.shah Exp $
 */

package com.netspective.chronix.schedule;

import java.util.Calendar;
import java.util.Date;

import com.netspective.chronix.CalendarUtils;
import com.netspective.chronix.schedule.mock.MockScheduleElementProvider;
import com.netspective.chronix.schedule.model.ScheduleEvent;
import com.netspective.chronix.schedule.model.ScheduleEvents;
import com.netspective.chronix.schedule.model.ScheduleSlots;
import com.netspective.chronix.schedule.model.ScheduleTemplate;
import com.netspective.chronix.schedule.model.ScheduleTemplateSlot;
import com.netspective.chronix.schedule.model.ScheduleTemplateSlots;
import com.netspective.chronix.schedule.model.ScheduleTemplates;
import com.netspective.chronix.set.MinuteRangesSet;

public class ScheduleManagerTest extends ScheduleTestCase
{
    public void testScheduleEvents()
    {
        CalendarUtils calendarUtils = getCalendarUtils();
        Date beginDate = calendarUtils.createDate(Calendar.JANUARY, 1, 2004);
        Date endDate = calendarUtils.createDate(Calendar.JANUARY, 1, 2004);

        int beginDay = calendarUtils.getJulianDay(beginDate);
        int endDay = calendarUtils.getJulianDay(endDate);

        ScheduleEvents scheduleEvents = getEventProvider().getScheduledEvents(getScheduleManager(), beginDate, endDate);
        ScheduleEvent[] scheduleEventsList = scheduleEvents.getScheduleEvents();
        assertEquals(MockScheduleElementProvider.MOCK_EVENT_HOURS.length, scheduleEventsList.length);

        for(int day = beginDay; day <= endDay; day++)
        {
            Date julianDate = calendarUtils.getDateFromJulianDay(day);

            for(int i = 0; i < MockScheduleElementProvider.MOCK_EVENT_HOURS.length; i++)
            {
                ScheduleEvent mockEvent = scheduleEventsList[i];

                int[] hm = MockScheduleElementProvider.MOCK_EVENT_HOURS[i];

                assertEquals(calendarUtils.createDate(julianDate, hm[0], hm[1]), mockEvent.getBeginDate());
                assertEquals(calendarUtils.createDate(julianDate, hm[2], hm[3]), mockEvent.getEndDate());

                MinuteRangesSet minuteRangesSet = mockEvent.getMinutesSet();
                assertFalse(minuteRangesSet.isMultipleDays());
                assertTrue(minuteRangesSet.isMember(hm[0], hm[1]));
                assertTrue(minuteRangesSet.isMember(hm[2], hm[3]));
            }
        }
    }

    public void testScheduleTemplateSlots()
    {
        CalendarUtils calendarUtils = getCalendarUtils();

        Date effBeginDate = calendarUtils.createDate(Calendar.JANUARY, 1, 2004);
        Date effEndDate = calendarUtils.createDate(Calendar.DECEMBER, 31, 2004);

        ScheduleTemplates scheduleTemplates = getTemplateProvider().getScheduleTemplates(getScheduleManager(), effBeginDate, effEndDate, null);
        ScheduleTemplate[] scheduleTemplatesList = scheduleTemplates.getScheduleTemplates();
        assertEquals(5, scheduleTemplatesList.length);

        Date slotsBeginDate = calendarUtils.createDate(Calendar.JANUARY, 1, 2004);
        Date slotsEndDate = calendarUtils.createDate(Calendar.JANUARY, 3, 2004);

        ScheduleTemplateSlots slots = scheduleTemplates.getScheduleTemplateSlots(slotsBeginDate, slotsEndDate);

        ScheduleTemplateSlot[] slotsList = slots.getScheduleTemplateSlots();
        assertEquals(8, slotsList.length);

        ScheduleTemplateSlot mockSlot = slotsList[0];
        assertTrue(mockSlot.getScheduleTemplate().isAvailable());
        assertEquals(calendarUtils.createDate(Calendar.JANUARY, 1, 2004,  8,  0,  0), mockSlot.getBeginDate());
        assertEquals(calendarUtils.createDate(Calendar.JANUARY, 1, 2004, 16, 59, 59), mockSlot.getEndDate());
        assertEquals(540, mockSlot.getMinutesSet().size());

        mockSlot = slotsList[1];
        assertFalse(mockSlot.getScheduleTemplate().isAvailable());
        assertEquals(calendarUtils.createDate(Calendar.JANUARY, 1, 2004, 10,  0,  0), mockSlot.getBeginDate());
        assertEquals(calendarUtils.createDate(Calendar.JANUARY, 1, 2004, 10, 14, 59), mockSlot.getEndDate());
        assertEquals(15, mockSlot.getMinutesSet().size());

        mockSlot = slotsList[2];
        assertFalse(mockSlot.getScheduleTemplate().isAvailable());
        assertEquals(calendarUtils.createDate(Calendar.JANUARY, 1, 2004, 12,  0,  0), mockSlot.getBeginDate());
        assertEquals(calendarUtils.createDate(Calendar.JANUARY, 1, 2004, 13, 29, 59), mockSlot.getEndDate());
        assertEquals(90, mockSlot.getMinutesSet().size());

        mockSlot = slotsList[3];
        assertFalse(mockSlot.getScheduleTemplate().isAvailable());
        assertEquals(calendarUtils.createDate(Calendar.JANUARY, 1, 2004, 15,  0,  0), mockSlot.getBeginDate());
        assertEquals(calendarUtils.createDate(Calendar.JANUARY, 1, 2004, 15, 14, 59), mockSlot.getEndDate());
        assertEquals(15, mockSlot.getMinutesSet().size());

        mockSlot = slotsList[4];
        assertTrue(mockSlot.getScheduleTemplate().isAvailable());
        assertEquals(calendarUtils.createDate(Calendar.JANUARY, 2, 2004,  8,  0,  0), mockSlot.getBeginDate());
        assertEquals(calendarUtils.createDate(Calendar.JANUARY, 2, 2004, 16, 59, 59), mockSlot.getEndDate());
        assertEquals(540, mockSlot.getMinutesSet().size());

        mockSlot = slotsList[5];
        assertFalse(mockSlot.getScheduleTemplate().isAvailable());
        assertEquals(calendarUtils.createDate(Calendar.JANUARY, 2, 2004, 10,  0,  0), mockSlot.getBeginDate());
        assertEquals(calendarUtils.createDate(Calendar.JANUARY, 2, 2004, 10, 14, 59), mockSlot.getEndDate());
        assertEquals(15, mockSlot.getMinutesSet().size());

        mockSlot = slotsList[6];
        assertFalse(mockSlot.getScheduleTemplate().isAvailable());
        assertEquals(calendarUtils.createDate(Calendar.JANUARY, 2, 2004, 12,  0,  0), mockSlot.getBeginDate());
        assertEquals(calendarUtils.createDate(Calendar.JANUARY, 2, 2004, 13, 29, 59), mockSlot.getEndDate());
        assertEquals(90, mockSlot.getMinutesSet().size());

        mockSlot = slotsList[7];
        assertFalse(mockSlot.getScheduleTemplate().isAvailable());
        assertEquals(calendarUtils.createDate(Calendar.JANUARY, 2, 2004, 15,  0,  0), mockSlot.getBeginDate());
        assertEquals(calendarUtils.createDate(Calendar.JANUARY, 2, 2004, 15, 14, 59), mockSlot.getEndDate());
        assertEquals(15, mockSlot.getMinutesSet().size());

        ScheduleSlots.ResolvedSlotMinutes resolvedSlotMinutes = slots.getResolvedSlotMinutes();
        MinuteRangesSet open = resolvedSlotMinutes.getOpenMinutes();
        MinuteRangesSet closed = resolvedSlotMinutes.getClosedMinutes();

        assertTrue(open.isMultipleDays());
        assertEquals(resolvedSlotMinutes.getEarliestSlot().getBeginDate(), open.getBaselineDate());
        assertEquals("0d 08:00-0d 16:59, 1d 08:00-1d 16:59", open.toString());

        assertTrue(closed.isMultipleDays());
        assertEquals(resolvedSlotMinutes.getEarliestSlot().getBeginDate(), closed.getBaselineDate());
        assertEquals("0d 10:00-0d 10:14, 0d 12:00-0d 13:29, 0d 15:00-0d 15:14, 1d 10:00-1d 10:14, 1d 12:00-1d 13:29, 1d 15:00-1d 15:14", closed.toString());
    }
}
