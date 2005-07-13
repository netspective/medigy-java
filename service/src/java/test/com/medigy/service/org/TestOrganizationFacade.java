package com.medigy.service.org;

import com.medigy.persist.model.org.Organization;
import com.medigy.persist.model.org.OrganizationsRelationship;
import com.medigy.persist.reference.custom.party.OrganizationsRelationshipType;
import com.medigy.service.AbstractSpringTestCase;

import java.util.List;

public class TestOrganizationFacade extends AbstractSpringTestCase
{
    private OrganizationFacade organizationFacade;

    public void setOrganizationFacade(final OrganizationFacade organizationFacade)
    {
        this.organizationFacade = organizationFacade;
    }

    public void testAddInsuranceGroup()
    {
        Organization parentOrg = new Organization();
        parentOrg.setOrganizationName("Some Big Company");
        getSession().save(parentOrg);

        // TEST addInsuranceGroup
        organizationFacade.addInsuranceGroup(parentOrg, "Software Developers");

        List list = getSession().createCriteria(OrganizationsRelationship.class).list();
        assertEquals(1, list.size());
        final OrganizationsRelationship relationship = ((OrganizationsRelationship) list.toArray()[0]);
        assertEquals(OrganizationsRelationshipType.Cache.ORGANIZATION_ROLLUP.getEntity(), relationship.getType());
        assertEquals(parentOrg.getPartyId(), relationship.getPrimaryOrgRole().getOrganization().getPartyId());
        assertEquals("Software Developers", relationship.getSecondaryOrgRole().getOrganization().getPartyName());

        // TEST listInsuranceGroups()
        final List groups = organizationFacade.listInsuranceGroups(parentOrg);
        assertEquals(1, groups.size());
    }

}
