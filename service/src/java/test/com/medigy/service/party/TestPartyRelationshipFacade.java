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

import com.medigy.persist.model.party.PartyRelationship;
import com.medigy.persist.model.party.PartyRole;
import com.medigy.persist.model.person.Person;
import com.medigy.persist.reference.custom.party.PartyRelationshipType;
import com.medigy.persist.reference.custom.person.PersonRoleType;
import com.medigy.persist.reference.type.GenderType;
import com.medigy.service.AbstractSpringTestCase;
import org.hibernate.Session;

import java.util.Calendar;
import java.util.List;

public class TestPartyRelationshipFacade extends AbstractSpringTestCase
{
    private PartyRelationshipFacade partyRelationshipFacade;

    public void setPartyRelationshipFacade(final PartyRelationshipFacade partyRelationshipFacade)
    {
        this.partyRelationshipFacade = partyRelationshipFacade;
    }

    public void testAddPartyRelationship()
    {
        final Session session = getSession();
        Calendar calendar = Calendar.getInstance();
        calendar.set(1980,1,1);

        Person personA = new Person();
        personA.setLastName("A");
        personA.setFirstName("Person");
        personA.setBirthDate(calendar.getTime());
        personA.addGender(GenderType.Cache.FEMALE.getEntity());

        Person personB = new Person();
        personB.setLastName("B");
        personB.setFirstName("Person");
        personB.setBirthDate(calendar.getTime());
        personB.addGender(GenderType.Cache.FEMALE.getEntity());

        personA.addPartyRole(PersonRoleType.Cache.CHILD.getEntity());
        personB.addPartyRole(PersonRoleType.Cache.PARENT.getEntity());

        getSession().save(personA);
        getSession().save(personB);

        assertNotNull(personA.getPartyRoles());
        assertNotNull(personB.getPartyRoles());
        assertEquals(1, personA.getPartyRoles().size());
        assertEquals(1, personB.getPartyRoles().size());

        PartyRole roleA = personA.getPartyRole(PersonRoleType.Cache.CHILD.getEntity());
        assertEquals(PersonRoleType.Cache.CHILD.getEntity(), roleA.getType());

        PartyRole roleB = personB.getPartyRole(PersonRoleType.Cache.PARENT.getEntity());
        assertEquals(PersonRoleType.Cache.PARENT.getEntity(), roleB.getType());

        PartyRelationship rel = partyRelationshipFacade.addPartyRelationship(PartyRelationshipType.Cache.FAMILY.getEntity(), roleA, roleB);

        final List relationships = session.createCriteria(PartyRelationship.class).list();
        assertEquals(1, relationships.size());
        PartyRelationship partyRelationship = ((PartyRelationship) relationships.toArray()[0]);
        PartyRelationshipType type = partyRelationship.getType();
        assertEquals(PartyRelationshipType.Cache.FAMILY.getEntity(),
                type);
        assertEquals(personA.getPersonId(), partyRelationship.getPartyFrom().getPartyId());
        assertEquals(personB.getPersonId(), partyRelationship.getPartyTo().getPartyId());
    }


}
