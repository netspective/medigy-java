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
package com.medigy.presentation.form.common;

import com.medigy.presentation.model.common.ServiceCountAndListAction;
import com.medigy.service.SearchReturnValues;
import com.medigy.service.SearchService;
import com.medigy.service.SearchServiceParameters;
import com.medigy.service.ServiceVersion;
import com.medigy.service.dto.ServiceReturnValues;
import com.medigy.service.dto.query.SearchCondition;

import java.util.Arrays;
import java.util.List;

/**
 * Class implementing the ISelectCountAndListAction. It is based on invoking services implementing the
 *  {@link SearchService}.
 */
public class SearchServiceCountAndListAction extends ServiceCountAndListAction
{
    protected List<String> columnNames = null;


    public SearchServiceCountAndListAction(final SearchResultPanel panel, final SearchService service)
    {
        super(panel, service);
    }

    /**
     * Gets the column names returned from the invocation of {@link SearchService#search(com.medigy.service.SearchServiceParameters)}.
     *
     * @return  Null if none of the execute() methods have been called
     * @see com.medigy.service.SearchReturnValues#getColumnNames()
     */
    public List<String> getColumnNames()
    {
       return columnNames;
    }

    public Object execute(Object queryObject)
    {
        if (queryObject == null)
            return 0;

        ServiceReturnValues values = ((SearchService) getService()).search(createSearchServiceParameters(queryObject));
        if(values.getErrorMessage() != null)
        {
            return 0;
        }
        SearchReturnValues srv = (SearchReturnValues) values;
        columnNames = srv.getSearchResults() != null ? srv.getColumnNames() : null;
        return srv.getSearchResults().size();
    }

    public List execute(Object queryObject, final int startFromRow, int numberOfRows)
    {
        ServiceReturnValues values = ((SearchService) getService()).search(createSearchServiceParameters(queryObject, startFromRow, numberOfRows));
        if(values.getErrorMessage() != null)
        {
            return null;
        }
        SearchReturnValues srv = (SearchReturnValues) values;
        columnNames = srv.getSearchResults() != null ? srv.getColumnNames() : null;
        return srv.getSearchResults();
    }

    public SearchServiceParameters createSearchServiceParameters(final Object queryObject)
    {
        final CriteriaSearchFormModelObject formModelObject = (CriteriaSearchFormModelObject) queryObject;
        return new SearchServiceParameters()  {
            public List<SearchCondition> getConditions()
            {
                final SearchCondition searchCondition = new SearchCondition();
                searchCondition.setCondition(formModelObject.getSearchCriterias());
                searchCondition.setFieldValue(formModelObject.getSearchCriteriaValue());
                return Arrays.asList(searchCondition);
            }

            public List<String> getOrderBys()
            {
                return null;
            }

            public List<String> getDisplayFields()
            {
                return null;
            }

            public int getStartFromRow()
            {
                return 0;
            }

            public ServiceVersion getServiceVersion()
            {
                return null;
            };
        };
    }

    public SearchServiceParameters createSearchServiceParameters(final Object queryObject, final int startFromRow, int numberOfRows)
    {
        final CriteriaSearchFormModelObject formModelObject = (CriteriaSearchFormModelObject) queryObject;
        return new SearchServiceParameters()  {
            public List<SearchCondition> getConditions()
            {
                final SearchCondition searchCondition = new SearchCondition();
                searchCondition.setCondition(formModelObject.getSearchCriterias());
                searchCondition.setFieldValue(formModelObject.getSearchCriteriaValue());
                return Arrays.asList(searchCondition);
            }

            public List<String> getDisplayFields()
            {
                return null;
            }

            public List<String> getOrderBys()
            {
                return null;
            }

            public int getStartFromRow()
            {
                return startFromRow;
            }

            public ServiceVersion getServiceVersion()
            {
                return null;
            }
        };
    }
}
