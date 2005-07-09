/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.query.impl;

import com.medigy.service.query.QueryDefinition;
import com.medigy.service.query.QueryDefinitionJoin;
import com.medigy.service.query.exception.QueryDefinitionException;
import com.medigy.service.query.exception.QueryDefnJoinNotFoundException;

import java.util.List;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class QueryDefinitionJoinImpl implements QueryDefinitionJoin
{
    private QueryDefinition owner;
    private String name;

    private String tableName;

    private String fromClauseExpr;
    private String condition;
    private boolean autoInclude;
    private String implyJoinsStr;
    private List<QueryDefinitionJoin> impliedJoins = new ArrayList<QueryDefinitionJoin>();

    public QueryDefinitionJoinImpl(final QueryDefinition owner)
    {
        this.owner = owner;
    }

    public QueryDefinition getOwner()
    {
        return owner;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public String getTableName()
    {
        return tableName;
    }

    public void setTableName(final String tableName)
    {
        this.tableName = tableName;
    }

    public String getFromClauseExpr()
    {
        return fromClauseExpr;
    }

    public void setFromClauseExpr(final String fromClauseExpr)
    {
        this.fromClauseExpr = fromClauseExpr;
    }

    public String getCondition()
    {
        return condition;
    }

    public void setCondition(final String condition)
    {
        this.condition = condition;
    }

    public boolean isAutoInclude()
    {
        return autoInclude;
    }

    public void setAutoInclude(final boolean autoInclude)
    {
        this.autoInclude = autoInclude;
    }

    public String getImplyJoinsStr()
    {
        return implyJoinsStr;
    }

    public void setImplyJoinsStr(final String implyJoinsStr)
    {
        this.implyJoinsStr = implyJoinsStr;
    }

    public List<QueryDefinitionJoin> getImpliedJoins() throws QueryDefinitionException
    {
        if(implyJoinsStr != null && impliedJoins == null)
        {
            StringTokenizer st = new StringTokenizer(implyJoinsStr, ",");
            List<QueryDefinitionJoin> implyJoinsList = new ArrayList<QueryDefinitionJoin>();
            while(st.hasMoreTokens())
            {
                String join = st.nextToken();
                QueryDefinitionJoin joinDefinition = owner.getJoins().get(join);
                if(joinDefinition == null)
                    throw new QueryDefnJoinNotFoundException(owner, join, "implied join '" + join + "' not found in join '" + getName() + "'");
                implyJoinsList.add(joinDefinition);
            }
            impliedJoins = implyJoinsList;
        }
        return impliedJoins;
    }

    public void setImpliedJoins(final List<QueryDefinitionJoin> impliedJoins)
    {
        this.impliedJoins = impliedJoins;
    }

    public void addImpliedJoin(final QueryDefinitionJoin impliedJoin)
    {
        this.impliedJoins.add(impliedJoin);
    }

    public String getFromExpr()
    {
        String tableName = getTable();
        return fromClauseExpr != null
               ? fromClauseExpr : (tableName.equals(name) ? tableName : (tableName + " as " + name));
    }

    public String getTable()
    {
        return tableName == null ? name : tableName;
    }
}
