/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.query.comparison;

import com.medigy.service.query.QueryDefinitionSelect;
import com.medigy.service.query.QueryDefnCondition;
import com.medigy.service.query.QueryDefnStatementGenerator;
import com.medigy.service.query.SqlComparison;
import com.medigy.service.query.exception.QueryDefinitionException;
import com.medigy.service.dto.ServiceParameters;

public class StartsWithComparisonIgnoreCase extends BinaryOpComparison
{
    public static final String COMPARISON_NAME = "Starts with (ignore case)";
    public StartsWithComparisonIgnoreCase()
    {
        super(SqlComparison.Group.STRING);
    }

    public String getWhereCondExpr(QueryDefinitionSelect select, QueryDefnStatementGenerator statement,
                                   QueryDefnCondition cond, final Object value) throws QueryDefinitionException
    {
        if (!(value instanceof String))
            throw new QueryDefinitionException(cond.getOwner(), "Cannot assign a non-string value to a string" +
                    "comparison.");
        statement.addBindParam(cond.getField(), value + "%");
        String retString = "";
        String bindExpression = cond.getBindExpr();
        if(bindExpression != null && bindExpression.length() > 0)
            retString = cond.getField().getWhereClauseExpr() + " like UPPER(" + bindExpression + ")";
        else
            retString = cond.getField().getWhereClauseExpr() + " like UPPER(?)";
        return retString;
    }

    public String getName()
    {
        return COMPARISON_NAME;
    }
}
