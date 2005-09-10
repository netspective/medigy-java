/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.impl.person;

import com.medigy.persist.model.common.Entity;
import com.medigy.persist.reference.custom.person.PersonRoleType;
import com.medigy.persist.util.query.QueryDefinitionField;
import com.medigy.persist.util.query.QueryDefnCondition;
import com.medigy.persist.util.query.QueryDefnStatementGenerator;
import com.medigy.service.AbstractQueryDefinitionSearchServiceImpl;
import com.medigy.service.SearchServiceParameters;
import com.medigy.service.ServiceVersion;
import com.medigy.service.dto.ServiceParameters;
import com.medigy.service.dto.ServiceReturnValues;
import com.medigy.service.dto.person.PersonSearchParameters;
import com.medigy.service.dto.person.PersonSearchReturnValues;
import com.medigy.service.person.PatientSearchQueryDefinition;
import com.medigy.service.person.PersonSearchService;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PersonSearchServiceImpl extends AbstractQueryDefinitionSearchServiceImpl implements PersonSearchService
{
    private final Log log = LogFactory.getLog(PersonSearchServiceImpl.class);

    /**
     * Even though the person search service is using the Query definitions search service, it is hiding this  fact by
     *  NOT IMPLEMENTING the SearchService interface and wrapping its calls to the SearchService.
     */
    public PersonSearchServiceImpl(final SessionFactory sessionFactory)
    {
        super(sessionFactory, PatientSearchQueryDefinition.class, PatientSearchQueryDefinition.CRITERIA_SEARCH_SELECT);
    }

    // ================================ Interface methods ========================================

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
        return new PersonSearchReturnValues() {
             public String getErrorMessage()
             {
                 return errorMessage;
             }

            public ServiceParameters getParameters()
            {
                return params;
            }

            public List<Person> getSearchResults()
            {
                return null;
            }
        };
    }

    public List<QueryDefnCondition> getSearchCriterias()
    {
        return getCriteriaList();
    }

    public PersonSearchReturnValues searchPerson(final PersonSearchParameters params)
    {
        return (PersonSearchReturnValues) search(params);
    }


    // ================================ hidden methods ========================================

    protected ServiceReturnValues extractResult(final SearchServiceParameters params, final QueryDefnStatementGenerator generator, final List queryResultList)
    {
        final List<PersonSearchReturnValues.Person> people = new ArrayList<PersonSearchReturnValues.Person>();
        final List<QueryDefinitionField> displayFields = generator.getQuerySelect().getDisplayFields();
        try
        {
            for (Object obj: queryResultList)
            {
                PersonSearchReturnValues.Person person = new PersonSearchReturnValues.Person();
                if (obj instanceof Entity)
                {
                    for (QueryDefinitionField field : displayFields)
                    {
                        final Object fieldValue = PropertyUtils.getNestedProperty(obj, field.getEntityPropertyName());
                        if (field.getName().equals(PatientSearchQueryDefinition.Field.FIRST_NAME.getName()))
                            person.setFirstName(fieldValue.toString());
                        else if (field.getName().equals(PatientSearchQueryDefinition.Field.LAST_NAME.getName()))
                            person.setLastName(fieldValue.toString());
                        else if (field.getName().equals(PatientSearchQueryDefinition.Field.GENDER.getName()))
                            person.setGender(fieldValue.toString());
                        else if (field.getName().equals(PatientSearchQueryDefinition.Field.DOB.getName()))
                            person.setBirthDate((Date) fieldValue);
                        else if (field.getName().equals(PatientSearchQueryDefinition.Field.PATIENT_ID.getName()))
                            person.setPersonId((Long)fieldValue);
                        else if (field.getName().equals(PatientSearchQueryDefinition.Field.SSN.getName()))
                            person.setSsn(fieldValue.toString());
                        else if (field.getName().equals(PatientSearchQueryDefinition.Field.PRIMARY_ROLE.getName()))
                            person.setPrimaryRole(((PersonRoleType) fieldValue).getLabel());
                    }

                }
                else if (obj instanceof Object[])
                {
                    for (int fieldIndex=0; fieldIndex < displayFields.size(); fieldIndex++)
                    {
                        QueryDefinitionField field = displayFields.get(fieldIndex);
                        final Object fieldValue = ((Object[]) obj)[fieldIndex];
                        if (fieldValue == null)
                            continue;
                        if (field.getName().equals(PatientSearchQueryDefinition.Field.FIRST_NAME.getName()))
                            person.setFirstName(fieldValue.toString());
                        else if (field.getName().equals(PatientSearchQueryDefinition.Field.LAST_NAME.getName()))
                            person.setLastName(fieldValue.toString());
                        else if (field.getName().equals(PatientSearchQueryDefinition.Field.GENDER.getName()))
                            person.setGender(fieldValue.toString());
                        else if (field.getName().equals(PatientSearchQueryDefinition.Field.DOB.getName()))
                            person.setBirthDate((Date) fieldValue);
                        else if (field.getName().equals(PatientSearchQueryDefinition.Field.PATIENT_ID.getName()))
                            person.setPersonId((Long)fieldValue);
                        else if (field.getName().equals(PatientSearchQueryDefinition.Field.SSN.getName()))
                            person.setSsn(fieldValue.toString());
                        else if (field.getName().equals(PatientSearchQueryDefinition.Field.PRIMARY_ROLE.getName()))
                            person.setPrimaryRole(((PersonRoleType) fieldValue).getLabel());
                    }
                }
                people.add(person);
            }
        }
        catch (Exception e)
        {
            log.error(e);
            e.printStackTrace();
            return createErrorResponse(params, e.getMessage());
        }

        return new PersonSearchReturnValues() {
            public List<Person> getSearchResults()
            {
                return people;
            }

            public String getErrorMessage()
            {
                return null;
            }

            public ServiceParameters getParameters()
            {
                return params;
            }
        };
    }

}
