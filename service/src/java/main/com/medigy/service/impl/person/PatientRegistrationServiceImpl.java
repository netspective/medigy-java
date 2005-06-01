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
import com.medigy.persist.util.HibernateUtil;
import com.medigy.service.ServiceVersion;
import com.medigy.service.contact.ContactMechanismFacade;
import com.medigy.service.dto.ServiceParameters;
import com.medigy.service.dto.insurance.InsuranceCoverageParameters;
import com.medigy.service.dto.party.PhoneParameters;
import com.medigy.service.dto.party.PostalAddressParameters;
import com.medigy.service.dto.person.PersonParameters;
import com.medigy.service.dto.person.RegisterPatientParameters;
import com.medigy.service.dto.person.RegisteredPatient;
import com.medigy.service.insurance.InsurancePolicyFacade;
import com.medigy.service.person.PatientRegistrationService;
import com.medigy.service.person.PersonFacade;
import com.medigy.service.util.ReferenceEntityFacade;
import com.medigy.service.util.UnknownReferenceTypeException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;

/**
 * Service class for registering a new patient
 */
public class PatientRegistrationServiceImpl implements PatientRegistrationService
{
    private static final Log log = LogFactory.getLog(PatientRegistrationServiceImpl.class);
    private boolean createEmployerIfUnknown = true;
    private boolean createInsuranceProviderIfUnknown = true;


    private ReferenceEntityFacade referenceEntityFacade;
    private PersonFacade personFacade;
    private ContactMechanismFacade contactMechanismFacade;
    private InsurancePolicyFacade insurancePolicyFacade;

    public InsurancePolicyFacade getInsurancePolicyFacade()
    {
        return insurancePolicyFacade;
    }

    public void setInsurancePolicyFacade(final InsurancePolicyFacade insurancePolicyFacade)
    {
        this.insurancePolicyFacade = insurancePolicyFacade;
    }

    public ContactMechanismFacade getContactMechanismFacade()
    {
        return contactMechanismFacade;
    }

    public void setContactMechanismFacade(final ContactMechanismFacade contactMechanismFacade)
    {
        this.contactMechanismFacade = contactMechanismFacade;
    }

    public PersonFacade getPersonFacade()
    {
        return personFacade;
    }

    public void setPersonFacade(final PersonFacade personFacade)
    {
        this.personFacade = personFacade;
    }

    public ReferenceEntityFacade getReferenceEntityFacade()
    {
        return referenceEntityFacade;
    }

    public void setReferenceEntityFacade(final ReferenceEntityFacade referenceEntityFacade)
    {
        this.referenceEntityFacade = referenceEntityFacade;
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
        final InsuranceCoverageParameters[] insurancePolicies = params.getInsuranceCoverages();
        for (InsuranceCoverageParameters param: insurancePolicies)
        {
            final Person contractHolder = personFacade.getPersonById(param.getInsuranceContractHolderId());
            final InsurancePlan insPlan = insurancePolicyFacade.getInsurancePlanById(param.getInsurancePlanId());
            final InsurancePolicyType type = referenceEntityFacade.getInsurancePolicyType(param.getInsurancePolicyTypeCode());

            final InsurancePolicy policy = insurancePolicyFacade.createInsurancePolicy(person, contractHolder, insPlan, param.getInsurancePolicyNumber(),
                param.getInsuranceGroupNumber(), type, param.getCoverageStartDate(), param.getCoverageEndDate());

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
        final PostalAddressParameters param = patientParameters.getPostalAddress();
        final PostalAddress address = contactMechanismFacade.addPostalAddress(param.getStreet1(), param.getStreet2(),
            param.getCity(), param.getState(), param.getProvince(), param.getCounty(),  param.getPostalCode(), param.getCountry());

        // NOTE: using the PostalAddress as the contact mechanism introduces multiple
        // PartyContactMechanisms so don't use the PostalAddress, instead use ContactMechanism
        ContactMechanism cm = (ContactMechanism) HibernateUtil.getSession().load(ContactMechanism.class, address.getContactMechanismId());
        contactMechanismFacade.addPartyContactMechanism(cm, patient, param.getPurposeType(), param.getPurposeDescription());

    }

    public RegisteredPatient registerPatient(final RegisterPatientParameters params)
    {
        Person person = Person.createNewPatient();
        try
        {
            final PersonParameters patientParameters = params.getPerson();

            person.setLastName(patientParameters.getLastName());
            person.setFirstName(patientParameters.getFirstName());
            person.setMiddleName(patientParameters.getMiddleName());
            person.setBirthDate(patientParameters.getBirthDate());

            final GenderType genderType = referenceEntityFacade.getGenderType(patientParameters.getGenderCode());
            final MaritalStatusType maritalStatusType = referenceEntityFacade.getMaritalStatusType(patientParameters.getMaritalStatusCode());
            person.addMaritalStatus(maritalStatusType);
            person.addGender(genderType);

            // add the languages
            for (String language : patientParameters.getLanguageCodes())
                person.addLanguage(referenceEntityFacade.getLanguageType(language));

            // add the ethnicities
            for (String ethnicity : patientParameters.getEthnicityCodes())
                person.addEthnicity(referenceEntityFacade.getEthnicityType(ethnicity));

            if (patientParameters.getSsn() != null)
                person.setSsn(patientParameters.getSsn());
            if (patientParameters.getDriversLicenseNumber() != null)
                person.setDriversLicenseNumber(patientParameters.getDriversLicenseNumber());
                
            HibernateUtil.getSession().save(person);
            registerPostalAddress(person, params);
            registerInsuranceInformation(person, params);
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
        catch (final Exception e)
        {
            log.error(e);
            RegisteredPatient patient = new RegisteredPatient()
            {
                public Serializable getPatientId()
                {
                    return null;
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
                    return e.getMessage();
                }
            };
            return patient;
        }
    }

    private void registerHomePhone(final Person person, final RegisterPatientParameters params)
    {
        final PhoneParameters homePhone = params.getHomePhone();
        final PhoneNumber phone = contactMechanismFacade.addPhone(homePhone.getCountryCode(), homePhone.getAreaCode(), homePhone.getNumber(),
            homePhone.getExtension());
        

    }

    // TODO: Put a validator and return a list of errors/warnings
    public boolean isValid(ServiceParameters params)
    {
        RegisterPatientParameters patientParameters = (RegisterPatientParameters) params;
        assert  patientParameters.getPerson().getGenderCode() != null :
            "Gender cannot be empty.";
        final String[] languageCodes = patientParameters.getPerson().getLanguageCodes();
        assert languageCodes != null && languageCodes.length > 0 :
            "There must be at least one language.";
        final String[] ethnicities = patientParameters.getPerson().getEthnicityCodes();
        assert ethnicities != null && ethnicities.length > 0 :
            "There must be at least one ethnicity.";

        return true;
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
                 * @return
                 */
                public String getErrorMessage()
                {
                    return errorMessage;
                }
            };
    }
}
