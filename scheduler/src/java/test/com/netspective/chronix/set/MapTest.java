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

public class MapTest extends TestCase
{
    boolean ok;

    String[] sets = {"-", "(-)", "(-0", "0-)", "1", "5", "1-5", "3-7", "1-3,8,10-23", };

    static class Null implements IntSpan.Mappable
    {
        public int[] map(int n)
        {
            return new int[]{};
        }

        public String toString()
        {
            return "null";
        }
    }

    static class One implements IntSpan.Mappable
    {
        public int[] map(int n)
        {
            return new int[]{1};
        }

        public String toString()
        {
            return "1";
        }
    }

    static class I implements IntSpan.Mappable
    {
        public int[] map(int n)
        {
            return new int[]{n};
        }

        public String toString()
        {
            return "n";
        }
    }

    static class Neg implements IntSpan.Mappable
    {
        public int[] map(int n)
        {
            return new int[]{-n};
        }

        public String toString()
        {
            return "-n";
        }
    }

    static class P5 implements IntSpan.Mappable
    {
        public int[] map(int n)
        {
            return new int[]{n + 5};
        }

        public String toString()
        {
            return "n+5";
        }
    }

    static class MP implements IntSpan.Mappable
    {
        public int[] map(int n)
        {
            return new int[]{-n, n};
        }

        public String toString()
        {
            return "-n, n";
        }
    }

    static class Mod5 implements IntSpan.Mappable
    {
        public int[] map(int n)
        {
            return new int[]{n % 5};
        }

        public String toString()
        {
            return "n%5";
        }
    }

    IntSpan.Mappable[] maps = {new Null(), new One(), new I(), new Neg(), new P5(), new MP(), new Mod5()};

    String[][] expected =
            {
                {"", "", "", "", "", "", ""},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {"", "1", "1", "-1", "6", "-1,1", "1"},
                {"", "1", "5", "-5", "10", "-5,5", "0"},
                {"", "1", "1-5", "-5--1", "6-10", "-5--1,1-5", "0-4"},
                {"", "1", "3-7", "-7--3", "8-12", "-7--3,3-7", "0-4"},
                {"", "1", "1-3,8,10-23", "-23--10,-8,-3--1", "6-8,13,15-28", "-23--10,-8,-3--1,1-3,8,10-23", "0-4"},
            };

    public MapTest()
    {
        ok = true;
    }

    public void testMap()
    {
        for(int s = 0; s < sets.length; s++)
        {
            IntSpan set = new IntSpan(sets[s]);

            for(int m = 0; m < maps.length; m++)
            {
                IntSpan.Mappable map = maps[m];
                IntSpan act = set.map(map);
                String st = expected[s][m];
                IntSpan exp = st == null ? null : new IntSpan(st);

                if(act == null ^ exp == null ||
                   act != null && exp != null && !IntSpan.equal(act, exp))
                {
                    Object[] args = {map, set, act};
                    String problem = java.text.MessageFormat.format("map {0} {1} -> {2}", args);
                    System.out.println(problem);

                    ok = false;
                }
            }
        }

        assertTrue(ok);
        //Object[] args = {ok ? "" : "not "};
        //String msg = java.text.MessageFormat.format("MapTest {0}ok", args);
        //System.out.println(msg);
    }
}
