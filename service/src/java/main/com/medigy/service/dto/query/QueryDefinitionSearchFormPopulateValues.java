/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.dto.query;

import com.medigy.service.dto.ServiceReturnValues;
import com.medigy.persist.util.query.QueryDefinitionField;

import java.util.Map;
import java.util.List;

public interface QueryDefinitionSearchFormPopulateValues extends ServiceReturnValues
{
    public Map<String, QueryDefinitionField> getConditionFields();
    public Map<String, QueryDefinitionField> getDisplayFields();
    public Map<String, QueryDefinitionField> getSortByFields();

    public List<String>getConnectors();
}
