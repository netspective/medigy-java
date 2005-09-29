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
package com.medigy.persist.model.order;

import com.medigy.persist.reference.custom.health.MedicationDurationType;
import com.medigy.persist.reference.custom.health.MedicationType;
import com.medigy.persist.reference.type.UnitOfMeasureType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class PerscriptionItem extends OrderItem
{
    public static final String PK_COLUMN_NAME = "perscription_item_id";

    private MedicationType medicationType;
    private String dosageForm;
    private Float dose;
    private UnitOfMeasureType doseUnits;
    private boolean allowGeneric;
    private String frequency;
    private String directions;
    private String notes;
    private Long numberOfRefills;
    private Long duration;
    private MedicationDurationType durationType;

    @ManyToOne
    @JoinColumn(name = MedicationType.PK_COLUMN_NAME)
    public MedicationType getMedicationType()
    {
        return medicationType;
    }

    public void setMedicationType(final MedicationType medicationType)
    {
        this.medicationType = medicationType;
    }

    @Column(length = 128, name = "dosage_form")
    public String getDosageForm()
    {
        return dosageForm;
    }

    public void setDosageForm(final String dosageForm)
    {
        this.dosageForm = dosageForm;
    }

    public Float getDose()
    {
        return dose;
    }

    public void setDose(final Float dose)
    {
        this.dose = dose;
    }

    @ManyToOne
    @JoinColumn(name = UnitOfMeasureType.PK_COLUMN_NAME)
    public UnitOfMeasureType getDoseUnits()
    {
        return doseUnits;
    }

    public void setDoseUnits(final UnitOfMeasureType doseUnits)
    {
        this.doseUnits = doseUnits;
    }

    public boolean isAllowGeneric()
    {
        return allowGeneric;
    }

    public void setAllowGeneric(final boolean allowGeneric)
    {
        this.allowGeneric = allowGeneric;
    }

    public String getFrequency()
    {
        return frequency;
    }

    public void setFrequency(final String frequency)
    {
        this.frequency = frequency;
    }

    public String getDirections()
    {
        return directions;
    }

    public void setDirections(final String directions)
    {
        this.directions = directions;
    }

    public String getNotes()
    {
        return notes;
    }

    public void setNotes(final String notes)
    {
        this.notes = notes;
    }

    public Long getNumberOfRefills()
    {
        return numberOfRefills;
    }

    public void setNumberOfRefills(final Long numberOfRefills)
    {
        this.numberOfRefills = numberOfRefills;
    }

    public Long getDuration()
    {
        return duration;
    }

    public void setDuration(final Long duration)
    {
        this.duration = duration;
    }

    @ManyToOne
    @JoinColumn(name = MedicationDurationType.PK_COLUMN_NAME)
    public MedicationDurationType getDurationType()
    {
        return durationType;
    }

    public void setDurationType(final MedicationDurationType durationType)
    {
        this.durationType = durationType;
    }
}
