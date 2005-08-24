/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.util.query.impl;

import com.medigy.persist.util.query.QueryDefinition;
import com.medigy.persist.util.query.QueryDefinitionField;
import com.medigy.persist.util.query.QueryDefinitionJoin;
import com.medigy.persist.util.query.QueryDefinitionJoins;
import com.medigy.persist.util.query.QueryDefinitionSelect;
import com.medigy.persist.util.query.QueryDefnSelects;
import com.medigy.persist.util.query.exception.QueryDefinitionException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasicQueryDefinition implements QueryDefinition
{

    private String[] conditionConnectors = new String[] {"and", "or"};

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

    public Map<String, QueryDefinitionField> getFields()
    {
        return fields;
    }

    public void setFields(final Map<String, QueryDefinitionField> fields)
    {
        this.fields = fields;
    }

    /**
     * @param fieldName
     * @param columnName
     * @return the field
     */
    protected QueryDefinitionField createField(final String fieldName, final String columnName, final QueryDefinitionJoin join)
    {
        return new QueryDefinitionFieldImpl(fieldName, columnName, join, this);
    }

    public QueryDefinitionField addField(final String fieldName, final String propertyName,
                                         final String caption,
                                         final QueryDefinitionJoin join)
    {
        final QueryDefinitionField field = new QueryDefinitionFieldImpl(fieldName, propertyName, join, this);
        field.setCaption(caption);
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

    public List<String> getConnectors()
    {
        final List<String> list= new ArrayList<String>();
        for (String connector : conditionConnectors)
        {
            list.add(connector);
        }
        return list;
    }


}
