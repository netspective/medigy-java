/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.org;

import com.medigy.persist.util.query.impl.BasicQueryDefinition;

public class OrganizationSearchQueryDefinition extends BasicQueryDefinition
{
    public static final String QUERY_DEFINITION_NAME = "organizationSearch";

    public OrganizationSearchQueryDefinition()
    {
        super(QUERY_DEFINITION_NAME);
    }
}
