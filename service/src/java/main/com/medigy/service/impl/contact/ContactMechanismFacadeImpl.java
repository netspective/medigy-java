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

import com.medigy.persist.model.contact.City;
import com.medigy.persist.model.contact.Country;
import com.medigy.persist.model.contact.County;
import com.medigy.persist.model.contact.PostalCode;
import com.medigy.persist.model.contact.Province;
import com.medigy.persist.model.contact.State;
import com.medigy.persist.model.party.ContactMechanism;
import com.medigy.persist.model.party.Party;
import com.medigy.persist.model.party.PartyContactMechanism;
import com.medigy.persist.model.party.PartyContactMechanismPurpose;
import com.medigy.persist.model.party.PhoneNumber;
import com.medigy.persist.model.party.PostalAddress;
import com.medigy.persist.model.party.ElectronicAddress;
import com.medigy.persist.reference.custom.party.ContactMechanismPurposeType;
import com.medigy.persist.reference.type.ContactMechanismType;
import com.medigy.service.contact.ContactMechanismFacade;
import com.medigy.service.util.AbstractFacade;
import com.medigy.service.util.ReferenceEntityFacade;
import com.medigy.service.util.UnknownReferenceTypeException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.SessionFactory;
import org.hibernate.Query;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class ContactMechanismFacadeImpl extends AbstractFacade implements ContactMechanismFacade
{
    private static final Log log = LogFactory.getLog(ContactMechanismFacadeImpl.class);

    private ReferenceEntityFacade  referenceEntityFacade;

    public ContactMechanismFacadeImpl(final SessionFactory sessionFactory, final ReferenceEntityFacade  referenceEntityFacade)
    {
        super(sessionFactory);
        this.referenceEntityFacade = referenceEntityFacade;
    }

    /**
     * Adds a contact mechanism for a party
     *
     * @param cm            the contact mechanism
     * @param party         the party
     * @param purposeType
     * @param purposeDescription
     */
    public void addPartyContactMechanism(final ContactMechanism cm, final Party party, final String purposeType, final String purposeDescription)
    {
        // now create the relationship entry between party and the postal address
        final PartyContactMechanism mech = new PartyContactMechanism();
        mech.setParty(party);

        final PartyContactMechanismPurpose purpose = new PartyContactMechanismPurpose();
        ContactMechanismPurposeType contactMechanismPurposeType = null;
        try
        {
            contactMechanismPurposeType = referenceEntityFacade.getContactMechanismPurposeType(purposeType);
        }
        catch (UnknownReferenceTypeException e)
        {
            // unknown built-in purpose type
            contactMechanismPurposeType = ContactMechanismPurposeType.Cache.OTHER.getEntity();
            log.error(e);
        }
        if (contactMechanismPurposeType.equals(ContactMechanismPurposeType.Cache.OTHER.getEntity()))
            purpose.setDescription(purposeDescription);
        purpose.setType(contactMechanismPurposeType);
        purpose.setPartyContactMechanism(mech);

        mech.addPurpose(purpose);
        cm.addPartyContactMechanism(mech);
        getSession().save(mech);
    }

    public PostalAddress addPostalAddress(final String street1, final String street2, final String cityName,
                                          final String stateCode, final String provinceCode, final String countyName,
                                          final String postalCode, final String countryCode)
    {
        final PostalAddress address = new PostalAddress();
        address.setAddress1(street1);
        address.setAddress2(street2);

        Country country = (Country) getSession().createCriteria(Country.class).add(Restrictions.eq("countryAbbreviation", countryCode).ignoreCase()).uniqueResult();
        address.setCountry(country);

        if (stateCode != null)
        {
            State state = country.getStateByCode(stateCode);
            address.setState(state);

            if (countyName != null && countyName.length() > 0)
            {
                County county =  state.getCountyByName(countyName);
                if (county == null)
                {
                    county = new County();
                    county.setCountyName(countyName);
                    state.addCounty(county);
                    getSession().save(county);
                }
                address.setCounty(county);
            }
            // now add the city
            City city = state.getCityByName(cityName);
            if (city == null)
            {
                city = new City();
                city.setCityName(cityName);
                state.addCity(city);
                getSession().save(city);
            }
            address.setCity(city);

            PostalCode zip = state.getPostalCodeByValue(postalCode);
            if (zip == null)
            {
                zip = new PostalCode();
                zip.setCodeValue(postalCode);
                state.addPostalCode(zip);
                getSession().save(zip);
            }
            address.setPostalCode(zip);
        }
        else if (provinceCode != null)
        {
            Province province = country.getProvinceByName(provinceCode);
            address.setProvince(province);
            City city = province.getCityByName(cityName);
            if (city == null)
            {
                city = new City();
                city.setCityName(cityName);
                province.addCity(city);
                getSession().save(city);
            }
            address.setCity(city);
        }
        getSession().save(address);

        return address;
    }

    public PhoneNumber addPhone(final String countryCode,  final String areaCode, final String number,
                                final String extension)
    {
        final PhoneNumber phone = new PhoneNumber();
        phone.setCountryCode(countryCode);
        phone.setNumberValue(number);
        phone.setAreaCode(areaCode);
        phone.setExtension(extension);
        getSession().save(phone);
        return phone;
    }

    public Country getCountry(final String countryName)
    {
        return (Country) getSession().createCriteria(Country.class).add(Restrictions.eq("countryName", countryName)).uniqueResult();
    }

    public State getState(final String stateName)
    {
        return (State) getSession().createCriteria(State.class).add(Restrictions.eq("stateName", stateName)).uniqueResult();
    }

    public ContactMechanism getContactMechanismById(final Serializable id)
    {
        return (ContactMechanism) getSession().load(ContactMechanism.class, id);
    }

    public List<PostalAddress> listPostalAddresses(final Long partyId)
    {
        final ContactMechanismType postalAddressType = ContactMechanismType.Cache.POSTAL_ADDRESS.getEntity();
        final Query query = getSession().createQuery("select address from PostalAddress address, PartyContactMechanism pcm where " +
                "address.id = pcm.contactMechanism.id and " +
                "pcm.contactMechanism.type.code = ? and " +
                "pcm.party.id = ?");
        query.setString(0, postalAddressType.getCode());
        query.setLong(1, partyId);
        List list = query.list();

        List<PostalAddress> addresses = new ArrayList<PostalAddress>(list.size());
        this.convert(PostalAddress.class, list, addresses);
        return addresses;
    }

    public List<PhoneNumber> listPhoneNumbers(final Long partyId)
    {
        final ContactMechanismType phoneType = ContactMechanismType.Cache.PHONE.getEntity();
        final Query query = getSession().createQuery("select phone from PhoneNumber phone, PartyContactMechanism pcm where " +
                "phone.id = pcm.contactMechanism.id and " +
                "pcm.contactMechanism.type.code = ? and " +
                "pcm.party.id = ?");
        query.setString(0, phoneType.getCode());
        query.setLong(1, partyId);
        List list = query.list();

        List<PhoneNumber> phoneList = new ArrayList<PhoneNumber>(list.size());
        this.convert(PhoneNumber.class, list, phoneList);
        return phoneList;
    }

    public List<ElectronicAddress> listEmails(final Long partyId)
    {
        final ContactMechanismType emailType = ContactMechanismType.Cache.EMAIL_ADDRESS.getEntity();
        final Query query = getSession().createQuery("select address from ElectronicAddress address, PartyContactMechanism pcm where " +
                "address.id = pcm.contactMechanism.id and " +
                "pcm.contactMechanism.type.code = ? and " +
                "pcm.party.id = ?");
        query.setString(0, emailType.getCode());
        query.setLong(1, partyId);
        List list = query.list();

        List<ElectronicAddress> emailList = new ArrayList<ElectronicAddress>(list.size());
        this.convert(ElectronicAddress.class, list, emailList);
        return emailList;
    }

}
