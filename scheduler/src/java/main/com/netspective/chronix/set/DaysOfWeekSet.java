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
 * $Id: DaysOfWeekSet.java,v 1.2 2004-04-10 20:08:55 shahid.shah Exp $
 */

package com.netspective.chronix.set;

import java.util.Calendar;
import java.util.StringTokenizer;

/**
 * Type-safe wrapper to manage a set of days in a month (actual day numbers). The values that this set manages is the
 * same type as used by Calendar.get(Calendar.DAY_OF_WEEK).
 */
public class DaysOfWeekSet
{
    public class DayOfWeek
    {
        private int dayOfWeek;
        private boolean applicable;
        private boolean[] applicableInWeeksOfMonth = new boolean[10]; // we'll never need more than this

        public DayOfWeek(int dayOfWeek, boolean applicable)
        {
            setDayOfWeek(dayOfWeek);
            this.applicable = applicable;
            for(int i = 0; i < this.applicableInWeeksOfMonth.length; i++)
                this.applicableInWeeksOfMonth[i] = true;
        }

        public DayOfWeek(int dayOfWeek, boolean applicable, boolean[] applicableInWeeksOfMonth)
        {
            this(dayOfWeek, applicable);
            this.applicableInWeeksOfMonth = applicableInWeeksOfMonth;
        }

        /**
         * Initialize a day specification such as X or X:Y or X:Y;A-B where X is the day number that meets the range
         * specified by Calendar.DAY_OF_WEEK and Y is a week of the month in which X should be applicable. Ranges of
         * the week in the month (like A-B) may also be supplied.
         * @param spec
         */
        public DayOfWeek(String spec)
        {
            if(spec.indexOf(':') > 0)
            {
                // since we have days of week specified, we're going to start with an empty list (all inapplicable)
                for(int i = 0; i < this.applicableInWeeksOfMonth.length; i++)
                    this.applicableInWeeksOfMonth[i] = false;

                StringTokenizer itemTokenizer = new java.util.StringTokenizer(spec, ":");
                setDayOfWeek(Integer.parseInt(itemTokenizer.nextToken()));
                this.applicable = true;

                String applicableWeeksOfMonthText = itemTokenizer.nextToken();
                StringTokenizer wkOfMonthTokenizer = new java.util.StringTokenizer(applicableWeeksOfMonthText, ",");
                while(wkOfMonthTokenizer.hasMoreTokens())
                {
                    String wkOfMonthItemText = wkOfMonthTokenizer.nextToken();
                    if(wkOfMonthItemText.indexOf('-') > 0)
                    {
                        StringTokenizer rangeTokenizer = new java.util.StringTokenizer(wkOfMonthItemText, "-");
                        int startWeekOfMonth = Integer.parseInt(rangeTokenizer.nextToken());
                        int endWeekOfMonth = Integer.parseInt(rangeTokenizer.nextToken());
                        for(int i = startWeekOfMonth; i <= endWeekOfMonth; i++)
                            setApplicableInWeekOfMonth(i, true);
                    }
                    else
                        setApplicableInWeekOfMonth(Integer.parseInt(wkOfMonthItemText), true);
                }
            }
            else
            {
                setDayOfWeek(Integer.parseInt(spec));
                this.applicable = true;
                for(int i = 0; i < this.applicableInWeeksOfMonth.length; i++)
                    this.applicableInWeeksOfMonth[i] = true;
            }
        }

        public int getDayOfWeek()
        {
            return dayOfWeek;
        }

        protected void setDayOfWeek(int dayOfWeek)
        {
            this.dayOfWeek = dayOfWeek;
            if(this.dayOfWeek < Calendar.SUNDAY || this.dayOfWeek > Calendar.SATURDAY)
                throw new RuntimeException("Invalid day of the week: " + this.dayOfWeek);
        }

        public boolean isApplicable()
        {
            return applicable;
        }

        public void setApplicable(boolean applicable)
        {
            this.applicable = applicable;
        }

        public boolean[] getApplicableInWeeksOfMonth()
        {
            return applicableInWeeksOfMonth;
        }

        public boolean isApplicableInWeekOfMonth(int weekOfMonth)
        {
            return applicable &&
                   (weekOfMonth >=0 && weekOfMonth < applicableInWeeksOfMonth.length) &&
                   applicableInWeeksOfMonth[weekOfMonth];
        }

        public void setApplicableInWeekOfMonth(int weekOfMonth, boolean value)
        {
            if (weekOfMonth <0 || weekOfMonth >= applicableInWeeksOfMonth.length)
                throw new RuntimeException("Invalid week of month: must be between 0.." + applicableInWeeksOfMonth.length);

            applicableInWeeksOfMonth[weekOfMonth] = value;
        }
    }

    private DayOfWeek[] daysOfWeekSet = new DayOfWeek[Calendar.SATURDAY + 1]; // array is 0-based but the 0'th element is not used since Calendar.SUNDAY == 1

    public DaysOfWeekSet()
    {
        for(int i = Calendar.SUNDAY; i <= Calendar.SATURDAY; i++)
        {
            DayOfWeek dow = new DayOfWeek(i, false);
            daysOfWeekSet[i] = dow;
        }
    }

    public DaysOfWeekSet(int[] daysOfWeek)
    {
        this();
        for(int i = 0; i < daysOfWeek.length; i++)
        {
            DayOfWeek dow = daysOfWeekSet[daysOfWeek[i]];
            if(dow == null)
                throw new RuntimeException("Invalid day of week: " + daysOfWeek[i]);
            dow.setApplicable(true);
        }
    }

    public DaysOfWeekSet(String runList)
    {
        this();
        runList = IntSpan.stripWhitespace(runList);

        if (runList.equals("-"))
            return;  // empty set;

        StringTokenizer runListTokenizer = new java.util.StringTokenizer(runList, ",");
        while (runListTokenizer.hasMoreTokens())
        {
            String itemText = runListTokenizer.nextToken();
            if(itemText.indexOf('-') > 0)
            {
                StringTokenizer itemTokenizer = new java.util.StringTokenizer(itemText, "-");
                setRange(itemTokenizer.nextToken(), itemTokenizer.nextToken());
            }
            else
            {
                setSingle(itemText);
            }
        }
    }

    public void setSingle(String spec)
    {
        DayOfWeek dow = new DayOfWeek(spec);
        daysOfWeekSet[dow.getDayOfWeek()] = dow;
    }

    public void setRange(String start, String end)
    {
        DayOfWeek startDow = new DayOfWeek(start);
        DayOfWeek endDow = new DayOfWeek(end);

        daysOfWeekSet[startDow.getDayOfWeek()] = startDow;
        daysOfWeekSet[endDow.getDayOfWeek()] = endDow;

        for(int i = startDow.getDayOfWeek()+1; i < endDow.getDayOfWeek(); i++)
        {
            DayOfWeek dow = new DayOfWeek(i, true, startDow.getApplicableInWeeksOfMonth());
            daysOfWeekSet[dow.getDayOfWeek()] = dow;
        }
    }

    public boolean isMember(int calendarDayOfWeek)
    {
        return daysOfWeekSet[calendarDayOfWeek].isApplicable();
    }

    public boolean isMember(int calendarDayOfWeek, int calendarWeekInMonth)
    {
        return daysOfWeekSet[calendarDayOfWeek].isApplicableInWeekOfMonth(calendarWeekInMonth);
    }

    public String toString()
    {
        return daysOfWeekSet.toString();
    }
}
