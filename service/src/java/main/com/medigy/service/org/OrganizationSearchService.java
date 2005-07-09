/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.org;

public interface OrganizationSearchService
{
    /**
     * Column for SELECT
     */
    public void addColumn(final String column);

    public void addOrderBy(final String column, boolean ascending);

    

}
