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
package com.medigy.service.person;

import com.medigy.persist.model.contact.State;
import com.medigy.persist.model.person.Person;
import com.medigy.persist.reference.custom.person.EthnicityType;
import com.medigy.persist.reference.type.GenderType;
import com.medigy.persist.reference.type.LanguageType;
import com.medigy.persist.reference.type.MaritalStatusType;
import com.medigy.persist.util.HibernateUtil;
import com.medigy.service.TestCase;
import com.medigy.service.dto.person.PersonParameters;

import java.util.Calendar;
import java.util.Date;

public class TestPersonFacade extends TestCase
{

    public void testListPersonByLastName() throws Exception
    {
        final Calendar cal = Calendar.getInstance();
        cal.set(1980, 1, 1);

        Person personA = new Person();
        personA.setLastName("Doe");
        personA.setFirstName("John");
        personA.setMiddleName("D");
        personA.setBirthDate(cal.getTime());
        personA.addGender(GenderType.Cache.MALE.getEntity());
        HibernateUtil.getSession().save(personA);

        Person personB = new Person();
        personB.setLastName("Hackett");
        personB.setFirstName("Brian");
        personB.setBirthDate(cal.getTime());
        personB.addGender(GenderType.Cache.MALE.getEntity());
        HibernateUtil.getSession().save(personB);
        HibernateUtil.closeSession();

        final PersonFacade personFacade = (PersonFacade) getRegistry().getService(PersonFacade.class);

        Person[] personList = personFacade.listPersonByLastName("d%", false);
        assertNotNull(personList);
        assertEquals(personList.length, 1);
        assertEquals(personList[0].getFirstName(), "John");
        assertEquals(personList[0].getLastName(), "Doe");
        assertEquals(personList[0].getMiddleName(), "D");

        personList = personFacade.listPersonByLastName("Doe", true);
        assertNotNull(personList);
        assertEquals(personList.length, 1);
        assertEquals(personList[0].getFirstName(), "John");
        assertEquals(personList[0].getLastName(), "Doe");
        assertEquals(personList[0].getMiddleName(), "D");

        personList = personFacade.listPersonByLastName("%", false);
        assertNotNull(personList);
        assertEquals(personList.length, 2);
        assertEquals(personList[0].getFirstName(), "John");
        assertEquals(personList[0].getLastName(), "Doe");
        assertEquals(personList[0].getMiddleName(), "D");
        assertEquals(personList[1].getFirstName(), "Brian");
        assertEquals(personList[1].getLastName(), "Hackett");
    }

    public void testCreatePerson()  throws Exception
    {
        final Calendar cal = Calendar.getInstance();
        cal.set(1980, 1, 1);
        final PersonFacade personFacade = (PersonFacade) getRegistry().getService(PersonFacade.class);
        final Person person = personFacade.createPerson(new PersonParameters() {
            public String getFirstName()
            {
                return "Ryan";
            }

            public String getLastName()
            {
                return "Hackett";
            }

            public String getMiddleName()
            {
                return "P";
            }

            public String getSuffix()
            {
                return "Sr.";
            }

            public Date getBirthDate()
            {
                return cal.getTime();
            }

            public String getGenderCode()
            {
                return GenderType.Cache.MALE.getEntity().getCode();
            }

            public String getMaritalStatusCode()
            {
                return MaritalStatusType.Cache.SINGLE.getEntity().getCode();
            }

            public String getSsn()
            {
                return "123456789";
            }

            public String getDriversLicenseNumber()
            {
                return "000000000";
            }

            public String getDriversLicenseStateCode()
            {
                return State.Cache.VIRGINIA.getEntity().getStateAbbreviation();
            }

            public String getEmployerName()
            {
                return null;
            }

            public String getEmployerId()
            {
                return null;
            }

            public String getOccupation()
            {
                return null;
            }

            public String[] getEthnicityCodes()
            {
                return new String[] { EthnicityType.Cache.AFRICAN_AMERICAN.getEntity().getCode() };
            }

            public String[] getLanguageCodes()
            {
                return new String[] { LanguageType.Cache.ENGLISH.getEntity().getCode()};
            }
        });

        assertThat(person, NOT_NULL);
        assertThat(person.getLastName(), eq("Hackett"));
        assertThat(person.getFirstName(), eq("Ryan"));
        assertThat(person.getMiddleName(), eq("P"));
        cal.setTime(person.getBirthDate());
        assertThat(cal.get(Calendar.YEAR), eq(1980));
        //assertThat(person.getSsn(), eq("123456789"));
        //assertThat(person.getDriversLicenseNumber(), eq("000000000"));
        assertThat(person.hasEthnicity(EthnicityType.Cache.AFRICAN_AMERICAN.getEntity()), eq(true));
        assertThat(person.speaksLanguage(LanguageType.Cache.ENGLISH.getEntity()), eq(true));
    }
}
