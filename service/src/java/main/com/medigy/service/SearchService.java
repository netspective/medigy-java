/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service;

import com.medigy.persist.util.query.QueryDefnCondition;
import com.medigy.service.dto.ServiceReturnValues;

import java.util.List;


public interface SearchService extends Service
{
    public ServiceReturnValues search(final SearchServiceParameters params);

    public List<QueryDefnCondition> getCriteriaList();
}
