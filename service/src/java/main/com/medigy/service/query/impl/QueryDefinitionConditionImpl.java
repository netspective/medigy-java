/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.query.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.medigy.service.query.QueryDefinition;
import com.medigy.service.query.QueryDefinitionField;
import com.medigy.service.query.SqlComparison;
import com.medigy.service.query.QueryDefinitionConditionValue;
import com.medigy.service.query.QueryDefinitionConditions;
import com.medigy.service.query.QueryDefinitionSelect;
import com.medigy.service.query.QueryDefnStatementGenerator;
import com.medigy.service.query.QueryDefnCondition;
import com.medigy.service.query.exception.QueryDefinitionException;
import com.medigy.service.dto.ServiceParameters;

public class QueryDefinitionConditionImpl implements QueryDefnCondition
{
    private static final Log log = LogFactory.getLog(QueryDefinitionConditionImpl.class);

    private QueryDefinitionSelect select;
    private QueryDefinition owner;
    private QueryDefnCondition parentCondition;
    private QueryDefinitionField field;
    private SqlComparison comparison;
    private QueryDefinitionConditions nestedConditions;
    private boolean removeIfValueNull = true;
    private boolean joinOnly = true; // use only the join condition from the field (changed to false if comparison is provided)
    private String bindExpr;

    private Connector connector;

    public QueryDefinitionConditionImpl(final QueryDefinitionSelect select, final QueryDefinition owner)
    {
        this.select = select;
        this.owner = owner;
        this.connector = Connector.AND;
    }

    public boolean isRemoveIfValueNull()
    {
        return removeIfValueNull;
    }

    public void setRemoveIfValueNull(final boolean removeIfValueNull)
    {
        this.removeIfValueNull = removeIfValueNull;
    }

    public void setConnector(final Connector connector)
    {
        this.connector = connector;
    }

    public String getConnectorSql()
    {
        return this.connector.sql();
    }

    public QueryDefinition getOwner()
    {
        return owner;
    }

    public QueryDefnCondition getParentCondition()
    {
        return parentCondition;
    }

    public void setParentCondition(final QueryDefnCondition parentCondition)
    {
        this.parentCondition = parentCondition;
    }

    public QueryDefinitionField getField()
    {
        return field;
    }

    public void setField(final QueryDefinitionField field)
    {
        this.field = field;
    }

    public String getBindExpr()
    {
        return bindExpr;
    }

    public void setBindExpr(final String bindExpr)
    {
        this.bindExpr = bindExpr;
    }

    public SqlComparison getComparison()
    {
        return comparison;
    }

    public void setComparison(final SqlComparison comparison)
    {
        this.comparison = comparison;
        setJoinOnly(false);
    }

    public boolean isJoinOnly()
    {
        return joinOnly;
    }

    public void setJoinOnly(final boolean joinOnly)
    {
        this.joinOnly = joinOnly;
    }

    public String getWhereCondExpr(QueryDefinitionSelect select, QueryDefnStatementGenerator stmt,
                                   final Object value) throws QueryDefinitionException
    {
        if(nestedConditions == null)
            return comparison.getWhereCondExpr(select, stmt, this, value);

        StringBuffer sql = new StringBuffer();
        int lastNestedCond = nestedConditions.size() - 1;
        for(int c = 0; c <= lastNestedCond; c++)
        {
            QueryDefnCondition cond = nestedConditions.get(c);
            stmt.addJoin(cond.getField());
            sql.append(" (" + cond.getWhereCondExpr(select, stmt, value) + ")");
            if(c != lastNestedCond)
                sql.append(cond.getConnectorSql());
        }
        return sql.toString();
    }

    public boolean useCondition(QueryDefnStatementGenerator stmtGen, QueryDefinitionConditions usedConditions,
                                final Object value) throws QueryDefinitionException
    {
        /*
        if(script != null)
        {
            Object scriptResult = null;
            try
            {
                ValueSource vs = getValue();
                if(vs != null)
                {
                    Value value = vs.getValue(vc);
                    if(value == null)
                        return false;

                    ScriptContext sc = vc.getScriptContext(script);
                    scriptResult = script.callFunction(sc, null, "useCondition", new Object[]{this, vc, value});
                }
            }
            catch(ScriptException e)
            {
                throw new QueryDefinitionException(getOwner(), e);
            }

            boolean useCondition = true;
            if(scriptResult instanceof Boolean)
                useCondition = ((Boolean) scriptResult).booleanValue();
            else if(scriptResult != null)
                useCondition = TextUtils.getInstance().toBoolean(scriptResult.toString(), false);

            if(! useCondition)
                return false;
        }
        */
        // if we don't allow nulls, always use the condition
        if(!removeIfValueNull)
            return true;


        // DOES NOT SUPPORT NESTED CONDITIONS
        if(value == null)
            return false;

        // TODO: Need to figure out using reflection?
        /*
        if(value.isListValue())
        {
            String[] values = value.getTextValues();
            if(values == null || values.length == 0 || (values.length == 1 && (values[0] == null || values[0].length() == 0)))
                return false;
        }
        else
        {
            String textValue = value.getTextValue();
            if(textValue == null || textValue.length() == 0)
                return false;
        }
        */
        usedConditions.add(this);
        stmtGen.addJoin(field);
        return true;

    }

}
