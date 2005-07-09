package com.medigy.service.org;

import com.medigy.persist.model.org.Organization;
import com.medigy.service.util.Facade;

import java.util.List;
import java.util.Set;

public interface OrganizationFacade extends Facade
{
    /**
     * Adds a logical insurance group under a Employer organization. This could just
     * be a place holder for a group # for an employee's insurance policy.
     * @param org   Employer organization
     */
    public void addInsuranceGroup(final Organization org, final String groupName);

    /**
     * Lists all logical insurance groups under an Employer organization.
     * @param parentOrg
     * @return  list of insurance groups
     */
    public List listInsuranceGroups(final Organization parentOrg);

    public Organization getOrganizationById(final Long orgId);

    public List<Organization> listOrganizationsByName(final String orgName, final boolean exactMatch);
    public Integer countOrganizationsByName(final String orgName);

    public List<Organization> listOrganizationsByParentId(final Long parentOrgId);

}
