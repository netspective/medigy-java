/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.presentation.model;

import com.medigy.persist.reference.custom.insurance.InsuranceSequenceType;
import com.medigy.persist.reference.custom.person.EthnicityType;
import com.medigy.persist.reference.custom.person.PatientResponsiblePartyRoleType;
import com.medigy.persist.reference.type.*;
import com.medigy.service.ServiceVersion;
import com.medigy.service.dto.person.RegisterPatientParameters;
import com.medigy.service.validator.ValidEntity;
import com.medigy.wicket.form.FieldCreator;
import com.medigy.wicket.form.FormFieldFactory.*;
import com.medigy.wicket.form.SelectFieldStyle;

import java.util.Date;


public final class PatientRegistrationFormModel implements RegisterPatientParameters
{
	private String firstName;
    private String lastName;
	private String middleName;
    private String suffix;
    private Date birthDate;
    private String genderCode;
    private String maritalStatusCode;
	private String ssn;
    private String driversLicenseNumber;
	private String driversLicenseStateCode;
    private String employerName;
    private String employerId;
    private String occupation;
    private String[] ethnicityCodes;
    private String[] languageCodes;
	private String personId;
	private String account;
	private String chartNumber;
	private String responsiblePartyId;
	private String responsiblePartyRole;
	private String miscNotes;
	private String homePhoneNumber;
	private String workPhoneNumber;
	private String homePhone;
	private String workPhone;
	private String mobilePhone;
	private String pager;
	private String alternate;
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
	private String employerPhoneNumber;
	private String primaryInsuranceProductId;
	private String primaryInsurancePlanId;
	private String patientRelationshipToInsuredOtherRelationship;
	private String primaryInsurancePolicyTypeCode;
	private String primaryInsuranceGroupNumber;
	private String primaryInsurancePolicyNumber;
	private Date primaryInsuranceCoverageStartDate;
	private Date primaryInsuranceCoverageEndDate;
	private Float primaryInsuranceIndividualDeductibleAmount;
	private Float primaryInsuranceFamilyDeductibleAmount;
	private String individualDeductibleRemaining;   // TODO: this field is not in the RegisterPatientParameters interface. Ask Aye if it is needed
	private String familyDeductibleRemaining;       // TODO: this field is not in the RegisterPatientParameters interface. Ask Aye if it is needed
	private Float primaryInsurancePercentagePay;
	private Float primaryInsuranceMaxThresholdAmount;
	private Float primaryInsuranceOfficeVisitCoPay;
	private String relationshipToResponsible;
	private String employmentStatus;
	private String insuranceSequence;
    private String primaryInsuranceContractHolderId;
	private String primaryInsuranceContractHolderRole;
	private String bloodType;
    private String primaryCareProviderId;
    private String primaryInsuranceCarrierId;
    private ServiceVersion version;

    private RegisterPatientParameters params;


	public PatientRegistrationFormModel(RegisterPatientParameters params)
	{
        this.params = params;

        // initialize bean from param data
	}

    public ServiceVersion getServiceVersion()
    {
        return version;
    }

    public void setServiceVersion(ServiceVersion version)
    {
        this.version = version;
    }

	public String getPersonId()
	{
		return personId;
	}

	public void setPersonId(String personId)
	{
		this.personId = personId;
	}

	public String getAccount()
	{
		return account;
	}

	public void setAccount(String account)
	{
		this.account = account;
	}

	public String getChartNumber()
	{
		return chartNumber;
	}

	public void setChartNumber(String chartNumber)
	{
		this.chartNumber = chartNumber;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getMiddleName()
	{
		return middleName;
	}

	public void setMiddleName(String middleName)
	{
		this.middleName = middleName;
	}

    @ValidEntity(entity = PersonNamePrefixType.class)
    @SelectFieldStyle(style = SelectFieldStyle.Style.COMBO)
    public String getSuffix()
    {
        return suffix;
    }

    public void setSuffix(String suffix)
    {
        this.suffix = suffix;
    }

    @FieldCreator(creator = SocialSecurityFieldCreator.class)
	public String getSsn()
	{
		return ssn;
	}

	public void setSsn(String ssn)
	{
		this.ssn = ssn;
	}

	public Date getBirthDate()
	{
		return birthDate;
	}

	public void setBirthDate(Date birthDate)
	{
		this.birthDate = birthDate;
	}

    public String getResponsiblePartyId()
	{
		return responsiblePartyId;
	}

	public void setResponsiblePartyId(String responsiblePartyId)
	{
		this.responsiblePartyId = responsiblePartyId;
	}

    public String getResponsiblePartyRole()
	{
		return responsiblePartyRole;
	}

    public void setResponsiblePartyRole(String responsiblePartyRole)
	{
		this.responsiblePartyRole = responsiblePartyRole;
	}

    public String getDriversLicenseNumber()
	{
		return driversLicenseNumber;
	}

	public void setDriversLicenseNumber(String driversLicenseNumber)
	{
		this.driversLicenseNumber = driversLicenseNumber;
	}

    public String getDriversLicenseStateCode()
	{
		return driversLicenseStateCode;
	}

	public void setDriversLicenseStateCode(String driversLicenseStateCode)
	{
		this.driversLicenseStateCode = driversLicenseStateCode;
	}

    @FieldCreator(creator = MemoFieldCreator.class)
    public String getMiscNotes()
	{
		return miscNotes;
	}

	public void setMiscNotes(String miscNotes)
	{
		this.miscNotes = miscNotes;
	}

    @FieldCreator(creator = PhoneFieldCreator.class)
    public String getHomePhoneNumber()
	{
		return homePhoneNumber;
	}

	public void setHomePhoneNumber(String homePhoneNumber)
	{
		this.homePhoneNumber = homePhoneNumber;
	}

    @FieldCreator(creator = PhoneFieldCreator.class)
    public String getWorkPhoneNumber()
	{
		return workPhoneNumber;
	}

	public void setWorkPhoneNumber(String workPhoneNumber)
	{
		this.workPhoneNumber = workPhoneNumber;
	}

    @FieldCreator(creator = PhoneFieldCreator.class)
    public String getHomePhone()
	{
		return homePhone;
	}

	public void setHomePhone(String homePhone)
	{
		this.homePhone = homePhone;
	}

    @FieldCreator(creator = PhoneFieldCreator.class)
    public String getWorkPhone()
    {
        return workPhone;
    }

    public void setWorkPhone(String workPhone)
    {
        this.workPhone = workPhone;
    }

    @FieldCreator(creator = PhoneFieldCreator.class)
    public String getMobilePhone()
    {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone)
    {
        this.mobilePhone = mobilePhone;
    }

     @FieldCreator(creator = PhoneFieldCreator.class)
    public String getPager()
    {
        return pager;
    }

    public void setPager(String pager)
    {
        this.pager = pager;
    }

    public String getAlternate()
    {
        return alternate;
    }

    public void setAlternate(String alternate)
    {
        this.alternate = alternate;
    }

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

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    @FieldCreator(creator = ZipCodeFieldCreator.class)
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

    @FieldCreator(creator = EmailFieldCreator.class)
    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getEmployerId()
    {
        return employerId;
    }

    public void setEmployerId(String employerId)
    {
        this.employerId = employerId;
    }

    public String getOccupation()
    {
        return occupation;
    }

    public void setOccupation(String occupation)
    {
        this.occupation = occupation;
    }

    public String getEmployerPhoneNumber()
    {
        return employerPhoneNumber;
    }

    public void setEmployerPhoneNumber(String employerPhoneNumber)
    {
        this.employerPhoneNumber = employerPhoneNumber;
    }

    public String getPrimaryInsuranceProductId()
    {
        return primaryInsuranceProductId;
    }

    public void setPrimaryInsuranceProductId(String primaryInsuranceProductId)
    {
        this.primaryInsuranceProductId = primaryInsuranceProductId;
    }

    public String getPrimaryInsurancePlanId()
    {
        return primaryInsurancePlanId;
    }

    public void setPrimaryInsurancePlanId(String primaryInsurancePlanId)
    {
        this.primaryInsurancePlanId = primaryInsurancePlanId;
    }

    public String getPatientRelationshipToInsuredOtherRelationship()
    {
        return patientRelationshipToInsuredOtherRelationship;
    }

    public void setPatientRelationshipToInsuredOtherRelationship(String patientRelationshipToInsuredOtherRelationship)
    {
        this.patientRelationshipToInsuredOtherRelationship = patientRelationshipToInsuredOtherRelationship;
    }

    public String getPrimaryInsuranceContractHolderId()
    {
        return primaryInsuranceContractHolderId;
    }

    public void setPrimaryInsuranceContractHolderId(String primaryInsuranceContractHolderId)
    {
        this.primaryInsuranceContractHolderId = primaryInsuranceContractHolderId;
    }

    public String getEmployerName()
    {
        return employerName;
    }

    public void setEmployerName(String employerName)
    {
        this.employerName = employerName;
    }

    public String getPrimaryInsurancePolicyTypeCode()
    {
        return primaryInsurancePolicyTypeCode;
    }

    public void setPrimaryInsurancePolicyTypeCode(String primaryInsurancePolicyTypeCode)
    {
        this.primaryInsurancePolicyTypeCode = primaryInsurancePolicyTypeCode;
    }

    public String getPrimaryInsuranceGroupNumber()
    {
        return primaryInsuranceGroupNumber;
    }

    public void setPrimaryInsuranceGroupNumber(String primaryInsuranceGroupNumber)
    {
        this.primaryInsuranceGroupNumber = primaryInsuranceGroupNumber;
    }

    public String getPrimaryInsurancePolicyNumber()
    {
        return primaryInsurancePolicyNumber;
    }

    public void setPrimaryInsurancePolicyNumber(String primaryInsurancePolicyNumber)
    {
        this.primaryInsurancePolicyNumber = primaryInsurancePolicyNumber;
    }

    public Date getPrimaryInsuranceCoverageStartDate()
    {
        return primaryInsuranceCoverageStartDate;
    }

    public void setPrimaryInsuranceCoverageStartDate(Date primaryInsuranceCoverageStartDate)
    {
        this.primaryInsuranceCoverageStartDate = primaryInsuranceCoverageStartDate;
    }

    public Date getPrimaryInsuranceCoverageEndDate()
    {
        return primaryInsuranceCoverageEndDate;
    }

    public void setPrimaryInsuranceCoverageEndDate(Date primaryInsuranceCoverageEndDate)
    {
        this.primaryInsuranceCoverageEndDate = primaryInsuranceCoverageEndDate;
    }

    public Float getPrimaryInsuranceIndividualDeductibleAmount()
    {
        return primaryInsuranceIndividualDeductibleAmount;
    }

    public void setPrimaryInsuranceIndividualDeductibleAmount(Float primaryInsuranceIndividualDeductibleAmount)
    {
        this.primaryInsuranceIndividualDeductibleAmount = primaryInsuranceIndividualDeductibleAmount;
    }

    public Float getPrimaryInsuranceFamilyDeductibleAmount()
    {
        return primaryInsuranceFamilyDeductibleAmount;
    }

    public void setPrimaryInsuranceFamilyDeductibleAmount(Float primaryInsuranceFamilyDeductibleAmount)
    {
        this.primaryInsuranceFamilyDeductibleAmount = primaryInsuranceFamilyDeductibleAmount;
    }

    public String getIndividualDeductibleRemaining()
    {
        return individualDeductibleRemaining;
    }

    public void setIndividualDeductibleRemaining(String individualDeductibleRemaining)
    {
        this.individualDeductibleRemaining = individualDeductibleRemaining;
    }

    public String getFamilyDeductibleRemaining()
    {
        return familyDeductibleRemaining;
    }

    public void setFamilyDeductibleRemaining(String familyDeductibleRemaining)
    {
        this.familyDeductibleRemaining = familyDeductibleRemaining;
    }

    public Float getPrimaryInsurancePercentagePay()
    {
        return primaryInsurancePercentagePay;
    }

    public void setPrimaryInsurancePercentagePay(Float primaryInsurancePercentagePay)
    {
        this.primaryInsurancePercentagePay = primaryInsurancePercentagePay;
    }

    public Float getPrimaryInsuranceMaxThresholdAmount()
    {
        return primaryInsuranceMaxThresholdAmount;
    }

    public void setPrimaryInsuranceMaxThresholdAmount(Float primaryInsuranceMaxThresholdAmount)
    {
        this.primaryInsuranceMaxThresholdAmount = primaryInsuranceMaxThresholdAmount;
    }

    public Float getPrimaryInsuranceOfficeVisitCoPay()
    {
        return primaryInsuranceOfficeVisitCoPay;
    }

    public void setPrimaryInsuranceOfficeVisitCoPay(Float primaryInsuranceOfficeVisitCoPay)
    {
        this.primaryInsuranceOfficeVisitCoPay = primaryInsuranceOfficeVisitCoPay;
    }

    @ValidEntity(entity = GenderType.class)
    @SelectFieldStyle(style = SelectFieldStyle.Style.COMBO)
    public String getGenderCode()
    {
        return genderCode;
    }

    public void setGenderCode(String genderCode)
    {
        this.genderCode = genderCode;
    }

    @ValidEntity(entity = MaritalStatusType.class)
    @SelectFieldStyle(style = SelectFieldStyle.Style.COMBO)
    public String getMaritalStatusCode()
    {
        return maritalStatusCode;
    }

    public void setMaritalStatusCode(String maritalStatusCode)
    {
        this.maritalStatusCode = maritalStatusCode;
    }

    @ValidEntity(entity = PatientResponsiblePartyRoleType.class)
    @SelectFieldStyle(style = SelectFieldStyle.Style.COMBO)
    public String getRelationshipToResponsible()
    {
        return relationshipToResponsible;
    }

    public void setRelationshipToResponsible(String relationshipToResponsible)
    {
        this.relationshipToResponsible = relationshipToResponsible;
    }

    @ValidEntity(entity = EmploymentStatusType.class)
    @SelectFieldStyle(style = SelectFieldStyle.Style.COMBO)
    public String getEmploymentStatus()
    {
        return employmentStatus;
    }

    public void setEmploymentStatus(String employmentStatus)
    {
        this.employmentStatus = employmentStatus;
    }

    @ValidEntity(entity = InsuranceSequenceType.class)
    @SelectFieldStyle(style = SelectFieldStyle.Style.COMBO)
    public String getInsuranceSequence()
    {
        return insuranceSequence;
    }

    public void setInsuranceSequence(String insuranceSequence)
    {
        this.insuranceSequence = insuranceSequence;
    }

    @ValidEntity(entity = PatientResponsiblePartyRoleType.class)
    @SelectFieldStyle(style = SelectFieldStyle.Style.COMBO)
    public String getPrimaryInsuranceContractHolderRole()
    {
        return primaryInsuranceContractHolderRole;
    }

    public void setPrimaryInsuranceContractHolderRole(String primaryInsuranceContractHolderRole)
    {
        this.primaryInsuranceContractHolderRole = primaryInsuranceContractHolderRole;
    }

    @ValidEntity(entity = BloodType.class)
    @SelectFieldStyle(style = SelectFieldStyle.Style.COMBO)
    public String getBloodType()
    {
        return bloodType;
    }

    public void setBloodType(String bloodType)
    {
        this.bloodType = bloodType;
    }

    @ValidEntity(entity = EthnicityType.class)
    @SelectFieldStyle(style = SelectFieldStyle.Style.RADIO)
    public String[] getEthnicityCodes()
    {
        return ethnicityCodes;
    }

    public void setEthnicityCodes(String[] ethnicityCodes)
    {
        this.ethnicityCodes = ethnicityCodes;
    }

    @ValidEntity(entity = LanguageType.class)
    @SelectFieldStyle(style = SelectFieldStyle.Style.RADIO)
    public String[] getLanguageCodes()
    {
        return languageCodes;
    }

    public void setLanguageCodes(String[] languageCodes)
    {
        this.languageCodes = languageCodes;
    }

    public String getPrimaryCareProviderId()
    {
        return primaryCareProviderId;
    }

    public void setPrimaryCareProviderId(String primaryCareProviderId)
    {
        this.primaryCareProviderId = primaryCareProviderId;
    }

    public String getPrimaryInsuranceCarrierId()
    {
        return primaryInsuranceCarrierId;
    }

    public void setPrimaryInsuranceCarrierId(String primaryInsuranceCarrierId)
    {
        this.primaryInsuranceCarrierId = primaryInsuranceCarrierId;
    }

    public String toString()
	{
		StringBuffer b = new StringBuffer();
		b.append("[TestInputObject personId = '").append(personId)
		 .append("', account = ").append(account)
		 .append(", chartNumber = ").append(chartNumber)
		 .append(", lastName = ").append(lastName)
		 .append(", firstName = ").append(firstName)
		 .append(", middleName = ").append(middleName)
		 .append(", birthDate = ").append(birthDate)
		 .append(", ssn = ").append(ssn)
		 .append(", responsiblePartyId = ").append(responsiblePartyId)
		 .append(", responsiblePartyRole = ").append(responsiblePartyRole)
		 .append(", driversLicenseNumber = ").append(driversLicenseNumber)
		 .append(", driversLicenseStateCode = ").append(driversLicenseStateCode)
		 .append(", miscNotes = ").append(miscNotes)
		 .append(", homePhoneNumber = ").append(homePhoneNumber)
		 .append(", workPhone = ").append(workPhone)
		 .append(", homePhone = ").append(homePhone)
		 .append(", workPhoneNumber = ").append(workPhoneNumber)
		 .append(", mobilePhone = ").append(mobilePhone)
		 .append(", pager = ").append(pager)
		 .append(", alternate = ").append(alternate)
		 .append(", street1 = ").append(street1)
		 .append(", street2 = ").append(street2)
		 .append(", city = ").append(city)
		 .append(", state = ").append(state)
		 .append(", postalCode = ").append(postalCode)
		 .append(", province = ").append(province)
		 .append(", county = ").append(county)
		 .append(", country = ").append(country)
		 .append(", postalAddressPurposeType = ").append(postalAddressPurposeType)
		 .append(", postalAddressPurposeDescription = ").append(postalAddressPurposeDescription)
		 .append(", email = ").append(email)
		 .append(", employerId = ").append(employerId)
		 .append(", occupation = ").append(occupation)
		 .append(", employerPhoneNumber = ").append(employerPhoneNumber)
		 .append(", primaryInsuranceProductId = ").append(primaryInsuranceProductId)
		 .append(", primaryInsurancePlanId = ").append(primaryInsurancePlanId)
		 .append(", patientRelationshipToInsuredOtherRelationship = ").append(patientRelationshipToInsuredOtherRelationship)
		 .append(", primaryInsuranceContractHolderId = ").append(primaryInsuranceContractHolderId)
		 .append(", employerName = ").append(employerName)
		 .append(", primaryInsurancePolicyTypeCode = ").append(primaryInsurancePolicyTypeCode)
		 .append(", primaryInsuranceGroupNumber = ").append(primaryInsuranceGroupNumber)
		 .append(", primaryInsurancePolicyNumber = ").append(primaryInsurancePolicyNumber)
		 .append(", primaryInsuranceCoverageStartDate = ").append(primaryInsuranceCoverageStartDate)
		 .append(", primaryInsuranceCoverageEndDate = ").append(primaryInsuranceCoverageEndDate)
		 .append(", primaryInsuranceIndividualDeductibleAmount = ").append(primaryInsuranceIndividualDeductibleAmount)
		 .append(", primaryInsuranceFamilyDeductibleAmount = ").append(primaryInsuranceFamilyDeductibleAmount)
		 .append(", individualDeductibleRemaining = ").append(individualDeductibleRemaining)
		 .append(", familyDeductibleRemaining = ").append(familyDeductibleRemaining)
		 .append(", primaryInsurancePercentagePay = ").append(primaryInsurancePercentagePay)
		 .append(", primaryInsuranceMaxThresholdAmount = ").append(primaryInsuranceMaxThresholdAmount)
		 .append(", primaryInsuranceOfficeVisitCoPay = ").append(primaryInsuranceOfficeVisitCoPay)
		 .append(", suffix = ").append(suffix)
		 .append(", genderCode = ").append(genderCode)
		 .append(", maritalStatusCode = ").append(maritalStatusCode)
		 .append(", relationshipToResponsible = ").append(relationshipToResponsible)
		 .append(", employmentStatus = ").append(employmentStatus)
		 .append(", insuranceSequence = ").append(insuranceSequence)
		 .append(", primaryInsuranceContractHolderRole = ").append(primaryInsuranceContractHolderRole)
		 .append(", bloodType = ").append(bloodType)
		 .append(", ethnicityCodes = ").append(ethnicityCodes)
		 .append(", languageCodes = ").append(languageCodes)
		 .append(", primaryCareProviderId = ").append(primaryCareProviderId)
		 .append(", primaryInsuranceCarrierId = ").append(primaryInsuranceCarrierId);

		b.append("]");
		return b.toString();
	}
}