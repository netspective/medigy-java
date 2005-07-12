/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.util.query;

public interface QueryDefinitionSortFieldReference
{
    public enum OrderBy
    {
        ASCENDING("asc"), DESCENDING("desc");

        private String name;

        OrderBy(final String name)
        {
            this.name = name;
        }
    }

    public QueryDefinitionField getField();
    public OrderBy getOrderBy();
}
