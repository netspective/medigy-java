/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.query;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

public class QueryDefinitionJoins
{
    private List<QueryDefinitionJoin> joins = new ArrayList<QueryDefinitionJoin>();
    private Map<String, QueryDefinitionJoin> joinsByName = new HashMap<String, QueryDefinitionJoin>();
    private List<QueryDefinitionJoin> autoIncludeJoins = new ArrayList<QueryDefinitionJoin>();

    public void add(QueryDefinitionJoin join)
    {
        joins.add(join);
        joinsByName.put(join.getName(), join);
    }

    public QueryDefinitionJoin get(int i)
    {
        return joins.get(i);
    }

    public QueryDefinitionJoin get(String name)
    {
        return joinsByName.get(name);
    }

    public List getAutoIncludeJoins()
    {
        autoIncludeJoins.clear();
        for (QueryDefinitionJoin join : joins)
        {
            if(join.isAutoInclude())
                autoIncludeJoins.add(join);
        }
        return autoIncludeJoins;
    }
}
