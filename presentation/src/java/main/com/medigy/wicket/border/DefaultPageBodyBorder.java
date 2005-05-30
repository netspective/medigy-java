/*
 * $Id: DefaultPageBodyBorder.java,v 1.4 2005-05-30 18:05:45 shahid.shah Exp $
 * $Revision: 1.4 $
 * $Date: 2005-05-30 18:05:45 $
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.medigy.wicket.border;

import com.medigy.wicket.DefaultApplication;
import com.medigy.wicket.menu.Menu;
import com.medigy.wicket.menu.MenuItem;
import wicket.AttributeModifier;
import wicket.Component;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.basic.Label;
import wicket.markup.html.border.Border;
import wicket.markup.html.link.BookmarkablePageLink;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.ListView;
import wicket.model.Model;

public class DefaultPageBodyBorder extends Border
{
    private WebMarkupContainer mainMenuElem;
    private WebMarkupContainer navigatorPanelCell;
    private WebMarkupContainer navigatorPanelSeparatorCell;
    private WebMarkupContainer calloutPanelCell;
    private WebMarkupContainer calloutPanelBottomCell;
    private WebMarkupContainer footerBarCell;

    public DefaultPageBodyBorder(final String componentName)
    {
        super(componentName);
        add(mainMenuElem = new MainMenu("main-menu", ((DefaultApplication) getApplication()).getMainMenu()));

        add(navigatorPanelCell = new WebMarkupContainer("page-navigation-panel"));
        add(navigatorPanelSeparatorCell = new WebMarkupContainer("page-nav-and-main-content-separator"));
        add(calloutPanelCell = new WebMarkupContainer("callout-panel"));
        add(calloutPanelBottomCell = new WebMarkupContainer("callout-panel-bottom"));

        add(footerBarCell = new WebMarkupContainer("footer-bar"));
        footerBarCell.add(new AttributeModifier("colspan", true, new Model() {
            public Object getObject(final Component component)
            {
                // if there is no callout panel we want the footer to extend the entire page width
                return isCalloutPanelVisible() ? "1" : "2";
            }
        }));
    }

    public boolean isCalloutPanelVisible()
    {
        return calloutPanelCell.isVisible();
    }

    public void setCalloutPanelVisible(boolean calloutPanelVisible)
    {
        calloutPanelCell.setVisible(calloutPanelVisible);
        calloutPanelBottomCell.setVisible(calloutPanelVisible);
    }

    public boolean isNavigatorPanelVisible()
    {
        return navigatorPanelCell.isVisible();
    }

    public void setNavigatorPanelVisible(boolean navigatorPanelVisible)
    {
        navigatorPanelCell.setVisible(navigatorPanelVisible);
        navigatorPanelSeparatorCell.setVisible(navigatorPanelVisible);
    }

    protected class MainMenu extends ListView
    {
        private Menu mainMenu;

        public MainMenu(final String componentName, final Menu mainMenu)
        {
            super(componentName, mainMenu.getMenuItems());
            this.mainMenu = mainMenu;
        }

        protected void populateItem(final ListItem listItem)
        {
            final MenuItem item = mainMenu.getMenuItems().get(listItem.getIndex());
            if (! item.isDisabled())
                listItem.add(new MainMenuItem(item));
        }
    }

    protected class MainMenuItem extends BookmarkablePageLink
    {
        private MenuItem menuItem;

        public MainMenuItem(final MenuItem menuItem)
        {
            super("main-menu-item", menuItem.getPage());
            this.menuItem = menuItem;
            add(new Label("main-menu-item-label", menuItem.getLabel()));
            setAutoEnable(false);
        }
    }
}
