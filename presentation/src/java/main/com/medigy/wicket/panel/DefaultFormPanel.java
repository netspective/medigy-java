/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.wicket.panel;

import com.medigy.wicket.border.StandardPanelBorder;
import com.medigy.wicket.form.FormMode;
import wicket.IFeedback;
import wicket.markup.html.form.Form;
import wicket.markup.html.panel.FeedbackPanel;

public abstract class DefaultFormPanel extends DefaultPanel
{
    public DefaultFormPanel(final String componentName, final FormMode mode)
	{
        super(componentName);

        final StandardPanelBorder border = new StandardPanelBorder("panel_border");
        add(border);

        // Create feedback panel and add to page
        final FeedbackPanel feedback = new FeedbackPanel("feedback");
        border.add(feedback);

        border.add(createForm("form", feedback, mode));
    }
    
    protected abstract Form createForm(final String componentName, final IFeedback feedback, final FormMode mode);
}
