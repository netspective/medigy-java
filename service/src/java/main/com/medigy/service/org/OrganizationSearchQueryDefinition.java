/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.org;

import com.medigy.persist.model.org.Organization;
import com.medigy.persist.util.query.QueryDefinition;
import com.medigy.persist.util.query.QueryDefinitionField;
import com.medigy.persist.util.query.QueryDefinitionJoin;
import com.medigy.persist.util.query.QueryDefinitionSelect;
import com.medigy.persist.util.query.QueryDefinitionSortBy;
import com.medigy.persist.util.query.QueryDefnCondition;
import com.medigy.persist.util.query.SqlComparisonFactory;
import com.medigy.persist.util.query.comparison.EqualsComparison;
import com.medigy.persist.util.query.comparison.StartsWithComparisonIgnoreCase;
import com.medigy.persist.util.query.exception.QueryDefinitionException;
import com.medigy.persist.util.query.impl.BasicQueryDefinition;
import com.medigy.persist.util.query.impl.QueryDefinitionConditionImpl;
import com.medigy.persist.util.query.impl.QueryDefinitionJoinImpl;
import com.medigy.persist.util.query.impl.QueryDefinitionSelectImpl;

public class OrganizationSearchQueryDefinition extends BasicQueryDefinition implements QueryDefinition
{
    public static final String QUERY_DEFINITION_NAME = "organizationSearch";
    public static final String CRITERIA_SEARCH_SELECT = "criteriaSearch";

    public enum Field
    {
        ORG_NAME("orgName"),
        ORG_ID("orgId");

        private String name;
        Field(final String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return name;
        }
    }

    public OrganizationSearchQueryDefinition()
    {
        super(QUERY_DEFINITION_NAME);
        register();
    }

    protected void register()
    {
        final QueryDefinitionJoin orgJoin = new QueryDefinitionJoinImpl("org", Organization.class, this);
        orgJoin.setAutoInclude(true);

        this.addJoin(orgJoin);

        addField("orgName", "partyName", "Primary Name", orgJoin);
        addField("orgId", "partyId","Organization ID", orgJoin);

        try
        {
            addSelect(registerCriteriaSearch());
        }
        catch (QueryDefinitionException e)
        {
            throw new RuntimeException(e);
        }
    }

    protected QueryDefinitionSelect registerCriteriaSearch() throws QueryDefinitionException
    {
        final QueryDefinitionSelect select = new QueryDefinitionSelectImpl(CRITERIA_SEARCH_SELECT, this);

        select.addDisplayField(getField(Field.ORG_NAME.getName()));
        select.addDisplayField(getField(Field.ORG_ID.getName()));
        // TODO: Add org type criteria

        final QueryDefnCondition orgNameCondition = new QueryDefinitionConditionImpl(getField(Field.ORG_NAME.getName()),
                SqlComparisonFactory.getInstance().getComparison(StartsWithComparisonIgnoreCase.COMPARISON_NAME));
        orgNameCondition.setConnector("and");
        select.addCondition(orgNameCondition);

        final QueryDefnCondition idCondition = new QueryDefinitionConditionImpl(getField(Field.ORG_ID.getName()),
                SqlComparisonFactory.getInstance().getComparison(EqualsComparison.COMPARISON_NAME));
        idCondition.setConnector("and");
        select.addCondition(idCondition);

        final QueryDefinitionField idField = getField(Field.ORG_NAME.getName());
        select.addOrderBy(new QueryDefinitionSortBy() {
            public boolean isDescending()
            {
                return false;
            }

            public QueryDefinitionField getField()
            {
                return idField;
            }

            public String getExplicitOrderByClause()
            {
                return null;
            }
        });
        return select;
    }
}
