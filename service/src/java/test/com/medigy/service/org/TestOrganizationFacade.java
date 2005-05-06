package com.medigy.service.org;

import com.medigy.persist.DbUnitTestCase;
import com.medigy.persist.model.org.Organization;
import com.medigy.persist.model.party.PartyRelationship;
import com.medigy.persist.reference.custom.party.PartyRelationshipType;
import com.medigy.persist.util.HibernateUtil;
import com.medigy.service.util.OrganizationFacade;
import com.medigy.service.util.OrganizationFacadeImpl;

import java.util.List;

public class TestOrganizationFacade extends DbUnitTestCase
{
    
    
    public void testAddInsuranceGroup()
    {
        // SETUP
        Organization parentOrg = new Organization();
        parentOrg.setOrganizationName("Netspective");
        HibernateUtil.getSession().save(parentOrg);
        // SETUP
        
        OrganizationFacade facade = new OrganizationFacadeImpl();
        facade.addInsuranceGroup(parentOrg, "Group 123");        
        HibernateUtil.closeSession();
        
        List list = HibernateUtil.getSession().createCriteria(PartyRelationship.class).list();
        assertEquals(1, list.size());
        PartyRelationship relationship = ((PartyRelationship) list.toArray()[0]);
        assertEquals(PartyRelationshipType.Cache.ORGANIZATION_ROLLUP.getEntity(), relationship.getType());
        assertEquals(parentOrg, relationship.getPartyTo());       
    }
    
    public void testListInsuranceGroups()
    {
        // TODO: Need to replace all these setup stuff with dbUnit but the Reference data needs to be 
        // inserted using dbUnit instead of the EntitySeed populator!
        // SETUP
        Organization parentOrg = new Organization();
        parentOrg.setOrganizationName("Netspective");
        
        Organization childOrg = new Organization();
        childOrg.setOrganizationName("Group 123");
        
        
        // SETUP
        
        HibernateUtil.closeSession();
        
    }

    public String getDataSetFile()
    {
        return "/com/medigy/service/org/TestOrganizationFacade.xml";
    }

}
