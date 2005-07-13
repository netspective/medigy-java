/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.app.pbs.component.record.add;

import wicket.MarkupContainer;
import wicket.markup.ComponentTag;
import wicket.markup.MarkupStream;
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
        //TODO: add custom label support -- add(new MenuLabelComponent("menuLabel"));
    }

    protected void onComponentTagBody(final MarkupStream markupStream, final ComponentTag componentTag)
    {
        // TODO: the /medigy-pbs/app is hardcoded right now and should be fixed
        if(componentTag.getName().equalsIgnoreCase("select"))
            componentTag.getAttributes().put("onchange",
                    "if(select.selectedIndex > 0) window.location.href = '/medigy-pbs/app?bookmarkablePage=' + select.options[select.selectedIndex].value");
        super.onComponentTagBody(markupStream, componentTag);
    }

    public String getMenuLabel()
    {
        return menuLabel;
    }

    public void setMenuLabel(String menuLabel)
    {
        this.menuLabel = menuLabel;
    }

    public class MenuLabelComponent extends MarkupContainer
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
