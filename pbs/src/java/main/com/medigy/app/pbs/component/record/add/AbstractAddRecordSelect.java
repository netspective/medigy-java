/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.app.pbs.component.record.add;

import wicket.markup.ComponentTag;
import wicket.markup.MarkupStream;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.panel.Panel;

public class AbstractAddRecordSelect extends Panel
{
    /**
     * this is usually set from within the HTML using <wicket:component ... menuLabel="x"> attribute
     */
    private String menuLabel;

    public AbstractAddRecordSelect(final String id)
    {
        super(id);
        add(new MenuLabelComponent("menuLabel"));
    }

    public String getMenuLabel()
    {
        return menuLabel;
    }

    public void setMenuLabel(String menuLabel)
    {
        this.menuLabel = menuLabel;
    }

    public class MenuLabelComponent extends WebMarkupContainer
    {
        public MenuLabelComponent(final String id)
        {
            super(id);
        }

        protected void onComponentTagBody(final MarkupStream markupStream, final ComponentTag componentTag)
        {
            final String menuLabel = getMenuLabel();
            if(menuLabel == null)
                renderComponentTagBody(markupStream, componentTag);
            else
                replaceComponentTagBody(markupStream, componentTag, getMenuLabel());
        }
    }
}
