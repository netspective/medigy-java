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
package com.medigy.persist.model.insurance;

import com.medigy.persist.TestCase;
import com.medigy.persist.model.org.Organization;
import com.medigy.persist.model.person.Person;
import com.medigy.persist.reference.custom.insurance.CoverageLevelBasisType;
import com.medigy.persist.reference.custom.insurance.CoverageLevelType;
import com.medigy.persist.reference.custom.insurance.CoverageType;
import com.medigy.persist.reference.custom.insurance.InsurancePolicyType;
import com.medigy.persist.reference.custom.insurance.InsuranceProductType;
import com.medigy.persist.reference.custom.org.OrganizationClassificationType;
import com.medigy.persist.reference.type.GenderType;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

import java.util.Calendar;

public class TestInsurancePlanCoverage extends TestCase
{
    public void testInsurancePlanCoverage()
    {
        Session session = openSession();
        Transaction transaction = session.beginTransaction();
        // create the insurance company
        final Organization blueCross = new Organization();
        blueCross.setOrganizationName("Blue Cross Blue Shield");
        blueCross.addPartyClassification(OrganizationClassificationType.Cache.INSURANCE.getEntity());

        final InsuranceProduct insProduct = new InsuranceProduct();
        insProduct.setType(InsuranceProductType.Cache.PPO.getEntity());
        insProduct.setOrganization(blueCross);
        blueCross.addInsuranceProduct(insProduct);

        final InsurancePlan insPlan = new InsurancePlan();
        insPlan.setName("Test Plan");
        insPlan.setOrganization(blueCross);
        insPlan.setMedigapId("123");
        insPlan.setInsuranceProduct(insProduct);
        insProduct.addInsurancePlan(insPlan);

        session.save(blueCross);

        final Coverage coverage = new Coverage();
        final CoverageLevel indDeductibleLevel = new CoverageLevel();
        indDeductibleLevel.setType(CoverageLevelType.Cache.INDIVIDUAL_DEDUCTIBLE.getEntity());
        indDeductibleLevel.setValue(new Float(500));
        indDeductibleLevel.addCoverageLevelBasis(CoverageLevelBasisType.Cache.PER_YEAR.getEntity());
        coverage.addCoverageLevel(indDeductibleLevel);

        final CoverageLevel famDeductibleLevel = new CoverageLevel();
        famDeductibleLevel.setType(CoverageLevelType.Cache.FAMILY_DEDUCTIBLE.getEntity());
        famDeductibleLevel.setValue(new Float(5000));
        famDeductibleLevel.addCoverageLevelBasis(CoverageLevelBasisType.Cache.PER_YEAR.getEntity());
        coverage.addCoverageLevel(famDeductibleLevel);

        coverage.setType(CoverageType.Cache.MAJOR_MEDICAL.getEntity());
        session.save(coverage);

        InsurancePlanCoverageLevel planRel = new InsurancePlanCoverageLevel();
        planRel.setCoverageLevel(indDeductibleLevel);
        planRel.setInsurancePlan(insPlan);
        insPlan.addCoverageLevelRelationship(planRel);
        session.save(planRel);

        InsurancePlanCoverageLevel planFamRel = new InsurancePlanCoverageLevel();
        planFamRel.setCoverageLevel(famDeductibleLevel);
        planFamRel.setInsurancePlan(insPlan);
        insPlan.addCoverageLevelRelationship(planFamRel);
        session.save(planFamRel);
        transaction.commit();
        session.close();

        session = openSession();
        final InsurancePlan newPlan = (InsurancePlan) session.load(InsurancePlan.class, insPlan.getInsurancePlanId());
        assertThat(newPlan.getName(), eq("Test Plan"));
        assertThat(newPlan.getCoverageLevelRelationships().size(), eq(2));
        assertThat(newPlan.getCoverageLevelRelationship(CoverageLevelType.Cache.INDIVIDUAL_DEDUCTIBLE.getEntity()).getInsurancePlanCoverageId(),
            eq(planRel.getInsurancePlanCoverageId()));
        assertThat(newPlan.getCoverageLevelRelationship(CoverageLevelType.Cache.FAMILY_DEDUCTIBLE.getEntity()).getInsurancePlanCoverageId(),
            eq(planFamRel.getInsurancePlanCoverageId()));


        transaction = session.beginTransaction();
        Calendar cal = Calendar.getInstance();
        cal.set(1970, 1, 1);
        final Person person = new Person();
        person.setLastName("Hackett");
        person.setFirstName("Ryan");
        person.setBirthDate(cal.getTime());
        person.addGender(GenderType.Cache.MALE.getEntity());
        session.save(person);

        final InsurancePolicy newPolicy = new InsurancePolicy();
        newPolicy.setPolicyNumber("123");
        newPolicy.setGroupNumber("XXX");
        newPolicy.setType(InsurancePolicyType.Cache.INDIVIDUAL_INSURANCE_POLICY.getEntity());
        newPlan.addInsurancePolicy(newPolicy);
        person.addInsurancePolicy(newPolicy);
        session.save(newPolicy);

        assertThat(newPolicy.getInsurancePlan().getInsurancePlanId(), eq(newPlan.getInsurancePlanId()));
        assertThat(newPolicy.getCoverageLevelRelationships().size(), eq(2));
        assertThat(newPolicy.hasCoverageLevelRelationship(CoverageLevelType.Cache.INDIVIDUAL_DEDUCTIBLE.getEntity()),
            eq(true));
        assertThat(newPolicy.hasCoverageLevelRelationship(CoverageLevelType.Cache.FAMILY_DEDUCTIBLE.getEntity()),
            eq(true));
        final CoverageLevel oldIndCoverageLevel = newPolicy.getCoverageLevelRelationship(CoverageLevelType.Cache.INDIVIDUAL_DEDUCTIBLE.getEntity()).getCoverageLevel();
        assertThat(oldIndCoverageLevel.getCoverageLevelId(), eq(indDeductibleLevel.getCoverageLevelId()));

        // now give the policy the new coverage lvl
        final CoverageLevel newIndDeductibleLevel = new CoverageLevel();
        newIndDeductibleLevel.setType(CoverageLevelType.Cache.INDIVIDUAL_DEDUCTIBLE.getEntity());
        newIndDeductibleLevel.setValue(new Float(300));
        newIndDeductibleLevel.addCoverageLevelBasis(CoverageLevelBasisType.Cache.PER_YEAR.getEntity());
        oldIndCoverageLevel.getCoverage().addCoverageLevel(newIndDeductibleLevel);

        newPolicy.addCoverageLevelRelationship(newIndDeductibleLevel);
        session.save(newIndDeductibleLevel);
        transaction.commit();
        session.close();

        assertThat(newPolicy.getCoverageLevelRelationships().size(), eq(2));
        assertThat(newPolicy.getCoverageLevelRelationship(CoverageLevelType.Cache.INDIVIDUAL_DEDUCTIBLE.getEntity()).getCoverageLevel().getCoverageLevelId(),
                eq(newIndDeductibleLevel.getCoverageLevelId()));
    }
}
