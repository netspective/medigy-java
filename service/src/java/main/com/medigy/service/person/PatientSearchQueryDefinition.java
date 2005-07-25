/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.person;

import com.medigy.persist.util.query.QueryDefinition;
import com.medigy.persist.util.query.QueryDefinitionField;
import com.medigy.persist.util.query.QueryDefinitionJoin;
import com.medigy.persist.util.query.QueryDefnCondition;
import com.medigy.persist.util.query.QueryDefinitionSelect;
import com.medigy.persist.util.query.QueryDefinitionSortBy;
import com.medigy.persist.util.query.SqlComparisonFactory;
import com.medigy.persist.util.query.comparison.StartsWithComparisonIgnoreCase;
import com.medigy.persist.util.query.comparison.EqualsComparison;
import com.medigy.persist.util.query.exception.QueryDefinitionException;
import com.medigy.persist.util.query.impl.BasicQueryDefinition;
import com.medigy.persist.util.query.impl.QueryDefinitionSelectImpl;
import com.medigy.persist.util.query.impl.QueryDefinitionConditionImpl;
import com.medigy.persist.util.query.impl.QueryDefinitionJoinImpl;
import com.medigy.persist.util.query.impl.QueryDefinitionFieldImpl;
import com.medigy.persist.model.party.PartyIdentifier;
import com.medigy.persist.model.org.Organization;
import com.medigy.persist.model.person.PersonRole;
import com.medigy.persist.model.person.Person;
import com.medigy.persist.model.person.PersonIdentifier;
import com.medigy.persist.reference.custom.person.PersonIdentifierType;

public class PatientSearchQueryDefinition extends BasicQueryDefinition implements QueryDefinition
{
    public static final String QUERY_DEFINITION_NAME = "patientSearch";

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

        final QueryDefinitionJoin personIdentifierJoin =  new QueryDefinitionJoinImpl("personIdent", PersonIdentifier.class, this);
        personIdentifierJoin.setCondition("personIdent.party.id = person.id");
        // "and personIdent.type.id = " + PersonIdentifierType.Cache.SSN.getEntity().getSystemId());

        final QueryDefinitionJoin orgJoin =  new QueryDefinitionJoinImpl("org", Organization.class, this);

        final QueryDefinitionJoin personRoleJoin = new QueryDefinitionJoinImpl("personRole", PersonRole.class, this);
        personRoleJoin.setCondition("personRole.party.id = person.id");


        this.addJoin(personJoin);
        this.addJoin(orgJoin);
        this.addJoin(personIdentifierJoin);
        this.addJoin(personRoleJoin);

        addField("personId", "partyId", "Patient ID", personJoin);
        addField("firstName", "firstName", "First Name", personJoin);
        addField("lastName", "lastName", "Last Name", personJoin);
        addField("birthDate", "birthDate", "Date of Birth", personJoin);
        //addField("gender", "currentGenderType.label", "Gender", personJoin);
        //addField("ssn", "currentGenderType.label", "Gender", personJoin);


        /*
        final QueryDefinitionField optionalOrgField = addField("organizationId", "org.id", "Organization ID", personJoin);
        optionalOrgField.setHqlJoinExpr("left outer join person.roles as personRoles " +
                "left outer join personRoles.personOrgRelationships as rel " +
                "left outer join rel.organizationRole as orgRole " +
                "left outer join orgRole.organization as org ");
        optionalOrgField.setSelectClauseExpr("org.id");
        optionalOrgField.setWhereClauseExpr("org.id");
        optionalOrgField.setDisplayAllowed(false);

        // this should only be available for CONDITION.
        final QueryDefinitionField orgIdField = addField("orgId", "partyId", "Organization ID", personJoin);
        orgIdField.setHqlJoinExpr("left join person.roles as personRoles " +
                "left join personRoles.personOrgRelationships as rel " +
                "left join rel.organizationRole as orgRole " +
                "left join orgRole.organization as org ");

        // this should only be available for display if the ORG ID was provided as a condition
        final QueryDefinitionField orgNameField = addField("orgName", "partyName", "Organization Name",
                personJoin);
        orgNameField.setHqlJoinExpr("left join person.roles as personRoles " +
                "left join personRoles.personOrgRelationships as rel " +
                "left join rel.organizationRole as orgRole " +
                "left join orgRole.organization as org ");
        */

        // use this for display of SSN in result but it shouldn't be part of the SELECT clause!
        final QueryDefinitionField ssnPropertyField = addField("ssnProperty", "ssn", "SSN", personJoin);
        ssnPropertyField.setHqlJoinExpr("left join fetch " + personJoin.getName() + ".personIdentifiers as pi");
        ssnPropertyField.setWhereClauseExpr("pi.type.id = " + PersonIdentifierType.Cache.SSN.getEntity().getSystemId()  +
            " AND pi.identifierValue ");

        /*
        final QueryDefinitionField driversLicensePropertyField = addField("driversLicProperty", "driversLicenseNumber",
                "Driver's License", personJoin);
        driversLicensePropertyField.setHqlJoinExpr("left join fetch " + personJoin.getName() + ".personIdentifiers as pi");
        */
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
        ORG_ID("orgId");

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
        final QueryDefinitionSelect select = new QueryDefinitionSelectImpl("criteriaSearch", this);
        select.addDisplayField(getField(Field.PATIENT_ID.getName()));
        select.addDisplayField(getField(Field.FIRST_NAME.getName()));
        select.addDisplayField(getField(Field.LAST_NAME.getName()));
        //select.addDisplayField(getField(Field.GENDER.getName()));
        select.addDisplayField(getField(Field.DOB.getName()));
        select.addDisplayField(getField("ssnProperty"));

        final QueryDefnCondition lastNameCondition = new QueryDefinitionConditionImpl(getField(Field.LAST_NAME.getName()),
                SqlComparisonFactory.getInstance().getComparison(StartsWithComparisonIgnoreCase.COMPARISON_NAME));
        lastNameCondition.setConnector("and");
        select.addCondition(lastNameCondition);

        final QueryDefnCondition idCondition = new QueryDefinitionConditionImpl(getField(Field.PATIENT_ID.getName()),
                SqlComparisonFactory.getInstance().getComparison(EqualsComparison.COMPARISON_NAME));
        idCondition.setConnector("and");
        select.addCondition(idCondition);

        final QueryDefnCondition dobCondition = new QueryDefinitionConditionImpl(getField(Field.DOB.getName()),
                SqlComparisonFactory.getInstance().getComparison(EqualsComparison.COMPARISON_NAME));
        dobCondition.setConnector("and");
        select.addCondition(dobCondition);

        final QueryDefnCondition ssnCondition = new QueryDefinitionConditionImpl(getField("ssnProperty"),
                SqlComparisonFactory.getInstance().getComparison(EqualsComparison.COMPARISON_NAME));
        ssnCondition.setConnector("and");
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
