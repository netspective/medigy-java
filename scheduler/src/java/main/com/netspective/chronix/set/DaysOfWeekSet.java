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

import java.util.Calendar;
import java.util.StringTokenizer;

public class DaysOfWeekSet
{
    public class DayOfWeek
    {
        private int dayOfWeek;
        private boolean applicable;
        private boolean applicableInAllWeeksOfMonth;
        private boolean[] applicableInWeeksOfMonth = new boolean[10]; // we'll never need more than this

        public DayOfWeek(int dayOfWeek, boolean applicable)
        {
            setDayOfWeek(dayOfWeek);
            this.applicable = applicable;
            this.applicableInAllWeeksOfMonth = true;
            for(int i = 0; i < this.applicableInWeeksOfMonth.length; i++)
                this.applicableInWeeksOfMonth[i] = true;
        }

        public DayOfWeek(int dayOfWeek, boolean applicable, boolean[] applicableInWeeksOfMonth)
        {
            this(dayOfWeek, applicable);
            this.applicableInAllWeeksOfMonth = false;
            this.applicableInWeeksOfMonth = applicableInWeeksOfMonth;
        }

        /**
         * Initialize a day specification such as X or X:Y or X:Y;A;B where X is the day number that meets the range
         * specified by Calendar.DAY_OF_WEEK and Y is a week of the month in which X should be applicable.
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
                this.applicableInAllWeeksOfMonth = false;

                String applicableWeeksOfMonthText = itemTokenizer.nextToken();
                StringTokenizer wkOfMonthTokenizer = new java.util.StringTokenizer(applicableWeeksOfMonthText, ";");
                while(wkOfMonthTokenizer.hasMoreTokens())
                    setApplicableInWeekOfMonth(Integer.parseInt(wkOfMonthTokenizer.nextToken()), true);
            }
            else
            {
                setDayOfWeek(Integer.parseInt(spec));
                this.applicable = true;
                this.applicableInAllWeeksOfMonth = true;
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

        public boolean isApplicableInAllWeeksOfMonth()
        {
            return applicableInAllWeeksOfMonth;
        }

        public boolean isApplicableInWeekOfMonth(int weekOfMonth)
        {
            return applicable &&
                   (weekOfMonth >= 0 && weekOfMonth < applicableInWeeksOfMonth.length) &&
                   applicableInWeeksOfMonth[weekOfMonth];
        }

        public void setApplicableInWeekOfMonth(int weekOfMonth, boolean value)
        {
            if(weekOfMonth < 0 || weekOfMonth >= applicableInWeeksOfMonth.length)
                throw new RuntimeException("Invalid week of month: must be between 0.." + applicableInWeeksOfMonth.length);

            applicableInWeeksOfMonth[weekOfMonth] = value;
        }

        public String toString()
        {
            if(applicableInAllWeeksOfMonth)
                return Integer.toString(dayOfWeek);
            else
            {
                StringBuffer applWeeksInMonth = new StringBuffer();
                for(int i = 0; i < this.applicableInWeeksOfMonth.length; i++)
                {
                    if(this.applicableInWeeksOfMonth[i])
                    {
                        if(applWeeksInMonth.length() > 0)
                            applWeeksInMonth.append(";");
                        applWeeksInMonth.append(i);
                    }
                }

                if(applWeeksInMonth.length() > 0)
                    return dayOfWeek + ":" + applWeeksInMonth;
                else
                    return Integer.toString(dayOfWeek);
            }
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

        if(runList.equals("-"))
            return;  // empty set;

        StringTokenizer runListTokenizer = new java.util.StringTokenizer(runList, ",");
        while(runListTokenizer.hasMoreTokens())
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

        for(int i = startDow.getDayOfWeek() + 1; i < endDow.getDayOfWeek(); i++)
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
        StringBuffer sb = new StringBuffer();
        for(int i = Calendar.SUNDAY; i <= Calendar.SATURDAY; i++)
        {
            if(daysOfWeekSet[i].isApplicable())
            {
                if(sb.length() > 0) sb.append(",");
                sb.append(daysOfWeekSet[i]);
            }
        }
        return sb.toString();
    }
}
