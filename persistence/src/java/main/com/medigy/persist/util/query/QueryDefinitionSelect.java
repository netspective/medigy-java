/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.util.query;

import java.util.Map;
import java.util.List;

public interface QueryDefinitionSelect
{
    public String getName();
    public void setName(final String name);
    public QueryDefinition getQueryDefinition();
    public boolean isDistinctRows();
    public void setDistinctRows(final boolean distinctRows);
    public Map<String, QueryDefinitionField> getDisplayFields();
    public void setDisplayFields(final Map<String, QueryDefinitionField> displayFields);
    public void addDisplayField(final QueryDefinitionField field);
    public Map<String, QueryDefinitionField> getGroupByFields();
    public void setGroupByFields(final Map<String, QueryDefinitionField> groupByFields);
    public void addGroupByField(final QueryDefinitionField field);

    public QueryDefinitionConditions getConditions();
    public void setConditions(final QueryDefinitionConditions conditions);
    public void addCondition(final QueryDefnCondition condition);
    public void addOrderBy(final QueryDefinitionSortBy sortBy);
    public List<QueryDefinitionSortBy> getOrderBys();
}
