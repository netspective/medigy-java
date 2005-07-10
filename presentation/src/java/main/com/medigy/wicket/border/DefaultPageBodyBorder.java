/*
 * $Id: DefaultPageBodyBorder.java,v 1.7 2005-07-10 21:22:37 shahid.shah Exp $
 * $Revision: 1.7 $
 * $Date: 2005-07-10 21:22:37 $
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
import com.medigy.wicket.page.PageHeadingProvider;
import wicket.AttributeModifier;
import wicket.Component;
import wicket.markup.ComponentTag;
import wicket.markup.MarkupStream;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.border.Border;
import wicket.model.Model;

public class DefaultPageBodyBorder extends Border
{
    private PageHeadingProvider headingProvider;
    private WebMarkupContainer mainMenu;
    private WebMarkupContainer navigatorPanelCell;
    private WebMarkupContainer navigatorPanelSeparatorCell;
    private WebMarkupContainer calloutPanelCell;
    private WebMarkupContainer calloutPanelBottomCell;
    private WebMarkupContainer mainContentCell;
    private WebMarkupContainer footerBarCell;
    private WebMarkupContainer headingDiv;

    public DefaultPageBodyBorder(final String componentName)
    {
        super(componentName);

        add(mainMenu = ((DefaultApplication) getApplication()).createMainMenuComponent("mainMenu"));

        add(calloutPanelCell = new WebMarkupContainer("callout-panel"));
        add(calloutPanelBottomCell = new WebMarkupContainer("callout-panel-bottom"));

        add(mainContentCell = new WebMarkupContainer("main-content-cell"));
        mainContentCell.add(new AttributeModifier("colspan", true, new Model() {
            public Object getObject(final Component component)
            {
                // if there is no callout panel we want the footer to extend the entire page width
                return isCalloutPanelVisible() ? "1" : "2";
            }
        }));

        mainContentCell.add(navigatorPanelCell = new WebMarkupContainer("page-navigation-panel"));
        mainContentCell.add(navigatorPanelSeparatorCell = new WebMarkupContainer("page-nav-and-main-content-separator"));
        mainContentCell.add(headingDiv = new WebMarkupContainer("heading")
        {
            protected void onComponentTagBody(final MarkupStream markupStream, final ComponentTag componentTag)
            {
                replaceComponentTagBody(markupStream, componentTag, headingProvider == null ? "No Page Heading Provider Available" : headingProvider.getPageHeading());
            }
        });

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

    public void setCalloutPanelVisible(final boolean calloutPanelVisible)
    {
        calloutPanelCell.setVisible(calloutPanelVisible);
        calloutPanelBottomCell.setVisible(calloutPanelVisible);
    }

    public boolean isNavigatorPanelVisible()
    {
        return navigatorPanelCell.isVisible();
    }

    public void setNavigatorPanelVisible(final boolean navigatorPanelVisible)
    {
        navigatorPanelCell.setVisible(navigatorPanelVisible);
        navigatorPanelSeparatorCell.setVisible(navigatorPanelVisible);
    }

    public void setHeadingProvider(final PageHeadingProvider headingProvider)
    {
        this.headingProvider = headingProvider;
    }
}
