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
package com.medigy.service.impl.insurance;

import com.medigy.service.insurance.AddInsuranceCoverageService;
import com.medigy.service.ServiceVersion;
import com.medigy.service.dto.ServiceParameters;
import com.medigy.service.dto.ServiceReturnValues;
import com.medigy.service.dto.insurance.AddInsuranceCoverageParameters;
import com.medigy.service.dto.insurance.NewInsuranceCoverageData;
import com.medigy.persist.model.person.Person;
import com.medigy.persist.model.insurance.InsurancePlan;
import com.medigy.persist.model.insurance.InsurancePolicy;
import com.medigy.persist.model.insurance.InsurancePlanCoverageLevel;
import com.medigy.persist.model.insurance.CoverageLevel;
import com.medigy.persist.model.insurance.InsurancePolicyCoverageLevel;
import com.medigy.persist.util.HibernateUtil;
import com.medigy.persist.reference.custom.insurance.CoverageLevelType;

import java.io.Serializable;
import java.util.Set;

public class AddInsuranceCoverageServiceImpl implements AddInsuranceCoverageService
{
    public NewInsuranceCoverageData add(final AddInsuranceCoverageParameters params)
    {
        final Person person = (Person) HibernateUtil.getSession().load(Person.class, params.getPatientId());
        if (person == null)
            return (NewInsuranceCoverageData) createErrorResponse(params, "Unknown patient ID");

        final Person contractHolderPerson = (Person) HibernateUtil.getSession().load(Person.class, params.getInsuranceContractHolderId());
        if (contractHolderPerson == null)
            return (NewInsuranceCoverageData) createErrorResponse(params, "Unknown contract holder ID");

        final InsurancePlan insPlan = (InsurancePlan) HibernateUtil.getSession().load(InsurancePlan.class, params.getInsurancePlanId());
        if (insPlan == null)
            return (NewInsuranceCoverageData) createErrorResponse(params, "Unknown insurance plan ID");

        final InsurancePolicy insPolicy =  new InsurancePolicy();
        insPolicy.setGroupNumber(params.getInsuranceGroupNumber());
        insPolicy.setPolicyNumber(params.getInsurancePolicyNumber());
        insPolicy.setInsuredPerson(person);
        insPolicy.setContractHolderPerson(contractHolderPerson);
        insPolicy.setInsurancePlan(insPlan);
        insPlan.addInsurancePolicy(insPolicy);

        // handle the coverages
        // 1. first copy the plans coverages into the policy
        final Set<InsurancePlanCoverageLevel> coverageLevelRelationships = insPlan.getCoverageLevelRelationships();
        for (InsurancePlanCoverageLevel level : coverageLevelRelationships)
        {
            final CoverageLevel coverageLevel = level.getCoverageLevel();
            final InsurancePolicyCoverageLevel policyCoverageLevel = new InsurancePolicyCoverageLevel();
            policyCoverageLevel.setCoverageLevel(coverageLevel);
            insPolicy.addCoverageLevelRelationship(policyCoverageLevel);
        }

        // 2. now override(create) coverage levels for policy
        final Float indDeductible = params.getIndividualDeductibleAmount();
        InsurancePolicyCoverageLevel indDeductibleCoverageLevel = insPolicy.getCoverageLevelRelationship(CoverageLevelType.Cache.INDIVIDUAL_DEDUCTIBLE.getEntity());
        final CoverageLevel existingCoverageLevel = indDeductibleCoverageLevel.getCoverageLevel();
        if (!existingCoverageLevel.getValue().equals(indDeductible))
        {
            // if the patient supplied value equals the plan's then no need to do anything.
            CoverageLevel newLevel = new CoverageLevel();
            newLevel.setType(CoverageLevelType.Cache.INDIVIDUAL_DEDUCTIBLE.getEntity());

            // right now the basis is not being accepted at the service level so copy current basises into the new level
            newLevel.setCoverageLevelBasises(existingCoverageLevel.getCoverageLevelBasises());
            newLevel.setCoverage(existingCoverageLevel.getCoverage());
            existingCoverageLevel.getCoverage().addCoverageLevel(newLevel);
            indDeductibleCoverageLevel.setCoverageLevel(newLevel);
        }

        // TODO: Handle the family deductible
        final Float famDeductible = params.getFamilyDeductibleAmount();

        HibernateUtil.getSession().save(insPolicy);
        return new NewInsuranceCoverageData() {
            public AddInsuranceCoverageParameters getAddInsuranceCoverageParameters()
            {
                return params;
            }

            public Serializable getInsurancePolicyId()
            {
                return insPolicy.getInsurancePolicyId();
            }

            public String getErrorMessage()
            {
                return null;
            }
        };
    }

    public ServiceVersion[] getSupportedServiceVersions()
    {
        return new ServiceVersion[0];
    }

    public boolean isValid(ServiceParameters parameters)
    {
        return false;
    }

    public ServiceReturnValues createErrorResponse(final ServiceParameters params, final String errorMessage)
    {
        return new NewInsuranceCoverageData() {
            public AddInsuranceCoverageParameters getAddInsuranceCoverageParameters()
            {
                return (AddInsuranceCoverageParameters) params;
            }

            public Serializable getInsurancePolicyId()
            {
                return null;
            }

            public String getErrorMessage()
            {
                return errorMessage;
            }
        };
    }
}