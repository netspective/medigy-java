package com.medigy.persist.model.party;

import com.medigy.persist.reference.type.ContactMechanismType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Transient;

/**
 * Class for Phone contact mechanism entity.
 */
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class PhoneNumber extends ContactMechanism
{

    private String countryCode;
    private String cityCode;

    private String areaCode;
    private String numberValue;
    private String extension;

    public PhoneNumber()
    {
        this.type = ContactMechanismType.Cache.PHONE.getEntity();
    }

    @Transient
    public Long getPhoneNumberId()
    {
        return super.getContactMechanismId();
    }

    protected void setPhoneNumberId(final Long phoneNumberId)
    {
        setContactMechanismId(phoneNumberId);
    }

    /**
     * Gets the country code
     * @return country code
     */
    @Column(length = 5)
    public String getCountryCode()
    {
        return countryCode;
    }

    public void setCountryCode(final String countryCode)
    {
        this.countryCode = countryCode;
    }

    /**
     * Gets the city code
     * @return city code
     */
    @Column(length = 3, name = "city_code")
    public String getCityCode()
    {
        return cityCode;
    }

    public void setCityCode(final String cityCode)
    {
        this.cityCode = cityCode;
    }

    /**
     * Gets the area code
     * @return area code
     */
    @Column(length = 5, name = "area_code")
    public String getAreaCode()
    {
        return areaCode;
    }

    public void setAreaCode(final String areaCode)
    {
        this.areaCode = areaCode;
    }

    /**
     * Gets the seven digit phone number
     * @return  phone number
     */
    @Column(length = 7, nullable = false, name = "number_value")
    public String getNumberValue()
    {
        return numberValue;
    }

    public void setNumberValue(final String numberValue)
    {
        this.numberValue = numberValue;
    }

    /**
     * Gets the phone extension
     * @return office extension
     */
    @Column(length = 5)
    public String getExtension()
    {
        return extension;
    }

    public void setExtension(final String extension)
    {
        this.extension = extension;
    }
}
