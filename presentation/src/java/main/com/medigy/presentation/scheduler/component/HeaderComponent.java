package com.medigy.presentation.scheduler.component;

import com.medigy.persist.model.person.Person;
import com.medigy.persist.model.party.Facility;

import java.util.Date;
import java.util.List;
import java.util.Iterator;

public class HeaderComponent extends AbstractScheduleDeskComponent
{
    private Date startDate;
    private Date endDate;

    private List<Person> physicians;
    private List<Facility> facilities;

    public HeaderComponent(final String componentId)
    {
        super(componentId);
    }

    public Date getStartDate()
    {
        return this.startDate;
    }

    public void setStartDate(final Date startDate)
    {
        this.startDate = startDate;
    }

    public Date getEndDate()
    {
        return this.endDate;
    }

    public void setEndDate(final Date endDate)
    {
        this.endDate = endDate;
    }

    public String getPhysicians()
    {
        StringBuilder builder = new StringBuilder();
        Iterator iter = physicians.iterator();

        while(iter.hasNext())
        {
            builder.append(((Person)iter.next()).getFullName());
            if(iter.hasNext())
                builder.append(", ");
        }

        return builder.toString();
    }

    public String getFacilities()
    {
        StringBuilder builder = new StringBuilder();
        Iterator iter = facilities.iterator();

        while(iter.hasNext())
        {
            builder.append(((Facility)iter.next()).getDescription());
            if(iter.hasNext())
                builder.append(", ");
        }

        return builder.toString();
    }
}
