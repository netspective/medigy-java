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
package com.medigy.service.dto.person;

import com.medigy.service.ServiceVersion;

import java.util.Date;

/**
 * Interface for DTO containing data specific to the Add Patient service
 */
public interface RegisterPatientParameters extends ServiceVersion
{
    /**
     * Gets the patient's first name. REQUIRED.
     * @return
     */
    public String getFirstName();

    /**
     * Gets the patient's last name. REQUIRED.
     * @return
     */
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
     * Get's the patient's birth date. REQUIRED.
     * @return
     */
    public Date getBirthDate();

    /**
     * Gets the patients gender. REQUIRED.
     * @return
     * @see com.medigy.persist.reference.type.GenderType#getTypeId()
     */
    //@referenceType
    public String getGender();

    /**
     * Gets the patient's marital status. REQUIRED.
     * @return
     * @see com.medigy.persist.reference.type.MaritalStatusType#getTypeId()
     */
    public String getMaritalStatus();

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
     * @see com.medigy.persist.reference.type.LanguageType#getTypeId()
     */
    public String[] getLanguageCodes();

    /**
     * Get's the responsible party's last name. REQUIRED.
     * @return
     */
    public String getResponsiblePartyLastName();

    /**
     * Get's the responsible party's first name. REQUIRED.
     * @return
     */
    public String getResponsiblePartyFirstName();

    public String getResponsiblePartySuffix();

    /**
     * Get's the responsible party's unique ID
     * @return
     */
    public String getResponsiblePartyId();

    /**
     * Get's the relationship of the responsible party to the patient
     * @return
     * @see com.medigy.persist.reference.custom.person.PatientResponsiblePartyRoleType#getCode()
     */
    public String getResponsiblePartyRole();

    public String getHomePhoneCountryCode();    // optional
    public String getHomePhoneCityCode();       // optional
    public String getHomePhoneAreaCode();
    public String getHomePhoneNumber();

    public String getWorkPhoneCountryCode();    // optional
    public String getWorkPhoneCityCode();       // optional
    public String getWorkPhoneAreaCode();
    public String getWorkPhoneNumber();

    public String getMobilePhoneCountryCode();  // optional
    public String getMobilePhoneCityCode();     // optional
    public String getMobilePhoneAreaCode();
    public String getMobilePhoneNumber();

    public String getStreetAddress1();
    public String getStreetAddress2();
    public String getCity();
    public String getCounty();
    public String getProvince();
    public String getState();
    public String getPostalCode();
    public String getCountry();

    /**
     * Get's the patient's primary care provider ID
     * @return
     */
    public String getPrimaryCareProviderId();

    /**
     * Get's the patient's primary care provider last name
     * @return
     */
    public String getPrimaryCareProviderLastName();

    /**
     * Gets the patient's primary care provider first name
     * @return
     */
    public String getPrimaryCareProviderFirstName();

    /**
     * Gets the list of insurance policy numbers. The first one is considered
     * the primary insurance policy.
     * @return
     */
    public String[] getInsurancePolicyNumbers();

    /**
     * Gets the list of insurance policy providers.
     * @return
     */
    public String[] getInsurancePolicyProviders();
    public String[] getInsurancePolicyProviderIds();

    /**
     * Gets the type of insurance policy
     * @return
     * @see com.medigy.persist.reference.custom.insurance.InsurancePolicyType#getCode()
     */
    public String[] getInsurancePolicyTypes();
    public String[] getInsurancePolicyHolderLastNames();
    public String[] getInsurancePolicyHolderFirstNames();
    public String[] getInsurancePolicyHolderSuffix();
    /**
     * Gets the list of ID of the insurance policy holders
     * @return
     */
    public String[] getInsurancePolicyHolderId();

    /**
     * Gets the list of roles (relationship to the patient) of the insurance policy
     * holder.
     * @return
     */
    public String[] getInsurancePolicyHolderRole();
}
