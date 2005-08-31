/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service;

import com.medigy.service.dto.ServiceReturnValues;

import java.util.List;
import java.util.Map;

public interface SearchReturnValues extends ServiceReturnValues
{
    public List<String> getColumnNames();
    public List<Map<String, Object>> getSearchResults();
}
