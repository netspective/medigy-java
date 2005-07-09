/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.query;

import com.medigy.service.query.exception.QueryDefinitionException;
import com.medigy.service.dto.ServiceParameters;

import java.util.List;
import java.util.ArrayList;

/**
 * This interface is used to declare a condition  defined after the WHERE clause.
 * The condition describes the field it is associated with and the SQL comparison to use; it DOES NOT
 * describe where it is getting the value from to bind to the field. The caller of the
 * {@link QueryDefnCondition#getWhereCondExpr(QueryDefinitionSelect, QueryDefnStatementGenerator, Object)}
 * method MUST explicitly pass in the binding value.
 *
 */
public interface QueryDefnCondition
{
    public enum Connector
    {
        AND("and"), OR("or");

        private final String name;

        Connector(final String sql)
        {
            this.name = sql;
        }

        public String sql()
        {
            return name;
        }

        public static List<String> listNames()
        {
            final List<String> list = new ArrayList<String>();
            for (Connector conn : Connector.values())
            {
                list.add(conn.sql());
            }
            return list;
        }
    }

    public boolean isRemoveIfValueNull();
    public void setRemoveIfValueNull(final boolean removeIfValueNull);
    public void setConnector(final Connector connector);
    public String getConnectorSql();
    public QueryDefinition getOwner();
    public QueryDefnCondition getParentCondition();
    public void setParentCondition(final QueryDefnCondition parentCondition);
    public QueryDefinitionField getField();
    public void setField(final QueryDefinitionField field);
    public String getBindExpr();
    public void setBindExpr(final String bindExpr);
    public SqlComparison getComparison();
    public void setComparison(final SqlComparison comparison);
    public boolean isJoinOnly();
    public void setJoinOnly(final boolean joinOnly);
    public String getWhereCondExpr(QueryDefinitionSelect select, QueryDefnStatementGenerator stmt,
                                   final Object value) throws QueryDefinitionException;

    public boolean useCondition(QueryDefnStatementGenerator stmtGen, QueryDefinitionConditions usedConditions,
                                final Object value) throws QueryDefinitionException;

}
