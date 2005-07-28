/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service;

import com.medigy.service.dto.ServiceParameters;
import com.medigy.service.dto.query.SearchCondition;

import java.util.List;

public interface SearchServiceParameters extends ServiceParameters
{
    public List<SearchCondition> getConditions();
    
    public List<String> getOrderBys();

    public List<String> getDisplayFields();

    public int getStartFromRow();
}
