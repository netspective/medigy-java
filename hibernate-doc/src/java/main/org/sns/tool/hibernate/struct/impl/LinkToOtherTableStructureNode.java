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
import java.util.List;

import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Table;
import org.sns.tool.hibernate.struct.ColumnCategory;
import org.sns.tool.hibernate.struct.ColumnDetail;
import org.sns.tool.hibernate.struct.TableCategory;
import org.sns.tool.hibernate.struct.TableStructure;
import org.sns.tool.hibernate.struct.TableStructureNode;

public class LinkToOtherTableStructureNode implements TableStructureNode, Comparable
{
    private final TableStructureNode parentNode;
    private final TableStructureNode linkedNode;
    private final int level;
    private final TableStructureNode[] ancestorNodes;

    public LinkToOtherTableStructureNode(final TableStructureNode linkedNode, final TableStructureNode parentNode, final int level)
    {
        this.linkedNode = linkedNode;
        this.parentNode = parentNode;
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

    public int compareTo(Object o)
    {
        final TableStructureNode other = (TableStructureNode) o;
        return Collator.getInstance().compare(this.getTable().getName().toUpperCase(), other.getTable().getName().toUpperCase());

    }

    public TableStructure getOwner()
    {
        return linkedNode.getOwner();
    }

    public int getLevel()
    {
        return level;
    }

    public Table getTable()
    {
        return linkedNode.getTable();
    }

    public PersistentClass getPersistentClass()
    {
        return linkedNode.getPersistentClass();
    }

    public TableStructureNode getParentNode()
    {
        return parentNode;
    }

    public TableStructureNode[] getChildNodes()
    {
        return new TableStructureNode[0];
    }

    public TableStructureNode[] getAncestorNodes()
    {
        return ancestorNodes;
    }

    public boolean hasChildren()
    {
        return getChildNodes().length > 0;
    }

    public boolean isLinkedNode()
    {
        return true;
    }

    public TableStructureNode getLinkedNode()
    {
        return linkedNode;
    }

    public ColumnDetail[] getAllColumns()
    {
        return linkedNode.getAllColumns();
    }

    public ColumnDetail[] getColumnsInCategory(final ColumnCategory columnCategory)
    {
        return linkedNode.getColumnsInCategory(columnCategory);
    }

    public TableStructureNode[] getDependencies()
    {
        return linkedNode.getDependencies();
    }

    public ColumnCategory getColumnCategory(final String id)
    {
        return linkedNode.getColumnCategory(id);
    }

    public TableCategory[] getTableCategories()
    {
        return linkedNode.getTableCategories();
    }

    public TableStructureNode[] getDescendents()
    {
        return new TableStructureNode[0];
    }

    public boolean isInCategory(final TableCategory category)
    {
        return linkedNode.isInCategory(category);
    }

    public boolean isInCategory(final String categoryId)
    {
        return linkedNode.isInCategory(categoryId);
    }
}
