/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.presentation.form.person.patient;

import com.medigy.presentation.form.query.QueryDefinitionSearchFormPanel;
import com.medigy.service.person.PatientSearchQueryDefinition;
import com.medigy.wicket.form.FormMode;

public class PatientSearchFormPanel  extends QueryDefinitionSearchFormPanel
{
    public PatientSearchFormPanel(final String componentName, final FormMode formMode)
    {
        // person ID, last name, first or last name, ssn, DOB, phone number, account number, chart number

        // 1. control bar
        // 2. view
        // 3. page
        super(componentName, formMode, PatientSearchQueryDefinition.class);
    }
}
