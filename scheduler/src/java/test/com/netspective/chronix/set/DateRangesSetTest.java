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
package com.netspective.chronix.set;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.netspective.chronix.CalendarUtils;

import junit.framework.TestCase;

public class DateRangesSetTest extends TestCase
{
    private DateFormat dateFormat = new SimpleDateFormat("M/d/yyyy");
    protected Calendar calendar = Calendar.getInstance();
    protected CalendarUtils calendarUtils = new CalendarUtils(calendar);

    public Date createDate(int month, int day, int year)
    {
        calendar.set(year, month, day);
        return calendar.getTime();
    }

    public void testDateRangesSetSimple()
    {
        Date beginDate = createDate(0, 1, 2004);
        Date endDate = createDate(0, 15, 2004);

        DateRangesSet dateSet = new DateRangesSet(calendarUtils, beginDate, endDate, null, null, null, null);
        assertEquals("1/1/2004-1/15/2004", dateSet.toString(dateFormat, ", "));
    }

    public void testDateRangesSetJanThruApril()
    {
        Date beginDate = createDate(0, 1, 2004);
        Date endDate = createDate(11, 31, 2004);

        DateRangesSet dateSet = new DateRangesSet(calendarUtils, beginDate, endDate, null, new MonthsOfYearSet(Calendar.JANUARY + "-" + Calendar.APRIL), null, null);
        assertEquals("1/1/2004-4/30/2004", dateSet.toString(dateFormat, ", "));
    }

    public void testDateRangesSetJanThruAprilFirstFiveDays()
    {
        Date beginDate = createDate(0, 1, 2004);
        Date endDate = createDate(11, 31, 2004);

        DateRangesSet dateSet = new DateRangesSet(calendarUtils, beginDate, endDate, null, new MonthsOfYearSet(Calendar.JANUARY + "-" + Calendar.APRIL), new DaysOfMonthSet("1-5"), null);
        assertEquals("1/1/2004-1/5/2004, 2/1/2004-2/5/2004, 3/1/2004-3/5/2004, 4/1/2004-4/5/2004", dateSet.toString(dateFormat, ", "));
    }

    public void testDateRangesSetSeptOctMondays()
    {
        Date beginDate = createDate(0, 1, 2004);
        Date endDate = createDate(11, 31, 2004);

        DateRangesSet dateSet = new DateRangesSet(calendarUtils, beginDate, endDate, null, new MonthsOfYearSet(Calendar.SEPTEMBER + "-" + Calendar.OCTOBER), null, new DaysOfWeekSet(new int[]{
            Calendar.MONDAY
        }));
        assertEquals("9/6/2004, 9/13/2004, 9/20/2004, 9/27/2004, 10/4/2004, 10/11/2004, 10/18/2004, 10/25/2004", dateSet.toString(dateFormat, ", "));
    }
}
