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
package com.medigy.tool.persist.hibernate;

import java.util.Iterator;

import org.hibernate.mapping.Collection;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.ForeignKey;
import org.hibernate.mapping.PrimaryKey;
import org.hibernate.mapping.Table;
import org.sns.tool.hibernate.struct.TableCategory;
import org.sns.tool.hibernate.struct.TableStructure;
import org.sns.tool.hibernate.struct.TableStructureNode;
import org.sns.tool.hibernate.struct.TableStructureRules;

import com.medigy.persist.reference.ReferenceEntity;
import com.medigy.persist.reference.custom.CustomReferenceEntity;

public class HibernateStructureRules implements TableStructureRules
{
    private TableCategory defaultTableCategory = new TableCategoryImpl("Application Tables");
    private TableCategory refTableCategory = new TableCategoryImpl("Reference Tables");
    private TableCategory customRefTableCategory = new TableCategoryImpl("Custom Reference Tables");

    public HibernateStructureRules()
    {
    }

    public boolean isParentRelationship(final TableStructure structure, final ForeignKey foreignKey)
    {
        for (Iterator colls = structure.getConfiguration().getCollectionMappings(); colls.hasNext();)
        {
            final Collection coll = (Collection) colls.next();
            if (coll.isOneToMany())
            {
                if (foreignKey.getReferencedTable() == coll.getOwner().getTable() && foreignKey.getTable() == coll.getCollectionTable())
                    return true;
            }
        }

        return false;
    }

    public boolean isParentRelationship(final TableStructure structure, final ForeignKey foreignKey, final Table table)
    {
        return foreignKey.getReferencedTable() == table && isParentRelationship(structure, foreignKey);
    }

    public TableCategory[] getTableCategories(final TableStructureNode tableNode)
    {
        if(ReferenceEntity.class.isAssignableFrom(tableNode.getPersistentClass().getMappedClass()))
            return new TableCategory[] { refTableCategory };
        else if(CustomReferenceEntity.class.isAssignableFrom(tableNode.getPersistentClass().getMappedClass()))
            return new TableCategory[] { customRefTableCategory };
        else
            return new TableCategory[] { defaultTableCategory };
    }

    public String getTranslatedDataType(String defaultDataType, TableStructureNode tableNode, Column column, PrimaryKey partOfPrimaryKey, ForeignKey partOfForeignKey)
    {
        return defaultDataType;
    }

    protected class TableCategoryImpl implements TableCategory, Comparable
    {
        private String name;

        public TableCategoryImpl(String name)
        {
            this.name = name;
        }

        public String getCategoryName()
        {
            return name;
        }

        public int compareTo(Object o)
        {
            TableCategoryImpl other = (TableCategoryImpl) o;
            return name.compareTo(other.getCategoryName());
        }

        public boolean equals(Object o)
        {
            TableCategoryImpl other = (TableCategoryImpl) o;
            return name.equals(other.getCategoryName());
        }

        public String toString()
        {
            return getCategoryName();
        }
    }
}
