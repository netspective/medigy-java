/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.insurance;

import com.medigy.persist.model.insurance.InsurancePolicy;
import com.medigy.service.AbstractSpringTestCase;
import com.medigy.service.dto.insurance.CareProviderSelectionData;
import com.medigy.service.dto.insurance.SelectCareProviderParameters;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;
import java.util.Date;

public class TestSelectCareProviderService extends AbstractSpringTestCase
{
    private SelectCareProviderService selectCareProviderService;

    public void setSelectCareProviderService(final SelectCareProviderService selectCareProviderService)
    {
        this.selectCareProviderService = selectCareProviderService;
    }

    public void testSelectCareProvider()
    {
        CareProviderSelectionData data = selectCareProviderService.selectCareProvider(new SelectCareProviderParameters()
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

        Criteria criteria = getSession().createCriteria(InsurancePolicy.class);
        criteria.add(Restrictions.eq("policyNumber", "12345"));
        criteria.createCriteria("agreementRoles").createCriteria("party").add(Restrictions.eq("partyId", new Long(3)));
        final InsurancePolicy policy = (InsurancePolicy) criteria.uniqueResult();

    }

}
