/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.model.person;

import com.medigy.persist.model.party.Party;
import com.medigy.persist.model.party.PartyRole;
import com.medigy.persist.reference.custom.party.PartyRoleType;
import com.medigy.persist.reference.custom.person.PersonRoleType;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

@Entity
public class PersonRole extends PartyRole
{
    public static final String PK_COLUMN_NAME = PartyRole.PK_COLUMN_NAME;

    private Person person;
    private PersonRoleType type;

    private List<PersonAndOrgRelationship> personOrgRelationships = new ArrayList<PersonAndOrgRelationship>();
    private List<PeopleRelationship> primaryPersonRelationships = new ArrayList<PeopleRelationship>();
    private List<PeopleRelationship> secondaryPersonRelationships = new ArrayList<PeopleRelationship>();

    @Transient
    public Party getParty()
    {
        return person;
    }

    @ManyToOne
    @JoinColumn(name = Person.PK_COLUMN_NAME)
    public Person getPerson()
    {
        return person;
    }

    public void setPerson(final Person person)
    {
        this.person = person;
    }

    @ManyToOne
    @JoinColumn(name = PersonRoleType.PK_COLUMN_NAME)
    public PersonRoleType getType()
    {
        return type;
    }

    public void setType(final PartyRoleType type)
    {
        this.type = (PersonRoleType) type;
    }

    @OneToMany(mappedBy = "personRole", cascade = CascadeType.ALL)
    public List<PersonAndOrgRelationship> getPersonOrgRelationships()
    {
        return personOrgRelationships;
    }

    public void setPersonOrgRelationships(final List<PersonAndOrgRelationship> personOrgRelationships)
    {
        this.personOrgRelationships = personOrgRelationships;
    }

    @Transient
    public void addPersonOrgRelationship(final PersonAndOrgRelationship rel)
    {
        this.personOrgRelationships.add(rel);
    }

    /**
     * Gets all the person-to-person relationships where this role is used as the PRIMARY role
     * (e.g. Financial Responsible person role is secondary and patient role is primary and
     * the relationship is Financial Responsible Party relationship)
     * @return list of relationships
     */
    @OneToMany(mappedBy = "primaryPersonRole", cascade = CascadeType.ALL)
    public List<PeopleRelationship> getPrimaryPersonRelationships()
    {
        return primaryPersonRelationships;
    }

    public void setPrimaryPersonRelationships(final List<PeopleRelationship> primaryPersonRelationships)
    {
        this.primaryPersonRelationships = primaryPersonRelationships;
    }

    /**
     * Gets all the person-to-person relationships where this role is used as the secondary role
     * (e.g. Financial Responsible person role is secondary and patient role is primary and
     * the relationship is Financial Responsible Party relationship)
     * @return list of relationships
     */
    @OneToMany(mappedBy = "secondaryPersonRole", cascade = CascadeType.ALL)
    public List<PeopleRelationship> getSecondaryPersonRelationships()
    {
        return secondaryPersonRelationships;
    }

    public void setSecondaryPersonRelationships(final List<PeopleRelationship> secondaryPersonRelationships)
    {
        this.secondaryPersonRelationships = secondaryPersonRelationships;
    }
}
