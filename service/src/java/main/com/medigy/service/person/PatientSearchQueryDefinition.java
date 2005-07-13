/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.person;

import com.medigy.persist.util.query.QueryDefinition;
import com.medigy.persist.util.query.QueryDefinitionField;
import com.medigy.persist.util.query.QueryDefinitionJoin;
import com.medigy.persist.util.query.impl.BasicQueryDefinition;
import com.medigy.persist.model.party.PartyIdentifier;
import com.medigy.persist.model.org.Organization;
import com.medigy.persist.model.person.PersonRole;
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
        final QueryDefinitionJoin personJoin = new QueryDefinitionJoin() {
            public QueryDefinition getOwner()
            {
                return null;
            }

            public String getName()
            {
                return "person";
            }

            public String getFromExpr()
            {
                return getTable() + " " + getName();
            }

            public String getCondition()
            {
                return null;
            }

            public boolean isAutoInclude()
            {
                return true;
            }

            public String getImplyJoinsStr()
            {
                return "";
            }

            public List<QueryDefinitionJoin> getImpliedJoins()
            {
                return null;
            }

            public String getTable()
            {
                return "Person";
            }
        };

        final QueryDefinitionJoin personIdentifierJoin = new QueryDefinitionJoin() {
            public QueryDefinition getOwner()
            {
                return null;
            }

            public String getName()
            {
                return "personIdent";
            }

            public String getFromExpr()
            {
                return getTable() + " " + getName();
            }

            public String getCondition()
            {
                return "personIdent.party.id = person.id and personIdent.type.id = " + PersonIdentifierType.Cache.SSN.getEntity().getSystemId();
            }

            public boolean isAutoInclude()
            {
                return false;
            }

            public String getImplyJoinsStr()
            {
                return null;
            }

            public List<QueryDefinitionJoin> getImpliedJoins()
            {
                return null;
            }

            public String getTable()
            {
                return PartyIdentifier.class.getSimpleName();
            }
        };

        final QueryDefinitionJoin orgJoin = new QueryDefinitionJoin() {
            public QueryDefinition getOwner()
            {
                return null;
            }

            public String getName()
            {
                return "org";
            }

            public String getFromExpr()
            {
                return getTable() + " " + getName();
            }

            public String getCondition()
            {
                return null;
            }

            public boolean isAutoInclude()
            {
                return false;
            }

            public String getImplyJoinsStr()
            {
                return null;
            }

            public List<QueryDefinitionJoin> getImpliedJoins()
            {
                return null;
            }

            public String getTable()
            {
                return Organization.class.getSimpleName();
            }
        };



        final QueryDefinitionJoin personRoleJoin = new QueryDefinitionJoin() {
            public QueryDefinition getOwner()
            {
                return null;
            }

            public String getName()
            {
                return "personRole";
            }

            public String getFromClauseExpr()
            {
                return getTable() + " " + getName();
            }

            public String getCondition()
            {
                return "personRole.party.id = person.id";
            }

            public boolean isAutoInclude()
            {
                return false;
            }

            public String getImplyJoinsStr()
            {
                return null;
            }

            public List<QueryDefinitionJoin> getImpliedJoins()
            {
                return null;
            }

            public String getFromExpr()
            {
                return null;
            }

            public String getTable()
            {
                return PersonRole.class.getSimpleName();
            }
        };

        this.addJoin(personJoin);
        this.addJoin(orgJoin);
        //this.addJoin(partyRelationshipJoin);
        this.addJoin(personIdentifierJoin);
        this.addJoin(personRoleJoin);

        addField("firstName", "firstName", "First Name", personJoin);
        addField("lastName", "lastName", "Last Name", personJoin);
        //addField("firstName", "firstName", personJoin);
        addField("gender", "currentGenderType.label", "Gender", personJoin);
        //addField("personRole", "type.code", personRoleJoin);
        //addField("ssn", "identifierValue", personIdentifierJoin);
        //addField("driversLicense", "driversLicenseNumber", personJoin);


        final QueryDefinitionField orgField = addField("organizationId", "org.id", "Organization ID", personJoin);
        orgField.setHqlJoinExpr("left outer join person.roles as personRoles " +
                "left outer join personRoles.personOrgRelationships as rel " +
                "left outer join rel.organizationRole as orgRole " +
                "left outer join orgRole.organization as org ");
        orgField.setSelectClauseExpr("org.id");



        addField("personId", "id", "Patient ID", personJoin);
        //addField("organizationRelationshipTypeCode", "type.code", partyRelationshipJoin);

    }

}
