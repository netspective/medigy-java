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
 * $Id: CardinalTest.java,v 1.1 2004-04-10 18:04:54 shahid.shah Exp $
 */

package com.netspective.chronix.set;

import junit.framework.TestCase;

public class CardinalTest extends TestCase
{
    boolean ok;

    static Test[] tests =
            {
                //		         C  E  F  N  P  I  U  min              max
                new Test("  -   ", 0, 1, 1, 0, 0, 0, 0, null, null),
                new Test(" (-)  ", -1, 0, 0, 1, 1, 1, 1, null, null),
                new Test(" (-0  ", -1, 0, 0, 1, 0, 1, 0, null, new Integer(0)),
                new Test(" 0-)  ", -1, 0, 0, 0, 1, 1, 0, new Integer(0), null),
                new Test("  1   ", 1, 0, 1, 0, 0, 0, 0, new Integer(1), new Integer(1)),
                new Test("  5   ", 1, 0, 1, 0, 0, 0, 0, new Integer(5), new Integer(5)),
                new Test(" 1,3,5", 3, 0, 1, 0, 0, 0, 0, new Integer(1), new Integer(5)),
                new Test(" 1,3-5", 4, 0, 1, 0, 0, 0, 0, new Integer(1), new Integer(5)),
                new Test("-1-5  ", 7, 0, 1, 0, 0, 0, 0, new Integer(-1), new Integer(5)),
            };

    public CardinalTest()
    {
        ok = true;
    }

    public void testCardinal()
    {

        for (int i = 0; i < tests.length; i++)
        {
            Test t = tests[i];

            // System.out.println(t.runList);
            IntSpan set = new IntSpan(t.runList);
            metric(t, "cardinality", t.cardinality, set.size());
            predicate(t, "empty", t.empty, set.isEmpty());
            predicate(t, "finite", t.finite, set.isFinite());
            predicate(t, "negInf", t.negInf, set.isNegInfite());
            predicate(t, "posInf", t.posInf, set.isPosInfite());
            predicate(t, "infinite", t.infinite, set.isInfinite());
            predicate(t, "universal", t.universal, set.isUniversal());
            metric(t, "min", t.min, set.getMinInteger());
            metric(t, "max", t.max, set.getMaxInteger());
        }

        assertTrue(ok);
        //Object[] args = {ok ? "" : "not "};
        //String msg = java.text.MessageFormat.format("CardinalTest {0}ok", args);
        //System.out.println(msg);
    }

    void metric(Test t, String name, int expected, int actual)
    {
        if (expected != actual)
        {
            Object[] args = {t.runList, name, new Integer(expected),
                             new Integer(actual)};
            String format = "{0} {1}: {2} -> {3}";
            String msg = java.text.MessageFormat.format(format, args);
            System.out.println(msg);
            ok = false;
        }
    }

    void metric(Test t, String name, Integer expected, Integer actual)
    {
        if (expected == null ^ actual == null ||
                expected != null && actual != null &&
                expected.intValue() != actual.intValue())
        {
            Object[] args = {t.runList, name, expected, actual};
            String format = "{0} {1}: {2} -> {3}";
            String msg = java.text.MessageFormat.format(format, args);
            System.out.println(msg);
            ok = false;
        }
    }

    void predicate(Test t, String name, boolean expected, boolean actual)
    {
        if (expected ^ actual)
        {
            Object[] args = {t.runList, name, new Boolean(expected),
                             new Boolean(actual)};
            String format = "{0} {1}: {2} -> {3}";
            String msg = java.text.MessageFormat.format(format, args);
            System.out.println(msg);
            ok = false;
        }
    }

    static class Test
    {
        String runList;
        int cardinality;
        boolean empty;
        boolean finite;
        boolean negInf;
        boolean posInf;
        boolean infinite;
        boolean universal;
        Integer min;
        Integer max;

        Test(String runList,
             int cardinality,
             int empty,
             int finite,
             int negInf,
             int posInf,
             int infinite,
             int universal,
             Integer min,
             Integer max)
        {
            this.runList = runList;
            this.cardinality = cardinality;
            this.empty = empty > 0 ? true : false;
            this.finite = finite > 0 ? true : false;
            this.negInf = negInf > 0 ? true : false;
            this.posInf = posInf > 0 ? true : false;
            this.infinite = infinite > 0 ? true : false;
            this.universal = universal > 0 ? true : false;
            this.min = min;
            this.max = max;
        }
    }
}
