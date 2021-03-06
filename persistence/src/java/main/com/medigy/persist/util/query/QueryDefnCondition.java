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
package com.medigy.persist.util.query;

import com.medigy.persist.util.query.exception.QueryDefinitionException;
import com.medigy.persist.util.value.ValueContext;
import com.medigy.persist.util.value.ValueProvider;

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
    public String getName();
    public void setName(final String name);

    public String getDisplayCaption();

    public boolean isRemoveIfValueNull();   // may not be needed
    public void setIsRemoveIfValueNull(final boolean flag);

    public String getConnector();
    public void setConnector(final String connector);

    // =========== good for the single field one
    public QueryDefinitionField getField();
    public void setField(QueryDefinitionField field);
    public String getBindExpr();
    public void setBindExpr(String expr);
    // ============

    public SqlComparison getComparison();
    public void setComparison(SqlComparison comp);

    public boolean isJoinOnly();
    public void setJoinOnly(boolean flag);

    public QueryDefnCondition getParentCondition();
    public void setParentCondition(QueryDefnCondition condition);

    public QueryDefinition getQueryDefinition();
    public void setQueryDefinition(QueryDefinition defn);

    public QueryDefinitionSelect getQueryDefinitionSelect();
    public void setQueryDefinitionSelect(QueryDefinitionSelect select);

    public ValueProvider getValueProvider();
    public void setValueProvider(ValueProvider provider);

    public boolean useCondition(final QueryDefnStatementGenerator stmtGen, final ValueContext valueContext) throws QueryDefinitionException;
}
