package com.medigy.persist.model.party;

import com.medigy.persist.TestCase;
import com.medigy.persist.model.org.Organization;
import com.medigy.persist.model.org.OrganizationsRelationship;
import com.medigy.persist.model.person.PeopleRelationship;
import com.medigy.persist.model.person.Person;
import com.medigy.persist.model.person.PersonRole;
import com.medigy.persist.reference.custom.party.OrganizationRoleType;
import com.medigy.persist.reference.custom.party.OrganizationsRelationshipType;
import com.medigy.persist.reference.custom.party.PeopleRelationshipType;
import com.medigy.persist.reference.custom.person.PatientResponsiblePartyRoleType;
import com.medigy.persist.reference.custom.person.PersonRoleType;
import com.medigy.persist.util.HibernateUtil;

public final class TestPartyRelationship extends TestCase
{

    public void testPersonRelationship()
    {
        // Adding two patients in this test
        Person patientA = new Person("Hackett", "Brian");
        Person mom = new Person("Hackett", "Mom");
        Person dad = new Person("Hackett", "Dad");
        Person patientB = new Person("Hackett", "Sister");

        patientA.addRole(PersonRoleType.Cache.PATIENT.getEntity());
        patientB.addRole(PersonRoleType.Cache.PATIENT.getEntity());
        mom.addRole(PatientResponsiblePartyRoleType.Cache.PARENT.getEntity());
        dad.addRole(PatientResponsiblePartyRoleType.Cache.PARENT.getEntity());

        HibernateUtil.getSession().save(patientA);
        HibernateUtil.getSession().save(patientB);
        HibernateUtil.getSession().save(mom);
        HibernateUtil.getSession().save(dad);
        HibernateUtil.closeSession();

        final PersonRole patientARole = patientA.addRole(PersonRoleType.Cache.PATIENT.getEntity());
        final PersonRole patientBRole = patientB.addRole(PersonRoleType.Cache.PATIENT.getEntity());
        final PersonRole dadRole = dad.addRole(PersonRoleType.Cache.PARENT.getEntity());
        final PersonRole momRole = mom.addRole(PersonRoleType.Cache.PARENT.getEntity());

        final PeopleRelationship patientAMomRel = new PeopleRelationship();
        patientAMomRel.setType(PeopleRelationshipType.Cache.FINANCIAL_RESP_PARTY.getEntity());
        patientAMomRel.setSecondaryPersonRole(momRole);
        patientAMomRel.setPrimaryPersonRole(patientARole);

        final PeopleRelationship patientADadRel = new PeopleRelationship();
        patientADadRel.setType(PeopleRelationshipType.Cache.FINANCIAL_RESP_PARTY.getEntity());
        patientADadRel.setSecondaryPersonRole(dadRole);
        patientADadRel.setPrimaryPersonRole(patientARole);

        final PeopleRelationship patientBMomRel = new PeopleRelationship();
        patientBMomRel.setType(PeopleRelationshipType.Cache.FINANCIAL_RESP_PARTY.getEntity());
        patientBMomRel.setSecondaryPersonRole(momRole);
        patientBMomRel.setPrimaryPersonRole(patientBRole);

        final PeopleRelationship patientBDadRel = new PeopleRelationship();
        patientBDadRel.setType(PeopleRelationshipType.Cache.FINANCIAL_RESP_PARTY.getEntity());
        patientBDadRel.setSecondaryPersonRole(dadRole);
        patientBDadRel.setPrimaryPersonRole(patientBRole);

        HibernateUtil.getSession().save(patientAMomRel);
        HibernateUtil.getSession().save(patientBMomRel);
        HibernateUtil.getSession().save(patientADadRel);
        HibernateUtil.getSession().save(patientBDadRel);

        final PeopleRelationship savedPatientAMomRel = (PeopleRelationship) HibernateUtil.getSession().load(PeopleRelationship.class, patientAMomRel.getRelationshipId());
        assertEquals(savedPatientAMomRel.getType(), PeopleRelationshipType.Cache.FINANCIAL_RESP_PARTY.getEntity());
        assertEquals(savedPatientAMomRel.getPrimaryPersonRole().getPerson(), patientA);
        assertEquals(savedPatientAMomRel.getSecondaryPersonRole().getPerson(), mom);

        final PeopleRelationship savedPatientBMomRel = (PeopleRelationship) HibernateUtil.getSession().load(PeopleRelationship.class, patientBMomRel.getRelationshipId());
        assertEquals(savedPatientBMomRel.getType(), PeopleRelationshipType.Cache.FINANCIAL_RESP_PARTY.getEntity());
        assertEquals(savedPatientBMomRel.getPrimaryPersonRole().getPerson(), patientB);
        assertEquals(savedPatientBMomRel.getSecondaryPersonRole().getPerson(), mom);

        final PeopleRelationship savedPatientADadRel = (PeopleRelationship) HibernateUtil.getSession().load(PeopleRelationship.class, patientADadRel.getRelationshipId());
        assertEquals(savedPatientADadRel.getType(), PeopleRelationshipType.Cache.FINANCIAL_RESP_PARTY.getEntity());
        assertEquals(savedPatientADadRel.getPrimaryPersonRole().getPerson(), patientA);
        assertEquals(savedPatientADadRel.getSecondaryPersonRole().getPerson(), dad);

        final PeopleRelationship savedPatientBDadRel = (PeopleRelationship) HibernateUtil.getSession().load(PeopleRelationship.class, patientBDadRel.getRelationshipId());
        assertEquals(savedPatientBDadRel.getType(), PeopleRelationshipType.Cache.FINANCIAL_RESP_PARTY.getEntity());
        assertEquals(savedPatientBDadRel.getPrimaryPersonRole().getPerson(), patientB);
        assertEquals(savedPatientBDadRel.getSecondaryPersonRole().getPerson(), dad);

    }

    public void testOrgRelationship()
    {
        final Organization primaryOrg = new Organization();
        primaryOrg.setOrganizationName("Acme Inc");
        primaryOrg.addRole(OrganizationRoleType.Cache.PARENT_ORG.getEntity());

        final Organization secondaryOrg = new Organization();
        secondaryOrg.setOrganizationName("Road Runner Enterprises");
        secondaryOrg.addRole(OrganizationRoleType.Cache.OTHER_ORG_UNIT.getEntity());

        HibernateUtil.getSession().save(primaryOrg);
        HibernateUtil.getSession().save(secondaryOrg);

        final Organization parentOrg = (Organization) HibernateUtil.getSession().load(Organization.class, primaryOrg.getOrgId());
        final Organization childOrg = (Organization) HibernateUtil.getSession().load(Organization.class, secondaryOrg.getOrgId());
        assertNotNull(parentOrg);
        assertNotNull(childOrg);

        final OrganizationsRelationship rel = new OrganizationsRelationship();
        rel.setPrimaryOrgRole(parentOrg.getRole(OrganizationRoleType.Cache.PARENT_ORG.getEntity()));
        rel.setSecondaryOrgRole(childOrg.getRole(OrganizationRoleType.Cache.OTHER_ORG_UNIT.getEntity()));
        rel.setType(OrganizationsRelationshipType.Cache.ORGANIZATION_ROLLUP.getEntity());

        HibernateUtil.getSession().save(rel);
        HibernateUtil.closeSession();

        OrganizationsRelationship newRelation = (OrganizationsRelationship) HibernateUtil.getSession().load(OrganizationsRelationship.class, rel.getRelationshipId());
        assertNotNull(newRelation);
        assertEquals(childOrg.getPartyId(), newRelation.getSecondaryOrgRole().getOrganization().getPartyId());
        assertEquals(parentOrg.getPartyId(), newRelation.getPrimaryOrgRole().getOrganization().getPartyId());
    }

    public String getDataSetFile()
    {
        return "/com/medigy/persist/model/party/TestPartyRelationship.xml";
    }
}
