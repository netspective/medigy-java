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

import com.medigy.persist.model.health.HealthCareEpisode;
import com.medigy.persist.model.health.HealthCareLicense;
import com.medigy.persist.model.health.HealthCareVisit;
import com.medigy.persist.model.insurance.InsurancePolicy;
import com.medigy.persist.model.insurance.FeeSchedule;
import com.medigy.persist.model.party.Party;
import com.medigy.persist.model.party.PartyIdentifier;
import com.medigy.persist.reference.custom.health.HealthCareLicenseType;
import com.medigy.persist.reference.custom.party.PartyIdentifierType;
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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.LobType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
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

    //private Set<CareProviderSelection> careProviderSelections = new HashSet<CareProviderSelection>();

    private Set<Ethnicity> ethnicities = new HashSet<Ethnicity>();
    private Set<Gender> genders = new HashSet<Gender>();
    private Set<MaritalStatus> maritalStatuses = new HashSet<MaritalStatus>();
    private Set<PhysicalCharacteristic> physicalCharacteristics = new HashSet<PhysicalCharacteristic>();
    private Set<HealthCareVisit> healthCareVisits = new HashSet<HealthCareVisit>();
    private Set<HealthCareEpisode> healthCareEpisodes = new HashSet<HealthCareEpisode>();
    private Set<Language> languages = new HashSet<Language>();
    private Set<HealthCareLicense> licenses = new HashSet<HealthCareLicense>();

    private Set<InsurancePolicy> insurancePolicies = new HashSet<InsurancePolicy>();
    private Set<InsurancePolicy> responsibleInsurancePolicies = new HashSet<InsurancePolicy>();

    private Set<FeeSchedule> feeSchedules = new HashSet<FeeSchedule>();
    private Set<User> users = new HashSet<User>();

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

    @Column(length = 128, nullable = false)
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

    @Column(length = 128, nullable = false)
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

    @Column(length = 96)
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

    @Column()
    @Past
    public Date getBirthDate()
    {
        return birthDate;
    }

    public void setBirthDate(Date birthDate)
    {
        this.birthDate = birthDate;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "person")
    @Size(min = 1)
    public Set<Gender> getGenders()
    {
        return genders;
    }

    protected void setGenders(final Set<Gender> genders)
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
        if (throughDate != null)
            gender.setThroughDate(throughDate);
        this.genders.add(gender);
    }

    @Transient
    public GenderType getCurrentGender()
    {
        final Set<Gender> genders = getGenders();
        if (genders.size() == 0)
            return GenderType.Cache.UNKNOWN.getEntity();
        TreeSet<Gender> inverseSorted = new TreeSet<Gender>(Collections.reverseOrder());
        inverseSorted.addAll(genders);
        return inverseSorted.first().getType();
    }

    @Transient
    public MaritalStatusType getCurrentMaritalStatus()
    {
        final Set<MaritalStatus> maritalStatuses = getMaritalStatuses();
        if(maritalStatuses.size() == 0)
            return MaritalStatusType.Cache.UNKNOWN.getEntity();

        TreeSet<MaritalStatus> inverseSorted = new TreeSet<MaritalStatus>(Collections.reverseOrder());
        inverseSorted.addAll(maritalStatuses);
        return inverseSorted.first().getType();
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "person", fetch = FetchType.LAZY)
    public Set<MaritalStatus> getMaritalStatuses()
    {
        return maritalStatuses;
    }

    protected void setMaritalStatuses(final Set<MaritalStatus> maritalStatuses)
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
    public Set<HealthCareVisit> getHealthCareVisits()
    {
        return healthCareVisits;
    }

    public void setHealthCareVisits(final Set<HealthCareVisit> healthCareVisits)
    {
        this.healthCareVisits = healthCareVisits;
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
    protected String getPartyIdentifierValue(final PartyIdentifierType type)
    {
        // this assumes that there is only one type of identifier each
        for (PartyIdentifier pi: partyIdentifiers)
        {
            if (pi.getType().equals(type))
            {
                return pi.getIdentifierValue();
            }
        }
        return null;
    }

    @Transient
    public boolean hasPartyIdentifier(final PartyIdentifierType type)
    {
        for (PartyIdentifier pi: partyIdentifiers)
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
        return getPartyIdentifierValue(PersonIdentifierType.Cache.SSN.getEntity());
    }

    @Transient
    public void setSsn(final String ssn)
    {
        final PartyIdentifier identifier = new PartyIdentifier();
        identifier.setType(PersonIdentifierType.Cache.SSN.getEntity());
        identifier.setIdentifierValue(ssn);
        identifier.setParty(this);
        partyIdentifiers.add(identifier);
    }

    @Transient
    public String getDriversLicenseNumber()
    {
        return getPartyIdentifierValue(PersonIdentifierType.Cache.DRIVERS_LICENSE.getEntity());
    }

    @Transient
    public void setDriversLicenseNumber(final String number)
    {
        final PartyIdentifier identifier = new PartyIdentifier();
        identifier.setType(PersonIdentifierType.Cache.DRIVERS_LICENSE.getEntity());
        identifier.setIdentifierValue(number);
        identifier.setParty(this);
        partyIdentifiers.add(identifier);
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

    public String getFirstNameSoundex()
    {
        return firstNameSoundex;
    }

    public void setFirstNameSoundex(final String firstNameSoundex)
    {
        this.firstNameSoundex = firstNameSoundex;
    }

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
                //", gender='" + getCurrentGender().getLabel() + "'" +
                ", maritalStatuses=" + maritalStatuses +
                //", contactMechanisms=" + getPartyContactMechanisms() +
                "}";
    }

    /**
     * Gets all the insurance policies of this person. This will include policies that have expired also.
     *
     * @return a set of insurance policies
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "insuredPerson", fetch = FetchType.LAZY)
    public Set<InsurancePolicy> getInsurancePolicies()
    {
        return insurancePolicies;
    }

    public void setInsurancePolicies(final Set<InsurancePolicy> insurancePolicies)
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
        patient.addPartyRole(PersonRoleType.Cache.PATIENT.getEntity());
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
        doctor.addPartyRole(PersonRoleType.Cache.INDIVIDUAL_HEALTH_CARE_PRACTITIONER.getEntity());
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
}
