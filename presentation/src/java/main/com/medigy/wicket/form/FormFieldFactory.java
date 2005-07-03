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

import com.medigy.presentation.model.ChoicesFactory;
import com.medigy.service.validator.ValidEntity;
import wicket.markup.html.form.DropDownChoice;
import wicket.markup.html.form.FormComponent;
import wicket.markup.html.form.RadioChoice;
import wicket.markup.html.form.model.IChoiceList;

import java.util.*;

public class FormFieldFactory
{
    private static final FormFieldFactory INSTANCE = new FormFieldFactory();

    public static FormFieldFactory getInstance()
    {
        return INSTANCE;
    }

    private Map<Class, FieldCreator> fieldCreatorMap = Collections.synchronizedMap(new HashMap<Class, FieldCreator>());
    private Map<String, ReflectedFormFieldDefn> reflectedFormFieldDefnsMap = Collections.synchronizedMap(new TreeMap<String, ReflectedFormFieldDefn>());

    protected FormFieldFactory()
    {
        addFieldCreator(ValidEntity.class, new ReferenceEntityFieldCreator());
        addFieldCreator(String.class, new TextField.TextFieldCreator());
        addFieldCreator(Date.class, new DateField.DateFieldCreator());
        addFieldCreator(SocialSecurityField.class, new SocialSecurityField.SocialSecurityFieldCreator());
        addFieldCreator(PhoneField.class, new PhoneField.PhoneFieldCreator());
    }

    public void addFieldCreator(final Class dataType, final FieldCreator creator)
    {
        fieldCreatorMap.put(dataType, creator);
    }

    public FieldCreator getFieldCreator(final Class dataType)
    {
        return fieldCreatorMap.get(dataType);
    }

    public ReflectedFormFieldDefn getReflectedFormFieldDefn(final String propertyName, final Class ... cls)
    {
        final String key = cls[0].getName() + "." + propertyName;
        ReflectedFormFieldDefn result = reflectedFormFieldDefnsMap.get(key);
        if(result == null)
        {
            result = new ReflectedFormFieldDefn(propertyName, cls);
            reflectedFormFieldDefnsMap.put(key, result);
        }

        return result;
    }

    public FormComponent createField(final String componentId, final String propertyName, final Class ... classes)
    {
        // TODO: classes beyond the first are not managed yet
        return getReflectedFormFieldDefn(propertyName, classes).createField(componentId);
    }

    public interface FieldCreator
    {
        public FormComponent createField(final String controlId, final ReflectedFormFieldDefn reflectedFormFieldDefn);
    }

    public class ReferenceEntityFieldCreator implements FieldCreator
    {
        public FormComponent createField(final String controlId, final ReflectedFormFieldDefn reflectedFormFieldDefn)
        {
            final ValidEntity reAnn = (ValidEntity) reflectedFormFieldDefn.getAnnotation(ValidEntity.class);
            if(reAnn == null)
                throw new RuntimeException("Unable to find a reference entity annotation on " + reflectedFormFieldDefn);

            final SelectFieldStyle sfsAnn = (SelectFieldStyle) reflectedFormFieldDefn.getAnnotation(SelectFieldStyle.class);
            final SelectFieldStyle.Style style = sfsAnn != null ? sfsAnn.style() : SelectFieldStyle.Style.COMBO;
            final IChoiceList referenceEntityChoices = ChoicesFactory.getInstance().getEntityChoices(reAnn.entity());

            switch(style)
            {
                case COMBO:
                    return new DropDownChoice(controlId, referenceEntityChoices);
                case RADIO:
                    return new RadioChoice(controlId, referenceEntityChoices);

                default:
                    throw new RuntimeException("No other styles supported yet...make this error message better.");
            }
        }
    }

}
