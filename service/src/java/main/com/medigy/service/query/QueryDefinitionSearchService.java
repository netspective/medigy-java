/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.query;

import com.medigy.persist.util.query.QueryDefinition;
import com.medigy.service.dto.query.QueryDefinitionSearchParameters;
import com.medigy.service.dto.query.QueryDefinitionSearchReturnValues;
import com.medigy.service.dto.query.QueryDefinitionSearchFormPopulateValues;

public interface QueryDefinitionSearchService
{
    public QueryDefinition getQueryDefinition(final Class queryDefClass);
    public QueryDefinitionSearchReturnValues search(final QueryDefinitionSearchParameters params);

    /**
     * Gets all the available choices for the query definition search form such as all display fields, condition fields,
     * and sort by fields.
     *
     * @return all available search criteria and respective choices
     */
    public QueryDefinitionSearchFormPopulateValues getAvailableSearchParameters(final Class queryDefinitionClass);

}
