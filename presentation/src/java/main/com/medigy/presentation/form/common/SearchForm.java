/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.presentation.form.common;

import com.medigy.presentation.model.common.ServiceSearchResultModel;
import wicket.IFeedback;
import wicket.markup.html.form.Form;
import wicket.model.IModel;

public abstract class SearchForm extends Form
{
    private ServiceSearchResultModel resultModel;

    public SearchForm(final String id, final IFeedback feedback)
    {
        super(id, feedback);
    }

    public SearchForm(final String id, IModel model, final IFeedback feedback)
    {
        super(id, model, feedback);
    }

    /**
     * Abstract method to use to add fields or set the form's model after construction of the form
     */
    public abstract void initializeForm();
}
