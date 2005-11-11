package com.medigy.wicket.form.validation;

import wicket.markup.html.form.validation.AbstractValidator;
import wicket.markup.html.form.FormComponent;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateValidator extends AbstractValidator
{

    private SimpleDateFormat format;
    private boolean futureOnly;
    private boolean pastOnly;

    public static final String[] DATE_FORMATS = new String[] {
        "MM/dd/yyyy", "MM/dd/yyyy HH:mm", "MM/yyyy"
    };

    public DateValidator(String dateFormat, boolean futureOnly, boolean pastOnly)
    {
        this.futureOnly = futureOnly;
        this.pastOnly = pastOnly;

        format = new SimpleDateFormat(dateFormat);
    }

    public void validate(FormComponent formComponent)
    {
		if (!this.isValid(formComponent.getInput()))
		{
			error(formComponent);
		}
    }

    public boolean isValid(final Object value)
    {
        if(!isValidType(value))
            return false;

        Date dateValue = (Date) value;
        if(dateValue == null)
            return true;

        if(pastOnly || futureOnly)
        {
            Date now = new Date();
            if(pastOnly && dateValue.after(now))
            {
                return false;
            }
            if(futureOnly && dateValue.before(now))
            {
                return false;
            }
        }
        return true;
    }

    protected boolean isValidType(Object value)
    {
        if(value != null && !value.getClass().isAssignableFrom(Date.class))
            return false;

        return true;
    }

    public SimpleDateFormat getFormat()
    {
        return format;
    }

    public void setFormat(SimpleDateFormat format)
    {
        this.format = format;
    }

    public String format(Date date)
    {
        synchronized(format)
        {
            return format.format(date);
        }
    }

    public Date parse(String text) throws ParseException
    {
        synchronized(format)
        {
            return format.parse(text);
        }
    }

    public String toPattern()
    {
        synchronized(format)
        {
            return format.toPattern();
        }
    }
}
