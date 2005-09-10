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
package com.medigy.persist.model.contact;

import com.medigy.persist.TestCase;
import com.medigy.persist.model.party.PartyContactMechanism;
import com.medigy.persist.model.party.PhoneNumber;
import com.medigy.persist.model.party.PostalAddress;
import com.medigy.persist.model.person.Person;
import com.medigy.persist.reference.custom.party.ContactMechanismPurposeType;
import com.medigy.persist.reference.custom.person.EthnicityType;
import com.medigy.persist.reference.type.GenderType;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class TestContactMechanism extends TestCase
{
    public void testPhoneNumber()
    {
        final Calendar cal = new GregorianCalendar();
        cal.set(1970, 1, 1);

        final Session session = openSession();
        final Transaction transaction = session.beginTransaction();

        final PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setAreaCode("703");
        phoneNumber.setNumberValue("1234567");

        final PhoneNumber phoneNumber2 = new PhoneNumber();
        phoneNumber2.setAreaCode("703");
        phoneNumber2.setNumberValue("1234567");

        final Country country = new Country();
        country.setCountryName("Serenity");
        country.setIsoThreeDigitCode("123");
        country.setIsoThreeLetterCode("ABC");
        country.setIsoTwoLetterCode("AB");

        final State state = new State();
        state.setStateName("Firefly");
        state.setStateAbbreviation("FF");
        final PostalCode zip = new PostalCode();
        zip.setCodeValue("00000");
        zip.setParentState(state);
        state.addPostalCode(zip);
        state.setParentCountry(country);
        country.addState(state);

        final City city = new City();
        city.setCityName("Acme City");
        city.setParentState(state);
        state.addCity(city);
        session.save(country);

        final PostalAddress address = new PostalAddress();
        address.setAddress1("123 Acme Street");
        address.setCity(city);
        address.setState(state);
        address.setPostalCode(zip);

        final Person johnDoe = new Person();
        johnDoe.setLastName("Doe");
        johnDoe.setFirstName("John");
        johnDoe.addGender(GenderType.Cache.MALE.getEntity());
        johnDoe.setBirthDate(cal.getTime());
        johnDoe.addPartyContactMechanism(phoneNumber, ContactMechanismPurposeType.Cache.HOME_PHONE.getEntity());
        johnDoe.addPartyContactMechanism(address, ContactMechanismPurposeType.Cache.HOME_ADDRESS.getEntity());
        johnDoe.addEthnicity(EthnicityType.Cache.CAUCASIAN.getEntity());
        johnDoe.addEthnicity(EthnicityType.Cache.NATIVE_AMERICAN.getEntity());
        johnDoe.addEthnicity(EthnicityType.Cache.HISPANIC.getEntity());


        final Person ryanHackett = new Person();
        ryanHackett.setLastName("Hackett");
        ryanHackett.setFirstName("Ryan");
        ryanHackett.addGender(GenderType.Cache.MALE.getEntity());
        ryanHackett.setBirthDate(cal.getTime());
        ryanHackett.addPartyContactMechanism(phoneNumber2, ContactMechanismPurposeType.Cache.HOME_PHONE.getEntity());

        session.save(phoneNumber);
        session.save(phoneNumber2);
        session.save(address);
        session.save(johnDoe);
        session.save(ryanHackett);
        transaction.commit();
        session.close();

        final Session session2 = openSession();
        final Query query = session2.createQuery("from Person person " +
                "left join fetch person.personIdentifiers " +
                "left join fetch person.ethnicities " +
                "left join fetch person.insurancePolicies " +
                "left join fetch person.maritalStatuses " +
                "left join fetch person.partyContactMechanisms as contacts " +
                "left join fetch contacts.purposes " +
                "WHERE person.partyId = :patientId");
        query.setLong("patientId", johnDoe.getPartyId());
        final Person patient = (Person) query.uniqueResult();
        session.close();

        assertThat(patient.getPartyContactMechanisms().size(), eq(2));
        final List<PartyContactMechanism> phoneNumbers = patient.getPhoneNumbers();
        final List<PartyContactMechanism> addresses = patient.getAddresses();

        assertThat(phoneNumbers.size(), eq(1));
        assertThat(addresses.size(), eq(1));
        assertThat(phoneNumbers.get(0).getPurposes().size(), eq(1));
        assertThat(addresses.get(0).getPurposes().size(), eq(1));

    }
}
