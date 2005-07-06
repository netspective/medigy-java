/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.query;

import java.util.Map;
import java.util.HashMap;

public class QueryDefinition
{
    private String name;
    // these are all the possible joins and fields
    private Map<String, QueryDefnField> fields = new HashMap<String, QueryDefnField>();
    private QueryDefnJoins joins = new QueryDefnJoins();

    // these are the actual SELECT objects utilizing the above joins and fields
    private QueryDefnSelects selects = new QueryDefnSelects();

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public Map<String, QueryDefnField> getFields()
    {
        return fields;
    }

    public void setFields(final Map<String, QueryDefnField> fields)
    {
        this.fields = fields;
    }

    public void addField(final QueryDefnField field)
    {
        this.fields.put(field.getName(), field);
    }

    public void addField(final String fieldName, final String columnName, final String joinName)
    {
        final QueryDefnField field = new QueryDefnField(fieldName, columnName, this);
        addField(field);
    }

    public QueryDefnField addField(final String fieldName, final String columnName, final QueryDefnJoin join)
    {
        final QueryDefnField field = new QueryDefnField(fieldName, columnName, this);
        field.setJoinDefn(join);
        addField(field);
        return field;
    }

    public QueryDefnField getField(final String fieldName)
    {
        return this.fields.get(fieldName);
    }

    public void addJoin(final QueryDefnJoin join)
    {
        this.joins.add(join);
    }

    public QueryDefnJoins getJoins()
    {
        return joins;
    }

    public void setJoins(final QueryDefnJoins joins)
    {
        this.joins = joins;
    }

    public QueryDefnSelects getSelects()
    {
        return selects;
    }

    public void setSelects(final QueryDefnSelects selects)
    {
        this.selects = selects;
    }

    public void addSelect(final QueryDefinitionSelect select)
    {
        select.setQueryDefinition(this);
        this.selects.add(select);
    }



}
