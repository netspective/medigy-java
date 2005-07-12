/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.util.query.exception;

import com.medigy.persist.util.query.QueryDefinition;
import org.hibernate.exception.NestableException;

public class QueryDefinitionException extends NestableException
{
    private QueryDefinition queryDefn;

    public QueryDefinitionException(String msg)
    {
        super(msg);
    }

    public QueryDefinitionException(Throwable cause)
    {
        super(cause);
    }

    public QueryDefinitionException(String msg, Throwable cause)
    {
        super(msg, cause);
    }

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
