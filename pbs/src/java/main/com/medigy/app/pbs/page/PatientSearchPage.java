/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.app.pbs.page;

import com.medigy.wicket.page.DefaultFormPage;
import com.medigy.wicket.form.FormMode;
import com.medigy.presentation.form.person.patient.PatientSearchFormPanel;

public class PatientSearchPage extends DefaultFormPage
{
    public PatientSearchPage()
    {
        super(PatientSearchFormPanel.class, FormMode.NONE);
    }
}
