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

public class RelationTest extends TestCase
{
    boolean ok;

    static String sets[] =
            {
                "-", "(-)", "(-0", "0-)", "1", "5", "1-5", "3-7", "1-3,8,10-23"
            };

    static int[][] equal =
            {
                {1, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 1},
            };

    static int[][] equivalent =
            {
                {1, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 1, 1, 0, 0, 0, 0, 0},
                {0, 1, 1, 1, 0, 0, 0, 0, 0},
                {0, 1, 1, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 1, 0, 0, 0},
                {0, 0, 0, 0, 1, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 1, 0},
                {0, 0, 0, 0, 0, 0, 1, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 1},
            };

    static int[][] superset =
            {
                {1, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 1, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 1, 1, 1, 1, 1, 1},
                {1, 0, 0, 0, 1, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 1, 0, 0, 0},
                {1, 0, 0, 0, 1, 1, 1, 0, 0},
                {1, 0, 0, 0, 0, 1, 0, 1, 0},
                {1, 0, 0, 0, 1, 0, 0, 0, 1},
            };

    static int[][] subset =
            {
                {1, 1, 1, 1, 1, 1, 1, 1, 1, },
                {0, 1, 0, 0, 0, 0, 0, 0, 0, },
                {0, 1, 1, 0, 0, 0, 0, 0, 0, },
                {0, 1, 0, 1, 0, 0, 0, 0, 0, },
                {0, 1, 0, 1, 1, 0, 1, 0, 1, },
                {0, 1, 0, 1, 0, 1, 1, 1, 0, },
                {0, 1, 0, 1, 0, 0, 1, 0, 0, },
                {0, 1, 0, 1, 0, 0, 0, 1, 0, },
                {0, 1, 0, 1, 0, 0, 0, 0, 1, },
            };

    public RelationTest()
    {
        ok = true;
    }

    // System.out.println(runList);

    public void testRelation()
    {
        for(int i = 0; i < sets.length; i++)
        {
            for(int j = 0; j < sets.length; j++)
            {
                IntSpan A = new IntSpan(sets[i]);
                IntSpan B = new IntSpan(sets[j]);

                relation(A, B, new Equal(), equal[i][j]);
                relation(A, B, new Equivalent(), equivalent[i][j]);
                relation(A, B, new Subset(), subset[i][j]);
                relation(A, B, new Superset(), superset[i][j]);
            }
        }

        assertTrue(ok);
        //Object[] args = {ok ? "" : "not "};
        //String msg = java.text.MessageFormat.format("RelationTest {0}ok", args);
        //System.out.println(msg);
    }

    void relation(IntSpan A, IntSpan B, Operable op, int expected)
    {
        if(op.relation(A, B) ^ expected == 1)
        {
            Object[] args = {A, op, B};
            String problem = java.text.MessageFormat.format("{0} {1} {2}", args);
            System.out.println(problem);
            ok = false;
        }
    }

    static private interface Operable
    {
        boolean relation(IntSpan a, IntSpan b);
    }

    static class Equal implements Operable
    {
        public boolean relation(IntSpan a, IntSpan b)
        {
            return IntSpan.equal(a, b);
        }

        public String toString()
        {
            return "equal";
        }
    }

    static class Equivalent implements Operable
    {
        public boolean relation(IntSpan a, IntSpan b)
        {
            return IntSpan.equivalent(a, b);
        }

        public String toString()
        {
            return "equivalent";
        }
    }

    static class Subset implements Operable
    {
        public boolean relation(IntSpan a, IntSpan b)
        {
            return IntSpan.subset(a, b);
        }

        public String toString()
        {
            return "subset";
        }
    }

    static class Superset implements Operable
    {
        public boolean relation(IntSpan a, IntSpan b)
        {
            return IntSpan.superset(a, b);
        }

        public String toString()
        {
            return "superset";
        }
    }
}
