package com.medigy.app.pbs.component.navigate;

import wicket.Component;
import wicket.MarkupContainer;
import wicket.markup.html.link.Link;
import wicket.markup.html.panel.Panel;

public class MainMenu extends Panel
{
    public MainMenu(final String id)
    {
        super(id);
    }

    public MarkupContainer add(final Component component)
    {
        // we don't want any of our links to be auto enabled because we use CSS and wicket uses HTML before/after for disabling buttons
        if(component instanceof Link)
            ((Link) component).setAutoEnable(false);

        return super.add(component);
    }

}
