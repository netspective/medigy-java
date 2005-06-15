package com.medigy.service.impl.org;

import com.medigy.persist.model.org.Organization;
import com.medigy.persist.model.party.PartyRelationship;
import com.medigy.persist.model.party.PartyRole;
import com.medigy.persist.reference.custom.party.OrganizationRoleType;
import com.medigy.persist.reference.custom.party.PartyRelationshipType;
import com.medigy.service.org.OrganizationFacade;
import com.medigy.service.util.AbstractFacade;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Expression;

import java.util.List;

public class OrganizationFacadeImpl extends AbstractFacade implements OrganizationFacade
{
    protected OrganizationFacadeImpl(final SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }

    protected PartyRelationshipType getGroupToEmployerRelationshipType()
    {
        return PartyRelationshipType.Cache.ORGANIZATION_ROLLUP.getEntity();
    }

    /**
     * Adds a logical insurance group under a Employer organization. This could just
     * be a place holder for a group # for an employee's insurance policy.
     * @param parentOrg   Employer organization
     */
    public void addInsuranceGroup(final Organization parentOrg, final String groupName)
    {
        final Organization childOrg = new Organization();
        childOrg.setOrganizationName(groupName);
        
        final PartyRole childRole = new PartyRole();
        childRole.setType(OrganizationRoleType.Cache.HOSPITAL.getEntity());
        childRole.setParty(childOrg);
        childOrg.addPartyRole(childRole);
        getSession().save(childOrg);

        // the parent org might already have the parent role
        PartyRole parentRole = null;
        if (!parentOrg.hasPartyRole(OrganizationRoleType.Cache.EMPLOYER.getEntity()))
        {
            parentRole = new PartyRole();
            parentRole.setType(OrganizationRoleType.Cache.EMPLOYER.getEntity());
            parentRole.setParty(parentOrg);
            parentOrg.addPartyRole(parentRole);
            getSession().save(parentRole);
        }
        else
        {
            parentRole = parentOrg.getPartyRole(OrganizationRoleType.Cache.EMPLOYER.getEntity());
        }
        
        final PartyRelationship relationship = new PartyRelationship();
        relationship.setType(getGroupToEmployerRelationshipType());
        relationship.setPartyRoleFrom(childRole);
        relationship.setPartyRoleTo(parentRole);
        relationship.setPartyFrom(childOrg);
        relationship.setPartyTo(parentOrg);
        
        getSession().save(relationship);
    }

    public List listInsuranceGroups(Organization parentOrg)
    {
        Criteria criteria = getSession().createCriteria(Organization.class);
        final Criteria relationshipCriteria = criteria.createCriteria("toPartyRelationships");
        relationshipCriteria.createCriteria("type").add(Expression.eq("partyRelationshipTypeId", getGroupToEmployerRelationshipType().getSystemId()));
        relationshipCriteria.createCriteria("partyTo").add(Expression.eq("partyId", parentOrg.getPartyId()));
        return criteria.list();
    }
    
}
