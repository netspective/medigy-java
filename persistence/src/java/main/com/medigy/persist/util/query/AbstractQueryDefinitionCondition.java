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

import com.medigy.persist.util.value.ValueProvider;

public abstract class AbstractQueryDefinitionCondition implements QueryDefnCondition
{
    private String name;
    private QueryDefinitionField field;
    private SqlComparison comparison;
    private boolean isJoinOnly = false;
    private String bindExpr;
    private String connector;
    private QueryDefnCondition parentCondition;
    private QueryDefinition queryDefinition;
    private QueryDefinitionSelect queryDefinitionSelect;
    private boolean isRemoveIfValueNull = true;
    private ValueProvider valueProvider;

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public void setBindExpr(final String bindExpr)
    {
        this.bindExpr = bindExpr;
    }

    public void setComparison(final SqlComparison comparison)
    {
        this.comparison = comparison;
    }

    public void setField(final QueryDefinitionField field)
    {
        this.field = field;
    }

    public void setJoinOnly(final boolean joinOnly)
    {
        isJoinOnly = joinOnly;
    }

    public void setRemoveIfValueNull(final boolean removeIfValueNull)
    {
        isRemoveIfValueNull = removeIfValueNull;
    }

    public void setParentCondition(final QueryDefnCondition parentCondition)
    {
        this.parentCondition = parentCondition;
    }

    public boolean isRemoveIfValueNull()
    {
        return isRemoveIfValueNull;
    }

    public void setIsRemoveIfValueNull(final boolean flag)
    {
        this.isRemoveIfValueNull = flag;
    }

    public String getConnector()
    {
        return connector;
    }

    public void setConnector(final String connector)
    {
        this.connector = connector;
    }

    public QueryDefinitionField getField()
    {
        return field;
    }

    public String getBindExpr()
    {
        return bindExpr;
    }

    public SqlComparison getComparison()
    {
        return comparison;
    }

    public boolean isJoinOnly()
    {
        return isJoinOnly;
    }

    public ValueProvider getValueProvider()
    {
        return valueProvider;
    }

    public void setValueProvider(ValueProvider provider)
    {
        this.valueProvider = provider;
    }

    public QueryDefnCondition getParentCondition()
    {
        return parentCondition;
    }

    public QueryDefinition getQueryDefinition()
    {
        return queryDefinition;
    }

    public void setQueryDefinition(final QueryDefinition queryDefinition)
    {
        this.queryDefinition = queryDefinition;
    }

    public QueryDefinitionSelect getQueryDefinitionSelect()
    {
        return queryDefinitionSelect;
    }

    public void setQueryDefinitionSelect(final QueryDefinitionSelect queryDefinitionSelect)
    {
        this.queryDefinitionSelect = queryDefinitionSelect;
    }

}
