/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.util.query;

import com.medigy.persist.util.query.exception.QueryDefinitionException;
import com.medigy.persist.util.value.ValueContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class QueryDefinitionConditions
{
    private final Log log = LogFactory.getLog(QueryDefinitionConditions.class);

    private List<QueryDefnCondition> list = new ArrayList<QueryDefnCondition>();
    private Map<String, QueryDefnCondition> map = new HashMap<String, QueryDefnCondition>();

    private QueryDefnCondition parentCondition;
    private boolean haveAnyDynamicConditions = false;

    public QueryDefinitionConditions()
    {

    }

    public QueryDefinitionConditions(QueryDefnCondition parent)
    {
        parentCondition = parent;
    }

    public List<QueryDefnCondition> getList()
    {
        return list;
    }

    public void setList(final List<QueryDefnCondition> list)
    {
        this.list = list;
    }

    /**
     * A unique ID with respective to this condition list is assigned
     * @param condition
     */
    public void add(final QueryDefnCondition condition)
    {
        int index = list.size();
        list.add(condition);
        final String key = (condition.getParentCondition() != null ? condition.getParentCondition().getName() : "") + "." + condition.getField().getName() + index;
        condition.setName(key);
        map.put(key, condition);
        if(!haveAnyDynamicConditions && condition.isRemoveIfValueNull())
            haveAnyDynamicConditions = true;
    }

    public QueryDefnCondition get(final String id)
    {
        return map.get(id);
    }

    public QueryDefnCondition getParentCondition()
    {
        return parentCondition;
    }

    public void setParentCondition(final QueryDefnCondition parentCondition)
    {
        this.parentCondition = parentCondition;
    }

    public boolean isHaveAnyDynamicConditions()
    {
        return haveAnyDynamicConditions;
    }

    public void setHaveAnyDynamicConditions(final boolean haveAnyDynamicConditions)
    {
        this.haveAnyDynamicConditions = haveAnyDynamicConditions;
    }

    public int size()
    {
        return list.size();
    }

    public QueryDefinitionConditions getUsedConditions(final QueryDefnStatementGenerator stmtGen,
                                                       final ValueContext valueContext) throws QueryDefinitionException
    {
        // if we don't have any dynamic conditions, all the conditions will be used :)
        if(!haveAnyDynamicConditions)
            return this;

        // if we get to here, it means only some of the query conditions will be used
        // we we need to keep track of them
        QueryDefinitionConditions usedConditions = new QueryDefinitionConditions(parentCondition);

        int allCondsCount = list.size();
        for(int c = 0; c < allCondsCount; c++)
        {
            QueryDefnCondition cond = list.get(c);
            //cond.useCondition(stmtGen, usedConditions, fieldValues);
            if (useCondition(cond, valueContext))
            {
                System.out.println("Use condition: " + cond.getField().getName());
                usedConditions.add(cond);
                stmtGen.addJoin(cond.getField());
            }
            else
            {
                System.out.println("Dont use: " + cond.getField().getName());
            }
        }

        return usedConditions;
    }

    /**
     * Checks to see if the condition should be included in the statement
     * @param condition
     * @param valueContext  the context to use to lookup condition values
     * @return True if the condition should be used as part of the query
     */
    protected boolean useCondition(final QueryDefnCondition condition, final ValueContext valueContext)
    {
        // if we don't allow nulls, always use the condition
        if(!condition.isRemoveIfValueNull())
            return true;

        // DOES NOT SUPPORT NESTED CONDITIONS
        if(condition.getValueProvider() == null || condition.getValueProvider().getValue() == null)
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
        return true;
    }

    /**
     * Create the SQL string for the list of Query conditions
     *
     * @return String
     */
    public String createSql(QueryDefnStatementGenerator stmtGen, QueryDefinitionConditions usedConditions,
                            final ValueContext valueContext) throws QueryDefinitionException
    {
        StringBuffer sb = new StringBuffer();
        QueryDefinitionSelect select = stmtGen.getQuerySelect();
        int usedCondsCount = usedConditions.list.size();
        int condsUsedLast = usedCondsCount - 1;

        for(int c = 0; c < usedCondsCount; c++)
        {
            boolean conditionAdded = false;
            Object condObj = usedConditions.list.get(c);
            if(condObj instanceof QueryDefinitionConditions)
            {
                String sql = createSql(stmtGen, (QueryDefinitionConditions) condObj, valueContext);
                if(sql != null && sql.length() > 0)
                {
                    sb.append("(" + sql + ")");
                    conditionAdded = true;
                }
                if(c != condsUsedLast)
                    sb.append(parentCondition.getConnector());
            }
            else
            {
                // single query condition. not a list.
                QueryDefnCondition cond = usedConditions.list.get(c);
                if(!cond.isJoinOnly())
                {
                    
                    sb.append(" (" + cond.getComparison().getWhereCondExpr(select, stmtGen, cond, valueContext) + ")");
                    conditionAdded = true;
                }
                if(c != condsUsedLast && !((QueryDefnCondition) usedConditions.list.get(c + 1)).isJoinOnly())
                    sb.append(" " + cond.getConnector() + " ");
            }
            if(conditionAdded)
                sb.append("\n");
        }
        log.info("Condition SQL: " + sb.toString());
        return sb.toString();
    }
}
