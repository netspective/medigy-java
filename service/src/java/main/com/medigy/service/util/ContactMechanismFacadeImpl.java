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
package com.medigy.service.util;

import com.medigy.persist.model.party.ContactMechanism;
import com.medigy.persist.model.party.Party;
import com.medigy.persist.model.party.PartyContactMechanism;
import com.medigy.persist.model.party.PartyContactMechanismPurpose;
import com.medigy.persist.model.contact.Country;
import com.medigy.persist.model.contact.State;
import com.medigy.persist.reference.custom.party.ContactMechanismPurposeType;
import com.medigy.persist.util.HibernateUtil;
import com.medigy.service.common.ReferenceEntityFacade;
import com.medigy.service.common.UnknownReferenceTypeException;
import org.hibernate.criterion.Restrictions;

public class ContactMechanismFacadeImpl implements ContactMechanismFacade
{
    private ReferenceEntityFacade  referenceEntityService;

    public ReferenceEntityFacade getReferenceEntityService()
    {
        return referenceEntityService;
    }

    public void setReferenceEntityService(final ReferenceEntityFacade referenceEntityService)
    {
        this.referenceEntityService = referenceEntityService;
    }

    /**
     * Adds a contact mechanism for a party
     *
     * @param cm            the contact mechanism
     * @param party         the party
     * @param purposeType
     * @param purposeDescription
     */
    public void addPartyContactMechanism(final ContactMechanism cm, final Party party, final String purposeType, final String purposeDescription)
    {
        // now create the relationship entry between party and the postal address
        final PartyContactMechanism mech = new PartyContactMechanism();
        mech.setParty(party);

        final PartyContactMechanismPurpose purpose = new PartyContactMechanismPurpose();
        ContactMechanismPurposeType contactMechanismPurposeType = null;
        try
        {
            contactMechanismPurposeType = referenceEntityService.getContactMechanismPurposeType(purposeType);
        }
        catch (UnknownReferenceTypeException e)
        {
            // unknown built-in purpose type
            contactMechanismPurposeType = ContactMechanismPurposeType.Cache.OTHER.getEntity();
        }
        if (contactMechanismPurposeType.equals(ContactMechanismPurposeType.Cache.OTHER.getEntity()))
            purpose.setDescription(purposeDescription);
        purpose.setType(contactMechanismPurposeType);
        purpose.setPartyContactMechanism(mech);

        mech.addPurpose(purpose);
        mech.setContactMechanism(cm);
        HibernateUtil.getSession().save(mech);
    }

    public Country getCountry(final String countryName)
    {
        return (Country) HibernateUtil.getSession().createCriteria(Country.class).add(Restrictions.eq("name", countryName)).uniqueResult();
    }

    public State getState(final String stateName)
    {
        return (State) HibernateUtil.getSession().createCriteria(State.class).add(Restrictions.eq("name", stateName)).uniqueResult();
    }
}
