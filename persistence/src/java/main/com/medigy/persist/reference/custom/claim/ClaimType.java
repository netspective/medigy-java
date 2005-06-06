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
package com.medigy.persist.reference.custom.claim;

import com.medigy.persist.reference.custom.AbstractCustomReferenceEntity;
import com.medigy.persist.reference.custom.CachedCustomReferenceEntity;
import com.medigy.persist.reference.custom.CustomReferenceEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratorType;
import javax.persistence.Column;

@Entity
public class ClaimType extends AbstractCustomReferenceEntity
{
    public static final String PK_COLUMN_NAME = "claim_type_id";

    public enum Cache implements CachedCustomReferenceEntity
    {
        INSURANCE("INSURANCE", "Insurance"),
        WORKERS_COMPENSATION("WORKERS_COMP", "Workers Compensation"),
        THIRD_PARTY("THIRD_PARTY", "Third Party Payer"),
        
        /**
         * <data>
			<row>
				<col name="id">0</col>
				<col name="caption">Self-Pay</col>
			</row>
			<row>
				<col name="id">1</col>
				<col name="caption">Insurance</col>
				<col name="group_name">insurance</col>
			</row>
			<row>
				<col name="id">2</col>
				<col name="caption">HMO(cap)</col>
				<col name="group_name">insurance</col>
			</row>
			<row>
				<col name="id">3</col>
				<col name="caption">PPO</col>
				<col name="group_name">insurance</col>
			</row>
			<row>
				<col name="id">4</col>
				<col name="caption">Medicare</col>
				<col name="group_name">insurance</col>
			</row>
			<row>
				<col name="id">5</col>
				<col name="caption">Medicaid</col>
				<col name="group_name">insurance</col>
			</row>
			<row>
				<col name="id">6</col>
				<col name="caption">Workers Compensation</col>
				<col name="group_name">workers compensation</col>
			</row>
			<row>
				<col name="id">7</col>
				<col name="caption">Third-Party Payer</col>
				<col name="group_name">third-party</col>
			</row>
			<row>
				<col name="id">8</col>
				<col name="caption">Champus</col>
				<col name="group_name">insurance</col>
			</row>
			<row>
				<col name="id">9</col>
				<col name="caption">ChampVA</col>
				<col name="group_name">insurance</col>
			</row>
			<row>
				<col name="id">10</col>
				<col name="caption">FECA Blk Lung</col>
				<col name="group_name">insurance</col>
			</row>
			<row>
				<col name="id">11</col>
				<col name="caption">BCBS</col>
				<col name="group_name">insurance</col>
			</row>
			<row>
				<col name="id">12</col>
				<col name="caption">HMO(non-cap)</col>
				<col name="group_name">insurance</col>
			</row>
			<row>
				<col name="id">13</col>
				<col name="caption">MC(managed care choice)</col>
				<col name="group_name">insurance</col>
			</row>
			<row>
				<col name="id">14</col>
				<col name="caption">POS(point of service)</col>
				<col name="group_name">insurance</col>
			</row>
			<row>
				<col name="id">15</col>
				<col name="caption">Railroad Medicare</col>
				<col name="group_name">insurance</col>
			</row>
		</data>
         */
        INSTITUTIONAL("INST", "UB-92 Forms"),
        MEDICAL("MED", "HCFA 1500 Forms"),
        DENTAL("DENT", "ADA-94"),
        HOME_CARE("HOME", "Home Care");

        private final String label;
        private final String code;
        private ClaimType entity;

        private Cache(final String code, final String label)
        {
            this.code = code;
            this.label = label;
        }

        public String getCode()
        {
            return code;
        }


        public ClaimType getEntity()
        {
            return entity;
        }

        public void setEntity(final CustomReferenceEntity entity)
        {
            this.entity = (ClaimType) entity;
        }

        public String getLabel()
        {
            return label;
        }
    }

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)        
    public Long getClaimTypeId()
    {
        return super.getSystemId();
    }

    protected void setClaimTypeId(final Long id)
    {
        super.setSystemId(id);
    }
}
