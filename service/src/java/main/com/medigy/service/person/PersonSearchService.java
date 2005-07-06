/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.person;

import com.medigy.persist.model.person.Person;
import com.medigy.service.dto.person.PersonSearchParameters;

public interface PersonSearchService
{
    public Person search(final PersonSearchParameters params);

}
