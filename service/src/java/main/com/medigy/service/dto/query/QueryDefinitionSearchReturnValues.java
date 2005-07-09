/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.dto.query;

import com.medigy.service.dto.ServiceReturnValues;
import com.medigy.service.dto.ServiceParameters;

import java.util.Map;
import java.util.List;

public interface QueryDefinitionSearchReturnValues extends ServiceReturnValues
{
    public List<Map<String, Object>> getSearchResults();
}
