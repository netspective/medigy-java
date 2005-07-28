/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.dto;

import com.medigy.persist.util.query.QueryDefnCondition;
import com.medigy.service.SearchServiceParameters;

public interface CriteriaSearchParameters extends SearchServiceParameters
{
    public QueryDefnCondition getSearchCriteria();

    public String getSearchCriteriaValue();
}
