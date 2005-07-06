/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.query;

import com.medigy.service.query.exception.QueryDefinitionException;
import com.medigy.service.dto.ServiceParameters;

import java.util.List;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class QueryDefinitionConditions
{
    private final Log log = LogFactory.getLog(QueryDefinitionConditions.class);

    private List<QueryDefnCondition> list = new ArrayList<QueryDefnCondition>();
    private QueryDefnCondition parentCondition;
    private boolean haveAnyDynamicConditions;

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

    public void add(final QueryDefnCondition condition)
    {
        list.add(condition);
        if(!haveAnyDynamicConditions && condition.isRemoveIfValueNull())
            haveAnyDynamicConditions = true;
    }

    public QueryDefnCondition get(int i)
    {
        return (QueryDefnCondition) list.get(i);
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

    public QueryDefinitionConditions getUsedConditions(QueryDefnStatementGenerator stmtGen,
                                                       final  ServiceParameters params) throws QueryDefinitionException
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
            cond.useCondition(stmtGen, usedConditions, params);
        }

        return usedConditions;
    }

    /**
     * Create the SQL string for the list of Query conditions
     *
     * @return String
     */
    public String createSql(QueryDefnStatementGenerator stmtGen, QueryDefinitionConditions usedConditions,
                            final ServiceParameters params) throws QueryDefinitionException
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
                String sql = createSql(stmtGen, (QueryDefinitionConditions) condObj, params);
                if(sql != null && sql.length() > 0)
                {
                    sb.append("(" + sql + ")");
                    conditionAdded = true;
                }
                if(c != condsUsedLast)
                    sb.append(parentCondition.getConnectorSql());
            }
            else
            {
                // single query condition. not a list.
                QueryDefnCondition cond = (QueryDefnCondition) usedConditions.list.get(c);
                if(!cond.isJoinOnly())
                {
                    // create and add the where condition string only if this condition has a valid comparison
                    // (meaning the Select Condition was not inluded only for the sake of including the Join Condition of the table)
                    sb.append(" (" + cond.getWhereCondExpr(select, stmtGen, params) + ")");
                    conditionAdded = true;
                }
                if(c != condsUsedLast && !((QueryDefnCondition) usedConditions.list.get(c + 1)).isJoinOnly())
                    sb.append(cond.getConnectorSql());
            }
            if(conditionAdded)
                sb.append("\n");
        }
        log.info("Condition SQL: " + sb.toString());
        return sb.toString();
    }

}
