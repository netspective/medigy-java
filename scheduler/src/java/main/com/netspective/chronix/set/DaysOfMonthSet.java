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
 * $Id: DaysOfMonthSet.java,v 1.1 2004-04-10 18:37:04 shahid.shah Exp $
 */

package com.netspective.chronix.set;

import java.util.Collection;
import java.util.Set;

import com.netspective.chronix.set.IntSpan.ElementFormatter;
import com.netspective.chronix.set.IntSpan.IntSpanIterator;
import com.netspective.chronix.set.IntSpan.Mappable;
import com.netspective.chronix.set.IntSpan.Testable;

/**
 * Type-safe wrapper to manage a set of days in a month (actual day numbers). The values that this set manages is the
 * same type as used by Calendar.get(Calendar.DAY_OF_MONTH).
 */
public class DaysOfMonthSet implements Set
{
    private IntSpan daysOfMonthSet;

    public DaysOfMonthSet()
    {
        daysOfMonthSet = new IntSpan();
    }

    public DaysOfMonthSet(String runList)
    {
        daysOfMonthSet = new IntSpan(runList);
    }

    public DaysOfMonthSet(int[] elements)
    {
        daysOfMonthSet = new IntSpan(elements);
    }

    protected DaysOfMonthSet(IntSpan yearsSet)
    {
        this.daysOfMonthSet = yearsSet;
    }

    public void add(int n)
    {
        daysOfMonthSet.add(n);
    }

    public boolean add(Object o)
    {
        return daysOfMonthSet.add(o);
    }

    public boolean addAll(Collection c)
    {
        return daysOfMonthSet.addAll(c);
    }

    public void addClosed(int lower, int upper)
    {
        daysOfMonthSet.addClosed(lower, upper);
    }

    public void clear()
    {
        daysOfMonthSet.clear();
    }

    public Object clone()
    {
        return daysOfMonthSet.clone();
    }

    public DaysOfMonthSet complement(IntSpan s)
    {
        return new DaysOfMonthSet(IntSpan.complement(s));
    }

    public IntSpanIterator constructIterator(boolean empty, Integer start)
    {
        return daysOfMonthSet.constructIterator(empty, start);
    }

    public boolean contains(Object o)
    {
        return daysOfMonthSet.contains(o);
    }

    public boolean containsAll(Collection c)
    {
        return daysOfMonthSet.containsAll(c);
    }

    public DaysOfMonthSet diff(DaysOfMonthSet a, DaysOfMonthSet b)
    {
        return new DaysOfMonthSet(IntSpan.diff(a.daysOfMonthSet, b.daysOfMonthSet));
    }

    public boolean equal(DaysOfMonthSet a, DaysOfMonthSet b)
    {
        return IntSpan.equal(a.daysOfMonthSet, b.daysOfMonthSet);
    }

    public boolean equivalent(DaysOfMonthSet a, DaysOfMonthSet b)
    {
        return IntSpan.equivalent(a.daysOfMonthSet, b.daysOfMonthSet);
    }

    public IntSpanIterator first()
    {
        return daysOfMonthSet.first();
    }

    public int[] getElements()
    {
        return daysOfMonthSet.getElements();
    }

    public String getFormattedRunList(ElementFormatter formatter)
    {
        return daysOfMonthSet.getFormattedRunList(formatter);
    }

    public int getMax()
    {
        return daysOfMonthSet.getMax();
    }

    public Integer getMaxInteger()
    {
        return daysOfMonthSet.getMaxInteger();
    }

    public int getMin()
    {
        return daysOfMonthSet.getMin();
    }

    public Integer getMinInteger()
    {
        return daysOfMonthSet.getMinInteger();
    }

    public DaysOfMonthSet grep(Testable predicate)
    {
        return new DaysOfMonthSet(daysOfMonthSet.grep(predicate));
    }

    public DaysOfMonthSet intersect(DaysOfMonthSet a, DaysOfMonthSet b)
    {
        return new DaysOfMonthSet(IntSpan.intersect(a.daysOfMonthSet, b.daysOfMonthSet));
    }

    public boolean isEmpty()
    {
        return daysOfMonthSet.isEmpty();
    }

    public boolean isFinite()
    {
        return daysOfMonthSet.isFinite();
    }

    public boolean isInfinite()
    {
        return daysOfMonthSet.isInfinite();
    }

    public boolean isMember(int n)
    {
        return daysOfMonthSet.isMember(n);
    }

    public boolean isNegInfite()
    {
        return daysOfMonthSet.isNegInfite();
    }

    public boolean isPosInfite()
    {
        return daysOfMonthSet.isPosInfite();
    }

    public boolean isUniversal()
    {
        return daysOfMonthSet.isUniversal();
    }

    public java.util.Iterator iterator()
    {
        return daysOfMonthSet.iterator();
    }

    public IntSpanIterator last()
    {
        return daysOfMonthSet.last();
    }

    public DaysOfMonthSet map(Mappable trans)
    {
        return new DaysOfMonthSet(daysOfMonthSet.map(trans));
    }

    public void remove(int n)
    {
        daysOfMonthSet.remove(n);
    }

    public boolean remove(Object o)
    {
        return daysOfMonthSet.remove(o);
    }

    public boolean removeAll(Collection c)
    {
        return daysOfMonthSet.removeAll(c);
    }

    public boolean retainAll(Collection c)
    {
        return daysOfMonthSet.retainAll(c);
    }

    public String runList()
    {
        return daysOfMonthSet.runList();
    }

    public int size()
    {
        return daysOfMonthSet.size();
    }

    public IntSpanIterator start(int n)
    {
        return daysOfMonthSet.start(n);
    }

    public boolean subset(DaysOfMonthSet a, DaysOfMonthSet b)
    {
        return IntSpan.subset(a.daysOfMonthSet, b.daysOfMonthSet);
    }

    public boolean superset(DaysOfMonthSet a, DaysOfMonthSet b)
    {
        return IntSpan.superset(a.daysOfMonthSet, b.daysOfMonthSet);
    }

    public Object[] toArray()
    {
        return daysOfMonthSet.toArray();
    }

    public Object[] toArray(Object list[])
    {
        return daysOfMonthSet.toArray(list);
    }

    public String toString()
    {
        return daysOfMonthSet.toString();
    }

    public DaysOfMonthSet union(DaysOfMonthSet a, DaysOfMonthSet b)
    {
        return new DaysOfMonthSet(IntSpan.union(a.daysOfMonthSet, b.daysOfMonthSet));
    }

    public DaysOfMonthSet xor(DaysOfMonthSet a, DaysOfMonthSet b)
    {
        return new DaysOfMonthSet(IntSpan.xor(a.daysOfMonthSet, b.daysOfMonthSet));
    }
}
