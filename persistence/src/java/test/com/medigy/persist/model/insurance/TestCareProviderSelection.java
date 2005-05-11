/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.model.insurance;

import com.medigy.persist.DbUnitTestCase;
import com.medigy.persist.model.party.PartyRole;
import com.medigy.persist.model.person.Person;
import com.medigy.persist.reference.custom.person.PersonRoleType;
import com.medigy.persist.util.HibernateUtil;

import java.util.Date;


/**
 * Test case for testing the CareProviderSelection class
 *
 * @see com.medigy.persist.model.insurance.CareProviderSelection
 */
public final class TestCareProviderSelection  extends DbUnitTestCase
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
        System.out.println(physician.getPartyId() + " " + physician.getLastName());        

        System.out.println(PersonRoleType.Cache.INDIVIDUAL_HEALTH_CARE_PRACTITIONER.getEntity().getSystemId());
        PartyRole physicianRole = physician.getPartyRole(PersonRoleType.Cache.INDIVIDUAL_HEALTH_CARE_PRACTITIONER.getEntity());
        if (physicianRole == null)
        {
            physicianRole = new PartyRole();
            physicianRole.setType(PersonRoleType.Cache.INDIVIDUAL_HEALTH_CARE_PRACTITIONER.getEntity());
            physicianRole.setParty(physician);
            physician.addPartyRole(physicianRole);
            //HibernateUtil.getSession().save(physicianRole);
            HibernateUtil.getSession().flush();
        }

        PartyRole patientRole = policyHolder.getPartyRole(PersonRoleType.Cache.PATIENT.getEntity());
        if (patientRole == null)
        {
            patientRole = new PartyRole();
            patientRole.setType(PersonRoleType.Cache.PATIENT.getEntity());
            patientRole.setParty(policyHolder);
            policyHolder.addPartyRole(patientRole);
            HibernateUtil.getSession().flush();
        }

        //InsurancePolicyRole patientRole = (InsurancePolicyRole) HibernateUtil.getSession().load(InsurancePolicyRole.class, new Long(1));

        final CareProviderSelection selection = new CareProviderSelection();
        selection.setFromDate(new Date());
        selection.setPartyTo(physician);
        selection.setPartyFrom(policyHolder);
        selection.setPartyRoleTo(physicianRole);
        selection.setPartyRoleFrom(patientRole);
        HibernateUtil.getSession().save(selection);
        HibernateUtil.closeSession();

        final CareProviderSelection savedSelection = (CareProviderSelection) HibernateUtil.getSession().load(CareProviderSelection.class, selection.getCareProviderSelectionId());
        assertNotNull(savedSelection);
        //assertEquals(physician.getPartyId(), savedSelection.getHealthCarePractitioner().getPartyId());
        //assertEquals(patientRole.getInsurancePolicyRoleId(), savedSelection.getInsurancePolicyRole().getInsurancePolicyRoleId());

    }
}
