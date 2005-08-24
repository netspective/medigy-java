/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.util.query;

import com.medigy.persist.util.query.exception.QueryDefinitionException;

import java.util.List;
import java.util.Map;

public interface QueryDefinition
{
    public String getName();

    public Map<String, QueryDefinitionField> getFields();
    public void setFields(final Map<String, QueryDefinitionField> fields);

    public QueryDefinitionField addField(final String fieldName, final String columnName, final String caption, final QueryDefinitionJoin join);
    public QueryDefinitionField getField(final String fieldName) throws QueryDefinitionException;

    public void addJoin(final QueryDefinitionJoin join);
    public QueryDefinitionJoins getJoins();
    public void setJoins(final QueryDefinitionJoins joins);

    public QueryDefinitionSelect createSelect(final String selectName);

    public QueryDefnSelects getSelects();
    public void setSelects(final QueryDefnSelects selects);
    public void addSelect(final QueryDefinitionSelect select);

    public List<String> getConnectors();

}
