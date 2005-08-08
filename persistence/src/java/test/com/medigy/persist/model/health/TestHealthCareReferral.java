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
import com.medigy.persist.model.person.Person;
import com.medigy.persist.model.person.PersonRole;
import com.medigy.persist.reference.custom.health.HealthCareReferralType;
import com.medigy.persist.reference.custom.person.PersonRoleType;
import com.medigy.persist.reference.type.GenderType;
import com.medigy.persist.reference.type.LanguageType;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

import java.util.Calendar;
import java.util.Date;

public class TestHealthCareReferral extends TestCase
{
    public void testHealthCareReferral()
    {
        Session session = openSession();
        Transaction transaction = session.beginTransaction();
        Calendar cal = Calendar.getInstance();
        cal.set(1965, 1, 1);
        final Person patient = Person.createNewPatient();
        patient.setLastName("Hackett");
        patient.setFirstName("Ryan");
        patient.setBirthDate(cal.getTime());
        patient.addGender(GenderType.Cache.MALE.getEntity());
        patient.addLanguage(LanguageType.Cache.ENGLISH.getEntity());

        final PersonRole patientRole = new PersonRole();
        patientRole.setType(PersonRoleType.Cache.PATIENT.getEntity());
        patientRole.setPerson(patient);
        patient.addRole(patientRole);

        final Person requestorDoctor = Person.createNewPhysician();
        requestorDoctor.setLastName("Doctor");
        requestorDoctor.setFirstName("Requestor");
        requestorDoctor.setBirthDate(cal.getTime());
        requestorDoctor.addGender(GenderType.Cache.MALE.getEntity());
        requestorDoctor.addLanguage(LanguageType.Cache.ENGLISH.getEntity());

        final PersonRole requestorRole = new PersonRole();
        requestorRole.setType(PersonRoleType.Cache.INDIVIDUAL_HEALTH_CARE_PRACTITIONER.getEntity());
        requestorRole.setPerson(requestorDoctor);
        requestorDoctor.addRole(requestorRole);

        final Person provider = Person.createNewPhysician();
        provider.setLastName("Doctor");
        provider.setFirstName("Provider");
        provider.setBirthDate(cal.getTime());
        provider.addGender(GenderType.Cache.MALE.getEntity());
        provider.addLanguage(LanguageType.Cache.ENGLISH.getEntity());

        final PersonRole providerRole = new PersonRole();
        providerRole.setType(PersonRoleType.Cache.INDIVIDUAL_HEALTH_CARE_PRACTITIONER.getEntity());
        providerRole.setPerson(provider);
        provider.addRole(providerRole);

        session.save(patient);
        session.save(requestorDoctor);
        session.save(provider);

        cal.set(2005, 5, 16);
        final HealthCareReferral referral = new HealthCareReferral();
        referral.setPatientRole(patientRole);
        referral.setProviderRole(providerRole);
        referral.setRequesterRole(requestorRole);
        final Date time = cal.getTime();
        referral.setReferralDate(time);
        referral.setType(HealthCareReferralType.Cache.CONSULTATION.getEntity());
        session.save(referral);
        transaction.commit();
        session.close();

        session = openSession();
        final HealthCareReferral newReferral = (HealthCareReferral) session.load(HealthCareReferral.class, referral.getHealthCareReferralId());
        cal.setTime(newReferral.getReferralDate());
        assertThat(cal.get(Calendar.YEAR), eq(2005));
        assertThat(cal.get(Calendar.MONTH), eq(5));
        assertThat(cal.get(Calendar.DATE), eq(16));
        assertThat(newReferral.getPatientRole().getRoleId(), eq(patientRole.getRoleId()));
        assertThat(newReferral.getRequesterRole().getRoleId(), eq(requestorRole.getRoleId()));
        assertThat(newReferral.getProviderRole().getRoleId(), eq(providerRole.getRoleId()));
        session.close();
    }
}
