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

import java.util.Collection;
import java.util.Set;

import com.netspective.chronix.set.IntSpan.ElementFormatter;
import com.netspective.chronix.set.IntSpan.IntSpanIterator;
import com.netspective.chronix.set.IntSpan.Mappable;
import com.netspective.chronix.set.IntSpan.Testable;

/**
 * Type-safe wrapper to manage a set of years.  The values that this set manages is the
 * same type as used by Calendar.get(Calendar.YEAR).
 */
public class YearsSet implements Set
{
    private IntSpan yearsSet;

    public YearsSet()
    {
        yearsSet = new IntSpan();
    }

    public YearsSet(String runList)
    {
        yearsSet = new IntSpan(runList);
    }

    public YearsSet(int[] elements)
    {
        yearsSet = new IntSpan(elements);
    }

    protected YearsSet(IntSpan yearsSet)
    {
        this.yearsSet = yearsSet;
    }

    public void add(int n)
    {
        yearsSet.add(n);
    }

    public boolean add(Object o)
    {
        return yearsSet.add(o);
    }

    public boolean addAll(Collection c)
    {
        return yearsSet.addAll(c);
    }

    public void addClosed(int lower, int upper)
    {
        yearsSet.addClosed(lower, upper);
    }

    public void clear()
    {
        yearsSet.clear();
    }

    public Object clone()
    {
        return yearsSet.clone();
    }

    public YearsSet complement(IntSpan s)
    {
        return new YearsSet(IntSpan.complement(s));
    }

    public IntSpanIterator constructIterator(boolean empty, Integer start)
    {
        return yearsSet.constructIterator(empty, start);
    }

    public boolean contains(Object o)
    {
        return yearsSet.contains(o);
    }

    public boolean containsAll(Collection c)
    {
        return yearsSet.containsAll(c);
    }

    public YearsSet diff(YearsSet a, YearsSet b)
    {
        return new YearsSet(IntSpan.diff(a.yearsSet, b.yearsSet));
    }

    public boolean equal(YearsSet a, YearsSet b)
    {
        return IntSpan.equal(a.yearsSet, b.yearsSet);
    }

    public boolean equivalent(YearsSet a, YearsSet b)
    {
        return IntSpan.equivalent(a.yearsSet, b.yearsSet);
    }

    public IntSpanIterator first()
    {
        return yearsSet.first();
    }

    public int[] getElements()
    {
        return yearsSet.getElements();
    }

    public String getFormattedRunList(ElementFormatter formatter)
    {
        return yearsSet.getFormattedRunList(formatter);
    }

    public int getMax()
    {
        return yearsSet.getMax();
    }

    public Integer getMaxInteger()
    {
        return yearsSet.getMaxInteger();
    }

    public int getMin()
    {
        return yearsSet.getMin();
    }

    public Integer getMinInteger()
    {
        return yearsSet.getMinInteger();
    }

    public YearsSet grep(Testable predicate)
    {
        return new YearsSet(yearsSet.grep(predicate));
    }

    public YearsSet intersect(YearsSet a, YearsSet b)
    {
        return new YearsSet(IntSpan.intersect(a.yearsSet, b.yearsSet));
    }

    public boolean isEmpty()
    {
        return yearsSet.isEmpty();
    }

    public boolean isFinite()
    {
        return yearsSet.isFinite();
    }

    public boolean isInfinite()
    {
        return yearsSet.isInfinite();
    }

    public boolean isMember(int n)
    {
        return yearsSet.isMember(n);
    }

    public boolean isNegInfite()
    {
        return yearsSet.isNegInfite();
    }

    public boolean isPosInfite()
    {
        return yearsSet.isPosInfite();
    }

    public boolean isUniversal()
    {
        return yearsSet.isUniversal();
    }

    public java.util.Iterator iterator()
    {
        return yearsSet.iterator();
    }

    public IntSpanIterator last()
    {
        return yearsSet.last();
    }

    public YearsSet map(Mappable trans)
    {
        return new YearsSet(yearsSet.map(trans));
    }

    public void remove(int n)
    {
        yearsSet.remove(n);
    }

    public boolean remove(Object o)
    {
        return yearsSet.remove(o);
    }

    public boolean removeAll(Collection c)
    {
        return yearsSet.removeAll(c);
    }

    public boolean retainAll(Collection c)
    {
        return yearsSet.retainAll(c);
    }

    public String runList()
    {
        return yearsSet.runList();
    }

    public int size()
    {
        return yearsSet.size();
    }

    public IntSpanIterator start(int n)
    {
        return yearsSet.start(n);
    }

    public boolean subset(YearsSet a, YearsSet b)
    {
        return IntSpan.subset(a.yearsSet, b.yearsSet);
    }

    public boolean superset(YearsSet a, YearsSet b)
    {
        return IntSpan.superset(a.yearsSet, b.yearsSet);
    }

    public Object[] toArray()
    {
        return yearsSet.toArray();
    }

    public Object[] toArray(Object list[])
    {
        return yearsSet.toArray(list);
    }

    public String toString()
    {
        return yearsSet.toString();
    }

    public YearsSet union(YearsSet a, YearsSet b)
    {
        return new YearsSet(IntSpan.union(a.yearsSet, b.yearsSet));
    }

    public YearsSet xor(YearsSet a, YearsSet b)
    {
        return new YearsSet(IntSpan.xor(a.yearsSet, b.yearsSet));
    }
}
