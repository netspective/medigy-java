/*
 * Copyright (c) 2000-2003 Netspective Communications LLC. All rights reserved.
 *
 * Netspective Communications LLC ("Netspective") permits redistribution, modification and use of this file in source
 * and binary form ("The Software") under the Netspective Source License ("NSL" or "The License"). The following
 * conditions are provided as a summary of the NSL but the NSL remains the canonical license and must be accepted
 * before using The Software. Any use of The Software indicates agreement with the NSL.
 *
 * 1. Each copy or derived work of The Software must preserve the copyright notice and this notice unmodified.
 *
 * 2. Redistribution of The Software is allowed in object code form only (as Java .class files or a .jar file
 *    containing the .class files) and only as part of an application that uses The Software as part of its primary
 *    functionality. No distribution of the package is allowed as part of a software development kit, other library,
 *    or development tool without written consent of Netspective. Any modified form of The Software is bound by these
 *    same restrictions.
 *
 * 3. Redistributions of The Software in any form must include an unmodified copy of The License, normally in a plain
 *    ASCII text file unless otherwise agreed to, in writing, by Netspective.
 *
 * 4. The names "Netspective", "Axiom", "Commons", "Junxion", and "Sparx" are trademarks of Netspective and may not be
 *    used to endorse products derived from The Software without without written consent of Netspective. "Netspective",
 *    "Axiom", "Commons", "Junxion", and "Sparx" may not appear in the names of products derived from The Software
 *    without written consent of Netspective.
 *
 * 5. Please attribute functionality where possible. We suggest using the "powered by Netspective" button or creating
 *    a "powered by Netspective(tm)" link to http://www.netspective.com for each application using The Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT,
 * ARE HEREBY DISCLAIMED.
 *
 * NETSPECTIVE AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE OR ANY THIRD PARTY AS A
 * RESULT OF USING OR DISTRIBUTING THE SOFTWARE. IN NO EVENT WILL NETSPECTIVE OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THE SOFTWARE, EVEN
 * IF HE HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 */
package com.medigy.service.impl.person;

import com.medigy.persist.model.insurance.InsurancePlan;
import com.medigy.persist.model.insurance.InsurancePolicy;
import com.medigy.persist.model.party.ContactMechanism;
import com.medigy.persist.model.party.PhoneNumber;
import com.medigy.persist.model.party.PostalAddress;
import com.medigy.persist.model.person.Person;
import com.medigy.persist.reference.custom.insurance.CoverageLevelType;
import com.medigy.persist.reference.custom.insurance.InsurancePolicyType;
import com.medigy.persist.reference.type.GenderType;
import com.medigy.persist.reference.type.MaritalStatusType;
import com.medigy.service.AbstractService;
import com.medigy.service.ServiceVersion;
import com.medigy.service.contact.ContactMechanismFacade;
import com.medigy.service.dto.ServiceParameters;
import com.medigy.service.dto.insurance.InsuranceCoverageParameters;
import com.medigy.service.dto.party.PostalAddressParameters;
import com.medigy.service.dto.person.RegisterPatientParameters;
import com.medigy.service.dto.person.RegisteredPatient;
import com.medigy.service.insurance.InsurancePolicyFacade;
import com.medigy.service.person.PatientRegistrationService;
import com.medigy.service.person.PersonFacade;
import com.medigy.service.util.ReferenceEntityFacade;
import com.medigy.service.util.UnknownReferenceTypeException;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.validator.InvalidStateException;
import org.hibernate.validator.InvalidValue;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Past;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Service class for registering a new patient
 */
public class PatientRegistrationServiceImpl extends AbstractService implements PatientRegistrationService
{
    private static final Log log = LogFactory.getLog(PatientRegistrationServiceImpl.class);
    private boolean createEmployerIfUnknown = true;
    private boolean createInsuranceProviderIfUnknown = true;


    private ReferenceEntityFacade referenceEntityFacade;
    private PersonFacade personFacade;
    private ContactMechanismFacade contactMechanismFacade;
    private InsurancePolicyFacade insurancePolicyFacade;

    public PatientRegistrationServiceImpl(final SessionFactory sessionFactory, final ReferenceEntityFacade referenceEntityFacade,
                                          final PersonFacade personFacade, final ContactMechanismFacade contactMechanismFacade,
                                          final InsurancePolicyFacade insurancePolicyFacade)
    {
        super(sessionFactory);
        this.referenceEntityFacade = referenceEntityFacade;
        this.personFacade = personFacade;
        this.contactMechanismFacade = contactMechanismFacade;
        this.insurancePolicyFacade = insurancePolicyFacade;
    }

    public ServiceVersion[] getSupportedServiceVersions()
    {
        return new ServiceVersion[0];
    }

    /**
     * Check to see if Employer organization should be automatically created if it is not known (no ID).
     * @return
     */
    public boolean isCreateEmployerIfUnknown()
    {
        return createEmployerIfUnknown;
    }

    public void setCreateEmployerIfUnknown(boolean createEmployerIfUnknown)
    {
        this.createEmployerIfUnknown = createEmployerIfUnknown;
    }

    /**
     * Check to see if Insurance Provider organization should be automatically created if it is not known (no ID)
     * @return
     */
    public boolean isCreateInsuranceProviderIfUnknown()
    {
        return createInsuranceProviderIfUnknown;
    }

    public void setCreateInsuranceProviderIfUnknown(boolean createInsuranceProviderIfUnknown)
    {
        this.createInsuranceProviderIfUnknown = createInsuranceProviderIfUnknown;
    }

    private static Serializable getPrimaryKey(String keyString)
    {
        return Long.parseLong(keyString);
    }

    protected void registerInsuranceInformation(final Person person, final RegisterPatientParameters params) throws Exception
    {
        if (params.getPrimaryInsurancePlanId() == null)
                return;
        final InsuranceCoverageParameters[] insurancePolicies = new InsuranceCoverageParameters[] {
            new InsuranceCoverageParameters() {
                public Serializable getInsuranceCarrierId()
                {
                    return params.getPrimaryInsuranceCarrierId();
                }

                public Serializable getInsuranceProductId()
                {
                    return params.getPrimaryInsuranceProductId();
                }

                public Serializable getInsurancePlanId()
                {
                    return params.getPrimaryInsurancePlanId();
                }

                public String getInsurancePolicyNumber()
                {
                    return params.getPrimaryInsurancePolicyNumber();
                }

                public String getInsurancePolicyTypeCode()
                {
                    return params.getPrimaryInsurancePolicyTypeCode();
                }

                public String getInsuranceGroupNumber()
                {
                    return params.getPrimaryInsuranceGroupNumber();
                }

                public Serializable getInsuranceContractHolderId()
                {
                    return params.getPrimaryInsuranceContractHolderId();
                }

                public String getInsuranceContractHolderRole()
                {
                    return params.getPrimaryInsuranceContractHolderRole();
                }

                public Date getCoverageStartDate()
                {
                    return params.getPrimaryInsuranceCoverageStartDate();
                }

                public Date getCoverageEndDate()
                {
                    return params.getPrimaryInsuranceCoverageEndDate();
                }

                public Float getIndividualDeductibleAmount()
                {
                    return params.getPrimaryInsuranceIndividualDeductibleAmount();
                }

                public Float getFamilyDeductibleAmount()
                {
                    return params.getPrimaryInsuranceFamilyDeductibleAmount();
                }

                public Float getOfficeVisitCoPay()
                {
                    return params.getPrimaryInsuranceOfficeVisitCoPay();
                }

                public Float getPercentagePay()
                {
                    return params.getPrimaryInsurancePercentagePay();
                }

                public Float getMaxThresholdAmount()
                {
                    return params.getPrimaryInsuranceMaxThresholdAmount();
                }
            }
        };

        for (InsuranceCoverageParameters param: insurancePolicies)
        {
            Person contractHolder = null;
            if (param.getInsuranceContractHolderId() != null)
                contractHolder = personFacade.getPersonById(param.getInsuranceContractHolderId());
            else
                contractHolder = person;

            final InsurancePlan insPlan = insurancePolicyFacade.getInsurancePlanById(param.getInsurancePlanId());
            final InsurancePolicyType type = referenceEntityFacade.getInsurancePolicyType(param.getInsurancePolicyTypeCode());

            final InsurancePolicy policy = insurancePolicyFacade.createInsurancePolicy(person, contractHolder, insPlan,
                    param.getInsurancePolicyNumber(), param.getInsuranceGroupNumber(), type,
                    param.getCoverageStartDate(), param.getCoverageEndDate());

            if (param.getIndividualDeductibleAmount() != null)
                insurancePolicyFacade.addInsurancePolicyCoverageLevel(policy, CoverageLevelType.Cache.INDIVIDUAL_DEDUCTIBLE.getEntity(),
                    param.getIndividualDeductibleAmount());

            if (param.getFamilyDeductibleAmount() != null)
                insurancePolicyFacade.addInsurancePolicyCoverageLevel(policy, CoverageLevelType.Cache.FAMILY_DEDUCTIBLE.getEntity(),
                    param.getFamilyDeductibleAmount());
        }

    }

    /**
     * Registers a responsible party associated with a new patient. This is a sub-task belonging to the
     * patient registration process.
     *
     * @param person
     * @param patientParameters
     */
    protected void registerResponsibleParty(final Person person, final RegisterPatientParameters patientParameters)
    {
        // TODO: FINANCIAL responsible party can be an organization and it can be changed per visit too. Need table to relate to PARTY_RELATIONSHIP.

    }

    /**
     * Registers a primary care provider relationship with the patient. This assumes that the primary care provider already
     * exists.
     * @param patient
     * @param patientParameters
     */
    protected void registerPrimaryCareProvider(final Person patient, final RegisterPatientParameters patientParameters)
    {


    }

    protected void registerPostalAddress(final Person patient, final RegisterPatientParameters patientParameters) throws UnknownReferenceTypeException
    {
        final PostalAddressParameters param = new PostalAddressParameters()
            {
                public String getStreet1()
                {
                    return patientParameters.getStreet1();
                }

                public String getStreet2()
                {
                    return  patientParameters.getStreet2();
                }

                public String getCity()
                {
                    return  patientParameters.getCity();
                }

                public String getState()
                {
                    return patientParameters.getState();
                }

                public String getProvince()
                {
                    return patientParameters.getProvince();
                }

                public String getPostalCode()
                {
                    return patientParameters.getPostalCode();
                }

                public String getCounty()
                {
                    return patientParameters.getCounty();
                }

                public String getCountry()
                {
                    return patientParameters.getCountry();
                }

                public String getPurposeType()
                {
                    return patientParameters.getPostalAddressPurposeType();
                }

                public String getPurposeDescription()
                {
                    return patientParameters.getPostalAddressPurposeDescription();
                }
            };
        final PostalAddress address = contactMechanismFacade.addPostalAddress(param.getStreet1(), param.getStreet2(),
            param.getCity(), param.getState(), param.getProvince(), param.getCounty(),  param.getPostalCode(), param.getCountry());

        // NOTE: using the PostalAddress as the contact mechanism introduces multiple
        // PartyContactMechanisms so don't use the PostalAddress, instead use ContactMechanism
        ContactMechanism cm = (ContactMechanism) getSession().load(ContactMechanism.class, address.getContactMechanismId());
        contactMechanismFacade.addPartyContactMechanism(cm, patient, param.getPurposeType(), param.getPurposeDescription());

    }

    public RegisteredPatient registerPatient(final RegisterPatientParameters params)
    {
        Person person = Person.createNewPatient();
        try
        {
            person.setLastName(params.getLastName());
            person.setFirstName(params.getFirstName());
            person.setMiddleName(params.getMiddleName());
            person.setBirthDate(params.getBirthDate());

            final GenderType genderType = referenceEntityFacade.getGenderType(params.getGenderCode());
            final MaritalStatusType maritalStatusType = referenceEntityFacade.getMaritalStatusType(params.getMaritalStatusCode());
            person.addMaritalStatus(maritalStatusType);
            person.addGender(genderType);

            // add the languages
            if(params.getLanguageCodes() != null)
                for (String language : params.getLanguageCodes())
                    person.addLanguage(referenceEntityFacade.getLanguageType(language));

            // add the ethnicities
            if(params.getEthnicityCodes() != null)
                for (String ethnicity : params.getEthnicityCodes())
                    person.addEthnicity(referenceEntityFacade.getEthnicityType(ethnicity));

            if (params.getSsn() != null)
                person.setSsn(params.getSsn());
            if (params.getDriversLicenseNumber() != null)
            {
                person.setDriversLicenseNumber(params.getDriversLicenseNumber());
            }

            getSession().save(person);
            // TODO: Temporary. uncomment when Country/State data are in (Aye 7/5/05)
            //registerPostalAddress(person, params);
            //registerInsuranceInformation(person, params);
            if(params.getHomePhone() != null)
                registerHomePhone(person, params);

            final Long patientId = (Long) person.getPersonId();
            final RegisteredPatient patient = new RegisteredPatient()
            {
                public Serializable getPatientId()
                {
                    return patientId;
                }

                public RegisterPatientParameters getRegisterPatientParameters()
                {
                    return params;
                }

                /**
                 * Locale specific error message
                 *
                 * @return
                 */
                public String getErrorMessage()
                {
                    return null;
                }
            };
            if (log.isInfoEnabled())
                log.info("New PERSON created with id = " + patient.getPatientId());
           return patient;
        }
        catch (InvalidStateException e)
        {
            log.error(e);
            for (InvalidValue value : e.getInvalidValues())
                log.error("\t" + value.getPropertyName() + " " + value.getMessage());
            return createErrorResponse(params, e.getMessage());
        }
        catch (final Exception e)
        {
            log.error(ExceptionUtils.getStackTrace(e));
            return createErrorResponse(params, e.getMessage());
        }
    }

    public RegisterPatientParameters getNewPatientParameters()
    {
        return new RegisterPatientParameters() {
            public String getPersonId()
            {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @NotNull public String getPatientId()
            {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public String getFirstName()
            {
                return null;
            }

            public String getLastName()
            {
                return null;
            }

            public String getMiddleName()
            {
                return null;
            }

            public String getSuffix()
            {
                return null;
            }

            public Date getBirthDate()
            {
                return null;
            }

            @NotNull @Past public Date getDeathDate()
            {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public String getGenderCode()
            {
                return null;
            }

            public String getMaritalStatusCode()
            {
                return null;
            }

            public String getSsn()
            {
                return null;
            }

            public String getDriversLicenseNumber()
            {
                return null;
            }

            public String getDriversLicenseStateCode()
            {
                return null;
            }

            public String getEmployerName()
            {
                return null;
            }

            public String getEmployerId()
            {
                return null;
            }

            public String getOccupation()
            {
                return null;
            }

            public List<String> getEthnicityCodes()
            {
                return new ArrayList<String>();
            }

            public List<String> getLanguageCodes()
            {
                return new ArrayList<String>();
            }

            public String getResponsiblePartyId()
            {
                return null;
            }

            public String getResponsiblePartyRole()
            {
                return null;
            }

            public String getHomePhone()
            {
                return null;
            }

            public String getWorkPhone()
            {
                return null;
            }

            public String getMobilePhone()
            {
                return null;
            }

            @NotNull
            public String getStreet1()
            {
                return null;
            }

            public String getStreet2()
            {
                return null;
            }

            @NotNull
            public String getCity()
            {
                return null;
            }

            @NotNull
            public String getState()
            {
                return null;
            }

            public String getProvince()
            {
                return null;
            }

            @NotNull
            public String getPostalCode()
            {
                return null;
            }

            public String getCounty()
            {
                return null;
            }

            @NotNull
            public String getCountry()
            {
                // TODO: Need to create some defalt countries
                return "USA";
            }

            public String getPostalAddressPurposeType()
            {
                return null;
            }

            public String getPostalAddressPurposeDescription()
            {
                return null;
            }

            public String getPrimaryCareProviderId()
            {
                return null;
            }

            public Serializable getPrimaryInsuranceCarrierId()
            {
                return null;
            }

            public Serializable getPrimaryInsuranceProductId()
            {
                return null;
            }

            @NotNull
            public Serializable getPrimaryInsurancePlanId()
            {
                return null;
            }

            @NotNull
            public String getPrimaryInsurancePolicyNumber()
            {
                return null;
            }

            public String getPrimaryInsurancePolicyTypeCode()
            {
                return null;
            }

            @NotNull
            public String getPrimaryInsuranceGroupNumber()
            {
                return null;
            }

            @NotNull
            public Serializable getPrimaryInsuranceContractHolderId()
            {
                return null;
            }

            @NotNull
            public String getPrimaryInsuranceContractHolderRole()
            {
                return null;
            }

            public Date getPrimaryInsuranceCoverageStartDate()
            {
                return null;
            }

            public Date getPrimaryInsuranceCoverageEndDate()
            {
                return null;
            }

            public Float getPrimaryInsuranceIndividualDeductibleAmount()
            {
                return null;
            }

            public Float getPrimaryInsuranceFamilyDeductibleAmount()
            {
                return null;
            }

            public Float getPrimaryInsuranceOfficeVisitCoPay()
            {
                return null;
            }

            public Float getPrimaryInsurancePercentagePay()
            {
                return null;
            }

            public Float getPrimaryInsuranceMaxThresholdAmount()
            {
                return null;
            }

            public ServiceVersion getServiceVersion()
            {
                return null;
            }
        };
    }

    private PhoneNumber registerHomePhone(final Person person, final RegisterPatientParameters params)
    {
        return contactMechanismFacade.addPhone(null, null, params.getHomePhone());
    }

    // TODO: Put a validator and return a list of errors/warnings
    public String[] isValid(ServiceParameters params)
    {
        RegisterPatientParameters patientParameters = (RegisterPatientParameters) params;
        assert  patientParameters.getGenderCode() != null :
            "Gender cannot be empty.";
        final List<String> languageCodes = patientParameters.getLanguageCodes();
        assert languageCodes != null && languageCodes.size() > 0 :
            "There must be at least one language.";
        final List<String> ethnicities = patientParameters.getEthnicityCodes();
        assert ethnicities != null && ethnicities.size() > 0 :
            "There must be at least one ethnicity.";

        return null;
    }

    public RegisteredPatient createErrorResponse(final ServiceParameters params, final String errorMessage)
    {
        return new RegisteredPatient()
            {
                public Serializable getPatientId()
                {
                    return null;
                }

                public RegisterPatientParameters getRegisterPatientParameters()
                {
                    return (RegisterPatientParameters) params;
                }

                /**
                 * Locale specific error message
                 *
                 * @return  error message
                 */
                public String getErrorMessage()
                {
                    return errorMessage;
                }
            };
    }
}
