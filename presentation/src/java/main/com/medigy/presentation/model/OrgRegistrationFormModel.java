package com.medigy.presentation.model;

import com.medigy.service.ServiceVersion;
import com.medigy.service.dto.org.AddOrganization;
import com.medigy.service.dto.party.PostalAddressParameters;
import com.medigy.wicket.form.FieldCreator;
import com.medigy.wicket.form.FormFieldFactory;
import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.validator.NotNull;

import java.io.Serializable;


public final class OrgRegistrationFormModel implements AddOrganization
{
	private Long orgId;
	private String name;
	private String parentOrganizationName;
	private String parentOrganizationId;
    private String street1;
	private String street2;
	private String city;
	private String state;
	private String postalCode;
    private String province;
    private String county;
    private String country;
    private String postalAddressPurposeType;
    private String postalAddressPurposeDescription;
	private String email;
	private String phone;
	private String fax;
	private String websiteUrl;
    private ServiceVersion version;

    private AddOrganization params;


	public OrgRegistrationFormModel(AddOrganization params)
	{
        this.params = params;
        try
        {
            BeanUtils.copyProperties(this, params);
        }
        catch(Exception e)
        {
        }
	}

    @NotNull public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getParentOrganizationName()
    {
        return parentOrganizationName;
    }

    public void setParentOrganizationName(String parentOrganizationName)
    {
        this.parentOrganizationName = parentOrganizationName;
    }

    public Serializable getParentOrganizationId()
    {
        return parentOrganizationId;
    }

    public void setParentOrganizationId(String parentOrganizationId)
    {
        this.parentOrganizationId = parentOrganizationId;
    }

    @NotNull public PostalAddressParameters getMailingAddress()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @NotNull
    public String getStreet1()
    {
        return street1;
    }

    public void setStreet1(String street1)
    {
        this.street1= street1;
    }

    public String getStreet2()
    {
        return street2;
    }

    public void setStreet2(String street2)
    {
        this.street2 = street2;
    }

    @NotNull
    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    @NotNull
    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    @NotNull
    @FieldCreator(creator = FormFieldFactory.ZipCodeFieldCreator.class)
    public String getPostalCode()
    {
        return postalCode;
    }

    public void setPostalCode(String postalCode)
    {
        this.postalCode = postalCode;
    }

    public String getProvince()
    {
        return province;
    }

    public void setProvince(String province)
    {
        this.province = province;
    }

    public String getCounty()
    {
        return county;
    }

    public void setCounty(String county)
    {
        this.county= county;
    }

    @NotNull
    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public String getPostalAddressPurposeType()
    {
        return postalAddressPurposeType;
    }

    public void setPostalAddressPurposeType(String postalAddressPurposeType)
    {
        this.postalAddressPurposeType = postalAddressPurposeType;
    }

    public String getPostalAddressPurposeDescription()
    {
        return postalAddressPurposeDescription;
    }

    public void setPostalAddressPurposeDescription(String postalAddressPurposeDescription)
    {
        this.postalAddressPurposeDescription = postalAddressPurposeDescription;
    }

    @FieldCreator(creator = FormFieldFactory.EmailFieldCreator.class)
    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getFax()
    {
        return fax;
    }

    public void setFax(String fax)
    {
        this.fax = fax;
    }

    public String getWebsiteUrl()
    {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl)
    {
        this.websiteUrl = websiteUrl;
    }

    public ServiceVersion getServiceVersion()
    {
        return version;
    }

    public void setServiceVersion(ServiceVersion version)
    {
        this.version = version;
    }

    @NotNull public Long getOrgId()
    {
        return orgId;
    }

    public void setOrgId(Long id)
    {
        this.orgId = id;
    }


    public String toString()
	{
		StringBuffer b = new StringBuffer();
		b.append("[TestInputObject orgId = '").append(orgId)
		.append("name = '").append(name)
		.append("parentOrganizationName = '").append(parentOrganizationName)
		.append("parentOrganizationId = '").append(parentOrganizationId)
		.append("street1 = '").append(street1)
		.append("street2 = '").append(street2)
		.append("state = '").append(state)
		.append("postalCode = '").append(postalCode)
		.append("province = '").append(province)
		.append("county = '").append(county)
		.append("postalAddressPurposeType = '").append(postalAddressPurposeType)
		.append("postalAddressPurposeDescription = '").append(postalAddressPurposeDescription)
		.append("email = '").append(email)
		.append("websiteUrl = '").append(websiteUrl)
		.append("version = '").append(version)
	    ;
        
		b.append("]");
		return b.toString();
	}
}