package com.medigy.service.util;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;

import com.medigy.model.org.Organization;
import com.medigy.model.party.PartyRelationship;
import com.medigy.model.party.PartyRole;
import com.medigy.reference.custom.party.OrganizationRoleType;
import com.medigy.reference.custom.party.PartyRelationshipType;
import com.medigy.util.HibernateUtil;

public class OrganizationFacadeImpl implements OrganizationFacade
{
    protected PartyRelationshipType getGroupToEmployerRelationshipType()
    {
        return PartyRelationshipType.Cache.ORGANIZATION_ROLLUP.getEntity();
    }
    
    public void addInsuranceGroup(final Organization parentOrg, final String groupName)
    {
        final Organization childOrg = new Organization();
        childOrg.setOrganizationName(groupName);
        
        final PartyRole childRole = new PartyRole();
        childRole.setType(OrganizationRoleType.Cache.GROUP.getEntity());
        childRole.setParty(childOrg);
        
        // the parent org might already have the parent role
        PartyRole parentRole = null;
        if (!parentOrg.hasPartyRole(OrganizationRoleType.Cache.PARENT_ORG.getEntity()))
        {
            parentRole = new PartyRole();
            parentRole.setType(OrganizationRoleType.Cache.PARENT_ORG.getEntity());
            parentRole.setParty(parentOrg);
        }
        else
        {
            parentRole = parentOrg.getPartyRole(OrganizationRoleType.Cache.PARENT_ORG.getEntity());
        }
        
        final PartyRelationship relationship = new PartyRelationship();
        relationship.setType(getGroupToEmployerRelationshipType());
        relationship.setPartyRoleFrom(childRole);
        relationship.setPartyRoleTo(parentRole);
        relationship.setPartyFrom(childOrg);
        relationship.setPartyTo(parentOrg);
        
        HibernateUtil.getSession().save(relationship);
    }

    public List listInsuranceGroups(Organization parentOrg)
    {
        Criteria criteria = HibernateUtil.getSession().createCriteria(Organization.class);        
        final Criteria relationshipCriteria = criteria.createCriteria("partyRelationships");
        relationshipCriteria.createCriteria("type").add(Expression.eq("systemId", getGroupToEmployerRelationshipType().getSystemId()));
        relationshipCriteria.createCriteria("partyTo").add(Expression.eq("partyId", parentOrg.getPartyId()));
        return criteria.list();
    }
    
}
