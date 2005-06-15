/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.impl.insurance;

import com.medigy.persist.model.insurance.InsurancePlan;
import com.medigy.persist.model.insurance.InsurancePlanContactMechanism;
import com.medigy.persist.model.org.Organization;
import com.medigy.persist.model.party.PostalAddress;
import com.medigy.persist.model.party.PhoneNumber;
import com.medigy.persist.util.HibernateUtil;
import com.medigy.persist.reference.custom.party.ContactMechanismPurposeType;
import com.medigy.service.ServiceVersion;
import com.medigy.service.contact.ContactMechanismFacade;
import com.medigy.service.dto.ServiceParameters;
import com.medigy.service.dto.ServiceReturnValues;
import com.medigy.service.dto.insurance.AddInsurancePlanParameters;
import com.medigy.service.dto.insurance.NewInsurancePlanValues;
import com.medigy.service.dto.party.PhoneParameters;
import com.medigy.service.dto.party.PostalAddressParameters;
import com.medigy.service.insurance.AddInsurancePlanService;

import java.io.Serializable;

public class AddInsurancePlanServiceImpl implements AddInsurancePlanService
{
    private ContactMechanismFacade contactMechanismFacade;

    public ContactMechanismFacade getContactMechanismFacade()
    {
        return contactMechanismFacade;
    }

    public void setContactMechanismFacade(final ContactMechanismFacade contactMechanismFacade)
    {
        this.contactMechanismFacade = contactMechanismFacade;
    }

    public NewInsurancePlanValues add(AddInsurancePlanParameters parameters)
    {
        final Organization insuranceCarrier = (Organization) HibernateUtil.getSession().load(Organization.class, parameters.getInsuranceCarrierId());
        if (insuranceCarrier == null)
            return (NewInsurancePlanValues) createErrorResponse(parameters, "Unknown insurance carrier");

        final InsurancePlan plan = new InsurancePlan();
        plan.setName(parameters.getPlanName());
        insuranceCarrier.addInsurancePlan(plan);

        // handle the billing address
        final PostalAddressParameters billingAddress = parameters.getBillingAddress();
        if (billingAddress != null)
        {
            final PostalAddress address = contactMechanismFacade.addPostalAddress(billingAddress.getStreet1(), billingAddress.getStreet2(), billingAddress.getCity(),
                billingAddress.getState(), billingAddress.getProvince(), billingAddress.getCounty(), billingAddress.getPostalCode(),
                billingAddress.getCountry());
            InsurancePlanContactMechanism ipcm = new InsurancePlanContactMechanism();
            ipcm.setContactMechanism(address);
            ipcm.addPurpose(ContactMechanismPurposeType.Cache.BILLING_ADDRESS.getEntity());
            plan.addContactMechanismRelationship(ipcm);
        }
        // handle the phone and fax
        final PhoneParameters phoneParams = parameters.getPhone();
        if (phoneParams != null)
        {
            final PhoneNumber phone = contactMechanismFacade.addPhone(phoneParams.getCountryCode(), phoneParams.getAreaCode(), phoneParams.getNumber(),
                phoneParams.getExtension());
            InsurancePlanContactMechanism ipcm = new InsurancePlanContactMechanism();
            ipcm.setContactMechanism(phone);
            ipcm.addPurpose(ContactMechanismPurposeType.Cache.MAIN_OFFICE_NUMBER.getEntity());
            plan.addContactMechanismRelationship(ipcm);
        }
        final PhoneParameters faxParams = parameters.getFax();
        if (phoneParams != null)
        {
            final PhoneNumber fax = contactMechanismFacade.addPhone(faxParams.getCountryCode(), faxParams.getAreaCode(), faxParams.getNumber(),
                faxParams.getExtension());
            InsurancePlanContactMechanism ipcm = new InsurancePlanContactMechanism();
            ipcm.setContactMechanism(fax);
            ipcm.addPurpose(ContactMechanismPurposeType.Cache.FAX_NUMBER.getEntity());
            plan.addContactMechanismRelationship(ipcm);
        }

        // handle the CHAMPUS attributes
        
        return null;
    }

    public ServiceReturnValues createErrorResponse(final ServiceParameters params, final String errorMessage)
    {
        return new NewInsurancePlanValues() {
            public Serializable getNewInsurancePlanId()
            {
                return null;
            }

            public AddInsurancePlanParameters getAddInsurancePlanParameters()
            {
                return (AddInsurancePlanParameters) params;
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
