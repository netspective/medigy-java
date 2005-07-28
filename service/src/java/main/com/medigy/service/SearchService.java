/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service;

import com.medigy.persist.util.query.QueryDefnCondition;

import java.util.List;


public interface SearchService extends Service
{
    public SearchReturnValues search(final SearchServiceParameters params);

    public List<QueryDefnCondition> getCriteriaList();
}
