/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.presentation.model.common;

import com.medigy.service.SearchReturnValues;
import com.medigy.service.SearchService;
import com.medigy.service.SearchServiceParameters;
import com.medigy.service.Service;
import com.medigy.service.dto.ServiceReturnValues;
import wicket.contrib.data.model.ISelectCountAndListAction;

import java.util.List;

/**
 * Action interface for a dobule action that does a count and a list selection by invoking
 * a {@link Service}. The {@link #execute(Object)} and {@link #execute(Object, int, int)} methods
 * are invoked by PageableList in the {@link com.medigy.presentation.model.common.ServiceSearchResultModel#onAttach()}
 * method call.
 */
public abstract class ServiceCountAndListAction implements ISelectCountAndListAction
{
    private final SearchService service;
    protected List<String> columnNames;

    public ServiceCountAndListAction(final SearchService service)
    {
        this.service = service;
    }

    public Service getService()
    {
        return service;
    }

    public List<String> getColumnNames()
    {
        return columnNames;
    }

    public Object execute(Object queryObject)
    {
        if (queryObject == null)
            return 0;

        ServiceReturnValues values = service.search(createSearchServiceParameters(queryObject));
        if(values.getErrorMessage() != null)
        {
            return 0;
        }

        SearchReturnValues srv = (SearchReturnValues) values;
        columnNames = srv.getSearchResults() != null ? srv.getColumnNames() : null;
        return srv.getSearchResults().size();

    }

    public abstract SearchServiceParameters createSearchServiceParameters(final Object queryObject);
    public abstract SearchServiceParameters createSearchServiceParameters(final Object queryObject, int startFromRow,
                                                                          int numberOfRows);

    public List execute(Object queryObject, final int startFromRow, int numberOfRows)
    {
        ServiceReturnValues values = service.search(createSearchServiceParameters(queryObject, startFromRow, numberOfRows));
        if(values.getErrorMessage() != null)
        {
            return null;
        }
        
        SearchReturnValues srv = (SearchReturnValues) values;

        columnNames = srv.getSearchResults() != null ? srv.getColumnNames() : null;
        //columnNames = values.getSearchResults() != null && values.getSearchResults().size() > 0 ? new ArrayList<String>(values.getSearchResults().get(0).keySet()) : null;
        return srv.getSearchResults();
    }
}
