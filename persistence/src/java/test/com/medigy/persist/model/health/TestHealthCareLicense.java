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
package com.medigy.persist.model.health;

import com.medigy.persist.TestCase;
import com.medigy.persist.model.contact.Country;
import com.medigy.persist.model.contact.State;
import com.medigy.persist.model.person.Person;
import com.medigy.persist.reference.custom.health.HealthCareLicenseType;
import com.medigy.persist.reference.type.GenderType;
import com.medigy.persist.reference.type.LanguageType;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Set;

public class TestHealthCareLicense extends TestCase
{

    public void testHealthCareLicense()
    {
        Session session = openSession();
        Transaction transaction = session.beginTransaction();
        Person doctor = new Person();
        Calendar cal = new GregorianCalendar();
        cal.set(1965, 1, 1);

        doctor.setLastName("Bond");
        doctor.setFirstName("James");
        doctor.setBirthDate(cal.getTime());
        doctor.addGender(GenderType.Cache.MALE.getEntity());
        doctor.addLanguage(LanguageType.Cache.ENGLISH.getEntity());
        session.save(doctor);
        transaction.commit();
        session.close();

        session = openSession();
        transaction = session.beginTransaction();
        final Calendar calendar = Calendar.getInstance();
        final Country country = new Country();
        country.setCountryName("USA");
        country.setIsoTwoLetterCode("US");
        country.setIsoThreeLetterCode("USA");
        country.setIsoThreeDigitCode("123");
        session.save(country);

        final State state = new State("Virginia", "VA");
        country.addState(state);
        session.save(state);

        final Person doctor2 = (Person) session.createCriteria(Person.class).uniqueResult();
        final HealthCareLicense license = new HealthCareLicense();
        final HealthCareLicense license2 = new HealthCareLicense();
        license.setType(HealthCareLicenseType.Cache.BOARD_CERTIFICATION.getEntity());
        license.setLicenseNumber("007");
        calendar.set(3000, 5, 1);
        license.setThroughDate(calendar.getTime());
        license.setState(state);
        doctor2.addLicense(license);
        session.save(license);

        calendar.set(2000, 5, 1);
        license2.setType(HealthCareLicenseType.Cache.OTHER.getEntity());
        license2.setDescription("Voodoo License");
        license2.setLicenseNumber("XXX");
        license2.setThroughDate(calendar.getTime());
        doctor2.addLicense(license2);
        session.save(license2);
        transaction.commit();
        session.close();

        session = openSession();
        final Person savedDoctor = (Person) session.createCriteria(Person.class).uniqueResult();
        assertThat(savedDoctor, NOT_NULL);
        final Set<HealthCareLicense> licenses = savedDoctor.getLicenses();
        assertThat(licenses.size(), eq(2));
        final HealthCareLicense newLicense = savedDoctor.getLicense(HealthCareLicenseType.Cache.BOARD_CERTIFICATION.getEntity());
        assertThat(newLicense.getLicenseId(), eq(license.getLicenseId()));
        assertThat(newLicense.getState().getStateName(), eq("Virginia"));
        assertThat(newLicense.getLicenseNumber(), eq("007"));
        assertThat(newLicense.isExpired(), eq(false));
        final HealthCareLicense otherLicense = savedDoctor.getLicense(HealthCareLicenseType.Cache.OTHER.getEntity());
        assertThat(otherLicense.getLicenseId(), eq(license2.getLicenseId()));
        assertThat(otherLicense.getDescription(), eq("Voodoo License"));
        assertThat(otherLicense.getLicenseNumber(), eq("XXX"));
        assertThat(otherLicense.isExpired(), eq(true));
        session.close();

    }
}
