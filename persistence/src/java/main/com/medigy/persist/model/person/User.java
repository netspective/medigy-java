/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.model.person;

import com.medigy.persist.model.common.AbstractTopLevelEntity;
import com.medigy.persist.model.org.Organization;
import org.hibernate.validator.Min;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class User extends AbstractTopLevelEntity
{
    private Long userId;

    private String userName;
    private String password;
    private Long quantity;
    private Person person;
    private Organization organization;

    // TODO: Need to discuss if  USER should be associated to Person and Org thorugh the person's roles
    private PersonRole personRole;

    @Id(generate = GeneratorType.AUTO)
    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(final Long userId)
    {
        this.userId = userId;
    }

    @Column(nullable = false, length = 16)
    @Min(value = 5)
    public String getUserName()
    {
        return userName;
    }

    public void setUserName(final String userName)
    {
        this.userName = userName;
    }

    @Column(nullable = false, length = 16)
    @Min(value = 8)
    public String getPassword()
    {
        return password;
    }

    public void setPassword(final String password)
    {
        this.password = password;
    }

    /**
     * Describes how many times the user is allowed to login
     * @return The number of times login is allowed from different sessions
     */
    public Long getQuantity()
    {
        return quantity;
    }

    public void setQuantity(final Long quantity)
    {
        this.quantity = quantity;
    }

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = Person.PK_COLUMN_NAME, nullable = false)
    public Person getPerson()
    {
        return person;
    }

    public void setPerson(final Person person)
    {
        this.person = person;
    }

    @ManyToOne
    @JoinColumn(name = "org_id", referencedColumnName = Organization.PK_COLUMN_NAME, nullable = false)
    public Organization getOrganization()
    {
        return organization;
    }

    public void setOrganization(final Organization organization)
    {
        this.organization = organization;
    }


}
