/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service;

import com.medigy.persist.util.query.QueryDefnCondition;
import com.medigy.persist.util.query.QueryDefinition;
import com.medigy.persist.util.query.QueryDefinitionSelect;
import com.medigy.persist.util.query.QueryDefnStatementGenerator;
import com.medigy.persist.util.query.QueryDefinitionField;
import com.medigy.persist.util.query.exception.QueryDefinitionException;
import com.medigy.persist.util.value.ValueProvider;
import com.medigy.persist.util.value.ValueContext;
import com.medigy.persist.model.person.Person;
import com.medigy.persist.model.common.Entity;
import com.medigy.service.dto.CriteriaSearchParameters;
import com.medigy.service.dto.ServiceParameters;
import com.medigy.service.query.QueryDefinitionFactory;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.lang.reflect.InvocationTargetException;

import org.hibernate.SessionFactory;
import org.hibernate.Query;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.beanutils.PropertyUtils;

public abstract class AbstractSearchServiceImpl extends AbstractService implements SearchService
{
    private final Log log = LogFactory.getLog(AbstractSearchServiceImpl.class);

    private final Class searchClass;

    public AbstractSearchServiceImpl(final SessionFactory sessionFactory, final Class searchClass)
    {
        super(sessionFactory);
        this.searchClass = searchClass;
    }

    public SearchReturnValues search(final SearchParameters params)
    {
        final CriteriaSearchParameters searchParams = (CriteriaSearchParameters) params;
        final int rowStart = params.getStartFromRow();

        final QueryDefinition queryDefinition = QueryDefinitionFactory.getInstance().getQueryDefinition(searchClass);
        final QueryDefinitionSelect select = queryDefinition.getSelects().get("criteriaSearch");
        // all display fields in the query def will be used
        final List<QueryDefnCondition> conditionList = select.getConditions().getList();

        try
        {
            for (QueryDefnCondition cond : conditionList)
            {
                if (searchParams.getSearchCriteria().getName().equals(cond.getName()))
                {
                    final Class entityPropertyType = cond.getField().getEntityPropertyType();
                    final String searchCriteriaValue = searchParams.getSearchCriteriaValue();
                    if (Date.class.isAssignableFrom(entityPropertyType))
                    {
                        SimpleDateFormat format = new SimpleDateFormat();
                        format.applyPattern("MM/dd/yyyy");
                        final Date date = format.parse(searchCriteriaValue);
                        cond.setValueProvider(new ValueProvider() {
                            public Object getValue()
                            {
                                return date;
                            }
                        });

                    }
                    else if (Float.class.isAssignableFrom(entityPropertyType))
                    {
                        cond.setValueProvider(new ValueProvider() {
                            public Object getValue()
                            {
                                return new Float(searchCriteriaValue);
                            }
                        });

                    }
                    else
                    {
                        cond.setValueProvider(new ValueProvider() {
                            public Object getValue()
                            {
                                return searchCriteriaValue;
                            }
                        });
                    }
                }
                else
                {
                    cond.setValueProvider(null);                   
                }
            }
            return executeQuery(params, select);
        }
        catch (final Exception e)
        {
            log.error(e);
            e.printStackTrace();
            return (SearchReturnValues) createErrorResponse(params, e.getMessage());
        }
    }

    protected SearchReturnValues executeQuery(final SearchParameters params, final QueryDefinitionSelect select)
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
        for (int k=0; k < list.size(); k++)
        {
            final Object rowObject = list.get(k);
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

    public List<QueryDefnCondition> getCriteriaList()
    {
        final QueryDefinition queryDef = QueryDefinitionFactory.getInstance().getQueryDefinition(searchClass);
        return queryDef.getSelects().get("criteriaSearch").getConditions().getList();
    }
}
