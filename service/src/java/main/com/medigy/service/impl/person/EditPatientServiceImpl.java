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
import com.medigy.persist.model.person.Gender;
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
import com.medigy.service.dto.person.EditPatient;
import com.medigy.service.dto.person.EditPatientParameters;
import com.medigy.service.insurance.InsurancePolicyFacade;
import com.medigy.service.person.EditPatientService;
import com.medigy.service.person.PersonFacade;
import com.medigy.service.util.ReferenceEntityFacade;
import com.medigy.service.util.UnknownReferenceTypeException;
import com.medigy.service.validator.ValidEntity;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Past;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Service class for editing an existing patient
 */
public class EditPatientServiceImpl extends AbstractService implements EditPatientService
{
    private static final Log log = LogFactory.getLog(EditPatientServiceImpl.class);
    private boolean createEmployerIfUnknown = true;
    private boolean createInsuranceProviderIfUnknown = true;


    private ReferenceEntityFacade referenceEntityFacade;
    private PersonFacade personFacade;
    private ContactMechanismFacade contactMechanismFacade;
    private InsurancePolicyFacade insurancePolicyFacade;

    public EditPatientServiceImpl(final SessionFactory sessionFactory, final ReferenceEntityFacade referenceEntityFacade, final PersonFacade personFacade,
        final ContactMechanismFacade contactMechanismFacade, final InsurancePolicyFacade insurancePolicyFacade)
    {
        super(sessionFactory);
        this.referenceEntityFacade = referenceEntityFacade;
        this.personFacade = personFacade;
        this.contactMechanismFacade = contactMechanismFacade;
        this.insurancePolicyFacade = insurancePolicyFacade;
    }


    public EditPatient editPatient(final EditPatientParameters params)
    {
        Person person = (Person)getSession().get(Person.class,  params.getPersonId());
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
            //editPostalAddress(person, params);
            //editInsuranceInformation(person, params);
            if(params.getHomePhone() != null)
                editHomePhone(person, params);

            final Long patientId = (Long) person.getPersonId();
            final EditPatient patient = new EditPatient()
            {
                public Serializable getPatientId()
                {
                    return patientId;
                }

                public EditPatientParameters getEditPatientParameters()
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
            log.error(ExceptionUtils.getStackTrace(e));
            return createErrorResponse(params, e.getMessage());
        }
    }

    public EditPatientParameters getEditPatientParameters(Serializable personId)
    {
        final Person person = this.personFacade.getPersonById(personId);
        EditPatientBean patient = new EditPatientBean();



        return new EditPatientParameters() {

            public Long getPersonId()
            {
                return person.getPersonId();
            }

            public String getFirstName()
            {
                return person.getFirstName();
            }

            public String getLastName()
            {
                return person.getLastName();
            }

            public String getMiddleName()
            {
                return person.getMiddleName();
            }

            public String getSuffix()
            {
                return person.getSuffix();
            }

            public Date getBirthDate()
            {
                return person.getBirthDate();
            }

            public Date getDeathDate()
            {
                return person.getDeathDate();
            }

            public String getGenderCode()
            {
                return null;//person.getGenders().iterator().next().getType().toString();
            }

            public String getMaritalStatusCode()
            {
                return null; //person.getMaritalStatuses().iterator().next().getType().toString();
            }

            public String getSsn()
            {
                return person.getSsn();
            }

            public String getDriversLicenseNumber()
            {
                return null;//person.getDriversLicenseNumber();
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
                return null;//new ArrayList<String>();  // TODO: figure out how to find occupation.
            }

            public List<String> getLanguageCodes()
            {
                return null;//new ArrayList<String>(); // TODO: figure out how to find occupation.
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

    private PhoneNumber editHomePhone(final Person person, final EditPatientParameters params)
    {
        // TODO: create edit phone method
        return contactMechanismFacade.addPhone(null, null, params.getHomePhone());
    }

    // TODO: Put a validator and return a list of errors/warnings
    public String[] isValid(ServiceParameters params)
    {
        EditPatientParameters patientParameters = (EditPatientParameters) params;
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

    protected void editInsuranceInformation(final Person person, final EditPatientParameters params) throws Exception
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
    protected void editResponsibleParty(final Person person, final EditPatientParameters patientParameters)
    {
        // TODO: FINANCIAL responsible party can be an organization and it can be changed per visit too. Need table to relate to PARTY_RELATIONSHIP.

    }

    /**
     * Registers a primary care provider relationship with the patient. This assumes that the primary care provider already
     * exists.
     * @param patient
     * @param patientParameters
     */
    protected void editPrimaryCareProvider(final Person patient, final EditPatientParameters patientParameters)
    {


    }

    protected void editPostalAddress(final Person patient, final EditPatientParameters patientParameters) throws UnknownReferenceTypeException
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


    public EditPatient createErrorResponse(final ServiceParameters params, final String errorMessage)
    {
        return new EditPatient()
        {
            public Serializable getPatientId()
            {
                return null;
            }

            public EditPatientParameters getEditPatientParameters()
            {
                return (EditPatientParameters) params;
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

    public class EditPatientBean implements EditPatientParameters
    {
        private String firstName;
        private String lastName;
        private String middleName;
        private String suffix;
        private Date birthDate;
        private Date deathDate;
        private String genderCode;
        private String maritalStatusCode;
        private String ssn;
        private String driversLicenseNumber;
        private String driversLicenseStateCode;
        private String employerName;
        private String employerId;
        private String occupation;
        private List<String> ethnicityCodes;
        private List<String> languageCodes;
        private Long personId;
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
        private String individualDeductibleRemaining;
        private String familyDeductibleRemaining;
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

        @NotNull public Long getPersonId()
        {
            return personId;
        }

        public void setPersonId(Long id)
        {
            this.personId = id;
        }

        @NotNull public String getFirstName()
        {
            return firstName;
        }

        public void setFirstName(String firstName)
        {
            this.firstName = firstName;
        }

        @NotNull public String getLastName()
        {
            return lastName;
        }

        public void setLastName(String lastName)
        {
            this.lastName = lastName;
        }

        public String getMiddleName()
        {
            return middleName;
        }

        public void setMiddleName(String middleName)
        {
            this.middleName = middleName;
        }

        public String getSuffix()
        {
            return suffix;
        }

        public void setSuffix(String suffix)
        {
            this.suffix = suffix;
        }

        @NotNull @Past public Date getBirthDate()
        {
            return birthDate;
        }

        public void setBirthDate(Date birthDate)
        {
            this.birthDate = birthDate;
        }

        @NotNull @Past public Date getDeathDate()
        {
            return deathDate;
        }

        public void setDeathDate(Date deathDate)
        {
            this.deathDate = deathDate;
        }

        @NotNull @ValidEntity(entity = Gender.class) public String getGenderCode()
        {
            return this.genderCode;
        }

        public void setGenderCode(String genderCode)
        {
            this.genderCode = genderCode;
        }

        public String getMaritalStatusCode()
        {
            return maritalStatusCode;
        }

        public void setMaritalStatusCode(String maritalStatusCode)
        {
            this.maritalStatusCode = maritalStatusCode;
        }

        public String getSsn()
        {
            return ssn;
        }

        public void setSsn(String ssn)
        {
            this.ssn = ssn;
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

        public String getEmployerName()
        {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public String getEmployerId()
        {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public String getOccupation()
        {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public List<String> getEthnicityCodes()
        {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public List<String> getLanguageCodes()
        {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public String getResponsiblePartyId()
        {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public String getResponsiblePartyRole()
        {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public String getHomePhone()
        {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public String getWorkPhone()
        {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public String getMobilePhone()
        {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @NotNull public String getStreet1()
        {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public String getStreet2()
        {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @NotNull public String getCity()
        {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @NotNull public String getState()
        {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public String getProvince()
        {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @NotNull public String getPostalCode()
        {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
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

        public ServiceVersion getServiceVersion()
        {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }
    }
}
