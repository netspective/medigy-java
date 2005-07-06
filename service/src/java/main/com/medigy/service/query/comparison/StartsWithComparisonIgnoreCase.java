/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.query.comparison;

import com.medigy.service.query.QueryDefinitionSelect;
import com.medigy.service.query.QueryDefnCondition;
import com.medigy.service.query.QueryDefnStatementGenerator;
import com.medigy.service.query.exception.QueryDefinitionException;
import com.medigy.service.dto.ServiceParameters;

public class StartsWithComparisonIgnoreCase extends BinaryOpComparison
{

    public StartsWithComparisonIgnoreCase(final String name)
    {
        super(name);
    }

    public StartsWithComparisonIgnoreCase(final String name, final String caption)
    {
        super(name, caption);
    }

    public StartsWithComparisonIgnoreCase(final String name, final String caption,  final String sqlExpr)
    {
        super(name, caption, sqlExpr);
    }

    public String getWhereCondExpr(QueryDefinitionSelect select, QueryDefnStatementGenerator statement,
                                   QueryDefnCondition cond, final ServiceParameters params) throws QueryDefinitionException
    {
        statement.addBindParam(cond.getField(), cond.getValue(params) + "%");
        String retString = "";
        String bindExpression = cond.getBindExpr();
        if(bindExpression != null && bindExpression.length() > 0)
            retString = cond.getField().getWhereClauseExpr() + " like UPPER(" + bindExpression + ")";
        else
            retString = cond.getField().getWhereClauseExpr() + " like UPPER(?)";
        return retString;
    }
}
