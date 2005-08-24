/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.model.person;

import com.medigy.persist.model.common.AbstractDateDurationEntity;
import com.medigy.persist.reference.custom.party.PeopleRelationshipType;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class PeopleRelationship extends AbstractDateDurationEntity
{
    public static final String PK_COLUMN_NAME = "relationship_id";
    private Long relationshipId;

    private PersonRole primaryPersonRole;
    private PersonRole secondaryPersonRole;
    private PeopleRelationshipType type;
    private String typeDescription;

    @Id(generate = GeneratorType.AUTO)
    public Long getRelationshipId()
    {
        return relationshipId;
    }

    public void setRelationshipId(final Long relationshipId)
    {
        this.relationshipId = relationshipId;
    }

    @ManyToOne
    @JoinColumn(name = "prim_person_role_id", referencedColumnName = PersonRole.PK_COLUMN_NAME, nullable = false)
    @NotNull
    public PersonRole getPrimaryPersonRole()
    {
        return primaryPersonRole;
    }

    public void setPrimaryPersonRole(final PersonRole primaryPersonRole)
    {
        this.primaryPersonRole = primaryPersonRole;
    }

    @ManyToOne
    @JoinColumn(name = "sec_person_role_id", referencedColumnName = PersonRole.PK_COLUMN_NAME, nullable = false)
    @NotNull
    public PersonRole getSecondaryPersonRole()
    {
        return secondaryPersonRole;
    }

    public void setSecondaryPersonRole(final PersonRole secondaryPersonRole)
    {
        this.secondaryPersonRole = secondaryPersonRole;
    }

    @ManyToOne
    @JoinColumn(name = PeopleRelationshipType.PK_COLUMN_NAME, nullable = false)
    @NotNull
    public PeopleRelationshipType getType()
    {
        return type;
    }

    public void setType(final PeopleRelationshipType type)
    {
        this.type = type;
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
