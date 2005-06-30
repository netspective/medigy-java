/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.dto.person;

import org.hibernate.validator.NotNull;
import org.hibernate.validator.Past;

import java.util.Date;

import com.medigy.persist.model.person.Gender;

public interface PersonParameters
{
    /**
     * Gets the patient's first name.
     * @return
     */
    @NotNull
    public String getFirstName();

    /**
     * Gets the patient's last name. .
     * @return
     */
    @NotNull
    public String getLastName();

    /**
     * Gets the patient's middle name.
     * @return
     */
    public String getMiddleName();

    /**
     * Gets the patient's suffix name.
     * @return
     */
    public String getSuffix();

    /**
     * Get's the patient's birth date. .
     * @return
     */
    @NotNull
    @Past
    public Date getBirthDate();

    /**
     * Gets the patients gender. .
     * @return
     * @see com.medigy.persist.reference.type.GenderType#getCode()
     */
    @NotNull
    @com.medigy.service.validator.ReferenceEntity(referenceEntityClass = com.medigy.persist.model.person.Gender.class)
    public String getGenderCode();

    /**
     * Gets the patient's marital status..
     * @return
     * @see com.medigy.persist.reference.type.MaritalStatusType#getCode()
     */
    public String getMaritalStatusCode();

    /**
     * Get's the patient's social security number
     * @return
     */
    public String getSsn();

    /**
     * Gets the patient's driver license number
     * @return
     */
    public String getDriversLicenseNumber();

    public String getDriversLicenseStateCode();

    /**
     * Get's the patient's employer name
     * @return
     */
    public String getEmployerName();

    /**
     * Get's the patient's employer unique ID
     * @return
     */
    public String getEmployerId();

    /**
     * Get's the patient's employment title
     * @return
     */
    public String getOccupation();

    /**
     * Get's the patient's ethnicity list. The first one is considered the primary one.
     * @return
     * @see com.medigy.persist.reference.custom.person.EthnicityType#getCode()
     */
    public String[] getEthnicityCodes();

    /**
     * Gets the patient's spoken languages. The first one is considered the primary language.
     * @return
     * @see com.medigy.persist.reference.type.LanguageType#getCode()
     */
    public String[] getLanguageCodes();

}
