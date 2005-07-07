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

import java.util.Collection;

import wicket.markup.ComponentTag;
import wicket.markup.MarkupStream;
import wicket.markup.html.form.IOnChangeListener;
import wicket.markup.html.form.ListMultipleChoice;
import wicket.markup.html.form.model.IChoice;
import wicket.markup.html.form.model.IChoiceList;
import wicket.model.IModel;
import wicket.util.string.Strings;

/**
 * A multiple choice group of checkboxes component.
 *
 * @author Jonathan Locke
 * @author Johan Compagner
 */
public class MultiCheckChoice extends ListMultipleChoice
{
	/**
	 * @see wicket.markup.html.form.AbstractChoice#AbstractChoice(String)
	 */
	public MultiCheckChoice(final String id)
	{
		super(id);
	}

	/**
	 * @see wicket.markup.html.form.AbstractChoice#AbstractChoice(String, java.util.Collection)
	 */
	public MultiCheckChoice(final String id, final Collection choices)
	{
		super(id, choices);
	}

	/**
	 * @see wicket.markup.html.form.AbstractChoice#AbstractChoice(String, wicket.markup.html.form.model.IChoiceList)
	 */
	public MultiCheckChoice(final String id, final IChoiceList choices)
	{
		super(id, choices);
	}

	/**
	 * @see wicket.markup.html.form.AbstractChoice#AbstractChoice(String, wicket.model.IModel, Collection)
	 */
	public MultiCheckChoice(final String id, IModel object, final Collection choices)
	{
		super(id, object, choices);
	}

	/**
	 * @see wicket.markup.html.form.AbstractChoice#AbstractChoice(String, IModel, IChoiceList)
	 */
	public MultiCheckChoice(final String id, IModel object, final IChoiceList choices)
	{
		super(id, object, choices);
	}

    /**
     * @see wicket.markup.html.form.IOnChangeListener#onSelectionChanged()
     */
    public void onSelectionChanged()
    {
        updateModel();
        onSelectionChanged(getModelObject());
    }

    /**
     * Template method that can be overriden by clients that implement
     * IOnChangeListener to be notified by onChange events of a select element.
     * This method does nothing by default.
     * <p>
     * Called when a option is selected of a dropdown list that wants to be
     * notified of this event. This method is to be implemented by clients that
     * want to be notified of selection events.
     *
     * @param newSelection
     *			  The newly selected object of the backing model NOTE this is
     *			  the same as you would get by calling getModelObject() if the
     *			  new selection were current
     */
    protected void onSelectionChanged(Object newSelection)
    {
    }

    /**
     * Whether this component's onSelectionChanged event handler should called using
     * javascript if the selection changes. If true, a roundtrip will be generated with
     * each selection change, resulting in the model being updated (of just this component)
     * and onSelectionChanged being called. This method returns false by default.
     * @return True if this component's onSelectionChanged event handler should
     *			called using javascript if the selection changes
     */
    protected boolean wantOnSelectionChangedNotifications()
    {
        return false;
    }

    /**
     * @return Prefix to use before choice
     */
    protected String getPrefix()
    {
        return "";
    }

    /**
     * @return Separator to use between radio options
     */
    protected String getSuffix()
    {
        return "<br>\n";
    }

    /**
     * @see wicket.Component#onComponentTagBody(wicket.markup.MarkupStream, ComponentTag)
     */
    protected final void onComponentTagBody(final MarkupStream markupStream,
            final ComponentTag openTag)
    {
        // Buffer to hold generated body
        final StringBuffer buffer = new StringBuffer();

        // Iterate through choices
        final IChoiceList choices = getChoices();

        // Loop through choices
        for (int i = 0; i < choices.size(); i++)
        {
            // Get next choice
            final IChoice choice = choices.get(i);

            // Get label for choice
            final String label = choice.getDisplayValue();

            // If there is a display value for the choice, then we know that the
            // choice is automatic in some way. If label is /null/ then we know
            // that the choice is a manually created radio tag at some random
            // location in the page markup!
            if (label != null)
            {
                // Append option suffix
                buffer.append(getPrefix());

                // Add radio tag
                buffer.append("<input name=\"" + getPath() + "\"" + " type=\"checkbox\""
                        + (isSelected(choice) ? " checked" : "") + " value=\"" + choice.getId()
                        + "\"");

                // Should a roundtrip be made (have onSelectionChanged called) when the option is clicked?
                if (wantOnSelectionChangedNotifications())
                {
                    final String url = urlFor(IOnChangeListener.class);

                    // NOTE: do not encode the url as that would give invalid JavaScript
                    buffer.append(" onclick=\"location.href='" + url + "&" + getPath()
                            + "=" + choice.getId() + "';\"");
                }

                buffer.append(">");

                // Add label for radio button
                String display = getLocalizer().getString(getId() + "." + label, this, label);
                String escaped = Strings.escapeMarkup(display, false, true);
                buffer.append(escaped);

                // Append option suffix
                buffer.append(getSuffix());
            }
        }

        // Replace body
        replaceComponentTagBody(markupStream, openTag, buffer.toString());
    }
}