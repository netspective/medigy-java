/*
 * $Id: DefaultPageBodyBorder.java,v 1.9 2005-07-10 22:08:29 shahid.shah Exp $
 * $Revision: 1.9 $
 * $Date: 2005-07-10 22:08:29 $
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
import wicket.AttributeModifier;
import wicket.Component;
import wicket.Page;
import wicket.markup.ComponentTag;
import wicket.markup.MarkupStream;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.border.Border;
import wicket.model.Model;

public class DefaultPageBodyBorder extends Border
{
    public interface HeadingProvider
    {
        public String getPageHeading();
        public String getPageTitle();
    }

    public interface PathProvider
    {
        // TODO: implement this
    }

    public interface NavigationPanelProvider
    {
        // TODO: implement this
    }

    public interface CalloutPanelProvider
    {
        // TODO: implement this
    }

    private final HeadingProvider headingProvider;
    private final PathProvider pathProvider;
    private final WebMarkupContainer pagePathDiv;
    private final WebMarkupContainer navigatorPanelCell;
    private final WebMarkupContainer navigatorPanelSeparatorCell;
    private final WebMarkupContainer calloutPanelCell;
    private final WebMarkupContainer calloutPanelBottomCell;
    private final WebMarkupContainer mainContentCell;
    private final WebMarkupContainer footerBarCell;

    public DefaultPageBodyBorder(final String componentName, final Page page)
    {
        super(componentName);

        this.headingProvider = page instanceof HeadingProvider ? (HeadingProvider) page : null;
        this.pathProvider = page instanceof PathProvider ? (PathProvider) page : null;

        final boolean navigatorPanelVisible = page instanceof NavigationPanelProvider;
        final boolean calloutPanelVisible = page instanceof CalloutPanelProvider;

        add(((DefaultApplication) getApplication()).createMainMenuComponent("mainMenu"));

        add((pagePathDiv = new WebMarkupContainer("pagePath")));
        pagePathDiv.setVisible(pathProvider != null);

        add(calloutPanelCell = new WebMarkupContainer("callout-panel"));
        add(calloutPanelBottomCell = new WebMarkupContainer("callout-panel-bottom"));
        calloutPanelCell.setVisible(calloutPanelVisible);
        calloutPanelBottomCell.setVisible(calloutPanelVisible);

        add(mainContentCell = new WebMarkupContainer("main-content-cell"));
        mainContentCell.add(new AttributeModifier("colspan", true, new Model() {
            public Object getObject(final Component component)
            {
                // if there is no callout panel we want the footer to extend the entire page width
                return calloutPanelVisible ? "1" : "2";
            }
        }));

        mainContentCell.add(navigatorPanelCell = new WebMarkupContainer("page-navigation-panel"));
        mainContentCell.add(navigatorPanelSeparatorCell = new WebMarkupContainer("page-nav-and-main-content-separator"));
        navigatorPanelCell.setVisible(navigatorPanelVisible);
        navigatorPanelSeparatorCell.setVisible(navigatorPanelVisible);

        mainContentCell.add(new WebMarkupContainer("heading")
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
                return calloutPanelVisible ? "1" : "2";
            }
        }));
    }
}
