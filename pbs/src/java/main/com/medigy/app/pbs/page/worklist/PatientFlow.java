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
package com.medigy.app.pbs.page.worklist;

import com.medigy.presentation.form.query.SearchResultsListView;
import com.medigy.service.ServiceVersion;
import com.medigy.service.dto.person.PatientWorkListItem;
import com.medigy.service.dto.person.PatientWorklistParameters;
import com.medigy.service.dto.person.PatientWorklistReturnValues;
import com.medigy.service.person.PatientWorklistService;
import com.medigy.wicket.DefaultApplication;
import wicket.PageParameters;
import wicket.markup.ComponentTag;
import wicket.markup.MarkupStream;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.ListView;
import wicket.markup.html.panel.Panel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PatientFlow extends AbstractWorklistPage
{
    private SearchResultsListView listView;

    public PatientFlow(final PageParameters parameters)
    {
        super(parameters);
        final PageParameters pageParameters = getPageParameters();

        try
        {
            final String timeType = pageParameters.containsKey("time") ? pageParameters.getString("time") : null;
            final String time1String =   pageParameters.getString("time1");
            final String time2String =   pageParameters.getString("time2");
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            // default to today if no date is found
            final Date selectedDate = pageParameters.containsKey("selectedDate") && pageParameters.getString("selectedDate").length() > 0 ? sdf.parse(pageParameters.getString("selectedDate")) : new Date();

            PatientWorklistParameters serviceParams = null;
            if  (time1String != null && time1String.contains(":"))
            {
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm Z");
                final Date time1 = timeFormat.parse(time1String);
                final Date time2 = timeFormat.parse(time2String);
                serviceParams = new PatientWorklistParameters() {
                    public Integer getUserId()
                    {
                        return null;
                    }

                    public Integer getOrganizationId()
                    {
                        return null;
                    }

                    public Date getSelectedDate()
                    {
                        return selectedDate;
                    }

                    public int getBeforeMinutes()
                    {
                        return 0;
                    }

                    public int getAfterMinutes()
                    {
                        return 0;
                    }

                    public Date getStartingTime()
                    {
                        return time1;
                    }

                    public Date getEndingTime()
                    {
                        return time2;
                    }

                    public ServiceVersion getServiceVersion()
                    {
                        return null;
                    }
                };
            }
            else
            {
                // DEFAULT if no value is given: 60 minutes before and 60 minutes after
                final int time1 = (time1String != null && time1String.length() > 0) ? Integer.parseInt(time1String) : 60;
                final int time2 = (time2String != null && time2String.length() > 0) ? Integer.parseInt(time2String) : 60;
                serviceParams = new PatientWorklistParameters() {
                    public Integer getUserId()
                    {
                        return null;
                    }

                    public Integer getOrganizationId()
                    {
                        return null;
                    }

                    public Date getSelectedDate()
                    {
                        return selectedDate;
                    }

                    public int getBeforeMinutes()
                    {
                        return time1;
                    }

                    public int getAfterMinutes()
                    {
                        return time2;
                    }

                    public Date getStartingTime()
                    {
                        return null;
                    }

                    public Date getEndingTime()
                    {
                        return null;
                    }

                    public ServiceVersion getServiceVersion()
                    {
                        return null;
                    }
                };
            }

            final PatientWorklistService service = (PatientWorklistService) ((DefaultApplication) getApplication()).getService(PatientWorklistService.class);
            // execute the query
            final PatientWorklistReturnValues worklist = service.getWorkList(serviceParams);
            List<PatientWorkListItem> itemList = worklist.getItems();
            PatientWorkListView listView = new PatientWorkListView("workListView", itemList);
            worklistBorder.add(listView);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public Panel getWorklistControlBarPanel(final String id)
    {
        return new PatientFlowControlBar(id);
    }

    public class PatientWorkListView extends ListView
    {
        public PatientWorkListView(final String id, final List<PatientWorkListItem> list)
        {
            super(id, list);
        }

        protected ListItem newItem(final int index)
        {
            final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
            return new ListItem(index, getListItemModel(getModel(), index))
            {
                protected void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag)
                {
                    StringBuffer buffer = new StringBuffer();
                    final PatientWorkListItem item = (PatientWorkListItem) getModelObject();

                    final Long patientId = item.getPatientId();
                    final Long encounterId = item.getEncounterId();
                    final String actionUrl = "&patientId=" + patientId + "&encounterId=" + encounterId;

                    final String patientHtml = "<a href=\"?bookmarkablePage=com.medigy.app.pbs.page.PatientRegistrationPage\"> " +
                            item.getPatientLastName() + ", "+  item.getPatientFirstName() +
                            "</a><br/><a href=\"\">" + item.getPhysicianLastName() + ", " + item.getPhysicianFirstName() + "</a>";
                    final String checkinHtml = "<a href=\"?bookmarkablePage=com.medigy.app.pbs.form.CheckInPage" + actionUrl +"\">Checkin</a>";
                    final String checkoutHtml = "<a href=\"?bookmarkablePage=com.medigy.app.pbs.form.CheckOutPage" + actionUrl+ "\">Checkout</a>";

                    buffer.append("<td>" + patientHtml + "</td>");
                    final Date appointmentTimestamp = item.getAppointmentTimestamp();
                    buffer.append("<td>" + (appointmentTimestamp != null ? sdf.format(appointmentTimestamp) : "&nbsp;") + "</td>");
                    buffer.append("<td>" + (item.getCheckinTimestamp() != null ? item.getCheckinTimestamp() : checkinHtml) + "</td>");
                    buffer.append("<td>" + (item.getCheckoutTimestamp() != null ? item.getCheckoutTimestamp() : checkoutHtml) + "</td>");
                    buffer.append("<td>" + (item.getInvoiceNumber() != null ? item.getInvoiceNumber() : "&nbsp;") + "</td>");
                    buffer.append("<td>" + (item.getCopay() != null ? item.getCopay() : "&nbsp;") + "</td>");
                    buffer.append("<td>" + (item.getAccountBalance() != null ? item.getAccountBalance() : "&nbsp;") + "</td>");
                    buffer.append("<td>" + (item.getPatientBalance() != null ? item.getPatientBalance() : "&nbsp;") + "</td>");

                    buffer.append("<td>\n" +
                            "                        <a class=\"action\" href=\"?bookmarkablePage=com.medigy.app.pbs.form.AppointmentPage\">A</a>\n" +
                            "                        <a class=\"action\" href=\"?bookmarkablePage=com.medigy.app.pbs.form.InsuranceClaimPage\">I</a>\n" +
                            "                        <a class=\"action\" href=\"?bookmarkablePage=com.medigy.app.pbs.form.ReferralPPMSPage\">R</a>\n" +
                            "                        <a class=\"action\" href=\"#\">C</a>\n" +
                            "</td> ");
                    buffer.append("<td class=\"last\">Action</td>");

                    replaceComponentTagBody(markupStream, openTag, buffer.toString());
                    super.onComponentTagBody(markupStream, openTag);
                }
            };
        }

        protected void populateItem(final ListItem item)
        {

        }
    }
}
