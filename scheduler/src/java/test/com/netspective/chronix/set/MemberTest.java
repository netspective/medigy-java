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
 * $Id: MemberTest.java,v 1.1 2004-04-10 18:04:55 shahid.shah Exp $
 */

package com.netspective.chronix.set;

import junit.framework.TestCase;

public class MemberTest extends TestCase
{
    boolean ok;

    String[] sets = {"-", "(-)", "(-3", "3-)", "3", "3-5", "3-5,7-9"};

    int[][] isMember =
            {
                {0, 0, 0, 0, 0, 0, 0},
                {1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 0, 0, 0, 0},
                {0, 0, 1, 1, 1, 1, 1},
                {0, 0, 1, 0, 0, 0, 0},
                {0, 0, 1, 1, 1, 0, 0},
                {0, 0, 1, 1, 1, 0, 1},
            };

    String[][] inserted =
            {
                {"1        ", "2      ", "3      ", "4      ", "5      ", "6      ", "7      "},
                {"(-)      ", "(-)    ", "(-)    ", "(-)    ", "(-)    ", "(-)    ", "(-)    "},
                {"(-3      ", "(-3    ", "(-3    ", "(-4    ", "(-3,5  ", "(-3,6  ", "(-3,7  "},
                {"1,3-)    ", "2-)    ", "3-)    ", "3-)    ", "3-)    ", "3-)    ", "3-)    "},
                {"1,3      ", "2-3    ", "3      ", "3-4    ", "3,5    ", "3,6    ", "3,7    "},
                {"1,3-5    ", "2-5    ", "3-5    ", "3-5    ", "3-5    ", "3-6    ", "3-5,7  "},
                {"1,3-5,7-9", "2-5,7-9", "3-5,7-9", "3-5,7-9", "3-5,7-9", "3-9    ", "3-5,7-9"},
            };

    String[][] removed =
            {
                {"-        ", "-      ", "-      ", "-      ", "-      ", "-      ", "-      "},
                {"(-0,2-)  ", "(-1,3-)", "(-2,4-)", "(-3,5-)", "(-4,6-)", "(-5,7-)", "(-6,8-)"},
                {"(-0,2-3  ", "(-1,3  ", "(-2    ", "(-3    ", "(-3    ", "(-3    ", "(-3    "},
                {"3-)      ", "3-)    ", "4-)    ", "3,5-)  ", "3-4,6-)", "3-5,7-)", "3-6,8-)"},
                {"3        ", "3      ", "-      ", "3      ", "3      ", "3      ", "3      "},
                {"3-5      ", "3-5    ", "4-5    ", "3,5    ", "3-4    ", "3-5    ", "3-5    "},
                {"3-5,7-9  ", "3-5,7-9", "4-5,7-9", "3,5,7-9", "3-4,7-9", "3-5,7-9", "3-5,8-9"},
            };


    public MemberTest()
    {
        ok = true;
    }

    public void testMember()
    {
        for (int i = 0; i < sets.length; i++)
        {
            for (int j = 0; j < 7; j++)
            {
                int n = j + 1;
                member(sets[i], n, isMember[i][j]);
                delta(sets[i], n, new Insert(), inserted[i][j]);
                delta(sets[i], n, new Remove(), removed[i][j]);
            }
        }

        assertTrue(ok);
        //Object[] args = {ok ? "" : "not "};
        //String msg = java.text.MessageFormat.format("MemberTest {0}ok", args);
        //System.out.println(msg);
    }

    void member(String runList, int n, int isMember)
    {
        IntSpan s = new IntSpan(runList);

        if (s.isMember(n) ^ isMember == 1)
        {
            Object[] args = {runList, new Integer(n)};
            String problem = java.text.MessageFormat.format("{1} element {0}", args);
            System.out.println(problem);

            ok = false;
        }
    }

    void delta(String runList, int n, Operable op, String expected)
    {
        IntSpan s = new IntSpan(runList);
        op.delta(s, n);
        String actual = s.runList();

        if (!expected.trim().equals(actual))
        {
            Object[] args = {op, s, new Integer(n), actual};
            String problem = java.text.MessageFormat.format("{0} {1} {2} -> {3}", args);
            System.out.println(problem);
            ok = false;
        }
    }

    static interface Operable
    {
        void delta(IntSpan s, int n);
    }

    static class Insert implements Operable
    {
        public void delta(IntSpan s, int n)
        {
            s.add(n);
        }

        public String toString()
        {
            return "insert";
        }
    }

    static class Remove implements Operable
    {
        public void delta(IntSpan s, int n)
        {
            s.remove(n);
        }

        public String toString()
        {
            return "remove";
        }
    }
}
