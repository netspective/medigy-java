/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.impl.person;

import com.medigy.service.SearchService;
import com.medigy.service.SearchReturnValues;
import com.medigy.service.SearchParameters;
import com.medigy.service.ServiceVersion;
import com.medigy.service.AbstractService;
import com.medigy.service.person.PatientSearchQueryDefinition;
import com.medigy.service.person.PersonSearchService;
import com.medigy.service.query.QueryDefinitionFactory;
import com.medigy.service.dto.ServiceParameters;
import com.medigy.service.dto.ServiceReturnValues;
import com.medigy.service.dto.CriteriaSearchParameters;
import com.medigy.persist.util.query.QueryDefnCondition;
import com.medigy.persist.util.query.QueryDefinition;
import com.medigy.persist.util.query.QueryDefinitionSelect;
import com.medigy.persist.util.query.QueryDefnStatementGenerator;
import com.medigy.persist.util.query.QueryDefinitionField;
import com.medigy.persist.util.value.ValueProvider;
import com.medigy.persist.util.value.ValueContext;

import java.util.List;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.ArrayList;

import org.hibernate.SessionFactory;
import org.hibernate.Query;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PersonSearchServiceImpl extends AbstractService implements PersonSearchService
{
    private final Log log = LogFactory.getLog(PersonSearchServiceImpl.class);

    public PersonSearchServiceImpl(final SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }

    public SearchReturnValues search(final SearchParameters params)
    {
        final List<Map<String, Object>> searchResult = new ArrayList<Map<String, Object>>();
        final CriteriaSearchParameters searchParams = (CriteriaSearchParameters) params;
        final int rowStart = params.getStartFromRow();

        final QueryDefinition queryDefinition = QueryDefinitionFactory.getInstance().getQueryDefinition(PatientSearchQueryDefinition.class);
        final QueryDefinitionSelect select = queryDefinition.getSelects().get("criteriaSearch");
        // all display fields in the query def will be used
        final List<QueryDefnCondition> conditionList = select.getConditions().getList();
        for (QueryDefnCondition cond : conditionList)
        {
            if (searchParams.getSearchCriteria().getName().equals(cond.getName()))
            {
                cond.setValueProvider(new ValueProvider() {
                    public Object getValue()
                    {
                        return searchParams.getSearchCriteriaValue();
                    }
                });
            }
        }

        try
        {
            // construct the HQL generator
            QueryDefnStatementGenerator generator = new QueryDefnStatementGenerator(select);
            // pass the condition field values and create the HQL
            String sql = generator.generateQuery(new ValueContext() {
                // for now the value context is empty
            });
            final List<String> columnNames = new ArrayList<String>();
            final List<QueryDefinitionField> displayFields = generator.getQuerySelect().getDisplayFields();
            for (int i=0; i < displayFields.size(); i++)
            {
                columnNames.add(i, displayFields.get(i).getColumnName());
            }
            final Query query = getSession().createQuery(sql);
            query.setFirstResult(params.getStartFromRow());
            final List bindParams = generator.getBindParams();
            System.out.println(sql + "\n" + bindParams.size());

            int i=0;
            for (Object param : bindParams)
            {
                if (param instanceof String)
                    query.setString(i, (String) param);
                else if (param instanceof Long)
                    query.setLong(i, (Long) param);
                else if (param instanceof Integer)
                    query.setInteger(i, (Integer) param);
                else if (param instanceof Date)
                    query.setDate(i, (Date) param);
                else
                    throw new RuntimeException("Unsupported bind parameter type: " + param.getClass());
                i++;
            }

            final List list = query.list();
            log.info("Query Definition search query returned: " + list.size() + " row(s).");
            for (int k=0; k < list.size(); k++)
            {
                final Map<String, Object> map = new HashMap<String, Object>();
                final Object rowObject = list.get(k);

                Object[] columns = null;
                if (rowObject instanceof Object[])
                    columns = (Object[]) rowObject;
                else
                    columns = new Object[] { rowObject };

                for (int j=0; j < columns.length; j++)
                {
                    Object fieldValue = columns[j];
                    map.put(columnNames.get(j), fieldValue);
                }
                searchResult.add(map);
            }

            return new SearchReturnValues()
            {
                public List<String> getColumnNames()
                {
                    return columnNames;
                }

                public List<Map<String, Object>> getSearchResults()
                {
                    return searchResult;
                }

                public ServiceParameters getParameters()
                {
                    return params;
                }

                public String getErrorMessage()
                {
                    return null;
                }
            };
        }
        catch (Exception e)
        {
            log.error(e);
            e.printStackTrace();
        }

        return null;
    }

    public List<QueryDefnCondition> getCriteriaList()
    {
        final QueryDefinition queryDef = QueryDefinitionFactory.getInstance().getQueryDefinition(PatientSearchQueryDefinition.class);
        return queryDef.getSelects().get("criteriaSearch").getConditions().getList();
    }

    public ServiceVersion[] getSupportedServiceVersions()
    {
        return new ServiceVersion[0];
    }

    public String[] isValid(ServiceParameters parameters)
    {
        return new String[0];
    }

    public ServiceReturnValues createErrorResponse(final ServiceParameters params, final String errorMessage)
    {
        return null;
    }
}
