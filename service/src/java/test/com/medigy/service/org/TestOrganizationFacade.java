package com.medigy.service.org;

import com.medigy.persist.model.org.Organization;
import com.medigy.persist.model.party.PartyRelationship;
import com.medigy.persist.reference.custom.party.OrganizationRoleType;
import com.medigy.persist.reference.custom.party.PartyRelationshipType;
import com.medigy.persist.util.HibernateUtil;
import com.medigy.service.TestCase;
import com.medigy.service.impl.org.OrganizationFacadeImpl;
import com.medigy.service.org.OrganizationFacade;

import java.util.List;

public class TestOrganizationFacade extends TestCase
{
    public void testAddInsuranceGroup()
    {
        Organization parentOrg = (Organization) HibernateUtil.getSession().load(Organization.class, new Long(2));
        OrganizationFacade facade = new OrganizationFacadeImpl();

        // TEST addInsuranceGroup
        facade.addInsuranceGroup(parentOrg, "Software Developers");
        HibernateUtil.closeSession();

        List list = HibernateUtil.getSession().createCriteria(PartyRelationship.class).list();
        assertEquals(1, list.size());
        final PartyRelationship relationship = ((PartyRelationship) list.toArray()[0]);
        assertEquals(PartyRelationshipType.Cache.ORGANIZATION_ROLLUP.getEntity(), relationship.getType());
        assertEquals(parentOrg.getPartyId(), relationship.getPartyTo().getPartyId());
        assertEquals("Software Developers", relationship.getPartyFrom().getPartyName());
        assertTrue(relationship.getPartyFrom().hasPartyRole(OrganizationRoleType.Cache.GROUP.getEntity()));

        // TEST listInsuranceGroups()
        final List groups = facade.listInsuranceGroups(parentOrg);
        assertEquals(1, groups.size());
    }
    
    public String getDataSetFile()
    {
        return "/com/medigy/service/org/TestOrganizationFacade.xml";
    }

}
