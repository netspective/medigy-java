/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.presentation.model.common;

import com.medigy.presentation.form.common.SearchResultPanel;
import com.medigy.service.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wicket.contrib.data.model.ISelectCountAndListAction;

import java.util.List;

/**
 * Action interface for a dobule action that does a count and a list selection by invoking
 * a {@link Service}. The {@link #execute(Object)} and {@link #execute(Object, int, int)} methods
 * are invoked by PageableList in the {@link com.medigy.presentation.model.common.ServiceSearchResultModel#onAttach()}
 * method call.
 */
public class ServiceCountAndListAction implements ISelectCountAndListAction
{
    private final Log log = LogFactory.getLog(ServiceCountAndListAction.class);

    private final Service service;
    private final SearchResultPanel panel;

    public ServiceCountAndListAction(final SearchResultPanel panel, final Service service)
    {
        this.service = service;
        this.panel = panel;
    }

    public SearchResultPanel getParentPanel()
    {
        return panel;
    }

    public Service getService()
    {
        return service;
    }

    public Object execute(final Object queryObject)
    {
        if (queryObject == null)
            return 0;
        // let the parent panel handle the service invocation
        return getParentPanel().invokeService(queryObject);
    }

    public List execute(final Object queryObject, final int startFromRow, final int numberOfRows)
    {
        if (queryObject == null)
            return null;

        return getParentPanel().invokeService(queryObject, startFromRow, numberOfRows);
    }
}
