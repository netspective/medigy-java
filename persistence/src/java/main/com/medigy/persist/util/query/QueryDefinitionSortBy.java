/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.util.query;

public interface QueryDefinitionSortBy
{
    public boolean isDescending();
    public QueryDefinitionField getField();
    public String getExplicitOrderByClause();
}
