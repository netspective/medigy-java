/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.model.org;

import com.medigy.persist.model.party.Party;
import com.medigy.persist.model.party.PartyRole;
import com.medigy.persist.model.person.PersonAndOrgRelationship;
import com.medigy.persist.reference.custom.party.OrganizationRoleType;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

@Entity
public class OrganizationRole extends PartyRole
{
    private Organization organization;
    private OrganizationRoleType type;
    private String typeDescription;

    private List<PersonAndOrgRelationship> personOrgRelationships = new ArrayList<PersonAndOrgRelationship>();
    private List<OrganizationsRelationship> primaryOrgRelationships = new ArrayList<OrganizationsRelationship>();
    private List<OrganizationsRelationship> secondaryOrgRelationships = new ArrayList<OrganizationsRelationship>();

    @ManyToOne
    @JoinColumn(name = Organization.PK_COLUMN_NAME)
    public Organization getOrganization()
    {
        return organization;
    }

    public void setOrganization(final Organization organization)
    {
        this.organization = organization;
    }

    @OneToMany(mappedBy = "organizationRole", cascade = CascadeType.ALL)
    public List<PersonAndOrgRelationship> getPersonOrgRelationships()
    {
        return personOrgRelationships;
    }

    public void setPersonOrgRelationships(final List<PersonAndOrgRelationship> personOrgRelationships)
    {
        this.personOrgRelationships = personOrgRelationships;
    }

    @Transient
    public Party getParty()
    {
        return organization;
    }

    @ManyToOne
    @JoinColumn(name = OrganizationRoleType.PK_COLUMN_NAME)
    public OrganizationRoleType getType()
    {
        return type;
    }

    public void setType(final OrganizationRoleType type)
    {
        this.type = type;
    }

    @OneToMany(mappedBy = "primaryOrgRole", cascade = CascadeType.ALL)
    public List<OrganizationsRelationship> getPrimaryOrgRelationships()
    {
        return primaryOrgRelationships;
    }

    public void setPrimaryOrgRelationships(final List<OrganizationsRelationship> primaryOrgRelationships)
    {
        this.primaryOrgRelationships = primaryOrgRelationships;
    }

    @OneToMany(mappedBy = "secondaryOrgRole", cascade = CascadeType.ALL)
    public List<OrganizationsRelationship> getSecondaryOrgRelationships()
    {
        return secondaryOrgRelationships;
    }

    public void setSecondaryOrgRelationships(final List<OrganizationsRelationship> secondaryOrgRelationships)
    {
        this.secondaryOrgRelationships = secondaryOrgRelationships;
    }

    @Column(length = 128)
    public String getTypeDescription()
    {
        return typeDescription;
    }

    public void setTypeDescription(final String typeDescription)
    {
        this.typeDescription = typeDescription;
    }
}
