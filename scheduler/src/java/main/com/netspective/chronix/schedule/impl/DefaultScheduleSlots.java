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
 * $Id: DefaultScheduleSlots.java,v 1.1 2004-04-14 20:44:11 shahid.shah Exp $
 */

package com.netspective.chronix.schedule.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import com.netspective.chronix.schedule.model.ScheduleManager;
import com.netspective.chronix.schedule.model.ScheduleSlot;
import com.netspective.chronix.schedule.model.ScheduleSlots;
import com.netspective.chronix.set.MinuteRangesSet;

public class DefaultScheduleSlots implements ScheduleSlots
{
    private ScheduleManager scheduleManager;
    private SortedSet sortedSlotsSet = new TreeSet();
    private ScheduleSlot earliestSlot, latestSlot;

    public DefaultScheduleSlots(ScheduleManager scheduleManager)
    {
        this.scheduleManager = scheduleManager;
    }

    public Collection getScheduleSlotsCollection()
    {
        return sortedSlotsSet;
    }

    public ScheduleSlot getEarliestSlot()
    {
        return (ScheduleSlot) sortedSlotsSet.first();
    }

    public ScheduleSlot getLatestSlot()
    {
        return (ScheduleSlot) sortedSlotsSet.last();
    }

    protected void addSlot(ScheduleSlot slot)
    {
        sortedSlotsSet.add(slot);
    }

    protected void addSlots(ScheduleSlots slots)
    {
        sortedSlotsSet.addAll(slots.getScheduleSlotsCollection());
    }

    public ScheduleSlot[] getScheduleSlots()
    {
        return (ScheduleSlot[]) sortedSlotsSet.toArray(new ScheduleSlot[sortedSlotsSet.size()]);
    }

    public String getFormattedList(String delim, String indent)
    {
        StringBuffer sb = new StringBuffer();

        for(Iterator i = sortedSlotsSet.iterator(); i.hasNext(); )
        {
            ScheduleSlot slot = (ScheduleSlot) i.next();
            if(sb.length() > 0) sb.append(delim);
            sb.append(indent);
            sb.append(slot);
        }

        return sb.toString();
    }

    public String toString()
    {
        return getClass().getName() + " ("+ sortedSlotsSet.size() +")\n" + getFormattedList("\n", "  ");
    }

    public ResolvedSlotMinutes getResolvedSlotMinutes()
    {
        final ScheduleSlot earliestSlot = getEarliestSlot();
        final MinuteRangesSet openMinutes = new MinuteRangesSet(scheduleManager.getCalendarUtils(), earliestSlot.getBeginDate(), true);
        final MinuteRangesSet closedMinutes = new MinuteRangesSet(scheduleManager.getCalendarUtils(), earliestSlot.getBeginDate(), true);

        for(Iterator i = sortedSlotsSet.iterator(); i.hasNext(); )
        {
            ScheduleSlot slot = (ScheduleSlot) i.next();
            MinuteRangesSet set = slot.isOpenSlot() ? openMinutes : closedMinutes;
            set.applyDateRange(slot.getBeginDate(), slot.getEndDate());
        }

        return new ResolvedSlotMinutes()
        {
            public MinuteRangesSet getClosedMinutes()
            {
                return closedMinutes;
            }

            public ScheduleSlot getEarliestSlot()
            {
                return earliestSlot;
            }

            public MinuteRangesSet getOpenMinutes()
            {
                return openMinutes;
            }
        };
    }
}
