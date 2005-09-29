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
package com.medigy.persist.reference.custom.invoice;

import com.medigy.persist.reference.custom.AbstractCustomReferenceEntity;
import com.medigy.persist.reference.custom.CachedCustomReferenceEntity;
import com.medigy.persist.reference.custom.CustomReferenceEntity;

import javax.persistence.Column;
import javax.persistence.GeneratorType;
import javax.persistence.Id;

public class InvoiceAttributeType extends AbstractCustomReferenceEntity
{
    public static final String PK_COLUMN_NAME = "inv_attr_type_id";


    public enum Cache implements CachedCustomReferenceEntity
    {
        ACTIVE_FEE_SCHEDULE("ACT_FEE_SCH", "Active Fee Schedule"),
        DEFAULT_FEE_SCHEUDLE("DEF_FEE_SCH", "Default Fee Schedule"),
        CLAIM_FILING("CLAIM_FILE", "Claim Filing/Indicator"),
        BILLING_CONTACT("BILL_CONTACT", "Billing Contact"),
        BILLING_PHONE_NUMBER("BILL_PHONE", "Billing Phone"),
        SUBMISSION_ORDER("SUB_ORDER", "Submission Order"),
        BATCH_DATE("BATCH_DATE", "Batch Date"),
        BATCH_ID("BATCH_ID", "Invoice/Creation/Batch ID"),
        PRIOR_AUTH_NUMBER("PRIOR_AUTH_NUM", "Prior Authorization Number"),
        REFERRAL_PROVIDER_ID("REF_PROV_ID", "Ref Provider/Identification"),
        REFERRAL_PROVIDER_LAST_NAME("REF_PROV_LNAME", "Ref Provider/Name/Last"),
        REFERRAL_PROVIDER_FIRST_NAME("REF_PROV_FNAME", "Ref Provider/Name/First"),
        REFERRAL_PROVIDER_MIDDLE_NAME("REF_PROV_MNAME", "Ref Provider/Name/Middle"),
        INCIDENT_RELATED_TO("INC_REL_TO", "Condition/Related To"),     // INCIDENT Table
        INCIDENT_STATE("INC_STATE", "Incident State"),
        INCIDENT_EMPLOYMENT_RELATED("INC_EMP", "Incident Employment Related"),
        DEDUCTIBLE_BALANCE("DEDUCT", "Patient/Deductible/Balance"),
        ILLNESS_START_DATE("ILL_START", "Patient/Illness/Dates"),
        ILLNESS_END_DATE("ILL_END", "Patient/Illness/Dates"),
        DISABILITY_START_DATE("DIS_START", "Patient/Disability/Dates"),
        DISABILITY_END_DATE("DIS_END", "Patient/Disability/Dates"),
        HOSP_START_DATE("HOSP_START", "Patient/Hospitalization/Dates"),
        HOSP_END_DATE("HOSP_END", "Patient/Hospitalization/Dates");

        private final String label;
        private final String code;
        private InvoiceAttributeType entity;

        Cache(final String code, final String label)
        {
            this.code = code;
            this.label = label;
        }

        public String getCode()
        {
            return code;
        }

        public InvoiceAttributeType getEntity()
        {
            return entity;
        }

        public void setEntity(final CustomReferenceEntity entity)
        {
            this.entity = (InvoiceAttributeType) entity;
        }

        public String getLabel()
        {
            return label;
        }
    }

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getInvoiceAttributeTypeId()
    {
        return super.getSystemId();
    }

    protected void setInvoiceAttributeTypeId(final Long id)
    {
        super.setSystemId(id);
    }
}
