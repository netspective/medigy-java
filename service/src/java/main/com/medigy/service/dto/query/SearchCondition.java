/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.dto.query;

import com.medigy.persist.util.query.QueryDefnCondition;

/**
 * DTO to contain the search criteria. You can either specify the condition specifically using
 * {@link #setCondition} or create a completely new condition using the {@link #setField}, {@link #setFieldComparison(String)}
 * , and {@link #setConnector(String)} .
 */
public class SearchCondition
{
    private String field;
    private String fieldComparison;
    private String fieldValue;
    private String connector;

    private QueryDefnCondition condition;

    public QueryDefnCondition getCondition()
    {
        return condition;
    }

    public void setCondition(final QueryDefnCondition condition)
    {
        this.condition = condition;
    }

    public String getField()
    {
        return field;
    }

    public void setField(final String field)
    {
        this.field = field;
    }

    public String getFieldComparison()
    {
        return fieldComparison;
    }

    public void setFieldComparison(final String fieldComparison)
    {
        this.fieldComparison = fieldComparison;
    }

    public String getFieldValue()
    {
        return fieldValue;
    }

    public void setFieldValue(final String fieldValue)
    {
        this.fieldValue = fieldValue;
    }

    public String getConnector()
    {
        return connector;
    }

    public void setConnector(final String connector)
    {
        this.connector = connector;
    }
}
