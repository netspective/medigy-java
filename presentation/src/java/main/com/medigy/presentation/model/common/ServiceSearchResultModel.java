/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.presentation.model.common;

import com.medigy.presentation.form.common.SearchResultPanel;
import com.medigy.service.Service;
import wicket.Component;
import wicket.contrib.data.model.ISelectCountAndListAction;
import wicket.contrib.data.model.PageableList;
import wicket.model.AbstractReadOnlyDetachableModel;
import wicket.model.IModel;

import java.util.List;

/**
 * Model behind the search result. Expects to use a service to get the search results.
 */
public class ServiceSearchResultModel extends AbstractReadOnlyDetachableModel
{
    private SearchResultPageableList list;
    private int rowsPerPage = 10;
    private ServiceCountAndListAction countAndListAction;
    private Service service;
    private SearchFormModelObject formModelObject;
    private SearchResultPanel parent;

    public  ServiceSearchResultModel(final SearchResultPanel panel, final Service service, final ServiceCountAndListAction calAction)
    {
        this.parent = panel;
        this.service = service;
        this.countAndListAction = calAction;
    }

    public SearchResultPanel getParent()
    {
        return parent;
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

    protected void onAttach()
    {
        list = new SearchResultPageableList(this, getRowsPerPage(), countAndListAction);

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

    /**
     * Gets the result column names relative to the last time the model was loaded.
     */
    public List<String> getResultColumnNames()
   {
       return (list != null ? list.getResultColumnNames() : null);
   }

    /**
     * Sets the search input parameters. This will be needed by the count and list action!
     * @param params
     */
    public void setSearchParameters(final SearchFormModelObject params)
    {
        detach();
        this.formModelObject = params;
    }

    public SearchFormModelObject getSearchParameters()
    {
        return formModelObject;
    }

    /**
     * @see PageableList
     */
    public class SearchResultPageableList extends PageableList
    {
        protected List<String> resultColumnNames;
        private ServiceSearchResultModel searchResultModel;

        public SearchResultPageableList(final ServiceSearchResultModel searchResultModel, int pageSize, ISelectCountAndListAction countAndListAction)
        {
            super(pageSize, countAndListAction);
            this.searchResultModel = searchResultModel;
        }

        /**
         * Gets the object that holds the parameters for executing queries.
         * @return QueryDefinitionSearchParameters
         */
        protected Object getQueryObject()
        {
            return getSearchParameters();
        }

        protected List load(int startFromIndex, int numberOfElements)
        {
            final List list = super.load(startFromIndex, numberOfElements);
            // the result column names are not a part of the list
            //resultColumnNames = ((ServiceCountAndListAction) getCountAndListAction()).getColumnNames();
            return list;
        }

        public List<String> getResultColumnNames()
        {
            return resultColumnNames;
        }
    }
}
