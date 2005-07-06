/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.query;

import com.medigy.service.query.comparison.BinaryOpComparison;
import com.medigy.service.query.comparison.StartsWithComparisonIgnoreCase;
import com.medigy.service.query.comparison.EqualsComparison;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SqlComparisonFactory
{
    private static SqlComparisonFactory factory = new SqlComparisonFactory();

    private List<SqlComparison> comparisonsList = new ArrayList<SqlComparison>();
    private Map<String, SqlComparison> comparisonsMap = new HashMap<String, SqlComparison>();

    private SqlComparisonFactory()
    {
        registerComparison(new StartsWithComparisonIgnoreCase("starts-with-ignore-case"));
        registerComparison(new EqualsComparison("equals"));
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
}
