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
package com.medigy.service.impl.util;

import com.medigy.persist.reference.custom.health.HealthCareLicenseType;
import com.medigy.persist.reference.custom.insurance.FeeScheduleItemCostType;
import com.medigy.persist.reference.custom.insurance.InsurancePolicyType;
import com.medigy.persist.reference.custom.insurance.InsuranceProductType;
import com.medigy.persist.reference.custom.invoice.BillRemittanceType;
import com.medigy.persist.reference.custom.party.ContactMechanismPurposeType;
import com.medigy.persist.reference.custom.party.OrganizationRoleType;
import com.medigy.persist.reference.custom.person.EthnicityType;
import com.medigy.persist.reference.custom.person.PatientType;
import com.medigy.persist.reference.custom.person.PersonRoleType;
import com.medigy.persist.reference.custom.claim.ClaimType;
import com.medigy.persist.reference.custom.CustomReferenceEntity;
import com.medigy.persist.reference.type.GenderType;
import com.medigy.persist.reference.type.LanguageType;
import com.medigy.persist.reference.type.MaritalStatusType;
import com.medigy.persist.reference.ReferenceEntity;
import com.medigy.persist.model.party.Party;
import com.medigy.service.util.AbstractFacade;
import com.medigy.service.util.ReferenceEntityFacade;
import com.medigy.service.util.UnknownReferenceTypeException;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.Query;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Restrictions;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.util.List;
import java.util.ArrayList;

public class ReferenceEntityFacadeImpl extends AbstractFacade implements ReferenceEntityFacade
{
    private final Log log = LogFactory.getLog(ReferenceEntityFacadeImpl.class);


    public ReferenceEntityFacadeImpl(final SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }

    /**
     * Gets a list of all the custom reference entites of the requested class. This method assumes that
     * the list being requested is for reference entities belonging to the SYS GLOBAL scope.
     *
     * @param customReferenceEntityClass
     * @return list of custom reference entities
     */
    public List<CustomReferenceEntity> listCustomReferenceEntities(final Class customReferenceEntityClass)
    {
        if (!CustomReferenceEntity.class.isAssignableFrom(customReferenceEntityClass))
            return null;
        List<CustomReferenceEntity> newList = new ArrayList<CustomReferenceEntity>();
        try
        {
            List list = getSession().createCriteria(customReferenceEntityClass).list();
            convert(customReferenceEntityClass, list, newList);
        }
        catch (Exception e)
        {
            log.error(ExceptionUtils.getStackTrace(e));
            final String error = e.getMessage();
            newList.add(new CustomReferenceEntity() {
                public Long getSystemId()
                {
                    return null;
                }

                public String getCode()
                {
                    return error;
                }

                public String getLabel()
                {
                    return error;
                }

                public Party getParty()
                {
                    return null;
                }
            });
        }
        return newList;
    }

    public CustomReferenceEntity getCustomReferenceEntity(final Class customReferenceEntityClass, final String code)
    {
        return getCustomReferenceEntity(customReferenceEntityClass, code, null);
    }

    public CustomReferenceEntity getCustomReferenceEntity(final Class customReferenceEntityClass, final String code,
                                                          final Long partyId)
    {
        if (!CustomReferenceEntity.class.isAssignableFrom(customReferenceEntityClass))
            return null;
        try
        {
            Query query = null;
            if (partyId == null)
            {
                query = getSession().createQuery("from " + customReferenceEntityClass.getName() + "  where " +
                    "code = ? and party.id = ?");
                query.setString(0, code);
                query.setLong(1, Party.Cache.SYS_GLOBAL_PARTY.getEntity().getPartyId());
                return (CustomReferenceEntity) query.uniqueResult();
            }
            else
            {
                query = getSession().createQuery("from " + customReferenceEntityClass.getName() + "  where " +
                    "code = ? and (party.id = ? or party.id = ?)");
                query.setString(0, code);
                query.setLong(1, partyId);
                query.setLong(2, Party.Cache.SYS_GLOBAL_PARTY.getEntity().getPartyId());
                return (CustomReferenceEntity) query.uniqueResult();
            }
        }
        catch (Exception e)
        {
            log.error(ExceptionUtils.getStackTrace(e));
            return null;
        }
    }

    public ReferenceEntity getReferenceEntity(final Class referenceEntityClass, final String code)
    {
        if (!ReferenceEntity.class.isAssignableFrom(referenceEntityClass))
            return null;
        try
        {
            return (ReferenceEntity) getSession().createCriteria(referenceEntityClass).add(Restrictions.eq("code", code)).uniqueResult();
        }
        catch (Exception e)
        {
            log.error(ExceptionUtils.getStackTrace(e));
            return null;
        }
    }



    public InsurancePolicyType getInsurancePolicyType(final String code)
    {
        InsurancePolicyType type = InsurancePolicyType.Cache.getEntity(code);
        if (type == null)
        {
            final Criteria criteria = getSession().createCriteria(InsurancePolicyType.class);
            criteria.add(Restrictions.eq("code", code));
            type = (InsurancePolicyType) criteria.uniqueResult();
        }
        return type;
    }

    /**
     * Gets the language type based on the code
     *
     * @param code      Language code
     * @return
     */
    public LanguageType getLanguageType(final String code)
    {
        return LanguageType.Cache.getEntity(code);
    }

    /**
     * Gets the gender type based on the code
     * @param genderCode
     * @return
     */
    public GenderType getGenderType(final String genderCode)
    {
        return GenderType.Cache.getEntity(genderCode);
    }

    /**
     * Gets the ethnicity type based on the code. This will look at static cached items
     * first and only when no match is found it will query the underlying DB to get the
     * reference entity.
     *
     * @param ethnicityCode
     * @return
     */
    public EthnicityType getEthnicityType(final String ethnicityCode)
    {
        EthnicityType type = EthnicityType.Cache.getEntity(ethnicityCode);
        if (type == null)
        {
            final Criteria criteria = getSession().createCriteria(EthnicityType.class);
            criteria.add(Expression.eq("code", ethnicityCode));
            type = (EthnicityType) criteria.uniqueResult();
        }
        return type;
    }

    /**
     * Gets the marital status type based on the code.
     * @param statusCode
     * @return
     * @throws UnknownReferenceTypeException
     */
    public MaritalStatusType getMaritalStatusType(String statusCode) throws UnknownReferenceTypeException
    {
        final MaritalStatusType type = MaritalStatusType.Cache.getEntity(statusCode);
        if (type == null)
            throw new UnknownReferenceTypeException();
        return type;
    }

    public PersonRoleType getPersonRoleType(String roleCode)
    {
        PersonRoleType  type = PersonRoleType.Cache.getEntity(roleCode);
        if (type == null)
        {
            final Criteria criteria = getSession().createCriteria(PersonRoleType.class);
            criteria.add(Expression.eq("code", roleCode));
            type = (PersonRoleType) criteria.uniqueResult();
        }
        return type;
    }

    public OrganizationRoleType getOrganizationRoleType(String roleCode)
    {
        OrganizationRoleType  type = OrganizationRoleType.Cache.getEntity(roleCode);
        if (type == null)
        {
            final Criteria criteria = getSession().createCriteria(OrganizationRoleType.class);
            criteria.add(Expression.eq("code", roleCode));
            type = (OrganizationRoleType) criteria.uniqueResult();
        }
        return type;
    }

    public ContactMechanismPurposeType getContactMechanismPurposeType(String purposeCode) throws UnknownReferenceTypeException
    {
        ContactMechanismPurposeType  type = ContactMechanismPurposeType.Cache.getEntity(purposeCode);
        if (type == null)
        {
            final Criteria criteria = getSession().createCriteria(ContactMechanismPurposeType.class);
            criteria.add(Expression.eq("code", purposeCode));
            type = (ContactMechanismPurposeType) criteria.uniqueResult();
            if (type == null)
                throw new UnknownReferenceTypeException();
        }
        return type;
    }

    public HealthCareLicenseType getLicenseType(final String licenseType)
    {
        HealthCareLicenseType type = HealthCareLicenseType.Cache.getEntity(licenseType);
        if (type == null)
        {
            final Criteria criteria = getSession().createCriteria(HealthCareLicenseType.class);
            criteria.add(Expression.eq("code", licenseType));
            type = (HealthCareLicenseType) criteria.uniqueResult();
        }
        return type;
    }

    public InsuranceProductType getInsuranceProductType(final String productTypeCode)
    {
        InsuranceProductType type = InsuranceProductType.Cache.getEntity(productTypeCode);
        if (type == null)
        {
            final Criteria criteria = getSession().createCriteria(InsurancePolicyType.class);
            criteria.add(Restrictions.eq("code", productTypeCode));
            type = (InsuranceProductType) criteria.uniqueResult();
        }
        return type;
    }

    public BillRemittanceType getBillRemittanceType(final String remittanceTypeCode)
    {
        BillRemittanceType type = BillRemittanceType.Cache.getEntity(remittanceTypeCode);
        if (type == null)
        {
            final Criteria criteria = getSession().createCriteria(BillRemittanceType.class);
            criteria.add(Restrictions.eq("code", remittanceTypeCode));
            type = (BillRemittanceType) criteria.uniqueResult();
        }
        return type;
    }

    public FeeScheduleItemCostType getFeeScheduleItemCostType(final String costTypeCode)
    {
        FeeScheduleItemCostType type = FeeScheduleItemCostType.Cache.getEntity(costTypeCode);
        if (type == null)
        {
            final Criteria criteria = getSession().createCriteria(FeeScheduleItemCostType.class);
            criteria.add(Restrictions.eq("code", costTypeCode));
            type = (FeeScheduleItemCostType) criteria.uniqueResult();
        }
        return type;
    }

    public PatientType getPatientType(final String patientTypeCode)
    {
        PatientType type = PatientType.Cache.getEntity(patientTypeCode);
        if (type == null)
        {
            final Criteria criteria = getSession().createCriteria(PatientType.class);
            criteria.add(Restrictions.eq("code", patientTypeCode));
            type = (PatientType) criteria.uniqueResult();
        }
        return type;
    }

    public ClaimType getClaimType(final String claimTypeCode)
    {
        return ClaimType.Cache.getEntity(claimTypeCode);
    }
}
