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

import com.medigy.persist.model.contact.Country;
import com.medigy.persist.model.contact.State;
import com.medigy.persist.model.health.HealthCareLicense;
import com.medigy.persist.model.person.Person;
import com.medigy.persist.reference.custom.health.HealthCareLicenseType;
import com.medigy.persist.reference.custom.person.EthnicityType;
import com.medigy.persist.reference.type.GenderType;
import com.medigy.persist.reference.type.LanguageType;
import com.medigy.persist.util.HibernateUtil;
import com.medigy.service.TestCase;
import com.medigy.service.dto.person.HealthCareLicenseParameters;
import com.medigy.service.dto.person.PersonParameters;
import com.medigy.service.dto.person.RegisterHealthCareProviderParameters;
import com.medigy.service.dto.person.RegisteredProvider;

import java.util.Calendar;
import java.util.Date;

public class TestRegisterHealthCareProviderService extends TestCase
{
    protected void setUp() throws Exception
    {
        super.setUp();

        HibernateUtil.beginTransaction();
        final Country usa = new Country("United States of America", "USA");
        final State va = new State("Virginia", "VA");
        va.setParentCountry(usa);
        usa.addState(va);
        HibernateUtil.getSession().save(usa);
        HibernateUtil.commitTransaction();
    }

    public void testRegisterHealthCareProviderService()
    {
        final RegisterHealthCareProviderService service = (RegisterHealthCareProviderService) getRegistry().getService(RegisterHealthCareProviderService.class);

        final Calendar calendar = Calendar.getInstance();
        calendar.set(3000, 1, 1);
        final Date expirationDate = calendar.getTime();
        calendar.set(2000, 1, 1);
        final Date certificationDate = calendar.getTime() ;

        HibernateUtil.beginTransaction();
        final RegisteredProvider newProvider = service.register(new RegisterHealthCareProviderParameters() {
            public PersonParameters getPerson()
            {
                return new PersonParameters() {
                    public String getMaritalStatusCode()
                    {
                        return null;
                    }

                    public String getDriversLicenseNumber()
                    {
                        return null;
                    }

                    public String getDriversLicenseStateCode()
                    {
                        return null;
                    }

                    public String getEmployerName()
                    {
                        return null;
                    }

                    public String getOccupation()
                    {
                        return null;
                    }

                    public String getLastName()
                    {
                        return "Bond";
                    }

                    public String getFirstName()
                    {
                        return "James";
                    }

                    public String getMiddleName()
                    {
                        return null;
                    }

                    public String getSuffix()
                    {
                        return null;
                    }

                    public Date getBirthDate()
                    {
                        final Calendar calendar = Calendar.getInstance();
                        calendar.set(1970, 1, 1);
                        return calendar.getTime();
                    }

                    public String getSsn()
                    {
                        return "123456789";
                    }

                    public String getGenderCode()
                    {
                        return GenderType.Cache.MALE.getEntity().getCode();
                    }

                    public String[] getEthnicityCodes()
                    {
                        return new String[] { EthnicityType.Cache.AFRICAN_AMERICAN.getEntity().getCode(), EthnicityType.Cache.ASIAN_PACIFIC_ISLANDER.getEntity().getCode()};
                    }

                    public String[] getLanguageCodes()
                    {
                        return new String[] { LanguageType.Cache.ENGLISH.getEntity().getCode(), LanguageType.Cache.FRENCH.getEntity().getCode()};
                    }

                    public String getEmployerId()
                    {
                        return null;
                    }
                };
            }


            public HealthCareLicenseParameters[] getLicenseParameters()
            {
                return new HealthCareLicenseParameters[]  {
                    new HealthCareLicenseParameters() {
                        public String getLicenseNumber()
                        {
                            return "1234567";
                        }

                        public String getLicenseType()
                        {
                            return HealthCareLicenseType.Cache.BOARD_CERTIFICATION.getEntity().getCode();
                        }

                        public String getState()
                        {
                            return "Virginia";
                        }

                        public String getCountry()
                        {
                            return "United States of America";
                        }

                        public String getDescription()
                        {
                            return null;
                        }

                        public Date getExpirationDate()
                        {
                            return expirationDate;
                        }

                        public Date getCertificationDate()
                        {
                            return certificationDate;
                        }
                    }
                };
            }

            public String getComponentVersion()
            {
                return null;
            }
        });
        assertThat(newProvider.getErrorMessage(), NULL);
        HibernateUtil.commitTransaction();
        HibernateUtil.closeSession();


        final Person person = (Person) HibernateUtil.getSession().load(Person.class, newProvider.getRegisteredProviderId());
        assertThat(person, NOT_NULL);
        assertThat(person.getLastName(), eq("Bond"));
        assertThat(person.getFirstName(), eq("James"));
        assertThat(person.getCurrentGender().getCode(), eq(GenderType.Cache.MALE.getEntity().getCode()));
        assertThat(person.getLicenses().size(), eq(1));
        assertThat(person.getEthnicities().size(), eq(2));
        assertThat(person.getLanguages().size(), eq(2));

        final HealthCareLicense license = person.getLicense(HealthCareLicenseType.Cache.BOARD_CERTIFICATION.getEntity());
        assertThat(license, NOT_NULL);
        assertThat(license.getLicenseNumber(), eq("1234567"));
        assertThat(license.getThroughDate(), eq(expirationDate));
        assertThat(license.isExpired(), eq(false));
        assertThat(license.getFromDate(), eq(certificationDate));


    }
}
