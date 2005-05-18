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


import com.medigy.persist.TestCase;
import com.medigy.persist.model.person.Person;
import com.medigy.persist.reference.custom.insurance.InsurancePolicyType;
import com.medigy.persist.reference.custom.party.PartyRelationshipType;
import com.medigy.persist.reference.custom.person.EthnicityType;
import com.medigy.persist.reference.type.GenderType;
import com.medigy.persist.reference.type.LanguageType;
import com.medigy.persist.reference.type.MaritalStatusType;
import com.medigy.persist.util.HibernateUtil;
import com.medigy.service.ServiceLocator;
import com.medigy.service.dto.person.RegisterPatientParameters;
import com.medigy.service.dto.person.RegisteredPatient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;

public class TestPatientRegistrationService extends TestCase
{
    private static final Log log = LogFactory.getLog(TestPatientRegistrationService.class);

    public void testPatientRegistrationService()
    {
        RegisterPatientParameters patientParameters = null;
                try
                {
                    patientParameters = new RegisterPatientParameters() {

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
                            return "Bluegrass";
                        }

                        public String getSuffix()
                        {
                            return null;
                        }

                        public Date getBirthDate()
                        {
                            return new Date();
                        }

                        public String getGender()
                        {
                            return GenderType.Cache.MALE.getCode();
                        }

                        public String getMaritalStatus()
                        {
                            return MaritalStatusType.Cache.SINGLE.getCode();
                        }

                        public String getEmployerName()
                        {
                            return "Netspective";
                        }

                        public String getEmployerId()
                        {
                            return "1";
                        }

                        public String getOccupation()
                        {
                            return "Consultant";
                        }

                        public String getSsn()
                        {
                            return "111111111";
                        }

                        public String[] getEthnicityCodes()
                        {
                            return new String[] { EthnicityType.Cache.AFRICAN_AMERICAN.getCode(), EthnicityType.Cache.ASIAN_PACIFIC_ISLANDER.getCode() };
                        }

                        public String[] getLanguageCodes()
                        {
                            return new String[] { LanguageType.Cache.ENGLISH.getCode(), LanguageType.Cache.SPANISH.getCode() };
                        }

                        public String getDriversLicenseNumber()
                        {
                            return "999999999";
                        }

                        public String getResponsiblePartyLastName()
                        {
                            return "Hackett";
                        }

                        public String getResponsiblePartyFirstName()
                        {
                            return "Bob";
                        }

                        public String getResponsiblePartyId()
                        {
                            return null;
                        }

                        public String getResponsiblePartyRole()
                        {
                            return PartyRelationshipType.Cache.PATIENT_RESPONSIBLE_PARTY.getCode();
                        }

                        public String getHomePhoneCountryCode()
                        {
                            return "95";
                        }

                        public String getHomePhoneCityCode()
                        {
                            return "1";
                        }

                        public String getHomePhoneAreaCode()
                        {
                            return "703";
                        }

                        public String getHomePhoneNumber()
                        {
                            return "1234567";
                        }

                        public String getWorkPhoneCountryCode()
                        {
                            return null;
                        }

                        public String getWorkPhoneCityCode()
                        {
                            return null;
                        }

                        public String getWorkPhoneAreaCode()
                        {
                            return "703";
                        }

                        public String getWorkPhoneNumber()
                        {
                            return "0000000";
                        }

                        public String getMobilePhoneCountryCode()
                        {
                            return null;
                        }

                        public String getMobilePhoneCityCode()
                        {
                            return null;
                        }

                        public String getMobilePhoneAreaCode()
                        {
                            return "703";
                        }

                        public String getMobilePhoneNumber()
                        {
                            return "1111111";
                        }

                        public String getStreetAddress1()
                        {
                            return "123 Penny Lane";
                        }

                        public String getStreetAddress2()
                        {
                            return null;
                        }

                        public String getCity()
                        {
                            return "Manchester";
                        }

                        public String getCounty()
                        {
                            return null;
                        }

                        public String getProvince()
                        {
                            return null;
                        }

                        public String getState()
                        {
                            return "KY";
                        }

                        public String getPostalCode()
                        {
                            return "12345";
                        }

                        public String getCountry()
                        {
                            return "USA";
                        }

                        public String getPrimaryCareProviderLastName()
                        {
                            return null;
                        }

                        public String getPrimaryCareProviderId()
                        {
                            return null;
                        }

                        /**
                         * Gets the patient's primary care provider first name
                         *
                         * @return
                         */
                        public String getPrimaryCareProviderFirstName()
                        {
                            return null;
                        }

                        /**
                         * Gets the list of insurance policy numbers. The first one is considered
                         * the primary insurance policy.
                         *
                         * @return
                         */
                        public String[] getInsurancePolicyNumbers()
                        {
                            return new String[]  { "12345", "ABC-12345" } ;
                        }

                        public String[] getInsurancePolicyProviders()
                        {
                            return new String[]  { "Blue Cross Blue Shield", "Anthem" };
                        }

                        public String[] getInsurancePolicyProviderIds()
                        {
                            return new String[0];
                        }

                        public String[] getInsurancePolicyTypes()
                        {
                            return new String[] { InsurancePolicyType.Cache.GROUP_INSURANCE_POLICY.getCode(),
                                InsurancePolicyType.Cache.INDIVIDUAL_INSURANCE_POLICY.getCode()};
                        }

                        public String[] getInsurancePolicyHolderLastNames()
                        {
                            return new String[] { "Hackett", "Hackett" };
                        }

                        public String[] getInsurancePolicyHolderFirstNames()
                        {
                            return new String[] { "Bryan", "Bryan" };
                        }

                        public String getResponsiblePartySuffix()
                        {
                            return null;
                        }

                        public String[] getInsurancePolicyHolderSuffix()
                        {
                            return new String[] { "Sr.", null };
                        }

                        /**
                         * Gets the list of ID of the insurance policy holders
                         *
                         * @return
                         */
                        public String[] getInsurancePolicyHolderId()
                        {
                            return new String[] { };
                        }

                        /**
                         * Gets the list of roles (relationship to the patient) of the insurance policy
                         * holder.
                         *
                         * @return
                         */
                        public String[] getInsurancePolicyHolderRole()
                        {
                            return new String[] { };
                        }

                        public String getComponentVersion()
                        {
                            return null;
                        }
                    };
                }
                catch (Exception e)
                {
                    fail(e.getMessage());
                }

        PatientRegistrationService service = (PatientRegistrationService) ServiceLocator.getInstance().getService(PatientRegistrationService.class);
        final RegisteredPatient registeredPatient = service.registerPatient(patientParameters);
        if (registeredPatient.getErrorMessage() != null)
            fail(registeredPatient.getErrorMessage());

            final Person persistedPerson = (Person) HibernateUtil.getSession().load(Person.class, registeredPatient.getPatientId());
        assertEquals(persistedPerson.getFirstName(), "Ryan");
        assertEquals(persistedPerson.getMiddleName(), "Bluegrass");
        assertEquals(persistedPerson.getLastName(), "Hackett");
        assertEquals(persistedPerson.getPartyName(), "Ryan Bluegrass Hackett");
        log.info("Names verified");
        assertEquals(persistedPerson.getLanguages().size(), 2);
        assertEquals(persistedPerson.speaksLanguage(LanguageType.Cache.ENGLISH.getEntity()), true);
        assertEquals(persistedPerson.speaksLanguage(LanguageType.Cache.SPANISH.getEntity()), true);
        assertEquals(persistedPerson.speaksLanguage(LanguageType.Cache.GERMAN.getEntity()), false);
        log.info("Languages verified");
        assertEquals(persistedPerson.getEthnicities().size(), 2);
        assertEquals(persistedPerson.hasEthnicity(EthnicityType.Cache.CAUCASIAN.getEntity()), false);
        assertEquals(persistedPerson.hasEthnicity(EthnicityType.Cache.AFRICAN_AMERICAN.getEntity()), true);
        assertEquals(persistedPerson.hasEthnicity(EthnicityType.Cache.ASIAN_PACIFIC_ISLANDER.getEntity()), true);
        log.info("Ethnicities verified");

        assertEquals(persistedPerson.getSsn(),  "111111111");
        assertEquals(persistedPerson.getDriversLicenseNumber(), "999999999");

        assertEquals(persistedPerson.getPartyContactMechanisms().size(), 2);
    }

}
