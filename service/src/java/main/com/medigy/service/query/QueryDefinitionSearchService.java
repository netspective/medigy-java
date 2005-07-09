/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.query;

import com.medigy.persist.model.person.Person;
import com.medigy.service.dto.person.QueryDefinitionSearchParameters;
import com.medigy.service.dto.query.QueryDefinitionSearchReturnValues;

import java.util.Map;

public interface QueryDefinitionSearchService
{
    public QueryDefinitionSearchReturnValues search(final QueryDefinitionSearchParameters params);

}
