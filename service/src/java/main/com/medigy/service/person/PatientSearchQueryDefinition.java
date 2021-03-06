/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.person;

import com.medigy.persist.model.org.Organization;
import com.medigy.persist.model.person.Person;
import com.medigy.persist.model.person.PersonIdentifier;
import com.medigy.persist.model.person.PersonRole;
import com.medigy.persist.reference.custom.person.PersonIdentifierType;
import com.medigy.persist.util.query.CompositeQueryDefinitionCondition;
import com.medigy.persist.util.query.QueryDefinition;
import com.medigy.persist.util.query.QueryDefinitionField;
import com.medigy.persist.util.query.QueryDefinitionJoin;
import com.medigy.persist.util.query.QueryDefinitionSelect;
import com.medigy.persist.util.query.QueryDefinitionSortBy;
import com.medigy.persist.util.query.QueryDefnCondition;
import com.medigy.persist.util.query.SqlComparisonFactory;
import com.medigy.persist.util.query.comparison.EqualsComparison;
import com.medigy.persist.util.query.comparison.StartsWithComparisonIgnoreCase;
import com.medigy.persist.util.query.exception.QueryDefinitionException;
import com.medigy.persist.util.query.impl.BasicQueryDefinition;
import com.medigy.persist.util.query.impl.CompositeQueryDefinitionConditionImpl;
import com.medigy.persist.util.query.impl.QueryDefinitionConditionImpl;
import com.medigy.persist.util.query.impl.QueryDefinitionJoinImpl;
import com.medigy.persist.util.query.impl.QueryDefinitionSelectImpl;

public class PatientSearchQueryDefinition extends BasicQueryDefinition implements QueryDefinition
{
    public static final String QUERY_DEFINITION_NAME = "patientSearch";

    // anytime you add a new SELECT, add the name here
    public static final String CRITERIA_SEARCH_SELECT = "criteriaSearch";

    public PatientSearchQueryDefinition()
    {
        super(QUERY_DEFINITION_NAME);
        register();
    }

    /**
     * COMMENTS for AYE (by AYE):
     * Since this is a person search, by default get the PERSON entity. Remember that by default only the
     * entity's immediate properties are retrieved and the association collections (one-to-many) are lazily
     * retrieved. For now whenever you want one or more items in an associated property collection, just go
     * ahead and get the whole collection and then use a @Transient method to get the specific property. To get
     * the associated collection, you need to add a LEFT JOIN FETCH.
     */
    protected void register()
    {
        final QueryDefinitionJoin personJoin = new QueryDefinitionJoinImpl("person", Person.class, this);
        personJoin.setAutoInclude(true);

        final QueryDefinitionJoin ssnJoin =  new QueryDefinitionJoinImpl("pi", PersonIdentifier.class, this);
        ssnJoin.setAssociatedJoin(personJoin);
        ssnJoin.setAssociatedJoinExpression("left outer join " + personJoin.getEntityAlias() + ".personIdentifiers as " +
                ssnJoin.getEntityAlias() + " with pi.type.id = '" + PersonIdentifierType.Cache.SSN.getEntity().getSystemId() + "'");

        final QueryDefinitionJoin driversLicenseJoin =  new QueryDefinitionJoinImpl("pi2", PersonIdentifier.class, this);
        driversLicenseJoin.setAssociatedJoin(personJoin);
        driversLicenseJoin.setAssociatedJoinExpression("left outer join " + personJoin.getEntityAlias() + ".personIdentifiers as " +
                driversLicenseJoin.getEntityAlias() + " with pi2.type.id = '" + PersonIdentifierType.Cache.DRIVERS_LICENSE.getEntity().getSystemId() + "'");

        final QueryDefinitionJoin primaryRoleJoin = new QueryDefinitionJoinImpl("roles", PersonRole.class, this);
        primaryRoleJoin.setAssociatedJoin(personJoin);
        primaryRoleJoin.setAssociatedJoinExpression("left outer join " + personJoin.getEntityAlias() + ".roles as " + primaryRoleJoin.getEntityAlias() +
            " with roles.isPrimaryRole = true");

        final QueryDefinitionJoin orgJoin =  new QueryDefinitionJoinImpl("org", Organization.class, this);

        final QueryDefinitionJoin personRoleJoin = new QueryDefinitionJoinImpl("personRole", PersonRole.class, this);
        personRoleJoin.setCondition("personRole.party.id = person.id");

        this.addJoin(personJoin);
        this.addJoin(orgJoin);
        this.addJoin(ssnJoin);
        this.addJoin(driversLicenseJoin);
        this.addJoin(personRoleJoin);
        this.addJoin(primaryRoleJoin);

        addField("personId", "partyId", "Patient ID", personJoin);
        addField("firstName", "firstName", "First Name", personJoin);
        addField("lastName", "lastName", "Last Name", personJoin);
        addField("birthDate", "birthDate", "Date of Birth", personJoin);
        addField("primaryRole", "type", "Primary Role", primaryRoleJoin);
        //addField("gender", "currentGenderType.label", "Gender", personJoin);
        //addField("ssn", "currentGenderType.label", "Gender", personJoin);

        // use this for display of SSN in result but it shouldn't be part of the SELECT clause!
        addField("ssnProperty", "identifierValue", "SSN", ssnJoin);

        addField("driversLicProperty", "identifierValue", "Driver's License", driversLicenseJoin);
        //driversLicensePropertyField.setWhereClauseExpr("pi2.type.code = " + PersonIdentifierType.Cache.DRIVERS_LICENSE.getCode() +
        //    " AND pi2.identifierValue ");

        try
        {
            addSelect(registerCriteriaSearch());
        }
        catch (QueryDefinitionException e)
        {
            throw new RuntimeException(e);
        }
    }

    public enum Field
    {
        PATIENT_ID("personId"),
        FIRST_NAME("firstName"),
        LAST_NAME("lastName"),
        DOB("birthDate"),
        GENDER("gender"),
        ORG_ID("orgId"),
        SSN("ssnProperty"),
        PRIMARY_ROLE("primaryRole");

        private String name;
        Field(final String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return name;
        }
    }

    protected QueryDefinitionSelect registerCriteriaSearch() throws QueryDefinitionException
    {
        final QueryDefinitionSelect select = new QueryDefinitionSelectImpl(CRITERIA_SEARCH_SELECT, this);
        select.addDisplayField(getField(Field.PATIENT_ID.getName()));
        select.addDisplayField(getField(Field.FIRST_NAME.getName()));
        select.addDisplayField(getField(Field.LAST_NAME.getName()));
        //select.addDisplayField(getField(Field.GENDER.getEntityAlias()));
        select.addDisplayField(getField(Field.DOB.getName()));
        select.addDisplayField(getField("ssnProperty"));
        select.addDisplayField(getField(Field.PRIMARY_ROLE.getName()));

        final CompositeQueryDefinitionCondition firstAndlastNameCondition = new CompositeQueryDefinitionConditionImpl("firstAndlastNameCondition");
        firstAndlastNameCondition.addChildCondition(new QueryDefinitionConditionImpl(
                "lastNameCondition",
                getField(Field.LAST_NAME.getName()),
                SqlComparisonFactory.getInstance().getComparison(StartsWithComparisonIgnoreCase.COMPARISON_NAME), "AND"));
        firstAndlastNameCondition.addChildCondition(new QueryDefinitionConditionImpl(
                "firstNameCondition",
                getField(Field.FIRST_NAME.getName()),
                SqlComparisonFactory.getInstance().getComparison(StartsWithComparisonIgnoreCase.COMPARISON_NAME), "AND"));
        firstAndlastNameCondition.setConnector("and");
        select.addCondition(firstAndlastNameCondition);

        final QueryDefnCondition lastNameCondition = new QueryDefinitionConditionImpl(
                "lastNameCondition",
                getField(Field.LAST_NAME.getName()),
                SqlComparisonFactory.getInstance().getComparison(StartsWithComparisonIgnoreCase.COMPARISON_NAME), "AND");
        select.addCondition(lastNameCondition);

        final QueryDefnCondition firstNameCondition = new QueryDefinitionConditionImpl(
                "firstNameCondition",
                getField(Field.FIRST_NAME.getName()),
                SqlComparisonFactory.getInstance().getComparison(StartsWithComparisonIgnoreCase.COMPARISON_NAME), "AND");
        select.addCondition(firstNameCondition);

        final QueryDefnCondition idCondition = new QueryDefinitionConditionImpl(
                "idCondition",
                getField(Field.PATIENT_ID.getName()),
                SqlComparisonFactory.getInstance().getComparison(EqualsComparison.COMPARISON_NAME), "AND");
        select.addCondition(idCondition);

        final QueryDefnCondition dobCondition = new QueryDefinitionConditionImpl(
                "dobCondition",
                getField(Field.DOB.getName()),
                SqlComparisonFactory.getInstance().getComparison(EqualsComparison.COMPARISON_NAME), "AND");
        select.addCondition(dobCondition);

        final QueryDefnCondition ssnCondition = new QueryDefinitionConditionImpl(
                "ssnCondition",
                getField("ssnProperty"),
                SqlComparisonFactory.getInstance().getComparison(EqualsComparison.COMPARISON_NAME), "AND");
        select.addCondition(ssnCondition);


        /*
        final QueryDefnCondition orgIdCondition = new QueryDefinitionConditionImpl(getField("orgId"),
                SqlComparisonFactory.getInstance().getComparison(EqualsComparison.COMPARISON_NAME));
        orgIdCondition.setConnector("and");
        select.addCondition(orgIdCondition);

        */
        final QueryDefinitionField idField = getField(Field.PATIENT_ID.getName());
        select.addOrderBy(new QueryDefinitionSortBy() {
            public boolean isDescending()
            {
                return false;
            }

            public QueryDefinitionField getField()
            {
                return idField;
            }

            public String getExplicitOrderByClause()
            {
                return null;
            }
        });
        return select;
    }

}
