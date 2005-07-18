/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.presentation.form.common;

import com.medigy.presentation.model.common.SearchFormModelObject;
import com.medigy.persist.util.query.QueryDefnCondition;

public class CriteriaSearchFormModelObject implements SearchFormModelObject
{
    private QueryDefnCondition searchCriterias;
    private String searchCriteriaValue;

    public QueryDefnCondition getSearchCriterias()
    {
        return searchCriterias;
    }

    public void setSearchCriterias(final QueryDefnCondition searchCriterias)
    {
        this.searchCriterias = searchCriterias;
    }

    public String getSearchCriteriaValue()
    {
        return searchCriteriaValue;
    }

    public void setSearchCriteriaValue(final String searchCriteriaValue)
    {
        this.searchCriteriaValue = searchCriteriaValue;
    }

}
