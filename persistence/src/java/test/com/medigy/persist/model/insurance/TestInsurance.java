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
import com.medigy.persist.reference.custom.insurance.InsurancePolicyRoleType;
import com.medigy.persist.reference.custom.insurance.InsurancePolicyType;
import com.medigy.persist.reference.custom.org.OrganizationClassificationType;
import com.medigy.persist.util.HibernateUtil;

import java.util.Date;

public class TestInsurance extends TestCase
{


    public void testInsurance()
    {
        // create the insurance company
        final Organization blueCross = new Organization();
        blueCross.setOrganizationName("Blue Cross Blue Shield");
        blueCross.addPartyClassification(OrganizationClassificationType.Cache.INSURANCE.getEntity());

        final InsuranceProduct product = new InsuranceProduct();
        product.setName("PPO Product");
        product.setOrganization(blueCross);

        final InsurancePlan plan =  new InsurancePlan();
        plan.setInsuranceProduct(product);
        plan.setName("Super Plan");
        product.addInsurancePlan(plan);

        blueCross.getProducts().add(product);
        HibernateUtil.getSession().save(blueCross);

        final Person johnDoe = new Person();
        johnDoe.setFirstName("John");
        johnDoe.setLastName("Doe");
        InsurancePolicyRole policyHolderRole = new InsurancePolicyRole();
        policyHolderRole.setPerson(johnDoe);
        policyHolderRole.setType(InsurancePolicyRoleType.Cache.INSURED_CONTRACT_HOLDER.getEntity());
        johnDoe.addInsurancePolicyRole(policyHolderRole);

        final Person patient = new Person();
        patient.setLastName("Doe");
        patient.setFirstName("Jane");
        InsurancePolicyRole dependentRole = new InsurancePolicyRole();
        dependentRole.setPerson(johnDoe);
        dependentRole.setType(InsurancePolicyRoleType.Cache.INSURED_DEPENDENT.getEntity());
        patient.addInsurancePolicyRole(dependentRole);

        HibernateUtil.getSession().save(patient);
        HibernateUtil.getSession().save(johnDoe);
        HibernateUtil.getSession().flush();

        InsurancePolicy johnDoePolicy = new InsurancePolicy();
        johnDoePolicy.setInsurancePlan(plan);
        johnDoePolicy.setInsurancePolicyRole(policyHolderRole);
        johnDoePolicy.setPolicyNumber("12345");
        johnDoePolicy.setGroupNumber("XXX");
        johnDoePolicy.setFromDate(new Date());
        johnDoePolicy.setType(InsurancePolicyType.Cache.INDIVIDUAL_INSURANCE_POLICY.getEntity());
        policyHolderRole.addInsurancePolicy(johnDoePolicy);

        InsurancePolicy patientPolicy = new InsurancePolicy();
        patientPolicy.setInsurancePlan(plan);
        patientPolicy.setInsurancePolicyRole(policyHolderRole);
        patientPolicy.setPolicyNumber("12345-1");
        patientPolicy.setGroupNumber("XXX");
        patientPolicy.setFromDate(new Date());
        patientPolicy.setParentPolicy(johnDoePolicy);
        patientPolicy.setType(InsurancePolicyType.Cache.INDIVIDUAL_INSURANCE_POLICY.getEntity());
        dependentRole.addInsurancePolicy(patientPolicy);
        johnDoePolicy.getChildPolicies().add(patientPolicy);

        HibernateUtil.getSession().save(johnDoePolicy);
        HibernateUtil.getSession().save(patientPolicy);
        HibernateUtil.closeSession();

        final Person mainPerson = (Person) HibernateUtil.getSession().load(Person.class, johnDoe.getPartyId());
        assertEquals(johnDoe.getPartyId(), mainPerson.getPartyId());
        assertEquals(1, johnDoe.getInsurancePolicyRoles().size());
        final InsurancePolicyRole insurancePolicyRole = johnDoe.getInsurancePolicyRole(InsurancePolicyRoleType.Cache.INSURED_CONTRACT_HOLDER.getEntity());
        assertEquals(policyHolderRole, insurancePolicyRole);
        assertEquals(1, insurancePolicyRole.getInsurancePolicies().size());
        final InsurancePolicy policy = (InsurancePolicy) insurancePolicyRole.getInsurancePolicies().toArray()[0];
        assertEquals(johnDoePolicy, policy);

    }

    
}
