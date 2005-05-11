/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.model.insurance;

import com.medigy.persist.DbUnitTestCase;
import com.medigy.persist.model.person.Person;
import com.medigy.persist.util.HibernateUtil;

import java.util.Date;


public class TestCareProviderSelection  extends DbUnitTestCase
{
    public String getDataSetFile()
    {
        return "/com/medigy/persist/model/insurance/TestCareProviderSelection.xml";
    }

    public void testCareProviderSelection()
    {
        Person physician = (Person) HibernateUtil.getSession().load(Person.class, new Long(7));
        Person policyHolder = (Person) HibernateUtil.getSession().load(Person.class, new Long(3));

        //InsurancePolicyRole patientRole = (InsurancePolicyRole) HibernateUtil.getSession().load(InsurancePolicyRole.class, new Long(1));

        final CareProviderSelection selection = new CareProviderSelection();
        selection.setFromDate(new Date());
        selection.setHealthCarePractitioner(physician);
        selection.setInsuredPerson(policyHolder);
        HibernateUtil.getSession().save(selection);
        HibernateUtil.closeSession();

        final CareProviderSelection savedSelection = (CareProviderSelection) HibernateUtil.getSession().load(CareProviderSelection.class, selection.getCareProviderSelectionId());
        assertEquals(physician.getPartyId(), savedSelection.getHealthCarePractitioner().getPartyId());
        //assertEquals(patientRole.getInsurancePolicyRoleId(), savedSelection.getInsurancePolicyRole().getInsurancePolicyRoleId());

    }
}
