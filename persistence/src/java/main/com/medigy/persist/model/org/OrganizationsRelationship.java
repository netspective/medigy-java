/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.model.org;

import com.medigy.persist.model.common.AbstractDateDurationEntity;
import com.medigy.persist.reference.type.PriorityType;
import com.medigy.persist.reference.custom.party.OrganizationsRelationshipType;

import javax.persistence.Id;
import javax.persistence.GeneratorType;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.NotNull;

@Entity
@Table(name = "Org_Rel")
public class OrganizationsRelationship extends AbstractDateDurationEntity
{
    public static final String PK_COLUMN_NAME = "relationship_id";

    private Long relationshipId;
    private OrganizationRole primaryOrgRole;
    private OrganizationRole secondaryOrgRole;
    private OrganizationsRelationshipType type;
    private String typeDescription;
    private PriorityType priorityType;

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getRelationshipId()
    {
        return relationshipId;
    }

    public void setRelationshipId(final Long relationshipId)
    {
        this.relationshipId = relationshipId;
    }

    @ManyToOne
    @JoinColumn(name = "prim_org_role_id", referencedColumnName = OrganizationRole.PK_COLUMN_NAME, nullable = false)
    @NotNull
    public OrganizationRole getPrimaryOrgRole()
    {
        return primaryOrgRole;
    }

    public void setPrimaryOrgRole(final OrganizationRole primaryOrgRole)
    {
        this.primaryOrgRole = primaryOrgRole;
    }

    @ManyToOne
    @JoinColumn(name = "sec_org_role_id", referencedColumnName = OrganizationRole.PK_COLUMN_NAME, nullable = false)
    @NotNull
    public OrganizationRole getSecondaryOrgRole()
    {
        return secondaryOrgRole;
    }

    public void setSecondaryOrgRole(final OrganizationRole secondaryOrgRole)
    {
        this.secondaryOrgRole = secondaryOrgRole;
    }

    @ManyToOne
    @JoinColumn(name = OrganizationsRelationshipType.PK_COLUMN_NAME, nullable = false)
    @NotNull
    public OrganizationsRelationshipType getType()
    {
        return type;
    }

    public void setType(final OrganizationsRelationshipType type)
    {
        this.type = type;
    }

    /**
     * Used when the default types do not suffice
     * @return type description
     */
    @Column(length = 128)
    public String getTypeDescription()
    {
        return typeDescription;
    }

    public void setTypeDescription(final String typeDescription)
    {
        this.typeDescription = typeDescription;
    }

    @ManyToOne
    @JoinColumn(name = "priority_type", referencedColumnName = PriorityType.PK_COLUMN_NAME)
    public PriorityType getPriorityType()
    {
        return priorityType;
    }

    public void setPriorityType(final PriorityType priorityType)
    {
        this.priorityType = priorityType;
    }
}
