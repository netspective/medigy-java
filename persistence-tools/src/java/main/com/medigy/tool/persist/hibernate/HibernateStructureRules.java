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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.hibernate.mapping.Collection;
import org.hibernate.mapping.ForeignKey;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Table;
import org.sns.tool.hibernate.struct.ColumnCategory;
import org.sns.tool.hibernate.struct.ColumnDetail;
import org.sns.tool.hibernate.struct.TableCategory;
import org.sns.tool.hibernate.struct.TableStructure;
import org.sns.tool.hibernate.struct.TableStructureNode;
import org.sns.tool.hibernate.struct.TableStructureRules;

import com.medigy.persist.reference.ReferenceEntity;
import com.medigy.persist.reference.custom.CustomReferenceEntity;

public class HibernateStructureRules implements TableStructureRules
{
    private final TableCategory defaultTableCategory = new TableCategoryImpl("app-table", "Application Tables", true);
    private final TableCategory refTableCategory = new TableCategoryImpl("reference-table", "Reference Tables", false);
    private final TableCategory customRefTableCategory = new TableCategoryImpl("custom-reference-table", "Custom Reference Tables", false);
    private final TableCategory housekeepingTableCategory = new TableCategoryImpl("housekeeping-table", "Housekeeping Tables", false);

    private final ColumnCategory pkColumnsCategory = new ColumnCategoryImpl("primary-key-column", "Primary Key Columns", true);
    private final ColumnCategory childKeyColumnsCategory = new ColumnCategoryImpl("child-key-column", "Child Key Columns", true);
    private final ColumnCategory defaultColumnCategory = new ColumnCategoryImpl("application-column", "Application Columns", true);
    private final ColumnCategory housekeepingColumnsCategory = new ColumnCategoryImpl("housekeeping-column", "Housekeeping Columns", false);
    private final ColumnCategory[] columnCategoriesSortOrder = new ColumnCategory[] { pkColumnsCategory, childKeyColumnsCategory, defaultColumnCategory, housekeepingColumnsCategory };

    private final Set housekeepingColumnNames = new HashSet();
    private final Set housekeepingTableNames = new HashSet();

    public HibernateStructureRules()
    {
        housekeepingTableNames.add("Work_Session");

        housekeepingColumnNames.add("lock_version");
        housekeepingColumnNames.add("create_timestamp");
        housekeepingColumnNames.add("update_timestamp");
        housekeepingColumnNames.add("record_status");
        housekeepingColumnNames.add("create_session_id");
        housekeepingColumnNames.add("update_session_id");
        housekeepingColumnNames.add("create_version_id");
        housekeepingColumnNames.add("update_version_id");
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

    public boolean isSubclassRelationship(final TableStructure structure, final ForeignKey foreignKey)
    {
        PersistentClass sourceClass = structure.getClassForTable(foreignKey.getTable());
        PersistentClass refClass = structure.getClassForTable(foreignKey.getReferencedTable());
        return refClass.getMappedClass().isAssignableFrom(sourceClass.getMappedClass());
    }

    public TableCategory[] getTableCategories(final TableStructureNode tableNode)
    {
        if(housekeepingTableNames.contains(tableNode.getTable().getName()))
            return new TableCategory[] { housekeepingTableCategory };
        else if(ReferenceEntity.class.isAssignableFrom(tableNode.getPersistentClass().getMappedClass()))
            return new TableCategory[] { refTableCategory };
        else if(CustomReferenceEntity.class.isAssignableFrom(tableNode.getPersistentClass().getMappedClass()))
            return new TableCategory[] { customRefTableCategory };
        else
            return new TableCategory[] { defaultTableCategory };
    }

    public ColumnCategory getColumnCategory(final ColumnDetail columnDetail)
    {
        if(columnDetail.isPrimaryKey())
            return pkColumnsCategory;
        else if(columnDetail.isChildKeyOfParent())
            return childKeyColumnsCategory;
        else if(housekeepingColumnNames.contains(columnDetail.getColumn().getName()))
            return housekeepingColumnsCategory;
        else
            return defaultColumnCategory;
    }

    public ColumnCategory[] getColumnCategoriesSortOrder(final TableStructure structure)
    {
        return columnCategoriesSortOrder;
    }

    public String getTranslatedDataType(final String defaultDataType, final ColumnDetail columnDetail)
    {
        return defaultDataType;
    }

    protected class TableCategoryImpl implements TableCategory, Comparable
    {
        private String id;
        private String label;
        private boolean generateDiagrams;

        public TableCategoryImpl(final String id, final String label, final boolean generateDiagrams)
        {
            this.id = id;
            this.label = label;
            this.generateDiagrams = generateDiagrams;
        }

        public String getTableCategoryId()
        {
            return id;
        }

        public String getTableCategoryLabel()
        {
            return label;
        }

        public boolean isGenerateDiagrams()
        {
            return generateDiagrams;
        }

        public int compareTo(Object o)
        {
            TableCategoryImpl other = (TableCategoryImpl) o;
            return id.compareTo(other.getTableCategoryId());
        }

        public boolean equals(Object o)
        {
            TableCategoryImpl other = (TableCategoryImpl) o;
            return id.equals(other.getTableCategoryId());
        }

        public String toString()
        {
            return getTableCategoryId();
        }
    }

    protected class ColumnCategoryImpl implements ColumnCategory, Comparable
    {
        private String id;
        private String label;
        private boolean includeInDiagrams;

        public ColumnCategoryImpl(final String id, final String label, final boolean includeInDiagrams)
        {
            this.id = id;
            this.label = label;
            this.includeInDiagrams = includeInDiagrams;
        }

        public ColumnCategoryImpl(String name)
        {
            this.id = name;
        }

        public String getColumnCategoryId()
        {
            return id;
        }

        public String getColumnCategoryLabel()
        {
            return label;
        }

        public boolean isIncludeInDiagrams()
        {
            return includeInDiagrams;
        }

        public int compareTo(Object o)
        {
            TableCategoryImpl other = (TableCategoryImpl) o;
            return id.compareTo(other.getTableCategoryId());
        }

        public boolean equals(Object o)
        {
            TableCategoryImpl other = (TableCategoryImpl) o;
            return id.equals(other.getTableCategoryId());
        }

        public String toString()
        {
            return getColumnCategoryId();
        }
    }
}
