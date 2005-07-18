/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.presentation.form.common;

import wicket.markup.html.form.Form;
import wicket.IFeedback;
import wicket.model.IModel;
import com.medigy.presentation.model.common.ServiceSearchResultModel;
import com.medigy.presentation.model.common.SearchFormModelObject;

public abstract class SearchForm extends Form
{
    private ServiceSearchResultModel resultModel;

    public SearchForm(final String id, IModel model, final IFeedback feedback)
    {
        super(id, model, feedback);
    }

    
}
