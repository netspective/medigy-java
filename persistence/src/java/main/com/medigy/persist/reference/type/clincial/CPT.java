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
package com.medigy.persist.reference.type.clincial;

import com.medigy.persist.reference.AbstractReferenceEntity;
import com.medigy.persist.reference.type.GenderType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * CPT Codes describe medical or psychiatric procedures performed by physicians and other health providers.
 * The codes were developed by the Health Care Financing Administration (HCFA) to assist in the assignment of
 * reimbursement amounts to providers by Medicare carriers. A growing number of managed care and other insurance
 * companies, however, base their reimbursements on the values established by HCFA.
 */
@Entity
public class CPT extends AbstractReferenceEntity
{
    private String comprehensiveCompoundCpts;
    private String comprehensiveCompoundFlags;
    private String mutualExclusiveCpts;
    private String mutualExclusiveFlags;
    private GenderType gender;
    private Boolean unlisted;
    private Boolean questionable;
    private Boolean nonRep;
    private Boolean nonCov;
    private Boolean ambulatorySurgeryCtr;

    @Column(length = 4000, name = "comprehensive_cmpd_cpts")
    public String getComprehensiveCompoundCpts()
    {
        return comprehensiveCompoundCpts;
    }

    public void setComprehensiveCompoundCpts(final String comprehensiveCompoundCpts)
    {
        this.comprehensiveCompoundCpts = comprehensiveCompoundCpts;
    }

    @Column(length = 4000, name = "comprehensive_cmpd_flags")
    public String getComprehensiveCompoundFlags()
    {
        return comprehensiveCompoundFlags;
    }

    public void setComprehensiveCompoundFlags(final String comprehensiveCompoundFlags)
    {
        this.comprehensiveCompoundFlags = comprehensiveCompoundFlags;
    }

    public String getMutualExclusiveCpts()
    {
        return mutualExclusiveCpts;
    }

    public void setMutualExclusiveCpts(final String mutualExclusiveCpts)
    {
        this.mutualExclusiveCpts = mutualExclusiveCpts;
    }

    @Column(length = 4000)
    public String getMutualExclusiveFlags()
    {
        return mutualExclusiveFlags;
    }

    public void setMutualExclusiveFlags(final String mutualExclusiveFlags)
    {
        this.mutualExclusiveFlags = mutualExclusiveFlags;
    }

    @ManyToOne
    @JoinColumn(name = "gender_type_id", referencedColumnName = GenderType.PK_COLUMN_NAME)
    public GenderType getGender()
    {
        return gender;
    }

    public void setGender(final GenderType gender)
    {
        this.gender = gender;
    }

    public Boolean getUnlisted()
    {
        return unlisted;
    }

    public void setUnlisted(final Boolean unlisted)
    {
        this.unlisted = unlisted;
    }

    public Boolean getQuestionable()
    {
        return questionable;
    }

    public void setQuestionable(final Boolean questionable)
    {
        this.questionable = questionable;
    }

    public Boolean getNonRep()
    {
        return nonRep;
    }

    public void setNonRep(final Boolean nonRep)
    {
        this.nonRep = nonRep;
    }

    public Boolean getNonCov()
    {
        return nonCov;
    }

    public void setNonCov(final Boolean nonCov)
    {
        this.nonCov = nonCov;
    }

    /**
     * Gets the CPT's status of being payable in  Ambulatory Surgery Centers (ASC)
     *
     * @return True if the CPT code is a part of the ASC list of covered procedures
     */
    public Boolean getAmbulatorySurgeryCtr()
    {
        return ambulatorySurgeryCtr;
    }

    public void setAmbulatorySurgeryCtr(final Boolean ambulatorySurgeryCtr)
    {
        this.ambulatorySurgeryCtr = ambulatorySurgeryCtr;
    }
}
