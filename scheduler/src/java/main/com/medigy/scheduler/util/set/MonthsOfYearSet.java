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
package com.medigy.scheduler.util.set;

import java.util.Collection;
import java.util.Set;

import com.medigy.scheduler.util.set.IntSpan.ElementFormatter;
import com.medigy.scheduler.util.set.IntSpan.IntSpanIterator;
import com.medigy.scheduler.util.set.IntSpan.Mappable;
import com.medigy.scheduler.util.set.IntSpan.Testable;

/**
 * Type-safe wrapper to manage a set of months in a year.  The values that this set manages is the
 * same type as used by Calendar.get(Calendar.MONTH).
 */
public class MonthsOfYearSet implements Set
{
    private IntSpan monthsInYearSet;

    public MonthsOfYearSet()
    {
        monthsInYearSet = new IntSpan();
    }

    public MonthsOfYearSet(String runList)
    {
        monthsInYearSet = new IntSpan(runList);
    }

    public MonthsOfYearSet(int[] elements)
    {
        monthsInYearSet = new IntSpan(elements);
    }

    protected MonthsOfYearSet(IntSpan monthsOfYearSet)
    {
        this.monthsInYearSet = monthsOfYearSet;
    }

    public void add(int n)
    {
        monthsInYearSet.add(n);
    }

    public boolean add(Object o)
    {
        return monthsInYearSet.add(o);
    }

    public boolean addAll(Collection c)
    {
        return monthsInYearSet.addAll(c);
    }

    public void addClosed(int lower, int upper)
    {
        monthsInYearSet.addClosed(lower, upper);
    }

    public void clear()
    {
        monthsInYearSet.clear();
    }

    public Object clone()
    {
        return monthsInYearSet.clone();
    }

    public MonthsOfYearSet complement(MonthsOfYearSet s)
    {
        return new MonthsOfYearSet(IntSpan.complement(s.monthsInYearSet));
    }

    public IntSpanIterator constructIterator(boolean empty, Integer start)
    {
        return monthsInYearSet.constructIterator(empty, start);
    }

    public boolean contains(Object o)
    {
        return monthsInYearSet.contains(o);
    }

    public boolean containsAll(Collection c)
    {
        return monthsInYearSet.containsAll(c);
    }

    public MonthsOfYearSet diff(MonthsOfYearSet a, MonthsOfYearSet b)
    {
        return new MonthsOfYearSet(IntSpan.diff(a.monthsInYearSet, b.monthsInYearSet));
    }

    public boolean equal(MonthsOfYearSet a, MonthsOfYearSet b)
    {
        return IntSpan.equal(a.monthsInYearSet, b.monthsInYearSet);
    }

    public boolean equivalent(MonthsOfYearSet a, MonthsOfYearSet b)
    {
        return IntSpan.equivalent(a.monthsInYearSet, b.monthsInYearSet);
    }

    public IntSpanIterator first()
    {
        return monthsInYearSet.first();
    }

    public int[] getElements()
    {
        return monthsInYearSet.getElements();
    }

    public String getFormattedRunList(ElementFormatter formatter)
    {
        return monthsInYearSet.getFormattedRunList(formatter);
    }

    public int getMax()
    {
        return monthsInYearSet.getMax();
    }

    public Integer getMaxInteger()
    {
        return monthsInYearSet.getMaxInteger();
    }

    public int getMin()
    {
        return monthsInYearSet.getMin();
    }

    public Integer getMinInteger()
    {
        return monthsInYearSet.getMinInteger();
    }

    public MonthsOfYearSet grep(Testable predicate)
    {
        return new MonthsOfYearSet(monthsInYearSet.grep(predicate));
    }

    public MonthsOfYearSet intersect(MonthsOfYearSet a, MonthsOfYearSet b)
    {
        return new MonthsOfYearSet(IntSpan.intersect(a.monthsInYearSet, b.monthsInYearSet));
    }

    public boolean isEmpty()
    {
        return monthsInYearSet.isEmpty();
    }

    public boolean isFinite()
    {
        return monthsInYearSet.isFinite();
    }

    public boolean isInfinite()
    {
        return monthsInYearSet.isInfinite();
    }

    public boolean isMember(int n)
    {
        return monthsInYearSet.isMember(n);
    }

    public boolean isNegInfite()
    {
        return monthsInYearSet.isNegInfite();
    }

    public boolean isPosInfite()
    {
        return monthsInYearSet.isPosInfite();
    }

    public boolean isUniversal()
    {
        return monthsInYearSet.isUniversal();
    }

    public java.util.Iterator iterator()
    {
        return monthsInYearSet.iterator();
    }

    public IntSpanIterator last()
    {
        return monthsInYearSet.last();
    }

    public MonthsOfYearSet map(Mappable trans)
    {
        return new MonthsOfYearSet(monthsInYearSet.map(trans));
    }

    public void remove(int n)
    {
        monthsInYearSet.remove(n);
    }

    public boolean remove(Object o)
    {
        return monthsInYearSet.remove(o);
    }

    public boolean removeAll(Collection c)
    {
        return monthsInYearSet.removeAll(c);
    }

    public boolean retainAll(Collection c)
    {
        return monthsInYearSet.retainAll(c);
    }

    public String runList()
    {
        return monthsInYearSet.runList();
    }

    public int size()
    {
        return monthsInYearSet.size();
    }

    public IntSpanIterator start(int n)
    {
        return monthsInYearSet.start(n);
    }

    public boolean subset(MonthsOfYearSet a, MonthsOfYearSet b)
    {
        return IntSpan.subset(a.monthsInYearSet, b.monthsInYearSet);
    }

    public boolean superset(MonthsOfYearSet a, MonthsOfYearSet b)
    {
        return IntSpan.superset(a.monthsInYearSet, b.monthsInYearSet);
    }

    public Object[] toArray()
    {
        return monthsInYearSet.toArray();
    }

    public Object[] toArray(Object list[])
    {
        return monthsInYearSet.toArray(list);
    }

    public String toString()
    {
        return monthsInYearSet.toString();
    }

    public MonthsOfYearSet union(MonthsOfYearSet a, MonthsOfYearSet b)
    {
        return new MonthsOfYearSet(IntSpan.union(a.monthsInYearSet, b.monthsInYearSet));
    }

    public MonthsOfYearSet xor(MonthsOfYearSet a, MonthsOfYearSet b)
    {
        return new MonthsOfYearSet(IntSpan.xor(a.monthsInYearSet, b.monthsInYearSet));
    }
}
