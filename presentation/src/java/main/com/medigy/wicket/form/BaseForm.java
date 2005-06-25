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

import com.medigy.presentation.model.ChoicesFactory;
import wicket.IFeedback;
import wicket.model.PropertyModel;
import wicket.model.IModel;
import wicket.markup.ComponentTag;
import wicket.markup.MarkupStream;
import wicket.markup.html.form.*;
import wicket.markup.html.form.model.IChoice;
import wicket.util.value.ValueMap;

public class BaseForm extends Form
{
    public static final String FIELD_LABEL_SUFFIX = "_label";
    public static final String FIELD_CONTROL_SUFFIX = "_control";
    protected static final Collection TEST_CHOICES = new ArrayList();

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
    }

    // TODO - This is used for test purposes only. Replace with more formal model.
    // TODO - Check to see if we need add* methods for all Wicket model types.
    private final ValueMap properties = new ValueMap();

    public BaseForm(final String componentName)
    {
        super(componentName);
    }

    public BaseForm(final String componentName, final IFeedback feedback)
    {
        super(componentName, feedback);
    }

    public BaseForm(String componentName, IModel model, IFeedback feedback)
    {
        super(componentName, model, feedback);
    }

    protected void addLabeledTextField(final String fieldName, int fieldFlags)
    {
        add(new FieldLabel(fieldName));
        add(new TextField(fieldName, fieldFlags));
    }

    protected void addLabeledTextField(final String fieldName)
    {
        add(new FieldLabel(fieldName));
        add(new TextField(fieldName));
    }

    protected void addLabeledDateField(final String fieldName)
    {
        add(new FieldLabel(fieldName));
        add(new TextField(fieldName));
    }

    protected void addLabeledPhoneField(final String fieldName)
    {
        add(new FieldLabel(fieldName));
        add(new PhoneField(fieldName));
    }

    protected void addLabeledBooleanField(final String fieldName)
    {
        add(new FieldLabel(fieldName));
        add(new BooleanField(fieldName));
    }

    protected void addLabeledCurrencyField(final String fieldName)
    {
        add(new FieldLabel(fieldName));
        add(new CurrencyField(fieldName));
    }

    protected void addLabeledSelectField(final String fieldName)
    {
        add(new FieldLabel(fieldName));
        add(new DropDownChoice(fieldName, TEST_CHOICES));
    }

    protected void addLabeledSelectField(final String fieldName, final Class referenceEntity)
    {
        add(new FieldLabel(fieldName));
        add(new DropDownChoice(fieldName, ChoicesFactory.getInstance().getReferenceEntityChoices(referenceEntity)));
    }

    protected void addLabeledMultiListField(final String fieldName, final Class multiListChoices)
    {
        add(new FieldLabel(fieldName));
        add(new ListMultipleChoice(fieldName, ChoicesFactory.getInstance().getReferenceEntityChoices(multiListChoices)));
    }

    protected void addLabeledMultiCheckField(final String fieldName, final Class multiCheckChoices)
    {
        add(new FieldLabel(fieldName));
        add(new ListMultipleChoice(fieldName, ChoicesFactory.getInstance().getReferenceEntityChoices(multiCheckChoices)));
    }

    protected void addLabeledRadioChoiceField(final String fieldName)
    {
        add(new FieldLabel(fieldName));
        add(new RadioChoice(fieldName, TEST_CHOICES));
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
                if(formComponent instanceof JavaScriptProvider)
                {
                    JavaScriptProvider jsProvider = (JavaScriptProvider) formComponent;
                    script.append(jsProvider.getJavaScript("dialog", "document.forms[0]"));
                }
            }
        });
        script.append("dialog.finalizeContents();\n</script>");

        super.onComponentTagBody(markupStream, componentTag);
        getResponse().write(script.toString());
    }
}
