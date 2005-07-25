/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.util.query.impl;

import com.medigy.persist.util.query.QueryDefinitionJoin;
import com.medigy.persist.util.query.QueryDefinition;

import javax.persistence.Table;
import java.util.List;
import java.util.ArrayList;

/**
 *
 */
public class QueryDefinitionJoinImpl implements QueryDefinitionJoin
{
    private final QueryDefinition queryDefinition;
    private final Class entityClass;
    private final String entityAlias;

    private String condition;
    private String fromExpr;
    private boolean autoInclude;

    private QueryDefinitionJoin associatedJoin;
    private String associatedJoinExpression;

    private List<QueryDefinitionJoin> implyJoins = new ArrayList<QueryDefinitionJoin>();

    public QueryDefinitionJoinImpl(final String name, final Class entityClass, final QueryDefinition queryDefinition)
    {
        this.entityAlias = name;
        this.queryDefinition = queryDefinition;
        this.entityClass = entityClass;
    }

    public String getAssociatedJoinExpression()
    {
        return associatedJoinExpression;
    }

    public void setAssociatedJoinExpression(final String associatedJoinExpression)
    {
        this.associatedJoinExpression = associatedJoinExpression;
    }

    public QueryDefinitionJoin getAssociatedJoin()
    {
        return associatedJoin;
    }

    public void setAssociatedJoin(final QueryDefinitionJoin associatedJoin)
    {
        this.associatedJoin = associatedJoin;
    }

    /**
     * Checks to see if this entity should only be included as a JOIN to some other ENTITY. If this is TRUE,
     * then this entity won't have a FROM clause of its own.
     * @return
     */
    public boolean isAssociatedJoinOnly()
    {
        return associatedJoin != null ? true : false;
    }

    /**
     * Gets the entity class entityAlias
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
     * Gets the unique entityAlias
     */
    public String getEntityAlias()
    {
        return entityAlias;
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

    /**
     * Gets the FROM EXPRESSION
     * @return
     */
    public String getFromExpr()
    {
        if (isAssociatedJoinOnly())
            return null;
        else
            return fromExpr != null ? fromExpr : (getEntityName() + " " + getEntityAlias());
    }

    /**
     * Only use this to OVERRIDE the generated FROM EXPRESSION which is a combination of the
     * {@link #getEntityName} and {@link #getEntityAlias}.  Also if {@link #isAssociatedJoinOnly()}
     * returns TRUE, this method SHOULD NOT be used.
     * @param expr
     */
    public void setFromExpr(final String expr)
    {
        if (isAssociatedJoinOnly())
            throw new RuntimeException("The FROM expression cannot be set if the '" + getEntityAlias() + "' is" +
                    "used as an associated join only.");
        this.fromExpr = expr;
    }

    /**
     * Gets the entity class name
     * @return
     */
    public String getEntityName()
    {
        return entityClass.getSimpleName();
    }

    /**
     * Gets the actual SQL table entityAlias
     * @return
     */
    public String getTableName()
    {
        if (getEntityClass().isAnnotationPresent(Table.class))
            return ((Table) getEntityClass().getAnnotation(Table.class)).name();
        else
            return getEntityName();
    }
}
