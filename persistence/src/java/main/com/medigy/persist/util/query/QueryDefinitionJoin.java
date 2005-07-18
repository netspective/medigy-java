/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.util.query;

import java.util.List;

public interface QueryDefinitionJoin
{
    // properties REQUIRED
    public QueryDefinition getOwner();
    public String getName();
    public Class getEntityClass();

    // optional properties
    public String getCondition();
    public void setCondition(final String condition);
    public boolean isAutoInclude();
    public void setAutoInclude(final boolean flag);
    public List<QueryDefinitionJoin> getImpliedJoins();
    public void addImplyJoin(final QueryDefinitionJoin join);
    public String getFromExpr();
    public void setFromExpr(final String expr);
    public String getTable();
}
