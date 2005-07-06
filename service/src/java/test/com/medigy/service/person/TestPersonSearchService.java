/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.person;

import com.medigy.service.AbstractSpringTestCase;
import com.medigy.service.ServiceVersion;
import com.medigy.service.dto.person.PersonSearchParameters;
import com.medigy.persist.reference.custom.party.PartyRelationshipType;

public class TestPersonSearchService  extends AbstractSpringTestCase
{
    private PersonSearchService personSearchService;

    public void setPersonSearchService(final PersonSearchService personSearchService)
    {
        this.personSearchService = personSearchService;
    }

    public void testSearch()
    {
        personSearchService.search(new PersonSearchParameters() {
            public String getLastName()
            {
                return null;
            }

            public String getFirstName()
            {
                return null;
            }

            // org related search criteria
            public Long getOrganizationId()
            {
                return null;
            }

            public String getOrganizationRelationshipTypeCode()
            {
                //return PartyRelationshipType.Cache.ORGANIZATION_ROLLUP.getEntity().getCode();
                return null;
            }

            public ServiceVersion getServiceVersion()
            {
                return null;
            }
        });

    }
}
