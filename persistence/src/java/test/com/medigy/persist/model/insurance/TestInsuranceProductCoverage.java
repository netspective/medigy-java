/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.model.insurance;

import com.medigy.persist.TestCase;
import com.medigy.persist.util.HibernateUtil;
import com.medigy.persist.reference.custom.org.OrganizationClassificationType;
import com.medigy.persist.reference.custom.insurance.CoverageType;
import com.medigy.persist.reference.custom.insurance.CoverageLevelType;
import com.medigy.persist.reference.custom.insurance.CoverageLevelBasisType;
import com.medigy.persist.model.org.Organization;

public class TestInsuranceProductCoverage extends TestCase
{
    public void testInsuranceProductCoverage()
    {
        // create the insurance company
        final Organization blueCross = new Organization();
        blueCross.setOrganizationName("Blue Cross Blue Shield");
        blueCross.addPartyClassification(OrganizationClassificationType.Cache.INSURANCE.getEntity());

        // create the product
        final InsuranceProduct ppoProduct = new InsuranceProduct();
        ppoProduct.setName("PPO Product");
        ppoProduct.setOrganization(blueCross);
        blueCross.addProduct(ppoProduct);

        HibernateUtil.getSession().save(blueCross);

        final Coverage coverage = new Coverage();
        coverage.setType(CoverageType.Cache.MAJOR_MEDICAL.getEntity());

        CoverageLevel copay = new CoverageLevel();
        copay.setType(CoverageLevelType.Cache.COPAY.getEntity());
        copay.setValue(new Float(10));
        copay.setCoverage(coverage);
        copay.addCoverageLevelBasis(CoverageLevelBasisType.Cache.PER_INCIDENT.getEntity());

        CoverageLevel coins = new CoverageLevel();
        coins.setType(CoverageLevelType.Cache.CONINSURANCE.getEntity());
        coins.setValue(new Float(80));
        coins.setCoverage(coverage);
        coins.addCoverageLevelBasis(CoverageLevelBasisType.Cache.PER_INCIDENT.getEntity());
        coins.addCoverageLevelBasis(CoverageLevelBasisType.Cache.PER_PERSON.getEntity());

        coverage.addCoverageLevel(copay);
        coverage.addCoverageLevel(coins);

        HibernateUtil.getSession().save(coverage);

        final InsuranceProductCoverage ipc = new InsuranceProductCoverage();
        ipc.setCoverage(coverage);
        ipc.setInsuranceProduct(ppoProduct);
        ppoProduct.addCoverageRelationship(ipc);
        coverage.addInsuranceProductRelationship(ipc);

        HibernateUtil.getSession().save(ipc);
        HibernateUtil.getSession().flush();
        HibernateUtil.closeSession();

        final InsuranceProduct product = (InsuranceProduct) HibernateUtil.getSession().load(InsuranceProduct.class, ppoProduct.getProductId());
        assertThat(product.getInsuranceProductCoverages().size(), eq(1));
        final InsuranceProductCoverage productCoverage = (InsuranceProductCoverage) product.getInsuranceProductCoverages().toArray()[0];
        assertThat(productCoverage.getInsuranceProductCoverageId(), eq(ipc.getInsuranceProductCoverageId()));
        assertThat(productCoverage.getCoverage().getCoverageId(), eq(coverage.getCoverageId()));

    }

}
