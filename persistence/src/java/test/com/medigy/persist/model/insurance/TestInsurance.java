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
import com.medigy.persist.reference.custom.org.OrganizationClassificationType;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Restrictions;

public class TestInsurance extends TestCase
{
    public void testInsurance()
    {
        Session session = openSession();
        Transaction transaction = session.beginTransaction();

        // create the insurance company
        final Organization blueCross = new Organization();
        blueCross.setOrganizationName("Blue Cross Blue Shield");
        blueCross.addPartyClassification(OrganizationClassificationType.Cache.INSURANCE.getEntity());
        session.save(blueCross);
        transaction.commit();
        session.close();

        session = openSession();
        final Organization blueCross2 = (Organization) session.createCriteria(Organization.class).add(Restrictions.eq("partyId", blueCross.getOrgId())).uniqueResult();
        assertThat(blueCross2.getOrganizationName(), eq(blueCross.getOrganizationName()));

        /*
        final InsuranceProduct ppoProduct = new InsuranceProduct();
        ppoProduct.setType(InsuranceProductType.Cache.MEDICAID.getEntity());
        ppoProduct.setOrganization(blueCross);
        session.save(ppoProduct);

        final InsurancePlan plan1 =  new InsurancePlan();
        plan1.setInsuranceProduct(ppoProduct);
        plan1.setName("Super Plan 1");
        plan1.setOrganization(blueCross);
        plan1.setInsuranceProduct(ppoProduct);
        session.save(plan1);

        final InsurancePlan plan2 =  new InsurancePlan();
        plan2.setInsuranceProduct(ppoProduct);
        plan2.setName("Super Plan 2");
        plan2.setOrganization(blueCross);
        plan2.setInsuranceProduct(ppoProduct);

        session.save(plan2);
        transaction.commit();
        final Long orgId = blueCross.getOrgId();
        session.close();

        session = openSession();
        assertThat(orgId, NOT_NULL);
        final Organization carrier = (Organization) session.createCriteria(Organization.class).add(Restrictions.eq("partyId", orgId)).uniqueResult();
        assertNotNull(carrier);
        final Set<InsuranceProduct> insuranceProducts = carrier.getInsuranceProducts();
        assertThat(insuranceProducts.size(), eq(1));
        final InsuranceProduct product = (InsuranceProduct) carrier.getInsuranceProducts().toArray()[0];
        assertThat(product.getInsurancePlans().size(), eq(2));
        assertThat(product.getInsurancePlan("Super Plan 1").getName(), eq("Super Plan 1"));
        assertThat(product.getInsurancePlan("Super Plan 2").getName(), eq("Super Plan 2"));

        transaction = session.beginTransaction();
        Calendar cal = Calendar.getInstance();
        cal.set(1965, 1, 1);
        final Person johnDoe = new Person();
        johnDoe.setFirstName("John");
        johnDoe.setLastName("Doe");
        johnDoe.setBirthDate(cal.getTime());
        johnDoe.addGender(GenderType.Cache.MALE.getEntity());
        johnDoe.addLanguage(LanguageType.Cache.ENGLISH.getEntity());

        final PersonRole parentRole = new PersonRole();
        parentRole.setType(PersonRoleType.Cache.PARENT.getEntity());
        parentRole.setPerson(johnDoe);
        johnDoe.addRole(parentRole);

        cal.set(1985, 1, 1);
        final Person patient = new Person();
        patient.setLastName("Doe");
        patient.setFirstName("Jane");
        patient.setBirthDate(cal.getTime());
        patient.addGender(GenderType.Cache.FEMALE.getEntity());
        patient.addLanguage(LanguageType.Cache.ENGLISH.getEntity());

        final PersonRole childRole = new PersonRole();
        childRole.setType(PersonRoleType.Cache.PARENT.getEntity());
        childRole.setPerson(patient);
        patient.addRole(childRole);

        session.save(patient);
        session.save(johnDoe);

        final PeopleRelationship relationship = new PeopleRelationship();
        relationship.setPrimaryPersonRole(childRole);
        relationship.setSecondaryPersonRole(parentRole);
        relationship.setFromDate(new Date());
        relationship.setType(PeopleRelationshipType.Cache.PARENT_CHILD.getEntity());
        session.save(relationship);

        InsurancePolicy johnDoePolicy = new InsurancePolicy();
        johnDoePolicy.setInsurancePlan(plan1);
        johnDoePolicy.setInsuredPerson(johnDoe);
        johnDoePolicy.setContractHolderPerson(johnDoe);
        johnDoePolicy.setPolicyNumber("12345");
        johnDoePolicy.setGroupNumber("XXX");
        johnDoePolicy.setFromDate(new Date());
        johnDoePolicy.setType(InsurancePolicyType.Cache.INDIVIDUAL_INSURANCE_POLICY.getEntity());
        johnDoe.addInsurancePolicy(johnDoePolicy);
        johnDoe.addResponsibleInsurancePolicy(johnDoePolicy);

        InsurancePolicy patientPolicy = new InsurancePolicy();
        patientPolicy.setInsurancePlan(plan1);
        patientPolicy.setInsuredPerson(patient);
        patientPolicy.setContractHolderPerson(johnDoe);
        patientPolicy.setPolicyNumber("12345-1");
        patientPolicy.setGroupNumber("XXX");
        patientPolicy.setFromDate(new Date());
        patientPolicy.setType(InsurancePolicyType.Cache.INDIVIDUAL_INSURANCE_POLICY.getEntity());
        patient.addInsurancePolicy(patientPolicy);
        johnDoe.addResponsibleInsurancePolicy(patientPolicy);

        session.save(johnDoePolicy);
        session.save(patientPolicy);
        transaction.commit();
        session.close();

        session = openSession();
        final Person mainPerson = (Person) session.load(Person.class, johnDoe.getPartyId());
        final Set<InsurancePolicy> mainPersonInsurancePolicies = mainPerson.getInsurancePolicies();
        assertThat(mainPersonInsurancePolicies.size(), eq(1));
        final Set<InsurancePolicy> mainPersonResponsibleInsurancePolicies = mainPerson.getResponsibleInsurancePolicies();
        assertThat(mainPersonResponsibleInsurancePolicies.size(), eq(2));

        final InsurancePolicy jdPolicy = (InsurancePolicy) mainPersonInsurancePolicies.toArray()[0];
        assertThat(jdPolicy.getInsuredPerson().getPersonId(), eq(johnDoe.getPersonId()));
        assertThat(jdPolicy.getContractHolderPerson().getPersonId(), eq(johnDoe.getPersonId()));
        assertThat(jdPolicy.getPolicyNumber(), eq("12345"));
        assertThat(jdPolicy.getGroupNumber(), eq("XXX"));
        assertThat(jdPolicy.getType(), eq(InsurancePolicyType.Cache.INDIVIDUAL_INSURANCE_POLICY.getEntity()));
        assertThat(jdPolicy.getInsurancePlan().getInsurancePlanId(), eq(plan1.getInsurancePlanId()));
        assertThat(jdPolicy.getInsurancePlan().getName(), eq("Super Plan 1"));
        assertThat(jdPolicy.getInsurancePlan().getInsuranceProduct().getInsuranceProductId(), eq(product.getInsuranceProductId()));
        assertThat(jdPolicy.getInsurancePlan().getInsuranceProduct().getType(), eq(InsuranceProductType.Cache.MEDICAID.getEntity()));

        final Person secondPerson = (Person) session.load(Person.class, patient.getPartyId());
        final Set<InsurancePolicy> insurancePolicies = secondPerson.getInsurancePolicies();
        assertThat(insurancePolicies.size(), eq(1));
        final Set<InsurancePolicy> responsibleInsurancePolicies = secondPerson.getResponsibleInsurancePolicies();
        assertThat(responsibleInsurancePolicies.size(), eq(0));

        final InsurancePolicy secondPolicy = (InsurancePolicy) insurancePolicies.toArray()[0];
        assertThat(secondPolicy.getInsuredPerson().getPersonId(), eq(patient.getPersonId()));
        assertThat(secondPolicy.getContractHolderPerson().getPersonId(), eq(johnDoe.getPersonId()));
        assertThat(secondPolicy.getPolicyNumber(), eq("12345-1"));
        assertThat(secondPolicy.getGroupNumber(), eq("XXX"));
        assertThat(secondPolicy.getType(), eq(InsurancePolicyType.Cache.INDIVIDUAL_INSURANCE_POLICY.getEntity()));
        assertThat(secondPolicy.getInsurancePlan().getInsurancePlanId(), eq(plan1.getInsurancePlanId()));
        assertThat(secondPolicy.getInsurancePlan().getName(), eq("Super Plan 1"));
        assertThat(secondPolicy.getInsurancePlan().getInsuranceProduct().getInsuranceProductId(), eq(product.getInsuranceProductId()));
        assertThat(secondPolicy.getInsurancePlan().getInsuranceProduct().getType(), eq(InsuranceProductType.Cache.MEDICAID.getEntity()));
        session.close();
        */
    }
}
