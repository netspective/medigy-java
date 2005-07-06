/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.dto.person;

import com.medigy.service.dto.ServiceParameters;

public interface PersonSearchParameters  extends ServiceParameters
{
    public String getLastName();
    public String getFirstName();

    // org related search criteria
    public Long getOrganizationId();
    public String getOrganizationRelationshipTypeCode();

}
