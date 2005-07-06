/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.query;

import com.medigy.persist.model.common.Entity;
import com.medigy.service.query.exception.QueryDefinitionException;
import com.medigy.service.query.exception.QueryDefnJoinNotFoundException;

import java.util.List;
import java.util.StringTokenizer;
import java.util.ArrayList;

public class QueryDefnJoin
{
    private QueryDefinition owner;
    private String name;

    private String tableName;

    private String fromClauseExpr;
    private String condition;
    private boolean autoInclude;
    private String implyJoinsStr;
    private List<QueryDefnJoin> impliedJoins = new ArrayList<QueryDefnJoin>();

    public QueryDefinition getOwner()
    {
        return owner;
    }

    public void setOwner(final QueryDefinition owner)
    {
        this.owner = owner;
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

    public List<QueryDefnJoin> getImpliedJoins() throws QueryDefinitionException
    {
        if(implyJoinsStr != null && impliedJoins == null)
        {
            StringTokenizer st = new StringTokenizer(implyJoinsStr, ",");
            List<QueryDefnJoin> implyJoinsList = new ArrayList<QueryDefnJoin>();
            while(st.hasMoreTokens())
            {
                String join = st.nextToken();
                QueryDefnJoin joinDefn = owner.getJoins().get(join);
                if(joinDefn == null)
                    throw new QueryDefnJoinNotFoundException(owner, join, "implied join '" + join + "' not found in join '" + getName() + "'");
                implyJoinsList.add(joinDefn);
            }
            impliedJoins = implyJoinsList;
        }
        return impliedJoins;
    }

    public void setImpliedJoins(final List<QueryDefnJoin> impliedJoins)
    {
        this.impliedJoins = impliedJoins;
    }

    public void addImpliedJoin(final QueryDefnJoin impliedJoin)
    {
        this.impliedJoins.add(impliedJoin);
    }

    public String getFromExpr()
    {
        String tableName = getTable();
        return fromClauseExpr != null
               ? fromClauseExpr : (tableName.equals(name) ? tableName : (tableName + " " + name));
    }

    public String getTable()
    {
        return tableName == null ? name : tableName;
    }
}
