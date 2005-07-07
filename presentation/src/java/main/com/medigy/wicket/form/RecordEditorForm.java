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

    private final FormMode mode;

    public RecordEditorForm(final String componentName, final IModel model, final IFeedback feedback, final FormMode formMode, final Class serviceParamClass)
    {
        super(componentName, model, feedback, serviceParamClass);
        this.mode = formMode;

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

        if(mc instanceof FormComponent)
        {
            final FormComponent fc = (FormComponent) component;
            //final ReflectedFormFieldDefn rffd = getReflectedFields().get(fc);
            // check for InvisibleWhen annotation and set visible to false based on the mode
        }

        return mc;
    }

    public void initInsert() {}
    public void initUpdate() {}
    public void initDelete() {}

    public void onSubmitInsert() {}
    public void onSubmitUpdate() {}
    public void onSubmitDelete() {}

    public FormMode getFormMode()
    {
        return this.mode;
    }
}
