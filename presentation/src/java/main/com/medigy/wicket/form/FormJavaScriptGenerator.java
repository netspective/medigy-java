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

import wicket.markup.html.form.Form;
import wicket.markup.html.form.FormComponent;
import wicket.markup.html.form.validation.RequiredValidator;

public class FormJavaScriptGenerator
{
    /**
     * Any form component that implements this method will have a DialogField javascript instance created and
     * automatically registered in the client's Dialog object.
     */
    public interface FieldTypeNameContributor
    {
        public String getJavaScriptFieldTypeId(FormJavaScriptGenerator generator);
    }

    /**
     * Any form component that wants to provide its own FieldType definition in JavaScript should implement this
     * interface
     */
    public interface FieldTypeScriptContributor extends FieldTypeNameContributor
    {
        /**
         * Using the provide StringBuilder param, contribute any valid JavaScript but usually of the following pattern:
         * <pre>
         *     function x_onFinalizeDefn(field) { }
         *     function x_onValidate(field, control) { }
         *     function x_onChange(field, control) { }
         *     function x_onFocus(field, control) { }
         *     function x_onBlur(field, control) { }
         *     function x_onKeyPress(field, control, event) { }
         *     function x_onClick(field, control) { }
         *
         *     addFieldType("x", x_onFinalizeDefn, x_onValidate, x_onChange, x_onFocus, x_onBlur, x_onKeyPress, x_onClick);
         * </pre>
         * If you do not have a specific method that must be provided you can pass in null to addFieldType().
         * @param generator The generator that is generating the rest of the Dialog javascript code.
         * @param component
         */
        public void appendJavaScriptFieldTypeDefn(final FormJavaScriptGenerator generator, FormComponent component, final StringBuilder typeDefScript);
    }

    /**
     * Any form component that wants to provide its own javascript field registration (instead of the default
     * field registration) shoudld implement this interface.
     */
    public interface FieldCustomizationScriptContributor
    {
        /**
         * Using the generator.getScript() method, contribute any valid JavaScript.
         * @param generator The generator that is generating the rest of the Dialog javascript code.
         * @param component
         */
        public void appendJavaScriptFieldCustomization(final FormJavaScriptGenerator generator, final FormComponent component);
    }

    /**
     * Any form component that wants to let the generator provide the field registration but wants to set some
     * additional fields in the created DialogField instance should implement this interface.
     */
    public interface FieldRegistrationContributor extends FieldTypeNameContributor
    {
        /**
         * Using the generator.getScript() method, contribute any valid JavaScript like the following:
         * <pre>
         *     generator.getScript().append(fieldVarName + ".required = false;\n");
         * </pre>
         * @param generator The generator that is generating the rest of the Dialog javascript code.
         * @param component
         */
        public void appendJavaScriptFieldRegistration(final FormJavaScriptGenerator generator, final FormComponent component, final String fieldVarName);
    }

    private final StringBuilder typeDefnScript = new StringBuilder();
    private final StringBuilder registrationScript = new StringBuilder();
    private final Form form;
    private final String jsDialogVarName;
    private final String jsFormObjectVarName;
    private boolean allowClientValidation = true;
    private boolean showDataChangedMessageOnLeave = true;

    public FormJavaScriptGenerator(final Form form, final String jsDialogVarName, final String jsFormObjectVarName)
    {
        this.form = form;
        this.jsDialogVarName = jsDialogVarName;
        this.jsFormObjectVarName = jsFormObjectVarName;
    }

    public FormJavaScriptGenerator(final Form form)
    {
        this(form, "dialog", "document.forms[0]");
    }

    public Form getForm()
    {
        return form;
    }

    public String getJsDialogVarName()
    {
        return jsDialogVarName;
    }

    public String getJsFormObjectVarName()
    {
        return jsFormObjectVarName;
    }

    public boolean isAllowClientValidation()
    {
        return allowClientValidation;
    }

    public boolean isShowDataChangedMessageOnLeave()
    {
        return showDataChangedMessageOnLeave;
    }

    public StringBuilder getTypeDefnScript()
    {
        return typeDefnScript;
    }

    public StringBuilder getRegistrationScript()
    {
        return registrationScript;
    }

    public void setAllowClientValidation(boolean allowClientValidation)
    {
        this.allowClientValidation = allowClientValidation;
    }

    public void setShowDataChangedMessageOnLeave(boolean showDataChangedMessageOnLeave)
    {
        this.showDataChangedMessageOnLeave = showDataChangedMessageOnLeave;
    }

    public void appendDefaultFieldRegistrationScript(final FormComponent component,
                                                     final FieldRegistrationContributor frc,
                                                     final FieldTypeScriptContributor ftsc,
                                                     final String fieldTypeName)
    {
        final String fieldVarName = component.getId();

        registrationScript.append("var ");
        registrationScript.append(fieldVarName);
        registrationScript.append(" = ");
        registrationScript.append("dialog.createField(\"");
        registrationScript.append(component.getPath());
        registrationScript.append("\", FIELD_TYPES[\"");
        registrationScript.append(fieldTypeName);
        registrationScript.append("\"]);\n");

        if(component.getValidators().contains(RequiredValidator.getInstance()))
        {
            registrationScript.append(fieldVarName);
            registrationScript.append(".setRequired(true);\n");
        }

        if(! component.isVisible())
        {
            registrationScript.append(fieldVarName);
            registrationScript.append(".setVisible(false);\n");
        }

        if(frc != null)
            frc.appendJavaScriptFieldRegistration(this, component, fieldVarName);

        if(ftsc != null)
            ftsc.appendJavaScriptFieldTypeDefn(this, component, typeDefnScript);
    }

    public void appendDialogRegistrationStart()
    {
        registrationScript.append("\n<script>\nvar ");
        registrationScript.append(jsDialogVarName);
        registrationScript.append(" = new Dialog(");
        registrationScript.append(jsFormObjectVarName);
        registrationScript.append(", ");
        registrationScript.append(allowClientValidation);
        registrationScript.append(", ");
        registrationScript.append(showDataChangedMessageOnLeave);
        registrationScript.append(");\n");
    }

    public void appendDialogRegistrationEnd()
    {
        registrationScript.append(jsDialogVarName);
        registrationScript.append(".finalizeContents();\n");
        registrationScript.append("</script>\n");
    }
}
