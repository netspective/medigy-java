package com.medigy.wicket.form.field.type;

import wicket.markup.html.form.TextField;

public class DateTimeField extends TextField
{
    public DateTimeField(final String componentId)
    {
        super(componentId);
    }

    public void updateMode()
    {
        setModelObject(getInput());
    }

}
