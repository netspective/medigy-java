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
package com.medigy.service.insurance;

import com.medigy.persist.model.insurance.InsurancePlan;
import com.medigy.persist.model.insurance.Coverage;
import com.medigy.persist.model.insurance.CoverageLevel;
import com.medigy.persist.model.insurance.InsurancePlanCoverageLevel;
import com.medigy.persist.model.org.Organization;
import com.medigy.persist.model.person.Person;
import com.medigy.persist.reference.custom.person.PersonRoleType;
import com.medigy.persist.reference.custom.insurance.CoverageLevelType;
import com.medigy.persist.reference.custom.insurance.CoverageLevelBasisType;
import com.medigy.persist.reference.custom.insurance.CoverageType;
import com.medigy.persist.reference.custom.insurance.InsurancePolicyType;
import com.medigy.persist.reference.type.GenderType;
import com.medigy.persist.util.HibernateUtil;
import com.medigy.service.ServiceVersion;
import com.medigy.service.TestCase;
import com.medigy.service.dto.insurance.InsuranceCoverageParameters;
import com.medigy.service.dto.insurance.NewInsuranceCoverageData;
import com.medigy.service.dto.insurance.AddInsuranceCoverageParameters;
import org.hibernate.validator.NotNull;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class TestAddInsuranceCoverageService extends TestCase
{
    protected void setUp() throws Exception
    {
        super.setUp();

    }

    public void testAddInsuranceCoverageService()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(1970, 1, 1);
        final Person patient = Person.createNewPatient();
        patient.setLastName("Hackett");
        patient.setFirstName("Ryan");
        patient.setBirthDate(cal.getTime());
        patient.addGender(GenderType.Cache.MALE.getEntity());

        final Person patientFather = new Person();
        patientFather.setLastName("Hackett");
        patientFather.setFirstName("John");
        patientFather.setBirthDate(cal.getTime());
        patientFather.addGender(GenderType.Cache.MALE.getEntity());

        final Organization anthem = new Organization();
        anthem.setOrganizationName("Anthem");

        final InsurancePlan ppoPlan = new InsurancePlan();
        ppoPlan.setName("PPO Plan");
        ppoPlan.setOrganization(anthem);
        anthem.addInsurancePlan(ppoPlan);

        HibernateUtil.getSession().save(patient);
        HibernateUtil.getSession().save(patientFather);
        HibernateUtil.getSession().save(anthem);

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
        HibernateUtil.getSession().save(coverage);

        InsurancePlanCoverageLevel planRel = new InsurancePlanCoverageLevel();
        planRel.setCoverageLevel(indDeductibleLevel);
        planRel.setInsurancePlan(ppoPlan);
        ppoPlan.addCoverageLevelRelationship(planRel);

        InsurancePlanCoverageLevel famDeductRel = new InsurancePlanCoverageLevel();
        famDeductRel.setCoverageLevel(famDeductibleLevel);
        famDeductRel.setInsurancePlan(ppoPlan);
        ppoPlan.addCoverageLevelRelationship(famDeductRel);

        HibernateUtil.getSession().save(planRel);
        HibernateUtil.getSession().save(famDeductRel);
        HibernateUtil.closeSession();

        final AddInsuranceCoverageService service = (AddInsuranceCoverageService) getRegistry().getService(AddInsuranceCoverageService.class);

        final AddInsuranceCoverageParameters params  = new AddInsuranceCoverageParameters() {
            public Serializable getPatientId()
            {
                return patient.getPartyId();
            }

            public InsuranceCoverageParameters getInsuranceCoverage()
            {
                return new InsuranceCoverageParameters() {
                    public Serializable getInsuranceCarrierId()
                    {
                        return anthem.getOrgId();
                    }

                    public Serializable getInsuranceProductId()
                    {
                        return null;
                    }

                    public Serializable getInsurancePlanId()
                    {
                        return ppoPlan.getInsurancePlanId();
                    }

                    public String getInsurancePolicyNumber()
                    {
                        return "999";
                    }

                    @NotNull
                    public String getInsuranceGroupNumber()
                    {
                        return "X123";
                    }

                    @NotNull
                    public Serializable getInsuranceContractHolderId()
                    {
                        return patientFather.getPartyId();
                    }

                    @NotNull
                    public String getInsuranceContractHolderRole()
                    {
                        return PersonRoleType.Cache.MOTHER.getEntity().getCode();
                    }

                    public Date getCoverageStartDate()
                    {
                        return new Date();
                    }

                    public Date getCoverageEndDate()
                    {
                        return null;
                    }

                    public Float getIndividualDeductibleAmount()
                    {
                        return new Float(100);
                    }

                    public Float getFamilyDeductibleAmount()
                    {
                        return new Float(500);
                    }

                    public Float getOfficeVisitCoPay()
                    {
                        return new Float(10);
                    }

                    public Float getPercentagePay()
                    {
                        return null;
                    }

                    public Float getMaxThresholdAmount()
                    {
                        return null;
                    }

                    public String getInsurancePolicyTypeCode()
                    {
                        return InsurancePolicyType.Cache.GROUP_INSURANCE_POLICY.getEntity().getCode();
                    }
                };
            }


            public ServiceVersion getServiceVersion()
            {
                return null;
            }
        };

        final NewInsuranceCoverageData newInsuranceCoverageData = service.add(params);
        assertThat(newInsuranceCoverageData.getErrorMessage(), NULL);



    }
}
