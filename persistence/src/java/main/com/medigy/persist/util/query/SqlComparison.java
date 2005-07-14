/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.util.query;

import com.medigy.persist.util.query.exception.QueryDefinitionException;
import com.medigy.persist.util.value.ValueContext;

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

    public String getWhereCondExpr(final QueryDefinitionSelect select, final QueryDefnStatementGenerator statement,
                                   final QueryDefnCondition cond, final ValueContext valueContext) throws QueryDefinitionException;
}