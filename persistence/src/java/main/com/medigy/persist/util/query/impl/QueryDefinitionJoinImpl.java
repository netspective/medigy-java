/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.util.query.impl;

import com.medigy.persist.util.query.QueryDefinitionJoin;
import com.medigy.persist.util.query.QueryDefinition;

import java.util.List;
import java.util.ArrayList;

public class QueryDefinitionJoinImpl implements QueryDefinitionJoin
{
    private final QueryDefinition queryDefinition;
    private final Class entityClass;
    private final String name;

    private String condition;
    private String fromExpr;
    private boolean autoInclude;

    private List<QueryDefinitionJoin> implyJoins = new ArrayList<QueryDefinitionJoin>();

    public QueryDefinitionJoinImpl(final String name, final Class entityClass, final QueryDefinition queryDefinition)
    {
        this.name = name;
        this.queryDefinition = queryDefinition;
        this.entityClass = entityClass;
    }

    /**
     * Gets the entity class name
     * @return the entity class
     */
    public Class getEntityClass()
    {
        return entityClass;
    }

    /**
     * Gets the parent query definition
     */
    public QueryDefinition getOwner()
    {
        return queryDefinition;
    }

    /**
     * Gets the unique name
     */
    public String getName()
    {
        return name;
    }

    public String getCondition()
    {
        return condition;
    }

    public void setCondition(final String condition)
    {
        this.condition = condition;
    }

    public boolean isAutoInclude()
    {
        return autoInclude;
    }

    public void setAutoInclude(final boolean autoInclude)
    {
        this.autoInclude = autoInclude;
    }

    public List<QueryDefinitionJoin> getImpliedJoins()
    {
        return implyJoins;
    }

    public void addImplyJoin(final QueryDefinitionJoin join)
    {
        implyJoins.add(join);
    }

    public String getFromExpr()
    {
        return fromExpr != null ? fromExpr : (getTable() + " " + getName());
    }

    public void setFromExpr(final String expr)
    {
        this.fromExpr = expr;
    }

    /**
     * Gets the table name defined by the entity class
     * @return database table name
     */
    public String getTable()
    {
        return entityClass.getSimpleName();
    }
}
