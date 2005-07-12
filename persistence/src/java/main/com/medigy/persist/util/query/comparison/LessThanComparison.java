/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.util.query.comparison;

public class LessThanComparison extends BinaryOpComparison
{
    public static final String COMPARISON_NAME = "Less Than";

    public LessThanComparison()
    {
        super("<", Group.GENERAL);
    }

    public String getName()
    {
        return COMPARISON_NAME;
    }
}
