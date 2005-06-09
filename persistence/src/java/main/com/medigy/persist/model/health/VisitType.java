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
package com.medigy.persist.model.health;

import com.medigy.persist.model.common.AbstractTopLevelEntity;
import com.medigy.persist.model.insurance.FeeSchedule;
import com.medigy.persist.model.org.Organization;
import com.medigy.persist.model.person.Person;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Size;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.Transient;
import java.util.Set;
import java.util.HashSet;

/**
 * Also known as Appointment Type
 */
@Entity
public class VisitType extends AbstractTopLevelEntity
{
    public static final String PK_COLUMN_NAME = "visit_type_id";

    private Long visitTypeId;
    private VisitType parentVisitType;
    private String caption;
    private Long duration;
    private Long lagTime;
    private Long leadTime;
    private Boolean backToBackAllowed;
    private Boolean multipleSimultaneousAllowed;
    private Long simultaneousLimit;
    private Long amLimit;
    private Long pmLimit;
    private Long dayLimit;
    private Organization organization;
    private FeeSchedule feeSchedule;
    private Long appointmentWidth;

    private Set<PersonVisitType> personVisitTypes = new HashSet<PersonVisitType>();
    private Set<VisitType> childVisitTypes = new HashSet<VisitType>();

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getVisitTypeId()
    {
        return visitTypeId;
    }

    public void setVisitTypeId(final Long visitTypeId)
    {
        this.visitTypeId = visitTypeId;
    }

    @Column(nullable = false, length = 128)
    @NotNull
    public String getCaption()
    {
        return caption;
    }

    public void setCaption(final String caption)
    {
        this.caption = caption;
    }

    @Column(nullable = false)
    @NotNull
    public Long getDuration()
    {
        return duration;
    }

    public void setDuration(final Long duration)
    {
        this.duration = duration;
    }

    public Long getLagTime()
    {
        return lagTime;
    }

    public void setLagTime(final Long lagTime)
    {
        this.lagTime = lagTime;
    }

    public Long getLeadTime()
    {
        return leadTime;
    }

    public void setLeadTime(final Long leadTime)
    {
        this.leadTime = leadTime;
    }

    public Boolean getBackToBackAllowed()
    {
        return backToBackAllowed;
    }

    public void setBackToBackAllowed(final Boolean backToBackAllowed)
    {
        this.backToBackAllowed = backToBackAllowed;
    }

    public Boolean getMultipleSimultaneousAllowed()
    {
        return multipleSimultaneousAllowed;
    }

    public void setMultipleSimultaneousAllowed(final Boolean multipleSimultaneousAllowed)
    {
        this.multipleSimultaneousAllowed = multipleSimultaneousAllowed;
    }

    public Long getSimultaneousLimit()
    {
        return simultaneousLimit;
    }

    public void setSimultaneousLimit(final Long simultaneousLimit)
    {
        this.simultaneousLimit = simultaneousLimit;
    }

    public Long getAmLimit()
    {
        return amLimit;
    }

    public void setAmLimit(final Long amLimit)
    {
        this.amLimit = amLimit;
    }

    public Long getPmLimit()
    {
        return pmLimit;
    }

    public void setPmLimit(final Long pmLimit)
    {
        this.pmLimit = pmLimit;
    }

    public Long getDayLimit()
    {
        return dayLimit;
    }

    public void setDayLimit(final Long dayLimit)
    {
        this.dayLimit = dayLimit;
    }

    @Column(nullable = false)
    @NotNull
    public Organization getOrganization()
    {
        return organization;
    }

    public void setOrganization(final Organization organization)
    {
        this.organization = organization;
    }

    @ManyToOne
    @JoinColumn(name = FeeSchedule.PK_COLUMN_NAME)
    public FeeSchedule getFeeSchedule()
    {
        return feeSchedule;
    }

    public void setFeeSchedule(final FeeSchedule feeSchedule)
    {
        this.feeSchedule = feeSchedule;
    }

    public Long getAppointmentWidth()
    {
        return appointmentWidth;
    }

    public void setAppointmentWidth(final Long appointmentWidth)
    {
        this.appointmentWidth = appointmentWidth;
    }

    /**
     * Gets all the people associated with the visit type
     * @return
     */
    @OneToMany(mappedBy = "visitType", cascade = CascadeType.ALL)
    @Size(min = 1)
    public Set<PersonVisitType> getPersonVisitTypes()
    {
        return personVisitTypes;
    }

    public void setPersonVisitTypes(final Set<PersonVisitType> personVisitTypes)
    {
        this.personVisitTypes = personVisitTypes;
    }

    @Transient
    public void addPersonVisitType(final Person person)
    {
        final PersonVisitType pvt = new PersonVisitType();
        pvt.setPerson(person);
        pvt.setVisitType(this);
        this.personVisitTypes.add(pvt);
    }

    @ManyToOne
    @JoinColumn(name = "parent_visit_type_id", referencedColumnName = VisitType.PK_COLUMN_NAME)
    public VisitType getParentVisitType()
    {
        return parentVisitType;
    }

    public void setParentVisitType(final VisitType parentVisitType)
    {
        this.parentVisitType = parentVisitType;
    }

    @OneToMany(mappedBy = "parentVisitType", cascade = CascadeType.ALL)
    public Set<VisitType> getChildVisitTypes()
    {
        return childVisitTypes;
    }

    public void setChildVisitTypes(final Set<VisitType> childVisitTypes)
    {
        this.childVisitTypes = childVisitTypes;
    }

}
