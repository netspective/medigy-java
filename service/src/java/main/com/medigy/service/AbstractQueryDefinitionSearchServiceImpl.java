/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service;

import com.medigy.persist.model.common.Entity;
import com.medigy.persist.util.query.CompositeQueryDefinitionCondition;
import com.medigy.persist.util.query.QueryDefinition;
import com.medigy.persist.util.query.QueryDefinitionField;
import com.medigy.persist.util.query.QueryDefinitionSelect;
import com.medigy.persist.util.query.QueryDefinitionSortBy;
import com.medigy.persist.util.query.QueryDefnCondition;
import com.medigy.persist.util.query.QueryDefnStatementGenerator;
import com.medigy.persist.util.query.SqlComparison;
import com.medigy.persist.util.query.SqlComparisonFactory;
import com.medigy.persist.util.query.exception.QueryDefinitionException;
import com.medigy.persist.util.query.impl.QueryDefinitionConditionImpl;
import com.medigy.persist.util.value.ValueContext;
import com.medigy.persist.util.value.ValueProvider;
import com.medigy.service.dto.ServiceParameters;
import com.medigy.service.dto.ServiceReturnValues;
import com.medigy.service.dto.query.SearchCondition;
import com.medigy.service.query.QueryDefinitionFactory;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Abstract service class for utlizing a query definition to execute a search
 */
public abstract class AbstractQueryDefinitionSearchServiceImpl extends AbstractService implements SearchService
{
    private final Log log = LogFactory.getLog(AbstractQueryDefinitionSearchServiceImpl.class);

    public static final String DATE_PATTERN = "MM/dd/yyyy";

    private final Class queryDefinitionClass;
    private final String querySelectName;

    public AbstractQueryDefinitionSearchServiceImpl(final SessionFactory sessionFactory,
                                                    final Class<? extends QueryDefinition> searchClass,
                                                    final String querySelectName)
    {
        super(sessionFactory);
        this.queryDefinitionClass = searchClass;
        this.querySelectName = querySelectName;
    }

    /**
     * Gets the query definition class that will be used to do the search
     * @return Class that implements {@link QueryDefinition}  interface
     */
    public Class getQueryDefinitionClass()
    {
        return queryDefinitionClass;
    }

    /**
     * Gets the name of the query select to use to apply the search conditions to
     * @return  the name of the existing query select
     */
    public String getQuerySelectName()
    {
        return querySelectName;
    }


    public List<QueryDefnCondition> getCriteriaList()
    {
        final QueryDefinition queryDef = QueryDefinitionFactory.getInstance().getQueryDefinition(queryDefinitionClass);
        return queryDef.getSelects().get("criteriaSearch").getConditions();
    }

    /**
     * Set the values for all the conditions defined inside the select using the values from the
     * search parameters.
     *
     * @param select
     * @param params
     * @throws QueryDefinitionException
     * @throws ParseException
     */
    protected void useExistingQuerySelect(final QueryDefinitionSelect select, final SearchServiceParameters params) throws QueryDefinitionException, ParseException
    {
        // loop through all the DEFINED CONDITIONS of the SELECT and apply the values for them from the
        // passed in search parameters
        final List<SearchCondition> searchConditionList = params.getConditions();
        final List<QueryDefnCondition> conditionList = select.getConditions();
        for (QueryDefnCondition selectCondition : conditionList)
        {
            boolean isUsed = false;
            for (SearchCondition searchCondition: searchConditionList)
            {
                // Since this is a predefined SELECT, the QUERY DEFN CONDITION in th search parameters is expected to be non-null
                final QueryDefnCondition useCondition = searchCondition.getCondition();
                final String searchCriteriaValue = searchCondition.getFieldValue();

                if (useCondition.getName().equals(selectCondition.getName()))
                {
                    isUsed = true;
                    if (CompositeQueryDefinitionCondition.class.isAssignableFrom(useCondition.getClass()))
                    {
                        final List<QueryDefnCondition> childConditions = ((CompositeQueryDefinitionCondition) useCondition).getChildConditions();
                        StringTokenizer tokenizer = new StringTokenizer(searchCriteriaValue, ",");
                        int i=0;
                        while(tokenizer.hasMoreTokens())
                        {
                            final String s = tokenizer.nextToken();
                            final Class entityPropertyType = childConditions.get(i).getField().getEntityPropertyType();
                            assignConditionValue(childConditions.get(i), s, entityPropertyType);
                            i++;
                        }
                    }
                    else
                    {
                        final Class entityPropertyType = selectCondition.getField().getEntityPropertyType();
                        assignConditionValue(selectCondition, searchCriteriaValue, entityPropertyType);
                    }
                    break;
                }
            }
            if (!isUsed)
                selectCondition.setValueProvider(null);
        }
    }

    private void assignConditionValue(final QueryDefnCondition selectCondition, final String searchCriteriaValue, final Class entityPropertyType)
            throws ParseException
    {
        if (Date.class.isAssignableFrom(entityPropertyType))
        {
            SimpleDateFormat format = new SimpleDateFormat();
            format.applyPattern(DATE_PATTERN);
            final Date date = format.parse(searchCriteriaValue);
            selectCondition.setValueProvider(new ValueProvider() {
                public Object getValue()
                {
                    return date;
                }
            });
        }
        else if (Float.class.isAssignableFrom(entityPropertyType))
        {
            selectCondition.setValueProvider(new ValueProvider() {
                public Object getValue()
                {
                    return new Float(searchCriteriaValue);
                }
            });
        }
        else
        {
            selectCondition.setValueProvider(new ValueProvider() {
                public Object getValue()
                {
                    return searchCriteriaValue;
                }
            });
        }
    }

    protected void useNewQuerySelect(final QueryDefinitionSelect select, final SearchServiceParameters params) throws QueryDefinitionException
    {
        final List<SearchCondition> searchConditionList = params.getConditions();
        // create new Query Defn Conditions
        for (SearchCondition searchCondition: searchConditionList)
        {
            final String fieldName = searchCondition.getField();
            if (fieldName == null)
                    continue;
            final QueryDefinitionField field = select.getQueryDefinition().getField(fieldName);
            final SqlComparison comparison = SqlComparisonFactory.getInstance().getComparison(searchCondition.getFieldComparison());
            final String connector = searchCondition.getConnector();
            final String value = searchCondition.getFieldValue();
            final QueryDefnCondition cond = new QueryDefinitionConditionImpl(fieldName, field, comparison, connector);
            cond.setValueProvider(new ValueProvider() {
                public Object getValue()
                {
                    return value;
                }
            });
            select.addCondition(cond);
        }
        final List<String> displayFields = params.getDisplayFields();
        for (String displayField: displayFields)
        {
            select.addDisplayField(select.getQueryDefinition().getField(displayField));
            System.out.println(displayField);
        }

        if (params.getOrderBys() != null)
        {
            for (String orderByField : params.getOrderBys())
            {
                final QueryDefinitionField field = select.getQueryDefinition().getField(orderByField);
                select.addOrderBy(new QueryDefinitionSortBy() {
                    public boolean isDescending()
                    {
                        return false;
                    }

                    public QueryDefinitionField getField()
                    {
                        return field;
                    }

                    public String getExplicitOrderByClause()
                    {
                        return null;
                    }
                });
            }
        }
    }

    public ServiceReturnValues search(final SearchServiceParameters params)
    {
        final QueryDefinition queryDefinition =
                QueryDefinitionFactory.getInstance().getQueryDefinition(getQueryDefinitionClass());

        QueryDefinitionSelect select = null;
        try
        {
            if (getQuerySelectName() != null)
            {
                // Use a pre-existing Query select defined inside the query definition
                select = queryDefinition.getSelects().get(getQuerySelectName());
                useExistingQuerySelect(select, params);
            }
            else
            {
                // Create a brand new select but don't add it to the query definition since its a one time use select
                select = queryDefinition.createSelect("search");
                useNewQuerySelect(select,params);
            }
            return executeQuery(params, select);
        }
        catch (final Exception e)
        {
            log.error(e);
            e.printStackTrace();
            return (ServiceReturnValues) createErrorResponse(params, e.getMessage());
        }
    }

    protected ServiceReturnValues executeQuery(final SearchServiceParameters params, final QueryDefinitionSelect select)
            throws QueryDefinitionException, IllegalAccessException, InvocationTargetException, NoSuchMethodException
    {
        final List<Map<String, Object>> searchResult = new ArrayList<Map<String, Object>>();
        // construct the HQL generator
        QueryDefnStatementGenerator generator = new QueryDefnStatementGenerator(select);
        // pass the condition field values and create the HQL
        String sql = generator.generateQuery(new ValueContext() {
            // for now the value context is empty
        });
        final List<String> displayPropertyNames = new ArrayList<String>();
        final List<QueryDefinitionField> displayFields = generator.getQuerySelect().getDisplayFields();

        for (int i=0; i < displayFields.size(); i++)
        {
            displayPropertyNames.add(i, displayFields.get(i).getCaption());
        }

        final Query query = getSession().createQuery(sql);
        query.setFirstResult(params.getStartFromRow());
        query.setFetchSize(50);
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
        if (log.isInfoEnabled())
            log.info("Query Definition search query returned: " + list.size() + " row(s).");
        return extractResult(params, generator, list);
    }

    protected ServiceReturnValues extractResult(final SearchServiceParameters params, final QueryDefnStatementGenerator generator, final List queryResultList)
    {
        final List<Map<String, Object>> searchResult = new ArrayList<Map<String, Object>>();
        final List<QueryDefinitionField> displayFields = generator.getQuerySelect().getDisplayFields();

        try
        {
            for (int k=0; k < queryResultList.size(); k++)
            {
                final Object rowObject = queryResultList.get(k);
                final Map<String, Object> map = new HashMap<String, Object>();

                if (rowObject instanceof Entity)
                {
                    for (QueryDefinitionField field : displayFields)
                    {
                         map.put(field.getCaption(), PropertyUtils.getNestedProperty(rowObject, field.getEntityPropertyName()));
                    }
                }
                else if (rowObject instanceof Object[])
                {
                    for (int fieldIndex=0; fieldIndex < displayFields.size(); fieldIndex++)
                    {
                        map.put(displayFields.get(fieldIndex).getCaption(), ((Object[])rowObject)[fieldIndex]);
                    }
                }
                searchResult.add(map);
            }
        }
        catch (Exception e)
        {
            log.error(e);
            return createErrorResponse(params, e.getMessage());
        }

        final List<String> displayPropertyNames = new ArrayList<String>();
        for (int i=0; i < displayFields.size(); i++)
        {
            displayPropertyNames.add(i, displayFields.get(i).getCaption());
        }

        return new SearchReturnValues()
        {
            public List<String> getColumnNames()
            {
                return displayPropertyNames;
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

}
