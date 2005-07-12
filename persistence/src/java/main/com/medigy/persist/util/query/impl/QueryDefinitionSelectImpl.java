/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.util.query.impl;

import com.medigy.persist.util.query.QueryDefinition;
import com.medigy.persist.util.query.QueryDefinitionConditions;
import com.medigy.persist.util.query.QueryDefinitionField;
import com.medigy.persist.util.query.QueryDefinitionSelect;
import com.medigy.persist.util.query.QueryDefinitionSortFieldReference;
import com.medigy.persist.util.query.QueryDefnCondition;
import com.medigy.persist.util.query.SqlComparison;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryDefinitionSelectImpl implements QueryDefinitionSelect
{
    private String name;
    private QueryDefinition queryDefinition;
    private boolean distinctRows;

    private Map<String, QueryDefinitionField> displayFields = new HashMap<String, QueryDefinitionField>();
    private List<QueryDefinitionSortFieldReference> orderBys = new ArrayList<QueryDefinitionSortFieldReference>();
    private Map<String, QueryDefinitionField> groupByFields = new HashMap<String, QueryDefinitionField>();
    private QueryDefinitionConditions conditions = new QueryDefinitionConditions();
    //private QueryDefnSqlWhereExpressions whereExprs = new QueryDefnSqlWhereExpressions();

    public QueryDefinitionSelectImpl(final String name, final QueryDefinition queryDefinition)
    {
        this.name = name;
        this.queryDefinition = queryDefinition;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public QueryDefinition getQueryDefinition()
    {
        return queryDefinition;
    }

    public boolean isDistinctRows()
    {
        return distinctRows;
    }

    public void setDistinctRows(final boolean distinctRows)
    {
        this.distinctRows = distinctRows;
    }

    public Map<String, QueryDefinitionField> getDisplayFields()
    {
        return displayFields;
    }

    public void setDisplayFields(final Map<String, QueryDefinitionField> displayFields)
    {
        this.displayFields = displayFields;
    }

    public void addDisplayField(final QueryDefinitionField field)
    {
        this.displayFields.put(field.getName(), field);
    }

    public Map<String, QueryDefinitionField> getGroupByFields()
    {
        return groupByFields;
    }

    public void setGroupByFields(final Map<String, QueryDefinitionField> groupByFields)
    {
        this.groupByFields = groupByFields;
    }

    public void addGroupByField(final QueryDefinitionField field)
    {
        this.groupByFields.put(field.getName(), field);
    }

    public QueryDefinitionConditions getConditions()
    {
        return conditions;
    }

    public void setConditions(final QueryDefinitionConditions conditions)
    {
        this.conditions = conditions;
    }


    public void addCondition(final QueryDefnCondition condition)
    {
        this.conditions.add(condition);
    }

    public void addOrderBy(final QueryDefinitionSortFieldReference fieldReference)
    {

    }
}
