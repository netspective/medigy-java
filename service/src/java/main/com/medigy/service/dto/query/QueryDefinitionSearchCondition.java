/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.dto.query;
public class QueryDefinitionSearchCondition
{
    private String field;
    private String fieldComparison;
    private String fieldValue;
    private String connector;

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
