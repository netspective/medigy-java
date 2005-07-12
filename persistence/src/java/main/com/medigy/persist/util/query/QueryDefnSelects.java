/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.util.query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryDefnSelects
{
    private static final Log log = LogFactory.getLog(QueryDefnSelects.class);

    private List<QueryDefinitionSelect> selects = new ArrayList<QueryDefinitionSelect>();
    private Map<String, QueryDefinitionSelect> selectsByName = new HashMap<String, QueryDefinitionSelect>();

    public QueryDefnSelects()
    {
    }

    public int size()
    {
        return selects.size();
    }

    public void add(QueryDefinitionSelect select)
    {
        selects.add(select);
        selectsByName.put(select.getName(), select);
    }

    public void add(QueryDefinitionSelect select, String[] aliases)
    {
        add(select);
        for(int i = 0; i < aliases.length; i++)
            selectsByName.put(aliases[i], select);
    }

    public QueryDefinitionSelect get(int i)
    {
        return selects.get(i);
    }

    public QueryDefinitionSelect get(String name)
    {
        QueryDefinitionSelect result = selectsByName.get(name);
        if(result == null && log.isDebugEnabled())
            log.debug("Unable to find QueryDefnSelect '" + name + "'. Available: " + selectsByName.keySet());
        return result;
    }



}
