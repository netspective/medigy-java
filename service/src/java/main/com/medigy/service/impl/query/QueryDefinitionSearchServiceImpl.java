/*
 * Copyright (c) 2000-2003 Netspective Communications LLC. All rights reserved.
 *
 * Netspective Communications LLC ("Netspective") permits redistribution, modification and use of this file in source
 * and binary form ("The Software") under the Netspective Source License ("NSL" or "The License"). The following
 * conditions are provided as a summary of the NSL but the NSL remains the canonical license and must be accepted
 * before using The Software. Any use of The Software indicates agreement with the NSL.
 *
 * 1. Each copy or derived work of The Software must preserve the copyright notice and this notice unmodified.
 *
 * 2. Redistribution of The Software is allowed in object code form only (as Java .class files or a .jar file
 *    containing the .class files) and only as part of an application that uses The Software as part of its primary
 *    functionality. No distribution of the package is allowed as part of a software development kit, other library,
 *    or development tool without written consent of Netspective. Any modified form of The Software is bound by these
 *    same restrictions.
 *
 * 3. Redistributions of The Software in any form must include an unmodified copy of The License, normally in a plain
 *    ASCII text file unless otherwise agreed to, in writing, by Netspective.
 *
 * 4. The names "Netspective", "Axiom", "Commons", "Junxion", and "Sparx" are trademarks of Netspective and may not be
 *    used to endorse products derived from The Software without without written consent of Netspective. "Netspective",
 *    "Axiom", "Commons", "Junxion", and "Sparx" may not appear in the names of products derived from The Software
 *    without written consent of Netspective.
 *
 * 5. Please attribute functionality where possible. We suggest using the "powered by Netspective" button or creating
 *    a "powered by Netspective(tm)" link to http://www.netspective.com for each application using The Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT,
 * ARE HEREBY DISCLAIMED.
 *
 * NETSPECTIVE AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE OR ANY THIRD PARTY AS A
 * RESULT OF USING OR DISTRIBUTING THE SOFTWARE. IN NO EVENT WILL NETSPECTIVE OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THE SOFTWARE, EVEN
 * IF HE HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 */
package com.medigy.service.impl.query;

import com.medigy.persist.util.query.QueryDefinition;
import com.medigy.persist.util.query.QueryDefinitionField;
import com.medigy.service.AbstractQueryDefinitionSearchServiceImpl;
import com.medigy.service.SearchReturnValues;
import com.medigy.service.ServiceVersion;
import com.medigy.service.dto.ServiceParameters;
import com.medigy.service.dto.query.QueryDefinitionSearchFormPopulateValues;
import com.medigy.service.query.QueryDefinitionFactory;
import com.medigy.service.query.QueryDefinitionSearchService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryDefinitionSearchServiceImpl extends AbstractQueryDefinitionSearchServiceImpl implements QueryDefinitionSearchService
{
    private final Log log = LogFactory.getLog(QueryDefinitionSearchServiceImpl.class);

    public QueryDefinitionSearchServiceImpl(final SessionFactory sessionFactory,
                                            final Class<? extends QueryDefinition> searchClass,
                                            final String querySelectName)
    {
        super(sessionFactory, searchClass, querySelectName);
    }

    public QueryDefinition getQueryDefinition(final Class queryDefClass)
    {
        return QueryDefinitionFactory.getInstance().getQueryDefinition(queryDefClass);
    }

    public QueryDefinitionSearchFormPopulateValues getAvailableSearchParameters()
    {
        final QueryDefinition queryDefinition = QueryDefinitionFactory.getInstance().getQueryDefinition(getQueryDefinitionClass());
        if (queryDefinition == null)
            throw new RuntimeException("Unknown query definition: " + getQueryDefinitionClass());
        return new QueryDefinitionSearchFormPopulateValues() {

            public Map<String, QueryDefinitionField> getSortByFields()
            {
                return queryDefinition.getFields();
            }

            public List<String>getConnectors()
            {
                return queryDefinition.getConnectors();
            }

            public Map<String, QueryDefinitionField> getConditionFields()
            {
                return queryDefinition.getFields();
            }

            public Map<String,QueryDefinitionField> getDisplayFields()
            {
                Map<String, QueryDefinitionField> map = new HashMap<String, QueryDefinitionField>();
                List<QueryDefinitionField> fieldList = new ArrayList<QueryDefinitionField>(queryDefinition.getFields().values());
                for (QueryDefinitionField field: fieldList)
                {
                    if (field.isDisplayAllowed())
                        map.put(field.getName(), field);
                }
                return map;
            }

            public ServiceParameters getParameters()
            {
                return null;
            }

            public String getErrorMessage()
            {
                return null;
            }
        };
    }

    public ServiceVersion[] getSupportedServiceVersions()
    {
        return new ServiceVersion[0];
    }

    public String[] isValid(ServiceParameters parameters)
    {
        return new String[0];
    }

    public SearchReturnValues createErrorResponse(final ServiceParameters params, final String errorMessage)
    {
        return new SearchReturnValues() {
            public List<String> getColumnNames()
            {
                return null;
            }

            public ServiceParameters getParameters()
            {
                return  params;
            }

            public List<Map<String, Object>> getSearchResults()
            {
                return null;
            }

            public String getErrorMessage()
            {
                return errorMessage;
            }
        };
    }
}
