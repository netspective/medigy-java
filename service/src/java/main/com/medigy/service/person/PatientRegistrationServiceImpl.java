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

import com.medigy.persist.model.insurance.InsurancePolicy;
import com.medigy.persist.model.org.Organization;
import com.medigy.persist.model.party.PartyRole;
import com.medigy.persist.model.person.Person;
import com.medigy.persist.reference.custom.insurance.InsurancePolicyType;
import com.medigy.persist.reference.custom.party.ContactMechanismPurposeType;
import com.medigy.persist.reference.custom.party.OrganizationRoleType;
import com.medigy.persist.reference.custom.party.PartyRelationshipType;
import com.medigy.persist.reference.custom.person.PatientResponsiblePartyRoleType;
import com.medigy.persist.reference.custom.person.PersonRoleType;
import com.medigy.persist.reference.type.GenderType;
import com.medigy.persist.reference.type.MaritalStatusType;
import com.medigy.persist.util.HibernateUtil;
import com.medigy.service.ServiceLocator;
import com.medigy.service.common.ReferenceEntityLookupService;
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
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;

/**
 * Service class for registering a new patient
 */
public class PatientRegistrationServiceImpl implements PatientRegistrationService
{
    private static final Log log = LogFactory.getLog(PatientRegistrationServiceImpl.class);

    private static Serializable getPrimaryKey(String keyString)
    {
        return Long.parseLong(keyString);
    }

    protected void registerInsuranceInformation(final Person person, final RegisterPatientParameters params) throws UnknownReferenceTypeException
    {
        final ReferenceEntityLookupService referenceEntityService = (ReferenceEntityLookupService) ServiceLocator.getInstance().getService(ReferenceEntityLookupService.class);

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
            final InsurancePolicyType type = referenceEntityService.getInsurancePolicyType(policyType);
            final String contractHolderId = contractHolderIds[i];
            final String contractHolderFirstName = contractHolderFirstNames[i];
            final String contractHolderLastName = contractHolderLastNames[i];
            final String policyProvider = policyProviders[i];
            final String policyProviderId = policyProviderIds[i];

            Person policyHolder = null;
            if (contractHolderId == null && contractHolderLastName != null)
            {
                // create a new contract holder but check if this is the same person as the responsible party
                if (params.getResponsiblePartyLastName().equals(contractHolderLastName) &&
                    params.getResponsiblePartyFirstName().equals(contractHolderFirstName))
                {
                    // TODO:
                }
                else
                {
                    policyHolder = new Person();
                    policyHolder.setLastName(contractHolderLastName);
                    policyHolder.setFirstName(contractHolderFirstName);
                    HibernateUtil.getSession().save(policyHolder);
                }
            }
            else
            {
                // NOTE: this will throw HibernateException if the ID doesnt exist
                policyHolder = (Person) HibernateUtil.getSession().load(Person.class,
                        getPrimaryKey(contractHolderId));
            }

            Organization providerOrg = null;
            if (policyProviderId != null)
            {
                providerOrg = (Organization) HibernateUtil.getSession().load(Organization.class,
                        getPrimaryKey(policyProviderId));
            }
            else
            {
                providerOrg = new Organization();
                providerOrg.setOrganizationName(policyProvider);
                providerOrg.addPartyRole(OrganizationRoleType.Cache.INSURANCE_PROVIDER.getEntity());
                HibernateUtil.getSession().save(providerOrg);
            }

            boolean newPolicy = false;

            Criteria criteria = HibernateUtil.getSession().createCriteria(InsurancePolicy.class);
            criteria.add(Restrictions.eq("policyNumber", policyNumbers[i]));
            Criteria providerCriteria = criteria.createCriteria("insuranceProvider");
            providerCriteria.add(Restrictions.eq("partyId", providerOrg.getPartyId()));
            Criteria policyHolderCriteria = criteria.createCriteria("pol");

            InsurancePolicy policy = new InsurancePolicy();
            policy.setType(type);
            policy.setPolicyNumber(policyNumbers[i]);
            policy.setInsuranceProvider(providerOrg);
            policy.setPolicyHolder(policyHolder);
            policy.addInsuredDependent(person);

            if (newPolicy)
                HibernateUtil.getSession().save(newPolicy);
            else
                HibernateUtil.getSession().flush();
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
        final ReferenceEntityLookupService referenceEntityService = (ReferenceEntityLookupService) ServiceLocator.getInstance().getService(ReferenceEntityLookupService.class);
        final PersonFacade personFacade = (PersonFacade) ServiceLocator.getInstance().getService(PersonFacade.class);
        final PartyRelationshipFacade partyRelFacade = (PartyRelationshipFacade) ServiceLocator.getInstance().getService(PartyRelationshipFacade.class);

        // Create a patient role for the new patient
        final PartyRole patientRole = personFacade.addPersonRole(person, PersonRoleType.Cache.PATIENT.getEntity());

        String respLastName = patientParameters.getResponsiblePartyLastName();
        if (respLastName != null)
        {
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

            // create a relationship between the patient and this person through the roles
            partyRelFacade.addPartyRelationship(PartyRelationshipType.Cache.PATIENT_RESPONSIBLE_PARTY.getEntity(), patientRole, respPartyRole);
        }
    }

    /**
     * Registers a primary care provider relationship with the patient. This assumes that the primary care provider already
     * exists.
     * @param patient
     * @param patientParameters
     */
    protected void registerPrimaryCareProvider(final Person patient, final RegisterPatientParameters patientParameters)
    {
        final ReferenceEntityLookupService referenceEntityService = (ReferenceEntityLookupService) ServiceLocator.getInstance().getService(ReferenceEntityLookupService.class);
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

                public String getPurpose()
                {
                    return null;
                    //return patientParameters.getAddressPurpose();
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
        final ReferenceEntityLookupService referenceEntityService = (ReferenceEntityLookupService) ServiceLocator.getInstance().getService(ReferenceEntityLookupService.class);
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
            registerResponsibleParty(person, patientParameters);
            registerInsuranceInformation(person, patientParameters);
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
            };
            if (log.isInfoEnabled())
                log.info("New PERSON created with id = " + patient.getPatientId());
           return patient;
        }
        catch (Exception e)
        {
            log.error(e);
        }
        return null;
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

            public String getPurpose()
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
