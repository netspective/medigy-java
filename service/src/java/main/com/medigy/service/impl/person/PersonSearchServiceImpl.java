/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.impl.person;

import com.medigy.service.AbstractQueryDefinitionSearchServiceImpl;
import com.medigy.service.ServiceVersion;
import com.medigy.service.dto.ServiceParameters;
import com.medigy.service.dto.ServiceReturnValues;
import com.medigy.service.person.PatientSearchQueryDefinition;
import com.medigy.service.person.PersonSearchService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;

public class PersonSearchServiceImpl extends AbstractQueryDefinitionSearchServiceImpl implements PersonSearchService
{
    private final Log log = LogFactory.getLog(PersonSearchServiceImpl.class);

    public PersonSearchServiceImpl(final SessionFactory sessionFactory)
    {
        super(sessionFactory, PatientSearchQueryDefinition.class, PatientSearchQueryDefinition.CRITERIA_SEARCH_SELECT);
    }

    public ServiceVersion[] getSupportedServiceVersions()
    {
        return new ServiceVersion[0];
    }

    public String[] isValid(ServiceParameters parameters)
    {
        return new String[0];
    }

    public ServiceReturnValues createErrorResponse(final ServiceParameters params, final String errorMessage)
    {
        return new ServiceReturnValues() {
             public String getErrorMessage()
             {
                 return errorMessage;
             }

            public ServiceParameters getParameters()
            {
                return params;
            }
        };
    }
}
