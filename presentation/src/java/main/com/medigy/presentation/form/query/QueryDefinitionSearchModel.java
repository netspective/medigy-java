/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.presentation.form.query;

import wicket.model.AbstractReadOnlyDetachableModel;
import wicket.model.IModel;
import wicket.Component;
import wicket.contrib.data.model.PageableList;
import wicket.contrib.data.model.ISelectCountAndListAction;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.medigy.service.query.QueryDefinitionSearchService;
import com.medigy.service.person.PatientSearchQueryDefinition;
import com.medigy.service.dto.query.QueryDefinitionSearchParameters;
import com.medigy.service.dto.query.QueryDefinitionSearchReturnValues;
import com.medigy.service.dto.query.QueryDefinitionSearchCondition;
import com.medigy.service.ServiceVersion;

public class QueryDefinitionSearchModel extends AbstractReadOnlyDetachableModel
{
    private PageableList list;
    private int rowsPerPage = 10;
    private ISelectCountAndListAction countAndListAction = new PatientSearchCountAndListAction();
    private QueryDefinitionSearchFormPanel.QueryDefinitionSearchModelObject searchParameters;
    private QueryDefinitionSearchService service;

    public QueryDefinitionSearchModel(final QueryDefinitionSearchService service)
    {
        this.service = service;
    }

    public IModel getNestedModel()
    {
        return null;
    }

    protected void onAttach()
    {
        list = new PageableList(rowsPerPage, countAndListAction);
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

    public final void setSearchParameters(final QueryDefinitionSearchFormPanel.QueryDefinitionSearchModelObject params)
    {
        detach();
        this.searchParameters = params;
    }

    public QueryDefinitionSearchFormPanel.QueryDefinitionSearchModelObject getSearchParameters()
    {
        return searchParameters;
    }

    public class PatientSearchCountAndListAction implements  ISelectCountAndListAction
    {

        public PatientSearchCountAndListAction()
        {
        }

        protected void setParameters()
        {
            final QueryDefinitionSearchFormPanel.QueryDefinitionSearchModelObject searchParameters = getSearchParameters();

        }

        public Object execute(Object object)
        {
            if (searchParameters == null)
                return 0;

            final Map<String, Object> fieldValues = new HashMap<String, Object>();
            // execute the query in here
            try
            {
                final QueryDefinitionSearchReturnValues values = service.search(new QueryDefinitionSearchParameters() {
                    public Class getQueryDefinitionClass()
                    {
                        return PatientSearchQueryDefinition.class;
                    }

                    public List<QueryDefinitionSearchCondition> getConditionFieldList()
                    {
                        return  searchParameters.getConditionFieldList();
                    }

                    public List<String> getDisplayFields()
                    {
                        return searchParameters != null ? searchParameters.getDisplayFields() : null;
                    }

                    public List<String> getSortByFields()
                    {
                        return searchParameters.getSortByFields();
                    }

                    public ServiceVersion getServiceVersion()
                    {
                        return null;
                    }
                });
                if (values.getErrorMessage() != null)
                    throw new RuntimeException("Failed to search: " + values.getErrorMessage());

                return values.getSearchResults().size();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return 0;
        }

        public List execute(Object object, int startFromRow, int numberOfRows)
        {
            final Map<String, Object> fieldValues = new HashMap<String, Object>();
            // execute the query in here
            try
            {
                final QueryDefinitionSearchReturnValues values = service.search(new QueryDefinitionSearchParameters() {
                    public Class getQueryDefinitionClass()
                    {
                        return PatientSearchQueryDefinition.class;
                    }

                    public List<QueryDefinitionSearchCondition> getConditionFieldList()
                    {

                        return  searchParameters.getConditionFieldList();

                    }

                    public List<String> getDisplayFields()
                    {
                        return searchParameters.getDisplayFields();
                    }

                    public List<String> getSortByFields()
                    {
                        return searchParameters.getSortByFields();
                    }

                    public ServiceVersion getServiceVersion()
                    {
                        return null;
                    }
                });
                if (values.getErrorMessage() != null)
                    throw new RuntimeException("Failed to search: " + values.getErrorMessage());

                return values.getSearchResults();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }
    }
}
