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
 * @author Shahid N. Shah
 */

/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.model.person;

import com.medigy.persist.model.health.HealthCareEncounter;
import com.medigy.persist.model.health.HealthCareEpisode;
import com.medigy.persist.model.health.HealthCareLicense;
import com.medigy.persist.model.health.lab.LabOrder;
import com.medigy.persist.model.insurance.FeeSchedule;
import com.medigy.persist.model.insurance.InsurancePolicy;
import com.medigy.persist.model.insurance.ResponsiblePartySelection;
import com.medigy.persist.model.party.Party;
import com.medigy.persist.reference.custom.health.HealthCareLicenseType;
import com.medigy.persist.reference.custom.person.EthnicityType;
import com.medigy.persist.reference.custom.person.PersonIdentifierType;
import com.medigy.persist.reference.custom.person.PersonRoleType;
import com.medigy.persist.reference.custom.person.PhysicalCharacteristicType;
import com.medigy.persist.reference.type.GenderType;
import com.medigy.persist.reference.type.LanguageType;
import com.medigy.persist.reference.type.MaritalStatusType;
import com.medigy.persist.reference.type.party.PartyType;
import org.apache.commons.codec.language.Soundex;
import org.hibernate.validator.Past;
import org.hibernate.validator.Size;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
//@Table(uniqueConstraints = {@UniqueConstraint(columnNames={"firstName", "lastName", "birthDate"})})
public class Person extends Party
{
    public static final String PK_COLUMN_NAME = Party.PK_COLUMN_NAME;

    private String firstName;
    private String lastName;
    private String middleName;
    private String suffix;
    private Date birthDate;
    private Date deathDate;
    private byte[] photo;

    private String firstNameSoundex;
    private String lastNameSoundex;

    private GenderType currentGenderType;

    private List<PersonIdentifier> personIdentifiers = new ArrayList<PersonIdentifier>();
    private Set<Ethnicity> ethnicities = new HashSet<Ethnicity>();
    private List<Gender> genders = new ArrayList<Gender>();
    private List<MaritalStatus> maritalStatuses = new ArrayList<MaritalStatus>();
    private Set<PhysicalCharacteristic> physicalCharacteristics = new HashSet<PhysicalCharacteristic>();
    private Set<HealthCareEncounter> healthCareEncounters = new HashSet<HealthCareEncounter>();
    private Set<HealthCareEpisode> healthCareEpisodes = new HashSet<HealthCareEpisode>();
    private Set<Language> languages = new HashSet<Language>();
    private Set<HealthCareLicense> licenses = new HashSet<HealthCareLicense>();

    private List<InsurancePolicy> insurancePolicies = new ArrayList<InsurancePolicy>();
    private Set<InsurancePolicy> responsibleInsurancePolicies = new HashSet<InsurancePolicy>();

    private Set<FeeSchedule> feeSchedules = new HashSet<FeeSchedule>();
    private Set<User> users = new HashSet<User>();

    private List<PersonRole> roles = new ArrayList<PersonRole>();
    private List<LabOrder> labOrders = new ArrayList<LabOrder>();
    private List<ResponsiblePartySelection> responsiblePartySelections = new ArrayList<ResponsiblePartySelection>();

    public Person()
    {
        super();
        this.partyType = PartyType.Cache.PERSON.getEntity();
    }

    public Person(final String lastName, final String firstName)
    {
        this();
        setLastName(lastName);
        setFirstName(firstName);
    }

    @Transient
    public Long getPersonId()
    {
        return super.getPartyId();
    }

    protected void setPersonId(final Long personId)
    {
        super.setPartyId(personId);
    }

    @Column(length = 128, nullable = false, name = "first_name")
    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(final String firstName)
    {
        this.firstName = firstName;
        setPartyName(getFullName());
        setFirstNameSoundex(createSoundexName(firstName));
    }

    @Transient
    public String createSoundexName(final String name)
    {
        // using default US_ENGLISH_MAPPING for now
        Soundex soundex = new Soundex();
        return soundex.encode(name);
    }

    @Column(length = 128, nullable = false, name = "last_name")
    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(final String lastName)
    {
        this.lastName = lastName;
        setPartyName(getFullName());
        setLastNameSoundex(createSoundexName(lastName));
    }

    @Column(length = 96, name = "middle_name")
    public String getMiddleName()
    {
        return middleName;
    }

    public void setMiddleName(final String middleName)
    {
        this.middleName = middleName;
        setPartyName(getFullName());
    }

    @Column(name="suffix", length=5)
    public String getSuffix()
    {
        return suffix;
    }

    public void setSuffix(String suffix)
    {
        this.suffix = suffix;
    }

    @Transient
    public String getFullName()
    {
        final StringBuffer sb = new StringBuffer();
        final String firstName = getFirstName();
        final String middleName = getMiddleName();
        final String lastName = getLastName();
        sb.append(firstName);
        if(middleName != null)
        {
            sb.append(' ');
            sb.append(middleName);
        }
        sb.append(' ');
        sb.append(lastName);
        return sb.toString();
    }

    @Transient
    public String getSortableName()
    {
        final StringBuffer sb = new StringBuffer();
        final String firstName = getFirstName();
        final String middleName = getMiddleName();
        final String lastName = getLastName();
        sb.append(lastName);
        sb.append(", ");
        sb.append(firstName);
        if(middleName != null)
        {
            sb.append(' ');
            sb.append(middleName.substring(0, 1));
        }
        return sb.toString();
    }

    @Past
    @Basic(temporalType = TemporalType.DATE)
    @Column(name = "birth_date")
    public Date getBirthDate()
    {
        return birthDate;
    }

    public void setBirthDate(Date birthDate)
    {
        this.birthDate = birthDate;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "person")
    @OrderBy(value = "fromDate asc")           // Order a collection using SQL ordering (not HQL ordering)
    @Size(min = 1)
    public List<Gender> getGenders()
    {
        return genders;
    }

    protected void setGenders(final List<Gender> genders)
    {
        this.genders = genders;
    }

    @Transient
    public void addGender(final GenderType type)
    {
        addGender(type, null, null);
    }

    @Transient
    public void addGender(final GenderType type, final Date fromDate, final Date throughDate)
    {
        final Gender gender = new Gender();
        gender.setType(type);
        gender.setPerson(this);
        if (fromDate != null)
            gender.setFromDate(fromDate);
        else if (birthDate != null)
            gender.setFromDate(birthDate);
        else
            gender.setFromDate(new Date());
        if (throughDate != null)
            gender.setThroughDate(throughDate);
        this.currentGenderType = type;
        this.genders.add(gender);
    }

    public void setCurrentGenderType(final GenderType currentGenderType)
    {
        this.currentGenderType = currentGenderType;
    }

    @ManyToOne
    @JoinColumn(referencedColumnName= GenderType.PK_COLUMN_NAME, name = "curr_gender_type_id")
    public GenderType getCurrentGenderType()
    {
        return currentGenderType;
        /*
        if (genders.size() == 0)
            return GenderType.Cache.UNKNOWN.getEntity();
        TreeSet<Gender> inverseSorted = new TreeSet<Gender>(Collections.reverseOrder());
        inverseSorted.addAll(genders);
        return inverseSorted.first().getType();
        */
    }

    @Transient
    public MaritalStatusType getCurrentMaritalStatus()
    {
        final List<MaritalStatus> maritalStatuses = getMaritalStatuses();
        if(maritalStatuses.size() == 0)
            return MaritalStatusType.Cache.UNKNOWN.getEntity();

        TreeSet<MaritalStatus> inverseSorted = new TreeSet<MaritalStatus>(Collections.reverseOrder());
        inverseSorted.addAll(maritalStatuses);
        return inverseSorted.first().getType();
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "person", fetch = FetchType.LAZY)
    @OrderBy(value = "fromDate asc")
    public List<MaritalStatus> getMaritalStatuses()
    {
        return maritalStatuses;
    }

    protected void setMaritalStatuses(final List<MaritalStatus> maritalStatuses)
    {
        this.maritalStatuses = maritalStatuses;
    }

    @Transient
    public void addMaritalStatus(final MaritalStatusType type)
    {
        addMaritalStatus(type, null, null);
    }

    @Transient
    public void addMaritalStatus(final MaritalStatusType type, final Date fromDate, final Date throughDate)
    {
        final MaritalStatus status = new MaritalStatus();
        status.setType(type);
        status.setPerson(this);
        if (fromDate != null)
            status.setFromDate(fromDate);
        if (throughDate != null)
            status.setThroughDate(throughDate);
        this.maritalStatuses.add(status);
    }

    @Basic(temporalType = TemporalType.DATE)
    @Column(name = "death_date")
    public Date getDeathDate()
    {
        return deathDate;
    }

    public void setDeathDate(final Date deathDate)
    {
        this.deathDate = deathDate;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy =  "person")
    public Set<PhysicalCharacteristic> getPhysicalCharacteristics()
    {
        return physicalCharacteristics;
    }

    public void setPhysicalCharacteristics(final Set<PhysicalCharacteristic> physicalCharacteristics)
    {
        this.physicalCharacteristics = physicalCharacteristics;
    }

    @Transient
    public void addPhysicalCharacteristic(final PhysicalCharacteristicType type, final Long value)
    {
        final PhysicalCharacteristic pc = new PhysicalCharacteristic();
        pc.setType(type);
        pc.setPerson(this);
        pc.setValue(value);
        this.physicalCharacteristics.add(pc);
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "patient", fetch = FetchType.LAZY)
    public Set<HealthCareEncounter> getHealthCareEncounters()
    {
        return healthCareEncounters;
    }

    public void setHealthCareEncounters(final Set<HealthCareEncounter> healthCareEncounters)
    {
        this.healthCareEncounters = healthCareEncounters;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "person")
    public Set<HealthCareEpisode> getHealthCareEpisodes()
    {
        return healthCareEpisodes;
    }

    public void setHealthCareEpisodes(final Set<HealthCareEpisode> healthCareEpisodes)
    {
        this.healthCareEpisodes = healthCareEpisodes;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "person", fetch = FetchType.LAZY)
    public Set<Ethnicity> getEthnicities()
    {
        return ethnicities;
    }

    public void setEthnicities(final Set<Ethnicity> ethnicities)
    {
        this.ethnicities = ethnicities;
    }

    @Transient
    public void addEthnicity(final EthnicityType type)
    {
        final Ethnicity ethnicity = new Ethnicity();
        ethnicity.setType(type);
        ethnicity.setPerson(this);
        ethnicities.add(ethnicity);
    }

    /**
     * Checks to see if the person's ethnicities contains the passed in type
     * @param type
     * @return
     */
    @Transient
    public boolean hasEthnicity(final EthnicityType type)
    {
        final Object[] array = ethnicities.toArray();
        for (int i = 0; i < array.length; i++)
        {
            Ethnicity ethnicity = (Ethnicity) array[i];
            if (ethnicity.getType().equals(type))
                return true;
        }
        return false;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "person", fetch = FetchType.LAZY)
    public Set<Language> getLanguages()
    {
        return languages;
    }

    public void setLanguages(final Set<Language> languages)
    {
        this.languages = languages;
    }

    @Transient
    public void addLanguage(final LanguageType type)
    {
        addLanguage(type, false);
    }

    /**
     * Creates a new language object of the passed in type and adds it to the language list of the person
     * @param type
     * @param isPrimary
     */
    @Transient
    public void addLanguage(final LanguageType type, boolean isPrimary)
    {
        final Language lang = new Language();
        lang.setType(type);
        lang.setPerson(this);
        lang.setPrimaryInd(isPrimary);

        if (getPrimaryLanguage() != null)
        {
            // if there is an existing primary language then clear that one
            getPrimaryLanguage().setPrimaryInd(false);
        }
        languages.add(lang);
    }

    /**
     * Checks to see if the person's languages contains the language type
     * @param type
     * @return
     */
    @Transient
    public boolean speaksLanguage(final LanguageType type)
    {
        final Object[] langList = languages.toArray();
        for (int i=0; i < langList.length; i++)
        {
            if (((Language)langList[i]).getType().equals(type))
                return true;
        }
        return false;
    }

    @Transient
    public Language getPrimaryLanguage()
    {
        final Object[] langList = languages.toArray();
        for (int i=0; i < langList.length; i++)
        {
            if (((Language)langList[i]).getPrimaryInd().booleanValue())
                return ((Language)langList[i]);
        }
        return null;
    }

    @Transient
    protected String getPersonIdentifierValue(final PersonIdentifierType type)
    {
        // this assumes that there is only one type of identifier each
        for (PersonIdentifier pi: personIdentifiers)
        {
            if (pi.getType().equals(type))
            {
                return pi.getIdentifierValue();
            }
        }
        return null;
    }

    @Transient
    public boolean hasPersonIdentifier(final PersonIdentifierType type)
    {
        for (PersonIdentifier pi: personIdentifiers)
        {
            if (pi.getType().equals(type))
            {
                return true;
            }
        }
        return false;
    }

    @Transient
    public String getSsn()
    {
        return getPersonIdentifierValue(PersonIdentifierType.Cache.SSN.getEntity());
    }

    @Transient
    public void setSsn(final String ssn)
    {
        final PersonIdentifier identifier = new PersonIdentifier();
        identifier.setType(PersonIdentifierType.Cache.SSN.getEntity());
        identifier.setIdentifierValue(ssn);
        identifier.setParty(this);
        personIdentifiers.add(identifier);
    }

    @Transient
    public String getDriversLicenseNumber()
    {
        return getPersonIdentifierValue(PersonIdentifierType.Cache.DRIVERS_LICENSE.getEntity());
    }

    @Transient
    public void setDriversLicenseNumber(final String number)
    {
        final PersonIdentifier identifier = new PersonIdentifier();
        identifier.setType(PersonIdentifierType.Cache.DRIVERS_LICENSE.getEntity());
        identifier.setIdentifierValue(number);
        identifier.setParty(this);
        personIdentifiers.add(identifier);
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "person", fetch = FetchType.LAZY)
    public Set<HealthCareLicense> getLicenses()
    {
        return licenses;
    }

    public void setLicenses(final Set<HealthCareLicense> licenses)
    {
        this.licenses = licenses;
    }

    @Transient
    public void addLicense(final HealthCareLicense healthCareLicense)
    {
        healthCareLicense.setPerson(this);
        getLicenses().add(healthCareLicense);
    }

    // TODO: Commented out because migration to HB annotations beta3 is giving  "not-null property references a null or transient value:" error
    /*
    @Lob(type = LobType.BLOB, optional = true)
    public byte[] getPhoto()
    {
        return photo;
    }

    public void setPhoto(final byte[] photo)
    {
        this.photo = photo;
    }
    */

    @Column(name = "first_name_soundex", length = 32)
    public String getFirstNameSoundex()
    {
        return firstNameSoundex;
    }

    public void setFirstNameSoundex(final String firstNameSoundex)
    {
        this.firstNameSoundex = firstNameSoundex;
    }

    @Column(name = "last_name_soundex", length = 32)
    public String getLastNameSoundex()
    {
        return lastNameSoundex;
    }

    public void setLastNameSoundex(final String lastNameSoundex)
    {
        this.lastNameSoundex = lastNameSoundex;
    }

    public String toString()
    {
        return "person{" +
                "identifier=" + getPersonId() +
                ", lastName='" + lastName + "'" +
                ", firstName='" + firstName + "'" +
                ", middleName='" + middleName + "'" +
                ", suffix=" + suffix + "'" +
                ", birthdate= '" + birthDate + "'" +
                ", deathdate= '" + deathDate + "'" +
                "}";
    }

    /**
     * Gets all the insurance policies of this person. This will include policies that have expired also.
     *
     * @return a set of insurance policies
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "insuredPerson")
    public List<InsurancePolicy> getInsurancePolicies()
    {
        return insurancePolicies;
    }

    public void setInsurancePolicies(final List<InsurancePolicy> insurancePolicies)
    {
        this.insurancePolicies = insurancePolicies;
    }

    @Transient
    public void addInsurancePolicy(final InsurancePolicy policy)
    {
        policy.setInsuredPerson(this);
        this.insurancePolicies.add(policy);
    }

    /**
     * Gets all the insurance policies to which this person is financially responsible for. This will also include
     * his/her own policies which they are responsible for.
     * @return a set of insurance policies
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contractHolderPerson", fetch = FetchType.LAZY)
    public Set<InsurancePolicy> getResponsibleInsurancePolicies()
    {
        return responsibleInsurancePolicies;
    }

    public void setResponsibleInsurancePolicies(final Set<InsurancePolicy> responsibleInsurancePolicies)
    {
        this.responsibleInsurancePolicies = responsibleInsurancePolicies;
    }

    @Transient
    public void addResponsibleInsurancePolicy(final InsurancePolicy policy)
    {
        this.responsibleInsurancePolicies.add(policy);
    }

    /**
     * Helper method to create a shell person having a patient role
     * @return the new person with a patient role attached
     */
    @Transient
    public static Person createNewPatient()
    {
        final Person patient = new Person();
        patient.addRole(PersonRoleType.Cache.PATIENT.getEntity(), true);
        return patient;
    }

    /**
     * Helper method to create a shell person having a physician role
     * @return  the new person with a physician role attached
     */
    @Transient
    public static Person createNewPhysician()
    {
        final Person doctor = new Person();
        doctor.addRole(PersonRoleType.Cache.INDIVIDUAL_HEALTH_CARE_PRACTITIONER.getEntity(), true);
        return doctor;
    }

    @Transient
    public HealthCareLicense getLicense(final HealthCareLicenseType type)
    {
        for (HealthCareLicense license : getLicenses())
        {
            if (license.getType().equals(type))
                return license;
        }
        return null;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "person")
    public Set<FeeSchedule> getFeeSchedules()
    {
        return feeSchedules;
    }

    public void setFeeSchedules(final Set<FeeSchedule> feeSchedules)
    {
        this.feeSchedules = feeSchedules;
    }

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    public Set<User> getUsers()
    {
        return users;
    }

    public void setUsers(final Set<User> users)
    {
        this.users = users;
    }

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    public List<PersonRole> getRoles()
    {
        return roles;
    }

    public void setRoles(final List<PersonRole> roles)
    {
        this.roles = roles;
    }

    @Transient
    public void addRole(final PersonRole role)
    {
        this.roles.add(role);
    }

    @Transient
    public PersonRole addRole(final PersonRoleType type)
    {
        return addRole(type, false);
    }

    @Transient
    public PersonRole addRole(final PersonRoleType type, final boolean isPrimary)
    {
        final PersonRole role = new PersonRole();
        role.setPerson(this);
        role.setType(type);
        role.setIsPrimaryRole(isPrimary);
        this.roles.add(role);
        return role;
    }

    @Transient
    public PersonRole getPrimaryRole()
    {
        for (PersonRole role: roles)
        {
            if (role.getIsPrimaryRole())
                return role;
        }
        return null;
    }

    @Transient
    public PersonRole getRole(final PersonRoleType entity)
    {
        for (PersonRole role: roles)
        {
            if (role.getType().equals(entity))
                return role;
        }
        return null;
    }

    @Transient
    public boolean hasRole(final PersonRoleType entity)
    {
        for (PersonRole role: roles)
        {
            if (role.getType().equals(entity))
                return true;
        }
        return false;
    }

    @OneToMany(mappedBy = "patient")
    public List<LabOrder> getLabOrders()
    {
        return labOrders;
    }

    public void setLabOrders(final List<LabOrder> labOrders)
    {
        this.labOrders = labOrders;
    }

    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL)
    public List<PersonIdentifier> getPersonIdentifiers()
    {
        return personIdentifiers;
    }

    public void setPersonIdentifiers(final List<PersonIdentifier> personIdentifiers)
    {
        this.personIdentifiers = personIdentifiers;
    }

    @Transient
    public void addPersonIdentifier(final PersonIdentifier ident)
    {
        this.personIdentifiers.add(ident);
    }

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    public List<ResponsiblePartySelection> getResponsiblePartySelections()
    {
        return responsiblePartySelections;
    }

    public void setResponsiblePartySelections(final List<ResponsiblePartySelection> responsiblePartySelections)
    {
        this.responsiblePartySelections = responsiblePartySelections;
    }

    @Transient
    public ResponsiblePartySelection getDefaultResponsiblePartySelection()
    {
        for (ResponsiblePartySelection sel: responsiblePartySelections)
        {
            if (sel.isDefaultSelection())
                return sel;
        }
        return null;
    }

    @Transient
    public void addResponsiblePersonSelection(final Person person, final PeopleRelationship rel, final boolean isDefault)
    {
        final ResponsiblePartySelection selection = new ResponsiblePartySelection();
        selection.setPatient(this);
        selection.setPeopleRelationship(rel);
        selection.setDefaultSelection(isDefault);

        if (isDefault)
        {
            final ResponsiblePartySelection defaultSel = getDefaultResponsiblePartySelection();
            if (defaultSel != null)
                defaultSel.setDefaultSelection(false);
        }
        responsiblePartySelections.add(selection);
    }
}
