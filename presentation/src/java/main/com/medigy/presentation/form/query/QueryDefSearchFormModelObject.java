/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.presentation.form.query;

import com.medigy.service.dto.query.QueryDefinitionSearchCondition;
import com.medigy.service.dto.query.QueryDefinitionSearchFormPopulateValues;
import com.medigy.presentation.model.common.SearchFormModelObject;

import java.util.List;
import java.util.ArrayList;

/**
 *
 */
public class QueryDefSearchFormModelObject implements SearchFormModelObject
{
    // This is actually the values used to populate the form with choices
    private QueryDefinitionSearchFormPopulateValues defaultValues;

    // These fields are actually the values enetered by the user
    private List<QueryDefinitionSearchCondition> conditionFieldList = new ArrayList<QueryDefinitionSearchCondition>();
    private List<String> displayFields = new ArrayList<String>();
    private List<String> sortByFields = new ArrayList<String>();

    public QueryDefinitionSearchFormPopulateValues getDefaultValues()
    {
        return defaultValues;
    }

    public void setDefaultValues(final QueryDefinitionSearchFormPopulateValues defaultValues)
    {
        this.defaultValues = defaultValues;
    }

    public List<QueryDefinitionSearchCondition> getConditionFieldList()
    {
        return conditionFieldList;
    }

    public void setConditionFieldList(final List<QueryDefinitionSearchCondition> conditionFieldList)
    {
        this.conditionFieldList = conditionFieldList;
    }

    public void addConditionFieldList(final QueryDefinitionSearchCondition condition)
    {
        this.conditionFieldList.add(condition);
    }

    public List<String> getDisplayFields()
    {
        return displayFields;
    }

    public void setDisplayFields(final List<String> displayFields)
    {
        this.displayFields = displayFields;
    }

    public List<String> getSortByFields()
    {
        return sortByFields;
    }

    public void setSortByFields(final List<String> sortByFields)
    {
        this.sortByFields = sortByFields;
    }
}
