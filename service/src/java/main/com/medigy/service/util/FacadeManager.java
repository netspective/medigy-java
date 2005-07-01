/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.util;

import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.BeansException;

import java.util.Map;

/**
 * A manager class which can be used to lookup Facade implementations which are all singletons.
 * This manager hides the fact that it is using the Spring context to look up these singleton
 * facades.
 */
public class FacadeManager implements ApplicationContextAware
{
    private static FacadeManager facadeManager = new FacadeManager();
    private ApplicationContext applicationContext;

    private FacadeManager()
    {

    }

    public static FacadeManager getInstance()
    {
        return facadeManager;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        this.applicationContext = applicationContext;
    }

    public Object getFacade(final Class facadeInterface)
    {
        final Map facadeMap = applicationContext.getBeansOfType(facadeInterface);
        if (facadeMap != null && facadeMap.size() > 0)
            return facadeMap.values().toArray()[0];
        else
            return null;
    }
}
