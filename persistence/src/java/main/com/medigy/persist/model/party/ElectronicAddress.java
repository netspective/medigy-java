package com.medigy.persist.model.party;

import com.medigy.persist.reference.type.ContactMechanismType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Transient;

/**
 * Contact mechanism class for electronic addresses such as email and urls.
 */
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class ElectronicAddress extends ContactMechanism
{
    private String electronicAddress;

    public ElectronicAddress()
    {
        this.type = ContactMechanismType.Cache.EMAIL_ADDRESS.getEntity();
    }

    @Transient
    public Long getElectronicAddressId()
    {
        return getContactMechanismId();
    }

    protected void setElectronicAddressId(final Long electronicAddressId)
    {
        setContactMechanismId(electronicAddressId);
    }

    @Column(length = 256, name = "electronic_address", nullable = false)
    public String getElectronicAddress()
    {
        return electronicAddress;
    }

    public void setElectronicAddress(final String electronicAddress)
    {
        this.electronicAddress = electronicAddress;
    }
}
