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

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.medigy.presentation.model.ChoicesFactory;
import com.medigy.service.validator.ValidEntity;
import wicket.markup.html.form.DropDownChoice;
import wicket.markup.html.form.FormComponent;
import wicket.markup.html.form.ListMultipleChoice;
import wicket.markup.html.form.RadioChoice;
import wicket.markup.html.form.TextArea;
import wicket.markup.html.form.TextField;
import wicket.markup.html.form.model.IChoiceList;
import wicket.markup.html.form.validation.EmailAddressPatternValidator;
import wicket.markup.html.form.validation.TypeValidator;

public class FormFieldFactory
{
    private static final Log log = LogFactory.getLog(FormFieldFactory.class);

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
        addFieldCreator(String.class, new TextFieldCreator());
        addFieldCreator(Float.class, new FloatFieldCreator());
        addFieldCreator(Date.class, new DateFieldCreator());
        addFieldCreator(SocialSecurityFieldCreator.class, new SocialSecurityFieldCreator());
        addFieldCreator(PhoneFieldCreator.class, new PhoneFieldCreator());
        addFieldCreator(ZipCodeFieldCreator.class, new ZipCodeFieldCreator());
        addFieldCreator(MemoFieldCreator.class, new MemoFieldCreator());
        addFieldCreator(EmailFieldCreator.class, new EmailFieldCreator());
    }

    public void addFieldCreator(final Class dataType, final FieldCreator creator)
    {
        fieldCreatorMap.put(dataType, creator);
    }

    public FieldCreator getFieldCreator(Class dataType)
    {
        if(dataType.isAssignableFrom(Serializable.class))  // TODO: some of the getter return types in RegisterPatientParameters are Serializable - need to figure out how to handle this
        {
            log.warn("Attempt to create field with return type of 'Serializable'");
            dataType = String.class;
        }

        FieldCreator creator = fieldCreatorMap.get(dataType);
        if(creator == null)
        {
            log.error("FieldCreator for dataType '" + dataType + "' not found in fieldCreatorMap");
            throw new RuntimeException("FieldCreator for dataType '" + dataType + "' not found in fieldCreatorMap");
        }

        return creator;
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

    public ConstructedField createField(final String componentId, final String propertyName, final Class ... classes)
    {
        final ReflectedFormFieldDefn rffd = getReflectedFormFieldDefn(propertyName, classes);
        final FormComponent fc = rffd.createField(componentId);

        return new ConstructedField()
        {
            public FormComponent getField()
            {
                return fc;
            }

            public ReflectedFormFieldDefn getReflectedFormFieldDefn()
            {
                return rffd;
            }
        };
    }

    public interface ConstructedField
    {
        public FormComponent getField();
        public ReflectedFormFieldDefn getReflectedFormFieldDefn();
    }

    public interface FieldCreator
    {
        public FormComponent createField(final String controlId, final ReflectedFormFieldDefn reflectedFormFieldDefn);
    }

    public class BooleanFieldCreator implements FieldCreator, FormJavaScriptGenerator.FieldTypeNameContributor
    {
        public FormComponent createField(final String controlId, final ReflectedFormFieldDefn reflectedFormFieldDefn)
        {
            final TextField result = new TextField(controlId);
            reflectedFormFieldDefn.initializeField(reflectedFormFieldDefn, result);
            result.add(new TypeValidator(Boolean.class));
            return result;
        }

        public String getJavaScriptFieldTypeId(FormJavaScriptGenerator generator)
        {
            return "boolean";
        }
    }

    public class DateFieldCreator implements FieldCreator, FormJavaScriptGenerator.FieldTypeNameContributor
    {
        public FormComponent createField(final String controlId, final ReflectedFormFieldDefn reflectedFormFieldDefn)
        {
            final TextField result = new TextField(controlId);
            reflectedFormFieldDefn.initializeField(reflectedFormFieldDefn, result);
            result.add(new TypeValidator(Date.class));
            return result;
        }

        public String getJavaScriptFieldTypeId(FormJavaScriptGenerator generator)
        {
            return "date-time";
        }
    }

    public class EmailFieldCreator implements FieldCreator, FormJavaScriptGenerator.FieldTypeNameContributor
    {
        public FormComponent createField(final String controlId, final ReflectedFormFieldDefn reflectedFormFieldDefn)
        {
            final TextField result = new TextField(controlId);
            reflectedFormFieldDefn.initializeField(reflectedFormFieldDefn, result);
            result.add(new EmailAddressPatternValidator());
            return result;
        }

        public String getJavaScriptFieldTypeId(FormJavaScriptGenerator generator)
        {
            return "text";
        }
    }

    public class FloatFieldCreator implements FieldCreator, FormJavaScriptGenerator.FieldTypeNameContributor
    {
        public FormComponent createField(final String controlId, final ReflectedFormFieldDefn reflectedFormFieldDefn)
        {
            final TextField result = new TextField(controlId);
            reflectedFormFieldDefn.initializeField(reflectedFormFieldDefn, result);
            result.add(new TypeValidator(Float.class));
            return result;
        }

        public String getJavaScriptFieldTypeId(FormJavaScriptGenerator generator)
        {
            return "float";
        }
    }

    public class IntegerFieldCreator implements FieldCreator, FormJavaScriptGenerator.FieldTypeNameContributor
    {
        public FormComponent createField(final String controlId, final ReflectedFormFieldDefn reflectedFormFieldDefn)
        {
            final TextField result = new TextField(controlId);
            reflectedFormFieldDefn.initializeField(reflectedFormFieldDefn, result);
            result.add(new TypeValidator(Integer.class));
            return result;
        }

        public String getJavaScriptFieldTypeId(FormJavaScriptGenerator generator)
        {
            return "integer";
        }
    }

    public class MemoFieldCreator implements FieldCreator, FormJavaScriptGenerator.FieldTypeNameContributor
    {
        public FormComponent createField(final String controlId, final ReflectedFormFieldDefn reflectedFormFieldDefn)
        {
            final TextArea result = new TextArea(controlId);
            reflectedFormFieldDefn.initializeField(reflectedFormFieldDefn, result);
            return result;
        }

        public String getJavaScriptFieldTypeId(FormJavaScriptGenerator generator)
        {
            return "memo";
        }
    }

    public class PasswordFieldCreator implements FieldCreator, FormJavaScriptGenerator.FieldTypeNameContributor
    {
        public FormComponent createField(final String controlId, final ReflectedFormFieldDefn reflectedFormFieldDefn)
        {
            final TextField result = new TextField(controlId);
            reflectedFormFieldDefn.initializeField(reflectedFormFieldDefn, result);
            // TODO: need to add wicket validator class
            return result;
        }

        public String getJavaScriptFieldTypeId(FormJavaScriptGenerator generator)
        {
            return "text";
        }
    }

    public class PhoneFieldCreator implements FieldCreator, FormJavaScriptGenerator.FieldTypeNameContributor
    {
        private final Pattern DASH_VALIDATE_PATTERN = Pattern.compile("^([\\d][\\d][\\d])[\\.-]?([\\d][\\d][\\d])[\\.-]?([\\d]{4})([ ][x][\\d]{1,5})?$");

        public FormComponent createField(final String controlId, final ReflectedFormFieldDefn reflectedFormFieldDefn)
        {
            final TextField result = new TextField(controlId);
            reflectedFormFieldDefn.initializeField(reflectedFormFieldDefn, result);
            result.add(new PatternValidator(DASH_VALIDATE_PATTERN, "999-999-9999 x999"));
            return result;
        }

        public String getJavaScriptFieldTypeId(FormJavaScriptGenerator generator)
        {
            return "phone";
        }
    }

    public class SocialSecurityFieldCreator implements FieldCreator, FormJavaScriptGenerator.FieldTypeNameContributor
    {
        private final Pattern SOCIAL_SECURITY_PATTERN = Pattern.compile("^([\\d]{3})[-]?([\\d]{2})[-]?([\\d]{4})$");

        public FormComponent createField(final String controlId, final ReflectedFormFieldDefn reflectedFormFieldDefn)
        {
            final TextField result = new TextField(controlId);
            reflectedFormFieldDefn.initializeField(reflectedFormFieldDefn, result);
            result.add(new PatternValidator(SOCIAL_SECURITY_PATTERN, "999-99-9999"));
            return result;
        }

        public String getJavaScriptFieldTypeId(FormJavaScriptGenerator generator)
        {
            return "ssn";
        }
    }

    public class TextFieldCreator implements FieldCreator, FormJavaScriptGenerator.FieldTypeNameContributor
    {
        public FormComponent createField(final String controlId, final ReflectedFormFieldDefn reflectedFormFieldDefn)
        {
            final TextField result = new TextField(controlId);
            reflectedFormFieldDefn.initializeField(reflectedFormFieldDefn, result);
            return result;
        }

        public String getJavaScriptFieldTypeId(FormJavaScriptGenerator generator)
        {
            return "text";
        }
    }

    public class ZipCodeFieldCreator implements FieldCreator, FormJavaScriptGenerator.FieldTypeNameContributor
    {
        public static final String VALIDATE_PATTERN = "^([\\d]{5})([-][\\d]{4})?$";

        public FormComponent createField(final String controlId, final ReflectedFormFieldDefn reflectedFormFieldDefn)
        {
            final TextField result = new TextField(controlId);
            reflectedFormFieldDefn.initializeField(reflectedFormFieldDefn, result);
            result.add(new PatternValidator(VALIDATE_PATTERN, "99999-9999"));
            return result;
        }

        public String getJavaScriptFieldTypeId(FormJavaScriptGenerator generator)
        {
            return "text"; // TODO: need to supply special client side validation for zip fields, too
        }
    }

    protected class ReferenceEntityFieldCreator implements FieldCreator, FormJavaScriptGenerator.FieldTypeNameContributor,
                                                                         FormJavaScriptGenerator.FieldRegistrationContributor
    {
        public FormComponent createField(final String controlId, final ReflectedFormFieldDefn reflectedFormFieldDefn)
        {
            final ValidEntity reAnn = (ValidEntity) reflectedFormFieldDefn.getAnnotation(ValidEntity.class);
            if(reAnn == null)
            {
                log.error("Unable to find a reference entity annotation on " + reflectedFormFieldDefn);
                throw new RuntimeException("Unable to find a reference entity annotation on " + reflectedFormFieldDefn);
            }

            final SelectFieldStyle sfsAnn = (SelectFieldStyle) reflectedFormFieldDefn.getAnnotation(SelectFieldStyle.class);
            final SelectFieldStyle.Style style = sfsAnn != null ? sfsAnn.style() : SelectFieldStyle.Style.COMBO;
            final IChoiceList referenceEntityChoices = ChoicesFactory.getInstance().getEntityChoices(reAnn.entity());

            switch(style)
            {
                case COMBO:
                    return new DropDownChoice(controlId, referenceEntityChoices);
                case RADIO:
                    return new RadioChoice(controlId, referenceEntityChoices);

                case MULTICHECK:
                    return new MultiCheckChoice(controlId, referenceEntityChoices);

                case MULTIDUAL:  // TODO: implement this but it defaults to MULTILIST
                case MULTILIST:
                    return new ListMultipleChoice(controlId, referenceEntityChoices);

                default:
                {
                    log.error("Style with enumeration code '" + style + "' not supported yet.");
                    throw new RuntimeException("Style with enumeration code '" + style + "' not supported yet.");
                }
            }
        }

        public String getJavaScriptFieldTypeId(FormJavaScriptGenerator generator)
        {
            return "select";
        }

        public void appendJavaScriptFieldRegistration(final FormJavaScriptGenerator generator, final FormComponent component, final String fieldVarName)
        {
            final ReflectedFormFieldDefn reflectedFormFieldDefn = ((BaseForm) component.getForm()).getReflectedFields().get(component);
            if(reflectedFormFieldDefn != null)
            {
                final SelectFieldStyle sfsAnn = (SelectFieldStyle) reflectedFormFieldDefn.getAnnotation(SelectFieldStyle.class);
                final SelectFieldStyle.Style style = sfsAnn != null ? sfsAnn.style() : SelectFieldStyle.Style.COMBO;

                generator.getRegistrationScript().append(fieldVarName + ".style = SELECTSTYLE_" + style.name() + "\n");
            }
        }
    }

}
