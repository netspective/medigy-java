/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.dto.person;

import com.medigy.service.dto.ServiceParameters;
import com.medigy.service.dto.query.QueryDefinitionSearchCondition;

import java.util.List;

public interface QueryDefinitionSearchParameters  extends ServiceParameters
{
    public Class getQueryDefinitionClass();
    public List<QueryDefinitionSearchCondition> getConditionFieldList();
    public List<String> getDisplayFields();
    public List<String> getSortByFields();
}
