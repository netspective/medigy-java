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
 * $Id: DaysOfWeekSetTest.java,v 1.1 2004-04-10 20:53:13 shahid.shah Exp $
 */

package com.netspective.chronix.set;

import java.util.Calendar;

import junit.framework.TestCase;

public class DaysOfWeekSetTest extends TestCase
{
    public void testEmpty()
    {
        DaysOfWeekSet set = new DaysOfWeekSet();
        assertFalse(set.isMember(Calendar.SUNDAY));
        assertFalse(set.isMember(Calendar.MONDAY));
        assertFalse(set.isMember(Calendar.TUESDAY));
        assertFalse(set.isMember(Calendar.WEDNESDAY));
        assertFalse(set.isMember(Calendar.THURSDAY));
        assertFalse(set.isMember(Calendar.FRIDAY));
        assertFalse(set.isMember(Calendar.SATURDAY));
    }

    public void testMondayWednesdayFriday()
    {
        DaysOfWeekSet set = new DaysOfWeekSet(new int[] { Calendar.MONDAY, Calendar.WEDNESDAY, Calendar.FRIDAY });
        assertFalse(set.isMember(Calendar.SUNDAY));
        assertTrue(set.isMember(Calendar.MONDAY));
        assertFalse(set.isMember(Calendar.TUESDAY));
        assertTrue(set.isMember(Calendar.WEDNESDAY));
        assertFalse(set.isMember(Calendar.THURSDAY));
        assertTrue(set.isMember(Calendar.FRIDAY));
        assertFalse(set.isMember(Calendar.SATURDAY));
    }

    public void testMondayThroughFridayText()
    {
        DaysOfWeekSet set = new DaysOfWeekSet(Calendar.MONDAY + "-" + Calendar.FRIDAY);
        assertFalse(set.isMember(Calendar.SUNDAY));
        assertTrue(set.isMember(Calendar.MONDAY));
        assertTrue(set.isMember(Calendar.TUESDAY));
        assertTrue(set.isMember(Calendar.WEDNESDAY));
        assertTrue(set.isMember(Calendar.THURSDAY));
        assertTrue(set.isMember(Calendar.FRIDAY));
        assertFalse(set.isMember(Calendar.SATURDAY));
    }

    public void testMondayAndWednesdayThroughFridayText()
    {
        DaysOfWeekSet set = new DaysOfWeekSet(Calendar.MONDAY + "," + Calendar.WEDNESDAY + "-" + Calendar.FRIDAY);
        assertFalse(set.isMember(Calendar.SUNDAY));
        assertTrue(set.isMember(Calendar.MONDAY));
        assertFalse(set.isMember(Calendar.TUESDAY));
        assertTrue(set.isMember(Calendar.WEDNESDAY));
        assertTrue(set.isMember(Calendar.THURSDAY));
        assertTrue(set.isMember(Calendar.FRIDAY));
        assertFalse(set.isMember(Calendar.SATURDAY));
    }

    public void testComplexText()
    {
        // we're testing that we can set and check the following:
        //    the first and third monday
        //    the second wednesday
        //    the first and third and fifth friday
        //    all the saturdays

        String spec = Calendar.MONDAY + ":1;3," + Calendar.WEDNESDAY + ":2," + Calendar.FRIDAY + ":1;3;5," + Calendar.SATURDAY;
        DaysOfWeekSet set = new DaysOfWeekSet(spec);

        // first test the days of the week
        assertFalse(set.isMember(Calendar.SUNDAY));
        assertTrue(set.isMember(Calendar.MONDAY));
        assertFalse(set.isMember(Calendar.TUESDAY));
        assertTrue(set.isMember(Calendar.WEDNESDAY));
        assertFalse(set.isMember(Calendar.THURSDAY));
        assertTrue(set.isMember(Calendar.FRIDAY));
        assertTrue(set.isMember(Calendar.SATURDAY));

        assertFalse(set.isMember(Calendar.SUNDAY, 1));
        assertFalse(set.isMember(Calendar.SUNDAY, 2));
        assertFalse(set.isMember(Calendar.SUNDAY, 3));
        assertFalse(set.isMember(Calendar.SUNDAY, 4));
        assertFalse(set.isMember(Calendar.SUNDAY, 5));

        assertTrue(set.isMember(Calendar.MONDAY, 1));
        assertFalse(set.isMember(Calendar.MONDAY, 2));
        assertTrue(set.isMember(Calendar.MONDAY, 3));
        assertFalse(set.isMember(Calendar.MONDAY, 4));
        assertFalse(set.isMember(Calendar.MONDAY, 5));

        assertFalse(set.isMember(Calendar.WEDNESDAY, 1));
        assertTrue(set.isMember(Calendar.WEDNESDAY, 2));
        assertFalse(set.isMember(Calendar.WEDNESDAY, 3));
        assertFalse(set.isMember(Calendar.WEDNESDAY, 4));
        assertFalse(set.isMember(Calendar.WEDNESDAY, 5));

        assertTrue(set.isMember(Calendar.FRIDAY, 1));
        assertFalse(set.isMember(Calendar.FRIDAY, 2));
        assertTrue(set.isMember(Calendar.FRIDAY, 3));
        assertFalse(set.isMember(Calendar.FRIDAY, 4));
        assertTrue(set.isMember(Calendar.FRIDAY, 5));

        assertTrue(set.isMember(Calendar.SATURDAY, 1));
        assertTrue(set.isMember(Calendar.SATURDAY, 2));
        assertTrue(set.isMember(Calendar.SATURDAY, 3));
        assertTrue(set.isMember(Calendar.SATURDAY, 4));
        assertTrue(set.isMember(Calendar.SATURDAY, 5));
    }
}
