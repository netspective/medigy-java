/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.impl.person;

import com.medigy.service.person.PersonSearchService;
import com.medigy.service.dto.person.PersonSearchParameters;
import com.medigy.service.dto.ServiceReturnValues;
import com.medigy.service.dto.ServiceParameters;
import com.medigy.service.query.QueryDefnJoin;
import com.medigy.service.query.QueryDefnField;
import com.medigy.service.query.QueryDefinitionSelect;
import com.medigy.service.query.QueryDefinition;
import com.medigy.service.query.SqlComparisonFactory;
import com.medigy.service.query.QueryDefnStatementGenerator;
import com.medigy.service.query.QueryDefinitionConditionValue;
import com.medigy.service.query.exception.QueryDefinitionException;
import com.medigy.service.AbstractService;
import com.medigy.service.ServiceVersion;
import com.medigy.persist.model.person.Person;
import com.medigy.persist.model.party.PartyRelationship;
import com.medigy.persist.model.org.Organization;
import org.hibernate.SessionFactory;
import org.hibernate.Query;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.util.List;
import java.util.Date;

public class PersonSearchServiceImpl extends AbstractService implements PersonSearchService
{
    private final Log log = LogFactory.getLog(PersonSearchServiceImpl.class);

    public static final String LAST_NAME_FIELD = "lastName";
    public static final String FIRST_NAME_FIELD = "firstName";

    private QueryDefinition queryDefn = new QueryDefinition();


    public PersonSearchServiceImpl(final SessionFactory sessionFactory)
    {
        super(sessionFactory);

        final QueryDefnJoin personJoin = new QueryDefnJoin();
        personJoin.setTableName(Person.class.getName());
        personJoin.setName("person");
        personJoin.setAutoInclude(true);

        final QueryDefnJoin orgJoin = new QueryDefnJoin();
        orgJoin.setTableName(Organization.class.getName());
        orgJoin.setName("org");
        //orgJoin.setImplyJoinsStr("org.");

        final QueryDefnJoin personRoleJoin = new QueryDefnJoin();
        personRoleJoin.setTableName("PartyRole");
        personRoleJoin.setName("personRole");


        final QueryDefnJoin orgRoleJoin = new QueryDefnJoin();
        orgRoleJoin.setTableName("PartyRole");
        orgRoleJoin.setName("personRole");

        final QueryDefnJoin partyRelationshipJoin = new QueryDefnJoin();
        partyRelationshipJoin.setTableName(PartyRelationship.class.getName());
        partyRelationshipJoin.setName("rel");
        partyRelationshipJoin.setCondition("rel.partyFrom.id = org.partyId AND rel.partyTo.id = person.partyId");
        partyRelationshipJoin.addImpliedJoin(orgJoin);

        // add all the supported fields
        final QueryDefnField firstNameField = queryDefn.addField("firstName", "firstName", personJoin);
        final QueryDefnField lastNameField = queryDefn.addField("lastName", "lastName", personJoin);
        final QueryDefnField orgIdField = queryDefn.addField("organizationId", "partyId", orgJoin);
        final QueryDefnField personIdField = queryDefn.addField("personId", "partyId", personJoin);
        final QueryDefnField partyRelTypeField = queryDefn.addField("organizationRelationshipTypeCode", "type.code", partyRelationshipJoin);


        final QueryDefinitionSelect select = new QueryDefinitionSelect("searchPerson", queryDefn);
        select.addDisplayField(lastNameField);
        select.addDisplayField(firstNameField);
        select.addCondition(lastNameField, SqlComparisonFactory.getInstance().getComparison("starts-with-ignore-case"),
            new QueryDefinitionConditionValue(PersonSearchParameters.class, "lastName"));
        select.addCondition(firstNameField, SqlComparisonFactory.getInstance().getComparison("starts-with-ignore-case"),
            new QueryDefinitionConditionValue(PersonSearchParameters.class, "firstName"));
        select.addCondition(partyRelTypeField, SqlComparisonFactory.getInstance().getComparison("equals"),
            new QueryDefinitionConditionValue(PersonSearchParameters.class, "organizationRelationshipTypeCode"));

        queryDefn.addJoin(personJoin);
        queryDefn.addJoin(orgJoin);
        queryDefn.addSelect(select);
    }

    public Person search(final PersonSearchParameters params)
    {
        QueryDefnStatementGenerator generator = new QueryDefnStatementGenerator(queryDefn.getSelects().get("searchPerson"));
        try
        {
            String sql = generator.generateQuery(params);
            final Query query = getSession().createQuery(sql);
            final List bindParams = generator.getBindParams();
            System.out.println(sql + "\n" + bindParams.size());

            int i=0;
            for (Object param : bindParams)
            {
                if (param instanceof String)
                    query.setString(i, (String) param);
                else if (param instanceof Long)
                    query.setLong(i, (Long) param);
                else if (param instanceof Integer)
                    query.setInteger(i, (Integer) param);
                else if (param instanceof Date)
                    query.setDate(i, (Date) param);
                else
                {
                    throw new RuntimeException("Unsupported bind parameter type: " + param.getClass());
                }
                i++;
            }

            final List list = query.list();
            log.info("Person search query returned: " + list.size() + " row(s).");
        }
        catch (QueryDefinitionException e)
        {
            e.printStackTrace();
        }

        return null;
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
            public ServiceParameters getParameters()
            {
                return (ServiceParameters) params;
            }

            public Serializable getRegisteredProviderId()
            {
                return null;
            }

            public String getErrorMessage()
            {
                return errorMessage;
            }
        };
    }
}
