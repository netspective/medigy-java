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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.medigy.wicket.form.FormFieldFactory.ConstructedField;
import com.medigy.wicket.form.FormFieldFactory.FieldCreator;
import com.medigy.wicket.form.FormJavaScriptGenerator.FieldCustomizationScriptContributor;
import com.medigy.wicket.form.FormJavaScriptGenerator.FieldTypeNameContributor;
import wicket.IComponentResolver;
import wicket.IFeedback;
import wicket.MarkupContainer;
import wicket.markup.ComponentTag;
import wicket.markup.MarkupStream;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.FormComponent;
import wicket.model.BoundCompoundPropertyModel;
import wicket.model.IModel;

public class BaseForm extends Form implements IComponentResolver
{
    private static final Log log = LogFactory.getLog(BaseForm.class);

    protected static final Collection TEST_CHOICES = new ArrayList();
    public static final String ATTRNAME_WICKET_ID = "wicket:id";

    /**
     * Ascertain whether or not client side validation scripts and event handlers should be attached.
     * If this variable is set to false, no client side validation scripts will be generated.
     */
    private boolean enableClientSideValidation = true;

    /**
     * The map which tracks a tag name (the key) like "input", "legend", or "select" and an associated TagResolver
     * implementation that will automatically create components based on that tag. The purpose of the auto resolvers is
     * to allow something like <input wicket:id="myFormField"> to be specified in HTML and a wicket form field to be
     * automatically created instead of having to be manually added within the form.
     */
    private Map<String, IComponentResolver> autoResolvers = new HashMap<String, IComponentResolver>();

    /**
     * The map with tracks a component and the associated reflected field information that was constructed by
     * the form field factory. Although wicket manages the components we want to keep track of auto-generated fields
     * by knowing their original types and such.
     */
    private Map<FormComponent, ReflectedFormFieldDefn> reflectedFields = new HashMap<FormComponent, ReflectedFormFieldDefn>();

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

        assert(model instanceof BoundCompoundPropertyModel); // we only handle this kind of model right now
        assert(getModel().getObject(null) != null);  // make sure a service bean is attached

        autoResolvers.put("legend", new IComponentResolver()
        {
            public boolean resolve(final MarkupContainer container, final MarkupStream markupStream, final ComponentTag tag)
            {
                container.autoAdd(new FieldGroupLabel(tag.getAttributes().getString(ATTRNAME_WICKET_ID)));
                return true;
            }
        });

        autoResolvers.put("label", new IComponentResolver()
        {
            public boolean resolve(final MarkupContainer container, final MarkupStream markupStream, final ComponentTag tag)
            {
                final String labelId = tag.getAttributes().getString(ATTRNAME_WICKET_ID);
                container.autoAdd(new FieldLabel(labelId));
                return true;
            }
        });

        final IComponentResolver controlResolver = new IComponentResolver()
        {
            public boolean resolve(final MarkupContainer container, final MarkupStream markupStream, final ComponentTag tag)
            {
                final String controlId = tag.getAttributes().getString(ATTRNAME_WICKET_ID);
                final Class serviceBeanClass = getModel().getObject(null).getClass();
                final ConstructedField newField = FormFieldFactory.getInstance().createField(controlId, controlId, serviceBeanClass, getModel().getClass());
                container.autoAdd(((BoundCompoundPropertyModel) getModel()).bind(newField.getField(), controlId));
                reflectedFields.put(newField.getField(), newField.getReflectedFormFieldDefn());
                return true;
            }
        };

        autoResolvers.put("input", controlResolver);
        autoResolvers.put("select", controlResolver);
        autoResolvers.put("span", controlResolver);
        autoResolvers.put("textarea", controlResolver);
    }

    public boolean isEnableClientSideValidation()
    {
        return enableClientSideValidation;
    }

    public void setEnableClientSideValidation(boolean enableClientSideValidation)
    {
        this.enableClientSideValidation = enableClientSideValidation;
    }

    public boolean resolve(final MarkupContainer container, final MarkupStream markupStream,
                           final ComponentTag tag)
    {
        if(tag.getAttributes().containsKey(ATTRNAME_WICKET_ID))
        {
            final IComponentResolver resolver = autoResolvers.get(tag.getName());
            if(resolver != null)
                return resolver.resolve(container, markupStream, tag);
        }
        return false;
    }

    protected void onComponentTag(ComponentTag componentTag)
    {
        super.onComponentTag(componentTag);
        if(isEnableClientSideValidation())
            componentTag.put("onsubmit", "return(document.forms[0].dialog.isValid())");
    }

    protected void onComponentTagBody(final MarkupStream markupStream, final ComponentTag componentTag)
    {
        super.onComponentTagBody(markupStream, componentTag);

        if(isEnableClientSideValidation())
        {
            final FormJavaScriptGenerator generator = new FormJavaScriptGenerator(this);

            generator.appendDialogRegistrationStart();
            visitFormComponents(
                new FormComponent.IVisitor()
                {
                    /**
                     * For each form field we're going to check if it implements some special javascript generation
                     * interfaces. If the form field itself does not implement the interfaces check to see if the
                     * reflected form fields' original creator implements the methods in question.
                     * @param formComponent
                     */
                    public void formComponent(final FormComponent formComponent)
                    {
                        if(formComponent instanceof FieldTypeNameContributor)
                            generator.getRegistrationScript().append(generator.getDefaultFieldRegistrationScript(formComponent,
                                    ((FieldTypeNameContributor) formComponent).getJavaScriptFieldTypeId(generator)));
                        else if(formComponent instanceof FieldCustomizationScriptContributor)
                            ((FieldCustomizationScriptContributor) formComponent).appendJavaScriptFieldCustomization(generator);
                        else
                        {
                            // Since the formComponent itself doesn't imlement any of our special methods, see if the
                            // associated field creators do (it is very common for formComponent to not implement the
                            // special methods since they are typically wicket components that don't know about our
                            // javascript generation.

                            final ReflectedFormFieldDefn rffd = reflectedFields.get(formComponent);
                            if(rffd != null)
                            {
                                final FieldCreator creator = rffd.getCreator();

                                if(creator instanceof FieldTypeNameContributor)
                                    generator.getRegistrationScript().append(generator.getDefaultFieldRegistrationScript(formComponent,
                                            ((FieldTypeNameContributor) creator).getJavaScriptFieldTypeId(generator)));
                                else if(creator instanceof FieldCustomizationScriptContributor)
                                    ((FieldCustomizationScriptContributor) formComponent).appendJavaScriptFieldCustomization(generator);
                            }
                        }
                    }
                }
            );
            generator.appendDialogRegistrationEnd();

            getResponse().write(generator.getTypeDefnScript().toString());
            getResponse().write(generator.getRegistrationScript().toString());
        }
    }
}
