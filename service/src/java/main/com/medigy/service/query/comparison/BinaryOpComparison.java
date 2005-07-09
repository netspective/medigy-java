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
    private String sqlExpr;
    private Group group;

    public BinaryOpComparison(final Group group)
    {
        this.group = group;
    }

    public BinaryOpComparison(String sqlExpr, final Group group)
    {
        this.sqlExpr = sqlExpr;
        this.group = group;
    }

    public Group getGroup()
    {
        return group;
    }

    public String getWhereCondExpr(final QueryDefinitionSelect select, final QueryDefnStatementGenerator statement,
                                   final QueryDefnCondition cond, final Object value) throws QueryDefinitionException
    {
        String retString = "";
        statement.addBindParam(cond.getField(), value);
        String bindExpression = cond.getBindExpr();

        if(bindExpression != null && bindExpression.length() > 0)
            retString = cond.getField().getWhereClauseExpr() + " " + sqlExpr + " " + bindExpression;
        else
            retString = cond.getField().getWhereClauseExpr() + " " + sqlExpr + " ?";
        return retString;
    }

}