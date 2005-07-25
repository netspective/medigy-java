/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.util.query;

import java.util.List;

public interface QueryDefinitionJoin
{
    public QueryDefinition getOwner();
    public String getEntityAlias();
    public Class getEntityClass();
    public String getEntityName();
    public String getTableName();

    public QueryDefinitionJoin getAssociatedJoin();
    public void setAssociatedJoin(final QueryDefinitionJoin associatedJoin);

    /**
     * Checks to see if this entity should only be included as a JOIN to some other ENTITY. If this is TRUE,
     * then this entity won't have a FROM clause of its own.
     * @return
     */
    public boolean isAssociatedJoinOnly();
    public String getAssociatedJoinExpression();
    public void setAssociatedJoinExpression(String expr);

    public String getCondition();
    public void setCondition(final String condition);

    /**
     * Whether or not to include this Entity in the FROM clause all the time
     * @return True if it is to be included all the time
     */
    public boolean isAutoInclude();
    public void setAutoInclude(final boolean flag);

    public List<QueryDefinitionJoin> getImpliedJoins();
    public void addImplyJoin(final QueryDefinitionJoin join);
    public String getFromExpr();
    public void setFromExpr(final String expr);
}
