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
package com.medigy.service.dto.person;

import java.util.Date;

public class PatientWorkListItemImpl implements PatientWorkListItem
{
    private Long encounterId;
    private Long patientId;
    private Long physicianId;
    private String patientLastName;
    private String patientFirstName;
    private String patientType;
    private String physicianLastName;
    private String physicianFirstName;
    private Date appointmentTimestamp;
    private Date checkinTimestamp;
    private Date checkoutTimestamp;
    private Float accountBalance;
    private Float patientBalance;

    public void setPatientId(final Long patientId)
    {
        this.patientId = patientId;
    }

    public void setPhysicianId(final Long physicianId)
    {
        this.physicianId = physicianId;
    }

    public void setPatientLastName(final String patientLastName)
    {
        this.patientLastName = patientLastName;
    }

    public void setPatientFirstName(final String patientFirstName)
    {
        this.patientFirstName = patientFirstName;
    }

    public void setPhysicianLastName(final String physicianLastName)
    {
        this.physicianLastName = physicianLastName;
    }

    public void setPhysicianFirstName(final String physicianFirstName)
    {
        this.physicianFirstName = physicianFirstName;
    }

    public void setAppointmentTimestamp(final Date appointmentTimestamp)
    {
        this.appointmentTimestamp = appointmentTimestamp;
    }

    public void setCheckinTimestamp(final Date checkinTimestamp)
    {
        this.checkinTimestamp = checkinTimestamp;
    }

    public void setCheckoutTimestamp(final Date checkoutTimestamp)
    {
        this.checkoutTimestamp = checkoutTimestamp;
    }

    public void setEncounterId(final Long encounterId)
    {
        this.encounterId = encounterId;
    }

    public void setPatientType(final String patientType)
    {
        this.patientType = patientType;
    }

    public Long getEncounterId()
    {
        return encounterId;
    }

    public Long getPatientId()
    {
        return patientId;
    }

    public String getPatientLastName()
    {
        return patientLastName;
    }

    public String getPatientFirstName()
    {
        return patientFirstName;
    }

    public String getPatientType()
    {
        return patientType;
    }

    public Long getPhysicianId()
    {
        return physicianId;
    }

    public String getPhysicianLastName()
    {
        return physicianLastName;
    }

    public String getPhysicianFirstName()
    {
        return physicianFirstName;
    }

    public Date getAppointmentTimestamp()
    {
        return appointmentTimestamp;
    }

    public Date getCheckinTimestamp()
    {
        return checkinTimestamp;
    }

    public Date getCheckoutTimestamp()
    {
        return checkoutTimestamp;
    }

    public String getInvoiceNumber()
    {
        return null;
    }

    public Long getInvoiceId()
    {
        return null;
    }

    public Float getCopay()
    {
        return null;
    }

    public Float getAccountBalance()
    {
        return this.accountBalance;
    }

    public Float getPatientBalance()
    {
        return patientBalance;
    }

    public void setAccountBalance(final Float balance)
    {
        this.accountBalance = balance;
    }

    public void setPatientBalance(final Float balance)
    {
        this.patientBalance = balance;
    }
}
