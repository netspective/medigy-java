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

import com.medigy.persist.model.invoice.Invoice;
import com.medigy.persist.model.org.Organization;
import com.medigy.persist.reference.custom.claim.ClaimType;
import com.medigy.persist.reference.custom.invoice.InvoiceType;
import com.medigy.service.AbstractService;
import com.medigy.service.ServiceVersion;
import com.medigy.service.dto.ServiceParameters;
import com.medigy.service.dto.ServiceReturnValues;
import com.medigy.service.dto.insurance.AddClaimParameters;
import com.medigy.service.insurance.AddClaimService;
import com.medigy.service.util.ReferenceEntityFacade;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;
import org.hibernate.SessionFactory;

import java.util.Locale;
import java.util.ResourceBundle;

public class AddClaimServiceImpl extends AbstractService implements AddClaimService
{
    private static final ClassValidator addressValidator = new ClassValidator(AddClaimParameters.class,
                ResourceBundle.getBundle("messages", Locale.ENGLISH));

    private ReferenceEntityFacade referenceEntityFacade;

    public AddClaimServiceImpl(final SessionFactory sessionFactory, final ReferenceEntityFacade referenceEntityFacade)
    {
        super(sessionFactory);
        this.referenceEntityFacade = referenceEntityFacade;
    }

    public void setReferenceEntityFacade(final ReferenceEntityFacade referenceEntityFacade)
    {
        this.referenceEntityFacade = referenceEntityFacade;
    }

    public NewClaimValues add(final AddClaimParameters paramaters)
    {
        String[] validationErrors =  isValid(paramaters);
        if (validationErrors != null)
            return  (NewClaimValues) createErrorResponse(paramaters, "");

        final Organization billOrg = (Organization) getSession().load(Organization.class,  paramaters.getBillingOrganizationId());
        final Organization serviceOrg = (Organization) getSession().load(Organization.class,  paramaters.getServiceOrganizationId());
        final Organization payToOrg = (Organization) getSession().load(Organization.class,  paramaters.getPayToOrganizationId());
        if (billOrg == null || serviceOrg == null || payToOrg == null)
            return  (NewClaimValues) createErrorResponse(paramaters, "Billing/Service/Pay To Organization ID is invalid.");

        final Invoice invoice = new Invoice();
        invoice.setBillingOrganization(billOrg);
        invoice.setServiceOrganization(serviceOrg);
        invoice.setPayToOrganization(payToOrg);

        final ClaimType claimType = referenceEntityFacade.getClaimType(paramaters.getClaimTypeCode());
        invoice.setType(InvoiceType.Cache.HCFA_1500.getEntity());

        // TODO: Need to figure out the TRANSACTION and INVOICE table some more

        return null;
    }

    public ServiceVersion[] getSupportedServiceVersions()
    {
        return new ServiceVersion[0];
    }

    public String[] isValid(ServiceParameters parameters)
    {

        InvalidValue[] validationMessages = addressValidator.getInvalidValues((AddClaimParameters)parameters);
        String[] errorMessages = new String[validationMessages.length];
        for (int i=0; i < validationMessages.length; i++)
        {
            errorMessages[i] = validationMessages[i].getMessage();
        }
        return errorMessages;
    }

    public ServiceReturnValues createErrorResponse(final ServiceParameters params, final String errorMessage)
    {
        return null;
    }
}
