/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.impl.insurance;

import com.medigy.service.insurance.AddInsurancePolicyHolderService;
import com.medigy.service.dto.insurance.AddInsurancePolicyHolderParameters;
import com.medigy.service.dto.insurance.NewInsurancePolicyHolderValues;
import com.medigy.service.dto.ServiceParameters;
import com.medigy.service.dto.ServiceReturnValues;
import com.medigy.service.dto.person.PersonParameters;
import com.medigy.service.ServiceVersion;
import com.medigy.service.util.ReferenceEntityFacade;
import com.medigy.service.person.PersonFacade;
import com.medigy.persist.model.person.Person;
import com.medigy.persist.reference.custom.person.PersonRoleType;

import java.io.Serializable;

/**
 * Adds a policy holder person related to a patient
 */
public class AddInsurancePolicyHolderServiceImpl implements AddInsurancePolicyHolderService
{
    private ReferenceEntityFacade referenceEntityFacade;
    private PersonFacade personFacade;

    public ReferenceEntityFacade getReferenceEntityFacade()
    {
        return referenceEntityFacade;
    }

    public void setReferenceEntityFacade(final ReferenceEntityFacade referenceEntityFacade)
    {
        this.referenceEntityFacade = referenceEntityFacade;
    }

    public PersonFacade getPersonFacade()
    {
        return personFacade;
    }

    public void setPersonFacade(final PersonFacade personFacade)
    {
        this.personFacade = personFacade;
    }

    public NewInsurancePolicyHolderValues add(final AddInsurancePolicyHolderParameters policyHolderParams)
    {
        final PersonParameters params = policyHolderParams.getPolicyHolder();
        final Person person;

        try
        {
            person = personFacade.createPerson(params);
        }
        catch (Exception e)
        {
            return (NewInsurancePolicyHolderValues) createErrorResponse(policyHolderParams, e.getMessage());
        }

        final PersonRoleType roleType = referenceEntityFacade.getPersonRoleType(policyHolderParams.getPolicyHolderRoleCode());
        personFacade.addPersonRole(person, roleType);

        // TODO: now create the relationship between the patient and the policy holder
        return  new NewInsurancePolicyHolderValues() {
            public Serializable getPolicyHolderId()
            {
                return person.getPartyId();
            }

            public AddInsurancePolicyHolderParameters getParameters()
            {
                return policyHolderParams;
            }

            public String getErrorMessage()
            {
                return null;
            }
        } ;
    }

    public ServiceReturnValues createErrorResponse(final ServiceParameters params, final String errorMessage)
    {
        return new NewInsurancePolicyHolderValues() {
            public Serializable getPolicyHolderId()
            {
                return null;
            }

            public AddInsurancePolicyHolderParameters getParameters()
            {
                return (AddInsurancePolicyHolderParameters) params;
            }

            public String getErrorMessage()
            {
                return errorMessage;
            }
        };
    }

    public ServiceVersion[] getSupportedServiceVersions()
    {
        return new ServiceVersion[0];
    }

    public String[] isValid(ServiceParameters parameters)
    {
        return null;
    }
}
