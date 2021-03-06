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

import java.util.Map;
import java.util.Set;

import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.Mapping;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Table;

public interface TableStructure
{
    /**
     * Return the hibernate configuration for which structural information was gathered
     */
    public Configuration getConfiguration();

    /**
     * The rules used to govern the structure.
     */
    public TableStructureRules getRules();

    /**
     * Get all the children for the tree -- each item in the set is a TableStructureNode that comprises the "main"
     * tables (entry points) for the hierarchy.
     */
    public Set getTopLevelTableNodes();

    /**
     * Obtain the table structure node for a particular table.
     */
    public TableStructureNode getNodeForTable(final Table table);

    /**
     * Using the rules provided in TableStructureRules, categorize the tableNode to put it into its appropriate
     * buckets.
     */
    public void categorize(final TableStructureNode tableNode);

    /**
     * Return the map of table categories whose key is the TableCategory instance and the values are sets of
     * TableStructureNode instances that belong to those categories.
     */
    public Map getTableCategories();

    /**
     * Retrieve the table category with the given id.
     * @return Null if no table category with id "id" exists, TableCategory instance otherwise
     */
    public TableCategory getTableCategory(final String id);

    /**
     * Create a new node or link to an existing one.
     * @return The new node which might be a real structure node or a link to an existing one if it's a duplicate
     */
    public TableStructureNode createNode(final PersistentClass mappedClass, final Table nodeForTable, final TableStructureNode parent, final int level);

    /**
     * Get the map that provides a link from a Table to its PersistentClass instance.
     */
    public Map getTableToClassMap();

    /**
     * Given a table, get the class that it's mapped to
     */
    public PersistentClass getClassForTable(final Table table);

    /**
     * Obtain the dialect we'll be using to do data type mapping
     */
    public Dialect getDialect();

    /**
     * Obtain the mapping we'll be using to do data type mapping
     */
    public Mapping getMapping();
}
