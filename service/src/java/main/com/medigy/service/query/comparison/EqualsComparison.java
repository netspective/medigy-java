/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.query.comparison;

public class EqualsComparison extends BinaryOpComparison
{
    public EqualsComparison(String name)
    {
        super(name, name,  "=");
    }

    public EqualsComparison(String name, String caption)
    {
        super(name, caption, "=");
    }
}
