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
 * @author Shahid N. Shah
 */

/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.model.person;

import com.medigy.persist.TestCase;
import com.medigy.persist.reference.custom.person.EthnicityType;
import com.medigy.persist.reference.type.GenderType;
import com.medigy.persist.reference.type.LanguageType;
import com.medigy.persist.reference.type.MaritalStatusType;
import com.medigy.persist.util.HibernateUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Calendar;
import java.util.Date;

public class TestPerson extends TestCase
{
    private static final Log log = LogFactory.getLog(TestPerson.class);

    public void testPerson()
    {
        final Calendar calendar = Calendar.getInstance();
        HibernateUtil.beginTransaction();

        Person newPerson = new Person();
        newPerson.setFirstName("Ryan");
        newPerson.setMiddleName("Bluegrass");
        newPerson.setLastName("Hackett");

        calendar.set(1990, 6, 14);
        newPerson.addMaritalStatus(MaritalStatusType.Cache.SINGLE.getEntity(), calendar.getTime(), new Date());
        newPerson.addMaritalStatus(MaritalStatusType.Cache.MARRIED.getEntity(), new Date(), null);
        newPerson.addGender(GenderType.Cache.MALE.getEntity());

        newPerson.setBirthDate(calendar.getTime());
        newPerson.addEthnicity(EthnicityType.Cache.CAUCASIAN.getEntity());
        newPerson.addEthnicity(EthnicityType.Cache.NATIVE_AMERICAN.getEntity());

        // Add languages
        newPerson.addLanguage(LanguageType.Cache.ENGLISH.getEntity(), true);
        newPerson.addLanguage(LanguageType.Cache.SPANISH.getEntity(), false);
        newPerson.setSsn("000-00-0000");

        HibernateUtil.getSession().save(newPerson);
        HibernateUtil.commitTransaction();
        HibernateUtil.closeSession();

        final Person persistedPerson = (Person) HibernateUtil.getSession().load(Person.class, newPerson.getPersonId());
        assertThat(persistedPerson.getFirstName(), eq("Ryan"));
        assertThat(persistedPerson.getMiddleName(), eq("Bluegrass"));
        assertThat(persistedPerson.getLastName(), eq("Hackett"));
        assertThat(persistedPerson.getPartyName(), eq("Ryan Bluegrass Hackett"));

        // verify the ethnicites
        assertThat(persistedPerson.getEthnicities().size(), eq(2));
        assertThat(persistedPerson.hasEthnicity(EthnicityType.Cache.CAUCASIAN.getEntity()), eq(true));
        assertThat(persistedPerson.hasEthnicity(EthnicityType.Cache.NATIVE_AMERICAN.getEntity()), eq(true));
    }
}
