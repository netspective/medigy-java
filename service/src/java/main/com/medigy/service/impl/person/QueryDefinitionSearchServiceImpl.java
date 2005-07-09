/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.impl.person;

import com.medigy.service.AbstractService;
import com.medigy.service.ServiceVersion;
import com.medigy.service.dto.ServiceParameters;
import com.medigy.service.dto.query.QueryDefinitionSearchReturnValues;
import com.medigy.service.dto.query.QueryDefinitionSearchCondition;
import com.medigy.service.dto.person.QueryDefinitionSearchParameters;
import com.medigy.service.query.QueryDefinitionSearchService;
import com.medigy.service.query.QueryDefinition;
import com.medigy.service.query.QueryDefinitionFactory;
import com.medigy.service.query.QueryDefinitionSelect;
import com.medigy.service.query.QueryDefnStatementGenerator;
import com.medigy.service.query.SqlComparisonFactory;
import com.medigy.service.query.exception.QueryDefinitionException;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class QueryDefinitionSearchServiceImpl extends AbstractService implements QueryDefinitionSearchService
{
    private final Log log = LogFactory.getLog(QueryDefinitionSearchServiceImpl.class);

    public static final String LAST_NAME_FIELD = "lastName";
    public static final String FIRST_NAME_FIELD = "firstName";


    public QueryDefinitionSearchServiceImpl(final SessionFactory sessionFactory)
    {
        super(sessionFactory);
        /*
        final QueryDefinitionJoin personJoin = new QueryDefinitionJoinImpl();
        personJoin.setTableName(Person.class.getName());
        personJoin.setName("person");
        personJoin.setAutoInclude(true);

        final QueryDefinitionJoin orgJoin = new QueryDefinitionJoinImpl();
        orgJoin.setTableName(Organization.class.getName());
        orgJoin.setName("org");
        //orgJoin.setImplyJoinsStr("org.");

        final QueryDefinitionJoin personRoleJoin = new QueryDefinitionJoinImpl();
        personRoleJoin.setTableName("PartyRole");
        personRoleJoin.setName("personRole");


        final QueryDefinitionJoin orgRoleJoin = new QueryDefinitionJoinImpl();
        orgRoleJoin.setTableName("PartyRole");
        orgRoleJoin.setName("personRole");

        final QueryDefinitionJoin partyRelationshipJoin = new QueryDefinitionJoinImpl();
        partyRelationshipJoin.setTableName(PartyRelationship.class.getName());
        partyRelationshipJoin.setName("rel");
        partyRelationshipJoin.setCondition("rel.partyFrom.id = org.partyId AND rel.partyTo.id = person.partyId");
        partyRelationshipJoin.addImpliedJoin(orgJoin);

        // add all the supported fields
        final QueryDefinitionField firstNameField = queryDefn.addField("firstName", "firstName", personJoin);
        final QueryDefinitionField lastNameField = queryDefn.addField("lastName", "lastName", personJoin);
        final QueryDefinitionField orgIdField = queryDefn.addField("organizationId", "partyId", orgJoin);
        final QueryDefinitionField personIdField = queryDefn.addField("personId", "partyId", personJoin);
        final QueryDefinitionField partyRelTypeField = queryDefn.addField("organizationRelationshipTypeCode", "type.code", partyRelationshipJoin);


        final QueryDefinitionSelect select = new QueryDefinitionSelectImpl("searchPerson", queryDefn);
        select.addDisplayField(lastNameField);
        select.addDisplayField(firstNameField);
        select.addCondition(lastNameField, SqlComparisonFactory.getInstance().getComparison("starts-with-ignore-case"),
            new QueryDefinitionConditionValue(QueryDefinitionSearchParameters.class, "lastName"));
        select.addCondition(firstNameField, SqlComparisonFactory.getInstance().getComparison("starts-with-ignore-case"),
            new QueryDefinitionConditionValue(QueryDefinitionSearchParameters.class, "firstName"));
        select.addCondition(partyRelTypeField, SqlComparisonFactory.getInstance().getComparison("equals"),
            new QueryDefinitionConditionValue(QueryDefinitionSearchParameters.class, "organizationRelationshipTypeCode"));

        queryDefn.addJoin(personJoin);
        queryDefn.addJoin(orgJoin);
        queryDefn.addSelect(select);
        */
    }

    public QueryDefinitionSearchReturnValues search(final QueryDefinitionSearchParameters params)
    {
        final List<Map<String, Object>> searchResult = new ArrayList<Map<String, Object>>();

        final QueryDefinition queryDefiniton = QueryDefinitionFactory.getInstance().getQueryDefinition(params.getQueryDefinitionClass());

        // create a new select object on the fly
        final QueryDefinitionSelect select = queryDefiniton.createSelect("searchPerson");
        final List<String> displayFields = params.getDisplayFields();

        try
        {
            if (displayFields != null)
            {
                for (String field: displayFields)
                {
                    // add the fields to be displayed in the report
                    select.addDisplayField(queryDefiniton.getField(field));
                }
            }

            final List<QueryDefinitionSearchCondition> conditionFieldList = params.getConditionFieldList();
            final Map<String, Object> conditionFieldValues = new HashMap<String, Object>();
            for (QueryDefinitionSearchCondition condition : conditionFieldList)
            {
                if (condition.getField() == null)
                    continue;
                // add all the where clause fields
                select.addCondition(queryDefiniton.getField(condition.getField()),
                    SqlComparisonFactory.getInstance().getComparison(condition.getFieldComparison()));
                // TODO: Need to convert the String values into column type specific values!
                conditionFieldValues.put(condition.getField(), condition.getFieldValue());
            }

            // construct the HQL generator
            QueryDefnStatementGenerator generator = new QueryDefnStatementGenerator(select);
            // pass the condition field values and create the HQL
            String sql = generator.generateQuery(conditionFieldValues);
            final Query query = getSession().createQuery(sql);
            final List bindParams = generator.getBindParams();
            log.info(sql + "\n" + bindParams.size());

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
                    throw new RuntimeException("Unsupported bind parameter type: " + param.getClass());
                i++;
            }

            final List list = query.list();
            log.info("Query Definition search query returned: " + list.size() + " row(s).");
            for (int k=0; k < list.size(); k++)
            {
                final Map<String, Object> map = new HashMap<String, Object>();
                final Object[] columns = (Object[]) list.get(i);
                for (int j=0; j < columns.length; j++)
                {
                    String displayFieldName = displayFields.get(j);
                    Object fieldValue = columns[j];
                    map.put(queryDefiniton.getField(displayFieldName).getCaption(), fieldValue);
                }
                searchResult.add(map);
            }
        }
        catch (QueryDefinitionException e)
        {
            log.error(ExceptionUtils.getStackTrace(e));
            return createErrorResponse(params, e.getMessage());
        }

        return new QueryDefinitionSearchReturnValues()
        {
            public List<Map<String, Object>> getSearchResults()
            {
                return searchResult;
            }

            public ServiceParameters getParameters()
            {
                return params;
            }

            public String getErrorMessage()
            {
                return null;
            }
        };
    }

    public ServiceVersion[] getSupportedServiceVersions()
    {
        return new ServiceVersion[0];
    }

    public String[] isValid(ServiceParameters parameters)
    {
        return new String[0];
    }

    public QueryDefinitionSearchReturnValues createErrorResponse(final ServiceParameters params, final String errorMessage)
    {
        return new QueryDefinitionSearchReturnValues() {
            public ServiceParameters getParameters()
            {
                return  params;
            }

            public List<Map<String, Object>> getSearchResults()
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
