/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.util.query.impl;

import com.medigy.persist.util.query.QueryDefinition;
import com.medigy.persist.util.query.QueryDefinitionField;
import com.medigy.persist.util.query.QueryDefinitionJoin;
import com.medigy.persist.util.query.SqlComparison;
import com.medigy.persist.util.query.exception.QueryDefinitionException;

import java.util.ArrayList;
import java.util.List;

public class QueryDefinitionFieldImpl implements QueryDefinitionField
{
    private String name;

    private String caption; // Localizable name
    private String columnName;

    private String selectClauseExpr;
    private String selectClauseExprAndLabel;
    private String whereClauseExpr;
    private String orderByClauseExpr;
    private String hqlJoinExpr;

    private QueryDefinitionJoin join;
    private QueryDefinition parentQueryDefinition;
    private boolean allowDisplay = true;
    private List<SqlComparison> validSqlComparisons = new ArrayList<SqlComparison>();

    public QueryDefinitionFieldImpl(final String name, final String column, final QueryDefinitionJoin join,  final QueryDefinition queryDefn)
    {
        this.name = name;
        this.columnName = column;
        this.join = join;
        this.parentQueryDefinition = queryDefn;
    }

    public String getName()
    {
        return name;
    }

    public String getCaption()
    {
        return caption != null ? caption : name;
    }

    public void setCaption(final String caption)
    {
        this.caption = caption;
    }

    public String getColumnName()
    {
        return columnName;
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
        return tableAlias != null ? (tableAlias + "." + getColumnName()) : getColumnName();
    }

    public String getTableName() throws QueryDefinitionException
    {
        QueryDefinitionJoin join = getJoin();
        return join != null ? join.getTable() : null;
    }

    public String getTableAlias() throws QueryDefinitionException
    {
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
                  ? selectClauseExprAndLabel : (getColumnExpr()))
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

    public QueryDefinitionJoin getJoin() throws QueryDefinitionException
    {
        return join;
    }

    /**
     * Gets all the SQL comparisons allowed for this field
     * @return list of SqlComparison
     */
    public List<SqlComparison> getValidSqlComparisons()
    {
        if (validSqlComparisons.size() == 0)
        {

        }
        return validSqlComparisons;
    }

    public QueryDefinition getQueryDefinition()
    {
        return this.parentQueryDefinition;
    }

    public String getHqlJoinExpr()
    {
        return hqlJoinExpr;
    }

    public void setHqlJoinExpr(final String hqlJoinExpr)
    {
        this.hqlJoinExpr = hqlJoinExpr;
    }

    public void setValidSqlComparisons(final List<SqlComparison> validSqlComparisons)
    {
        this.validSqlComparisons = validSqlComparisons;
    }
}
