/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.query;

import com.medigy.service.query.exception.QueryDefinitionException;
import com.medigy.service.dto.ServiceParameters;

public interface SqlComparison
{
    public String getName();

    public String getCaption();

    public String getWhereCondExpr(QueryDefinitionSelect select, QueryDefnStatementGenerator statement,
                                   QueryDefnCondition cond, ServiceParameters params) throws QueryDefinitionException;
}