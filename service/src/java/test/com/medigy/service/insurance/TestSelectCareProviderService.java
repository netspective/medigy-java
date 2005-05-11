/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.insurance;

import com.medigy.persist.DbUnitTestCase;
import com.medigy.persist.model.insurance.InsurancePolicy;
import com.medigy.persist.util.HibernateUtil;
import com.medigy.service.dto.insurance.SelectCareProviderParameters;
import com.medigy.service.dto.insurance.CareProviderSelectionData;
import com.medigy.service.util.FacadeManager;
import com.medigy.service.util.InsurancePolicyFacadeImpl;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;
import java.util.Date;

public class TestSelectCareProviderService extends DbUnitTestCase
{

    protected void setUp() throws Exception
    {
        super.setUp();
        FacadeManager.getInstance().add(new InsurancePolicyFacadeImpl());
    }

    public String getDataSetFile()
    {
        return "/com/medigy/service/insurance/TestSelectCareProviderService.xml";
    }

    public void testSelectCareProvider()
    {
        final SelectCareProviderService service = new SelectCareProviderServiceImpl();
        CareProviderSelectionData data = service.selectCareProvider(new SelectCareProviderParameters()
                {
                    public Serializable getPersonId()
                    {
                        return new Long(3);
                    }

                    public String getInsurancePolicyNumber()
                    {
                        return "12345";
                    }

                    public Serializable getCareProviderId()
                    {
                        return new Long(7);
                    }

                    public Date getFromDate()
                    {
                        return new Date();
                    }

                    public Date getThroughDate()
                    {
                        return null;
                    }
                });
        if (data.getErrorMessage() != null)
        {
            fail(data.getErrorMessage());
        }

        Criteria criteria = HibernateUtil.getSession().createCriteria(InsurancePolicy.class);
        criteria.add(Restrictions.eq("policyNumber", "12345"));
        criteria.createCriteria("agreementRoles").createCriteria("party").add(Restrictions.eq("partyId", new Long(3)));
        final InsurancePolicy policy = (InsurancePolicy) criteria.uniqueResult();
        //final Long careProviderId = policy.getInsuredPersonRole(new Long(3)).getCurrentCareProviderSelection().getHealthCarePractitioner().getPartyId();
        //assertEquals("7", careProviderId);

    }
}
