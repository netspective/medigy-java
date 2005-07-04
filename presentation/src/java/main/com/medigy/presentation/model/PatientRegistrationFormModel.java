/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.presentation.model;

import com.medigy.persist.reference.custom.insurance.InsuranceSequenceType;
import com.medigy.persist.reference.custom.person.EthnicityType;
import com.medigy.persist.reference.custom.person.PatientResponsiblePartyRoleType;
import com.medigy.persist.reference.type.*;
import com.medigy.service.validator.ValidEntity;
import com.medigy.wicket.form.*;
import org.hibernate.validator.NotNull;

import java.io.Serializable;
import java.util.Date;

/**
 * Simple model object for FormInput example. Has a number of simple properties
 * that can be retrieved and set.
 */
public final class PatientRegistrationFormModel implements Serializable
{
	private String firstName;
    private String lastName;
	private String middleName;
    private String suffix;
    private Date dateOfBirth;
    private String gender;     // TODO: change to xxxCode
    private String maritalStatus;   // TODO: change to xxxCode
	private String socialSecurityNumber;
    private String driversLicenseNumber;
	private String driversLicenseState;   // TODO: change to xxxCode
    private String employer;                // TODO: change to employerName
    private String employerId;
    private String occupation;
    private String ethnicity;     // TODO: multiple choice - change to xxxCodes array
    private String languageCodes; // TODO: this one is not on the form - add?

	private String personId;
	private String account;
	private String chartNumber;
	private String responsibleParty;
	private String relationshipToResponsibleOtherRelationship;
	private String miscNotes;
	private String homePhone;
	private String workPhone;
	private String homePhone2;
	private String workPhone2;
	private String cellPhone;
	private String pager;
	private String alternate;
	private String homeAddress;
	private String homeAddress2;
	private String city;
	private String state;
	private String zip;
	private String email;
	private String employerPhoneNumber;
	private String insuranceProduct;
	private String insurancePlan;
	private String patientRelationshipToInsuredPtherRelationship;
	private String insuredPersonId;
	private String groupName;
	private String groupNumber;
	private String memberNumber;
	private String coverageBeginDate;
	private String coverageEndDate;
	private String individualDeductible;
	private String familyDeductible;
	private String individualDeductibleRemaining;
	private String familyDeductibleRemaining;
	private String percentagePay;
	private String threshold;
	private String officeVisitCoPay;
	private String relationshipToResponsible;
	private String employmentStatus;
	private String insuranceSequence;
	private String patientRelationshipToInsured;
	private String bloodType;


	public PatientRegistrationFormModel()
	{
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

    @NotNull
	public String getLastName()
	{
		return lastName;
	}

    @NotNull
	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

    @NotNull
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

    @FormFieldType(type = SocialSecurityField.class)
	public String getSocialSecurityNumber()
	{
		return socialSecurityNumber;
	}

	public void setSocialSecurityNumber(String socialSecurityNumber)
	{
		this.socialSecurityNumber = socialSecurityNumber;
	}

	public Date getDateOfBirth()
	{
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth)
	{
		this.dateOfBirth = dateOfBirth;
	}

    public String getResponsibleParty()
	{
		return responsibleParty;
	}

	public void setResponsibleParty(String responsibleParty)
	{
		this.responsibleParty = responsibleParty;
	}

    public String getRelationshipToResponsibleOtherRelationship()
	{
		return relationshipToResponsibleOtherRelationship;
	}

	public void setRelationshipToResponsibleOtherRelationship(String relationshipToResponsibleOtherRelationship)
	{
		this.relationshipToResponsibleOtherRelationship = relationshipToResponsibleOtherRelationship;
	}

    public String getDriversLicenseNumber()
	{
		return driversLicenseNumber;
	}

	public void setDriversLicenseNumber(String driversLicenseNumber)
	{
		this.driversLicenseNumber = driversLicenseNumber;
	}

    public String getDriversLicenseState()
	{
		return driversLicenseState;
	}

	public void setDriversLicenseState(String driversLicenseState)
	{
		this.driversLicenseState = driversLicenseState;
	}

    public String getMiscNotes()
	{
		return miscNotes;
	}

	public void setMiscNotes(String miscNotes)
	{
		this.miscNotes = miscNotes;
	}

    @FormFieldType(type = PhoneField.class)
    public String getHomePhone()
	{
		return homePhone;
	}

	public void setHomePhone(String homePhone)
	{
		this.homePhone = homePhone;
	}

    @FormFieldType(type = PhoneField.class)
    public String getWorkPhone()
	{
		return workPhone;
	}

	public void setWorkPhone(String workPhone)
	{
		this.workPhone = workPhone;
	}

    @FormFieldType(type = PhoneField.class)
    public String getHomePhone2()
	{
		return homePhone2;
	}

	public void setHomePhone2(String homePhone2)
	{
		this.homePhone2 = homePhone2;
	}

    @FormFieldType(type = PhoneField.class)
    public String getWorkPhone2()
    {
        return workPhone2;
    }

    public void setWorkPhone2(String workPhone2)
    {
        this.workPhone2 = workPhone2;
    }

    @FormFieldType(type = PhoneField.class)
    public String getCellPhone()
    {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone)
    {
        this.cellPhone = cellPhone;
    }

    @FormFieldType(type = PhoneField.class)
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

    public String getHomeAddress()
    {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress)
    {
        this.homeAddress = homeAddress;
    }

    public String getHomeAddress2()
    {
        return homeAddress2;
    }

    public void setHomeAddress2(String homeAddress2)
    {
        this.homeAddress2 = homeAddress2;
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

    @FormFieldType(type = ZipCodeField.class)
    public String getZip()
    {
        return zip;
    }

    public void setZip(String zip)
    {
        this.zip = zip;
    }

    @FormFieldType(type = EmailField.class)
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

    public String getInsuranceProduct()
    {
        return insuranceProduct;
    }

    public void setInsuranceProduct(String insuranceProduct)
    {
        this.insuranceProduct = insuranceProduct;
    }

    public String getInsurancePlan()
    {
        return insurancePlan;
    }

    public void setInsurancePlan(String insurancePlan)
    {
        this.insurancePlan = insurancePlan;
    }

    public String getPatientRelationshipToInsuredPtherRelationship()
    {
        return patientRelationshipToInsuredPtherRelationship;
    }

    public void setPatientRelationshipToInsuredPtherRelationship(String patientRelationshipToInsuredPtherRelationship)
    {
        this.patientRelationshipToInsuredPtherRelationship = patientRelationshipToInsuredPtherRelationship;
    }

    public String getInsuredPersonId()
    {
        return insuredPersonId;
    }

    public void setInsuredPersonId(String insuredPersonId)
    {
        this.insuredPersonId = insuredPersonId;
    }

    public String getEmployer()
    {
        return employer;
    }

    public void setEmployer(String employer)
    {
        this.employer = employer;
    }

    public String getGroupName()
    {
        return groupName;
    }

    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }

    public String getGroupNumber()
    {
        return groupNumber;
    }

    public void setGroupNumber(String groupNumber)
    {
        this.groupNumber = groupNumber;
    }

    public String getMemberNumber()
    {
        return memberNumber;
    }

    public void setMemberNumber(String memberNumber)
    {
        this.memberNumber = memberNumber;
    }

    public String getCoverageBeginDate()
    {
        return coverageBeginDate;
    }

    public void setCoverageBeginDate(String coverageBeginDate)
    {
        this.coverageBeginDate = coverageBeginDate;
    }

    public String getCoverageEndDate()
    {
        return coverageEndDate;
    }

    public void setCoverageEndDate(String coverageEndDate)
    {
        this.coverageEndDate = coverageEndDate;
    }

    public String getIndividualDeductible()
    {
        return individualDeductible;
    }

    public void setIndividualDeductible(String individualDeductible)
    {
        this.individualDeductible = individualDeductible;
    }

    public String getFamilyDeductible()
    {
        return familyDeductible;
    }

    public void setFamilyDeductible(String familyDeductible)
    {
        this.familyDeductible = familyDeductible;
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

    public String getPercentagePay()
    {
        return percentagePay;
    }

    public void setPercentagePay(String percentagePay)
    {
        this.percentagePay = percentagePay;
    }

    public String getThreshold()
    {
        return threshold;
    }

    public void setThreshold(String threshold)
    {
        this.threshold = threshold;
    }

    public String getOfficeVisitCoPay()
    {
        return officeVisitCoPay;
    }

    public void setOfficeVisitCoPay(String officeVisitCoPay)
    {
        this.officeVisitCoPay = officeVisitCoPay;
    }

    @ValidEntity(entity = GenderType.class)
    @SelectFieldStyle(style = SelectFieldStyle.Style.COMBO)
    public String getGender()
    {
        return gender;
    }

    public void setGender(String gender)
    {
        this.gender = gender;
    }

    @ValidEntity(entity = MaritalStatusType.class)
    @SelectFieldStyle(style = SelectFieldStyle.Style.COMBO)
    public String getMaritalStatus()
    {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus)
    {
        this.maritalStatus = maritalStatus;
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
    public String getPatientRelationshipToInsured()
    {
        return patientRelationshipToInsured;
    }

    public void setPatientRelationshipToInsured(String patientRelationshipToInsured)
    {
        this.patientRelationshipToInsured = patientRelationshipToInsured;
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
    public String getEthnicity()
    {
        return ethnicity;
    }

    public void setEthnicity(String ethnicity)
    {
        this.ethnicity = ethnicity;
    }

    @ValidEntity(entity = LanguageType.class)
    @SelectFieldStyle(style = SelectFieldStyle.Style.RADIO)
    public String getLanguageCodes()
    {
        return languageCodes;
    }

    public void setLanguageCodes(String languageCodes)
    {
        this.languageCodes = languageCodes;
    }

    public String toString()
	{
		StringBuffer b = new StringBuffer();
		b.append("[TestInputObject personIdProperty = '").append(personId)
		 .append("', accountProperty = ").append(account)
		 .append(", chartNumberProperty = ").append(chartNumber)
		 .append(", lastNameProperty = ").append(lastName)
		 .append(", firstNameProperty = ").append(firstName)
		 .append(", middleNameProperty = ").append(middleName)
		 .append(", dateOfBirthProperty = ").append(dateOfBirth)
		 .append(", socialSecurityNumberProperty = ").append(socialSecurityNumber)
		 .append(", responsibleParty = ").append(responsibleParty)
		 .append(", responsibleParty = ").append(relationshipToResponsibleOtherRelationship)
		 .append(", driversLicenseNumber = ").append(driversLicenseNumber)
		 .append(", driversLicenseState = ").append(driversLicenseState)
		 .append(", miscNotes = ").append(miscNotes)
		 .append(", homePhone = ").append(homePhone)
		 .append(", workPhone = ").append(workPhone)
		 .append(", homePhone2 = ").append(homePhone2)
		 .append(", workPhone2 = ").append(workPhone2)
		 .append(", cellPhone = ").append(cellPhone)
		 .append(", pager = ").append(pager)
		 .append(", alternate = ").append(alternate)
		 .append(", homeAddress = ").append(homeAddress)
		 .append(", homeAddress2 = ").append(homeAddress2)
		 .append(", city = ").append(city)
		 .append(", state = ").append(state)
		 .append(", zip = ").append(zip)
		 .append(", email = ").append(email)
		 .append(", employerId = ").append(employerId)
		 .append(", occupation = ").append(occupation)
		 .append(", employerPhoneNumber = ").append(employerPhoneNumber)
		 .append(", insuranceProduct = ").append(insuranceProduct)
		 .append(", insurancePlan = ").append(insurancePlan)
		 .append(", patientRelationshipToInsuredPtherRelationship = ").append(patientRelationshipToInsuredPtherRelationship)
		 .append(", insuredPersonId = ").append(insuredPersonId)
		 .append(", employer = ").append(employer)
		 .append(", groupName = ").append(groupName)
		 .append(", groupNumber = ").append(groupNumber)
		 .append(", memberNumber = ").append(memberNumber)
		 .append(", coverageBeginDate = ").append(coverageBeginDate)
		 .append(", coverageEndDate = ").append(coverageEndDate)
		 .append(", individualDeductible = ").append(individualDeductible)
		 .append(", familyDeductible = ").append(familyDeductible)
		 .append(", individualDeductibleRemaining = ").append(individualDeductibleRemaining)
		 .append(", familyDeductibleRemaining = ").append(familyDeductibleRemaining)
		 .append(", percentagePay = ").append(percentagePay)
		 .append(", threshold = ").append(threshold)
		 .append(", officeVisitCoPay = ").append(officeVisitCoPay)
		 .append(", suffix = ").append(suffix)
		 .append(", gender = ").append(gender)
		 .append(", maritalStatus = ").append(maritalStatus)
		 .append(", relationshipToResponsible = ").append(relationshipToResponsible)
		 .append(", employmentStatus = ").append(employmentStatus)
		 .append(", insuranceSequence = ").append(insuranceSequence)
		 .append(", patientRelationshipToInsured = ").append(patientRelationshipToInsured)
		 .append(", bloodType = ").append(bloodType)
		 .append(", ethnicity = ").append(ethnicity)
		 .append(", languageCodes = ").append(languageCodes);

		b.append("]");
		return b.toString();
	}
}