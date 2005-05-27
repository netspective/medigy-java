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
 * @author Shahid N. Shah
 */

/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.model.org;

import com.medigy.persist.model.insurance.Enrollment;
import com.medigy.persist.model.insurance.InsurancePlan;
import com.medigy.persist.model.insurance.InsuranceProduct;
import com.medigy.persist.model.party.Party;
import com.medigy.persist.reference.custom.insurance.InsuranceProductType;
import com.medigy.persist.reference.custom.org.OrganizationClassificationType;
import com.medigy.persist.reference.type.party.PartyType;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.HashSet;
import java.util.Set;

/**
 * Specialized party for organizations.
 */
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@Table(name = "Org")
public class Organization extends Party
{
    private Set<InsuranceProduct> insuranceProducts = new HashSet<InsuranceProduct>();
    private Set<InsurancePlan> insurancePlan = new HashSet<InsurancePlan>();
    private Set<Enrollment> enrollments = new HashSet<Enrollment>();


    public Organization()
    {
        super();
        setPartyType(PartyType.Cache.ORGANIZATION.getEntity());
    }

    /**
     * Gets the organization name. Same as {@link #getPartyName()}
     *
     * @return org name
     */
    @Transient
    public String getOrganizationName()
    {
        return getPartyName();
    }

    public void setOrganizationName(final String organizationName)
    {
        super.setPartyName(organizationName);
    }

    /**
     * Gets the organizaton unique ID. Same as  {@link #getPartyId()}.
     * @return  org ID
     */
    @Transient
    public Long getOrgId()
    {
        return super.getPartyId();
    }

    public void setOrgId(final Long orgId)
    {
        super.setPartyId(orgId);
    }

    /**
     * Checks to see if this organization is classified as an Insurance company
     * @return  True if this organization is classified as an insurance carrier
     */
    @Transient
    public boolean isInsuranceCarrier()
    {
        return getPartyClassification(OrganizationClassificationType.Cache.INSURANCE.getEntity()) != null ? true : false;
    }

    /**
     * Gets all the insurance products advertised by this organization. Th
     *
     * @return a set of insurance products
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "organization")
    public Set<InsuranceProduct> getInsuranceProducts()
    {
        return insuranceProducts;
    }

    public void setInsuranceProducts(final Set<InsuranceProduct> insuranceProducts)
    {
        this.insuranceProducts = insuranceProducts;
    }

    @Transient
    public InsuranceProduct getInsuranceProduct(final InsuranceProductType type)
    {
        for (InsuranceProduct product : insuranceProducts)
        {
            if (product.getType().equals(type))
                return product;
        }
        return null;
    }

    @Override
    public String toString()
    {
        return "Org{" +
                "indentifier=" + getOrgId() +
                ",organizationName='" + getPartyName() + "'" +
                "}";
    }

    @Transient
    public void addInsuranceProduct(final InsuranceProduct product)
    {
        getInsuranceProducts().add(product);
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "organization")
    public Set<InsurancePlan> getInsurancePlan()
    {
        return insurancePlan;
    }

    public void setInsurancePlan(final Set<InsurancePlan> insurancePlan)
    {
        this.insurancePlan = insurancePlan;
    }

    @Transient
    public void addInsurancePlan(final InsurancePlan plan)
    {
        insurancePlan.add(plan);
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "organization")
    public Set<Enrollment> getEnrollments()
    {
        return enrollments;
    }

    public void setEnrollments(final Set<Enrollment> enrollments)
    {
        this.enrollments = enrollments;
    }


}
