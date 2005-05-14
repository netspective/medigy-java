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
package org.sns.tool.hibernate.struct.impl;

import java.text.Collator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.hibernate.mapping.Column;
import org.hibernate.mapping.ForeignKey;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.PrimaryKey;
import org.hibernate.mapping.Table;
import org.sns.tool.hibernate.struct.ColumnCategory;
import org.sns.tool.hibernate.struct.ColumnDetail;
import org.sns.tool.hibernate.struct.TableCategory;
import org.sns.tool.hibernate.struct.TableStructure;
import org.sns.tool.hibernate.struct.TableStructureNode;
import org.sns.tool.hibernate.struct.TableStructureRules;

public class DefaultTableStructureNode implements TableStructureNode, Comparable
{
    private final TableStructure owner;
    private final TableStructureNode parentNode;
    private final Table nodeForTable;
    private final PersistentClass mappedClass;
    private final int level;
    private final TableStructureNode[] ancestorNodes;
    private TableStructureNode[] childNodes;
    private TableStructureNode[] dependencies;
    private ColumnDetail[] allColumns;
    private Map columnDetailsByCategory = new HashMap(); // key is a ColumnCategory instance, value is ColumnDetail[] array

    public DefaultTableStructureNode(final PersistentClass mappedClass, final Table nodeForTable, final TableStructure owner, final TableStructureNode parent, final int level)
    {
        this.mappedClass = mappedClass;
        this.nodeForTable = nodeForTable;
        this.owner = owner;
        this.parentNode = parent;
        this.level = level;

        final List ancestorNodes = new ArrayList();
        TableStructureNode activeParentNode = parentNode;
        while(activeParentNode != null)
        {
            if(ancestorNodes.size() == 0)
                ancestorNodes.add(activeParentNode);
            else
                ancestorNodes.add(0, activeParentNode);

            activeParentNode = activeParentNode.getParentNode();
        }

        this.ancestorNodes = (TableStructureNode[]) ancestorNodes.toArray(new TableStructureNode[ancestorNodes.size()]);
    }

    protected void createChildNodes()
    {
        final Set childNodes = new TreeSet();
        for (final Iterator classes = owner.getConfiguration().getClassMappings(); classes.hasNext(); )
        {
            final PersistentClass pclass = (PersistentClass) classes.next();
            final Table table = (Table) pclass.getTable();

            for (final Iterator fKeys = table.getForeignKeyIterator(); fKeys.hasNext();)
            {
                final ForeignKey foreignKey = (ForeignKey) fKeys.next();
                if(owner.getRules().isParentRelationship(owner, foreignKey, nodeForTable))
                {
                    final TableStructureNode childNode = owner.createNode(pclass, table, this, level + 1);
                    childNodes.add(childNode);
                    owner.categorize(childNode);
                }
            }
        }
        this.childNodes = (TableStructureNode[]) childNodes.toArray(new TableStructureNode[childNodes.size()]);

        for(final Iterator i = childNodes.iterator(); i.hasNext(); )
        {
            final TableStructureNode childNode = (TableStructureNode) i.next();
            if(childNode instanceof DefaultTableStructureNode)
                ((DefaultTableStructureNode) childNode).createChildNodes();
        }
    }

    protected void resolveDependencies()
    {
        final List dependencies = new ArrayList();

        for (final Iterator classes = owner.getConfiguration().getClassMappings(); classes.hasNext(); )
        {
            final PersistentClass pclass = (PersistentClass) classes.next();
            final Table table = (Table) pclass.getTable();

            for (final Iterator fKeys = table.getForeignKeyIterator(); fKeys.hasNext();)
            {
                final ForeignKey foreignKey = (ForeignKey) fKeys.next();
                final Table referencedTable = foreignKey.getReferencedTable();
                if(referencedTable == getTable())
                    dependencies.add(owner.getNodeForTable(referencedTable));
            }
        }

        this.dependencies = (TableStructureNode[]) dependencies.toArray(new TableStructureNode[dependencies.size()]);
    }

    protected void createColumnDetail()
    {
        final TableStructureRules rules = owner.getRules();
        final List allColumns = new ArrayList();
        final Map columnDetailsByCategory = new HashMap();
        final Table table = getTable();
        final PrimaryKey primaryKeyColumns = table.getPrimaryKey();

        for (final Iterator columns = table.getColumnIterator(); columns.hasNext();)
        {
            final Column column = (Column) columns.next();

            ForeignKey partOfForeignKey = null;
            for (Iterator fkIterator = table.getForeignKeyIterator(); fkIterator.hasNext();)
            {
                final ForeignKey fKey = (ForeignKey) fkIterator.next();
                if (fKey.containsColumn(column))
                {
                    partOfForeignKey = fKey;
                    break;
                }
            }

            final ColumnDetail detail;
            if (primaryKeyColumns.containsColumn(column))
                detail = new DefaultColumnDetail(this, column, primaryKeyColumns, partOfForeignKey);
            else
                detail = new DefaultColumnDetail(this, column, null, partOfForeignKey);

            allColumns.add(detail);

            final ColumnCategory category = detail.getColumnCategory();
            if(category != null)
            {
                List list = (List) columnDetailsByCategory.get(category);
                if(list == null)
                {
                    list = new ArrayList();
                    columnDetailsByCategory.put(category, list);
                }
                list.add(detail);
            }
        }

        this.allColumns = (ColumnDetail[]) allColumns.toArray(new ColumnDetail[allColumns.size()]);

        this.columnDetailsByCategory = new HashMap();
        for(final Iterator i = columnDetailsByCategory.entrySet().iterator(); i.hasNext(); )
        {
            final Map.Entry entry = (Map.Entry) i.next();
            final List list = (List) entry.getValue();
            final ColumnDetail[] categoryColumns = (ColumnDetail[]) list.toArray(new ColumnDetail[list.size()]);
            this.columnDetailsByCategory.put(entry.getKey(), categoryColumns);
        }
    }

    public int compareTo(Object o)
    {
        final TableStructureNode other = (TableStructureNode) o;
        return Collator.getInstance().compare(nodeForTable.getName().toUpperCase(), other.getTable().getName().toUpperCase());
    }

    public TableStructure getOwner()
    {
        return owner;
    }

    public int getLevel()
    {
        return level;
    }

    public Table getTable()
    {
        return nodeForTable;
    }

    public PersistentClass getPersistentClass()
    {
        return mappedClass;
    }

    public TableStructureNode getParentNode()
    {
        return parentNode;
    }

    public TableStructureNode[] getChildNodes()
    {
        return childNodes;
    }

    public ColumnDetail[] getAllColumns()
    {
        return allColumns;
    }

    public ColumnDetail[] getColumnsInCategory(final ColumnCategory columnCategory)
    {
        return (ColumnDetail[]) columnDetailsByCategory.get(columnCategory);
    }

    public ColumnCategory getColumnCategory(final String id)
    {
        for(final Iterator i = columnDetailsByCategory.keySet().iterator(); i.hasNext(); )
        {
            final ColumnCategory columnCategory = (ColumnCategory) i.next();
            if(columnCategory.getColumnCategoryId().equalsIgnoreCase(id))
                return columnCategory;
        }

        return null;
    }

    public TableStructureNode[] getAncestorNodes()
    {
        return ancestorNodes;
    }

    public TableStructureNode[] getDescendents()
    {
        final Set descendents = new HashSet();
        for(int i = 0; i < childNodes.length; i++)
        {
            final TableStructureNode childNode = childNodes[i];
            final TableStructureNode[] childDescendents = childNode.getDescendents();

            descendents.add(childNode);

            for(int j = 0; j < childDescendents.length; j++)
                descendents.add(childDescendents[j]);
        }

        return (TableStructureNode[]) descendents.toArray(new TableStructureNode[descendents.size()]);
    }

    public boolean hasChildren()
    {
        return childNodes.length > 0;
    }

    public TableStructureNode getLinkedNode()
    {
        return null;
    }

    public TableCategory[] getTableCategories()
    {
        return getOwner().getRules().getTableCategories(this);
    }

    public boolean isInCategory(final TableCategory category)
    {
        final TableCategory[] tableCategories = getTableCategories();
        for(int i = 0; i < tableCategories.length; i++)
        {
            final TableCategory tableCategory = tableCategories[i];
            if(tableCategory == category)
                return true;
        }

        return false;
    }

    public boolean isInCategory(final String categoryId)
    {
        final TableCategory[] tableCategories = getTableCategories();
        for(int i = 0; i < tableCategories.length; i++)
        {
            final TableCategory tableCategory = tableCategories[i];
            if(tableCategory.getTableCategoryId().equalsIgnoreCase(categoryId))
                return true;
        }

        return false;
    }

    public TableStructureNode[] getDependencies()
    {
        return dependencies;
    }

    public boolean isLinkedNode()
    {
        return false;
    }
}
