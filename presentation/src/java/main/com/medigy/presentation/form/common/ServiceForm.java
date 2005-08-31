/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.presentation.form.common;

import com.medigy.service.Service;
import com.medigy.wicket.form.BaseForm;
import com.medigy.wicket.form.FormMode;
import wicket.IFeedback;
import wicket.model.IModel;

/**
 * A form that relies on a service for creation
 */
public abstract class ServiceForm extends BaseForm
{
    private Service service;

    public ServiceForm(final String id, final IFeedback feedback, final Service service)
    {
        super(id, feedback);
        this.service = service;
        initializeForm();
    }

    public ServiceForm(final String id, IModel model, final IFeedback feedback, final Service service)
    {
        super(id, model, feedback, null, FormMode.NONE);
        this.service = service;
        initializeForm();
    }

    public Service getService()
    {
        return service;
    }

    /**
     * Abstract method to use to add fields or set the form's model after construction of the form
     */
    public abstract void initializeForm();
}
