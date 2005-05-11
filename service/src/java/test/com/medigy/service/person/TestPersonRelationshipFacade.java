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
import com.medigy.persist.model.party.PartyRelationship;
import com.medigy.persist.model.party.PartyRole;
import com.medigy.persist.model.person.Person;
import com.medigy.persist.reference.custom.party.PartyRelationshipType;
import com.medigy.persist.reference.custom.person.PatientResponsiblePartyRoleType;
import com.medigy.persist.reference.custom.person.PersonRoleType;
import com.medigy.persist.util.HibernateUtil;
import com.medigy.service.util.PartyRelationshipFacade;
import com.medigy.service.util.PartyRelationshipFacadeImpl;
import com.medigy.service.util.PersonFacade;
import com.medigy.service.util.PersonFacadeImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;


public class TestPersonRelationshipFacade extends TestCase
{
    private static final Log log = LogFactory.getLog(TestPersonRelationshipFacade.class);
    /**
     * Validate the VALID_PARTY_RELATIONSHIP_ROLE table, Add two roles associated with one relationship and verify by
     *  reading it back out using the  PartyRelationshipFacade
     */
    /*
    public void testPersonRelationshipFacade()
    {
        Session session = new ProcessSession();
        session.setProcessName(TestPersonRelationshipFacade.class.getName() + ".testPersonRelationshipFacade()");
        SessionManager.getInstance().pushActiveSession(session);
        HibernateUtil.getSession().save(session);

        //
        HibernateUtil.beginTransaction();
        final ValidPartyRelationshipRole parentMapping = new ValidPartyRelationshipRole();
        parentMapping.setPartyRelationshipType(PartyRelationshipType.Cache.PATIENT_RESPONSIBLE_PARTY.getEntity());
        parentMapping.setPartyRoleType(PersonRoleType.Cache.PARENT.getEntity());
        HibernateUtil.getSession().save(parentMapping);

        final ValidPartyRelationshipRole childMapping = new ValidPartyRelationshipRole();
        childMapping.setPartyRelationshipType(PartyRelationshipType.Cache.PATIENT_RESPONSIBLE_PARTY.getEntity());
        childMapping.setPartyRoleType(PersonRoleType.Cache.CHILD.getEntity());
        HibernateUtil.getSession().save(childMapping);
        HibernateUtil.commitTransaction();

        PartyRelationshipType relType = PartyRelationshipType.Cache.PATIENT_RESPONSIBLE_PARTY.getEntity();
        PartyRelationshipFacade facade = new PartyRelationshipFacadeImpl();
        List validList = facade.getValidPartyRolesByRelationshipType(relType);
        assertEquals(validList.size(), 2);

        ValidPartyRelationshipRole validRelRole = (ValidPartyRelationshipRole) validList.toArray()[0];
        assertEquals(validRelRole.getPartyRoleType(), PersonRoleType.Cache.PARENT.getEntity());
        validRelRole = (ValidPartyRelationshipRole) validList.toArray()[1];
        assertEquals(validRelRole.getPartyRoleType(), PersonRoleType.Cache.CHILD.getEntity());


    }
    */

    public void testPartyRelationship()
    {
        final PersonFacade pFacade = new PersonFacadeImpl();
        final PartyRelationshipFacade facade = new PartyRelationshipFacadeImpl();

        // Adding two patients in this test
        Person patientA = pFacade.addPerson("Hackett", "Brian");
        Person mom = pFacade.addPerson("Hackett", "Mom");
        Person dad = pFacade.addPerson("Hackett", "Dad");
        Person patientB = pFacade.addPerson("Hackett", "Sister");

        final PartyRole patientARole = pFacade.addPersonRole(patientA, PersonRoleType.Cache.PATIENT.getEntity());
        final PartyRole patientBRole = pFacade.addPersonRole(patientB, PersonRoleType.Cache.PATIENT.getEntity());
        final PartyRole momRole = pFacade.addPersonRole(mom, PatientResponsiblePartyRoleType.Cache.PARENT.getEntity());
        final PartyRole dadRole = pFacade.addPersonRole(dad, PatientResponsiblePartyRoleType.Cache.PARENT.getEntity());

        HibernateUtil.getSession().save(patientA);
        HibernateUtil.getSession().save(patientB);
        HibernateUtil.getSession().save(mom);
        HibernateUtil.getSession().save(dad);

        final PartyRelationship patientAMomRel = facade.addPartyRelationship(PartyRelationshipType.Cache.PATIENT_RESPONSIBLE_PARTY.getEntity(), patientARole, momRole);
        final PartyRelationship patientBMomRel = facade.addPartyRelationship(PartyRelationshipType.Cache.PATIENT_RESPONSIBLE_PARTY.getEntity(), patientBRole, momRole);
        final PartyRelationship patientADadRel = facade.addPartyRelationship(PartyRelationshipType.Cache.PATIENT_RESPONSIBLE_PARTY.getEntity(), patientARole, dadRole);
        final PartyRelationship patientBDadRel = facade.addPartyRelationship(PartyRelationshipType.Cache.PATIENT_RESPONSIBLE_PARTY.getEntity(), patientBRole, dadRole);

        final PartyRelationship savedPatientAMomRel = (PartyRelationship) HibernateUtil.getSession().load(PartyRelationship.class, patientAMomRel.getPartyRelationshipId());
        assertEquals(savedPatientAMomRel.getType(), PartyRelationshipType.Cache.PATIENT_RESPONSIBLE_PARTY.getEntity());
        assertEquals(savedPatientAMomRel.getPartyFrom(), patientA);
        assertEquals(savedPatientAMomRel.getPartyTo(), mom);

        final PartyRelationship savedPatientBMomRel = (PartyRelationship) HibernateUtil.getSession().load(PartyRelationship.class, patientBMomRel.getPartyRelationshipId());
        assertEquals(savedPatientBMomRel.getType(), PartyRelationshipType.Cache.PATIENT_RESPONSIBLE_PARTY.getEntity());
        assertEquals(savedPatientBMomRel.getPartyFrom(), patientB);
        assertEquals(savedPatientBMomRel.getPartyTo(), mom);

        final PartyRelationship savedPatientADadRel = (PartyRelationship) HibernateUtil.getSession().load(PartyRelationship.class, patientADadRel.getPartyRelationshipId());
        assertEquals(savedPatientADadRel.getType(), PartyRelationshipType.Cache.PATIENT_RESPONSIBLE_PARTY.getEntity());
        assertEquals(savedPatientADadRel.getPartyFrom(), patientA);
        assertEquals(savedPatientADadRel.getPartyTo(), dad);

        final PartyRelationship savedPatientBDadRel = (PartyRelationship) HibernateUtil.getSession().load(PartyRelationship.class, patientBDadRel.getPartyRelationshipId());
        assertEquals(savedPatientBDadRel.getType(), PartyRelationshipType.Cache.PATIENT_RESPONSIBLE_PARTY.getEntity());
        assertEquals(savedPatientBDadRel.getPartyFrom(), patientB);
        assertEquals(savedPatientBDadRel.getPartyTo(), dad);

        final List aList = facade.listPatientResponsiblePartyRelationship(patientA);
        assertEquals(aList.size(), 2);
        assertEquals(((PartyRelationship) aList.toArray()[0]).getPartyTo(), mom);
        assertEquals(((PartyRelationship) aList.toArray()[1]).getPartyTo(), dad);

        final List bList = facade.listPatientResponsiblePartyRelationship(patientB);
        assertEquals(bList.size(), 2);
        assertEquals(((PartyRelationship) bList.toArray()[0]).getPartyTo(), mom);
        assertEquals(((PartyRelationship) bList.toArray()[1]).getPartyTo(), dad);

    }
}
