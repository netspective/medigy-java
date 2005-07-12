/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.util.query;

import com.medigy.persist.util.query.exception.QueryDefinitionException;

import java.util.ArrayList;
import java.util.List;

/**
 * This interface is used to declare a condition  defined after the WHERE clause.
 * The condition describes the field it is associated with and the SQL comparison to use; it DOES NOT
 * describe where it is getting the value from to bind to the field. Also there is no concrete
 * implementation of this interface and all logic has been moved into the
 * {@link QueryDefinitionConditions} class.
 *
 */
public interface QueryDefnCondition
{
    public static enum Connector
    {
        AND("and"), OR("or");

        private String name;

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
            System.out.println(">>>>>>> CONNECTORS" + list);
            return list;
        }
    }

    public boolean isRemoveIfValueNull();
    public String getConnectorSql();
    public QueryDefinitionField getField();
    public String getBindExpr();
    public SqlComparison getComparison();
    public boolean isJoinOnly();
}
