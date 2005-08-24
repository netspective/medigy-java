package com.medigy.presentation.scheduler.component;

import java.util.Date;

public class TimeslotsComponent extends AbstractScheduleDeskComponent
{
    public TimeslotsComponent(final String componentId)
    {
        super(componentId);
    }

    public class Timeslot
    {
        private Date hour;
    }
}
