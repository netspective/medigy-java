/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.util;

import java.util.Set;
import java.util.HashSet;

public class FacadeManager
{
    private static FacadeManager manager = new FacadeManager();

    private Set<Facade> facades = new HashSet<Facade>();

    private FacadeManager()
    {

    }

    public static FacadeManager getInstance()
    {
        return manager;
    }

    public void add(final Facade facade)
    {
        facades.add(facade);
    }

    public Facade get(final Class facadeInterface)
    {
        for (Facade facade : facades)
        {
            if (facadeInterface.isAssignableFrom(facade.getClass()))
            {
                return facade;
            }
        }
        return null;
    }
}
