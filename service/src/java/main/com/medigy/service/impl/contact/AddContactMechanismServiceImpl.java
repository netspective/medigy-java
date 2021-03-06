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
package com.medigy.service.impl.contact;

import com.medigy.persist.model.party.ContactMechanism;
import com.medigy.persist.model.party.ElectronicAddress;
import com.medigy.persist.model.party.Party;
import com.medigy.persist.model.party.PhoneNumber;
import com.medigy.persist.model.party.PostalAddress;
import com.medigy.persist.util.HibernateUtil;
import com.medigy.service.AbstractService;
import com.medigy.service.ServiceVersion;
import com.medigy.service.contact.AddContactMechanismService;
import com.medigy.service.contact.ContactMechanismFacade;
import com.medigy.service.dto.ServiceParameters;
import com.medigy.service.dto.party.AddEmailParameters;
import com.medigy.service.dto.party.AddPhoneParameters;
import com.medigy.service.dto.party.AddPostalAddressParameters;
import com.medigy.service.dto.party.NewEmail;
import com.medigy.service.dto.party.NewPhone;
import com.medigy.service.dto.party.NewPostalAddress;
import com.medigy.service.dto.party.PostalAddressParameters;
import com.medigy.service.util.UnknownReferenceTypeException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;
import org.hibernate.SessionFactory;

import java.io.Serializable;

public class AddContactMechanismServiceImpl extends AbstractService implements AddContactMechanismService
{
    private static final Log log = LogFactory.getLog(AddContactMechanismServiceImpl.class);
    private static final ClassValidator postalAddressValidator = new ClassValidator(AddPostalAddressParameters.class);


    public AddContactMechanismServiceImpl(final SessionFactory sessionFactory, final ContactMechanismFacade contactMechanismFacade)
    {
        super(sessionFactory);
        this.contactMechanismFacade = contactMechanismFacade;
    }

    private ContactMechanismFacade contactMechanismFacade;

    public ContactMechanismFacade getContactMechanismFacade()
    {
        return contactMechanismFacade;
    }

    public void setContactMechanismFacade(final ContactMechanismFacade contactMechanismFacade)
    {
        this.contactMechanismFacade = contactMechanismFacade;
    }

    public ServiceVersion[] getSupportedServiceVersions()
    {
        return new ServiceVersion[0];
    }

    public String[] isValid(ServiceParameters parameters)
    {
        if (parameters instanceof AddPostalAddressParameters)
        {
            return validatePostalAddress((AddPostalAddressParameters) parameters);
        }
        else if (parameters instanceof AddEmailParameters)
        {
            return validateEmail((AddEmailParameters) parameters);
        }
        else if (parameters instanceof AddPhoneParameters)
        {
            return validatePhone((AddPhoneParameters) parameters);
        }
        return null;
    }

    public NewPostalAddress createErrorResponse(final ServiceParameters params, final String errorMessage)
    {
        return new NewPostalAddress() {

                public Serializable getPostalAddressId()
                {
                    return null;
                }

                public AddPostalAddressParameters getParameters()
                {
                    return (AddPostalAddressParameters) params;
                }

                public String getErrorMessage()
                {
                    return errorMessage;
                }
            };
    }

    public String[] validatePostalAddress(final AddPostalAddressParameters addParams)
    {
        InvalidValue[] validationMessages = postalAddressValidator.getInvalidValues(addParams);
        String[] errorMessages = new String[validationMessages.length];
        for (int i=0; i < validationMessages.length; i++)
        {
            errorMessages[i] = validationMessages[i].getMessage();
        }
        return errorMessages;
    }

    public String[] validateEmail(final AddEmailParameters param)
    {
        // TODO: Add email validation rules
        return null;
    }

    public String[] validatePhone(final AddPhoneParameters param)
    {
        // TODO: Add phone validation rules
        return null;
    }


    public NewPostalAddress addPostalAddress(final AddPostalAddressParameters addParams)
    {
        final Party party = (Party) getSession().load(Party.class, addParams.getPartyId());
        final PostalAddressParameters param = addParams.getPostalAddressParameters();

        final PostalAddress address = contactMechanismFacade.addPostalAddress(param.getStreet1(), param.getStreet2(),
            param.getCity(), param.getState(), param.getProvince(), param.getCounty(),  param.getPostalCode(), param.getCountry());

        // NOTE: using the PostalAddress as the contact mechanism introduces multiple
        // PartyContactMechanisms so don't use the PostalAddress, instead use ContactMechanism
        try
        {
            //ContactMechanism cm = contactMechanismFacade.getContactMechanismById(address.getContactMechanismId());
            contactMechanismFacade.addPartyContactMechanism(address, party, param.getPurposeType(), param.getPurposeDescription());
        }
        catch (Exception e)
        {
            final String error = e.getMessage();
            return createErrorResponse(addParams, error);
        }

        return new NewPostalAddress()
        {
            public Serializable getPostalAddressId()
            {
                return address.getContactMechanismId();
            }

            public AddPostalAddressParameters getParameters()
            {
                return addParams;
            }

            public String getErrorMessage()
            {
                return null;
            }
        };
    }

    public NewEmail addEmail(final AddEmailParameters param)
    {
        final Party party = (Party) HibernateUtil.getSession().load(Party.class, param.getPartyId());
        final ElectronicAddress email = new ElectronicAddress();
        email.setElectronicAddress(param.getEmail());
        HibernateUtil.getSession().save(email);

        final ContactMechanism cm = (ContactMechanism) HibernateUtil.getSession().load(ContactMechanism.class,
                email.getContactMechanismId());
        try
        {
            contactMechanismFacade.addPartyContactMechanism(cm, party, param.getPurposeType(), param.getPurposeDescription());
        }
        catch (UnknownReferenceTypeException e)
        {
            final String error = e.getMessage();
            return new NewEmail() {
                public Serializable getEmailId()
                {
                    return null;
                }

                public String getErrorMessage()
                {
                    return error;
                }

                public ServiceParameters getParameters()
                {
                    return null;
                }
            } ;
        }

        return new NewEmail() {
            public Serializable getEmailId()
            {
                return cm.getContactMechanismId();
            }

            public String getErrorMessage()
            {
                return null;
            }

            public ServiceParameters getParameters()
            {
                return param;
            }
        } ;
    }

    public NewPhone addPhone(final AddPhoneParameters param)
    {
        final Party party = (Party) HibernateUtil.getSession().load(Party.class, param.getPartyId());
        final PhoneNumber phone = new PhoneNumber();
        phone.setCountryCode(param.getPhoneParameters().getCountryCode());
        phone.setNumberValue(param.getPhoneParameters().getNumber());
        phone.setAreaCode(param.getPhoneParameters().getAreaCode());
        phone.setExtension(param.getPhoneParameters().getExtension());
        HibernateUtil.getSession().save(phone);

        final ContactMechanism cm = (ContactMechanism) HibernateUtil.getSession().load(ContactMechanism.class,
                phone.getContactMechanismId());
        try
        {
            contactMechanismFacade.addPartyContactMechanism(cm, party, param.getPhoneParameters().getPurposeCode(),
                    param.getPhoneParameters().getPurposeDescription());
        }
        catch (UnknownReferenceTypeException e)
        {
            final String error = e.getMessage();
            return new NewPhone() {
                public Serializable getPhoneId()
                {
                    return null;
                }

                public String getErrorMessage()
                {
                    return error;
                }

                public ServiceParameters getParameters()
                {
                    return null;
                }
            };
        }
        return new NewPhone() {
            public Serializable getPhoneId()
            {
                return cm.getContactMechanismId();
            }

            public String getErrorMessage()
            {
                return null;
            }

            public ServiceParameters getParameters()
            {
                return param;
            }
        };
    }
}
