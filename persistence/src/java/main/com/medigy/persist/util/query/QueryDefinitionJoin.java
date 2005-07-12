/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.util.query;

import java.util.List;

public interface QueryDefinitionJoin
{
    public QueryDefinition getOwner();
    public String getName();
    public String getCondition();
    public boolean isAutoInclude();
    public String getImplyJoinsStr();
    public List<QueryDefinitionJoin> getImpliedJoins();
    public String getFromExpr();
    public String getTable();
}
