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
package com.medigy.service.party;

import com.medigy.persist.model.contact.Country;
import com.medigy.persist.model.contact.State;
import com.medigy.persist.model.contact.PostalCode;
import com.medigy.persist.model.contact.City;
import com.medigy.persist.model.party.PartyContactMechanism;
import com.medigy.persist.model.party.PhoneNumber;
import com.medigy.persist.model.party.PostalAddress;
import com.medigy.persist.model.party.ElectronicAddress;
import com.medigy.persist.model.person.Person;
import com.medigy.persist.model.org.Organization;
import com.medigy.persist.reference.custom.party.ContactMechanismPurposeType;
import com.medigy.persist.reference.type.GenderType;
import com.medigy.service.AbstractSpringTestCase;
import com.medigy.service.contact.ContactMechanismFacade;

import java.util.Calendar;
import java.util.List;

public class TestContactMechanismFacade extends AbstractSpringTestCase
{
    private ContactMechanismFacade contactMechanismFacade;

    public void setContactMechanismFacade(final ContactMechanismFacade contactMechanismFacade)
    {
        this.contactMechanismFacade = contactMechanismFacade;
    }

    public void testListPhoneNumbers()
    {
        final Organization org = new Organization();
        org.setOrganizationName("Blue Cross");
        getSession().save(org);

        final PhoneNumber phone = new PhoneNumber();
        phone.setAreaCode("703");
        phone.setNumberValue("1234567");

        final PhoneNumber phone2 = new PhoneNumber();
        phone2.setAreaCode("800");
        phone2.setNumberValue("1111111");
        getSession().save(phone);
        getSession().save(phone2);

        final PartyContactMechanism pcm = new PartyContactMechanism();
        pcm.setContactMechanism(phone);
        pcm.setParty(org);
        getSession().save(pcm);

        final PartyContactMechanism pcm2 = new PartyContactMechanism();
        pcm2.setContactMechanism(phone2);
        pcm2.setParty(org);
        getSession().save(pcm2);

        List<PhoneNumber> list = contactMechanismFacade.listPhoneNumbers(org.getPartyId());
        assertEquals(2, list.size());
        assertEquals("703", list.get(0).getAreaCode());
        assertEquals("800", list.get(1).getAreaCode());
        assertEquals("1234567", list.get(0).getNumberValue());  
        assertEquals("1111111", list.get(1).getNumberValue());
    }

    public void testListEmails()
    {
        final Organization org = new Organization();
        org.setOrganizationName("Blue Cross");
        getSession().save(org);

        final ElectronicAddress email = new ElectronicAddress();
        email.setElectronicAddress("test@test.com");

        final ElectronicAddress email2 = new ElectronicAddress();
        email2.setElectronicAddress("test2@test.com");

        getSession().save(email);
        getSession().save(email2);

        final PartyContactMechanism pcm = new PartyContactMechanism();
        pcm.setContactMechanism(email);
        pcm.setParty(org);
        getSession().save(pcm);

        final PartyContactMechanism pcm2 = new PartyContactMechanism();
        pcm2.setContactMechanism(email2);
        pcm2.setParty(org);
        getSession().save(pcm2);

        List<ElectronicAddress> list = contactMechanismFacade.listEmails(org.getPartyId());
        assertEquals(2, list.size());
        assertEquals("test@test.com", list.get(0).getElectronicAddress());
        assertEquals("test2@test.com", list.get(1).getElectronicAddress());
    }

    public void testListPostalAddresses()
    {
        final Organization org = new Organization();
        org.setOrganizationName("Blue Cross");
        getSession().save(org);

        final Country country = new Country();
        country.setCountryName("United States of America");
        country.setIsoThreeLetterCode("USA");
        country.setIsoTwoLetterCode("US");
        country.setIsoThreeDigitCode("123");

        final State virginia = new State();
        virginia.setStateName("Virginia");
        virginia.setStateAbbreviation("VA");
        country.addState(virginia);

        final PostalCode zip = new PostalCode();
        zip.setCodeValue("22033");
        virginia.addPostalCode(zip);

        final City fairfax = new City();
        fairfax.setCityName("Fairfax");
        virginia.addCity(fairfax);

        getSession().save(country);

        final PostalAddress address = new PostalAddress();
        address.setAddress1("123 Acme Road");
        address.setPostalCode(zip);
        address.setState(virginia);
        address.setCountry(country);
        address.setCity(fairfax);
        getSession().save(address);

        final PostalAddress address2 = new PostalAddress();
        address2.setAddress1("333 Penny Road");
        address2.setPostalCode(zip);
        address2.setState(virginia);
        address2.setCountry(country);
        address2.setCity(fairfax);
        getSession().save(address2);

        final PartyContactMechanism pcm = new PartyContactMechanism();
        pcm.setContactMechanism(address);
        pcm.setParty(org);
        getSession().save(pcm);

        final PartyContactMechanism pcm2 = new PartyContactMechanism();
        pcm2.setContactMechanism(address2);
        pcm2.setParty(org);
        getSession().save(pcm2);

        List<PostalAddress> list = contactMechanismFacade.listPostalAddresses(org.getPartyId());
        assertEquals(2, list.size());
    }

    public void testAddPartyContactMechanism() throws Exception
    {
        //ApplicationContext context = new ClassPathXmlApplicationContext("/com/medigy/service/contact/applicationContext.xml");
        //contactMechanismFacade = (ContactMechanismFacade) context.getBean("contactMechanismFacade");
        Calendar cal = Calendar.getInstance();
        cal.set(1970, 1, 1);

        final Person person = new Person();
        person.setLastName("Hackett");
        person.setFirstName("Josh");
        person.addGender(GenderType.Cache.MALE.getEntity());
        person.setBirthDate(cal.getTime());
        getSession().save(person);

        final PhoneNumber phone = new PhoneNumber();
        phone.setCountryCode("123");
        phone.setNumberValue("1234567");
        phone.setAreaCode("703");
        phone.setExtension(null);
        getSession().save(phone);

        contactMechanismFacade.addPartyContactMechanism(phone, person, ContactMechanismPurposeType.Cache.HOME_PHONE.getEntity().getCode(), null);
        contactMechanismFacade.addPartyContactMechanism(phone, person, ContactMechanismPurposeType.Cache.OTHER.getEntity().getCode(), "The Bat Phone");

        List contactMechList = getSession().createCriteria(PartyContactMechanism.class).list();
        assertEquals(contactMechList.size(), 2);

        PartyContactMechanism mech = (PartyContactMechanism) contactMechList.toArray()[0];
        assertTrue(mech.hasPurpose(ContactMechanismPurposeType.Cache.HOME_PHONE.getEntity()));

        mech =  (PartyContactMechanism) contactMechList.toArray()[1];
        assertTrue(mech.hasPurpose(ContactMechanismPurposeType.Cache.OTHER.getEntity()));
        assertEquals(mech.getPurpose(ContactMechanismPurposeType.Cache.OTHER.getEntity()).getDescription(), "The Bat Phone");
        
    }


    public void testAddPostalAddress()
    {
        final Country country = new Country();
        country.setCountryName("United States of America");
        country.setIsoThreeLetterCode("USA");
        country.setIsoTwoLetterCode("US");
        country.setIsoThreeDigitCode("123");

        final State virginia = new State();
        virginia.setStateName("Virginia");
        virginia.setStateAbbreviation("VA");
        country.addState(virginia);

        getSession().save(country);
        //ContactMechanismFacade contactMechanismFacade = (ContactMechanismFacade) getRegistry().getService(ContactMechanismFacade.class);
        final PostalAddress postalAddress = contactMechanismFacade.addPostalAddress("123 Acme Road", null, "Fairfax", "VA", null, "Fairfax County", "22033", "USA");

        assertNotNull(postalAddress);
        assertEquals(postalAddress.getAddressBoundaries().size(), 5);
        assertEquals(postalAddress.getState(), virginia);
        assertEquals(postalAddress.getAddress1(), "123 Acme Road");
        assertEquals(postalAddress.getCity().getCityName(), "Fairfax");
        assertEquals(postalAddress.getCounty().getCountyName(), "Fairfax County");
        assertEquals(postalAddress.getPostalCode().getCodeValue(), "22033");

    }

}
