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

public class BinaryTest extends TestCase
{
    boolean ok;

    static Test tests[] =
            {
                //         A              B       U           I        X           A-B      B-A
                new Test(" -        ", "  -  ", " -      ", " -   ", " -       ", " -  ", "  -  "),
                new Test(" -        ", " (-) ", "(-)     ", " -   ", "(-)      ", " -  ", " (-) "),
                new Test("(-)       ", " (-) ", "(-)     ", "(-)  ", " -       ", " -  ", "  -  "),
                new Test("(-)       ", " (-1 ", "(-)     ", "(-1  ", "2-)      ", "2-) ", "  -  "),
                new Test("(-0       ", " 1-) ", "(-)     ", " -   ", "(-)      ", "(-0 ", " 1-) "),
                new Test("(-0       ", " 2-) ", "(-0,2-) ", " -   ", "(-0,2-)  ", "(-0 ", " 2-) "),
                new Test("(-2       ", " 0-) ", "(-)     ", "0-2  ", "(--1,3-) ", "(--1", " 3-) "),
                new Test("1         ", " 1   ", "1       ", "1    ", " -       ", " -  ", "  -  "),
                new Test("1         ", " 2   ", "1-2     ", " -   ", "1-2      ", " 1  ", "  2  "),
                new Test("3-9       ", " 1-2 ", "1-9     ", " -   ", "1-9      ", "3-9 ", " 1-2 "),
                new Test("3-9       ", " 1-5 ", "1-9     ", "3-5  ", "1-2,6-9  ", "6-9 ", " 1-2 "),
                new Test("3-9       ", " 4-8 ", "3-9     ", "4-8  ", "3,9      ", "3,9 ", "  -  "),
                new Test("3-9       ", " 5-12", "3-12    ", "5-9  ", "3-4,10-12", "3-4 ", "10-12"),
                new Test("3-9       ", "10-12", "3-12    ", " -   ", "3-12     ", "3-9 ", "10-12"),
                new Test("1-3,5,8-11", " 1-6 ", "1-6,8-11", "1-3,5", "4,6,8-11 ", "8-11", "4,6  "),
            };

    public BinaryTest()
    {
        ok = true;
    }

    public void testBinary()
    {
        for(int i = 0; i < tests.length; i++)
        {
            Test t = tests[i];

            binary(t.A, t.B, new Union(), t.U);
            binary(t.A, t.B, new Intersect(), t.I);
            binary(t.A, t.B, new XOR(), t.X);
            binary(t.A, t.B, new Diff(), t.AB);
            binary(t.B, t.A, new Diff(), t.BA);
        }

        assertTrue(ok);
        //Object[] args = {ok ? "" : "not "};
        //String msg = java.text.MessageFormat.format("BinaryTest {0}ok", args);
        //System.out.println(msg);
    }

    void binary(String rlA, String rlB, Operable op, String expected)
    {
        IntSpan A = new IntSpan(rlA);
        IntSpan B = new IntSpan(rlB);
        String actual = op.binary(A, B).runList();

        if(!expected.trim().equals(actual))
        {
            Object[] args = {A, op, B, actual};
            String problem = java.text.MessageFormat.format("{0} {1} {2} -> {3}", args);
            System.out.println(problem);
            ok = false;
        }
    }

    static interface Operable
    {
        IntSpan binary(IntSpan a, IntSpan b);
    }

    static class Union implements Operable
    {
        public IntSpan binary(IntSpan a, IntSpan b)
        {
            return IntSpan.union(a, b);
        }

        public String toString()
        {
            return "union";
        }
    }

    static class Intersect implements Operable
    {
        public IntSpan binary(IntSpan a, IntSpan b)
        {
            return IntSpan.intersect(a, b);
        }

        public String toString()
        {
            return "intersect";
        }
    }

    static class XOR implements Operable
    {
        public IntSpan binary(IntSpan a, IntSpan b)
        {
            return IntSpan.xor(a, b);
        }

        public String toString()
        {
            return "xor";
        }
    }

    static class Diff implements Operable
    {
        public IntSpan binary(IntSpan a, IntSpan b)
        {
            return IntSpan.diff(a, b);
        }

        public String toString()
        {
            return "diff";
        }
    }

    static private class Test
    {
        String A;
        String B;
        String U;
        String I;
        String X;
        String AB;
        String BA;

        Test(String A,
             String B,
             String U,
             String I,
             String X,
             String AB,
             String BA)
        {
            this.A = A;
            this.B = B;
            this.U = U;
            this.I = I;
            this.X = X;
            this.AB = AB;
            this.BA = BA;
        }
    }
}
