package com.medigy.service.util;

import java.util.List;

import com.medigy.model.org.Organization;

public interface OrganizationFacade
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
     * @return
     */
    public List listInsuranceGroups(final Organization parentOrg);
}
