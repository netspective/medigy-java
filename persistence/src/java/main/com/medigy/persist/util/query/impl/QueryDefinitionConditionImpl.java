/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.util.query.impl;

import com.medigy.persist.util.query.AbstractQueryDefinitionCondition;
import com.medigy.persist.util.query.QueryDefinitionField;
import com.medigy.persist.util.query.QueryDefnStatementGenerator;
import com.medigy.persist.util.query.SqlComparison;
import com.medigy.persist.util.query.exception.QueryDefinitionException;
import com.medigy.persist.util.value.ValueContext;

public class QueryDefinitionConditionImpl extends AbstractQueryDefinitionCondition
{
    public QueryDefinitionConditionImpl(final String name, final QueryDefinitionField field, final SqlComparison comparison, final String connector)
    {
        setName(name);
        setField(field);
        setComparison(comparison);
        setConnector(connector);
    }

    public String getDisplayCaption()
    {
        return getField().getCaption();
    }

    /**
     * Checks to see if this condition should be used while generating the query statement
     * @param stmtGen
     * @param valueContext
     * @return True if the condition is to be used
     * @throws QueryDefinitionException
     */
    public boolean useCondition(final QueryDefnStatementGenerator stmtGen, final ValueContext valueContext) throws QueryDefinitionException
    {
        // if we don't allow nulls, always use the condition
        if(!isRemoveIfValueNull())
            return true;

        if(getValueProvider() == null || getValueProvider().getValue() == null)
            return false;
        return true;
    }
}
