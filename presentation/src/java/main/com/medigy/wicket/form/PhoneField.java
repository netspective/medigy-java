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
 */

/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.wicket.form;

import wicket.markup.ComponentTag;
import wicket.markup.html.form.validation.RequiredValidator;
import wicket.markup.html.form.validation.PatternValidator;

import java.util.regex.Pattern;

public class PhoneField extends wicket.markup.html.form.TextField implements JavaScriptProvider
{
    private static final Pattern DASH_VALIDATE_PATTERN = Pattern.compile("^([\\d][\\d][\\d])[\\.-]?([\\d][\\d][\\d])[\\.-]?([\\d]{4})([ ][x][\\d]{1,5})?$");
    private static final Pattern BRACKET_VALIDATE_PATTERN = Pattern.compile("^([\\d][\\d][\\d])[\\.-]?([\\d][\\d][\\d])[\\.-]?([\\d]{4})([ ][x][\\d]{1,5})?$");

    public static enum Style
    {
        DASH ,
        BRACKET ;
    }

    private String fieldName;
    private String fieldControlId;
    private long fieldFlags;
    private Style style;

    public PhoneField(final String fieldName, long fieldFlags)
    {
        super(fieldName + BaseForm.FIELD_CONTROL_SUFFIX);
        this.fieldName = fieldName;
        this.fieldFlags = fieldFlags;
        this.fieldControlId = fieldName + BaseForm.FIELD_CONTROL_SUFFIX;

        if((this.fieldFlags & FieldFlags.REQUIRED) != 0)
            add(RequiredValidator.getInstance());

        setStyle(Style.DASH);
    }

    public PhoneField(final String componentName)
    {
        this(componentName, FieldFlags.DEFAULT_FLAGS);
    }

    protected void onComponentTag(final ComponentTag componentTag)
    {
        ((BaseForm) getForm()).onFormComponentTag(componentTag, getFieldId(), fieldFlags);
        super.onComponentTag(componentTag);
    }

    protected String getFieldId()
    {
        return this.fieldControlId;
    }

    public String getJavaScript(final String dialogVarName, final String formObjectName)
    {
        return "var field = dialog.createField("+ formObjectName +"[\""+ getFieldId() +"\"], FIELD_TYPES[\""+ getClass().getName() +"\"], "+ fieldFlags +", null);\n";
    }

    public void setStyle(PhoneField.Style style)
    {
        this.style = style;
        switch(this.style)
        {
            case DASH:
                add(new PatternValidator(DASH_VALIDATE_PATTERN));
                break;

            case BRACKET:
                add(new PatternValidator(BRACKET_VALIDATE_PATTERN));
                break;
        }
    }
}
