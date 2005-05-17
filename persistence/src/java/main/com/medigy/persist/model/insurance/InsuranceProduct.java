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

import com.medigy.persist.model.product.Product;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.HashSet;
import java.util.Set;

/**
 * Insurance Products, which are a broad classification of insurance plans offered by an insurance organization.
 */
@Entity
@Inheritance(discriminatorValue = "Insurance")
public class InsuranceProduct extends Product
{
    public static final String PK_COLUMN_NAME = Product.PK_COLUMN_NAME;

    private Set<InsuranceProductCoverage> insuranceProductCoverages = new HashSet<InsuranceProductCoverage>();
    private Set<InsurancePlan> insurancePlans = new HashSet<InsurancePlan>();

    private Set<InsuranceProductCoverageLevel> coverageLevelRelationships = new HashSet<InsuranceProductCoverageLevel>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "insuranceProduct")
    public Set<InsuranceProductCoverageLevel> getCoverageLevelRelationships()
    {
        return coverageLevelRelationships;
    }

    public void setCoverageLevelRelationships(final Set<InsuranceProductCoverageLevel> coverageLevelRelationships)
    {
        this.coverageLevelRelationships = coverageLevelRelationships;
    }

    @Transient
    public void addCoverageLevelRelationships(final InsuranceProductCoverageLevel rel)
    {
        this.coverageLevelRelationships.add(rel);
    }

    /**
     * Gets all the insurance plans for this insurance product. The different Insurance Plans that are part of the same
     * Insurance Product can have differing specifications but will all conform to the broad guidelines laid down by
     * the Insurance Product.
     * @return   a set of insurance plans
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "insuranceProduct")
    public Set<InsurancePlan> getInsurancePlans()
    {
        return insurancePlans;
    }

    public void setInsurancePlans(final Set<InsurancePlan> insurancePlans)
    {
        this.insurancePlans = insurancePlans;
    }

    @Transient
    public void addInsurancePlan(final InsurancePlan policy)
    {
        getInsurancePlans().add(policy);
    }

    @Transient
    public InsurancePlan getInsurancePlan(final String planName)
    {
        for (InsurancePlan plan: insurancePlans)
        {
            if (plan.getName().equals(planName))
            return plan;
        }
        return null;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "insuranceProduct")
    public Set<InsuranceProductCoverage> getInsuranceProductCoverages()
    {
        return  insuranceProductCoverages;
    }

    public void setInsuranceProductCoverages(final Set<InsuranceProductCoverage> insuranceProductCoverages)
    {
        this.insuranceProductCoverages = insuranceProductCoverages;
    }

    @Transient
    public void addCoverageRelationship(final InsuranceProductCoverage rel)
    {
        this.insuranceProductCoverages.add(rel);
    }
}
