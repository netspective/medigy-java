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
 * $Id: DaysOfWeekSet.java,v 1.1 2004-04-10 18:37:04 shahid.shah Exp $
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
 * same type as used by Calendar.get(Calendar.DAY_OF_WEEK).
 */
public class DaysOfWeekSet implements Set
{
    private IntSpan daysOfWeekSet;

    public DaysOfWeekSet()
    {
        daysOfWeekSet = new IntSpan();
    }

    public DaysOfWeekSet(String runList)
    {
        daysOfWeekSet = new IntSpan(runList);
    }

    public DaysOfWeekSet(int[] elements)
    {
        daysOfWeekSet = new IntSpan(elements);
    }

    protected DaysOfWeekSet(IntSpan yearsSet)
    {
        this.daysOfWeekSet = yearsSet;
    }

    public void add(int n)
    {
        daysOfWeekSet.add(n);
    }

    public boolean add(Object o)
    {
        return daysOfWeekSet.add(o);
    }

    public boolean addAll(Collection c)
    {
        return daysOfWeekSet.addAll(c);
    }

    public void addClosed(int lower, int upper)
    {
        daysOfWeekSet.addClosed(lower, upper);
    }

    public void clear()
    {
        daysOfWeekSet.clear();
    }

    public Object clone()
    {
        return daysOfWeekSet.clone();
    }

    public DaysOfWeekSet complement(IntSpan s)
    {
        return new DaysOfWeekSet(IntSpan.complement(s));
    }

    public IntSpanIterator constructIterator(boolean empty, Integer start)
    {
        return daysOfWeekSet.constructIterator(empty, start);
    }

    public boolean contains(Object o)
    {
        return daysOfWeekSet.contains(o);
    }

    public boolean containsAll(Collection c)
    {
        return daysOfWeekSet.containsAll(c);
    }

    public DaysOfWeekSet diff(DaysOfWeekSet a, DaysOfWeekSet b)
    {
        return new DaysOfWeekSet(IntSpan.diff(a.daysOfWeekSet, b.daysOfWeekSet));
    }

    public boolean equal(DaysOfWeekSet a, DaysOfWeekSet b)
    {
        return IntSpan.equal(a.daysOfWeekSet, b.daysOfWeekSet);
    }

    public boolean equivalent(DaysOfWeekSet a, DaysOfWeekSet b)
    {
        return IntSpan.equivalent(a.daysOfWeekSet, b.daysOfWeekSet);
    }

    public IntSpanIterator first()
    {
        return daysOfWeekSet.first();
    }

    public int[] getElements()
    {
        return daysOfWeekSet.getElements();
    }

    public String getFormattedRunList(ElementFormatter formatter)
    {
        return daysOfWeekSet.getFormattedRunList(formatter);
    }

    public int getMax()
    {
        return daysOfWeekSet.getMax();
    }

    public Integer getMaxInteger()
    {
        return daysOfWeekSet.getMaxInteger();
    }

    public int getMin()
    {
        return daysOfWeekSet.getMin();
    }

    public Integer getMinInteger()
    {
        return daysOfWeekSet.getMinInteger();
    }

    public DaysOfWeekSet grep(Testable predicate)
    {
        return new DaysOfWeekSet(daysOfWeekSet.grep(predicate));
    }

    public DaysOfWeekSet intersect(DaysOfWeekSet a, DaysOfWeekSet b)
    {
        return new DaysOfWeekSet(IntSpan.intersect(a.daysOfWeekSet, b.daysOfWeekSet));
    }

    public boolean isEmpty()
    {
        return daysOfWeekSet.isEmpty();
    }

    public boolean isFinite()
    {
        return daysOfWeekSet.isFinite();
    }

    public boolean isInfinite()
    {
        return daysOfWeekSet.isInfinite();
    }

    public boolean isMember(int n)
    {
        return daysOfWeekSet.isMember(n);
    }

    public boolean isNegInfite()
    {
        return daysOfWeekSet.isNegInfite();
    }

    public boolean isPosInfite()
    {
        return daysOfWeekSet.isPosInfite();
    }

    public boolean isUniversal()
    {
        return daysOfWeekSet.isUniversal();
    }

    public java.util.Iterator iterator()
    {
        return daysOfWeekSet.iterator();
    }

    public IntSpanIterator last()
    {
        return daysOfWeekSet.last();
    }

    public DaysOfWeekSet map(Mappable trans)
    {
        return new DaysOfWeekSet(daysOfWeekSet.map(trans));
    }

    public void remove(int n)
    {
        daysOfWeekSet.remove(n);
    }

    public boolean remove(Object o)
    {
        return daysOfWeekSet.remove(o);
    }

    public boolean removeAll(Collection c)
    {
        return daysOfWeekSet.removeAll(c);
    }

    public boolean retainAll(Collection c)
    {
        return daysOfWeekSet.retainAll(c);
    }

    public String runList()
    {
        return daysOfWeekSet.runList();
    }

    public int size()
    {
        return daysOfWeekSet.size();
    }

    public IntSpanIterator start(int n)
    {
        return daysOfWeekSet.start(n);
    }

    public boolean subset(DaysOfWeekSet a, DaysOfWeekSet b)
    {
        return IntSpan.subset(a.daysOfWeekSet, b.daysOfWeekSet);
    }

    public boolean superset(DaysOfWeekSet a, DaysOfWeekSet b)
    {
        return IntSpan.superset(a.daysOfWeekSet, b.daysOfWeekSet);
    }

    public Object[] toArray()
    {
        return daysOfWeekSet.toArray();
    }

    public Object[] toArray(Object list[])
    {
        return daysOfWeekSet.toArray(list);
    }

    public String toString()
    {
        return daysOfWeekSet.toString();
    }

    public DaysOfWeekSet union(DaysOfWeekSet a, DaysOfWeekSet b)
    {
        return new DaysOfWeekSet(IntSpan.union(a.daysOfWeekSet, b.daysOfWeekSet));
    }

    public DaysOfWeekSet xor(DaysOfWeekSet a, DaysOfWeekSet b)
    {
        return new DaysOfWeekSet(IntSpan.xor(a.daysOfWeekSet, b.daysOfWeekSet));
    }
}
