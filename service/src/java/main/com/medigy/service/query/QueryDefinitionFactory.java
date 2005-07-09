/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.query;

import com.medigy.service.person.PatientSearchQueryDefinition;

import java.util.Map;
import java.util.HashMap;

public class QueryDefinitionFactory
{
    private static QueryDefinitionFactory factory = new QueryDefinitionFactory();
    private Map<Class, QueryDefinition> queryDefinitions = new HashMap<Class, QueryDefinition>();

    private QueryDefinitionFactory()
    {
        addQueryDefinition(new PatientSearchQueryDefinition());
    }

    public static QueryDefinitionFactory getInstance()
    {
        return factory;
    }

    public void addQueryDefinition(final QueryDefinition queryDefn)
    {
        queryDefinitions.put(queryDefn.getClass(), queryDefn);
    }

    public QueryDefinition getQueryDefinition(final Class queryDefinitionClass)
    {
        return queryDefinitions.get(queryDefinitionClass);
    }
}
