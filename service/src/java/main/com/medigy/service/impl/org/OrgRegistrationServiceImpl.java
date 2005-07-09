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
package com.medigy.service.impl.org;

import com.medigy.persist.model.org.Organization;
import com.medigy.persist.model.party.ContactMechanism;
import com.medigy.persist.model.party.PhoneNumber;
import com.medigy.persist.model.party.PostalAddress;
import com.medigy.persist.model.person.Person;
import com.medigy.service.AbstractService;
import com.medigy.service.ServiceVersion;
import com.medigy.service.contact.ContactMechanismFacade;
import com.medigy.service.dto.ServiceParameters;
import com.medigy.service.dto.org.AddOrganization;
import com.medigy.service.dto.org.RegisteredOrg;
import com.medigy.service.dto.party.PostalAddressParameters;
import com.medigy.service.dto.person.RegisterPatientParameters;
import com.medigy.service.insurance.InsurancePolicyFacade;
import com.medigy.service.org.OrgRegistrationService;
import com.medigy.service.person.PersonFacade;
import com.medigy.service.util.ReferenceEntityFacade;
import com.medigy.service.util.UnknownReferenceTypeException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.validator.InvalidStateException;
import org.hibernate.validator.InvalidValue;
import org.hibernate.validator.NotNull;

import java.io.Serializable;

/**
 * Service class for registering a new patient
 */
public class OrgRegistrationServiceImpl extends AbstractService implements OrgRegistrationService
{
    private static final Log log = LogFactory.getLog(OrgRegistrationServiceImpl.class);
    private boolean createEmployerIfUnknown = true;
    private boolean createInsuranceProviderIfUnknown = true;


    private ReferenceEntityFacade referenceEntityFacade;
    private PersonFacade personFacade;
    private ContactMechanismFacade contactMechanismFacade;
    private InsurancePolicyFacade insurancePolicyFacade;

    public OrgRegistrationServiceImpl(final SessionFactory sessionFactory, final ReferenceEntityFacade referenceEntityFacade,
                                          final PersonFacade personFacade, final ContactMechanismFacade contactMechanismFacade,
                                          final InsurancePolicyFacade insurancePolicyFacade)
    {
        super(sessionFactory);
        this.referenceEntityFacade = referenceEntityFacade;
        this.personFacade = personFacade;
        this.contactMechanismFacade = contactMechanismFacade;
        this.insurancePolicyFacade = insurancePolicyFacade;
    }

    public ServiceVersion[] getSupportedServiceVersions()
    {
        return new ServiceVersion[0];
    }

    /**
     * Check to see if Employer organization should be automatically created if it is not known (no ID).
     * @return
     */
    public boolean isCreateEmployerIfUnknown()
    {
        return createEmployerIfUnknown;
    }

    public void setCreateEmployerIfUnknown(boolean createEmployerIfUnknown)
    {
        this.createEmployerIfUnknown = createEmployerIfUnknown;
    }

    /**
     * Check to see if Insurance Provider organization should be automatically created if it is not known (no ID)
     * @return
     */
    public boolean isCreateInsuranceProviderIfUnknown()
    {
        return createInsuranceProviderIfUnknown;
    }

    public void setCreateInsuranceProviderIfUnknown(boolean createInsuranceProviderIfUnknown)
    {
        this.createInsuranceProviderIfUnknown = createInsuranceProviderIfUnknown;
    }

    private static Serializable getPrimaryKey(String keyString)
    {
        return Long.parseLong(keyString);
    }

    protected void registerPostalAddress(final Organization org, final AddOrganization orgParameters) throws UnknownReferenceTypeException
    {
        final PostalAddressParameters param = new PostalAddressParameters()
            {
                public String getStreet1()
                {
                    return orgParameters.getStreet1();
                }

                public String getStreet2()
                {
                    return  orgParameters.getStreet2();
                }

                public String getCity()
                {
                    return  orgParameters.getCity();
                }

                public String getState()
                {
                    return orgParameters.getState();
                }

                public String getProvince()
                {
                    return orgParameters.getProvince();
                }

                public String getPostalCode()
                {
                    return orgParameters.getPostalCode();
                }

                public String getCounty()
                {
                    return orgParameters.getCounty();
                }

                public String getCountry()
                {
                    return orgParameters.getCountry();
                }

                public String getPurposeType()
                {
                    return orgParameters.getPostalAddressPurposeType();
                }

                public String getPurposeDescription()
                {
                    return orgParameters.getPostalAddressPurposeDescription();
                }
            };
        final PostalAddress address = contactMechanismFacade.addPostalAddress(param.getStreet1(), param.getStreet2(),
            param.getCity(), param.getState(), param.getProvince(), param.getCounty(),  param.getPostalCode(), param.getCountry());

        // NOTE: using the PostalAddress as the contact mechanism introduces multiple
        // PartyContactMechanisms so don't use the PostalAddress, instead use ContactMechanism
        ContactMechanism cm = (ContactMechanism) getSession().load(ContactMechanism.class, address.getContactMechanismId());
        contactMechanismFacade.addPartyContactMechanism(cm, org, param.getPurposeType(), param.getPurposeDescription());

    }

    public RegisteredOrg registerOrganization(final AddOrganization params, String mode)
    {
        Organization org = null;
        if("insert".equals(mode))
            org = Organization.createNewOrganization();
        else if("update".equals(mode))
            org = (Organization)getSession().get(Organization.class, params.getOrgId());
        else if("delete".equals(mode))
        {
            org = (Organization)getSession().get(Organization.class, params.getOrgId());
            getSession().delete(org);
        }

        try
        {
            BeanUtils.copyProperties(org, params);

            getSession().save(org);
            // TODO: Temporary. uncomment when Country/State data are in
            //registerPostalAddress(org, params);

            final RegisteredOrg organization = new RegisteredOrg()
            {
                public Serializable getOrgId()
                {
                    return null;  //To change body of implemented methods use File | Settings | File Templates.
                }

                public AddOrganization getOrganizationParameters()
                {
                    return null;  //To change body of implemented methods use File | Settings | File Templates.
                }

                public String getErrorMessage()
                {
                    return null;
                }
            };
            if (log.isInfoEnabled())
                log.info("New ORGANIZATION created with id = " + org.getOrgId());
           return organization;
        }
        catch (InvalidStateException e)
        {
            log.error(e);
            for (InvalidValue value : e.getInvalidValues())
                log.error("\t" + value.getPropertyName() + " " + value.getMessage());
            return createErrorResponse(params, e.getMessage());
        }
        catch (final Exception e)
        {
            log.error(ExceptionUtils.getStackTrace(e));
            return createErrorResponse(params, e.getMessage());
        }
    }

    public AddOrganization getOrganizationParameters()
    {
        return new AddOrganization()
        {
            public Long getOrgId()
            {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @NotNull public String getName()
            {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public String getParentOrganizationName()
            {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public Serializable getParentOrganizationId()
            {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @NotNull public PostalAddressParameters getMailingAddress()
            {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public String getEmail()
            {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @NotNull public String getStreet1()
            {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public String getStreet2()
            {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @NotNull public String getCity()
            {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @NotNull public String getState()
            {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public String getProvince()
            {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @NotNull public String getPostalCode()
            {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public String getCounty()
            {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @NotNull public String getCountry()
            {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public String getPostalAddressPurposeType()
            {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public String getPostalAddressPurposeDescription()
            {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }


            public String getPhone()
            {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public String getFax()
            {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public String getWebsiteUrl()
            {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public ServiceVersion getServiceVersion()
            {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public String getErrorMessage()
            {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }
        };
    }

    private PhoneNumber registerHomePhone(final Person person, final RegisterPatientParameters params)
    {
        return contactMechanismFacade.addPhone(null, null, params.getHomePhone());
    }

    // TODO: Put a validator and return a list of errors/warnings
    public String[] isValid(ServiceParameters params)
    {
        AddOrganization orgParameters = (AddOrganization) params;
        assert  orgParameters.getName() != null :
            "Org name cannot be null.";

        return null;
    }

    public RegisteredOrg createErrorResponse(final ServiceParameters params, final String errorMessage)
    {
        return new RegisteredOrg()
        {
            public Serializable getOrgId()
            {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public AddOrganization getOrganizationParameters()
            {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            /**
             * Locale specific error message
             *
             * @return  error message
             */
            public String getErrorMessage()
            {
                return errorMessage;
            }
        };
    }
}
