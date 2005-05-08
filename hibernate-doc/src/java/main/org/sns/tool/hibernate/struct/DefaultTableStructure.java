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
 * @author Shahid N. Shah
 */

/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package org.sns.tool.hibernate.struct;

import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.text.Collator;

import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Table;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.ForeignKey;

public class DefaultTableStructure implements TableStructure
{
    /**
     * Map of tables and the persistent classes they represent.
     */
    private final Map tableToClassMap = new HashMap();

    /**
     * List of categories -- key is a string, value is a list of TableStructureNode instances
     */
    private final Map tableCategories = new TreeMap();

    /**
     * The database policy for which we're generating the schema generator; null to generate db-independent ERD
     */
    private final Configuration configuration;

    /**
     * The rules we're going to use to determine the structure
     */
    private final TableStructureRules rules;

    /**
     * The tables that have no parents (top level tables with level = 1).
     */
    private final List topLevelTableNodes = new ArrayList();

    public DefaultTableStructure(final Configuration configuration, final TableStructureRules rules)
    {
        this.configuration = configuration;
        this.configuration.buildMappings();
        this.rules = rules;

        final Set sortedChildren = new TreeSet(new TableTreeNodeComparator());
        for (final Iterator classes = configuration.getClassMappings(); classes.hasNext();)
        {
            final PersistentClass pclass = (PersistentClass) classes.next();
            final Table table = (Table) pclass.getTable();

            tableToClassMap.put(table, pclass);

            boolean isChildTable = false;
            for (final Iterator fKeys = table.getForeignKeyIterator(); fKeys.hasNext();)
            {
                final ForeignKey foreignKey = (ForeignKey) fKeys.next();
                if(rules.isParentRelationship(this, foreignKey))
                {
                    isChildTable = true;
                    break;
                }
            }

            if(! isChildTable)
            {
                final TableStructureNode topLevelNode = new DefaultTableStructureNode(pclass, table, this, null, 0);
                sortedChildren.add(topLevelNode);
                categorize(topLevelNode);
            }
        }
        topLevelTableNodes.addAll(sortedChildren);
    }

    protected void categorize(final TableStructureNode tableNode, final TableCategory category)
    {
        List entries = (List) tableCategories.get(category);
        if(entries == null)
        {
            entries = new ArrayList();
            tableCategories.put(category, entries);
        }

        entries.add(tableNode);
    }

    public void categorize(final TableStructureNode tableNode)
    {
        final TableCategory[] categories = rules.getTableCategories(tableNode);
        for(int i = 0; i < categories.length; i++)
        {
            final TableCategory category = categories[i];
            categorize(tableNode, category);
        }
    }

    public Configuration getConfiguration()
    {
        return configuration;
    }

    public TableStructureRules getRules()
    {
        return rules;
    }

    public List getTopLevelTableNodes()
    {
        return topLevelTableNodes;
    }

    public Map getTableToClassMap()
    {
        return tableToClassMap;
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();

        for(int c = 0; c < topLevelTableNodes.size(); c++)
            sb.append(topLevelTableNodes.get(c));

        return sb.toString();
    }

    public static class TableComparator implements Comparator
    {
        public int compare(Object o1, Object o2)
        {
            Table t1 = (Table) o1;
            Table t2 = (Table) o2;

            return Collator.getInstance().compare(t1.getName().toUpperCase(), t2.getName().toUpperCase());
        }
    }

    public static class TableTreeNodeComparator implements Comparator
    {
        public int compare(Object o1, Object o2)
        {
            TableStructureNode n1 = (TableStructureNode) o1;
            TableStructureNode n2 = (TableStructureNode) o2;

            return Collator.getInstance().compare(n1.getTable().getName().toUpperCase(), n2.getTable().getName().toUpperCase());
        }
    }

    public Map getTableCategories()
    {
        return tableCategories;
    }

}

