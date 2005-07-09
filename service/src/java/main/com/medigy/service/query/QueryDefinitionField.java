/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.query;

import com.medigy.service.query.exception.QueryDefinitionException;

import java.util.List;

public interface QueryDefinitionField
{
    public String getName();
    public String getCaption();
    public void setCaption(final String caption);
    public String getColumnName();

    public String getColumnLabel();
    public String getColumnExpr() throws QueryDefinitionException;

    public String getQualifiedColName() throws QueryDefinitionException;
    public String getTableName() throws QueryDefinitionException;
    public String getTableAlias() throws QueryDefinitionException;
    public String getSelectClauseExpr();
    public void setSelectClauseExpr(final String selectClauseExpr);
    public String getSelectClauseExprAndLabel() throws QueryDefinitionException;
    public void setSelectClauseExprAndLabel(final String selectClauseExprAndLabel);

    public String getWhereClauseExpr() throws QueryDefinitionException;
    public void setWhereClauseExpr(final String whereClauseExpr);
    public String getOrderByClauseExpr();
    public void setOrderByClauseExpr(final String orderByClauseExpr);
    public QueryDefinitionJoin getJoin() throws QueryDefinitionException;
    public List<SqlComparison> getValidSqlComparisons();
    public void setValidSqlComparisons(final List<SqlComparison> comparisonList);

    public QueryDefinition getQueryDefinition();
}
