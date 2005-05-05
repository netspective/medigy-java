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

import com.medigy.model.party.PartyRole;
import com.medigy.model.party.PostalAddress;
import com.medigy.model.party.PartyContactMechanism;
import com.medigy.model.party.PartyContactMechanismPurpose;
import com.medigy.model.person.Person;
import com.medigy.reference.custom.person.PersonRoleType;
import com.medigy.reference.custom.party.ContactMechanismPurposeType;
import com.medigy.reference.type.ContactMechanismType;
import com.medigy.service.util.PersonFacade;
import com.medigy.util.HibernateUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class PersonFacadeImpl implements PersonFacade
{
    private static final Log log = LogFactory.getLog(PersonFacadeImpl.class);


    /**
     * Lists all patients with the same last name. This query is not case sensitive.
     *
     * @param lastName      exact last name or partial last name
     * @param exactMatch    whether or not the name search should be an exact match
     * @return
     */
    public Person[] listPersonByLastName(final String lastName, boolean exactMatch)
    {
        Criteria criteria = HibernateUtil.getSession().createCriteria(Person.class);
        if (!exactMatch)
            criteria.add(Expression.like("lastName", lastName).ignoreCase());
        else
            criteria.add(Expression.eq("lastName", lastName).ignoreCase());
        List list = criteria.list();

        return list != null ? (Person[]) list.toArray(new Person[0]) : null;
    }

    /**
     * Gets the person from the database based on the party ID
     * @param id
     * @return
     */
    public Person getPersonById(final Serializable id)
    {
        return (Person) HibernateUtil.getSession().load(Person.class, id);
    }

    /**
     * Adds the person in the database. Any related entities will also be added based on the cascade types.
     *
     * @param person
     */
    public void addPerson(final Person person)
    {
        HibernateUtil.getSession().save(person);
    }

    /**
     * Adds a person in the database using the passed in name values
     * @param lastName
     * @param firstName
     * @return Person
     */
    public Person addPerson(final String lastName, final String firstName)
    {
        final Person person = new Person();
        person.setLastName(lastName);
        person.setFirstName(firstName);
        addPerson(person);
        return person;
    }

    /**
     * Creates new person (party) role and adds it to the person. If the person record already exists then
     * this will go ahead and create the role in the database.
     *
     * @param person    already existing Person
     * @param type
     * @return PersonRole
     */
    public PartyRole addPersonRole(final Person person, final PersonRoleType type)
    {
        final PartyRole respPartyRole = new PartyRole();
        respPartyRole.setType(type);
        respPartyRole.setParty(person);
        person.addPartyRole(respPartyRole);

        // if the person record already exists then go ahead and create the role in the database
        // but if the person record doesn't exist yet, don't write to the database yet and
        // let the cascade events of the person adding handle it
        if (person.getPartyId() != null)
            HibernateUtil.getSession().save(respPartyRole);

        return respPartyRole;
    }

    /**
     * Gets the home address for the person. Returns null if none is found
     * @param person
     * @return
     */
    public PostalAddress getHomeAddress(final Person person)
    {
        final Set<PartyContactMechanism> partyContactMechanisms = person.getPartyContactMechanisms();
        for (PartyContactMechanism pcm : partyContactMechanisms)
        {
            if (pcm.getContactMechanism().getType().equals(ContactMechanismType.Cache.POSTAL_ADDRESS.getEntity()))
            {
                final Set<PartyContactMechanismPurpose> purposes = pcm.getPurposes();
                for (PartyContactMechanismPurpose purpose : purposes)
                {
                    if (purpose.getType().equals(ContactMechanismPurposeType.Cache.HOME_ADDRESS.getEntity()))
                    {
                        Criteria criteria = HibernateUtil.getSession().createCriteria(PostalAddress.class);
                        criteria.add(Expression.eq("contactMechanismId", pcm.getContactMechanism().getContactMechanismId()));
                        return (PostalAddress) criteria.uniqueResult();
                    }
                }
            }
        }
        return null;
    }


}
