/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */

package com.medigy.presentation.form.org;

import com.medigy.presentation.model.OrgRegistrationFormModel;
import com.medigy.service.dto.org.AddOrganization;
import com.medigy.service.dto.org.RegisteredOrg;
import com.medigy.service.dto.person.RegisterPatientParameters;
import com.medigy.service.org.OrgRegistrationService;
import com.medigy.wicket.DefaultApplication;
import com.medigy.wicket.form.FormMode;
import com.medigy.wicket.form.RecordEditorForm;
import com.medigy.wicket.panel.DefaultFormPanel;
import wicket.IFeedback;
import wicket.model.BoundCompoundPropertyModel;

public class OrgRegistrationFormPanel extends DefaultFormPanel
{
    public OrgRegistrationFormPanel(final String componentName, final FormMode mode)
    {
        super(componentName, mode);
    }

    protected OrgRegistrationForm createForm(final String componentName, final IFeedback feedback, final FormMode mode)
    {
        final OrgRegistrationService service = (OrgRegistrationService) ((DefaultApplication) getApplication()).getService(OrgRegistrationService.class);
        final AddOrganization newOrgParameters = service.getOrganizationParameters();
        return new OrgRegistrationForm(componentName, feedback, new BoundCompoundPropertyModel(new OrgRegistrationFormModel(newOrgParameters)), newOrgParameters, mode, RegisterPatientParameters.class);
    }

    protected class OrgRegistrationForm extends RecordEditorForm
    {
        public OrgRegistrationForm(final String componentName, final IFeedback feedback, BoundCompoundPropertyModel model, final AddOrganization params, final FormMode formPerspective, final Class paramClass)
        {
            super(componentName, model, feedback, formPerspective, paramClass);
        }

        public void initInsert()
        {
        }

        public void initUpdate()
        {
        }

        public void initDelete()
        {
        }


        public void onSubmitInsert()
        {
            OrgRegistrationFormModel org = (OrgRegistrationFormModel)getModelObject();
            OrgRegistrationService service = (OrgRegistrationService)((DefaultApplication) getApplication()).getService(OrgRegistrationService.class);
            RegisteredOrg registeredOrg = service.registerOrganization(org, getFormMode().getLabel());
            if(registeredOrg.getOrgId() != null)
                info("Saved organization: " + ((AddOrganization) registeredOrg.getParameters()).getName());

            if(registeredOrg.getErrorMessage() != null)
                info("Errors: " + registeredOrg.getErrorMessage());
        }

        public void onSubmitUpdate()
        {
            OrgRegistrationFormModel org = (OrgRegistrationFormModel)getModelObject();
            OrgRegistrationService service = (OrgRegistrationService)((DefaultApplication) getApplication()).getService(OrgRegistrationService.class);
            RegisteredOrg registeredOrg = service.registerOrganization(org, getFormMode().getLabel());
            if(registeredOrg.getOrgId() != null)
                info("Saved organization: " + ((AddOrganization) registeredOrg.getParameters()).getName());

            if(registeredOrg.getErrorMessage() != null)
                info("Errors: " + registeredOrg.getErrorMessage());
        }

        public void onSubmitDelete()
        {

        }
    }
}
