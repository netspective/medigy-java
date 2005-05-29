package com.medigy.wicket.page;

import com.medigy.wicket.panel.SignInPanel;
import com.medigy.wicket.session.AuthenticatedSession;
import com.medigy.wicket.session.User;
import wicket.PageParameters;

public final class SignInPage extends BasePage
{
    public SignInPage(final PageParameters parameters)
    {
        add(new SignInPanel("signInPanel", false)
        {
            public boolean signIn(final String username, final String password)
            {
                // Sign the user in
                final User user = ((AuthenticatedSession)getSession()).authenticate(username, password);

                // If the user was signed in
                if (user != null)
                {
                    return true;
                }
                else
                {
                    error(getLocalizer().getString("signInForm.couldNotAuthenticate", this));
                    return false;
                }
            }
        });
    }
    
    public SignInPage()
    {
        this(null);
    }
}
