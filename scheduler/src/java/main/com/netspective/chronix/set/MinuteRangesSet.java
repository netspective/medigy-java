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
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import com.netspective.chronix.CalendarUtils;
import com.netspective.chronix.set.IntSpan.ElementFormatter;
import com.netspective.chronix.set.IntSpan.IntSpanIterator;
import com.netspective.chronix.set.IntSpan.Mappable;
import com.netspective.chronix.set.IntSpan.Testable;

/**
 * A type-safe wrapper of the IntSpan object designed to manage minutes of a day treated as an integer set. Can handle
 * multiple days if required.
 */
public class MinuteRangesSet implements Set
{
    public static final int MINUTES_PER_HOUR = 60;
    public static final int HOURS_PER_DAY = 24;
    public static final int MINUTES_PER_DAY = MINUTES_PER_HOUR * HOURS_PER_DAY;

    private CalendarUtils calendarUtils;
    private boolean multipleDays;
    private Date baselineDate;
    private IntSpan minutesSet = new IntSpan();

    public MinuteRangesSet(CalendarUtils calendarUtils)
    {
        this.calendarUtils = calendarUtils;
        this.multipleDays = false;
    }

    public MinuteRangesSet(CalendarUtils calendarUtils, Date baselineDate, boolean multipleDays)
    {
        this.calendarUtils = calendarUtils;
        this.baselineDate = baselineDate;
        this.multipleDays = multipleDays;
    }

    public MinuteRangesSet(CalendarUtils calendarUtils, Date baselineDate, boolean multipleDays, IntSpan minutesSet)
    {
        this(calendarUtils, baselineDate, multipleDays);
        this.minutesSet = minutesSet;
    }

    public Date getBaselineDate()
    {
        return baselineDate;
    }

    public boolean isMultipleDays()
    {
        return multipleDays;
    }

    /**
     * Create the minutes range in this class using the date/time specified in the beginDate and endDate instances.
     *
     * @param beginDate The starting date/time of the minutes range
     * @param endDate   The ending date/time of the minutes range
     */
    public void applyDateRange(Date beginDate, Date endDate)
    {
        Calendar calendar = calendarUtils.getCalendar();

        calendar.setTime(beginDate);
        int beginHours = calendar.get(Calendar.HOUR_OF_DAY);
        int beginMinutes = calendar.get(Calendar.MINUTE);

        calendar.setTime(endDate);
        int endHours = calendar.get(Calendar.HOUR_OF_DAY);
        int endMinutes = calendar.get(Calendar.MINUTE);

        if(!multipleDays)
        {
            minutesSet.addClosed((beginHours * MINUTES_PER_HOUR) + beginMinutes,
                                 (endHours * MINUTES_PER_HOUR) + endMinutes);
        }
        else
        {
            int baselineDay = calendarUtils.getJulianDay(baselineDate);
            int beginDay = calendarUtils.getJulianDay(beginDate) - baselineDay;
            int endDay = calendarUtils.getJulianDay(endDate) - baselineDay;

            minutesSet.addClosed((MINUTES_PER_DAY * beginDay) + (beginHours * MINUTES_PER_HOUR) + beginMinutes,
                                 (MINUTES_PER_DAY * endDay) + (endHours * MINUTES_PER_HOUR) + endMinutes);
        }
    }

    public boolean add(Object o)
    {
        return minutesSet.add(o);
    }

    public void clear()
    {
        minutesSet.clear();
    }

    public boolean contains(Object o)
    {
        return minutesSet.contains(o);
    }

    public boolean remove(Object o)
    {
        return minutesSet.remove(o);
    }

    public boolean addAll(Collection c)
    {
        return minutesSet.addAll(c);
    }

    public boolean containsAll(Collection c)
    {
        return minutesSet.containsAll(c);
    }

    public boolean removeAll(Collection c)
    {
        return minutesSet.removeAll(c);
    }

    public boolean retainAll(Collection c)
    {
        return minutesSet.retainAll(c);
    }

    public java.util.Iterator iterator()
    {
        return minutesSet.iterator();
    }

    public Object clone()
    {
        return minutesSet.clone();
    }

    public String runList()
    {
        return minutesSet.runList();
    }

    public String getFormattedRunList(ElementFormatter formatter)
    {
        return minutesSet.getFormattedRunList(formatter);
    }

    public int[] getElements()
    {
        return minutesSet.getElements();
    }

    public Object[] toArray()
    {
        return minutesSet.toArray();
    }

    public Object[] toArray(Object list[])
    {
        return minutesSet.toArray(list);
    }

    public int size()
    {
        return minutesSet.size();
    }

    public boolean isEmpty()
    {
        return minutesSet.isEmpty();
    }

    public boolean isFinite()
    {
        return minutesSet.isFinite();
    }

    public boolean isNegInfite()
    {
        return minutesSet.isNegInfite();
    }

    public boolean isPosInfite()
    {
        return minutesSet.isPosInfite();
    }

    public boolean isInfinite()
    {
        return minutesSet.isInfinite();
    }

    public boolean isUniversal()
    {
        return minutesSet.isUniversal();
    }

    public boolean isMember(int hour, int minute)
    {
        return minutesSet.isMember((hour * MINUTES_PER_HOUR) + minute);
    }

    public boolean isMember(int n)
    {
        return minutesSet.isMember(n);
    }

    public void add(int hour, int minute)
    {
        minutesSet.add((hour * MINUTES_PER_HOUR) + minute);
    }

    public void add(int n)
    {
        minutesSet.add(n);
    }

    public void remove(int hour, int minute)
    {
        minutesSet.remove((hour * MINUTES_PER_HOUR) + minute);
    }

    public void remove(int n)
    {
        minutesSet.remove(n);
    }

    public Integer getMinInteger()
    {
        return minutesSet.getMinInteger();
    }

    public Integer getMaxInteger()
    {
        return minutesSet.getMaxInteger();
    }

    public int getMin()
    {
        return minutesSet.getMin();
    }

    public int getMax()
    {
        return minutesSet.getMax();
    }

    public MinuteRangesSet grep(Testable predicate)
    {
        return new MinuteRangesSet(calendarUtils, baselineDate, multipleDays, minutesSet.grep(predicate));
    }

    public MinuteRangesSet map(Mappable trans)
    {
        return new MinuteRangesSet(calendarUtils, baselineDate, multipleDays, minutesSet.map(trans));
    }

    public IntSpanIterator first()
    {
        return minutesSet.first();
    }

    public IntSpanIterator last()
    {
        return minutesSet.last();
    }

    public IntSpanIterator start(int n)
    {
        return minutesSet.start(n);
    }

    public MinuteDateSpanIterator first(Date relativeToDate)
    {
        return new MinuteDateSpanIterator(minutesSet.first(), relativeToDate);
    }

    public MinuteDateSpanIterator last(Date relativeToDate)
    {
        return new MinuteDateSpanIterator(minutesSet.last(), relativeToDate);
    }

    public MinuteDateSpanIterator start(int n, Date relativeToDate)
    {
        return new MinuteDateSpanIterator(minutesSet.start(n), relativeToDate);
    }

    public String toString(String delim)
    {
        if(isMultipleDays())
            return minutesSet.getFormattedRunList(new MultiDayElementFormatter(delim));
        else
            return minutesSet.getFormattedRunList(new SingleDayElementFormatter(delim));
    }

    public String toString()
    {
        return toString(", ");
    }

    public static MinuteRangesSet union(MinuteRangesSet a, MinuteRangesSet b)
    {
        return new MinuteRangesSet(a.calendarUtils, a.baselineDate, a.multipleDays, IntSpan.union(a.minutesSet, b.minutesSet));
    }

    public static MinuteRangesSet intersect(MinuteRangesSet a, MinuteRangesSet b)
    {
        return new MinuteRangesSet(a.calendarUtils, a.baselineDate, a.multipleDays, IntSpan.intersect(a.minutesSet, b.minutesSet));
    }

    public static MinuteRangesSet diff(MinuteRangesSet a, MinuteRangesSet b)
    {
        return new MinuteRangesSet(a.calendarUtils, a.baselineDate, a.multipleDays, IntSpan.diff(a.minutesSet, b.minutesSet));
    }

    public static MinuteRangesSet xor(MinuteRangesSet a, MinuteRangesSet b)
    {
        return new MinuteRangesSet(a.calendarUtils, a.baselineDate, a.multipleDays, IntSpan.xor(a.minutesSet, b.minutesSet));
    }

    public static MinuteRangesSet complement(MinuteRangesSet s)
    {
        return new MinuteRangesSet(s.calendarUtils, s.baselineDate, s.multipleDays, IntSpan.complement(s.minutesSet));
    }

    public static boolean superset(MinuteRangesSet a, MinuteRangesSet b)
    {
        return IntSpan.superset(a.minutesSet, b.minutesSet);
    }

    public static boolean subset(MinuteRangesSet a, MinuteRangesSet b)
    {
        return IntSpan.subset(a.minutesSet, b.minutesSet);
    }

    public static boolean equal(MinuteRangesSet a, MinuteRangesSet b)
    {
        return IntSpan.equal(a.minutesSet, b.minutesSet);
    }

    public static boolean equivalent(MinuteRangesSet a, MinuteRangesSet b)
    {
        return IntSpan.equivalent(a.minutesSet, b.minutesSet);
    }

    private static final String formatDaysHoursMinutes(int days, int hours, int minutes)
    {
        return days + "d " + (hours < 10 ? "0" + hours : Integer.toString(hours)) + ":" + (minutes < 10
                                                                                           ? "0" + minutes
                                                                                           : Integer.toString(minutes));
    }

    private class SingleDayElementFormatter implements IntSpan.ElementFormatter
    {
        private String delim;

        public SingleDayElementFormatter(String delim)
        {
            this.delim = delim;
        }

        public String getElementDelimiter()
        {
            return delim;
        }

        public String getFormattedElement(int element)
        {
            int hours = element / MINUTES_PER_HOUR;
            int minutes = element - (hours * MINUTES_PER_HOUR);

            return formatDaysHoursMinutes(0, hours, minutes);
        }
    }

    private class MultiDayElementFormatter implements IntSpan.ElementFormatter
    {
        private String delim;

        public MultiDayElementFormatter(String delim)
        {
            this.delim = delim;
        }

        public String getElementDelimiter()
        {
            return delim;
        }

        public String getFormattedElement(int element)
        {
            int days = element / MINUTES_PER_DAY;
            int hours = (element - (days * MINUTES_PER_DAY)) / MINUTES_PER_HOUR;
            int minutes = (element - (days * MINUTES_PER_DAY)) - (hours * MINUTES_PER_HOUR);

            return formatDaysHoursMinutes(days, hours, minutes);
        }
    }

    public class MinuteDateSpanIterator implements Iterator
    {
        private IntSpanIterator i;
        private Date relativeToDate;
        private int relativeToJulianDay;

        public MinuteDateSpanIterator(IntSpanIterator i, Date relativeToDate)
        {
            this.i = i;
            this.relativeToDate = relativeToDate;
            this.relativeToJulianDay = calendarUtils.getJulianDay(relativeToDate);
        }

        public Date getRelativeToDate()
        {
            return relativeToDate;
        }

        public int getRelativeToJulianDay()
        {
            return relativeToJulianDay;
        }

        public boolean hasNext()
        {
            return i.hasNext();
        }

        public boolean hasPrevious()
        {
            return i.hasPrevious();
        }

        public Object next()
        {
            return nextDate();
        }

        public Object previous()
        {
            return previousDate();
        }

        public Date getDateForMinutes(int element)
        {
            if(multipleDays)
            {
                int days = element / MINUTES_PER_DAY;
                int hours = (element - (days * MINUTES_PER_DAY)) / MINUTES_PER_HOUR;
                int minutes = (element - (days * MINUTES_PER_DAY)) - (hours * MINUTES_PER_HOUR);

                return calendarUtils.getDateFromJulianDay(days, hours, minutes, 0);
            }
            else
            {
                int hours = element / MINUTES_PER_HOUR;
                int minutes = element - (hours * MINUTES_PER_HOUR);

                return calendarUtils.getDateFromJulianDay(relativeToJulianDay, hours, minutes, 0);
            }
        }

        public Date nextDate()
        {
            return getDateForMinutes(((Integer) i.next()).intValue());
        }

        public Date previousDate()
        {
            return getDateForMinutes(((Integer) i.previous()).intValue());
        }

        public void remove()
        {
        }

        public String toString()
        {
            return calendarUtils.formatDateTime(getDateForMinutes(Integer.parseInt(i.toString())));
        }
    }
}
