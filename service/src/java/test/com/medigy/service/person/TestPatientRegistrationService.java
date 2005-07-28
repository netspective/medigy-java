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
import com.medigy.persist.model.contact.Country;
import com.medigy.persist.model.person.Person;
import com.medigy.persist.model.org.Organization;
import com.medigy.persist.model.insurance.InsuranceProduct;
import com.medigy.persist.model.insurance.InsurancePlan;
import com.medigy.persist.reference.custom.insurance.InsurancePolicyType;
import com.medigy.persist.reference.custom.insurance.InsuranceProductType;
import com.medigy.persist.reference.custom.party.ContactMechanismPurposeType;
import com.medigy.persist.reference.custom.party.PeopleRelationshipType;
import com.medigy.persist.reference.custom.person.EthnicityType;
import com.medigy.persist.reference.custom.org.OrganizationClassificationType;
import com.medigy.persist.reference.type.GenderType;
import com.medigy.persist.reference.type.LanguageType;
import com.medigy.persist.reference.type.MaritalStatusType;
import com.medigy.service.AbstractSpringTestCase;
import com.medigy.service.ServiceVersion;
import com.medigy.service.dto.person.RegisterPatientParameters;
import com.medigy.service.dto.person.RegisteredPatient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Past;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Calendar;
import java.util.List;

public class TestPatientRegistrationService extends AbstractSpringTestCase
{
    private static final Log log = LogFactory.getLog(TestPatientRegistrationService.class);

    private PatientRegistrationService patientRegistrationService;

    public void setPatientRegistrationService(final PatientRegistrationService patientRegistrationService)
    {
        this.patientRegistrationService = patientRegistrationService;
    }

    public void testPatientRegistrationService()
    {
        final Calendar cal = Calendar.getInstance();
        cal.set(1980, 1, 1);

        Country usa = new Country();
        usa.setCountryName("United States of America");
        usa.setCountryAbbreviation("USA");

        final State virginia = new State();
        virginia.setStateName("Virginia");
        virginia.setStateAbbreviation("VA");
        usa.addState(virginia);
        getSession().save(usa);

        final Organization blueCross = new Organization();
        blueCross.setOrganizationName("Blue Cross Blue Shield");
        blueCross.addPartyClassification(OrganizationClassificationType.Cache.INSURANCE.getEntity());

        final InsuranceProduct insProduct = new InsuranceProduct();
        insProduct.setType(InsuranceProductType.Cache.PPO.getEntity());
        insProduct.setOrganization(blueCross);
        blueCross.addInsuranceProduct(insProduct);

        final InsurancePlan insPlan = new InsurancePlan();
        insPlan.setName("Test Plan");
        insPlan.setOrganization(blueCross);
        insPlan.setMedigapId("123");
        insPlan.setInsuranceProduct(insProduct);
        insProduct.addInsurancePlan(insPlan);
        getSession().save(blueCross);

        RegisterPatientParameters patientParameters = null;
                try
                {
                    patientParameters = new RegisterPatientParameters() {
                        public String getPersonId()
                        {
                            return null;  //To change body of implemented methods use File | Settings | File Templates.
                        }

                        @NotNull public String getPatientId()
                        {
                            return null;  //To change body of implemented methods use File | Settings | File Templates.
                        }

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
                            return cal.getTime();
                        }

                        @NotNull @Past public Date getDeathDate()
                        {
                            return null;  //To change body of implemented methods use File | Settings | File Templates.
                        }

                        public String getGenderCode()
                        {
                            return GenderType.Cache.MALE.getCode();
                        }

                        public String getMaritalStatusCode()
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

                        public List<String> getEthnicityCodes()
                        {
                            return Arrays.asList(new String[] { EthnicityType.Cache.AFRICAN_AMERICAN.getCode(), EthnicityType.Cache.ASIAN_PACIFIC_ISLANDER.getCode() });
                        }

                        public List<String> getLanguageCodes()
                        {
                            return Arrays.asList(new String[] { LanguageType.Cache.ENGLISH.getCode(), LanguageType.Cache.SPANISH.getCode() });
                        }

                        public String getDriversLicenseNumber()
                        {
                            return "999999999";
                        }

                        public String getDriversLicenseStateCode()
                        {
                            return State.Cache.VIRGINIA.getEntity().getStateAbbreviation();
                        }


                        public String getResponsiblePartyId()
                        {
                            return "999";
                        }

                        public String getResponsiblePartyRole()
                        {
                            return PeopleRelationshipType.Cache.FINANCIAL_RESP_PARTY.getCode();
                        }

                        public String getHomePhone()
                        {
                            return "7031234567";
                        }

                        public String getWorkPhone()
                        {
                            return "7030000000 x123";
                        }

                        public String getMobilePhone()
                        {
                            return "7031111111";
                        }

                        public String getStreet1()
                        {
                            return "123 Penny Lane";
                        }

                        public String getPostalAddressPurposeType()
                        {
                            return ContactMechanismPurposeType.Cache.HOME_ADDRESS.getEntity().getCode();
                        }

                        public String getPostalAddressPurposeDescription()
                        {
                            return null;
                        }

                        public String getStreet2()
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
                            return "VA";
                        }

                        public String getPostalCode()
                        {
                            return "12345";
                        }

                        public String getCountry()
                        {
                            return "USA";
                        }


                        public String getPrimaryCareProviderId()
                        {
                            return null;
                        }

                        public Serializable getPrimaryInsuranceCarrierId()
                        {
                            return blueCross.getPartyId();
                        }

                        public Serializable getPrimaryInsuranceProductId()
                        {
                            return insProduct.getInsuranceProductId();
                        }

                        public Serializable getPrimaryInsurancePlanId()
                        {
                            return insPlan.getInsurancePlanId();
                        }

                        public String getPrimaryInsurancePolicyNumber()
                        {
                            return "12345";
                        }

                        public String getPrimaryInsurancePolicyTypeCode()
                        {
                            return InsurancePolicyType.Cache.GROUP_INSURANCE_POLICY.getEntity().getCode();
                        }

                        public String getPrimaryInsuranceGroupNumber()
                        {
                            return "XXX";
                        }

                        public Serializable getPrimaryInsuranceContractHolderId()
                        {
                            return null;
                        }

                        public String getPrimaryInsuranceContractHolderRole()
                        {
                            return null;
                        }

                        public Date getPrimaryInsuranceCoverageStartDate()
                        {
                            return new Date();
                        }

                        public Date getPrimaryInsuranceCoverageEndDate()
                        {
                            return null;
                        }

                        public Float getPrimaryInsuranceIndividualDeductibleAmount()
                        {
                            return null;
                        }

                        public Float getPrimaryInsuranceFamilyDeductibleAmount()
                        {
                            return null;
                        }

                        public Float getPrimaryInsuranceOfficeVisitCoPay()
                        {
                            return null;
                        }

                        public Float getPrimaryInsurancePercentagePay()
                        {
                            return null;
                        }

                        public Float getPrimaryInsuranceMaxThresholdAmount()
                        {
                            return null;
                        }

                        public ServiceVersion getServiceVersion()
                        {
                            return null;
                        }
                    };
                }
                catch (Exception e)
                {
                    fail(e.getMessage());
                }

        final RegisteredPatient registeredPatient = patientRegistrationService.registerPatient(patientParameters, "insert");
        if (registeredPatient.getErrorMessage() != null)
        {
            fail(registeredPatient.getErrorMessage());
            return;
        }

        final Person persistedPerson = (Person) getSession().load(Person.class, registeredPatient.getPatientId());
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
