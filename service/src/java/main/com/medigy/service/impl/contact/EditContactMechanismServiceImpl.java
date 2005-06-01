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

import com.medigy.service.ServiceVersion;
import com.medigy.service.contact.EditContactMechanismService;
import com.medigy.service.contact.ContactMechanismFacade;
import com.medigy.service.dto.contact.EditPostalAddressParameters;
import com.medigy.service.dto.ServiceParameters;
import com.medigy.service.dto.ServiceReturnValues;
import com.medigy.persist.util.HibernateUtil;
import com.medigy.persist.model.party.PostalAddress;
import com.medigy.persist.model.contact.Country;
import com.medigy.persist.model.contact.State;
import com.medigy.persist.model.contact.City;
import com.medigy.persist.model.contact.County;
import com.medigy.persist.model.contact.PostalCode;

public class EditContactMechanismServiceImpl implements EditContactMechanismService
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

    public ServiceVersion[] getSupportedServiceVersions()
    {
        return new ServiceVersion[0];
    }

    public boolean isValid(ServiceParameters parameters)
    {
        if (parameters instanceof EditPostalAddressParameters)
        {
            isPostalAdressParametersValid((EditPostalAddressParameters) parameters);
        }
        return true;
    }

    public ServiceReturnValues createErrorResponse(final ServiceParameters params, final String errorMessage)
    {
        return null;
    }

    protected void isPostalAdressParametersValid(final EditPostalAddressParameters params)
    {
        assert (params.getPostalAddressId() != null) : "The Postal Address ID cannot be null.";

        Country country = getContactMechanismFacade().getCountry(params.getCountry());
        assert (country != null) : "The Country is UNKNOWN";

        State state = country.getStateByName(params.getState());
        assert (state != null) : "The State DOES NOT belong to the selected country";
        assert (params.getStreet1() != null) : "The Street Address Line 1 cannot be empty";
        assert (params.getCity() != null) : "The city name cannot be empty";
    }

    public void editPostalAddress(final EditPostalAddressParameters params)
    {
        HibernateUtil.beginTransaction();
        final PostalAddress address = (PostalAddress) HibernateUtil.getSession().createQuery("from PostalAddress where contactMechanismId = " +
                params.getPostalAddressId()).uniqueResult();

        if (address == null)
        {
            // TODO: Return error message when no matching address is found
            return;
        }

        address.setAddress1(params.getStreet1());
        address.setAddress2(params.getStreet2());

        Country country = getContactMechanismFacade().getCountry(params.getCountry());
        address.setCountry(country);
        State state = country.getStateByName(params.getState());
        address.setState(state);

        PostalCode zip = state.getPostalCodeByValue(params.getPostalCode());
        if (zip == null)
        {
            zip = new PostalCode();
            zip.setCodeValue(params.getPostalCode());
            zip.setParentState(state);
            state.addPostalCode(zip);
        }
        address.setPostalCode(zip);

        if (params.getCounty() != null)
        {
            County county = state.getCountyByName(params.getCounty());
            if (county == null)
            {
                county= new County();
                county.setCountyName(params.getCounty());
                county.setParentState(state);
                state.addCounty(county);
            }
            address.setCounty(county);
        }
        else
        {
            //maybe county name was removed
            address.setCountry(null);
        }

        City city = state.getCityByName(params.getCity());
        if (city == null)
        {
            city = new City();
            city.setCityName(params.getCity());
            city.setParentState(state);
            state.addCity(city);
        }
        address.setCity(city);

        HibernateUtil.getSession().flush();
        HibernateUtil.commitTransaction();
    }
}
