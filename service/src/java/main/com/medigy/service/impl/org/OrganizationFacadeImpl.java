package com.medigy.service.impl.org;

import com.medigy.persist.model.org.Organization;
import com.medigy.persist.model.org.OrganizationRole;
import com.medigy.persist.model.org.OrganizationsRelationship;
import com.medigy.persist.reference.custom.party.OrganizationRoleType;
import com.medigy.persist.reference.custom.party.OrganizationsRelationshipType;
import com.medigy.service.org.OrganizationFacade;
import com.medigy.service.util.AbstractFacade;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.Query;
import org.hibernate.criterion.Expression;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;

public class OrganizationFacadeImpl extends AbstractFacade implements OrganizationFacade
{
    public static final String ORG_SEARCH_WHERE_CLAUSE = "from Organization org where " +
                "(:orgName is null or upper(org.partyName like :orgName))";

    protected OrganizationFacadeImpl(final SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }

    protected OrganizationsRelationshipType getGroupToEmployerRelationshipType()
    {
        return OrganizationsRelationshipType.Cache.ORGANIZATION_ROLLUP.getEntity();
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
        final OrganizationRole childRole = new OrganizationRole();
        childRole.setType(OrganizationRoleType.Cache.HOSPITAL.getEntity());
        childRole.setOrganization(childOrg);
        childOrg.addRole(childRole);
        getSession().save(childOrg);

        // the parent org might already have the parent role
        OrganizationRole parentRole = null;
        if (!parentOrg.hasRole(OrganizationRoleType.Cache.EMPLOYER.getEntity()))
        {
            parentRole = new OrganizationRole();
            parentRole.setType(OrganizationRoleType.Cache.EMPLOYER.getEntity());
            parentRole.setOrganization(parentOrg);
            parentOrg.addRole(parentRole);
            getSession().save(parentRole);
        }
        else
        {
            parentRole = parentOrg.getRole(OrganizationRoleType.Cache.EMPLOYER.getEntity());
        }
        final OrganizationsRelationship relationship = new OrganizationsRelationship();
        relationship.setType(getGroupToEmployerRelationshipType());
        relationship.setSecondaryOrgRole(childRole);
        relationship.setPrimaryOrgRole(parentRole);
        getSession().save(relationship);
    }

    public List listInsuranceGroups(Organization parentOrg)
    {
        //TODO: This is failing
        Criteria criteria = getSession().createCriteria(Organization.class);
        final Criteria relationshipCriteria = criteria.createCriteria("toPartyRelationships");
        relationshipCriteria.createCriteria("type").add(Expression.eq("partyRelationshipTypeId", getGroupToEmployerRelationshipType().getSystemId()));
        relationshipCriteria.createCriteria("partyTo").add(Expression.eq("partyId", parentOrg.getPartyId()));
        return criteria.list();
    }

    public Organization getOrganizationById(final Long orgId)
    {
        final Organization org = (Organization) getSession().load(Organization.class, orgId);
        return org;
    }

    public List<Organization> listOrganizationsByName(final String orgName, final boolean exactMatch)
    {
        Criteria criteria = getSession().createCriteria(Organization.class);
        if (!exactMatch)
            criteria.add(Expression.like("partyName", orgName).ignoreCase());
        else
            criteria.add(Expression.eq("partyName", orgName).ignoreCase());
        List list = criteria.list();
        List<Organization> orgList = new ArrayList<Organization>();
        convert(Organization.class, list, orgList);
        return orgList;
    }

    public Integer countOrganizationsByName(final String orgName)
    {
        final Query query = getSession().createQuery("from Organization org where " +
                "(:orgName is null or upper(org.partyName like :orgName))") ;


        return null;
    }

    public List<Organization> listOrganizationsByParentId(final Long parentOrgId)
    {
        Criteria criteria = getSession().createCriteria(Organization.class);
        // TODO: Need to see if it would be better just to a parentOrganization to the Organization class
        return null;
    }

}
