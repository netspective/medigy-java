/*
 * $Id: DefaultPageBodyBorder.java,v 1.2 2005-05-30 02:26:27 shahid.shah Exp $
 * $Revision: 1.2 $
 * $Date: 2005-05-30 02:26:27 $
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

import wicket.AttributeModifier;
import wicket.Component;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.border.Border;
import wicket.model.Model;

public class DefaultPageBodyBorder extends Border
{
    private boolean navigatorPanelVisible;
    private boolean calloutPanelVisible;

    private WebMarkupContainer navigatorPanelCell;
    private WebMarkupContainer navigatorPanelSeparatorCell;
    private WebMarkupContainer calloutPanelCell;
    private WebMarkupContainer calloutPanelBottomCell;
    private WebMarkupContainer footerBarCell;

    public DefaultPageBodyBorder(final String componentName)
    {
        super(componentName);
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
        return calloutPanelVisible;
    }

    public void setCalloutPanelVisible(boolean calloutPanelVisible)
    {
        this.calloutPanelVisible = calloutPanelVisible;
        calloutPanelCell.setVisible(calloutPanelVisible);
        calloutPanelBottomCell.setVisible(calloutPanelVisible);
    }

    public boolean isNavigatorPanelVisible()
    {
        return navigatorPanelVisible;
    }

    public void setNavigatorPanelVisible(boolean navigatorPanelVisible)
    {
        this.navigatorPanelVisible = navigatorPanelVisible;
        navigatorPanelCell.setVisible(navigatorPanelVisible);
        navigatorPanelSeparatorCell.setVisible(navigatorPanelVisible);
    }
}
