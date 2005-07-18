/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.dto.query;

import com.medigy.service.dto.ServiceParameters;
import com.medigy.service.dto.query.QueryDefinitionSearchCondition;
import com.medigy.service.SearchParameters;
import com.medigy.persist.util.query.QueryDefinition;
import com.medigy.persist.util.query.QueryDefinitionField;

import java.util.List;

public interface QueryDefinitionSearchParameters  extends SearchParameters
{
    public Class getQueryDefinitionClass();
    public List<QueryDefinitionSearchCondition> getConditionFieldList();
    public List<String> getDisplayFields();
    public List<String> getSortByFields();

}
