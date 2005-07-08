package com.medigy.wicket.form;

import wicket.Component;
import wicket.IFeedback;
import wicket.MarkupContainer;
import wicket.markup.html.form.FormComponent;
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
                  throw new RuntimeException("Invalid Mode");
        }
    }

    public MarkupContainer add(Component component)
    {
        final MarkupContainer mc = super.add(component);

        if(component instanceof FormComponent)
        {
            final FormComponent fc = (FormComponent) component;
//            final ReflectedFormFieldDefn rffd = getReflectedFields().get(fc);
        }

        return mc;
    }

    public void onSubmit()
    {
        switch(getFormMode())
        {
            case INSERT:
                onSubmitInsert();
                break;
            case UPDATE:
                onSubmitUpdate();
                break;
            case DELETE:
                onSubmitDelete();
                break;
            default:
                throw new RuntimeException("Invalid Mode");
        }
    }

    public void initInsert() {}
    public void initUpdate() {}
    public void initDelete() {}

    public void onSubmitInsert() {}
    public void onSubmitUpdate() {}
    public void onSubmitDelete() {}
}
