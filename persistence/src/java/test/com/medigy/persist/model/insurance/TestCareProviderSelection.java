/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.model.insurance;

import com.medigy.persist.TestCase;
import com.medigy.persist.model.party.PartyRole;
import com.medigy.persist.model.person.Person;
import com.medigy.persist.model.person.PersonRole;
import com.medigy.persist.reference.custom.person.PersonRoleType;
import com.medigy.persist.util.HibernateUtil;

import java.util.Date;


/**
 * Test case for testing the CareProviderSelection class
 *
 * @see com.medigy.persist.model.insurance.CareProviderSelection
 */
public final class TestCareProviderSelection extends TestCase
{
    public String getDataSetFile()
    {
        return "/com/medigy/persist/model/insurance/TestCareProviderSelection.xml";
    }

    public void testCareProviderSelection() throws Exception
    {
        //assertDBAsExpected(new String[] {"PARTY_ROLE_TYPE"});

        Person physician = (Person) HibernateUtil.getSession().load(Person.class, new Long(7));
        assertNotNull(physician);
        Person policyHolder = (Person) HibernateUtil.getSession().load(Person.class, new Long(3));

        PersonRole physicianRole = physician.getRole(PersonRoleType.Cache.INDIVIDUAL_HEALTH_CARE_PRACTITIONER.getEntity());
        if (physicianRole == null)
        {
            physicianRole = new PersonRole();
            physicianRole.setType(PersonRoleType.Cache.INDIVIDUAL_HEALTH_CARE_PRACTITIONER.getEntity());
            physicianRole.setPerson(physician);
            physician.addRole(physicianRole);
            //HibernateUtil.getSession().save(physicianRole);
            HibernateUtil.getSession().flush();
        }

        PersonRole patientRole = policyHolder.getRole(PersonRoleType.Cache.PATIENT.getEntity());
        if (patientRole == null)
        {
            patientRole = new PersonRole();
            patientRole.setType(PersonRoleType.Cache.PATIENT.getEntity());
            patientRole.setPerson(policyHolder);
            policyHolder.addRole(patientRole);
            HibernateUtil.getSession().flush();
        }

        final CareProviderSelection selection = new CareProviderSelection();
        HibernateUtil.getSession().save(selection);
        HibernateUtil.closeSession();

    }
}
