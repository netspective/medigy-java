/*
 * Copyright (c) 2000-2003 Netspective Communications LLC. All rights reserved.
 *
 * Netspective Communications LLC ("Netspective") permits redistribution, modification and use of this file in source
 * and binary form ("The Software") under the Netspective Source License ("NSL" or "The License"). The following
 * conditions are provided as a summary of the NSL but the NSL remains the canonical license and must be accepted
 * before using The Software. Any use of The Software indicates agreement with the NSL.
 *
 * 1. Each copy or derived work of The Software must preserve the copyright notice and this notice unmodified.
 *
 * 2. Redistribution of The Software is allowed in object code form only (as Java .class files or a .jar file
 *    containing the .class files) and only as part of an application that uses The Software as part of its primary
 *    functionality. No distribution of the package is allowed as part of a software development kit, other library,
 *    or development tool without written consent of Netspective. Any modified form of The Software is bound by these
 *    same restrictions.
 *
 * 3. Redistributions of The Software in any form must include an unmodified copy of The License, normally in a plain
 *    ASCII text file unless otherwise agreed to, in writing, by Netspective.
 *
 * 4. The names "Netspective", "Axiom", "Commons", "Junxion", and "Sparx" are trademarks of Netspective and may not be
 *    used to endorse products derived from The Software without without written consent of Netspective. "Netspective",
 *    "Axiom", "Commons", "Junxion", and "Sparx" may not appear in the names of products derived from The Software
 *    without written consent of Netspective.
 *
 * 5. Please attribute functionality where possible. We suggest using the "powered by Netspective" button or creating
 *    a "powered by Netspective(tm)" link to http://www.netspective.com for each application using The Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT,
 * ARE HEREBY DISCLAIMED.
 *
 * NETSPECTIVE AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE OR ANY THIRD PARTY AS A
 * RESULT OF USING OR DISTRIBUTING THE SOFTWARE. IN NO EVENT WILL NETSPECTIVE OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THE SOFTWARE, EVEN
 * IF HE HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 */
package com.medigy.service.impl.insurance;

import com.medigy.persist.model.org.Organization;
import com.medigy.service.ServiceVersion;
import com.medigy.service.insurance.SelectCareProviderService;
import com.medigy.service.dto.ServiceParameters;
import com.medigy.service.dto.ServiceReturnValues;
import com.medigy.service.dto.insurance.CareProviderSelectionData;
import com.medigy.service.dto.insurance.SelectCareProviderParameters;

import java.util.List;

public class SelectCareProviderServiceImpl implements SelectCareProviderService
{
    public String[] isValid(ServiceParameters parameters)
    {
        return null;
    }

    public ServiceReturnValues createErrorResponse(final ServiceParameters params, final String errorMessage)
    {
        return null;
    }

    public ServiceVersion[] getSupportedServiceVersions()
    {
        return new ServiceVersion[0];
    }

  
    public CareProviderSelectionData selectCareProvider(final SelectCareProviderParameters params)
    {
        /*
        // first check to see if this person already has a primary care provider for this insurance policy
        InsurancePolicyFacade isManager = (InsurancePolicyFacade) FacadeManager.getInstance().get(InsurancePolicyFacade.class);
        final InsurancePolicy policy = isManager.getIndividualInsurancePolicy(params.getInsurancePolicyNumber());
        if (policy == null)
        {
            // no matching policy was found
            return new CareProviderSelectionData() {
                public Serializable getCareProviderSelectionId()
                {
                    return null;
                }

                public SelectCareProviderParameters getSelectCareProviderParameters()
                {
                    return null;
                }

                public String getErrorMessage()
                {
                    // TODO: This needs to be calculated based on Locale
                    return "No insurance policy was found with the insurance policy number: " + params.getInsurancePolicyNumber();
                }

            };
        }

        InsurancePolicyRole insuredPersonRole = policy.getInsuredPersonRole(params.getPatientId());
        if (insuredPersonRole == null)
        {
            return new CareProviderSelectionData() {
                public Serializable getCareProviderSelectionId()
                {
                    return null;
                }

                public SelectCareProviderParameters getSelectCareProviderParameters()
                {
                    return null;
                }

                public String getErrorMessage()
                {
                    // TODO: This needs to be calculated based on Locale
                    return "No insured person was found with the following ID: " + params.getPatientId();
                }

            };
        }
        //final CareProviderSelection currentCps = insuredPersonRole.getCurrentCareProviderSelection();
        final CareProviderSelection currentCps = (CareProviderSelection) insuredPersonRole.getCareProviderSelections().toArray()[0];
        if (currentCps != null)
        {
            currentCps.setThroughDate(new Date());
        }

        PersonFacade personFacade = (PersonFacade) FacadeManager.getInstance().get(PersonFacade.class);
        Person careProvider = personFacade.getPersonById(params.getCareProviderId());
        if (careProvider == null)
        {
            return new CareProviderSelectionData() {
                public Serializable getCareProviderSelectionId()
                {
                    return null;
                }

                public SelectCareProviderParameters getSelectCareProviderParameters()
                {
                    return null;
                }

                public String getErrorMessage()
                {
                    // TODO: This needs to be calculated based on Locale
                    return "Could not find Care Provider with ID: " + params.getCareProviderId();
                }
            };
        }


        final CareProviderSelection cps = new CareProviderSelection();
        cps.setInsurancePolicyRole(insuredPersonRole);
        cps.setHealthCarePractitioner(careProvider);
        cps.setFromDate(params.getFromDate());

        HibernateUtil.getSession().save(cps);
        HibernateUtil.getSession().flush();
        return new CareProviderSelectionData() {
            public SelectCareProviderParameters getSelectCareProviderParameters()
            {
                return params;
            }

            public Serializable getCareProviderSelectionId()
            {
                return cps.getCareProviderSelectionId();
            }

            public String getErrorMessage()
            {
                return null;
            }
        };
*/
        return null;
    }

    public List listCareProviders(final Organization providerOrganization)
    {
        return null;
    }
}
