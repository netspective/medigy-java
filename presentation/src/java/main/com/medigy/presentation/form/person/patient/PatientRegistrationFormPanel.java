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
package com.medigy.presentation.form.person.patient;

import com.medigy.presentation.model.PatientRegistrationFormModel;
import com.medigy.service.dto.person.RegisterPatientParameters;
import com.medigy.service.dto.person.RegisteredPatient;
import com.medigy.service.person.PatientRegistrationService;
import com.medigy.wicket.DefaultApplication;
import com.medigy.wicket.form.FormMode;
import com.medigy.wicket.form.RecordEditorForm;
import com.medigy.wicket.panel.DefaultFormPanel;
import wicket.model.BoundCompoundPropertyModel;
import wicket.feedback.IFeedback;

public class PatientRegistrationFormPanel extends DefaultFormPanel
{
    public PatientRegistrationFormPanel(final String componentName, final FormMode mode)
    {
        super(componentName, mode);
    }

    protected RecordEditorForm createForm(final String componentName, final IFeedback feedback, final FormMode mode)
    {
        final PatientRegistrationService service = (PatientRegistrationService) ((DefaultApplication) getApplication()).getService(PatientRegistrationService.class);
        final RegisterPatientParameters newPatientParameters = service.getNewPatientParameters();

        return new PatientRegistrationForm(componentName, feedback, new BoundCompoundPropertyModel(new PatientRegistrationFormModel(newPatientParameters)), newPatientParameters, mode, RegisterPatientParameters.class);
    }

    protected class PatientRegistrationForm extends RecordEditorForm
    {
        public PatientRegistrationForm(final String componentName, final IFeedback feedback, BoundCompoundPropertyModel model,
            final RegisterPatientParameters params, final FormMode formPerspective, final Class paramClz)
        {
            super(componentName, model, feedback, formPerspective, paramClz);

        }

        public void initInsert()
        {
             // hide form fields?
        }

        public void initUpdate()
        {
            // hide form fields
        }

        public void initDelete()
        {

        }


        public void onSubmitInsert()
        {
            PatientRegistrationFormModel patient = (PatientRegistrationFormModel)getModelObject();
            PatientRegistrationService service = (PatientRegistrationService)((DefaultApplication) getApplication()).getService(PatientRegistrationService.class);
            RegisteredPatient registeredPatient = service.registerPatient(patient, getFormMode().getLabel());
            if(registeredPatient.getPatientId() != null)
                info("Saved patient: " + ((RegisterPatientParameters)registeredPatient.getParameters()).getFirstName() + " " +
                        ((RegisterPatientParameters)registeredPatient.getParameters()).getLastName());

            if(registeredPatient.getErrorMessage() != null)
                info("Errors: " + registeredPatient.getErrorMessage());
        }

        public void onSubmitUpdate()
        {
            PatientRegistrationFormModel patient = (PatientRegistrationFormModel)getModelObject();
            PatientRegistrationService service = (PatientRegistrationService)((DefaultApplication) getApplication()).getService(PatientRegistrationService.class);
            RegisteredPatient registeredPatient = service.registerPatient(patient, getFormMode().getLabel());
            if(registeredPatient.getPatientId() != null)
                info("Saved patient: " + ((RegisterPatientParameters)registeredPatient.getParameters()).getFirstName() + " " +
                        ((RegisterPatientParameters)registeredPatient.getParameters()).getLastName());

            if(registeredPatient.getErrorMessage() != null)
                info("Errors: " + registeredPatient.getErrorMessage());
        }

        public void onSubmitDelete()
        {

        }
    }
}
