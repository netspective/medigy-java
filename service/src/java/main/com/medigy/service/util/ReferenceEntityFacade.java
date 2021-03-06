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
package com.medigy.service.util;

import com.medigy.persist.reference.custom.health.HealthCareLicenseType;
import com.medigy.persist.reference.custom.insurance.InsurancePolicyType;
import com.medigy.persist.reference.custom.insurance.InsuranceProductType;
import com.medigy.persist.reference.custom.insurance.FeeScheduleItemCostType;
import com.medigy.persist.reference.custom.party.ContactMechanismPurposeType;
import com.medigy.persist.reference.custom.party.OrganizationRoleType;
import com.medigy.persist.reference.custom.person.EthnicityType;
import com.medigy.persist.reference.custom.person.PersonRoleType;
import com.medigy.persist.reference.custom.person.PatientType;
import com.medigy.persist.reference.custom.invoice.BillRemittanceType;
import com.medigy.persist.reference.custom.claim.ClaimType;
import com.medigy.persist.reference.custom.CustomReferenceEntity;
import com.medigy.persist.reference.type.GenderType;
import com.medigy.persist.reference.type.LanguageType;
import com.medigy.persist.reference.type.MaritalStatusType;
import com.medigy.persist.reference.ReferenceEntity;

import java.util.List;

public interface ReferenceEntityFacade extends Facade
{
    public List<CustomReferenceEntity> listCustomReferenceEntities(final Class customReferenceEntityClass);

    public CustomReferenceEntity getCustomReferenceEntity(final Class customReferenceEntityClass, final String code);

    public CustomReferenceEntity getCustomReferenceEntity(final Class customReferenceEntityClass, final String code,
                                                          final Long partyId);

    public ReferenceEntity getReferenceEntity(final Class referenceEntityClass, final String code);

    public InsurancePolicyType getInsurancePolicyType(final String code);

    public LanguageType getLanguageType(final String code);

    public GenderType getGenderType(final String genderCode);

    public EthnicityType getEthnicityType(final String ethnicityCode);

    public MaritalStatusType getMaritalStatusType(String statusCode) throws UnknownReferenceTypeException;

    public PersonRoleType getPersonRoleType(String roleCode);

    public OrganizationRoleType getOrganizationRoleType(String roleCode);

    public ContactMechanismPurposeType getContactMechanismPurposeType(String purposeCode)  throws UnknownReferenceTypeException;

    public HealthCareLicenseType getLicenseType(final String licenseType);

    public InsuranceProductType getInsuranceProductType(final String productTypeCode);

    public BillRemittanceType getBillRemittanceType(final String remittanceTypeCode);

    public FeeScheduleItemCostType getFeeScheduleItemCostType(final String costTypeCode);

    public PatientType getPatientType(final String patientTypeCode);

    public ClaimType getClaimType(final String claimTypeCode);
}
