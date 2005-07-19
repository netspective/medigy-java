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
import com.medigy.persist.model.party.PartyIdentifier;
import com.medigy.persist.model.org.Organization;
import com.medigy.persist.model.person.PersonRole;
import com.medigy.persist.model.person.Person;
import com.medigy.persist.reference.custom.person.PersonIdentifierType;
import com.medigy.persist.reference.custom.person.PersonRoleType;

import java.util.List;
import java.util.ArrayList;

public class PatientSearchQueryDefinition extends BasicQueryDefinition implements QueryDefinition
{
    public static final String QUERY_DEFINITION_NAME = "patientSearch";

    public PatientSearchQueryDefinition()
    {
        super(QUERY_DEFINITION_NAME);
        register();
    }

    protected void register()
    {
        final QueryDefinitionJoin personJoin = new QueryDefinitionJoinImpl("person", Person.class, this);
        personJoin.setAutoInclude(true);

        final QueryDefinitionJoin personIdentifierJoin =  new QueryDefinitionJoinImpl("personIdent", PartyIdentifier.class, this);
        personIdentifierJoin.setCondition("personIdent.party.id = person.id and personIdent.type.id = " + PersonIdentifierType.Cache.SSN.getEntity().getSystemId());

        final QueryDefinitionJoin orgJoin =  new QueryDefinitionJoinImpl("org", Organization.class, this);

        final QueryDefinitionJoin personRoleJoin = new QueryDefinitionJoinImpl("personRole", PersonRole.class, this);
        personRoleJoin.setCondition("personRole.party.id = person.id");

        this.addJoin(personJoin);
        this.addJoin(orgJoin);
        this.addJoin(personIdentifierJoin);
        this.addJoin(personRoleJoin);

        addField("personId", "id", "Patient ID", personJoin);
        addField("firstName", "firstName", "First Name", personJoin);
        addField("lastName", "lastName", "Last Name", personJoin);
        addField("birthDate", "birthDate", "Date of Birth", personJoin);
        addField("gender", "currentGenderType.label", "Gender", personJoin);

        final QueryDefinitionField orgField = addField("organizationId", "org.id", "Organization ID", personJoin);
        orgField.setHqlJoinExpr("left outer join person.roles as personRoles " +
                "left outer join personRoles.personOrgRelationships as rel " +
                "left outer join rel.organizationRole as orgRole " +
                "left outer join orgRole.organization as org ");
        orgField.setSelectClauseExpr("org.id");
        orgField.setWhereClauseExpr("org.id");
        orgField.setDisplayAllowed(false);

        // TODO: displayAllowed = NONE, ALWAYS, ON_CRITERIA


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
        select.addDisplayField(getField(Field.GENDER.getName()));
        select.addDisplayField(getField(Field.DOB.getName()));

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
