/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.wicket.page;

import wicket.Component;
import wicket.MarkupContainer;
import wicket.model.IModel;
import wicket.markup.html.border.Border;
import com.medigy.wicket.border.DefaultPageBodyBorder;
import com.medigy.wicket.session.AuthenticatedSession;

/**
 * Ensures that user is authenticated in session.  If no user is signed in, a sign
 * in is forced by redirecting the browser to the SignIn page.
 * <p>
 * This base class also creates a border for each page subclass, automatically adding
 * children of the page to the border.  This accomplishes two important things:
 * (1) subclasses do not have to repeat the code to create the border navigation and
 * (2) since subclasses do not repeat this code, they are not hardwired to page
 * navigation structure details
 *
 * @author Jonathan Locke
 */
public class AuthenticatedWebPage extends BasePage
{
    private Border border;

    public AuthenticatedWebPage()
    {
    }

    public AuthenticatedWebPage(IModel iModel)
    {
        super(iModel);
    }

    /**
     * Adding children to instances of this class causes those children to
     * be added to the border child instead.
     * @see wicket.MarkupContainer#add(wicket.Component)
     */
    public MarkupContainer add(final Component child)
    {
        // Add children of the page to the page's border component
        if (border == null)
        {
            // Create border and add it to the page
            border = new DefaultPageBodyBorder("border");
            super.add(border);
        }
        border.add(child);
        return this;
    }

    /**
     * Removing children from instances of this class causes those children to
     * be removed from the border child instead.
     * @see wicket.MarkupContainer#removeAll()
     */
    public void removeAll()
    {
        border.removeAll();
    }

    /**
     * Replacing children on instances of this class causes those children
     * to be replaced on the border child instead.
     * @see wicket.MarkupContainer#replace(wicket.Component)
     */
    public MarkupContainer replace(Component child)
    {
        return border.replace(child);
    }

    /**
     * Get downcast session object
     * 
     * @return The session
     */
    public AuthenticatedSession getAuthenticatedSession()
    {
        return (AuthenticatedSession) getSession();
    }

    /**
     * @see wicket.Page#checkAccess()
     */
    protected boolean checkAccess()
    {
        // Is user signed in?
        if (getAuthenticatedSession().isSignedIn())
        {
            // okay to proceed
            return true;
        }
        else
        {
            // Force sign in
            redirectToInterceptPage(newPage(SignInPage.class));
            return false;
        }
    }
}

