/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.model.person;

import com.medigy.persist.model.common.AbstractDateDurationEntity;
import com.medigy.persist.model.person.Person;
import com.medigy.persist.model.org.Organization;
import com.medigy.persist.reference.custom.party.PersonOrgRelationshipType;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratorType;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Column;

import org.hibernate.validator.NotNull;

@Entity
@Table(name = "Person_Org_Rel")
public class PersonAndOrgRelationship extends AbstractDateDurationEntity
{
    public static final String PK_COLUMN_NAME = "relationship_id";
    private Long relationshipId;

    private Person person;
    private Organization organization;
    private PersonOrgRelationshipType type;
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
    @JoinColumn(name = Person.PK_COLUMN_NAME, nullable = false)
    @NotNull
    public Person getPerson()
    {
        return person;
    }

    public void setPerson(final Person person)
    {
        this.person = person;
    }

    @ManyToOne
    @JoinColumn(name = Organization.PK_COLUMN_NAME, nullable = false)
    @NotNull
    public Organization getOrganization()
    {
        return organization;
    }

    public void setOrganization(final Organization organization)
    {
        this.organization = organization;
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

    @Column(length  = 128)
    public String getTypeDescription()
    {
        return typeDescription;
    }

    public void setTypeDescription(final String typeDescription)
    {
        this.typeDescription = typeDescription;
    }

}
