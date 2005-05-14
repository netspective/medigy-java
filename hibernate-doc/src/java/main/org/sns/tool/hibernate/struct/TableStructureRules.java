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

import org.hibernate.mapping.ForeignKey;
import org.hibernate.mapping.Table;

public interface TableStructureRules
{
    /**
     * Return true if the referenced table in the foreign key is a "parent" of the referencee.
     * @param foreignKey The hibernate foreign key mapping
     * @return true if the provided fkey is a parent relationship, false otherwise
     */
    public boolean isParentRelationship(final TableStructure structure, final ForeignKey foreignKey);

    /**
     * Return true if the referenced table in the foreign key is a "parent" of the provided table.
     * @param foreignKey The hibernate foreign key mapping
     * @return true if the provided fkey has a parent relationship to given table, false otherwise
     */
    public boolean isParentRelationship(final TableStructure structure, final ForeignKey foreignKey, final Table table);

    /**
     * Ascertain whether the referenced class in the foreign key relationship is a superclass of the source class.
     * @param foreignKey The foreign key relationship
     * @return True if the source of the foreign key is a subclass of the referenced class
     */
    public boolean isSubclassRelationship(final TableStructure structure, final ForeignKey foreignKey);

    /**
     * Ascertain whether the given table node belongs in a special category (like "reference" or "application").
     * @param tableNode The table in question
     * @return The categories to which the table belongs (NULLs are not allowed)
     */
    public TableCategory[] getTableCategories(final TableStructureNode tableNode);

    /**
     * Ascertain whether the given column node belongs in a special category (like "common" or "child-key").
     * @param columnDetail The column in question
     * @return The category to which the column belongs (NULLs are not allowed)
     */
    public ColumnCategory getColumnCategory(final ColumnDetail columnDetail);

    /**
     * Whenever the column categories will be used for grouping columns and the columns are used for presentation or
     * other list, this will be the sort order used.
     */
    public ColumnCategory[] getColumnCategoriesSortOrder(final TableStructure structure);

    /**
     * Given a column of a table, translate the data type if the default data type is not acceptable.
     * @param defaultDataType The datatype that was figured out from the dialect
     * @param columnDetail The table the column belongs to
     * @return
     */
    public String getTranslatedDataType(final String defaultDataType, final ColumnDetail columnDetail);

}
