/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.util.query.comparison;

public class EqualsComparison extends BinaryOpComparison
{
    private static final String COMPARISON_NAME = "Equals";

    public EqualsComparison()
    {
        super("=", Group.GENERAL);
    }

    public String getName()
    {
        return COMPARISON_NAME;
    }
}
