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

import com.medigy.service.dto.ServiceParameters;
import com.medigy.service.validator.ValidEntity;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Past;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Interface for DTO containing data specific to the Add Patient service
 */
public interface RegisterPatientParameters extends ServiceParameters
{
    /**
     * Gets the patient's first name.
     * @return first name
     */
    @NotNull
    public String getFirstName();

    /**
     * Gets the patient's last name. .
     * @return last name
     */
    @NotNull
    public String getLastName();

    /**
     * Gets the patient's middle name.
     * @return   middle name
     */
    public String getMiddleName();

    /**
     * Gets the patient's suffix name.
     * @return  suffix
     */
    public String getSuffix();

    /**
     * Get's the patient's birth date. .
     * @return the patient's birth date
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
    @ValidEntity(entity = com.medigy.persist.model.person.Gender.class)
    public String getGenderCode();

    /**
     * Gets the patient's marital status..
     * @return
     * @see com.medigy.persist.reference.type.MaritalStatusType#getCode()
     */
    public String getMaritalStatusCode();

    /**
     * Get's the patient's social security number
     * @return ssn
     */
    public String getSsn();

    /**
     * Gets the patient's driver license number
     * @return  driver's license
     */
    public String getDriversLicenseNumber();

    public String getDriversLicenseStateCode();

    /**
     * Get's the patient's employer name
     * @return employer
     */
    public String getEmployerName();

    /**
     * Get's the patient's employer unique ID
     * @return employer ID
     */
    public String getEmployerId();

    /**
     * Get's the patient's employment title
     * @return occupation
     */
    public String getOccupation();

    /**
     * Get's the patient's ethnicity list. The first one is considered the primary one.
     * @return
     * @see com.medigy.persist.reference.custom.person.EthnicityType#getCode()
     */
    public List<String> getEthnicityCodes();

    /**
     * Gets the patient's spoken languages. The first one is considered the primary language.
     * @return
     * @see com.medigy.persist.reference.type.LanguageType#getCode()
     */
    public List<String> getLanguageCodes();


    /**
     * Get's the responsible party's unique ID.
     * @return  the responsible party's ID
     */
    public String getResponsiblePartyId();

    /**
     * Get's the relationship of the responsible party to the patient
     * @return
     * @see com.medigy.persist.reference.custom.person.PatientResponsiblePartyRoleType#getCode()
     */
    public String getResponsiblePartyRole();

    public String getHomePhone();
    public String getWorkPhone();
    public String getMobilePhone();

    @NotNull
    public String getStreet1();

    public String getStreet2();

    @NotNull
    public String getCity();

    @NotNull
    public String getState();

    public String getProvince();

    @NotNull
    public String getPostalCode();

    public String getCounty();

    @NotNull
    public String getCountry();

    public String getPostalAddressPurposeType();

    public String getPostalAddressPurposeDescription();


    /**
     * Get's the patient's primary care provider ID
     * @return  the primary care provider ID
     */
    public String getPrimaryCareProviderId();


    public Serializable getPrimaryInsuranceCarrierId();


    public Serializable getPrimaryInsuranceProductId();

    @NotNull
    public Serializable getPrimaryInsurancePlanId();

    @NotNull
    public String getPrimaryInsurancePolicyNumber();

    public String getPrimaryInsurancePolicyTypeCode();

    @NotNull
    public String getPrimaryInsuranceGroupNumber();

    @NotNull
    public Serializable getPrimaryInsuranceContractHolderId();

    @NotNull
    public String getPrimaryInsuranceContractHolderRole();

    public Date getPrimaryInsuranceCoverageStartDate();

    public Date getPrimaryInsuranceCoverageEndDate();

    public Float getPrimaryInsuranceIndividualDeductibleAmount();

    public Float getPrimaryInsuranceFamilyDeductibleAmount();

    public Float getPrimaryInsuranceOfficeVisitCoPay();

    public Float getPrimaryInsurancePercentagePay();

    public Float getPrimaryInsuranceMaxThresholdAmount();

}
