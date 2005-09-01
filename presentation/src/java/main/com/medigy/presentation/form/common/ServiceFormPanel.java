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
package com.medigy.presentation.form.common;

import com.medigy.service.SearchService;
import com.medigy.service.Service;
import com.medigy.wicket.DefaultApplication;
import com.medigy.wicket.form.FormMode;
import wicket.markup.html.form.Form;
import wicket.markup.html.panel.FeedbackPanel;
import wicket.markup.html.panel.Panel;
import wicket.feedback.IFeedback;

/**
 * Panel containing a form that relies on a service for creation of it. The panel also
 * contains a feedpack panel for the form.
 */
public abstract class ServiceFormPanel extends Panel
{
    public static final String FORM_COMPONENT_ID = "form";
    public static final String FEEDBACK_COMPONENT_ID = "feedback";

    private Service service;
    private Form form;

    public ServiceFormPanel(final String id, final Class<? extends Service> serviceClass)
    {
        this(id, FormMode.NONE, serviceClass);
    }

    public ServiceFormPanel(final String id, final FormMode formMode, final Class<? extends Service> serviceClass)
    {
        super(id);
        if (serviceClass != null)
            this.service = (SearchService) ((DefaultApplication) getApplication()).getService(serviceClass);
        final FeedbackPanel feedback = new FeedbackPanel(FEEDBACK_COMPONENT_ID);
        add(feedback);
        add(form = createForm(FORM_COMPONENT_ID, feedback, formMode));
    }

    /**
     * Gets the  associated service
     * @return Service object
     */
    public Service getService()
    {
        return service;
    }

    /**
     * Gets the child component form
     * @return Form child
     */
    public Form getForm()
    {
        return form;
    }

    /**
     * Creates the service-related form component
     * @param id        Form component ID
     * @param feedback  the form related feedback panel
     * @param mode      the form mode
     * @return          form
     */
    public abstract Form createForm(final String id, final IFeedback feedback, final FormMode mode);
}
