/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.query;

import com.medigy.service.query.exception.QueryDefinitionException;
import com.medigy.service.query.exception.QueryDefnJoinNotFoundException;

public class QueryDefnField
{
    private String name;
    private String caption;
    private String column;
    private String selectClauseExpr;
    private String selectClauseExprAndLabel;
    private String whereClauseExpr;
    private String orderByClauseExpr;
    private String join;
    private QueryDefnJoin joinDefn;
    private QueryDefinition parentQueryDefinition;
    private boolean allowDisplay = true;

    public QueryDefnField(final String name, final String column, final QueryDefinition queryDefn)
    {
        this.name = name;
        this.column = column;
        this.parentQueryDefinition = queryDefn;
    }

    public QueryDefnField(final String name, final String column, final String join,  final QueryDefinition queryDefn)
    {
        this.name = name;
        this.column = column;
        this.join = join;
        this.parentQueryDefinition = queryDefn;
    }


    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public String getCaption()
    {
        return caption;
    }

    public void setCaption(final String caption)
    {
        this.caption = caption;
    }

    public String getColumn()
    {
        return column;
    }

    public void setColumn(final String column)
    {
        this.column = column;
    }

    public String getColumnLabel()
    {
        return caption == null ? name : caption;
    }

    public String getColumnExpr() throws QueryDefinitionException
    {
        return allowDisplay ? (selectClauseExpr != null ? selectClauseExpr : getQualifiedColName()) : null;
    }

    public String getQualifiedColName() throws QueryDefinitionException
    {
        String tableAlias = getTableAlias();
        return tableAlias != null ? (tableAlias + "." + getColumn()) : getColumn();
    }

    public String getTableName() throws QueryDefinitionException
    {
        QueryDefnJoin join = getJoin();
        return join != null ? join.getTable() : null;
    }

    public String getTableAlias() throws QueryDefinitionException
    {
        QueryDefnJoin join = getJoin();
        return join != null ? join.getName() : null;
    }

    public String getSelectClauseExpr()
    {
        return selectClauseExpr;
    }

    public void setSelectClauseExpr(final String selectClauseExpr)
    {
        this.selectClauseExpr = selectClauseExpr;
    }

    public String getSelectClauseExprAndLabel() throws QueryDefinitionException
    {
        return allowDisplay
               ? (selectClauseExprAndLabel != null
                  ? selectClauseExprAndLabel : (getColumnExpr() + " as " + getColumnLabel() + ""))
               : null;
    }

    public void setSelectClauseExprAndLabel(final String selectClauseExprAndLabel)
    {
        this.selectClauseExprAndLabel = selectClauseExprAndLabel;
    }

    public String getWhereClauseExpr() throws QueryDefinitionException
    {
        return whereClauseExpr != null ? whereClauseExpr : getQualifiedColName();
    }

    public void setWhereClauseExpr(final String whereClauseExpr)
    {
        this.whereClauseExpr = whereClauseExpr;
    }

    public String getOrderByClauseExpr()
    {
        return orderByClauseExpr;
    }

    public void setOrderByClauseExpr(final String orderByClauseExpr)
    {
        this.orderByClauseExpr = orderByClauseExpr;
    }

    public QueryDefnJoin getJoin() throws QueryDefinitionException
    {
        if(join != null && joinDefn == null)
        {
            joinDefn = parentQueryDefinition.getJoins().get(join);
            if(joinDefn == null)
                throw new QueryDefnJoinNotFoundException(parentQueryDefinition, join, "join '" + join + "' not found in field '" + getName() + "'");
        }
        return joinDefn;
    }

    public void setJoinDefn(final QueryDefnJoin join)
    {
        this.joinDefn = join;
        this.join = join.getName();
    }

    public void setJoin(final String join)
    {
        this.join = join;
    }
}
