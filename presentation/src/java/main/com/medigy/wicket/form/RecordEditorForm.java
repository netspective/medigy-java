/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.wicket.form;

import wicket.IFeedback;
import wicket.MarkupContainer;
import wicket.model.IModel;

abstract public class RecordEditorForm extends BaseForm
{
    public interface RecordEditorModel extends IModel
    {
        public Class getServiceParametersClass();
    }

    public RecordEditorForm(final String componentName, final IModel model, final IFeedback feedback, final FormMode formMode, final Class serviceParamClass)
    {
        super(componentName, model, feedback, serviceParamClass, formMode);

        switch(formMode)
        {
            case INSERT:
                initInsert();
                break;

            case UPDATE:
                initUpdate();
                break;

            case DELETE:
                initDelete();
                break;

            default:
                  throw new RuntimeException("Invalid Mode " + formMode);
        }
    }

    protected void onValidate()
    {
        switch(getFormMode())
        {
            case INSERT:
                onValidateInsert();
                break;

            case UPDATE:
                onValidateUpdate();
                break;

            case DELETE:
                onValidateDelete();
                break;

            default:
                throw new RuntimeException("Invalid Mode " + getFormMode());
        }
    }

    public void onSubmit()
    {
        MarkupContainer mc = this.getParent();
        if(mc instanceof RecordEditorForm)
        {
        switch(getFormMode())
        {
            case INSERT:
                ((RecordEditorForm)mc).onSubmitInsert();
                break;

            case UPDATE:
                ((RecordEditorForm)mc).onSubmitUpdate();
                break;

            case DELETE:
                ((RecordEditorForm)mc).onSubmitDelete();
                break;

            default:
                throw new RuntimeException("Invalid Mode " + getFormMode());
        }
        }
    }

    public void initInsert() {}
    public void initUpdate() {}
    public void initDelete() {}

    public abstract void onSubmitInsert();
    public abstract void onSubmitUpdate();
    public abstract void onSubmitDelete();

    public void onValidateInsert() {}
    public void onValidateUpdate() {}
    public void onValidateDelete() {}
}
