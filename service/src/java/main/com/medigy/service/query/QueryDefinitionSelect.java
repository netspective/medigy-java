/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.query;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class QueryDefinitionSelect
{
    private String name;
    private QueryDefinition queryDefinition;
    private boolean distinctRows;

    private Map<String, QueryDefnField> displayFields = new HashMap<String, QueryDefnField>();
    //private QueryDefnSortFieldReferences orderByFieldRefs = new QueryDefnSortFieldReferences();
    private Map<String, QueryDefnField> groupByFields = new HashMap<String, QueryDefnField>();
    private QueryDefinitionConditions conditions = new QueryDefinitionConditions();
    //private QueryDefnSqlWhereExpressions whereExprs = new QueryDefnSqlWhereExpressions();

    public QueryDefinitionSelect(final String name, final QueryDefinition queryDefinition)
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

    public void setQueryDefinition(final QueryDefinition queryDefinition)
    {
        this.queryDefinition = queryDefinition;
    }

    public boolean isDistinctRows()
    {
        return distinctRows;
    }

    public void setDistinctRows(final boolean distinctRows)
    {
        this.distinctRows = distinctRows;
    }

    public Map<String, QueryDefnField> getDisplayFields()
    {
        return displayFields;
    }

    public void setDisplayFields(final Map<String, QueryDefnField> displayFields)
    {
        this.displayFields = displayFields;
    }

    public void addDisplayField(final QueryDefnField field)
    {
        this.displayFields.put(field.getName(), field);
    }

    public Map<String, QueryDefnField> getGroupByFields()
    {
        return groupByFields;
    }

    public void setGroupByFields(final Map<String, QueryDefnField> groupByFields)
    {
        this.groupByFields = groupByFields;
    }

    public void addGroupByField(final QueryDefnField field)
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


    public void addCondition(final QueryDefnField field, final SqlComparison comparison,
                             final QueryDefinitionConditionValue value)
    {
        final QueryDefnCondition condition = new QueryDefnCondition(queryDefinition);
        condition.setField(field);
        condition.setComparison(comparison);
        condition.setValue(value);
        addCondition(condition);
    }
}
