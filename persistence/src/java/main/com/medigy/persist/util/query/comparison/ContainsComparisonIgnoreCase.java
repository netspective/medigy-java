/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.util.query.comparison;

import com.medigy.persist.util.query.SqlComparison;
import com.medigy.persist.util.query.QueryDefinitionSelect;
import com.medigy.persist.util.query.QueryDefnStatementGenerator;
import com.medigy.persist.util.query.QueryDefnCondition;
import com.medigy.persist.util.query.exception.QueryDefinitionException;
import com.medigy.persist.util.value.ValueContext;

public class ContainsComparisonIgnoreCase extends BinaryOpComparison
{
    public static final String COMPARISON_NAME = "Contains (ignore case)";

    public ContainsComparisonIgnoreCase()
    {
        super(SqlComparison.Group.STRING);
    }

    public String getName()
    {
        return COMPARISON_NAME;
    }

    public String getWhereCondExpr(final QueryDefinitionSelect select, final QueryDefnStatementGenerator statement,
                                   final QueryDefnCondition cond, final ValueContext valueContext) throws QueryDefinitionException
    {
        statement.addBindParam("%" + cond.getValueProvider().getValue() + "%");
        String retString = "";
        String bindExpression = cond.getBindExpr();
        if(bindExpression != null && bindExpression.length() > 0)
            retString = "UPPER(" + cond.getField().getWhereClauseExpr() + ") like UPPER(" + bindExpression + ")";
        else
            retString = "UPPER(" + cond.getField().getWhereClauseExpr() + ") like UPPER(?)";
        return retString;
    }
}
