/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.util.query.impl;

import com.medigy.persist.util.query.QueryDefnCondition;
import com.medigy.persist.util.query.QueryDefinitionField;
import com.medigy.persist.util.query.SqlComparison;
import com.medigy.persist.util.query.QueryDefinition;
import com.medigy.persist.util.query.QueryDefinitionSelect;
import com.medigy.persist.util.query.QueryDefinitionConditions;
import com.medigy.persist.util.value.ValueProvider;

public class QueryDefinitionConditionImpl implements QueryDefnCondition
{
    private String name;
    private QueryDefinitionField field;
    private SqlComparison comparison;
    private boolean isJoinOnly = false;
    private String bindExpr;
    private String connector;
    private QueryDefnCondition parentCondition;
    private QueryDefinition queryDefinition;
    private QueryDefinitionSelect queryDefinitionSelect;
    private QueryDefinitionConditions childConditions;
    private boolean isRemoveIfValueNull = true;
    private ValueProvider valueProvider;

    public QueryDefinitionConditionImpl(final QueryDefinitionField field, final SqlComparison comparison)
    {
        this.field = field;
        this.comparison = comparison;
    }

    public QueryDefinitionConditionImpl(final QueryDefinitionField field, final SqlComparison comparison,
                                        final QueryDefnCondition parentCondition)
    {
        this.comparison = comparison;
        this.field = field;
        this.parentCondition = parentCondition;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public boolean isRemoveIfValueNull()
    {
        return isRemoveIfValueNull;
    }

    public void setIsRemoveIfValueNull(final boolean flag)
    {
        this.isRemoveIfValueNull = flag;
    }

    public String getConnector()
    {
        return connector;
    }

    public void setConnector(final String connector)
    {
        this.connector = connector;
    }

    public QueryDefinitionField getField()
    {
        return field;
    }

    public String getBindExpr()
    {
        return bindExpr;
    }

    public SqlComparison getComparison()
    {
        return comparison;
    }

    public boolean isJoinOnly()
    {
        return isJoinOnly;
    }

    public ValueProvider getValueProvider()
    {
        return valueProvider;
    }

    public void setValueProvider(ValueProvider provider)
    {
        this.valueProvider = provider;
    }

    public QueryDefnCondition getParentCondition()
    {
        return parentCondition;
    }

    public QueryDefinition getQueryDefinition()
    {
        return queryDefinition;
    }

    public void setQueryDefinition(final QueryDefinition queryDefinition)
    {
        this.queryDefinition = queryDefinition;
    }

    public QueryDefinitionSelect getQueryDefinitionSelect()
    {
        return queryDefinitionSelect;
    }

    public void setQueryDefinitionSelect(final QueryDefinitionSelect queryDefinitionSelect)
    {
        this.queryDefinitionSelect = queryDefinitionSelect;
    }

    public QueryDefinitionConditions getChildConditions()
    {
        return childConditions;
    }

    public void setChildConditions(final QueryDefinitionConditions childConditions)
    {
        this.childConditions = childConditions;
    }

}
