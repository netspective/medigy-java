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
package com.medigy.service.person;

import com.medigy.persist.model.org.Organization;
import com.medigy.persist.model.party.PartyRole;
import com.medigy.persist.model.person.Person;
import com.medigy.persist.reference.custom.insurance.InsurancePolicyType;
import com.medigy.persist.reference.custom.party.ContactMechanismPurposeType;
import com.medigy.persist.reference.custom.party.OrganizationRoleType;
import com.medigy.persist.reference.custom.person.PatientResponsiblePartyRoleType;
import com.medigy.persist.reference.custom.person.PersonRoleType;
import com.medigy.persist.reference.type.GenderType;
import com.medigy.persist.reference.type.MaritalStatusType;
import com.medigy.persist.util.HibernateUtil;
import com.medigy.service.ServiceLocator;
import com.medigy.service.ServiceVersion;
import com.medigy.service.common.ReferenceEntityFacade;
import com.medigy.service.common.UnknownReferenceTypeException;
import com.medigy.service.contact.AddContactMechanismService;
import com.medigy.service.dto.party.AddPhoneParameters;
import com.medigy.service.dto.party.AddPostalAddressParameters;
import com.medigy.service.dto.person.RegisterPatientParameters;
import com.medigy.service.dto.person.RegisteredPatient;
import com.medigy.service.util.PartyRelationshipFacade;
import com.medigy.service.util.PersonFacade;
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

    protected void registerInsuranceInformation(final Person person, final Person responsibleParty, final RegisterPatientParameters params) throws Exception
    {
        // usually there is one primary insurance and one possible secondary
        final String[] policyNumbers = params.getInsurancePolicyNumbers();
        final String[] policyTypes = params.getInsurancePolicyTypes();
        final String[] contractHolderIds = params.getInsurancePolicyHolderId();
        final String[] contractHolderLastNames = params.getInsurancePolicyHolderLastNames();
        final String[] contractHolderFirstNames = params.getInsurancePolicyHolderFirstNames();
        final String[] policyProviders = params.getInsurancePolicyProviders();
        final String[] policyProviderIds = params.getInsurancePolicyProviderIds();

        for (int i=0; i < policyNumbers.length; i++)
        {
            final String policyType = params.getInsurancePolicyTypes()[i];
            final String contractHolderId = contractHolderIds[i];
            final String contractHolderFirstName = contractHolderFirstNames[i];
            final String contractHolderLastName = contractHolderLastNames[i];
            final String policyProvider = policyProviders[i];
            final String policyProviderId = policyProviderIds[i];

            Person policyHolder = null;
            if (contractHolderId != null)
            {
                policyHolder = (Person) HibernateUtil.getSession().load(Person.class,
                    getPrimaryKey(contractHolderId));
            }
            else if (contractHolderLastName.equals(person.getLastName()) && contractHolderFirstName.equals(person.getFirstName()))
            {
                // the policy holder is self
                policyHolder = person;
            }
            else if (params.getResponsiblePartyLastName().equals(contractHolderLastName) &&
                    params.getResponsiblePartyFirstName().equals(contractHolderFirstName))
            {
                // TODO: The responsible party and policy Holder's names are the same so they're essentially the same person
                policyHolder = responsibleParty;
            }
            else
            {
                policyHolder = new Person();
                policyHolder.setLastName(contractHolderLastName);
                policyHolder.setFirstName(contractHolderFirstName);
                HibernateUtil.getSession().save(policyHolder);
            }

            Organization providerOrg = null;
            if (policyProviderId != null)
            {
                providerOrg = (Organization) HibernateUtil.getSession().load(Organization.class,
                        getPrimaryKey(policyProviderId));
            }
            else
            {
                // TODO: Determine if provider look up should be done using the Name also instead of just the ID
                if (isCreateInsuranceProviderIfUnknown())
                {
                    providerOrg = new Organization();
                    providerOrg.setOrganizationName(policyProvider);
                    providerOrg.addPartyRole(OrganizationRoleType.Cache.INSURANCE_PROVIDER.getEntity());
                    HibernateUtil.getSession().save(providerOrg);
                }
                else
                {
                    throw new Exception("Unknown policy provider: " + policyProvider);
                }
            }
            processInsurancePolicy(policyNumbers[i], policyType, providerOrg, policyHolder, person);
        }
    }

    /**
     * Process the insurance policy. If the policy already exists, update the policy and if it doesn't create a new one.
     * @param policyNumber
     * @param providerOrg
     * @param policyHolder
     * @param insuredDependent
     */
    protected void processInsurancePolicy(final String policyNumber,
                                          final String policyType,
                                          final Organization providerOrg,
                                          final Person policyHolder,
                                          final Person insuredDependent) throws UnknownReferenceTypeException
    {
        final ReferenceEntityFacade referenceEntityService = (ReferenceEntityFacade) ServiceLocator.getInstance().getService(ReferenceEntityFacade.class);
        final InsurancePolicyType type = referenceEntityService.getInsurancePolicyType(policyType);

        // TODO: Need to figure how what's going to be available during registration for INSURANCE (e.g. Product, Plan, etc)
    }


    /**
     * Registers a responsible party associated with a new patient. This is a sub-task belonging to the
     * patient registration process.
     *
     * @param person
     * @param patientParameters
     */
    protected Person registerResponsibleParty(final Person person, final RegisterPatientParameters patientParameters)
    {
        // TODO: FINANCIAL responsible party can be an organization and it can be changed per visit too. Need table to relate to PARTY_RELATIONSHIP.

        final ReferenceEntityFacade referenceEntityService = (ReferenceEntityFacade) ServiceLocator.getInstance().getService(ReferenceEntityFacade.class);
        final PersonFacade personFacade = (PersonFacade) ServiceLocator.getInstance().getService(PersonFacade.class);
        final PartyRelationshipFacade partyRelFacade = (PartyRelationshipFacade) ServiceLocator.getInstance().getService(PartyRelationshipFacade.class);

        // Create a patient role for the new patient
        final PartyRole patientRole = personFacade.addPersonRole(person, PersonRoleType.Cache.PATIENT.getEntity());

        String respLastName = patientParameters.getResponsiblePartyLastName();
        Person respParty = null;
        // if the responsible party ID is available then the person already exists
        if (patientParameters.getResponsiblePartyId() != null)
        {
            respParty = personFacade.getPersonById(patientParameters.getResponsiblePartyId());
        }
        else
        {
            respParty = new Person();
            respParty.setLastName(patientParameters.getResponsiblePartyLastName());
            respParty.setFirstName(patientParameters.getResponsiblePartyFirstName());
            personFacade.addPerson(respParty);
        }
        // responsible party processing
        final PatientResponsiblePartyRoleType entity = PatientResponsiblePartyRoleType.Cache.getEntity(patientParameters.getResponsiblePartyRole());
        final PartyRole respPartyRole = personFacade.addPersonRole(respParty, entity);

        //partyRelFacade.addPartyRelationship(PartyRelationshipType.Cache.PATIENT_RESPONSIBLE_PARTY.getEntity(), patientRole, respPartyRole);
        return respParty;
    }

    /**
     * Registers a primary care provider relationship with the patient. This assumes that the primary care provider already
     * exists.
     * @param patient
     * @param patientParameters
     */
    protected void registerPrimaryCareProvider(final Person patient, final RegisterPatientParameters patientParameters)
    {
        final ReferenceEntityFacade referenceEntityService = (ReferenceEntityFacade) ServiceLocator.getInstance().getService(ReferenceEntityFacade.class);
        final PersonFacade personFacade = (PersonFacade) ServiceLocator.getInstance().getService(PersonFacade.class);
        final PartyRelationshipFacade partyRelFacade = (PartyRelationshipFacade) ServiceLocator.getInstance().getService(PartyRelationshipFacade.class);

        patientParameters.getPrimaryCareProviderId();

        //partyRelFacade.addPartyRelationship(PartyRelationshipType.Cache..getEntity(), patientRole, respPartyRole);
    }

    protected void registerPostalAddress(final Person patient, final RegisterPatientParameters patientParameters)
    {
        final AddContactMechanismService contactMechService = (AddContactMechanismService) ServiceLocator.getInstance().getService(AddContactMechanismService.class);

        try
        {
            contactMechService.addPostalAddress(new AddPostalAddressParameters() {
                public Serializable getPartyId()
                {
                    return patient.getPartyId();
                }

                public String getStreet1()
                {
                    return patientParameters.getStreetAddress1();
                }

                public String getStreet2()
                {
                    return patientParameters.getStreetAddress2();
                }

                public String getCity()
                {
                    return patientParameters.getCity();
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
                    return null;
                    //return patientParameters.getAddressPurpose();
                }

                public String getPurposeDescription()
                {
                    return null;
                }
            });
        }
        catch (Exception e)
        {
            log.error(e);
        }
    }

    public RegisteredPatient registerPatient(final RegisterPatientParameters patientParameters)
    {
        final ReferenceEntityFacade referenceEntityService = (ReferenceEntityFacade) ServiceLocator.getInstance().getService(ReferenceEntityFacade.class);
        final PersonFacade personFacade = (PersonFacade) ServiceLocator.getInstance().getService(PersonFacade.class);

        Person person = new Person();
        try
        {
            person.setLastName(patientParameters.getLastName());
            person.setFirstName(patientParameters.getFirstName());
            person.setMiddleName(patientParameters.getMiddleName());

            final GenderType genderType = referenceEntityService.getGenderType(patientParameters.getGender());
            final MaritalStatusType maritalStatusType = referenceEntityService.getMaritalStatusType(patientParameters.getMaritalStatus());
            person.addMaritalStatus(maritalStatusType);
            person.addGender(genderType);

            // add the languages
            for (String language : patientParameters.getLanguageCodes())
                person.addLanguage(referenceEntityService.getLanguageType(language));

            // add the ethnicities
            for (String ethnicity : patientParameters.getEthnicityCodes())
                person.addEthnicity(referenceEntityService.getEthnicityType(ethnicity));

            if (patientParameters.getSsn() != null)
                person.setSsn(patientParameters.getSsn());
            if (patientParameters.getDriversLicenseNumber() != null)
                person.setDriversLicenseNumber(patientParameters.getDriversLicenseNumber());
                
            // Finally add the person
            personFacade.addPerson(person);
            registerPostalAddress(person, patientParameters);
            Person responsibleParty = registerResponsibleParty(person, patientParameters);
            registerInsuranceInformation(person, responsibleParty, patientParameters);
            registerHomePhone(person, patientParameters);

            final Long patientId = (Long) person.getPersonId();
            final RegisteredPatient patient = new RegisteredPatient()
            {
                public Serializable getPatientId()
                {
                    return patientId;
                }

                public RegisterPatientParameters getRegisterPatientParameters()
                {
                    return patientParameters;
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
                    return patientParameters;
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
        final AddContactMechanismService contactMechService = (AddContactMechanismService) ServiceLocator.getInstance().getService(AddContactMechanismService.class);

        contactMechService.addPhone(new AddPhoneParameters() {
            public Serializable getPartyId()
            {
                return person;
            }

            public String getCountryCode()
            {
                return params.getHomePhoneCountryCode();
            }

            public String getCityCode()
            {
                return params.getHomePhoneCityCode();
            }

            public String getAreaCode()
            {
                return params.getHomePhoneAreaCode();
            }

            public String getNumber()
            {
                return params.getHomePhoneNumber();
            }

            public String getExtension()
            {
                return null;
            }

            public String getPurposeDescription()
            {
                return null;
            }

            public String getPurposeType()
            {
                return ContactMechanismPurposeType.Cache.HOME_PHONE.getCode();
            }
        });
    }

    // TODO: Put a validator and return a list of errors/warnings
    public boolean isValid(RegisterPatientParameters patientParameters)
    {
        assert  patientParameters.getGender() != null :
            "Gender cannot be empty.";
        final String[] languageCodes = patientParameters.getLanguageCodes();
        assert languageCodes != null && languageCodes.length > 0 :
            "There must be at least one language.";
        final String[] ethnicities = patientParameters.getEthnicityCodes();
        assert ethnicities != null && ethnicities.length > 0 :
            "There must be at least one ethnicity.";
        assert patientParameters.getStreetAddress1() != null :
            "Home address cannot be empty.";
        assert patientParameters.getHomePhoneAreaCode() != null && patientParameters.getHomePhoneNumber() != null :
            "Home phone cannot be empty.";
        assert patientParameters.getResponsiblePartyLastName() != null :
            "Responsible party cannot be empty.";
        return false;
    }
}
