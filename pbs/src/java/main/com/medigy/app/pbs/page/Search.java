/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.app.pbs.page;

import com.medigy.presentation.form.person.patient.PatientSearchFormPanel;
import com.medigy.wicket.form.FormMode;
import com.medigy.wicket.page.DefaultFormPage;

public class Search extends DefaultFormPage
{
    public Search()
    {
        super(PatientSearchFormPanel.class, FormMode.NONE);
    }
}
