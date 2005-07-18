/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.dto;

import com.medigy.service.SearchParameters;
import com.medigy.persist.util.query.QueryDefnCondition;

public interface CriteriaSearchParameters extends SearchParameters
{
    public QueryDefnCondition getSearchCriteria();

    public String getSearchCriteriaValue();
}
