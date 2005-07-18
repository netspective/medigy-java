/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.util.query.comparison;

import com.medigy.persist.util.query.QueryDefinitionSelect;
import com.medigy.persist.util.query.QueryDefnCondition;
import com.medigy.persist.util.query.QueryDefnStatementGenerator;
import com.medigy.persist.util.query.SqlComparison;
import com.medigy.persist.util.query.exception.QueryDefinitionException;
import com.medigy.persist.util.value.ValueContext;

public class StartsWithComparisonIgnoreCase extends BinaryOpComparison
{
    public static final String COMPARISON_NAME = "Starts with (ignore case)";
    public StartsWithComparisonIgnoreCase()
    {
        super(SqlComparison.Group.STRING);
    }

    public String getWhereCondExpr(QueryDefinitionSelect select, QueryDefnStatementGenerator statement,
                                   QueryDefnCondition cond, final ValueContext valueContext) throws QueryDefinitionException
    {
        statement.addBindParam(cond.getValueProvider().getValue() + "%");
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
