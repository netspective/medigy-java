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

import junit.framework.TestCase;

public class IteratorTest extends TestCase
{
    boolean ok;

    String[] sets = {"-", "(-)", "(-0", "0-)", "1", "5", "1-5", "3-7", "1-3,8,10-23", };
    boolean[] first = {true, false, false, true, true, true, true, true, true};
    boolean[] last = {true, false, true, false, true, true, true, true, true};
    boolean[] start = {false, true, true, true, false, false, false, false, false};

    public IteratorTest()
    {
        ok = true;
    }

    public void testIterator()
    {
        create();
        next();
        nextInf();
        prev();
        prevInf();
        start();
        // remove  ();

        assertTrue(ok);
        //Object[] args = {ok ? "" : "not "};
        //String msg = java.text.MessageFormat.format("Interator {0}ok", args);
        //System.out.println(msg);
    }

    void create()
    {
        for(int i = 0; i < sets.length; i++)
        {
            IntSpan s = new IntSpan(sets[i]);
            create(s, new First(), first[i]);
            create(s, new Last(), last[i]);
            create(s, new Start(), start[i]);
        }
    }

    void create(IntSpan s, Creatable c, boolean expected)
    {
        boolean failed = false;

        try
        {
            IntSpan.IntSpanIterator actual = c.create(s);
            if(!expected)
                failed = true;
        }
        catch(java.util.NoSuchElementException e)
        {
            if(expected)
                failed = true;
        }

        if(failed)
        {
            Object[] args = {s, c};
            String problem = java.text.MessageFormat.format("iterator {0} {1} -> failed", args);
            System.out.println(problem);

            ok = false;
        }
    }

    static interface Creatable
    {
        public IntSpan.IntSpanIterator create(IntSpan s);
    }

    static class First implements Creatable
    {
        public IntSpan.IntSpanIterator create(IntSpan s)
        {
            return s.first();
        }

        public String toString()
        {
            return "first";
        }
    }

    static class Last implements Creatable
    {
        public IntSpan.IntSpanIterator create(IntSpan s)
        {
            return s.last();
        }

        public String toString()
        {
            return "last";
        }
    }

    static class Start implements Creatable
    {
        public IntSpan.IntSpanIterator create(IntSpan s)
        {
            return s.start(0);
        }

        public String toString()
        {
            return "start";
        }
    }

    void next()
    {
        for(int i = 0; i < sets.length; i++)
        {
            IntSpan s = new IntSpan(sets[i]);
            if(s.isInfinite() || !first[i])
                continue;

            IntSpan.IntSpanIterator it = s.first();
            IntSpan s1 = new IntSpan();

            while(it.hasNext())
                s1.add(((Integer) it.next()).intValue());

            if(!IntSpan.equal(s, s1))
            {
                Object[] args = {s, s1};
                String problem = java.text.MessageFormat.format("next {0} -> {1}", args);
                System.out.println(problem);

                ok = false;
            }
        }
    }

    void nextInf()
    {
        IntSpan s = new IntSpan("0-)");

        IntSpan.IntSpanIterator it = s.first();
        IntSpan s1 = new IntSpan();
        IntSpan s100 = new IntSpan("0-99");

        for(int i = 0; i < 100; i++)
            s1.add(((Integer) it.next()).intValue());

        if(!IntSpan.equal(s1, s100))
        {
            Object[] args = {s1};
            String problem = java.text.MessageFormat.format("nextInf -> {0}", args);
            System.out.println(problem);

            ok = false;
        }
    }

    void prev()
    {
        for(int i = 0; i < sets.length; i++)
        {
            IntSpan s = new IntSpan(sets[i]);
            if(s.isInfinite() || !first[i])
                continue;

            IntSpan.IntSpanIterator it = s.last();
            IntSpan s1 = new IntSpan();

            while(it.hasPrevious())
                s1.add(((Integer) it.previous()).intValue());

            if(!IntSpan.equal(s, s1))
            {
                Object[] args = {s, s1};
                String problem = java.text.MessageFormat.format("previous {0} -> {1}", args);
                System.out.println(problem);

                ok = false;
            }
        }
    }

    void prevInf()
    {
        IntSpan s = new IntSpan("(-0");

        IntSpan.IntSpanIterator it = s.last();
        IntSpan s1 = new IntSpan();
        IntSpan s100 = new IntSpan("-99-0");

        for(int i = 0; i < 100; i++)
            s1.add(((Integer) it.previous()).intValue());

        if(!IntSpan.equal(s1, s100))
        {
            Object[] args = {s1};
            String problem = java.text.MessageFormat.format("prevInf -> {0}", args);
            System.out.println(problem);

            ok = false;
        }
    }

    void start()
    {
        IntSpan s = new IntSpan("-20--15,-9--3,-1-5,8-13,17-20");
        IntSpan sPos = IntSpan.intersect(s, new IntSpan(" 0-20"));
        IntSpan sNeg = IntSpan.intersect(s, new IntSpan("-20-0"));

        IntSpan.IntSpanIterator itPos = s.start(0);
        IntSpan s1Pos = new IntSpan();

        while(itPos.hasNext())
            s1Pos.add(((Integer) itPos.next()).intValue());

        if(!IntSpan.equal(sPos, s1Pos))
        {
            Object[] args = {s, s1Pos};
            String problem = java.text.MessageFormat.format("start(0) pos {0} -> {1}", args);
            System.out.println(problem);

            ok = false;
        }

        IntSpan.IntSpanIterator itNeg = s.start(0);
        IntSpan s1Neg = new IntSpan();

        while(itNeg.hasPrevious())
            s1Neg.add(((Integer) itNeg.previous()).intValue());

        if(!IntSpan.equal(sNeg, s1Neg))
        {
            Object[] args = {s, s1Neg};
            String problem = java.text.MessageFormat.format("start(0) neg {0} -> {1}", args);
            System.out.println(problem);

            ok = false;
        }
    }

    void remove()
    {
        class IsEven implements IntSpan.Testable
        {
            public boolean test(int n)
            {
                return (n & 1) == 0;
            }
        }

        IntSpan s = new IntSpan("-20--15,-9--3,-1-5,8-13,17-20");
        IntSpan sAll = (IntSpan) s.clone();
        IntSpan sEven = s.grep(new IsEven());

        IntSpan.IntSpanIterator it = s.first();

        while(it.hasNext())
            if((((Integer) it.next()).intValue() & 1) == 1)
                it.remove();

        if(!IntSpan.equal(s, sEven))
        {
            Object[] args = {sAll, s};
            String problem = java.text.MessageFormat.format("remove {0} -> {1}", args);
            System.out.println(problem);

            ok = false;
        }
    }
}
