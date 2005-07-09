/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.wicket.page;

import wicket.Component;

import java.lang.reflect.InvocationTargetException;

public abstract class DefaultReportPage extends AuthenticatedWebPage
{
    public DefaultReportPage(final Class reportPanelClass, final Class reportClass)
    {
        try
        {
            final Component formPanel =
                    (Component) reportPanelClass.getConstructor(new Class[] { String.class }).newInstance("form-panel");
            add(formPanel);
        }
        catch (InstantiationException e)
        {
            throw new RuntimeException(e);
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
        catch (InvocationTargetException e)
        {
            throw new RuntimeException(e);
        }
        catch (NoSuchMethodException e)
        {
            throw new RuntimeException(e);
        }
    }
}
