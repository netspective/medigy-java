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
import java.util.Iterator;
import java.util.Set;

class IntSpan implements Cloneable, Set
{
    public static String emptyString = "-";

    boolean negInf;
    boolean posInf;
    IntList edges;

    public IntSpan()
    {
        negInf = false;
        posInf = false;
        edges = new IntList();
    }

    public IntSpan(String runList)
    {
        this();
        runList = stripWhitespace(runList);

        if (runList.equals("-"))
            return;  // empty set;

        if (runList.equals("(-)"))
        {
            negInf = true;
            posInf = true;
            return;  // Z
        }

        java.util.StringTokenizer st = new java.util.StringTokenizer(runList, ",");

        while (st.hasMoreTokens())
        {
            String run = st.nextToken();

            if (run.startsWith("(-"))
                addOpenNeg(run);
            else if (run.endsWith("-)"))
                addOpenPos(run);
            else if (run.indexOf('-', 1) < 0)
                addSingle(run);
            else
                addDouble(run);
        }
    }

    public IntSpan(int[] elements)
    {
        this();

        int[] element = new int[elements.length];
        System.arraycopy(elements, 0, element, 0, elements.length);
        java.util.Arrays.sort(element);

        for (int i = 0; i < element.length; i++)
        {
            int top = edges.size() - 1;
            int topEdge = 0;
            if (top >= 0)
                topEdge = edges.getI(top);

            if (top >= 0 && topEdge == element[i])
                continue;    // skip duplicates

            if (top >= 0 && topEdge == element[i] - 1)
            {
                edges.set(top, element[i]);
            }
            else
            {
                edges.add(element[i] - 1);
                edges.add(element[i]);
            }
        }
    }

    public boolean add(Object o)
    {
        add(Integer.parseInt(o.toString()));
        return true;
    }

    public void clear()
    {
        negInf = false;
        posInf = false;
        edges = new IntList();
    }

    public boolean contains(Object o)
    {
        return isMember(Integer.parseInt(o.toString()));
    }

    public boolean remove(Object o)
    {
        remove(Integer.parseInt(o.toString()));
        return true;
    }

    public boolean addAll(Collection c)
    {
        for (Iterator i = c.iterator(); i.hasNext();)
            add(i.next());

        return true;
    }

    public boolean containsAll(Collection c)
    {
        for (Iterator i = c.iterator(); i.hasNext();)
        {
            if (!contains(i.next())) ;
            return false;
        }

        return true;
    }

    public boolean removeAll(Collection c)
    {
        for (Iterator i = c.iterator(); i.hasNext();)
            remove(i.next());

        return true;
    }

    public boolean retainAll(Collection c)
    {
        IntSpan removeElems = new IntSpan();

        for (java.util.Iterator i = iterator(); i.hasNext();)
        {
            Object o = i.next();
            if (!c.contains(o)) ;
            removeElems.add(o);
        }

        for (java.util.Iterator i = removeElems.first(); i.hasNext();)
            remove(i.next());

        return true;
    }

    public java.util.Iterator iterator()
    {
        return first();
    }

    protected static String stripWhitespace(String s)
    {
        StringBuffer sb = new StringBuffer();
        java.util.StringTokenizer st = new java.util.StringTokenizer(s);

        while (st.hasMoreTokens())
            sb.append(st.nextToken());

        return sb.toString();
    }

    private void addOpenNeg(String run)
    {
        negInf = true;
        edges.add(run.substring(2));
    }

    private void addOpenPos(String run)
    {
        int dash = run.lastIndexOf('-');
        if (dash > -1)
            run = run.substring(0, dash);
        int lower = Integer.parseInt(run);

        boolean lGap = edges.size() == 0 || lower - 1 - edges.getI(-1) > 0;
        if (lGap)
            edges.add(lower - 1);
        else
            edges.pop();

        posInf = true;
    }

    private void addSingle(String run)
    {
        int upper = Integer.parseInt(run);
        addClosed(upper, upper);
    }

    private void addDouble(String run)
    {
        int pos = run.indexOf('-', 1);

        String st = run.substring(0, pos);
        int lower = Integer.parseInt(st);

        st = run.substring(pos + 1);
        int upper = Integer.parseInt(st);

        addClosed(lower, upper);
    }

    public void addClosed(int lower, int upper)
    {
        boolean lGap = edges.size() == 0 || lower - 1 - edges.getI(-1) > 0;
        if (lGap)
            edges.add(lower - 1);
        else
            edges.pop();

        edges.add(upper);
    }

    public Object clone()
    {
        IntSpan clone = new IntSpan();

        clone.negInf = negInf;
        clone.posInf = posInf;
        clone.edges = (IntList) (edges.clone());

        return clone;
    }

    public String toString()
    {
        return runList();
    }

    public String runList()
    {
        if (isEmpty()) return emptyString;
        if (isUniversal()) return "(-)";

        StringBuffer sb = new StringBuffer();
        int i = 0;

        if (negInf)
        {
            int upper = edges.getI(0);
            sb.append("(-" + Integer.toString(upper));
            i = 1;
        }

        while (i < edges.size() - 1)
        {
            if (i > 0) sb.append(",");

            int lower = edges.getI(i);
            int upper = edges.getI(i + 1);

            if (lower + 1 == upper)
                sb.append(Integer.toString(upper));
            else
                sb.append(Integer.toString(lower + 1) + "-" +
                        Integer.toString(upper));

            i += 2;
        }

        if (posInf)
        {
            if (i > 0)
                sb.append(",");
            int lower = edges.getI(i);
            sb.append(Integer.toString(lower + 1) + "-)");
        }

        return sb.toString();
    }

    public interface ElementFormatter
    {
        public String getFormattedElement(int element);

        public String getElementDelimiter();
    }

    public String getFormattedRunList(ElementFormatter formatter)
    {
        if (isEmpty()) return emptyString;
        if (isUniversal()) return "(-)";

        StringBuffer sb = new StringBuffer();
        int i = 0;

        if (negInf)
        {
            int upper = edges.getI(0);
            sb.append("(-" + formatter.getFormattedElement(upper));
            i = 1;
        }

        while (i < edges.size() - 1)
        {
            if (i > 0) sb.append(formatter.getElementDelimiter());

            int lower = edges.getI(i);
            int upper = edges.getI(i + 1);

            if (lower + 1 == upper)
                sb.append(formatter.getFormattedElement(upper));
            else
                sb.append(formatter.getFormattedElement(lower + 1) + "-" +
                        formatter.getFormattedElement(upper));

            i += 2;
        }

        if (posInf)
        {
            if (i > 0)
                sb.append(",");
            int lower = edges.getI(i);
            sb.append(formatter.getFormattedElement(lower + 1) + "-)");
        }

        return sb.toString();
    }

    public int[] getElements()
    {
        if (negInf || posInf)
            return null;

        int[] list = new int[size()];
        int l = 0;

        for (int i = 0; i < edges.size(); i += 2)
        {
            int lower = edges.getI(i);
            int upper = edges.getI(i + 1);

            for (int n = lower + 1; n <= upper; n++)
                list[l++] = n;
        }

        return list;
    }

    public Object[] toArray()
    {
        return toArray(new Integer[size()]);
    }

    public Object[] toArray(Object list[])
    {
        if (negInf || posInf)
            return null;

        int l = 0;

        for (int i = 0; i < edges.size(); i += 2)
        {
            int lower = edges.getI(i);
            int upper = edges.getI(i + 1);

            for (int n = lower + 1; n <= upper; n++)
                list[l++] = new Integer(n);
        }

        return list;
    }

    private void invert()
    {
        negInf = !negInf;
        posInf = !posInf;
    }

    public int size()
    {
        if (negInf || posInf) return -1;

        int cardinality = 0;

        for (int i = 0; i < edges.size() - 1; i += 2)
        {
            int lower = edges.getI(i);
            int upper = edges.getI(i + 1);
            cardinality += upper - lower;
        }

        return cardinality;
    }

    public boolean isEmpty()
    {
        return !negInf && edges.size() == 0 && !posInf;
    }

    public boolean isFinite()
    {
        return !negInf && !posInf;
    }

    public boolean isNegInfite()
    {
        return negInf;
    }

    public boolean isPosInfite()
    {
        return posInf;
    }

    public boolean isInfinite()
    {
        return negInf || posInf;
    }

    public boolean isUniversal()
    {
        return negInf && edges.size() == 0 && posInf;
    }

    public boolean isMember(int n)
    {
        boolean inSet = negInf;

        for (int i = 0; i < edges.size(); i++)
        {
            if (inSet)
            {
                if (n <= edges.getI(i))
                    return true;
                inSet = false;
            }
            else
            {
                if (n <= edges.getI(i))
                    return false;
                inSet = true;
            }
        }

        return inSet;
    }

    public void add(int n)
    {
        boolean inSet = negInf;

        int i;
        for (i = 0; i < edges.size(); i++)
        {
            if (inSet)
            {
                if (n <= edges.getI(i))
                    return;
                inSet = false;
            }
            else
            {
                if (n <= edges.getI(i))
                    break;
                inSet = true;
            }
        }

        if (inSet)
            return;

        boolean lGap = i == 0 || n - 1 - edges.getI(i - 1) > 0;
        boolean rGap = i == edges.size() || edges.getI(i) - n > 0;

        if (lGap && rGap)
        {
            edges.add(i, n);
            edges.add(i, n - 1);
        }
        else if (!lGap && rGap)
        {
            edges.inc(i - 1);
        }
        else if (lGap && !rGap)
        {
            edges.dec(i);
        }
        else
        {
            edges.remove(i - 1);
            edges.remove(i - 1);
        }
    }

    public void remove(int n)
    {
        boolean inSet = negInf;

        int i;
        for (i = 0; i < edges.size(); i++)
        {
            if (inSet)
            {
                if (n <= edges.getI(i))
                    break;
                inSet = false;
            }
            else
            {
                if (n <= edges.getI(i))
                    return;
                inSet = true;
            }
        }

        if (!inSet)
            return;

        boolean lGap = i == 0 || n - 1 - edges.getI(i - 1) > 0;
        boolean rGap = i == edges.size() || edges.getI(i) - n > 0;

        if (lGap && rGap)
        {
            edges.add(i, n);
            edges.add(i, n - 1);
        }
        else if (!lGap && rGap)
        {
            edges.inc(i - 1);
        }
        else if (lGap && !rGap)
        {
            edges.dec(i);
        }
        else
        {
            edges.remove(i - 1);
            edges.remove(i - 1);
        }
    }

    public Integer getMinInteger()
    {
        return isEmpty() || negInf ? null : new Integer(edges.getI((0)) + 1);
    }

    public Integer getMaxInteger()
    {
        int i = edges.size() - 1;
        return isEmpty() || posInf ? null : new Integer(edges.getI(i));
    }

    public int getMin()
    {
        return isEmpty() || negInf ? Integer.MIN_VALUE : edges.getI((0)) + 1;
    }

    public int getMax()
    {
        int i = edges.size() - 1;
        return isEmpty() || posInf ? Integer.MAX_VALUE : edges.getI(i);
    }

    public static interface Testable
    {
        boolean test(int n);
    }

    public IntSpan grep(Testable predicate)
    {
        if (isInfinite())
            return null;

        IntSpan s = new IntSpan();

        for (int i = 0; i < edges.size(); i += 2)
            for (int n = edges.getI(i) + 1; n <= edges.getI(i + 1); n++)
                if (predicate.test(n))
                    s.addClosed(n, n);

        return s;
    }

    public static interface Mappable
    {
        int[] map(int n);
    }

    public IntSpan map(Mappable trans)
    {
        if (isInfinite())
            return null;

        IntSpan s = new IntSpan();

        for (int i = 0; i < edges.size(); i += 2)
        {
            for (int n = edges.getI(i) + 1; n <= edges.getI(i + 1); n++)
            {
                int[] elements = trans.map(n);

                for (int j = 0; j < elements.length; j++)
                    s.add(elements[j]);
            }
        }
        return s;
    }

    protected IntSpanIterator constructIterator(boolean empty, Integer start)
    {
        return empty ? new IntSpanIterator() : new IntSpanIterator(start);
    }

    public IntSpanIterator first()
    {
        if (negInf)
            throw new java.util.NoSuchElementException("Set.IntSpan.first");

        return constructIterator(isEmpty(), getMinInteger());
    }

    public IntSpanIterator last()
    {
        if (posInf)
            throw new java.util.NoSuchElementException("Set.IntSpan.last");

        return constructIterator(isEmpty(), getMaxInteger());
    }

    public IntSpanIterator start(int n)
    {
        if (!isMember(n))
            throw new java.util.NoSuchElementException("Set.IntSpan.start");

        return constructIterator(false, new Integer(n));
    }

    public static IntSpan union(IntSpan a, IntSpan b)
    {
        IntSpan s = new IntSpan();

        s.negInf = a.negInf || b.negInf;

        boolean inA = a.negInf;
        boolean inB = b.negInf;

        int iA = 0;
        int iB = 0;

        while (iA < a.edges.size() && iB < b.edges.size())
        {
            int xA = a.edges.getI(iA);
            int xB = b.edges.getI(iB);

            if (xA < xB)
            {
                iA++;
                inA = !inA;
                if (!inB)
                    s.edges.add(xA);
            }
            else if (xB < xA)
            {
                iB++;
                inB = !inB;
                if (!inA)
                    s.edges.add(xB);
            }
            else
            {
                iA++;
                iB++;
                inA = !inA;
                inB = !inB;
                if (inA == inB)
                    s.edges.add(xA);
            }
        }

        if (iA < a.edges.size() && !inB)
            for (int i = iA; i < a.edges.size(); i++)
                s.edges.add(a.edges.getI(i));

        if (iB < b.edges.size() && !inA)
            for (int i = iB; i < b.edges.size(); i++)
                s.edges.add(b.edges.getI(i));

        s.posInf = a.posInf || b.posInf;

        return s;
    }

    public static IntSpan intersect(IntSpan a, IntSpan b)
    {
        a.invert();
        b.invert();

        IntSpan s = union(a, b);

        a.invert();
        b.invert();
        s.invert();

        return s;
    }

    public static IntSpan diff(IntSpan a, IntSpan b)
    {
        b.invert();

        IntSpan s = intersect(a, b);

        b.invert();

        return s;
    }

    public static IntSpan xor(IntSpan a, IntSpan b)
    {
        IntSpan s = new IntSpan();

        s.negInf = a.negInf ^ b.negInf;

        int iA = 0;
        int iB = 0;

        while (iA < a.edges.size() && iB < b.edges.size())
        {
            int xA = a.edges.getI(iA);
            int xB = b.edges.getI(iB);

            if (xA < xB)
            {
                iA++;
                s.edges.add(xA);
            }
            else if (xB < xA)
            {
                iB++;
                s.edges.add(xB);
            }
            else
            {
                iA++;
                iB++;
            }
        }

        if (iA < a.edges.size())
            for (int i = iA; i < a.edges.size(); i++)
                s.edges.add(a.edges.getI(i));

        if (iB < b.edges.size())
            for (int i = iB; i < b.edges.size(); i++)
                s.edges.add(b.edges.getI(i));

        s.posInf = a.posInf ^ b.posInf;

        return s;
    }

    public static IntSpan complement(IntSpan s)
    {
        IntSpan c = (IntSpan) (s.clone());
        c.invert();
        return c;
    }

    public static boolean superset(IntSpan a, IntSpan b)
    {
        return diff(b, a).isEmpty();
    }

    public static boolean subset(IntSpan a, IntSpan b)
    {
        return diff(a, b).isEmpty();
    }

    public static boolean equal(IntSpan a, IntSpan b)
    {
        if (a.negInf ^ b.negInf) return false;
        if (a.posInf ^ b.posInf) return false;

        if (a.edges.size() != b.edges.size())
            return false;

        for (int i = 0; i < a.edges.size(); i++)
            if (a.edges.getI(i) != b.edges.getI(i))
                return false;

        return true;
    }

    public static boolean equivalent(IntSpan a, IntSpan b)
    {
        return a.size() == b.size();
    }

    public class IntSpanIterator implements Iterator
    {
        int n, nRemove, iLo, iHi;

        private IntSpanIterator()
        {
            n = 0;
            nRemove = 0;
            iLo = 0;
            iHi = 0;
        }

        private IntSpanIterator(Integer start)
        {
            n = start.intValue();

            boolean inSet = negInf;
            int i;
            for (i = 0; i < edges.size(); i++)
            {
                if (inSet && n <= edges.getI(i))
                    break;
                inSet = !inSet;
            }

            iHi = i;
            iLo = i - 1;
        }

        public boolean hasNext()
        {
            return posInf || edges.size() > 0 && n <= edges.getI(-1);
        }

        public boolean hasPrevious()
        {
            return negInf || edges.size() > 0 && edges.getI(0) < n;
        }

        public Object next()
        {
            if (!hasNext())
                throw new java.util.NoSuchElementException("Set.IntSpan.IteratorTest.next");

            int nEdges = edges.size();
            if (iHi < nEdges && n <= edges.getI(iHi) || nEdges <= iHi)
            {
                Integer i = new Integer(n);
                nRemove = n;
                n++;
                return i;
            }

            iLo += 2;
            iHi += 2;

            n = edges.getI(iLo) + 1;
            nRemove = n;
            return new Integer(n);
        }

        public Object previous()
        {
            if (!hasPrevious())
                throw new java.util.NoSuchElementException("Set.IntSpan.IteratorTest.previous");

            int nEdges = edges.size();
            if (iLo < 0 || 0 <= iLo && iLo < nEdges && edges.getI(iLo) < n)
            {
                Integer i = new Integer(n);
                nRemove = n;
                n--;
                return i;
            }

            iLo -= 2;
            iHi -= 2;

            n = edges.getI(iHi);
            nRemove = n;
            return new Integer(n);
        }

        public void remove()
        {
        }

        public String toString()
        {
            return (new Integer(n)).toString();
        }
    }
}
