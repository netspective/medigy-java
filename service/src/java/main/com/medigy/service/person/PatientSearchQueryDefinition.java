/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.person;

import com.medigy.persist.util.query.QueryDefinition;
import com.medigy.persist.util.query.QueryDefinitionField;
import com.medigy.persist.util.query.QueryDefinitionJoin;
import com.medigy.persist.util.query.exception.QueryDefinitionException;
import com.medigy.persist.util.query.impl.BasicQueryDefinition;
import com.medigy.persist.model.party.PartyRelationship;
import com.medigy.persist.model.party.PartyRole;
import com.medigy.persist.model.party.PartyIdentifier;
import com.medigy.persist.model.org.Organization;
import com.medigy.persist.reference.custom.person.PersonIdentifierType;

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
                return null;
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

        final QueryDefinitionJoin partyRelationshipJoin = new QueryDefinitionJoin() {
            public QueryDefinition getOwner()
            {
                return null;
            }

            public String getName()
            {
                return "rel";
            }

            public String getFromExpr()
            {
                return getTable() + " " + getName();
            }

            public String getCondition()
            {
                return "rel.partyFrom.id = org.partyId AND rel.partyTo.id = person.partyId";
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
                final List<QueryDefinitionJoin> impliedJoins = new ArrayList<QueryDefinitionJoin>();
                impliedJoins.add(orgJoin);
                return impliedJoins;
            }

            public String getTable()
            {
                return PartyRelationship.class.getSimpleName();
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
                return PartyRole.class.getSimpleName();
            }
        };

        this.addJoin(personJoin);
        this.addJoin(orgJoin);
        //this.addJoin(partyRelationshipJoin);
        this.addJoin(personIdentifierJoin);
        this.addJoin(personRoleJoin);

        addField("firstName", "firstName", personJoin);
        addField("lastName", "lastName", personJoin);
        addField("firstName", "firstName", personJoin);
        addField("gender", "currentGenderType.label", personJoin);
        addField("personRole", "type.code", personRoleJoin);
        addField("ssn", "identifierValue", personIdentifierJoin);
        addField("driversLicense", "driversLicenseNumber", personJoin);


        addField("organizationId", "partyId", orgJoin);
        addField("personId", "id", personJoin);
        //addField("organizationRelationshipTypeCode", "type.code", partyRelationshipJoin);

    }

}
