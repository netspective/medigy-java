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
import com.medigy.persist.model.insurance.InsurancePolicy;
import com.medigy.persist.model.insurance.InsuranceProduct;
import com.medigy.persist.model.org.Organization;
import com.medigy.persist.model.person.Person;
import com.medigy.persist.reference.custom.insurance.InsurancePolicyType;
import com.medigy.persist.reference.custom.insurance.InsuranceProductType;
import com.medigy.persist.reference.type.GenderType;
import com.medigy.persist.util.HibernateUtil;
import com.medigy.service.TestCase;
import com.medigy.service.impl.insurance.InsurancePolicyFacadeImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TestInsurancePolicyFacade extends TestCase
{
    private static final Log log = LogFactory.getLog(TestInsurancePolicyFacade.class);


    public void testListInsuranceProducts()
    {
        HibernateUtil.beginTransaction();
        Organization blueCross = new Organization();
        blueCross.setOrganizationName("Blue Cross");
        InsuranceProduct insProduct = new InsuranceProduct();
        insProduct.setOrganization(blueCross);
        insProduct.setType(InsuranceProductType.Cache.PPO.getEntity());
        blueCross.addInsuranceProduct(insProduct);
        getSession().save(blueCross);
        HibernateUtil.commitTransaction();

        final InsurancePolicyFacade facade = new InsurancePolicyFacadeImpl();
        List<InsuranceProduct> productList = facade.listInsuranceProducts(blueCross);
        assertEquals(1, productList.size());
        final InsuranceProduct newProduct = productList.get(0);
        assertThat(newProduct.getType().getInsuranceProductTypeId(), eq(InsuranceProductType.Cache.PPO.getEntity().getInsuranceProductTypeId()));

    }

    public void testCreateIndividualInsurancePolicy()
    {
        final Organization blueCross = new Organization();
        blueCross.setOrganizationName("Blue Cross");
        final InsuranceProduct insProduct = new InsuranceProduct();
        insProduct.setOrganization(blueCross);
        insProduct.setType(InsuranceProductType.Cache.PPO.getEntity());
        blueCross.addInsuranceProduct(insProduct);
        final InsurancePlan insPlan = new InsurancePlan();
        insPlan.setName("Super Plan");
        insPlan.setInsuranceProduct(insProduct);
        insPlan.setOrganization(blueCross);
        blueCross.addInsurancePlan(insPlan);
        insProduct.addInsurancePlan(insPlan);
        HibernateUtil.getSession().save(blueCross);

        Calendar calendar = Calendar.getInstance();
        calendar.set(1980,1,1);

        Person policyHolder = new Person();
        policyHolder.setLastName("A");
        policyHolder.setFirstName("Person");
        policyHolder.setBirthDate(calendar.getTime());
        policyHolder.addGender(GenderType.Cache.FEMALE.getEntity());

        Person dependent = new Person();
        dependent.setLastName("B");
        dependent.setFirstName("Person");
        dependent.setBirthDate(calendar.getTime());
        dependent.addGender(GenderType.Cache.FEMALE.getEntity());
        HibernateUtil.getSession().save(policyHolder);
        HibernateUtil.getSession().save(dependent);

        final Person[] insuredDependents = new Person[] { dependent };

        final InsurancePolicyFacade facade = new InsurancePolicyFacadeImpl();
        for (Person depdendent : insuredDependents)
        {
            final InsurancePolicy policy = facade.createInsurancePolicy(depdendent, policyHolder, insPlan, "12345", "XXX",
                InsurancePolicyType.Cache.INDIVIDUAL_INSURANCE_POLICY.getEntity(), new Date(), null);
            HibernateUtil.getSession().flush();
        }
        final List newPolicies =  HibernateUtil.getSession().createCriteria(InsurancePolicy.class).list();
        assertThat(newPolicies.size(), eq(insuredDependents.length));
        final InsurancePolicy[] policyList = (InsurancePolicy[]) newPolicies.toArray(new InsurancePolicy[0]);
        for (InsurancePolicy newPolicy : policyList)
        {
            assertThat(newPolicy.getPolicyNumber(), eq("12345"));
            assertThat(newPolicy.getGroupNumber(), eq("XXX"));
            assertThat(newPolicy.getContractHolderPerson().getPartyId(), eq(policyHolder.getPartyId()));
        }

    }

    /*
    public String getDataSetFile()
    {
        return "/com/medigy/service/insurance/TestInsurancePolicyFacade.xml";
    }
    */
}
