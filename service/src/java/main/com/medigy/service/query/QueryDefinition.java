/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.query;

import com.medigy.service.query.impl.QueryDefinitionFieldImpl;
import com.medigy.service.query.impl.QueryDefinitionSelectImpl;
import com.medigy.service.query.exception.QueryDefinitionException;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public interface QueryDefinition
{
    public String getName();

    public List<String> getFieldNames();
    public Map<String, QueryDefinitionField> getFields();
    public void setFields(final Map<String, QueryDefinitionField> fields);

    public QueryDefinitionField addField(final String fieldName, final String columnName, final String joinName) throws QueryDefinitionException;
    public QueryDefinitionField addField(final String fieldName, final String columnName, final QueryDefinitionJoin join);
    public QueryDefinitionField getField(final String fieldName) throws QueryDefinitionException;

    public void addJoin(final QueryDefinitionJoin join);
    public QueryDefinitionJoins getJoins();
    public void setJoins(final QueryDefinitionJoins joins);

    public QueryDefinitionSelect createSelect(final String selectName);
    public QueryDefinitionJoin createJoin();

    public QueryDefnSelects getSelects();
    public void setSelects(final QueryDefnSelects selects);
    public void addSelect(final QueryDefinitionSelect select);

}
