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
 * $Id: GrepTest.java,v 1.1 2004-04-10 18:04:54 shahid.shah Exp $
 */

package com.netspective.chronix.set;

import com.netspective.chronix.set.IntSpan;
import junit.framework.TestCase;

public class GrepTest extends TestCase
{
    boolean ok;

    String[] sets = {"-", "(-)", "(-0", "0-)", "1", "5", "1-5", "3-7", "1-3,8,10-23", };

    static class T implements IntSpan.Testable
    {
        public boolean test(int n)
        {
            return true;
        }

        public String toString()
        {
            return "true";
        }
    }

    static class F implements IntSpan.Testable
    {
        public boolean test(int n)
        {
            return false;
        }

        public String toString()
        {
            return "false";
        }
    }

    static class Eq1 implements IntSpan.Testable
    {
        public boolean test(int n)
        {
            return n == 1;
        }

        public String toString()
        {
            return "n==1";
        }
    }

    static class Lt5 implements IntSpan.Testable
    {
        public boolean test(int n)
        {
            return n < 5;
        }

        public String toString()
        {
            return "n<5";
        }
    }

    static class Odd implements IntSpan.Testable
    {
        public boolean test(int n)
        {
            return (n & 1) == 1;
        }

        public String toString()
        {
            return "n&1";
        }
    }

    IntSpan.Testable[] predicates = {new T(), new F(), new Eq1(), new Lt5(), new Odd()};

    String[][] expected =
            {
                {"", "", "", "", ""},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {"1", "", "1", "1", "1"},
                {"5", "", "", "", "5"},
                {"1-5", "", "1", "1-4", "1,3,5"},
                {"3-7", "", "", "3-4", "3,5,7"},
                {"1-3,8,10-23", "", "1", "1-3", "1,3,11,13,15,17,19,21,23"}
            };

    public GrepTest()
    {
        ok = true;
    }

    public void testGrep()
    {
        for (int s = 0; s < sets.length; s++)
        {
            IntSpan set = new IntSpan(sets[s]);

            for (int p = 0; p < predicates.length; p++)
            {
                IntSpan.Testable test = predicates[p];
                IntSpan act = set.grep(test);
                String st = expected[s][p];
                IntSpan exp = st == null ? null : new IntSpan(st);

                if (act == null ^ exp == null ||
                        act != null && exp != null && !IntSpan.equal(act, exp))
                {
                    Object[] args = {test, set, act};
                    String problem = java.text.MessageFormat.format("grep {0} {1} -> {2}", args);
                    System.out.println(problem);

                    ok = false;
                }
            }
        }

        assertTrue(ok);
        //Object[] args = {ok ? "" : "not "};
        //String msg = java.text.MessageFormat.format("GrepTest {0}ok", args);
        //System.out.println(msg);
    }
}
