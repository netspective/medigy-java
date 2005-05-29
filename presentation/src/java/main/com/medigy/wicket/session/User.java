/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.wicket.session;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

public final class User implements Serializable
{
    private String name;

    public final String getName()
    {
        return name;
    }

    public final void setName(final String string)
    {
        name = string;
    }
}


