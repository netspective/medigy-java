/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.person;

import com.medigy.service.query.QueryDefinition;
import com.medigy.service.query.QueryDefinitionField;
import com.medigy.service.query.QueryDefinitionJoin;
import com.medigy.service.query.impl.BasicQueryDefinition;
import com.medigy.persist.model.person.Person;
import com.medigy.persist.model.party.PartyRelationship;
import com.medigy.persist.model.org.Organization;

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
        final QueryDefinitionJoin personJoin = createJoin();
        personJoin.setTableName(Person.class.getSimpleName());
        personJoin.setName("person");
        personJoin.setAutoInclude(true);

        final QueryDefinitionJoin orgJoin = createJoin();
        orgJoin.setTableName(Organization.class.getSimpleName());
        orgJoin.setName("org");

        final QueryDefinitionJoin partyRelationshipJoin = createJoin();
        partyRelationshipJoin.setTableName(PartyRelationship.class.getName());
        partyRelationshipJoin.setName("rel");
        partyRelationshipJoin.setCondition("rel.partyFrom.id = org.partyId AND rel.partyTo.id = person.partyId");
        partyRelationshipJoin.addImpliedJoin(orgJoin);

        this.addJoin(personJoin);
        this.addJoin(orgJoin);
        this.addJoin(partyRelationshipJoin);

        final QueryDefinitionField firstNameField = addField("firstName", "firstName", personJoin);
        final QueryDefinitionField lastNameField = addField("lastName", "lastName", personJoin);
        final QueryDefinitionField genderField = addField("gender", "genders[]", 
                personJoin);

        final QueryDefinitionField orgIdField = addField("organizationId", "partyId", orgJoin);
        final QueryDefinitionField personIdField = addField("personId", "partyId", personJoin);
        final QueryDefinitionField partyRelTypeField = addField("organizationRelationshipTypeCode", "type.code",
                partyRelationshipJoin);

    }

}
