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
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.netspective.chronix.CalendarUtils;
import com.netspective.chronix.set.IntSpan.ElementFormatter;
import com.netspective.chronix.set.IntSpan.IntSpanIterator;
import com.netspective.chronix.set.IntSpan.Mappable;
import com.netspective.chronix.set.IntSpan.Testable;

public class DateRangesSet
{
    private CalendarUtils calendarUtils;
    private IntSpan daysSet = new IntSpan();

    public DateRangesSet(CalendarUtils calendarUtils)
    {
        this.calendarUtils = calendarUtils;
    }

    public DateRangesSet(CalendarUtils calendarUtils, Date beginDate, Date endDate, YearsSet years, MonthsOfYearSet monthsOfTheYear, DaysOfMonthSet daysOfTheMonth, DaysOfWeekSet daysOfTheWeek)
    {
        this(calendarUtils);
        applyDateRange(beginDate, endDate, years, monthsOfTheYear, daysOfTheMonth, daysOfTheWeek);
    }

    /**
     * Calculate a set in which the months of the year, days of the month, and days of the week occur in between
     * the given begin and end date.
     *
     * @param beginDate       The date to start calculating from
     * @param endDate         The date to end calculation at
     * @param monthsOfTheYear A set of year members for which to calculate the date set (values must match input to Calendar.set(Calendar.YEAR)). May be NULL for all years.
     * @param monthsOfTheYear A set of month numbers for which to calculate the date set (values must match input to Calendar.set(Calendar.MONTH)). May be NULL for all months.
     * @param daysOfTheMonth  A set of of day numbers for which to calculate the date set (values must match input to Calendar.set(Calendar.DAY_OF_MONTH)). May be NULL for all days of month.
     * @param daysOfTheWeek   A set of of days of the week to calculate the date set (values must match input to Calendar.set(Calendar.DAY_OF_WEEK)). May be NULL for all days of week.
     */
    public void applyDateRange(Date beginDate, Date endDate, YearsSet years, MonthsOfYearSet monthsOfTheYear, DaysOfMonthSet daysOfTheMonth, DaysOfWeekSet daysOfTheWeek)
    {
        Calendar calendar = calendarUtils.getCalendar();

        final int begin = calendarUtils.getJulianDay(beginDate), end = calendarUtils.getJulianDay(endDate);
        final boolean haveYears = years != null, haveMonthsOfYear = monthsOfTheYear != null, haveDaysOfMonth = daysOfTheMonth != null, haveDaysOfWeek = daysOfTheWeek != null;

        for(int julianDay = begin; julianDay <= end; julianDay++)
        {
            Date activeDate = calendarUtils.getDateFromJulianDay(julianDay);
            calendar.setTime(activeDate);

            if((!haveYears || years.isMember(calendar.get(Calendar.YEAR))) &&
               (!haveMonthsOfYear || monthsOfTheYear.isMember(calendar.get(Calendar.MONTH))) &&
               (!haveDaysOfMonth || daysOfTheMonth.isMember(calendar.get(Calendar.DAY_OF_MONTH))) &&
               (!haveDaysOfWeek || daysOfTheWeek.isMember(calendar.get(Calendar.DAY_OF_WEEK), calendar.get(Calendar.WEEK_OF_MONTH))))
            {
                add(julianDay);
            }
        }
    }

    protected DateRangesSet(IntSpan typeSet)
    {
        this.daysSet = typeSet;
    }

    public boolean add(Object o)
    {
        add((Date) o);
        return true;
    }

    public void clear()
    {
        daysSet.clear();
    }

    public boolean contains(Object o)
    {
        return isMember((Date) o);
    }

    public boolean remove(Object o)
    {
        remove((Date) o);
        return true;
    }

    public boolean addAll(Collection c)
    {
        return daysSet.addAll(c);
    }

    public boolean containsAll(Collection c)
    {
        return daysSet.containsAll(c);
    }

    public boolean removeAll(Collection c)
    {
        return daysSet.removeAll(c);
    }

    public boolean retainAll(Collection c)
    {
        return daysSet.retainAll(c);
    }

    public Iterator iterator()
    {
        return new DateSpanIterator((IntSpanIterator) daysSet.iterator());
    }

    public Object clone()
    {
        return daysSet.clone();
    }

    public String runList()
    {
        return daysSet.runList();
    }

    public String getFormattedRunList(ElementFormatter formatter)
    {
        return daysSet.getFormattedRunList(formatter);
    }

    public int[] getElements()
    {
        return daysSet.getElements();
    }

    public Object[] toArray()
    {
        return daysSet.toArray();
    }

    public Object[] toArray(Object list[])
    {
        return daysSet.toArray(list);
    }

    public int size()
    {
        return daysSet.size();
    }

    public boolean isEmpty()
    {
        return daysSet.isEmpty();
    }

    public boolean isFinite()
    {
        return daysSet.isFinite();
    }

    public boolean isNegInfite()
    {
        return daysSet.isNegInfite();
    }

    public boolean isPosInfite()
    {
        return daysSet.isPosInfite();
    }

    public boolean isInfinite()
    {
        return daysSet.isInfinite();
    }

    public boolean isUniversal()
    {
        return daysSet.isUniversal();
    }

    public boolean isMember(Date date)
    {
        return daysSet.isMember(calendarUtils.getJulianDay(date));
    }

    public boolean isMember(int julianDay)
    {
        return daysSet.isMember(julianDay);
    }

    public void add(Date date)
    {
        add(calendarUtils.getJulianDay(date));
    }

    public void add(int julianDay)
    {
        daysSet.add(julianDay);
    }

    public void remove(Date date)
    {
        remove(calendarUtils.getJulianDay(date));
    }

    public void remove(int julianDay)
    {
        daysSet.remove(julianDay);
    }

    public int getMin()
    {
        return daysSet.getMin();
    }

    public int getMax()
    {
        return daysSet.getMax();
    }

    public Date getMinDate()
    {
        return calendarUtils.getDateFromJulianDay(getMin());
    }

    public Date getMaxDate()
    {
        return calendarUtils.getDateFromJulianDay(getMax());
    }

    public DateRangesSet grep(Testable predicate)
    {
        return new DateRangesSet(daysSet.grep(predicate));
    }

    public DateRangesSet map(Mappable trans)
    {
        return new DateRangesSet(daysSet.map(trans));
    }

    public DateSpanIterator first()
    {
        return new DateSpanIterator(daysSet.first());
    }

    public DateSpanIterator last()
    {
        return new DateSpanIterator(daysSet.last());
    }

    public DateSpanIterator start(int julianDay)
    {
        return new DateSpanIterator(daysSet.start(julianDay));
    }

    public DateSpanIterator start(Date date)
    {
        return new DateSpanIterator(daysSet.start(calendarUtils.getJulianDay(date)));
    }

    public String toString(DateFormat format, String delim)
    {
        return daysSet.getFormattedRunList(new ElementDateFormatter(format, delim));
    }

    public String toString()
    {
        return toString(DateFormat.getDateInstance(), ", ");
    }

    public static DateRangesSet union(DateRangesSet a, DateRangesSet b)
    {
        return new DateRangesSet(IntSpan.union(a.daysSet, b.daysSet));
    }

    public static DateRangesSet intersect(DateRangesSet a, DateRangesSet b)
    {
        return new DateRangesSet(IntSpan.intersect(a.daysSet, b.daysSet));
    }

    public static DateRangesSet diff(DateRangesSet a, DateRangesSet b)
    {
        return new DateRangesSet(IntSpan.diff(a.daysSet, b.daysSet));
    }

    public static DateRangesSet xor(DateRangesSet a, DateRangesSet b)
    {
        return new DateRangesSet(IntSpan.xor(a.daysSet, b.daysSet));
    }

    public static DateRangesSet complement(DateRangesSet s)
    {
        return new DateRangesSet(IntSpan.complement(s.daysSet));
    }

    public static boolean superset(DateRangesSet a, DateRangesSet b)
    {
        return IntSpan.superset(a.daysSet, b.daysSet);
    }

    public static boolean subset(DateRangesSet a, DateRangesSet b)
    {
        return IntSpan.subset(a.daysSet, b.daysSet);
    }

    public static boolean equal(DateRangesSet a, DateRangesSet b)
    {
        return IntSpan.equal(a.daysSet, b.daysSet);
    }

    public static boolean equivalent(DateRangesSet a, DateRangesSet b)
    {
        return IntSpan.equivalent(a.daysSet, b.daysSet);
    }

    private class ElementDateFormatter implements IntSpan.ElementFormatter
    {
        private DateFormat format;
        private String delim;

        public ElementDateFormatter(DateFormat format, String delim)
        {
            this.format = format;
            this.delim = delim;
        }

        public String getElementDelimiter()
        {
            return delim;
        }

        public String getFormattedElement(int element)
        {
            Date date = calendarUtils.getDateFromJulianDay(element);
            return format.format(date);
        }
    }

    public class DateSpanIterator implements Iterator
    {
        private IntSpanIterator i;

        public DateSpanIterator(IntSpanIterator i)
        {
            this.i = i;
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
            Integer result = (Integer) i.next();
            return calendarUtils.getDateFromJulianDay(result.intValue());
        }

        public Object previous()
        {
            Integer result = (Integer) i.previous();
            return calendarUtils.getDateFromJulianDay(result.intValue());
        }

        public Date nextDate()
        {
            Integer result = (Integer) i.next();
            return calendarUtils.getDateFromJulianDay(result.intValue());
        }

        public Date previousDate()
        {
            Integer result = (Integer) i.previous();
            return calendarUtils.getDateFromJulianDay(result.intValue());
        }

        public void remove()
        {
        }

        public String toString()
        {
            return calendarUtils.getDateFromJulianDay(Integer.parseInt(i.toString())).toString();
        }
    }
}
