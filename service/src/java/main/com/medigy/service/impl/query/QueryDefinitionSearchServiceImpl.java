/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.impl.query;

import com.medigy.service.AbstractService;
import com.medigy.service.ServiceVersion;
import com.medigy.service.dto.ServiceParameters;
import com.medigy.service.dto.query.QueryDefinitionSearchReturnValues;
import com.medigy.service.dto.query.QueryDefinitionSearchCondition;
import com.medigy.service.dto.query.QueryDefinitionSearchParameters;
import com.medigy.service.dto.query.QueryDefinitionSearchFormPopulateValues;
import com.medigy.persist.util.query.QueryDefinitionSortFieldReference;
import com.medigy.service.query.QueryDefinitionSearchService;
import com.medigy.service.query.QueryDefinitionFactory;
import com.medigy.persist.util.query.QueryDefinition;
import com.medigy.persist.util.query.QueryDefinitionSelect;
import com.medigy.persist.util.query.QueryDefnStatementGenerator;
import com.medigy.persist.util.query.SqlComparisonFactory;
import com.medigy.persist.util.query.QueryDefinitionField;
import com.medigy.persist.util.query.QueryDefnCondition;
import com.medigy.persist.util.query.SqlComparison;
import com.medigy.persist.util.query.exception.QueryDefinitionException;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class QueryDefinitionSearchServiceImpl extends AbstractService implements QueryDefinitionSearchService
{
    private final Log log = LogFactory.getLog(QueryDefinitionSearchServiceImpl.class);

    public QueryDefinitionSearchServiceImpl(final SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }

    public QueryDefinition getQueryDefinition(final Class queryDefClass)
    {
        return QueryDefinitionFactory.getInstance().getQueryDefinition(queryDefClass);
    }

    public QueryDefinitionSearchReturnValues search(final QueryDefinitionSearchParameters params)
    {
        final List<Map<String, Object>> searchResult = new ArrayList<Map<String, Object>>();
        final QueryDefinition queryDefiniton = QueryDefinitionFactory.getInstance().getQueryDefinition(params.getQueryDefinitionClass());

        // create a new select object on the fly
        final QueryDefinitionSelect select = queryDefiniton.createSelect("searchPerson");
        final List<String> displayFields = params.getDisplayFields();
        try
        {
            if (displayFields != null)
            {
                for (String field: displayFields)
                {
                    // add the fields to be displayed in the report
                    select.addDisplayField(queryDefiniton.getField(field));
                }
            }
            final List<QueryDefinitionSearchCondition> conditionFieldList = params.getConditionFieldList();
            final Map<String, Object> conditionFieldValues = new HashMap<String, Object>();
            for (QueryDefinitionSearchCondition condition : conditionFieldList)
            {
                if (condition.getField() == null)
                    continue;
                final QueryDefinitionField field = queryDefiniton.getField(condition.getField());
                final SqlComparison comparison = SqlComparisonFactory.getInstance().getComparison(condition.getFieldComparison());
                final String connector = condition.getConnector();
                select.addCondition(new QueryDefnCondition() {
                    public boolean isRemoveIfValueNull()
                    {
                        return true;
                    }

                    public String getConnectorSql()
                    {
                        return connector;
                    }

                    public QueryDefinitionField getField()
                    {
                        return field;
                    }

                    public String getBindExpr()
                    {
                        return null;
                    }

                    public SqlComparison getComparison()
                    {
                        return comparison;
                    }

                    public boolean isJoinOnly()
                    {
                        return false;
                    }
                });
                // TODO: Need to convert the String values into column type specific values!
                conditionFieldValues.put(condition.getField(), condition.getFieldValue());
            }

            for (String orderByField : params.getSortByFields())
            {
                final QueryDefinitionField field = queryDefiniton.getField(orderByField);
                select.addOrderBy(new QueryDefinitionSortFieldReference() {
                    public QueryDefinitionField getField()
                    {
                        return field;
                    }

                    public OrderBy getOrderBy()
                    {
                        return null;
                    }
                });
            }

            // construct the HQL generator
            QueryDefnStatementGenerator generator = new QueryDefnStatementGenerator(select);
            // pass the condition field values and create the HQL
            String sql = generator.generateQuery(conditionFieldValues);
            final Query query = getSession().createQuery(sql);
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
                final Object[] columns = (Object[]) list.get(k);
                for (int j=0; j < columns.length; j++)
                {
                    String displayFieldName = displayFields.get(j);
                    Object fieldValue = columns[j];
                    map.put(queryDefiniton.getField(displayFieldName).getCaption(), fieldValue);
                }
                searchResult.add(map);
            }
        }
        catch (QueryDefinitionException e)
        {
            log.error(ExceptionUtils.getStackTrace(e));
            return createErrorResponse(params, e.getMessage());
        }

        return new QueryDefinitionSearchReturnValues()
        {
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

    public QueryDefinitionSearchFormPopulateValues getAvailableSearchParameters(final Class queryDefinitionClass)
    {
        final QueryDefinition queryDefinition = QueryDefinitionFactory.getInstance().getQueryDefinition(queryDefinitionClass);

        return new QueryDefinitionSearchFormPopulateValues() {

            public Map<String, QueryDefinitionField> getSortByFields()
            {
                return queryDefinition.getFields();
            }

            public List<String>getConnectors()
            {
                return queryDefinition.getConnectors();
            }

            public Map<String, QueryDefinitionField> getConditionFields()
            {
                return queryDefinition.getFields();
            }

            public Map<String,QueryDefinitionField> getDisplayFields()
            {
                return queryDefinition.getFields();
            }

            public ServiceParameters getParameters()
            {
                return null;
            }

            public String getErrorMessage()
            {
                return null;
            }
        };
    }

    public ServiceVersion[] getSupportedServiceVersions()
    {
        return new ServiceVersion[0];
    }

    public String[] isValid(ServiceParameters parameters)
    {
        return new String[0];
    }

    public QueryDefinitionSearchReturnValues createErrorResponse(final ServiceParameters params, final String errorMessage)
    {
        return new QueryDefinitionSearchReturnValues() {
            public ServiceParameters getParameters()
            {
                return  params;
            }

            public List<Map<String, Object>> getSearchResults()
            {
                return null;
            }

            public String getErrorMessage()
            {
                return errorMessage;
            }
        };
    }
}
