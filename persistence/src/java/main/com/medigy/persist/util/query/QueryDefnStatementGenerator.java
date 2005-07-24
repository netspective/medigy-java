/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.util.query;

import com.medigy.persist.util.query.exception.QueryDefinitionException;
import com.medigy.persist.util.query.comparison.StartsWithComparisonIgnoreCase;
import com.medigy.persist.util.query.comparison.EqualsComparison;
import com.medigy.persist.util.value.ValueContext;
import com.medigy.persist.model.person.Person;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;

public class QueryDefnStatementGenerator
{
    private QueryDefinition queryDefn;
    private QueryDefinitionSelect select;

    private List<String> selectClause = new ArrayList<String>();
    private List<String> fromClause = new ArrayList<String>();
    private List<String> fromClauseComments = new ArrayList<String>();

    private List<String> whereJoinClause = new ArrayList<String>();
    private Set<QueryDefinitionJoin> joins = new HashSet<QueryDefinitionJoin>();

    private List bindParams = new ArrayList();

    public QueryDefnStatementGenerator(final QueryDefinitionSelect select)
    {
        this.queryDefn = select.getQueryDefinition();
        this.select = select;
    }

    public QueryDefinitionSelect getQuerySelect()
    {
        return select;
    }

    public void addJoin(QueryDefinitionField field) throws QueryDefinitionException
    {
        QueryDefinitionJoin join = field.getJoin();
        this.addJoin(join, false, null);
    }

    public void addJoin(QueryDefinitionJoin join, boolean autoInc, QueryDefinitionJoin impliedBy) throws QueryDefinitionException
    {
        if(join == null || joins.contains(join))
            return;

        // HQL Rules: OUTER JOINS and INNER JOINS can be defined so at the FROM clause there could be some more HQL
        // text appended after the table name. There might be limitations on this!
        String fromExpr = join.getFromExpr();
        Set<String> joinList = new HashSet<String>();
        for (QueryDefinitionField field : select.getDisplayFields())
        {
            if (field.getHqlJoinExpr() != null && field.getJoin().equals(join))
                joinList.add(field.getHqlJoinExpr());
        }
        for (QueryDefnCondition cond : select.getConditions().getList())
        {
            if (cond.getField().getHqlJoinExpr() != null && cond.getField().getJoin().equals(join))
                joinList.add(cond.getField().getHqlJoinExpr());
        }
        for (String joinExpr : joinList)
        {
            fromExpr = fromExpr + " " + joinExpr;
        }
        fromClause.add(fromExpr);

        if(autoInc || impliedBy != null)
        {
            StringBuffer comments = new StringBuffer();
            comments.append("/* ");
            if(autoInc) comments.append("auto-included for join definition '").append(join.getName()).append("'");
            if(impliedBy != null)
            {
                if(autoInc) comments.append(", ");
                comments.append("implied by join definition '").append(impliedBy.getName()).append("'");
            }
            comments.append(" */");
            fromClauseComments.add(comments.toString());
        }
        else
            fromClauseComments.add(null);

        String whereCriteria = join.getCondition();
        if(whereCriteria != null)
            whereJoinClause.add(whereCriteria);
        joins.add(join);

        final List<QueryDefinitionJoin> impliedJoins = join.getImpliedJoins();
        if(impliedJoins != null && impliedJoins.size() > 0)
        {
            for(int i = 0; i < impliedJoins.size(); i++)
                addJoin(impliedJoins.get(i), autoInc, join);
        }
    }

    public DetachedCriteria generateCriteria(final ValueContext valueContext) throws QueryDefinitionException
    {
        DetachedCriteria criteria = DetachedCriteria.forClass(Person.class);

        Map<String, Criteria> queryCriterias = new HashMap<String, Criteria>();

        final List<QueryDefinitionField> showFields = select.getDisplayFields();
        if (showFields.size() > 0)
        {
            for (final QueryDefinitionField queryDefinitionField : showFields)
            {
                if (queryDefinitionField.isAssociationProperty())
                {
                    criteria.setFetchMode(queryDefinitionField.getEntityPropertyName(), FetchMode.JOIN);
                }
                String selClauseAndLabel = queryDefinitionField.getSelectClauseExprAndLabel();
                if (selClauseAndLabel != null)
                    selectClause.add(selClauseAndLabel);
                addJoin(queryDefinitionField);
            }
        }
        QueryDefinitionConditions allSelectConditions = select.getConditions();
        QueryDefinitionConditions usedSelectConditions = allSelectConditions.getUsedConditions(this, valueContext);
        for (QueryDefnCondition cond : usedSelectConditions.getList())
        {
            final QueryDefinitionField field = cond.getField();
            final SqlComparison comparison = cond.getComparison();

            if (field.isAssociationProperty())
            {
                Criterion criterion = null;
                if (comparison.getClass().equals(StartsWithComparisonIgnoreCase.class))
                    criterion = Restrictions.like(field.getEntityPropertyName(),
                            cond.getValueProvider().getValue().toString(), MatchMode.START).ignoreCase();
                else if (comparison.getClass().equals(EqualsComparison.class))
                    criterion = Restrictions.eq(field.getEntityPropertyName(), cond.getValueProvider().getValue());
                else
                    throw new QueryDefinitionException("Unknown comparison");
                criteria.createCriteria(field.getEntityPropertyName()).add(criterion);
            }
        }

        return criteria;
    }


    public String generateQuery(final ValueContext valueContext)  throws QueryDefinitionException
    {
        final List<QueryDefinitionField> showFields = select.getDisplayFields();
        if (showFields.size() > 0)
        {
            for (final QueryDefinitionField queryDefinitionField : showFields)
            {
                if (!queryDefinitionField.isTransientProperty())
                {
                    String selClauseAndLabel = queryDefinitionField.getSelectClauseExprAndLabel();
                    if (selClauseAndLabel != null)
                        selectClause.add(selClauseAndLabel);
                }
                addJoin(queryDefinitionField);
            }
        }
        else
            selectClause.add("*");

        QueryDefinitionConditions allSelectConditions = select.getConditions();
        QueryDefinitionConditions usedSelectConditions = allSelectConditions.getUsedConditions(this, valueContext);

        // add join tables which have the auto-include flag set and their respective conditions to the
        // from and where clause lists. If the join is already in the 'joins' list, no need to add it in.
        List autoIncJoinList = this.queryDefn.getJoins().getAutoIncludeJoins();
        for (final Object aAutoIncJoinList : autoIncJoinList)
        {
            this.addJoin((QueryDefinitionJoin) aAutoIncJoinList, true, null);
        }


        StringBuffer sql = new StringBuffer();

        /*
        int selectCount = selectClause.size();
        int selectLast = selectCount - 1;
        sql.append("select ");

        if(select.isDistinctRows())
            sql.append("distinct \n");
        else
            sql.append("\n");

        for(int sc = 0; sc < selectCount; sc++)
        {
            sql.append("  " + selectClause.get(sc));
            if(sc != selectLast)
                sql.append(", ");
            sql.append("\n");
        }
        */
        int fromCount = fromClause.size();
        int fromLast = fromCount - 1;
        sql.append("from \n");
        for(int fc = 0; fc < fromCount; fc++)
        {
            sql.append("  " + fromClause.get(fc));
            if(fc != fromLast)
                sql.append(",");
            sql.append("\n");
        }

        StringBuffer whereClauseSql = new StringBuffer();

        boolean haveJoinWheres = false;
        int whereCount = whereJoinClause.size();
        int whereLast = whereCount - 1;
        if(whereCount > 0)
        {
            whereClauseSql.append("where\n  (\n");
            for(int wc = 0; wc < whereCount; wc++)
            {
                whereClauseSql.append("  " + whereJoinClause.get(wc));
                if(wc != whereLast)
                    whereClauseSql.append(" and ");
                whereClauseSql.append("\n");
            }
            whereClauseSql.append("  )");
            haveJoinWheres = true;
        }


        boolean haveCondWheres = false;
        int usedCondsCount = usedSelectConditions.size();
        if(usedCondsCount > 0)
        {
            String conditionSql = usedSelectConditions.createSql(this, usedSelectConditions, valueContext);
            if(conditionSql != null && conditionSql.length() > 0)
            {
                if(haveJoinWheres)
                {
                    whereClauseSql.append(" and (\n");
                }
                else
                {
                    whereClauseSql.append("where\n  (\n");
                }
                whereClauseSql.append(conditionSql + "  )\n");
            }
            haveCondWheres = true;
        }

        /*
        QueryDefnSqlWhereExpressions whereExprs = select.getWhereExprs();
        if(whereExprs != null && whereExprs.size() > 0)
        {
            boolean first = false;
            if(!haveJoinWheres && !haveCondWheres)
            {
                whereClauseSql.append("where\n  (\n");
                first = true;
            }

            int whereExprsCount = whereExprs.size();
            for(int we = 0; we < whereExprsCount; we++)
            {
                QueryDefnSqlWhereExpression expr = whereExprs.get(we);
                if(first)
                    first = false;
                else
                    whereClauseSql.append(expr.getConnector());

                whereClauseSql.append(" (");
                whereClauseSql.append(expr.getWhereCondExpr(this, vc));
                whereClauseSql.append("  )\n");
            }
        }
        */

        // save this because some callers might need just the where clause
        sql.append(whereClauseSql);

        final Map<String, QueryDefinitionField> groupBys = select.getGroupByFields();
        int groupBysCount = groupBys.size();
        if(groupBysCount > 0)
        {
            int groupByLast = groupBysCount - 1;
            sql.append("group by\n");
            for(int gb = 0; gb < groupBysCount; gb++)
            {
                QueryDefinitionField field = groupBys.get(gb);
                sql.append("  " + field.getName());
                if(gb != groupByLast)
                {
                    sql.append(", ");
                }
                sql.append("\n");
            }

        }

        final List<QueryDefinitionSortBy> orderBys = select.getOrderBys();
        final int orderBySize = orderBys.size();
        if(orderBySize > 0)
        {
            sql.append("order by\n");
            for(int i = 0; i < orderBySize; i++)
            {
                QueryDefinitionSortBy sortRef = orderBys.get(i);
                if (sortRef.getExplicitOrderByClause() != null)
                {
                    sql.append(sortRef.getExplicitOrderByClause());
                }
                else
                {
                    sql.append(sortRef.getField().getOrderByClauseExpr());
                    if(sortRef.isDescending())
                        sql.append(" desc");
                }
                if(i < orderBySize-1)
                    sql.append(", ");
                sql.append("\n");
            }
        }

        return sql.toString();
    }

    public List getBindParams()
    {
        return bindParams;
    }

    public void addBindParam(final Object obj)
    {
        bindParams.add(obj);
    }

}
