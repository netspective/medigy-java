/*
 * Copyright (c) 2000-2003 Netspective Communications LLC. All rights reserved.
 *
 * Netspective Communications LLC ("Netspective") permits redistribution, modification and use of this file in source
 * and binary form ("The Software") under the Netspective Source License ("NSL" or "The License"). The following
 * conditions are provided as a summary of the NSL but the NSL remains the canonical license and must be accepted
 * before using The Software. Any use of The Software indicates agreement with the NSL.
 *
 * 1. Each copy or derived work of The Software must preserve the copyright notice and this notice unmodified.
 *
 * 2. Redistribution of The Software is allowed in object code form only (as Java .class files or a .jar file
 *    containing the .class files) and only as part of an application that uses The Software as part of its primary
 *    functionality. No distribution of the package is allowed as part of a software development kit, other library,
 *    or development tool without written consent of Netspective. Any modified form of The Software is bound by these
 *    same restrictions.
 *
 * 3. Redistributions of The Software in any form must include an unmodified copy of The License, normally in a plain
 *    ASCII text file unless otherwise agreed to, in writing, by Netspective.
 *
 * 4. The names "Netspective", "Axiom", "Commons", "Junxion", and "Sparx" are trademarks of Netspective and may not be
 *    used to endorse products derived from The Software without without written consent of Netspective. "Netspective",
 *    "Axiom", "Commons", "Junxion", and "Sparx" may not appear in the names of products derived from The Software
 *    without written consent of Netspective.
 *
 * 5. Please attribute functionality where possible. We suggest using the "powered by Netspective" button or creating
 *    a "powered by Netspective(tm)" link to http://www.netspective.com for each application using The Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT,
 * ARE HEREBY DISCLAIMED.
 *
 * NETSPECTIVE AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE OR ANY THIRD PARTY AS A
 * RESULT OF USING OR DISTRIBUTING THE SOFTWARE. IN NO EVENT WILL NETSPECTIVE OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THE SOFTWARE, EVEN
 * IF HE HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * @author Shahid N. Shah
 */

/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.wicket.form;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.validator.ValidatorClass;

import com.medigy.presentation.model.ChoicesFactory;
import com.medigy.wicket.DefaultApplication;
import wicket.IComponentResolver;
import wicket.IFeedback;
import wicket.MarkupContainer;
import wicket.markup.ComponentTag;
import wicket.markup.MarkupStream;
import wicket.markup.html.form.DropDownChoice;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.FormComponent;
import wicket.markup.html.form.ListMultipleChoice;
import wicket.markup.html.form.RadioChoice;
import wicket.markup.html.form.model.IChoice;
import wicket.model.BoundCompoundPropertyModel;
import wicket.model.IModel;
import wicket.util.value.ValueMap;

public class BaseForm extends Form implements IComponentResolver
{
    private static final Log log = LogFactory.getLog(BaseForm.class);

    public static final String FIELD_LABEL_SUFFIX = "_label";
    public static final String FIELD_CONTROL_SUFFIX = "_control";
    protected static final Collection TEST_CHOICES = new ArrayList();
    protected static final Collection RELATIONSHIP_TO_RESPONSIBLE_CHOICES = new ArrayList();
    protected static final Collection EMPLOYMENT_STATUS_CHOICES = new ArrayList();
    protected static final Collection INSURANCE_SEQUENCE_CHOICES = new ArrayList();
    protected static final Collection PATIENT_RELATIONSHIP_TO_INSURED_CHOICES = new ArrayList();
    public static final String ATTRNAME_WICKET_ID = "wicket:id";

    static
    {
        TEST_CHOICES.add(new IChoice()
        {
            public String getDisplayValue()
            {
                return "Test";
            }

            public String getId()
            {
                return "Id";
            }

            public Object getObject()
            {
                return "Test";
            }
        });

        RELATIONSHIP_TO_RESPONSIBLE_CHOICES.add("Self");
        RELATIONSHIP_TO_RESPONSIBLE_CHOICES.add("Spouse");

        INSURANCE_SEQUENCE_CHOICES.add("Primary");
        INSURANCE_SEQUENCE_CHOICES.add("Secondary");
        INSURANCE_SEQUENCE_CHOICES.add("Tertiary");

        PATIENT_RELATIONSHIP_TO_INSURED_CHOICES.add("Self");
        PATIENT_RELATIONSHIP_TO_INSURED_CHOICES.add("Spouse");
        PATIENT_RELATIONSHIP_TO_INSURED_CHOICES.add("Paternal");
        PATIENT_RELATIONSHIP_TO_INSURED_CHOICES.add("Maternal");

    }

    protected interface TagResolver
    {
        public boolean addComponent(final ComponentTag tag);
    }

    private ChoicesFactory choicesFactory;
    private Map<String, TagResolver> tagResolvers = new HashMap<String, TagResolver>();

    protected BaseForm(final String componentName)
    {
        super(componentName);
    }

    protected BaseForm(final String componentName, final IFeedback feedback)
    {
        super(componentName, feedback);
    }

    public BaseForm(String componentName, IModel model, IFeedback feedback)
    {
        super(componentName, model, feedback);

        tagResolvers.put("legend", new TagResolver()
        {
            public boolean addComponent(final ComponentTag tag)
            {
                add(new FieldGroupLabel(tag.getAttributes().getString(ATTRNAME_WICKET_ID)));
                return true;
            }
        });

        tagResolvers.put("label", new TagResolver()
        {
            public boolean addComponent(final ComponentTag tag)
            {
                final String labelId = tag.getAttributes().getString(ATTRNAME_WICKET_ID);
                final String fieldName = labelId.endsWith(FIELD_LABEL_SUFFIX) ? labelId.substring(0, FIELD_LABEL_SUFFIX.length()) : labelId;
                add(new FieldLabel(fieldName));
                return true;
            }
        });

        final TagResolver controlResolver = new TagResolver()
        {
            public boolean addComponent(final ComponentTag tag)
            {
                final String controlid = tag.getAttributes().getString(ATTRNAME_WICKET_ID);
                final String fieldName = controlid.endsWith(FIELD_CONTROL_SUFFIX) ? controlid.substring(0, FIELD_CONTROL_SUFFIX.length()) : controlid;
                final Class serviceBeanClass = getModel().getObject(null).getClass();
                add(((BoundCompoundPropertyModel) getModel()).bind(FormFieldFactory.getInstance().createField(fieldName, serviceBeanClass, getModel().getClass()), fieldName));
                return true;
            }
        };

        tagResolvers.put("input", controlResolver);
        tagResolvers.put("select", controlResolver);
        tagResolvers.put("span", controlResolver);
    }

    // TODO: move this into DefaultApplication
    private ChoicesFactory getChoicesFactory()
    {
        if (choicesFactory == null)
        {
            choicesFactory = (ChoicesFactory) ((DefaultApplication) getApplication()).getService(ChoicesFactory.class);
        }
        return choicesFactory;
    }

    public boolean resolve(final MarkupContainer container, final MarkupStream markupStream,
                           final ComponentTag tag)
    {
        if(tag.getAttributes().containsKey(ATTRNAME_WICKET_ID))
        {
            final TagResolver resolver = tagResolvers.get(tag.getName());
            if(resolver != null)
                return resolver.addComponent(tag);
        }
        return false;
    }

    protected void addLabeledTextField(final String fieldName)
    {
        add(new FieldLabel(fieldName));
        add(new TextField(fieldName));
    }

    protected void addLabeledField(final String fieldName, final Class cls)
    {
        add(new FieldLabel(fieldName));
        add(((BoundCompoundPropertyModel)this.getModel()).bind(FormFieldFactory.getInstance().createField(fieldName, cls, getModel().getClass()), fieldName));
    }

    protected void addLabeledSelectField(final String fieldName, Collection choices)
    {
        add(new FieldLabel(fieldName));
        add(new DropDownChoice(fieldName, choices));
    }

    protected void addLabeledSelectField(final String fieldName)
    {
        add(new FieldLabel(fieldName));
        add(new DropDownChoice(fieldName, TEST_CHOICES));
    }

    protected void addLabeledSelectField(final String fieldName, final Class referenceEntity)
    {
        add(new FieldLabel(fieldName));
        add(new DropDownChoice(fieldName, getChoicesFactory().getReferenceEntityChoices(referenceEntity)));
    }

    protected void addLabeledMultiListField(final String fieldName, final Class multiListChoices)
    {
        add(new FieldLabel(fieldName));
        add(new ListMultipleChoice(fieldName, getChoicesFactory().getReferenceEntityChoices(multiListChoices)));
    }

    protected void addLabeledMultiCheckField(final String fieldName, final Class multiCheckChoices)
    {
        add(new FieldLabel(fieldName));
        add(new ListMultipleChoice(fieldName, getChoicesFactory().getReferenceEntityChoices(multiCheckChoices)));
    }

    protected void addLabeledRadioChoiceField(final String fieldName, Collection choices)
    {
        add(new FieldLabel(fieldName));
        add(new RadioChoice(fieldName, choices));
    }

    protected void addLabeledRadioChoiceField(final String fieldName, final Class choicesClass)
    {
        add(new FieldLabel(fieldName));
        add(new RadioChoice(fieldName, getChoicesFactory().getReferenceEntityChoices(choicesClass)));
    }

    protected void addLabeledCheckBox(final String fieldName)
    {
        add(new FieldLabel(fieldName));
        add(new CheckBox(fieldName));
    }

    protected void onFormComponentTag(final ComponentTag componentTag, final String fieldControlId, final long fieldFlags)
    {
        final ValueMap attributes = componentTag.getAttributes();
        final String idAttr = attributes.getString("id");
        if(idAttr == null)
            attributes.put("id", fieldControlId);  // label will point to this ID using for="{fieldName}-control"

        componentTag.put("onfocus", "return controlOnFocus(this, event)");
        componentTag.put("onchange", "controlOnChange(this, event)");
        componentTag.put("onblur", "controlOnBlur(this, event)");
        componentTag.put("onkeypress", "return controlOnKeypress(this, event)");
        componentTag.put("onclick", "controlOnClick(this, event)");

        if((fieldFlags & FieldFlags.REQUIRED) != 0)
            componentTag.put("class", "required");
    }

    protected void onComponentTag(ComponentTag componentTag)
    {
        super.onComponentTag(componentTag);
        componentTag.put("onsubmit", "return(document.forms[0].dialog.isValid())");
    }

    protected void onComponentTagBody(final MarkupStream markupStream, final ComponentTag componentTag)
    {
        final StringBuffer script = new StringBuffer("\n<script>\nvar dialog = new Dialog(document.forms[0], true, false, true);\n");
        visitFormComponents(new FormComponent.IVisitor()
        {
            public void formComponent(final FormComponent formComponent)
            {
                if(formComponent instanceof FormFieldJavaScriptProvider)
                {
                    FormFieldJavaScriptProvider jsProviderFormField = (FormFieldJavaScriptProvider) formComponent;
                    script.append(jsProviderFormField.getJavaScript("dialog", "document.forms[0]"));
                }
            }
        });
        script.append("dialog.finalizeContents();\n</script>");

        super.onComponentTagBody(markupStream, componentTag);
        getResponse().write(script.toString());
    }

    public Class<?> getReturnType(Class<?> theClass, String propertyName)
    {
        try
        {
            PropertyDescriptor[] descs = Introspector.getBeanInfo(theClass).getPropertyDescriptors();
            for(PropertyDescriptor desc : descs)
            {
                if(propertyName.equals(desc.getName()))
                    return desc.getReadMethod().getReturnType();
            }
        }
        catch(IntrospectionException e)
        {
            log.error("IntrospectionException");
        }
        return null;
    }

    private Set<Annotation> getConstraints(Annotation[] annotations)
    {
        Set<Annotation> constraints = new HashSet<Annotation>();
        for(Annotation a : annotations)
        {
            if (isConstraint(a))
                constraints.add(a);
        }
        return constraints;
    }

    public boolean isConstraint(Annotation annotation)
    {
        if(annotation == null)
        {
            log.error("Annotation null in isConstraint");
            throw new IllegalArgumentException("Not a legal value for annotation");
        }
        return annotation.annotationType().isAnnotationPresent(ValidatorClass.class);
    }

}
