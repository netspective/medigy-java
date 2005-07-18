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
    public String getEntityPropertyName();
    public Class getEntityPropertyType() throws QueryDefinitionException;

    public String getColumnLabel();
    public String getColumnExpr() throws QueryDefinitionException;

    public String getQualifiedColName();
    public String getTableName();
    public String getTableAlias();
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

    /**
     * Returns a HQL ansi-style JOIN expression
     * @return HQL join expression
     */
    public String getHqlJoinExpr();
    public void setHqlJoinExpr(String expr);

    /**
     * Whether or not this field should be a part of the fields allowed for display in a query def
     * @return True if field is allowed for display and False if field is only for conditions
     */
    public boolean isDisplayAllowed();
    public void setDisplayAllowed(final boolean display);
}
