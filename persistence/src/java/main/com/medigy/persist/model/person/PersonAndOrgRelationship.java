/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.model.person;

import com.medigy.persist.model.common.AbstractDateDurationEntity;
import com.medigy.persist.model.org.OrganizationRole;
import com.medigy.persist.reference.custom.party.PersonOrgRelationshipType;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Person_Org_Rel")
public class PersonAndOrgRelationship extends AbstractDateDurationEntity
{
    public static final String PK_COLUMN_NAME = "relationship_id";
    private Long relationshipId;

    private PersonRole personRole;
    private OrganizationRole organizationRole;
    private PersonOrgRelationshipType type;
    private String typeDescription;

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
    @JoinColumn(name = "person_role_id", referencedColumnName = PersonRole.PK_COLUMN_NAME, nullable = false)
    @NotNull
    public PersonRole getPersonRole()
    {
        return personRole;
    }

    public void setPersonRole(final PersonRole personRole)
    {
        this.personRole = personRole;
    }

    @ManyToOne
    @JoinColumn(name = "org_role_id", referencedColumnName = OrganizationRole.PK_COLUMN_NAME, nullable = false)
    @NotNull
    public OrganizationRole getOrganizationRole()
    {
        return organizationRole;
    }

    public void setOrganizationRole(final OrganizationRole organizationRole)
    {
        this.organizationRole = organizationRole;
    }

    @ManyToOne
    @JoinColumn(name = PersonOrgRelationshipType.PK_COLUMN_NAME, nullable = false)
    @NotNull
    public PersonOrgRelationshipType getType()
    {
        return type;
    }

    public void setType(final PersonOrgRelationshipType type)
    {
        this.type = type;
    }

    @Column(length  = 128, name = "type_description")
    public String getTypeDescription()
    {
        return typeDescription;
    }

    public void setTypeDescription(final String typeDescription)
    {
        this.typeDescription = typeDescription;
    }

}
