/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.presentation.model.query;

import com.medigy.service.query.QueryDefinitionSearchService;
import com.medigy.service.SearchReturnValues;
import com.medigy.service.dto.query.QueryDefinitionSearchParameters;
import com.medigy.service.dto.query.QueryDefinitionSearchCondition;
import com.medigy.service.ServiceVersion;
import com.medigy.service.person.PatientSearchQueryDefinition;
import com.medigy.presentation.form.query.QueryDefSearchFormModelObject;
import com.medigy.presentation.model.common.ServiceCountAndListAction;

import java.util.List;

/**
 * Action class for a dobule action that does a count and a list selection by invoking
 * the {@link QueryDefinitionSearchService}.
 */
public class QueryDefServiceCountAndListAction extends ServiceCountAndListAction
{
    private Class queryDefinitionClass;

    public QueryDefServiceCountAndListAction(final QueryDefinitionSearchService service, final Class queryDefinitionClass)
    {
        super(service);
        this.queryDefinitionClass = queryDefinitionClass;
    }

    public Object execute(Object object)
    {
        if (object == null)
            return 0;
        final QueryDefSearchFormModelObject paramsObject = (QueryDefSearchFormModelObject) object;
        // execute the query in here
        try
        {
            final SearchReturnValues values = ((QueryDefinitionSearchService) getService()).search(new QueryDefinitionSearchParameters() {
                public Class getQueryDefinitionClass()
                {
                    System.out.println(">>>>>>>>>>>>>" + queryDefinitionClass);
                    return queryDefinitionClass;
                }

                public List<QueryDefinitionSearchCondition> getConditionFieldList()
                {
                    return  paramsObject.getConditionFieldList();
                }

                public List<String> getDisplayFields()
                {
                    return paramsObject != null ? paramsObject.getDisplayFields() : null;
                }

                public List<String> getSortByFields()
                {
                    return paramsObject.getSortByFields();
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

    public List execute(Object object, final int startFromRow, int numberOfRows)
    {
        final QueryDefSearchFormModelObject paramsObject = (QueryDefSearchFormModelObject) object;
        // execute the query in here
        try
        {
            final SearchReturnValues values = ((QueryDefinitionSearchService) getService()).search(new QueryDefinitionSearchParameters() {
                public Class getQueryDefinitionClass()
                {
                    return PatientSearchQueryDefinition.class;
                }

                public List<QueryDefinitionSearchCondition> getConditionFieldList()
                {

                    return  paramsObject.getConditionFieldList();

                }

                public List<String> getDisplayFields()
                {
                    return paramsObject.getDisplayFields();
                }

                public List<String> getSortByFields()
                {
                    return paramsObject.getSortByFields();
                }

                public ServiceVersion getServiceVersion()
                {
                    return null;
                }

                public int getStartFromRow()
                {
                    return startFromRow;
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

    public List<String> getColumnNames()
    {
        return null;
    }
}
