/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.org;

import com.medigy.persist.util.query.impl.BasicQueryDefinition;
import com.medigy.persist.util.query.QueryDefinitionJoin;
import com.medigy.persist.util.query.QueryDefinition;
import com.medigy.persist.model.org.Organization;

import java.util.List;

public class OrganizationSearchQueryDefinition extends BasicQueryDefinition
{
    public static final String QUERY_DEFINITION_NAME = "organizationSearch";

    public static final String ORG_NAME_FIELD = "orgName";
    public static final String ORG_ID_FIELD = "orgId";

    public OrganizationSearchQueryDefinition()
    {
        super(QUERY_DEFINITION_NAME);
        register();
    }

    protected void register()
    {
        final QueryDefinitionJoin orgJoin = new QueryDefinitionJoin() {
            public QueryDefinition getOwner()
            {
                return null;
            }

            public String getName()
            {
                return "org";
            }

            public String getFromExpr()
            {
                return getTable() + " " + getName();
            }

            public String getCondition()
            {
                return null;
            }

            public boolean isAutoInclude()
            {
                return false;
            }

            public String getImplyJoinsStr()
            {
                return null;
            }

            public List<QueryDefinitionJoin> getImpliedJoins()
            {
                return null;
            }

            public String getTable()
            {
                return Organization.class.getSimpleName();
            }
        };

        this.addJoin(orgJoin);

        addField(ORG_NAME_FIELD, "partyName", "Primary Name", orgJoin);
        addField(ORG_ID_FIELD, "organizationId","Organization ID", orgJoin);
    }
}
