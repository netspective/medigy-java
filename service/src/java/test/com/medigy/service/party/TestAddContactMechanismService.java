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
import com.medigy.persist.model.party.Party;
import com.medigy.persist.model.party.PartyContactMechanism;
import com.medigy.persist.model.party.PartyContactMechanismPurpose;
import com.medigy.persist.model.party.PostalAddress;
import com.medigy.persist.model.person.Person;
import com.medigy.persist.reference.custom.party.ContactMechanismPurposeType;
import com.medigy.persist.reference.type.ContactMechanismType;
import com.medigy.persist.reference.type.GenderType;
import com.medigy.service.AbstractSpringTestCase;
import com.medigy.service.ServiceVersion;
import com.medigy.service.contact.AddContactMechanismService;
import com.medigy.service.dto.party.AddPostalAddressParameters;
import com.medigy.service.dto.party.NewPostalAddress;
import com.medigy.service.dto.party.PostalAddressParameters;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public class TestAddContactMechanismService extends AbstractSpringTestCase
{
    private static final Log log = LogFactory.getLog(TestAddContactMechanismService.class);

    private AddContactMechanismService addContactMechanismService;

    public void setAddContactMechanismService(final AddContactMechanismService addContactMechanismService)
    {
        this.addContactMechanismService = addContactMechanismService;
    }

    public void testAddPostalAddress() throws Exception
    {
        Country country = new Country();
        country.setCountryName("United States of America");
        country.setIsoThreeLetterCode("USA");
        country.setIsoThreeDigitCode("123");
        country.setIsoTwoLetterCode("US");
        State state = new State("Virginia", "VA");
        country.addState(state);
        getSession().save(country);


        final Person p = new Person();
        p.setLastName("Hackett");
        p.setFirstName("Ryan");
        p.addGender(GenderType.Cache.MALE.getEntity());
        p.setBirthDate(new Date(123456));
        getSession().save(p);
        getSession().flush();

        //AddContactMechanismService service =  (AddContactMechanismService) getRegistry().getService(AddContactMechanismService.class);
        final NewPostalAddress address = addContactMechanismService.addPostalAddress(new AddPostalAddressParameters() {
                public Serializable getPartyId()
                {
                    return p.getPartyId();
                }

                public ServiceVersion getServiceVersion()
                {
                    return null;
                }

                public PostalAddressParameters getPostalAddressParameters()
                {
                    return  new PostalAddressParameters() {
                        public String getStreet1()
                        {
                            return "123 Acme Road";
                        }

                        public String getStreet2()
                        {
                            return "Suite 100";
                        }

                        public String getCity()
                        {
                            return "Fairfax";
                        }

                        public String getState()
                        {
                            return "VA";
                        }

                        public String getProvince()
                        {
                            return null;
                        }

                        public String getPostalCode()
                        {
                            return "22033";
                        }

                        public String getCounty()
                        {
                            return "Fairfax County";
                        }

                        public String getCountry()
                        {
                            return "USA";
                        }

                        public String getPurposeDescription()
                        {
                            return null;
                        }

                        public ServiceVersion getServiceVersion()
                        {
                            return null;
                        }

                        public String getPurposeType()
                        {
                            return ContactMechanismPurposeType.Cache.HOME_ADDRESS.getCode();
                        }

                    };
                }

            });
        assertNull(address.getErrorMessage());

        /*
        Criteria criteria = getSession().createCriteria(PartyContactMechanism.class);
        criteria.createCriteria("party").add(Restrictions.eq("partyId", p.getPartyId()));
        List partyContactMechList = criteria.list();

        assertEquals(1, partyContactMechList.size());
        PartyContactMechanism partyContactMechanism = ((PartyContactMechanism)partyContactMechList.toArray()[0]);
        final Party party = partyContactMechanism.getPerson();
        assertEquals(party.getPartyId(), p.getPartyId());
        assertEquals(partyContactMechanism.getContactMechanism().getType(), ContactMechanismType.Cache.POSTAL_ADDRESS.getEntity());
        assertEquals(partyContactMechanism.getPurposes().size(), 1);
        final PartyContactMechanismPurpose purpose = (PartyContactMechanismPurpose) partyContactMechanism.getPurposes().toArray()[0];
        assertEquals(purpose.getType(), ContactMechanismPurposeType.Cache.HOME_ADDRESS.getEntity());
        */
        // verify the contact mchanism data
        final PostalAddress ps = (PostalAddress) getSession().get(PostalAddress.class, address.getPostalAddressId());
        assertEquals(ps.getAddress1(), "123 Acme Road");
        assertEquals(ps.getAddress2(), "Suite 100");
        assertEquals(5, ps.getAddressBoundaries().size());
        assertEquals(ps.getCity().getCityName(), "Fairfax");
        assertEquals(ps.getState().getStateAbbreviation(), "VA");
        assertEquals(ps.getPostalCode().getCodeValue(), "22033");
        assertEquals(ps.getCounty().getCountyName(), "Fairfax County");
        assertEquals(ps.getCountry().getIsoThreeLetterCode(), "USA");

        final Set<PartyContactMechanism> pcmList = ps.getPartyContactMechanisms();
        assertEquals(1, pcmList.size());
        final PartyContactMechanism partyContactMechanism = (PartyContactMechanism) pcmList.toArray()[0];
        final Party party = partyContactMechanism.getParty();
        assertEquals(party.getPartyId(), p.getPartyId());
        assertEquals(partyContactMechanism.getContactMechanism().getType(), ContactMechanismType.Cache.POSTAL_ADDRESS.getEntity());
        assertEquals(partyContactMechanism.getPurposes().size(), 1);
        final PartyContactMechanismPurpose purpose = (PartyContactMechanismPurpose) partyContactMechanism.getPurposes().toArray()[0];
        assertEquals(purpose.getType(), ContactMechanismPurposeType.Cache.HOME_ADDRESS.getEntity());
    }


}
