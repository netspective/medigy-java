package com.medigy.presentation.model;

import com.medigy.persist.model.person.Gender;
import com.medigy.service.ServiceVersion;
import com.medigy.service.dto.person.RegisterPatientParameters;
import com.medigy.service.validator.ValidEntity;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Past;

import java.io.Serializable;
import java.util.Date;

public class PatientRegistrationFormAdapter implements RegisterPatientParameters
{
    private PatientRegistrationFormModel model;

    public PatientRegistrationFormAdapter(PatientRegistrationFormModel model)
    {
        this.model = model;
    }

    @NotNull public String getFirstName()
    {
        return model.getFirstName();
    }

    @NotNull public String getLastName()
    {
        return model.getLastName();
    }

    public String getMiddleName()
    {
        return model.getMiddleName();
    }

    public String getSuffix()
    {
        return model.getSuffix();
    }

    @NotNull @Past public Date getBirthDate()
    {
        return model.getDateOfBirth();
    }

    @NotNull @ValidEntity(entity = Gender.class) public String getGenderCode()
    {
        return model.getGender();
    }

    public String getMaritalStatusCode()
    {
        return model.getMaritalStatus();
    }

    public String getSsn()
    {
        return model.getSocialSecurityNumber();
    }

    public String getDriversLicenseNumber()
    {
        return model.getDriversLicenseNumber();
    }

    public String getDriversLicenseStateCode()
    {
        return model.getDriversLicenseState();
    }

    public String getEmployerName()
    {
        return model.getEmployer();
    }

    public String getEmployerId()
    {
        return model.getEmployerId();
    }

    public String getOccupation()
    {
        return model.getOccupation();
    }

    public String[] getEthnicityCodes()
    {
        return new String[] {model.getEthnicity()}; // TODO: return multiple values in String array
    }

    public String[] getLanguageCodes()
    {
        return new String[] {model.getLanguageCodes()};  // TODO: return multiple values in String array
    }

    public String getResponsiblePartyId()
    {
        return model.getResponsibleParty();
    }

    public String getResponsiblePartyRole()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getHomePhoneCountryCode()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getHomePhoneCityCode()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getHomePhoneAreaCode()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getHomePhoneNumber()
    {
        return model.getHomePhone();
    }

    public String getWorkPhoneCountryCode()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getWorkPhoneCityCode()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getWorkPhoneAreaCode()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getWorkPhoneNumber()
    {
        return model.getWorkPhone();
    }

    public String getWorkPhoneExtension()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getMobilePhoneCountryCode()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getMobilePhoneCityCode()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getMobilePhoneAreaCode()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getMobilePhoneNumber()
    {
        return model.getCellPhone();
    }

    @NotNull public String getStreet1()
    {
        return model.getHomeAddress();
    }

    public String getStreet2()
    {
        return model.getHomeAddress2();
    }

    @NotNull public String getCity()
    {
        return model.getCity();
    }

    @NotNull public String getState()
    {
        return model.getState();
    }

    public ServiceVersion getServiceVersion()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getProvince()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Serializable getPrimaryInsuranceCarrierId()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Serializable getPrimaryInsuranceProductId()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @NotNull public Serializable getPrimaryInsurancePlanId()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @NotNull public String getPrimaryInsurancePolicyNumber()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getPrimaryInsurancePolicyTypeCode()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @NotNull public String getPrimaryInsuranceGroupNumber()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @NotNull public Serializable getPrimaryInsuranceContractHolderId()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @NotNull public String getPrimaryInsuranceContractHolderRole()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Date getPrimaryInsuranceCoverageStartDate()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Date getPrimaryInsuranceCoverageEndDate()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Float getPrimaryInsuranceIndividualDeductibleAmount()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Float getPrimaryInsuranceFamilyDeductibleAmount()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Float getPrimaryInsuranceOfficeVisitCoPay()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Float getPrimaryInsurancePercentagePay()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Float getPrimaryInsuranceMaxThresholdAmount()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @NotNull public String getPostalCode()
    {
        return model.getZip();
    }

    public String getCounty()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @NotNull public String getCountry()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getPostalAddressPurposeType()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getPostalAddressPurposeDescription()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getPrimaryCareProviderId()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
