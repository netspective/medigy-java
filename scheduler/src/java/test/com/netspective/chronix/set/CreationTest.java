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

public class CreationTest extends TestCase
{
    boolean ok;

    static Test[] tests =
            {
                new Test("", "-", new int[]{}),
                new Test("     ", "-", new int[]{}),
                new Test(" ( - )  ", "(-)", null),
                new Test("-2 -     -1  ", "-2--1", new int[]{-2, -1}),
                new Test("-", "-", new int[]{}),
                new Test("0", "0", new int[]{0}),
                new Test("1", "1", new int[]{1}),
                new Test("1-1", "1", new int[]{1}),
                new Test("-1", "-1", new int[]{-1}),
                new Test("1-2", "1-2", new int[]{1, 2}),
                new Test("-2--1", "-2--1", new int[]{-2, -1}),
                new Test("-2-1", "-2-1", new int[]{-2, -1, 0, 1}),
                new Test("1,2-4", "1-4", new int[]{1, 2, 3, 4}),
                new Test("1-3,4,5-7", "1-7", new int[]{1, 2, 3, 4, 5, 6, 7}),
                new Test("1-3,4", "1-4", new int[]{1, 2, 3, 4}),
                new Test("1,2,3,4,5,6,7", "1-7", new int[]{1, 2, 3, 4, 5, 6, 7}),
                new Test("1,2-)", "1-)", null),
                new Test("(-0,1-)", "(-)", null),
                new Test("(-)", "(-)", null),
                new Test("1-)", "1-)", null),
                new Test("(-1", "(-1", null),
                new Test("-3,-1-)", "-3,-1-)", null),
                new Test("(-1,3", "(-1,3", null)
            };

    public CreationTest()
    {
        ok = true;
    }

    public void testCreation()
    {

        for(int i = 0; i < tests.length; i++)
        {
            Test t = tests[i];

            // System.out.println(t.input);
            IntSpan set = new IntSpan(t.input);
            String actual = set.runList();
            String expected = t.runList;
            if(!expected.equals(actual))
                report(expected, actual);

            IntSpan set1 = (IntSpan) (set.clone());
            actual = set1.runList();
            if(!expected.equals(actual))
                report(expected, actual);

            int[] eActual = set.getElements();
            int[] eExpected = t.elements;

            if(eActual == null ^ eExpected == null ||
               eActual != null && eExpected != null &&
               !java.util.Arrays.equals(eActual, eExpected))
                report(eExpected, eActual);
        }

        assertTrue(ok);
        //Object[] args = {ok ? "" : "not "};
        //String msg = java.text.MessageFormat.format("CreationTest {0}ok", args);
        //System.out.println(msg);
    }

    void report(String expected, String actual)
    {
        Object[] args = {expected, actual};
        String problem = java.text.MessageFormat.format("{0} -> {1}", args);
        System.out.println(problem);
        ok = false;
    }

    void report(int[] expected, int[] actual)
    {
        dump(expected);
        System.out.print("-> ");
        dump(actual);
        System.out.println("");
        ok = false;
    }

    void dump(int[] a)
    {
        if(a == null)
            System.out.print("null");
        else
            for(int i = 0; i < a.length; i++)
            {
                System.out.print(a[i]);
                System.out.print(" ");
            }
    }

    static class Test
    {
        String input;
        String runList;
        int[] elements;

        Test(String input, String runList, int[] elements)
        {
            this.input = input;
            this.runList = runList;
            this.elements = elements;
        }
    }
}
