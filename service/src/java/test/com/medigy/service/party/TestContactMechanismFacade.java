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

import com.medigy.persist.model.party.PartyContactMechanism;
import com.medigy.persist.model.party.PhoneNumber;
import com.medigy.persist.model.party.PostalAddress;
import com.medigy.persist.model.person.Person;
import com.medigy.persist.model.contact.Country;
import com.medigy.persist.model.contact.State;
import com.medigy.persist.reference.custom.party.ContactMechanismPurposeType;
import com.medigy.persist.reference.type.GenderType;
import com.medigy.persist.util.HibernateUtil;
import com.medigy.service.TestCase;
import com.medigy.service.contact.ContactMechanismFacade;

import java.util.List;
import java.util.Calendar;

public class TestContactMechanismFacade extends TestCase
{
    public void testAddPartyContactMechanism() throws Exception
    {
        HibernateUtil.beginTransaction();
        Calendar cal = Calendar.getInstance();
        cal.set(1970, 1, 1);

        final Person person = new Person();
        person.setLastName("Hackett");
        person.setFirstName("Josh");
        person.addGender(GenderType.Cache.MALE.getEntity());
        person.setBirthDate(cal.getTime());
        HibernateUtil.getSession().save(person);

        final PhoneNumber phone = new PhoneNumber();
        phone.setCountryCode("123");
        phone.setNumber("1234567");
        phone.setAreaCode("703");
        phone.setExtension(null);
        HibernateUtil.getSession().save(phone);

        ContactMechanismFacade facade = (ContactMechanismFacade) getRegistry().getService(ContactMechanismFacade.class);
        facade.addPartyContactMechanism(phone, person, ContactMechanismPurposeType.Cache.HOME_PHONE.getEntity().getCode(), null);
        facade.addPartyContactMechanism(phone, person, ContactMechanismPurposeType.Cache.OTHER.getEntity().getCode(), "The Bat Phone");
        HibernateUtil.commitTransaction();

        List contactMechList = HibernateUtil.getSession().createCriteria(PartyContactMechanism.class).list();
        assertThat(contactMechList.size(), eq(2));

        PartyContactMechanism mech = (PartyContactMechanism) contactMechList.toArray()[0];
        assertThat(mech.hasPurpose(ContactMechanismPurposeType.Cache.HOME_PHONE.getEntity()), eq(true));

        mech =  (PartyContactMechanism) contactMechList.toArray()[1];
        assertThat(mech.hasPurpose(ContactMechanismPurposeType.Cache.OTHER.getEntity()), eq(true));
        assertThat(mech.getPurpose(ContactMechanismPurposeType.Cache.OTHER.getEntity()).getDescription(), eq("The Bat Phone"));

    }

    public void testAddPostalAddress()
    {
        final Country country = new Country();
        country.setCountryName("United States of America");
        country.setCountryAbbreviation("USA");

        final State virginia = new State();
        virginia.setStateName("Virginia");
        virginia.setStateAbbreviation("VA");
        country.addState(virginia);

        HibernateUtil.getSession().save(country);
        HibernateUtil.closeSession();

        ContactMechanismFacade facade = (ContactMechanismFacade) getRegistry().getService(ContactMechanismFacade.class);
        final PostalAddress postalAddress = facade.addPostalAddress("123 Acme Road", null, "Fairfax", "VA", null, "Fairfax County", "22033", "USA");

        assertThat(postalAddress, NOT_NULL);
        assertThat(postalAddress.getAddressBoundaries().size(), eq(5));
        assertThat(postalAddress.getState(), eq(virginia));
        assertThat(postalAddress.getAddress1(), eq("123 Acme Road"));
        assertThat(postalAddress.getCity().getCityName(), eq("Fairfax"));
        assertThat(postalAddress.getCounty().getCountyName(), eq("Fairfax County"));
        assertThat(postalAddress.getPostalCode().getCodeValue(), eq("22033"));

    }
}
