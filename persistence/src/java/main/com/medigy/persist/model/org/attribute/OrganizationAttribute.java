package com.medigy.persist.model.org.attribute;

import com.medigy.persist.model.common.attribute.EntityAttribute;
import com.medigy.persist.model.org.Organization;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Org_Attribute")
@Inheritance(strategy = InheritanceType.JOINED)
public class OrganizationAttribute extends EntityAttribute
{
    public static final String PK_COLUMN_NAME = "org_attr_id";
    private Organization organization;

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getOrganizationAttributeId()
    {
        return getAttributeId();
    }

    protected void setOrganizationAttributeId(final Long id)
    {
        setAttributeId(id);
    }

    @ManyToOne
    @JoinColumn(name = Organization.PK_COLUMN_NAME, nullable = false)
    public Organization getOrganization()
    {
        return organization;
    }

    public void setOrganization(final Organization organization)
    {
        this.organization = organization;
    }
}
