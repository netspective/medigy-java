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

import com.medigy.persist.model.common.AbstractDateDurationEntity;
import com.medigy.persist.model.party.ContactMechanism;
import com.medigy.persist.reference.custom.party.ContactMechanismPurposeType;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Ins_Product_Contact_Mech")
public class InsuranceProductContactMechanism extends AbstractDateDurationEntity
{
    public static final String PK_COLUMN_NAME = "ins_product_contact_mech_id";

    private Long insuranceProductContactMechanismId;
    private String notes;
    private InsuranceProduct insuranceProduct;
    private ContactMechanism contactMechanism;

    private Set<InsuranceProductContactMechanismPurpose> purposes = new HashSet<InsuranceProductContactMechanismPurpose>();

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getInsuranceProductContactMechanismId()
    {
        return insuranceProductContactMechanismId;
    }

    public void setInsuranceProductContactMechanismId(final Long insuranceProductContactMechanismId)
    {
        this.insuranceProductContactMechanismId = insuranceProductContactMechanismId;
    }

    @Column(length = 256)
    public String getNotes()
    {
        return notes;
    }

    public void setNotes(final String notes)
    {
        this.notes = notes;
    }

    @ManyToOne
    @JoinColumn(name = InsuranceProduct.PK_COLUMN_NAME)
    public InsuranceProduct getInsuranceProduct()
    {
        return insuranceProduct;
    }

    public void setInsuranceProduct(final InsuranceProduct insuranceProduct)
    {
        this.insuranceProduct = insuranceProduct;
    }

    @ManyToOne
    @JoinColumn(name = ContactMechanism.PK_COLUMN_NAME)
    public ContactMechanism getContactMechanism()
    {
        return contactMechanism;
    }

    public void setContactMechanism(final ContactMechanism contactMechanism)
    {
        this.contactMechanism = contactMechanism;
    }

    @OneToMany(mappedBy = "insuranceProductContactMechanism", cascade = CascadeType.ALL)
    public Set<InsuranceProductContactMechanismPurpose> getPurposes()
    {
        return purposes;
    }

    public void setPurposes(final Set<InsuranceProductContactMechanismPurpose> purposes)
    {
        this.purposes = purposes;
    }

    @Transient
    public void addPurpose(final InsuranceProductContactMechanismPurpose purpose)
    {
        purpose.setInsuranceProductContactMechanism(this);
        purposes.add(purpose);
    }

    @Transient
    public void addPurpose(final ContactMechanismPurposeType purposeType)
    {
        final InsuranceProductContactMechanismPurpose productCmp = new InsuranceProductContactMechanismPurpose();
        productCmp.setType(purposeType);
        productCmp.setInsuranceProductContactMechanism(this);
        purposes.add(productCmp);
    }

}
