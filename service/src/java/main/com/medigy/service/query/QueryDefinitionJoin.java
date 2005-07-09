/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.query;

import com.medigy.service.query.exception.QueryDefinitionException;

import java.util.List;

public interface QueryDefinitionJoin
{
    public QueryDefinition getOwner();
    public String getName();
    public void setName(final String name);
    public String getTableName();
    public void setTableName(final String tableName);
    public String getFromClauseExpr();
    public void setFromClauseExpr(final String fromClauseExpr);
    public String getCondition();
    public void setCondition(final String condition);
    public boolean isAutoInclude();
    public void setAutoInclude(final boolean autoInclude);
    public String getImplyJoinsStr();
    public void setImplyJoinsStr(final String implyJoinsStr);
    public List<QueryDefinitionJoin> getImpliedJoins() throws QueryDefinitionException;
    public void setImpliedJoins(final List<QueryDefinitionJoin> impliedJoins);
    public void addImpliedJoin(final QueryDefinitionJoin impliedJoin);
    public String getFromExpr();
    public String getTable();
}
