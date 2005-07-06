/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.query.exception;

import com.medigy.service.query.QueryDefinition;

public class QueryDefnJoinNotFoundException extends  QueryDefinitionException
{
    private String joinName;

    public QueryDefnJoinNotFoundException(QueryDefinition queryDefn, String joinId, String message)
    {
        super(queryDefn, message);
        this.joinName = joinId;
    }

    public String getJoinName()
    {
        return joinName;
    }
}
