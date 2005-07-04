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
package com.medigy.service.contact;

import com.medigy.persist.model.party.ContactMechanism;
import com.medigy.persist.model.party.Party;
import com.medigy.persist.model.party.PostalAddress;
import com.medigy.persist.model.party.PhoneNumber;
import com.medigy.persist.model.party.ElectronicAddress;
import com.medigy.persist.model.contact.Country;
import com.medigy.persist.model.contact.State;
import com.medigy.service.util.Facade;
import com.medigy.service.util.UnknownReferenceTypeException;

import java.io.Serializable;
import java.util.List;

public interface ContactMechanismFacade extends Facade
{
    /**
     * Creates a connection between a contact mechanism (email ,phone, etc) and a party and also
     * assigns a "purpose" for the connection.
     *
     * @param cm                    the contact mechanism object
     * @param party                 the party relating to the contact mechanism
     * @param purposeType           the purpose type for the relationship
     * @param purposeDescription    the description of the purpose if the type cannot be determined
     * @throws com.medigy.service.util.UnknownReferenceTypeException
     */
    public void addPartyContactMechanism(final ContactMechanism cm, final Party party, final String purposeType,
                                         final String purposeDescription)  throws UnknownReferenceTypeException;


    public PostalAddress addPostalAddress(final String street1, final String street2, final String cityName,
                                          final String stateCode, final String provinceCode, final String countyName,
                                          final String postalCode, final String countryCode);

    /**
     * Creates a new phone number entry
     * @param countryCode
     * @param areaCode
     * @param number
     * @param extension
     * @return
     */
    public PhoneNumber addPhone(final String countryCode, final String cityCode, final String areaCode, final String number,
                                final String extension);

    public PhoneNumber addPhone(final String countryCode, final String cityCode, final String number);

    /**
     * Gets a country geographic boundary based on the name.
     *
     * @param countryName
     * @return NULL if no match is found
     */
    public Country getCountry(final String countryName);
    public State getState(final String stateName);

    /**
     * Gets a contact mechanism by its unique id
     * @param id
     * @return
     */
    public ContactMechanism getContactMechanismById(final Serializable id);

    /**
     * Gets all postal addresses registered for the party by its ID
     * @param id    party ID
     * @return
     */
    public List<PostalAddress> listPostalAddresses(final Long id);
    public List<PhoneNumber> listPhoneNumbers(final Long id);
    public List<ElectronicAddress> listEmails(final Long id);
}
