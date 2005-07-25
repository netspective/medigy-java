/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.presentation.form.common;

import wicket.markup.html.panel.Panel;
import wicket.markup.html.list.PageableListViewNavigator;
import wicket.markup.html.list.ListView;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.basic.Label;
import wicket.model.Model;
import com.medigy.presentation.form.query.SearchResultsListView;
import com.medigy.presentation.model.common.ServiceSearchResultModel;
import com.medigy.presentation.model.common.ServiceCountAndListAction;
import com.medigy.presentation.model.common.SearchFormModelObject;
import com.medigy.service.SearchService;
import com.medigy.service.SearchReturnValues;
import com.medigy.service.ServiceVersion;
import com.medigy.service.dto.CriteriaSearchParameters;
import com.medigy.persist.util.query.QueryDefnCondition;
import com.medigy.wicket.DefaultApplication;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.Serializable;

public class SearchResultPanel extends Panel
{
    private int rowsPerPage = 10;
    protected SearchResultsListView resultsListView;
    protected ServiceSearchResultModel searchResultModel;
    protected SearchResultsHeaderView resultsHeaderView;
    protected SearchService service;
    protected PageableListViewNavigator navPanel;
    protected ServiceCountAndListAction countAndListAction;

    public SearchResultPanel(final String id, final Class searchServiceClass)
    {
        super(id);
        this.service = (SearchService) ((DefaultApplication) getApplication()).getService(searchServiceClass);

        searchResultModel = createSearchResultModel();
        resultsListView = createSearchResultsListView(searchResultModel, rowsPerPage);
        add(resultsListView);

        resultsHeaderView = new SearchResultsHeaderView("resultsHeader");
        add(resultsHeaderView);
        navPanel = new PageableListViewNavigator("navigation-panel", resultsListView);
        navPanel.setVisible(false);
        add(navPanel);
    }

    public void onSearchExecute(final SearchFormModelObject formModelObject)
    {
        searchResultModel.setSearchParameters(formModelObject);
        navPanel.setVisible(true);
        setCurrentResultPageToFirst();
        resultsHeaderView.setModel(new Model((Serializable) countAndListAction.getColumnNames()));
        info(getNumberOfResults() + " results found.");
    }

    protected SearchResultsListView createSearchResultsListView(final ServiceSearchResultModel searchResultModel, final int rowsPerPage)
    {
       return new SearchResultsListView("results", searchResultModel, rowsPerPage);
    }

    protected ServiceSearchResultModel createSearchResultModel()
    {
        countAndListAction = createCountAndListAction();
        return new ServiceSearchResultModel(service, countAndListAction) {
            private CriteriaSearchFormModelObject formModelObject;

            public void setSearchParameters(final SearchFormModelObject params)
            {
                detach();
                this.formModelObject = (CriteriaSearchFormModelObject) params;
            }

            public SearchFormModelObject getSearchParameters()
            {
                return formModelObject;
            }
        };
    }

    public void setCurrentResultPageToFirst()
    {
        resultsListView.setCurrentPage(0);
    }

    private int getNumberOfResults()
    {
        return ((List)resultsListView.getModelObject()).size();
    }

    public ServiceCountAndListAction createCountAndListAction()
    {
        return new ServiceCountAndListAction(service) {
            private List<String> columnNames;

            public List<String> getColumnNames()
            {
                return columnNames;
            }

            public Object execute(Object queryObject)
            {
                if (queryObject == null)
                    return 0;
                final CriteriaSearchFormModelObject formModelObject = (CriteriaSearchFormModelObject) queryObject;
                SearchReturnValues values = service.search(new CriteriaSearchParameters()  {
                    public QueryDefnCondition getSearchCriteria()
                    {
                        return formModelObject.getSearchCriterias();
                    }

                    public String getSearchCriteriaValue()
                    {
                        return formModelObject.getSearchCriteriaValue();
                    }

                    public int getStartFromRow()
                    {
                        return 0;
                    }

                    public ServiceVersion getServiceVersion()
                    {
                        return null;
                    }
                });
                columnNames = values.getSearchResults() != null ? values.getColumnNames() : null;
                return values.getSearchResults().size();

            }

            public List execute(Object queryObject, final int startFromRow, int numberOfRows)
            {
                final CriteriaSearchFormModelObject formModelObject = (CriteriaSearchFormModelObject) queryObject;

                SearchReturnValues values = service.search(new CriteriaSearchParameters()  {
                    public QueryDefnCondition getSearchCriteria()
                    {
                        return formModelObject.getSearchCriterias();
                    }

                    public String getSearchCriteriaValue()
                    {
                        return formModelObject.getSearchCriteriaValue();
                    }

                    public int getStartFromRow()
                    {
                        return startFromRow;
                    }

                    public ServiceVersion getServiceVersion()
                    {
                        return null;
                    }
                });
                columnNames = values.getSearchResults() != null ? values.getColumnNames() : null;
                resultsListView.setColumnNames(columnNames);
                //columnNames = values.getSearchResults() != null && values.getSearchResults().size() > 0 ? new ArrayList<String>(values.getSearchResults().get(0).keySet()) : null;
                return values.getSearchResults();
            }
        };
    }



    private class SearchResultsHeaderView extends ListView
    {
        public SearchResultsHeaderView(final String id)
        {
            super(id);
        }

        protected void populateItem(final ListItem item)
        {
            item.add(new Label("resultColumnName", item.getModelObjectAsString()));
        }
    }
}
