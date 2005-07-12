/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.util.query;

import com.medigy.persist.util.query.exception.QueryDefinitionException;

public interface SqlComparison
{
    public enum Group
    {
        GENERAL("General"), STRING("String"), DATE("Date");

        private String type;

        Group(final String name)
        {
            this.type = name;
        }

        public String type()
        {
            return type;
        }
    }

    public String getName();

    public Group getGroup();

    public String getWhereCondExpr(QueryDefinitionSelect select, QueryDefnStatementGenerator statement,
                                   QueryDefnCondition cond, Object value) throws QueryDefinitionException;
}