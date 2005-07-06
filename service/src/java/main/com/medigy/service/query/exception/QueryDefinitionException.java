/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.query.exception;

import com.medigy.service.query.QueryDefinition;
import org.hibernate.exception.NestableException;

public class QueryDefinitionException extends NestableException
{
    private QueryDefinition queryDefn;

    public QueryDefinitionException(QueryDefinition queryDefn, String message)
    {
        super(message);
        this.queryDefn = queryDefn;
    }

    public QueryDefinitionException(QueryDefinition queryDefn, Throwable throwable)
    {
        super(throwable);
        this.queryDefn = queryDefn;
    }

    public QueryDefinition getQueryDefn()
    {
        return queryDefn;
    }
}
