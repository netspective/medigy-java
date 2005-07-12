/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.util.query;

import com.medigy.persist.util.query.exception.QueryDefinitionException;

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

    /**
     * Implement this method only to generate the left side of the comparison explicitly in a condition.
     * For example, to get this following:
     * <pre>
     *      UPPER(lastName) = UPPER(?)
     * </pre>
     * you would implement this method and return the &quot;UPPER(lastName)&quot; string.
     * @return
     * @throws QueryDefinitionException
     */
    public String getWhereClauseExpr() throws QueryDefinitionException;
    public void setWhereClauseExpr(final String whereClauseExpr);
    public String getOrderByClauseExpr();
    public void setOrderByClauseExpr(final String orderByClauseExpr);
    public QueryDefinitionJoin getJoin() throws QueryDefinitionException;
    public List<SqlComparison> getValidSqlComparisons();
    public void setValidSqlComparisons(final List<SqlComparison> comparisonList);

    public QueryDefinition getQueryDefinition();
}
