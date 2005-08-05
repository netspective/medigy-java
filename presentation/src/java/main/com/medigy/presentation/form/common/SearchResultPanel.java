/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.presentation.form.common;

import com.medigy.presentation.form.query.SearchResultsListView;
import com.medigy.presentation.model.common.SearchFormModelObject;
import com.medigy.presentation.model.common.ServiceCountAndListAction;
import com.medigy.presentation.model.common.ServiceSearchResultModel;
import com.medigy.service.SearchService;
import com.medigy.service.SearchServiceParameters;
import com.medigy.service.ServiceVersion;
import com.medigy.service.dto.query.SearchCondition;
import com.medigy.wicket.DefaultApplication;
import wicket.markup.ComponentTag;
import wicket.markup.MarkupStream;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.basic.Label;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.ListView;
import wicket.markup.html.list.PageableListViewNavigator;
import wicket.markup.html.panel.Panel;

import java.util.Arrays;
import java.util.List;

public class SearchResultPanel extends Panel
{
    private int rowsPerPage = 10;
    protected SearchResultsListView resultsListView;
    protected ServiceSearchResultModel searchResultModel;
    //protected SearchResultsHeaderView resultsHeaderView;
    protected SearchService service;
    protected PageableListViewNavigator navPanel;
    protected ServiceCountAndListAction countAndListAction;

    public SearchResultPanel(final String id, final Class searchServiceClass)
    {
        super(id);
        if (searchServiceClass != null)
            service = (SearchService) ((DefaultApplication) getApplication()).getService(searchServiceClass);

        //resultsHeaderView = new SearchResultsHeaderView("resultsHeader", new ArrayList<String>());
        searchResultModel = createSearchResultModel();
        resultsListView = createSearchResultsListView(searchResultModel, rowsPerPage);
        add(resultsListView);

        WebMarkupContainer resultsTableHeader = new WebMarkupContainer("resultsHeader")
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


        add(resultsTableHeader);
        navPanel = new PageableListViewNavigator("navigation-panel", resultsListView);
        navPanel.setVisible(false);
        add(navPanel);
    }

    public void onSearchExecute(final SearchFormModelObject formModelObject)
    {
        searchResultModel.setSearchParameters(formModelObject);
        navPanel.setVisible(true);
        setCurrentResultPageToFirst();
        //resultsHeaderView.setModel(new Model((Serializable) searchResultModel.getResultColumnNames()));
        //info(getNumberOfResults() + " results found.");
    }

    protected void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag)
    {
        super.onComponentTagBody(markupStream, openTag);
    }

    protected SearchResultsListView createSearchResultsListView(final ServiceSearchResultModel searchResultModel, final int rowsPerPage)
    {
       return new SearchResultsListView("results", searchResultModel, rowsPerPage);
    }

    protected ServiceSearchResultModel createSearchResultModel()
    {
        return new ServiceSearchResultModel(this, service)
        {
            public ServiceCountAndListAction createCountAndListAction()
            {
                return new ServiceCountAndListAction(service) {

                    public SearchServiceParameters createSearchServiceParameters(final Object queryObject)
                    {
                        final CriteriaSearchFormModelObject formModelObject = (CriteriaSearchFormModelObject) queryObject;
                        return new SearchServiceParameters()  {
                            public List<SearchCondition> getConditions()
                            {
                                final SearchCondition searchCondition = new SearchCondition();
                                searchCondition.setCondition(formModelObject.getSearchCriterias());
                                searchCondition.setFieldValue(formModelObject.getSearchCriteriaValue());
                                return Arrays.asList(searchCondition);
                            }

                            public List<String> getOrderBys()
                            {
                                return null;
                            }

                            public List<String> getDisplayFields()
                            {
                                return null;
                            }

                            public int getStartFromRow()
                            {
                                return 0;
                            }

                            public ServiceVersion getServiceVersion()
                            {
                                return null;
                            };
                        };
                    }

                    public SearchServiceParameters createSearchServiceParameters(final Object queryObject, final int startFromRow, int numberOfRows)
                    {
                        final CriteriaSearchFormModelObject formModelObject = (CriteriaSearchFormModelObject) queryObject;
                        return new SearchServiceParameters()  {
                            public List<SearchCondition> getConditions()
                            {
                                final SearchCondition searchCondition = new SearchCondition();
                                searchCondition.setCondition(formModelObject.getSearchCriterias());
                                searchCondition.setFieldValue(formModelObject.getSearchCriteriaValue());
                                return Arrays.asList(searchCondition);
                            }

                            public List<String> getDisplayFields()
                            {
                                return null;
                            }

                            public List<String> getOrderBys()
                            {
                                return null;
                            }

                            public int getStartFromRow()
                            {
                                return startFromRow;
                            }

                            public ServiceVersion getServiceVersion()
                            {
                                return null;
                            }
                        };
                    }
                };
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
