/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.util.query;

import com.medigy.persist.util.query.exception.QueryDefinitionException;
import com.medigy.persist.util.value.ValueContext;

import java.util.List;
import java.util.Map;

public interface QueryDefinitionSelect
{
    public String getName();
    public void setName(final String name);
    public QueryDefinition getQueryDefinition();
    public boolean isDistinctRows();
    public void setDistinctRows(final boolean distinctRows);
    public List<QueryDefinitionField> getDisplayFields();
    public void setDisplayFields(final List<QueryDefinitionField> displayFields);
    public void addDisplayField(final QueryDefinitionField field);
    public Map<String, QueryDefinitionField> getGroupByFields();
    public void setGroupByFields(final Map<String, QueryDefinitionField> groupByFields);
    public void addGroupByField(final QueryDefinitionField field);

    public List<QueryDefnCondition> getConditions();
    public List<QueryDefnCondition> getUsedConditions(final QueryDefnStatementGenerator generator, final ValueContext valueContext) throws QueryDefinitionException;
    public String createSql(QueryDefnStatementGenerator stmtGen, List<QueryDefnCondition> usedConditions,
                            final ValueContext valueContext) throws QueryDefinitionException;

    public void setConditions(final List<QueryDefnCondition> conditions);
    public void addCondition(final QueryDefnCondition condition);
    public void addOrderBy(final QueryDefinitionSortBy sortBy);
    public List<QueryDefinitionSortBy> getOrderBys();
}
