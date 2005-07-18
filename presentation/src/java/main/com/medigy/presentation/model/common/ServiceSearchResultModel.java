/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.presentation.model.common;

import wicket.model.AbstractReadOnlyDetachableModel;
import wicket.model.IModel;
import wicket.Component;
import wicket.contrib.data.model.PageableList;

import java.util.List;

import com.medigy.service.Service;
import com.medigy.presentation.model.common.SearchFormModelObject;
import com.medigy.presentation.model.common.ServiceCountAndListAction;

/**
 * Model behind the search result. Expects to use a service to get the search results.
 */
public abstract class ServiceSearchResultModel extends AbstractReadOnlyDetachableModel
{
    private PageableList list;
    private int rowsPerPage = 10;
    private ServiceCountAndListAction countAndListAction;
    private Service service;

    public  ServiceSearchResultModel(final Service service, final ServiceCountAndListAction countAndListAction)
    {
        this.service = service;
        this.countAndListAction = countAndListAction;
    }

    protected Service getService()
    {
        return service;
    }

    public IModel getNestedModel()
    {
        return null;
    }

    protected int getRowsPerPage()
    {
        return rowsPerPage;
    }

    protected ServiceCountAndListAction getCountAndListAction()
    {
        return countAndListAction;
    }

    protected void onAttach()
    {
        list = new PageableList(getRowsPerPage(), getCountAndListAction()) {
            /**
             * Gets the object that holds the parameters for executing queries.
             * @return QueryDefinitionSearchParameters
             */
            protected Object getQueryObject()
            {
                return getSearchParameters();
            }
        };
    }

    protected void onDetach()
    {
        list = null;
    }

    protected Object onGetObject(final Component component)
    {
        return list;
    }

    public final boolean hasResults()
    {
        List results = (List)getObject(null);
        return (!results.isEmpty());
    }

    public abstract void setSearchParameters(final SearchFormModelObject params);
    public abstract SearchFormModelObject getSearchParameters();
}
