/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.presentation.form.common;

import com.medigy.presentation.form.query.DynamicSearchResultsListView;
import com.medigy.presentation.model.common.SearchFormModelObject;
import com.medigy.presentation.model.common.ServiceCountAndListAction;
import com.medigy.presentation.model.common.ServiceSearchResultModel;
import com.medigy.presentation.navigation.paging.PageableListViewNavigator;
import com.medigy.service.SearchService;
import com.medigy.wicket.DefaultApplication;
import wicket.markup.ComponentTag;
import wicket.markup.MarkupStream;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.basic.Label;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.ListView;
import wicket.markup.html.list.PageableListView;
import wicket.markup.html.panel.Panel;

import java.util.List;

public abstract class SearchResultPanel extends Panel
{
    private int rowsPerPage = 10;
    protected PageableListView resultsListView;
    protected ServiceSearchResultModel searchResultModel;
    protected SearchService service;
    protected PageableListViewNavigator navPanel;
    protected ServiceCountAndListAction countAndListAction;

    public SearchResultPanel(final String id, final Class searchServiceClass)
    {
        super(id);
        if (searchServiceClass != null)
            service = (SearchService) ((DefaultApplication) getApplication()).getService(searchServiceClass);
        initialize();
    }

    protected void initialize()
    {

        countAndListAction = createCountAndListAction();
        searchResultModel = createSearchResultModel();
        resultsListView = createSearchResultsListView(searchResultModel, rowsPerPage);
        add(resultsListView);
        add(createSearchResultHeader());
        navPanel = new PageableListViewNavigator("navigation-panel", resultsListView);
        navPanel.setVisible(false);
        add(navPanel);
    }


    protected WebMarkupContainer createSearchResultHeader()
    {
        return new WebMarkupContainer("resultsHeader")
		{
			public boolean isVisible()
			{
				return searchResultModel.hasResults();
			}

            protected void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag)
            {
                final StringBuffer buffer = new StringBuffer();
                final List<String> resultColumnNames = searchResultModel.getResultColumnNames();
                if (resultColumnNames != null)
                {
                    for (String colName : resultColumnNames)
                    {
                        buffer.append("<td> " + colName +"</td>");
                    }
                    replaceComponentTagBody(markupStream, openTag, buffer.toString());
                }
                super.onComponentTagBody(markupStream, openTag);
            }
        };
    }

    public SearchService getService()
    {
        return service;
    }

    /**
     * A callback method for the ServiceForm to call when it has been executed. The form doesn't actually invoke the
     * service but this result panel does.
     * @param formModelObject   the form model at execution time
     */
    public void onSearchExecute(final SearchFormModelObject formModelObject)
    {
        searchResultModel.setSearchParameters(formModelObject);
        navPanel.setVisible(true);
        setCurrentResultPageToFirst();
    }

    // the following are two callback methods used by the child ISelectCountAndListAction implementation to
    // get the row count and first page.
    public abstract List invokeService(final Object queryObject, final int startFromRow, final int numberOfRows);
    public abstract Object invokeService(final Object queryObject);

    protected PageableListView createSearchResultsListView(final ServiceSearchResultModel searchResultModel, final int rowsPerPage)
    {
       return new DynamicSearchResultsListView("results", searchResultModel, rowsPerPage) {
           public boolean isVisible()
           {
               return searchResultModel.hasResults();
           }
       };
    }

    protected ServiceSearchResultModel createSearchResultModel()
    {
        return new ServiceSearchResultModel(this, service, countAndListAction);
    }

    public void setCurrentResultPageToFirst()
    {
        resultsListView.setCurrentPage(0);
    }

    private int getNumberOfResults()
    {
        return ((List)resultsListView.getModelObject()).size();
    }

    /**
     * Since count and list actions invoke the actual underlying SERVICE, the
     * child classes must implement this method.
     */
    public ServiceCountAndListAction createCountAndListAction()
    {
        return new ServiceCountAndListAction(this, service);
    }

    public class SearchResultsHeaderView extends ListView
    {
        public SearchResultsHeaderView(final String id)
        {
            super(id);
        }

        public SearchResultsHeaderView(final String id, final List list)
        {
            super(id, list);
        }

        protected void populateItem(final ListItem item)
        {
            item.add(new Label("resultColumnName", item.getModelObjectAsString()));
        }
    }
}
