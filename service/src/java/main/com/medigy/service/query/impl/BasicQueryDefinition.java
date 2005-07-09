/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.query.impl;

import com.medigy.service.query.QueryDefinitionField;
import com.medigy.service.query.QueryDefinitionJoins;
import com.medigy.service.query.QueryDefnSelects;
import com.medigy.service.query.QueryDefinitionJoin;
import com.medigy.service.query.QueryDefinitionSelect;
import com.medigy.service.query.QueryDefinition;
import com.medigy.service.query.QueryDefnCondition;
import com.medigy.service.query.exception.QueryDefinitionException;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class BasicQueryDefinition implements QueryDefinition
{
    private String name;
    // these are all the possible joins and fields
    private Map<String, QueryDefinitionField> fields = new HashMap<String, QueryDefinitionField>();
    private QueryDefinitionJoins joins = new QueryDefinitionJoins();

    // these are the actual SELECT objects utilizing the above joins and fields
    private QueryDefnSelects selects = new QueryDefnSelects();

    public BasicQueryDefinition(final String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public List<String> getFieldNames()
    {
        return new ArrayList<String>(fields.keySet());
    }

    public Map<String, QueryDefinitionField> getFields()
    {
        return fields;
    }

    public void setFields(final Map<String, QueryDefinitionField> fields)
    {
        this.fields = fields;
    }

    /**
     * Used by {@link #addField(String, String, String)}. Override to use a specific field implementation.
     * @param fieldName
     * @param columnName
     * @return the field
     */
    protected QueryDefinitionField createField(final String fieldName, final String columnName, final QueryDefinitionJoin join)
    {
        return new QueryDefinitionFieldImpl(fieldName, columnName, join, this);
    }

    public QueryDefinitionField addField(final String fieldName, final String columnName, final String joinName) throws QueryDefinitionException
    {
        final QueryDefinitionJoin join = getJoins().get(joinName);
        if (join == null)
            throw new QueryDefinitionException(this, "Failed to find the Join with name: " + joinName);
        final QueryDefinitionField field = createField(fieldName, columnName, join);
        this.fields.put(field.getName(), field);
        return field;
    }

    public QueryDefinitionField addField(final String fieldName, final String columnName, final QueryDefinitionJoin join)
    {
        final QueryDefinitionField field = new QueryDefinitionFieldImpl(fieldName, columnName, join, this);
        this.fields.put(field.getName(), field);
        return field;
    }

    public QueryDefinitionField getField(final String fieldName) throws QueryDefinitionException
    {
        final QueryDefinitionField queryDefinitionField = this.fields.get(fieldName);
        if (queryDefinitionField == null)
            throw new QueryDefinitionException(this, "Unknown field name: " + fieldName);
        return queryDefinitionField;
    }

    public void addJoin(final QueryDefinitionJoin join)
    {
        this.joins.add(join);
    }

    public QueryDefinitionJoins getJoins()
    {
        return joins;
    }

    public void setJoins(final QueryDefinitionJoins joins)
    {
        this.joins = joins;
    }

    /**
     * Creates a new select. This DOES NOT add to the list of "cached" selects of this query definition.
     * @param selectName
     * @return new select
     */
    public QueryDefinitionSelect createSelect(final String selectName)
    {
        return new QueryDefinitionSelectImpl(selectName, this);
    }

    public QueryDefinitionJoin createJoin()
    {
        return new QueryDefinitionJoinImpl(this);
    }

    public QueryDefnSelects getSelects()
    {
        return selects;
    }

    public void setSelects(final QueryDefnSelects selects)
    {
        this.selects = selects;
    }

    /**
     * Adds a select to the query definition so that it is reusable.
     * @param select
     */
    public void addSelect(final QueryDefinitionSelect select)
    {
        this.selects.add(select);
    }


}
