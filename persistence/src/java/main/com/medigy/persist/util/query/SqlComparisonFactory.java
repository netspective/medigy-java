/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.util.query;

import com.medigy.persist.util.query.comparison.EqualsComparison;
import com.medigy.persist.util.query.comparison.StartsWithComparisonIgnoreCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlComparisonFactory
{
    private static SqlComparisonFactory factory = new SqlComparisonFactory();

    private List<SqlComparison> comparisonsList = new ArrayList<SqlComparison>();
    private Map<String, SqlComparison> comparisonsMap = new HashMap<String, SqlComparison>();

    private SqlComparisonFactory()
    {
        registerComparison(new StartsWithComparisonIgnoreCase());
        registerComparison(new EqualsComparison());
    }

    public static SqlComparisonFactory getInstance()
    {
        return factory;
    }

    public void registerComparison(SqlComparison comp)
    {
        comparisonsList.add(comp);
        comparisonsMap.put(comp.getName(), comp);
    }

    public SqlComparison getComparison(final String name)
    {
        return comparisonsMap.get(name);
    }

    public List<SqlComparison> listByGroup(final SqlComparison.Group group)
    {
        final List<SqlComparison> list = new ArrayList<SqlComparison>();
        for (SqlComparison comp : comparisonsList)
        {
            if (comp.getGroup().equals(group))
                list.add(comp);
        }
        return list;
    }

    public List<String> listAllNames()
    {
        return new ArrayList<String>(comparisonsMap.keySet());
    }
}
