/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.query;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

public class QueryDefnJoins
{
    private List<QueryDefnJoin> joins = new ArrayList<QueryDefnJoin>();
    private Map<String, QueryDefnJoin> joinsByName = new HashMap<String, QueryDefnJoin>();
    private List<QueryDefnJoin> autoIncludeJoins = new ArrayList<QueryDefnJoin>();

    public void add(QueryDefnJoin join)
    {
        joins.add(join);
        joinsByName.put(join.getName(), join);
    }

    public QueryDefnJoin get(int i)
    {
        return joins.get(i);
    }

    public QueryDefnJoin get(String name)
    {
        return joinsByName.get(name);
    }

    public List getAutoIncludeJoins()
    {
        autoIncludeJoins.clear();
        for(Iterator i = joins.iterator(); i.hasNext();)
        {
            QueryDefnJoin join = (QueryDefnJoin) i.next();
            if(join.isAutoInclude())
                autoIncludeJoins.add(join);
        }
        return autoIncludeJoins;
    }
}
