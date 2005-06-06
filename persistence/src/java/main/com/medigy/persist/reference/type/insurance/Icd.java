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
package com.medigy.persist.reference.type.insurance;

import com.medigy.persist.reference.AbstractReferenceEntity;
import com.medigy.persist.reference.type.GenderType;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;

@Entity
@Table(name = "ICD")
public class Icd  extends AbstractReferenceEntity
{
    /**
     * <column name="non_specific_code" type="boolean" descr="'True' if code requires additional 4th or 5th digit for reporting"/>
        <column name="sex" type="char" descr="M=Male; F=Female; otherwise space"/>
        <column name="age" type="char" descr="A=adult 14 and up; M=Maternal, 12-55; N=newborn less than 1; P=pediatric 0-17; otherwise space"/>
        <column name="major_diag_category" type="text" size="2" descr="Number of the MDC for the Prospective Payment System (PPS) 01-24, 99"/>
        <column name="comorbidity_complication" type="boolean" descr="True= indentifies secondary diagnosis codes as a complication or comorbidity"/>
        <column name="medicare_secondary_payer" type="boolean" descr="Trauma releated codes that may trigger investigation for liability insurance as the primary payer"/>
        <column name="manifestation_code" type="boolean" descr="True= Manifestation code, cannot be primary diagnosis"/>
        <column name="questionable_admission" type="boolean" descr="True or False"/>
        <column name="unacceptable_primary_wo" type="boolean" descr="True or False"/>
        <column name="unacceptable_principal" type="boolean" descr="True or False"/>
        <column name="unacceptable_procedure" type="boolean" descr="True= identifies code as operating room procedure"/>
        <column name="non_specific_procedure" type="boolean" descr="True= non-specific operating room procedure.  More specific code should be used"/>
        <column name="non_covered_procedure" type="boolean" descr="True= Medicare does not cover"/>
        <column name="cpts_allowed" type="text" size="4000" descr="List of CPT codes allowed"/>
     */

    private Boolean nonSpecificCode;
    private GenderType gender;
    private IcdAge age;
    private String majorDiagCategory;
    private Boolean comorbidityComplication;
    private Boolean medicareSecondaryPayer;
    private Boolean manifstationCode;
    private Boolean questionalbleAdmission;
    private Boolean unacceptablePrimaryWo;
    private Boolean unacceptablePrinciple;
    private Boolean unacceptableProcedure;
    private Boolean nonSpecificProcedure;
    private Boolean nonCoveredProcedure;
    private String cptsAllowed;

    public Boolean getNonSpecificCode()
    {
        return nonSpecificCode;
    }

    public void setNonSpecificCode(final Boolean nonSpecificCode)
    {
        this.nonSpecificCode = nonSpecificCode;
    }

    public GenderType getGender()
    {
        return gender;
    }

    public void setGender(final GenderType gender)
    {
        this.gender = gender;
    }

    public IcdAge getAge()
    {
        return age;
    }

    public void setAge(final IcdAge age)
    {
        this.age = age;
    }

    //@org.hibernate.annotations.Column(comment = "This is some unknown column")
    public String getMajorDiagCategory()
    {
        return majorDiagCategory;
    }

    public void setMajorDiagCategory(final String majorDiagCategory)
    {
        this.majorDiagCategory = majorDiagCategory;
    }

    public Boolean getComorbidityComplication()
    {
        return comorbidityComplication;
    }

    public void setComorbidityComplication(final Boolean comorbidityComplication)
    {
        this.comorbidityComplication = comorbidityComplication;
    }

    public Boolean getMedicareSecondaryPayer()
    {
        return medicareSecondaryPayer;
    }

    public void setMedicareSecondaryPayer(final Boolean medicareSecondaryPayer)
    {
        this.medicareSecondaryPayer = medicareSecondaryPayer;
    }

    public Boolean getManifstationCode()
    {
        return manifstationCode;
    }

    public void setManifstationCode(final Boolean manifstationCode)
    {
        this.manifstationCode = manifstationCode;
    }

    public Boolean getQuestionalbleAdmission()
    {
        return questionalbleAdmission;
    }

    public void setQuestionalbleAdmission(final Boolean questionalbleAdmission)
    {
        this.questionalbleAdmission = questionalbleAdmission;
    }

    public Boolean getUnacceptablePrimaryWo()
    {
        return unacceptablePrimaryWo;
    }

    public void setUnacceptablePrimaryWo(final Boolean unacceptablePrimaryWo)
    {
        this.unacceptablePrimaryWo = unacceptablePrimaryWo;
    }

    public Boolean getUnacceptablePrinciple()
    {
        return unacceptablePrinciple;
    }

    public void setUnacceptablePrinciple(final Boolean unacceptablePrinciple)
    {
        this.unacceptablePrinciple = unacceptablePrinciple;
    }

    public Boolean getUnacceptableProcedure()
    {
        return unacceptableProcedure;
    }

    public void setUnacceptableProcedure(final Boolean unacceptableProcedure)
    {
        this.unacceptableProcedure = unacceptableProcedure;
    }

    public Boolean getNonSpecificProcedure()
    {
        return nonSpecificProcedure;
    }

    public void setNonSpecificProcedure(final Boolean nonSpecificProcedure)
    {
        this.nonSpecificProcedure = nonSpecificProcedure;
    }

    public Boolean getNonCoveredProcedure()
    {
        return nonCoveredProcedure;
    }

    public void setNonCoveredProcedure(final Boolean nonCoveredProcedure)
    {
        this.nonCoveredProcedure = nonCoveredProcedure;
    }

    @Column(length = 4000)
    public String getCptsAllowed()
    {
        return cptsAllowed;
    }

    public void setCptsAllowed(final String cptsAllowed)
    {
        this.cptsAllowed = cptsAllowed;
    }
}
