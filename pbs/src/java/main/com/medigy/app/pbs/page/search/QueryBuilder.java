/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.app.pbs.page.search;

import com.medigy.presentation.form.person.patient.PatientSearchFormPanel;
import com.medigy.wicket.form.FormMode;
import com.medigy.wicket.page.DefaultFormPage;

public class QueryBuilder extends DefaultFormPage
{
    public QueryBuilder()
    {
        super(PatientSearchFormPanel.class, FormMode.NONE);
    }
}
