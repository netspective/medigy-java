/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.query.impl;

import com.medigy.service.query.QueryDefinition;
import com.medigy.service.query.QueryDefinitionField;
import com.medigy.service.query.QueryDefinitionConditions;
import com.medigy.service.query.QueryDefnCondition;
import com.medigy.service.query.SqlComparison;
import com.medigy.service.query.QueryDefinitionConditionValue;
import com.medigy.service.query.QueryDefinitionSelect;

import java.util.Map;
import java.util.HashMap;

public class QueryDefinitionSelectImpl implements QueryDefinitionSelect
{
    private String name;
    private QueryDefinition queryDefinition;
    private boolean distinctRows;

    private Map<String, QueryDefinitionField> displayFields = new HashMap<String, QueryDefinitionField>();
    //private QueryDefnSortFieldReferences orderByFieldRefs = new QueryDefnSortFieldReferences();
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

    public void addCondition(final QueryDefinitionField field, final SqlComparison comparison)
    {
        final QueryDefnCondition condition = new QueryDefinitionConditionImpl(this, queryDefinition);
        condition.setField(field);
        condition.setComparison(comparison);
        this.conditions.add(condition);
    }
}
