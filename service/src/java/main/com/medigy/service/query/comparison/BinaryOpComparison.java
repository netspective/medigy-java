/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.query.comparison;

import com.medigy.service.query.SqlComparison;
import com.medigy.service.query.QueryDefinitionSelect;
import com.medigy.service.query.QueryDefnCondition;
import com.medigy.service.query.QueryDefnStatementGenerator;
import com.medigy.service.query.exception.QueryDefinitionException;
import com.medigy.service.dto.ServiceParameters;

public abstract class BinaryOpComparison implements SqlComparison
{
    private String name;
    private String caption;
    private String sqlExpr;

    public BinaryOpComparison(final String name)
    {
        this.name = name;
    }

    public BinaryOpComparison(String name, String caption)
    {
        this.name = name;
        this.caption = caption;
    }

    public BinaryOpComparison(String name, String caption, String sqlExpr)
    {
        this.name = name;
        this.caption = caption;
        this.sqlExpr = sqlExpr;
    }

    public final String getName()
    {
        return name;
    }

    public final String getCaption()
    {
        return caption;
    }

    public String getWhereCondExpr(QueryDefinitionSelect select, QueryDefnStatementGenerator statement,
                                   QueryDefnCondition cond, final ServiceParameters params) throws QueryDefinitionException
    {
        String retString = "";
        statement.addBindParam(cond.getField(), cond.getValue(params));
        String bindExpression = cond.getBindExpr();

        if(bindExpression != null && bindExpression.length() > 0)
            retString = cond.getField().getWhereClauseExpr() + " " + sqlExpr + " " + bindExpression;
        else
            retString = cond.getField().getWhereClauseExpr() + " " + sqlExpr + " ?";
        return retString;
    }

}