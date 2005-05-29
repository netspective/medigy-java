package com.medigy.wicket.session;

import java.util.List;

import wicket.Application;
import wicket.protocol.http.WebSession;

public final class AuthenticatedSession extends WebSession
{
    private User user;
    
    public AuthenticatedSession(final Application application, final String styleName)
    { 
        super(application);
        setStyle(styleName);
    }

    /**
     * Checks the given username and password, returning a User object if
     * if the username and password identify a valid user.
     * @param username The username
     * @param password The password
     * @return The signed in user
     */
    public final User authenticate(final String username, final String password)
    {
        if (user == null)
        {
            // Trivial password "db"
            if ("medigy".equalsIgnoreCase(username)
                && "medigy".equalsIgnoreCase(password))
            {
                // Create User object
                final User user = new User();

                user.setName(username);
                setUser(user);
            }
        }

        return user;
    }

    public boolean isSignedIn()
    {
        return user != null;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(final User user)
    {
        this.user = user;
    }
}


