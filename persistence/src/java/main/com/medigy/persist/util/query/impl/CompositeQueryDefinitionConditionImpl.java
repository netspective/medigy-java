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
package com.medigy.persist.util.query.impl;

import com.medigy.persist.util.query.AbstractQueryDefinitionCondition;
import com.medigy.persist.util.query.CompositeQueryDefinitionCondition;
import com.medigy.persist.util.query.QueryDefnCondition;
import com.medigy.persist.util.query.QueryDefnStatementGenerator;
import com.medigy.persist.util.query.exception.QueryDefinitionException;
import com.medigy.persist.util.value.ValueContext;

import java.util.ArrayList;
import java.util.List;

public class CompositeQueryDefinitionConditionImpl extends AbstractQueryDefinitionCondition implements CompositeQueryDefinitionCondition
{
    private List<QueryDefnCondition> childConditions = new ArrayList<QueryDefnCondition>();

    public CompositeQueryDefinitionConditionImpl(final String name)
    {
        setName(name);
        setJoinOnly(true);
    }

    public List<QueryDefnCondition> getChildConditions()
    {
        return childConditions;
    }

    public void setChildConditions(final  List<QueryDefnCondition>  childConditions)
    {
        this.childConditions = childConditions;
    }

    public void addChildCondition(final QueryDefnCondition condition)
    {
        // if you're adding child conditions then this condition has no FIELD associated with it directly and should
        // only be used for JOIN purposes
        childConditions.add(condition);
    }

    public String getDisplayCaption()
    {
        StringBuffer buffer = new StringBuffer();
        for (int i=0; i < childConditions.size(); i++)
        {

            buffer.append(childConditions.get(i).getField().getCaption());
            if (i != childConditions.size()-1)
                buffer.append(", ");
        }
        return buffer.toString();
    }

    public boolean useCondition(final QueryDefnStatementGenerator stmtGen, final ValueContext valueContext) throws QueryDefinitionException
    {
        if (childConditions.size() == 0)
                return false;

        // if we don't allow nulls, always use the condition
        if(!isRemoveIfValueNull())
            return true;

        // by default all child conditions MUST be satisfied
        for (QueryDefnCondition cond : childConditions)
        {
            if (!cond.useCondition(stmtGen, valueContext))
                return false;
        }
        return true;
    }

}
