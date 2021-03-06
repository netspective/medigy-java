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
package com.medigy.service.impl.person;

import com.medigy.persist.reference.custom.claim.ClaimType;
import com.medigy.persist.reference.custom.health.HealthCareVisitRoleType;
import com.medigy.persist.reference.custom.invoice.InvoiceStatusType;
import com.medigy.service.AbstractService;
import com.medigy.service.ServiceVersion;
import com.medigy.service.dto.ServiceParameters;
import com.medigy.service.dto.ServiceReturnValues;
import com.medigy.service.dto.person.PatientWorkListItem;
import com.medigy.service.dto.person.PatientWorkListItemImpl;
import com.medigy.service.dto.person.PatientWorklistParameters;
import com.medigy.service.dto.person.PatientWorklistReturnValues;
import com.medigy.service.person.PatientWorklistService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatientWorklistServiceImpl extends AbstractService implements PatientWorklistService
{
    private static final Log log = LogFactory.getLog(PatientWorklistServiceImpl.class);

    private static final List<InvoiceStatusType.Cache> excludeStatusList = Arrays.asList(new InvoiceStatusType.Cache[] {
            InvoiceStatusType.Cache.INCOMPLETE, InvoiceStatusType.Cache.PENDING,
            InvoiceStatusType.Cache.ON_HOLD, InvoiceStatusType.Cache.CLOSED, InvoiceStatusType.Cache.VOID
            });

    public static final String ACCOUNT_BALANCE_HQL =
            "SELECT visit.patient.id, sum(invoice.balance) " +
            "FROM Invoice as invoice " +
            "JOIN invoice.visit as visit " +
            "JOIN  invoice.invoiceStatuses as statuses WITH " +
            "       statuses.invoiceStatusDate = (select max(status.invoiceStatusDate) from InvoiceStatus status where invoice = status.invoice GROUP BY invoice) AND " +
            "       statuses.type.id NOT IN (:excludeStatusList) " +
            "GROUP BY visit.patient " +
            "ORDER BY visit.patient.id";
    public static final String PATIENT_BALANCE_HQL =
            "SELECT visit.patient.id, sum(invoice.balance) " +
            "FROM Invoice as invoice " +
            "JOIN invoice.visit as visit " +
            "JOIN  invoice.invoiceStatuses as statuses WITH " +
            "       statuses.invoiceStatusDate = (select max(status.invoiceStatusDate) from InvoiceStatus status where invoice = status.invoice GROUP BY invoice) AND " +
            "       statuses.type.id NOT IN (:excludeStatusList) " +
            "JOIN invoice.claimType as claimType WITH claimType.id = :selfpayClaimType " +
            "GROUP BY visit.patient " +
            "ORDER BY visit.patient.id";

    public PatientWorklistServiceImpl(final SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }

    private Map<Long, Float> getPatientBalance(final PatientWorklistParameters parameters)
    {
        // TODO: There is no claimType in INVOICE. Need to re-do the patient balance query
        final Query query = getSession().createQuery(PATIENT_BALANCE_HQL);
        final List<Long> idList = new ArrayList<Long>();
        for (InvoiceStatusType.Cache cache : excludeStatusList)
            idList.add(cache.getEntity().getSystemId());
        query.setParameterList("excludeStatusList", idList);
        query.setLong("selfpayClaimType", ClaimType.Cache.SELFPAY.getEntity().getClaimTypeId());

        final Map<Long, Float> balanceMap = new HashMap<Long, Float>();
        final List list = query.list();
        for (Object rowObject : list)
        {
            if (rowObject instanceof Object[])
            {
                final Object[] columnValues = (Object[]) rowObject;
                balanceMap.put((Long) columnValues[0], (Float) columnValues[1]);
            }
        }
        return balanceMap;
    }

    private Map<Long, Float> getAccountBalance(final PatientWorklistParameters parameters)
    {
        final Query query = getSession().createQuery(ACCOUNT_BALANCE_HQL);
        final List<Long> idList = new ArrayList<Long>();
        for (InvoiceStatusType.Cache cache : excludeStatusList)
            idList.add(cache.getEntity().getSystemId());
        query.setParameterList("excludeStatusList", idList);

        final Map<Long, Float> balanceMap = new HashMap<Long, Float>();
        final List list = query.list();
        for (Object rowObject : list)
        {
            if (rowObject instanceof Object[])
            {
                final Object[] columnValues = (Object[]) rowObject;
                balanceMap.put((Long) columnValues[0], (Float) columnValues[1]);
            }
        }
        return balanceMap;
    }

    public PatientWorklistReturnValues getWorkList(final PatientWorklistParameters parameters)
    {
        final List<PatientWorkListItem> itemList = new ArrayList<PatientWorkListItem>();
        Query patientWorkListQuery = null;

        // if for some reason no date is supplied, use todays date
        final Date selectedDate = parameters.getSelectedDate() != null ? parameters.getSelectedDate() : new Date();

        final Date startingTime = parameters.getStartingTime();
        final Date endingTime = parameters.getEndingTime();

        Calendar cal = new GregorianCalendar();
        final Date currentDate = new Date();
        cal.setTime(currentDate);
        // get current hour and minute
        final int currentHour = cal.get(Calendar.HOUR_OF_DAY);
        final int currentMinute = cal.get(Calendar.MINUTE);

        final String queryString = "SELECT \n" +
                    "patient.id, \n" +
                    "patient.lastName, \n" +
                    "patient.firstName, \n" +
                    "roles.person.id, \n" +
                    "roles.person.lastName, \n " +
                    "roles.person.firstName, \n" +
                    "appt.scheduledTime, \n" +
                    "appt.startTime, \n" +
                    "appt.checkoutTime, \n" +
                    "appt.id, \n" +
                    "appt.patientType.label \n" +
                    "FROM HealthCareEncounter appt \n" +
                    " join appt.patient as patient \n" +
                    " left join appt.roles as roles \n" +
                    " with roles.type.id = " + HealthCareVisitRoleType.Cache.REQ_PHYSICIAN.getEntity().getSystemId() + " \n" +
                    "WHERE \n" +
                    "   appt.scheduledTime >= :beforeTime and \n" +
                    "   appt.scheduledTime < :afterTime " +
                    "ORDER BY \n" +
                    "   appt.scheduledTime, patient.lastName, patient.firstName";

        // now set the calendar to the selected date and the current hour/minute
        cal.setTime(selectedDate);
        if (startingTime == null)
        {
            cal.set(Calendar.HOUR_OF_DAY, currentHour);
            cal.set(Calendar.MINUTE, currentMinute);
            // save this date!
            final Date baseComparisonTime = cal.getTime();
            final int beforeMinutes = parameters.getBeforeMinutes();
            final int afterMinutes = parameters.getAfterMinutes();
            cal.add(Calendar.MINUTE, 0 - beforeMinutes);
            final Date beforeTime = cal.getTime();

            cal.setTime(baseComparisonTime);
            cal.add(Calendar.MINUTE, afterMinutes);
            final Date afterTime = cal.getTime();

            patientWorkListQuery = getSession().createQuery(queryString);
            patientWorkListQuery.setTimestamp("beforeTime", beforeTime);
            patientWorkListQuery.setTimestamp("afterTime", afterTime);
        }
        else
        {
            final Calendar startCal = new GregorianCalendar();
            startCal.setTime(startingTime);

            final Calendar endCal = new GregorianCalendar();
            endCal.setTime(endingTime);

            cal.set(Calendar.HOUR_OF_DAY, startCal.get(Calendar.HOUR_OF_DAY));
            cal.set(Calendar.MINUTE, startCal.get(Calendar.MINUTE));
            final Date beforeTime = cal.getTime();

            cal.set(Calendar.HOUR_OF_DAY, endCal.get(Calendar.HOUR_OF_DAY));
            cal.set(Calendar.MINUTE, endCal.get(Calendar.MINUTE));
            final Date afterTime = cal.getTime();

            patientWorkListQuery = getSession().createQuery(queryString);
            patientWorkListQuery.setTimestamp("beforeTime", beforeTime);
            patientWorkListQuery.setTimestamp("afterTime", afterTime);
        }
        final List list = patientWorkListQuery.list();
        if (log.isInfoEnabled())
            log.info(patientWorkListQuery.getQueryString() + " \n" + list.size());

        final Map<Long, Float> accountBalance = getAccountBalance(parameters);
        //final Map<Long, Float> patientBalance = getPatientBalance(parameters);
        for (int i=0; i < list.size(); i++)
        {
            final Object rowObject = list.get(i);
            if (rowObject instanceof Object[])
            {
                final PatientWorkListItemImpl item = new PatientWorkListItemImpl();
                final Object[] columnValues = (Object[]) rowObject;
                item.setPatientId((Long) columnValues[0]);

                item.setAccountBalance(accountBalance.get(item.getPatientId()));
                //item.setPatientBalance(patientBalance.get(item.getPatientId()));

                item.setPatientLastName((String) columnValues[1]);
                item.setPatientFirstName((String) columnValues[2]);
                item.setPhysicianId((Long) columnValues[3]);
                item.setPhysicianLastName((String) columnValues[4]);
                item.setPhysicianFirstName((String) columnValues[5]);
                item.setAppointmentTimestamp((Date) columnValues[6]);
                item.setCheckinTimestamp((Date) columnValues[7]);
                item.setCheckoutTimestamp((Date) columnValues[8]);
                item.setEncounterId((Long) columnValues[9]);
                item.setPatientType((String) columnValues[10]);
                itemList.add(item);
            }
        }
        return new PatientWorklistReturnValues() {
            public List<PatientWorkListItem> getItems()
            {
                return itemList;
            }

            public ServiceParameters getParameters()
            {
                return parameters;
            }

            public String getErrorMessage()
            {
                return null;
            }
        };
    }

    public ServiceVersion[] getSupportedServiceVersions()
    {
        return new ServiceVersion[0];
    }

    public String[] isValid(ServiceParameters parameters)
    {
        return new String[0];
    }

    public ServiceReturnValues createErrorResponse(final ServiceParameters params, final String errorMessage)
    {
        return new PatientWorklistReturnValues() {
            public List<PatientWorkListItem> getItems()
            {
                return null;
            }

            public ServiceParameters getParameters()
            {
                return params;
            }

            public String getErrorMessage()
            {
                return errorMessage;
            }
        };
    }
}
