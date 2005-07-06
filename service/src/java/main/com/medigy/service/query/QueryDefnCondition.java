/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.query;

import com.medigy.service.query.exception.QueryDefinitionException;
import com.medigy.service.dto.ServiceParameters;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class QueryDefnCondition
{
    private static final Log log = LogFactory.getLog(QueryDefnCondition.class);

    private QueryDefinition owner;
    private QueryDefnCondition parentCondition;
    private QueryDefnField field;
    private SqlComparison comparison;
    private QueryDefinitionConditionValue value;
    private QueryDefinitionConditions nestedConditions;
    private boolean removeIfValueNull = true;
    private boolean joinOnly = true; // use only the join condition from the field (changed to false if comparison is provided)
    private String bindExpr;

    private Connector connector;

    public enum Connector
    {
        AND("and"), OR("or");

        private final String sql;

        Connector(final String sql)
        {
            this.sql = sql;
        }

        public String sql()
        {
            return sql;
        }
    }


    public QueryDefnCondition(final QueryDefinition owner)
    {
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

    public void setOwner(final QueryDefinition owner)
    {
        this.owner = owner;
    }

    public QueryDefnCondition getParentCondition()
    {
        return parentCondition;
    }

    public void setParentCondition(final QueryDefnCondition parentCondition)
    {
        this.parentCondition = parentCondition;
    }

    public QueryDefnField getField()
    {
        return field;
    }

    public void setField(final QueryDefnField field)
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

    public Object getValue(final ServiceParameters params) throws QueryDefinitionException
    {
        return value.getValue(params);
    }

    public void setValue(final QueryDefinitionConditionValue value)
    {
        this.value = value;
    }

    public void setValue(final Class parameterClass, final String propertyName)
    {
        final QueryDefinitionConditionValue value = new QueryDefinitionConditionValue(this, parameterClass, propertyName);
        this.value = value;
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
                                   final ServiceParameters params) throws QueryDefinitionException
    {
        if(nestedConditions == null)
            return comparison.getWhereCondExpr(select, stmt, this, params);

        StringBuffer sql = new StringBuffer();
        int lastNestedCond = nestedConditions.size() - 1;
        for(int c = 0; c <= lastNestedCond; c++)
        {
            QueryDefnCondition cond = nestedConditions.get(c);
            stmt.addJoin(cond.getField());
            sql.append(" (" + cond.getWhereCondExpr(select, stmt, params) + ")");
            if(c != lastNestedCond)
                sql.append(cond.getConnectorSql());
        }
        return sql.toString();
    }

    public boolean useCondition(QueryDefnStatementGenerator stmtGen, QueryDefinitionConditions usedConditions,
                                final ServiceParameters params) throws QueryDefinitionException
    {
        log.info("Condition value: " + value.getPropertyName());
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

        if(nestedConditions != null)
        {
            QueryDefinitionConditions nestedUsedConditions = nestedConditions.getUsedConditions(stmtGen, params);
            if(nestedUsedConditions.size() == 0)
                return false;

            //usedConditions.add(nestedUsedConditions);
            return true;
        }
        else
        {

            if(value == null)
                return false;

            Object vs = getValue(params);
            log.info("Condition value: " + vs);
            if (vs == null)
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
}
