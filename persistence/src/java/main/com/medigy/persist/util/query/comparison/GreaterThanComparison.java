/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.util.query.comparison;

public class GreaterThanComparison extends BinaryOpComparison
{
    public static final String COMPARISON_NAME = "Greater Than";
    
    public GreaterThanComparison(final Group group)
    {
        super(">", group);
    }

    public String getName()
    {
        return COMPARISON_NAME;
    }
}
