package com.medigy.persist.model.party;

import com.medigy.persist.TestCase;
import com.medigy.persist.model.org.Organization;
import com.medigy.persist.model.person.Person;
import com.medigy.persist.reference.custom.party.PartyRelationshipType;
import com.medigy.persist.reference.custom.person.PatientResponsiblePartyRoleType;
import com.medigy.persist.reference.custom.person.PersonRoleType;
import com.medigy.persist.util.HibernateUtil;

import java.util.List;

public final class TestPartyRelationship extends TestCase
{

    public void testPersonRelationship()
    {
        // Adding two patients in this test
        Person patientA = new Person("Hackett", "Brian");
        Person mom = new Person("Hackett", "Mom");
        Person dad = new Person("Hackett", "Dad");
        Person patientB = new Person("Hackett", "Sister");

        patientA.addPartyRole(PersonRoleType.Cache.PATIENT.getEntity());
        patientB.addPartyRole(PersonRoleType.Cache.PATIENT.getEntity());
        mom.addPartyRole(PatientResponsiblePartyRoleType.Cache.PARENT.getEntity());
        dad.addPartyRole(PatientResponsiblePartyRoleType.Cache.PARENT.getEntity());

        HibernateUtil.getSession().save(patientA);
        HibernateUtil.getSession().save(patientB);
        HibernateUtil.getSession().save(mom);
        HibernateUtil.getSession().save(dad);
        HibernateUtil.closeSession();

        final PartyRole patientARole = patientA.getPartyRole(PersonRoleType.Cache.PATIENT.getEntity());
        final PartyRole patientBRole = patientB.getPartyRole(PersonRoleType.Cache.PATIENT.getEntity());
        final PartyRole dadRole = dad.getPartyRole(PersonRoleType.Cache.PARENT.getEntity());
        final PartyRole momRole = mom.getPartyRole(PersonRoleType.Cache.PARENT.getEntity());

        final PartyRelationship patientAMomRel = new PartyRelationship();
        patientAMomRel.setType(PartyRelationshipType.Cache.PATIENT_RESPONSIBLE_PARTY.getEntity());
        patientAMomRel.setPartyTo(mom);
        patientAMomRel.setPartyRoleTo(momRole);
        patientAMomRel.setPartyFrom(patientA);
        patientAMomRel.setPartyRoleFrom(patientARole);

        final PartyRelationship patientADadRel = new PartyRelationship();
        patientADadRel.setType(PartyRelationshipType.Cache.PATIENT_RESPONSIBLE_PARTY.getEntity());
        patientADadRel.setPartyTo(dad);
        patientADadRel.setPartyRoleTo(dadRole);
        patientADadRel.setPartyFrom(patientA);
        patientADadRel.setPartyRoleFrom(patientARole);

        final PartyRelationship patientBMomRel = new PartyRelationship();
        patientBMomRel.setType(PartyRelationshipType.Cache.PATIENT_RESPONSIBLE_PARTY.getEntity());
        patientBMomRel.setPartyTo(mom);
        patientBMomRel.setPartyRoleTo(momRole);
        patientBMomRel.setPartyFrom(patientB);
        patientBMomRel.setPartyRoleFrom(patientBRole);

        final PartyRelationship patientBDadRel = new PartyRelationship();
        patientBDadRel.setType(PartyRelationshipType.Cache.PATIENT_RESPONSIBLE_PARTY.getEntity());
        patientBDadRel.setPartyTo(dad);
        patientBDadRel.setPartyRoleTo(dadRole);
        patientBDadRel.setPartyFrom(patientB);
        patientBDadRel.setPartyRoleFrom(patientBRole);

        HibernateUtil.getSession().save(patientAMomRel);
        HibernateUtil.getSession().save(patientBMomRel);
        HibernateUtil.getSession().save(patientADadRel);
        HibernateUtil.getSession().save(patientBDadRel);

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

    }

    public void testPartyRelationship()
    {
        // don't really need to do this since the XML insertion  is WORKING?
        List partyList = HibernateUtil.getSession().createCriteria(Party.class).list();
        assertEquals(3, partyList.size());
        List orgList = HibernateUtil.getSession().createCriteria(Organization.class).list();
        assertEquals(2, orgList.size());
        //List personList = HibernateUtil.getSession().createCriteria(Person.class).list();
        
        final Party parentOrg = (Party) HibernateUtil.getSession().load(Party.class, new Long(2));
        final Party childOrg = (Party) HibernateUtil.getSession().load(Party.class, new Long(3));
        assertNotNull(parentOrg);
        assertNotNull(childOrg);
        
        PartyRole employerRole = (PartyRole) HibernateUtil.getSession().load(PartyRole.class, new Long(1));
        PartyRole groupRole = (PartyRole) HibernateUtil.getSession().load(PartyRole.class, new Long(2));
        PartyRelationshipType orgRelationshipType = (PartyRelationshipType) HibernateUtil.getSession().load(PartyRelationshipType.class, new Long(1));
        assertNotNull(orgRelationshipType);
        
        final PartyRelationship rel = new PartyRelationship();
        rel.setPartyRoleFrom(groupRole);
        rel.setPartyRoleTo(employerRole);
        rel.setPartyFrom(childOrg);
        rel.setPartyTo(parentOrg);
        rel.setType(orgRelationshipType);
        
        groupRole.getFromPartyRelationships().add(rel);
        employerRole.getToPartyRelationships().add(rel);
        HibernateUtil.getSession().save(rel);
        HibernateUtil.closeSession();
        
        PartyRelationship newRelation = (PartyRelationship) HibernateUtil.getSession().load(PartyRelationship.class, rel.getPartyRelationshipId());
        assertNotNull(newRelation);
        assertEquals(childOrg.getPartyId(), newRelation.getPartyFrom().getPartyId());
        assertEquals(parentOrg.getPartyId(), newRelation.getPartyTo().getPartyId());
        assertEquals(employerRole.getPartyRoleId(), newRelation.getPartyRoleTo().getPartyRoleId());
        assertEquals(groupRole.getPartyRoleId(), newRelation.getPartyTo().getPartyId());
    }

    public String getDataSetFile()
    {        
        return "/com/medigy/persist/model/party/TestPartyRelationship.xml";
    }
}
