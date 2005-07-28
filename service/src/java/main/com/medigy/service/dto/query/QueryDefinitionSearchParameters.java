/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.dto.query;

import com.medigy.service.SearchServiceParameters;

import java.util.List;

public interface QueryDefinitionSearchParameters  extends SearchServiceParameters
{
    public Class getQueryDefinitionClass();
    public List<String> getDisplayFields();

}
