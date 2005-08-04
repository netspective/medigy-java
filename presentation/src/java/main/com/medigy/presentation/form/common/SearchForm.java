/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.presentation.form.common;

import com.medigy.presentation.model.common.ServiceSearchResultModel;
import com.medigy.wicket.form.BaseForm;
import com.medigy.wicket.form.FormMode;
import wicket.IFeedback;
import wicket.model.IModel;

public abstract class SearchForm extends BaseForm
{
    private ServiceSearchResultModel resultModel;

    public SearchForm(final String id, final IFeedback feedback)
    {
        super(id, feedback);
    }

    public SearchForm(final String id, IModel model, final IFeedback feedback)
    {
        super(id, model, feedback, null, FormMode.NONE);
    }

    /**
     * Abstract method to use to add fields or set the form's model after construction of the form
     */
    public abstract void initializeForm();
}
