/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.person;

import com.medigy.persist.util.query.QueryDefinition;
import com.medigy.persist.util.query.comparison.StartsWithComparisonIgnoreCase;
import com.medigy.service.AbstractSpringTestCase;
import com.medigy.service.ServiceVersion;
import com.medigy.service.dto.query.QueryDefinitionSearchParameters;
import com.medigy.service.dto.query.SearchCondition;
import com.medigy.service.query.QueryDefinitionSearchService;

import java.util.ArrayList;
import java.util.List;

public class TestPersonSearchService  extends AbstractSpringTestCase
{
    private QueryDefinitionSearchService queryDefinitionSearchService;

    public void setPersonSearchService(final QueryDefinitionSearchService queryDefinitionSearchService)
    {
        this.queryDefinitionSearchService = queryDefinitionSearchService;
    }

    public void testSearch()
    {
        queryDefinitionSearchService.search(new QueryDefinitionSearchParameters() {

            public List<SearchCondition> getConditions()
            {
                return null;
            }

            public List<String> getOrderBys()
            {
                return null;
            }

            public QueryDefinition getQueryDefinition()
            {
                return null;
            }

            public int getStartFromRow()
            {
                return 0;
            }

            public Class getQueryDefinitionClass()
            {
                return PatientSearchQueryDefinition.class;
            }

            public List<SearchCondition> getConditionFieldList()
            {
                SearchCondition condition = new SearchCondition();
                condition.setField("lastName");
                condition.setFieldValue("s");
                condition.setFieldComparison(StartsWithComparisonIgnoreCase.COMPARISON_NAME);

                final ArrayList<SearchCondition> conditions = new ArrayList<SearchCondition>();
                //conditions.add(condition);
                return conditions;
            }

            public List<String> getDisplayFields()
            {
                final List<String> strings = new ArrayList<String>();
                strings.add("lastName");
                strings.add("firstName");
                return strings;
            }

            public List<String> getSortByFields()
            {
                return null;
            }

            public ServiceVersion getServiceVersion()
            {
                return null;
            }
        });

    }
}
