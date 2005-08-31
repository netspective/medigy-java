/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.person;

import com.medigy.persist.util.query.QueryDefnCondition;
import com.medigy.service.Service;
import com.medigy.service.dto.person.PersonSearchParameters;
import com.medigy.service.dto.person.PersonSearchReturnValues;

import java.util.List;

public interface PersonSearchService extends Service
{
    /**
     * Gets all the search conditions allowed for the search
     * @return
     */
    public List<QueryDefnCondition> getSearchCriterias();

    /**
     * Search for people
     * @param params
     * @return
     */
    public PersonSearchReturnValues searchPerson(final PersonSearchParameters params);
}
