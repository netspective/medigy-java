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

import com.medigy.persist.model.insurance.InsuranceProduct;
import com.medigy.persist.model.insurance.InsuranceProductContactMechanism;
import com.medigy.persist.model.org.Organization;
import com.medigy.persist.model.party.PhoneNumber;
import com.medigy.persist.model.party.PostalAddress;
import com.medigy.persist.reference.custom.party.ContactMechanismPurposeType;
import com.medigy.persist.reference.custom.insurance.InsuranceProductType;
import com.medigy.persist.reference.custom.invoice.BillRemittanceType;
import com.medigy.persist.util.HibernateUtil;
import com.medigy.service.ServiceVersion;
import com.medigy.service.util.ReferenceEntityFacade;
import com.medigy.service.contact.ContactMechanismFacade;
import com.medigy.service.dto.ServiceParameters;
import com.medigy.service.dto.ServiceReturnValues;
import com.medigy.service.dto.insurance.AddInsuranceProductParameters;
import com.medigy.service.dto.insurance.NewInsuranceProduct;
import com.medigy.service.dto.party.PhoneParameters;
import com.medigy.service.dto.party.PostalAddressParameters;
import com.medigy.service.insurance.AddInsuranceProductService;

import java.io.Serializable;

public class AddInsuranceProductServiceImpl implements AddInsuranceProductService
{
    private ContactMechanismFacade contactMechanismFacade;
    private ReferenceEntityFacade referenceEntityFacade;

    public ReferenceEntityFacade getReferenceEntityFacade()
    {
        return referenceEntityFacade;
    }

    public void setReferenceEntityFacade(final ReferenceEntityFacade referenceEntityFacade)
    {
        this.referenceEntityFacade = referenceEntityFacade;
    }

    public ContactMechanismFacade getContactMechanismFacade()
    {
        return contactMechanismFacade;
    }

    public void setContactMechanismFacade(final ContactMechanismFacade contactMechanismFacade)
    {
        this.contactMechanismFacade = contactMechanismFacade;
    }

    public NewInsuranceProduct add(final AddInsuranceProductParameters parameters)
    {
        final Organization insuranceCarrier = (Organization) HibernateUtil.getSession().load(Organization.class, parameters.getInsuranceCarrierId());
        if (insuranceCarrier == null)
            return (NewInsuranceProduct) createErrorResponse(parameters, "Unknown insurance carrier");
        final InsuranceProductType productType = referenceEntityFacade.getInsuranceProductType(parameters.getProductTypeCode());
        if (productType == null)
            return (NewInsuranceProduct)  createErrorResponse(parameters, "Unknown insurance product type");
        final BillRemittanceType remittanceType = referenceEntityFacade.getBillRemittanceType(parameters.getRemittanceTypeCode());
        if (remittanceType == null)
            return (NewInsuranceProduct)  createErrorResponse(parameters, "Unknown bill remittance type");

        final InsuranceProduct product = new InsuranceProduct();
        product.setName(parameters.getProductName());
        product.setType(productType);
        product.setRemittanceType(remittanceType);
        
        insuranceCarrier.addInsuranceProduct(product);

        // handle the billing address
        final PostalAddressParameters billingAddress = parameters.getBillingAddress();
        if (billingAddress != null)
        {
            final PostalAddress address = contactMechanismFacade.addPostalAddress(billingAddress.getStreet1(), billingAddress.getStreet2(), billingAddress.getCity(),
                billingAddress.getState(), billingAddress.getProvince(), billingAddress.getCounty(), billingAddress.getPostalCode(),
                billingAddress.getCountry());
            InsuranceProductContactMechanism ipcm = new InsuranceProductContactMechanism();
            ipcm.setContactMechanism(address);
            ipcm.addPurpose(ContactMechanismPurposeType.Cache.BILLING_ADDRESS.getEntity());
            product.addProductContactMechanism(ipcm);
        }
        // handle the phone and fax
        final PhoneParameters phoneParams = parameters.getPhone();
        if (phoneParams != null)
        {
            final PhoneNumber phone = contactMechanismFacade.addPhone(phoneParams.getCountryCode(), phoneParams.getAreaCode(), phoneParams.getNumber(),
                phoneParams.getExtension());
            InsuranceProductContactMechanism ipcm = new InsuranceProductContactMechanism();
            ipcm.setContactMechanism(phone);
            ipcm.addPurpose(ContactMechanismPurposeType.Cache.MAIN_OFFICE_NUMBER.getEntity());
            product.addProductContactMechanism(ipcm);
        }
        final PhoneParameters faxParams = parameters.getFax();
        if (phoneParams != null)
        {
            final PhoneNumber fax = contactMechanismFacade.addPhone(faxParams.getCountryCode(), faxParams.getAreaCode(), faxParams.getNumber(),
                faxParams.getExtension());
            InsuranceProductContactMechanism ipcm = new InsuranceProductContactMechanism();
            ipcm.setContactMechanism(fax);
            ipcm.addPurpose(ContactMechanismPurposeType.Cache.FAX_NUMBER.getEntity());
            product.addProductContactMechanism(ipcm);
        }

        return null;
    }

    public ServiceReturnValues createErrorResponse(final ServiceParameters params, final String errorMessage)
    {
        return new NewInsuranceProduct() {
            public Serializable getNewInsuranceProductId()
            {
                return null;
            }

            public AddInsuranceProductParameters getAddInsuranceProductParameters()
            {
                return (AddInsuranceProductParameters) params;
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
