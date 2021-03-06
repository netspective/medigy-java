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
package com.medigy.wicket;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.hibernate.validator.NotNullValidator;

import com.medigy.wicket.form.FieldLabel;
import wicket.Component;
import wicket.markup.html.form.FormComponent;
import wicket.markup.html.form.validation.EmailAddressPatternValidator;
import wicket.markup.html.form.validation.PatternValidator;
import wicket.markup.html.form.validation.TypeValidator;
import wicket.resource.IStringResourceLoader;
import wicket.util.lang.Classes;
import wicket.util.string.Strings;

/**
 * Provides string resources for various validators if none are found in *.properties files. Add this resource loader
 * using getApplicationSettings().addStringResourceLoader(new DefaultValidationMessagesResourceLoader()) in your
 * application subclass. Basically, we map all the validators we want default messages for in the constructor and add
 * others as required.
 *
 * We also associate labels with our input fields by using a naming convention. If the field is
 * name fieldXXX then the lable would be name fieldXXX_label. This allows lookup of the label component easily.
 *
 * This class assumes the use of a custom FieldLabel component that knows how to track the label's english text from
 * the markup (HTML) source. This way, if a field label is not provided dynamically it will default to what's in the
 * markup.
 */
public class DefaultValidationMessagesResourceLoader implements IStringResourceLoader
{
    public interface MessageProvider
    {
        public String getValidationErrorMessage(final FormComponent component, final String key);
    }
    
    private final Map<String, MessageProvider> providers = new HashMap<String, MessageProvider>();

    public DefaultValidationMessagesResourceLoader()
    {
        providers.put(Classes.name(NotNullValidator.class), new NotNullValidatorMessageProvider());
        providers.put(Classes.name(TypeValidator.class), new TypeValidatorMessageProvider());
        providers.put(Classes.name(PatternValidator.class), new PatternValidatorMessageProvider());
        providers.put(Classes.name(EmailAddressPatternValidator.class), new EmailAddressPatternValidatorMessageProvider());
    }

    public String loadStringResource(final Component component, final String key, final Locale locale, final String style)
    {
        // a form validation message key looks like FormName.FieldId.ValidatorClassName
        if(component instanceof FormComponent)
        {
            final String validatorName = Strings.lastPathComponent(key, '.');
            if(validatorName != null)
            {
                final MessageProvider provider = providers.get(validatorName);
                if(provider != null)
                    return provider.getValidationErrorMessage((FormComponent) component, key);
            }
        }

        return null;
    }

    public abstract class AbstractMessageProvider implements MessageProvider
    {
        public String getAssociatedLabel(final FormComponent component)
        {
            final Component label = (Component) component.getForm().get(component.getId() + FieldLabel.COMMON_SUFFIX);
            return label instanceof FieldLabel ? ((FieldLabel) label).getLabelTextFromMarkup() : component.getId();
        }
    }

    public class NotNullValidatorMessageProvider extends AbstractMessageProvider
    {
        public String getValidationErrorMessage(final FormComponent component, final String key)
        {
            return getAssociatedLabel(component) + " is required.";
        }
    }

    public class TypeValidatorMessageProvider extends AbstractMessageProvider
    {
        public String getValidationErrorMessage(final FormComponent component, final String key)
        {
            return getAssociatedLabel(component) + " '${input}' is not valid. Please use the format ${format}.";
        }
    }

    public class PatternValidatorMessageProvider extends AbstractMessageProvider
    {
        public String getValidationErrorMessage(final FormComponent component, final String key)
        {
            return getAssociatedLabel(component) + " '${input}' is not valid. Please use the format ${format}.";
        }
    }

    public class EmailAddressPatternValidatorMessageProvider extends AbstractMessageProvider
    {
        public String getValidationErrorMessage(final FormComponent component, final String key)
        {
            return getAssociatedLabel(component) + " '${input}' is not valid. Please use a valid email address.";
        }
    }
}
