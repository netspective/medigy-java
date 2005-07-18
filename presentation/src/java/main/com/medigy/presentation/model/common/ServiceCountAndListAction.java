/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.presentation.model.common;

import com.medigy.service.Service;
import wicket.contrib.data.model.ISelectCountAndListAction;

import java.util.List;

/**
 * Action interface for a dobule action that does a count and a list selection by invoking
 * a {@link Service}.
 */
public abstract class ServiceCountAndListAction implements ISelectCountAndListAction
{
    private Service service;

    public ServiceCountAndListAction(final Service service)
    {
        this.service = service;
    }

    public Service getService()
    {
        return service;
    }

    public abstract List<String> getColumnNames();
}
