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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.hibernate.MappingException;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.Mapping;
import org.hibernate.mapping.ForeignKey;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.Table;
import org.hibernate.type.Type;
import org.sns.tool.hibernate.document.diagram.HibernateDiagramGeneratorException;
import org.sns.tool.hibernate.struct.TableCategory;
import org.sns.tool.hibernate.struct.TableStructure;
import org.sns.tool.hibernate.struct.TableStructureNode;
import org.sns.tool.hibernate.struct.TableStructureRules;

public class DefaultTableStructure implements TableStructure
{
    /**
     * Map of categories -- key is a TableCategory instance, value is a set of TableStructureNode instances
     */
    private final Map tableCategories = new TreeMap();

    /**
     * Map of node structures by table. Key is the table name, value is a TableStructureNode instance. In the entire
     * structure there should only be a single physical copy of a table structure node -- all other instances must be
     * links to the primary one (wherever it happens to be in the tree).
     */
    private final Map tableNodes = new HashMap();

    /**
     * Map of Tables and their associated PersistentClass mapped classes.
     */
    private final Map tableToClassMap = new HashMap();

    /**
     * The database policy for which we're generating the schema generator; null to generate db-independent ERD
     */
    private final Configuration configuration;

    /**
     * The rules we're going to use to determine the structure
     */
    private final TableStructureRules rules;

    /**
     * The tables that have no parents (top level tables with level = 0).
     */
    private final Set topLevelTableNodes = new TreeSet();

    /**
     * The dialect we will use when obtaining the data types
     */
    private final Dialect dialect;

    /**
     * The mappings we're going to use for getting the data types and related information.
     */
    private final Mapping mapping;

    public DefaultTableStructure(final Configuration configuration, final TableStructureRules rules)
    {
        this.configuration = configuration;
        this.configuration.buildMappings();
        this.rules = rules;

        for (final Iterator classes = configuration.getClassMappings(); classes.hasNext();)
        {
            final PersistentClass pclass = (PersistentClass) classes.next();
            final Table table = (Table) pclass.getTable();

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
                final TableStructureNode topLevelNode = createNode(pclass, table, null, 0);
                topLevelTableNodes.add(topLevelNode);
                categorize(topLevelNode);
            }
        }

        for (final Iterator classes = configuration.getClassMappings(); classes.hasNext();)
        {
            final PersistentClass pclass = (PersistentClass) classes.next();
            final Table table = (Table) pclass.getTable();

            tableToClassMap.put(table, pclass);
        }

        // the following was copied from org.hibernate.cfg.Configuration.buildMapping() because buildMapping() was private
        this.mapping = new Mapping()
        {
            /**
             * Returns the identifier type of a mapped class
             */
            public Type getIdentifierType(String persistentClass) throws MappingException
            {
                final PersistentClass pc = configuration.getClassMapping(persistentClass);
                if (pc == null) throw new MappingException("persistent class not known: " + persistentClass);
                return pc.getIdentifier().getType();
            }

            public String getIdentifierPropertyName(String persistentClass) throws MappingException
            {
                final PersistentClass pc = configuration.getClassMapping(persistentClass);
                if (pc == null) throw new MappingException("persistent class not known: " + persistentClass);
                if (!pc.hasIdentifierProperty()) return null;
                return pc.getIdentifierProperty().getName();
            }

            public Type getPropertyType(String persistentClass, String propertyName) throws MappingException
            {
                final PersistentClass pc = configuration.getClassMapping(persistentClass);
                if (pc == null) throw new MappingException("persistent class not known: " + persistentClass);
                Property prop = pc.getProperty(propertyName);
                if (prop == null) throw new MappingException("property not known: " + persistentClass + '.' + propertyName);
                return prop.getType();
            }
        };

        String dialectName = configuration.getProperty(Environment.DIALECT);
        if (dialectName == null)
            dialectName = org.hibernate.dialect.GenericDialect.class.getName();

        try
        {
            final Class cls = Class.forName(dialectName);
            this.dialect = (Dialect) cls.newInstance();
        }
        catch (Exception e)
        {
            throw new HibernateDiagramGeneratorException(e);
        }

        resolveDependencies();
    }

    protected void resolveDependencies()
    {
        for(final Iterator i = topLevelTableNodes.iterator(); i.hasNext(); )
        {
            final DefaultTableStructureNode node = (DefaultTableStructureNode) i.next();
            node.createChildNodes();
        }

        for(final Iterator i = tableNodes.values().iterator(); i.hasNext(); )
        {
            final DefaultTableStructureNode node = (DefaultTableStructureNode) i.next();
            node.resolveDependencies();
        }

        for(final Iterator i = tableNodes.values().iterator(); i.hasNext(); )
        {
            final DefaultTableStructureNode node = (DefaultTableStructureNode) i.next();
            node.createColumnDetail();
        }
    }

    public TableStructureNode getNodeForTable(Table table)
    {
        return (TableStructureNode) tableNodes.get(table.getName().toUpperCase());
    }

    protected void categorize(final TableStructureNode tableNode, final TableCategory category)
    {
        Set entries = (Set) tableCategories.get(category);
        if(entries == null)
        {
            entries = new TreeSet();
            tableCategories.put(category, entries);
        }

        entries.add(tableNode);
    }

    public void categorize(final TableStructureNode tableNode)
    {
        final TableCategory[] categories = tableNode.getTableCategories();
        if(categories != null)
        {
            for(int i = 0; i < categories.length; i++)
            {
                final TableCategory category = categories[i];
                categorize(tableNode, category);
            }
        }
    }

    protected Map getTableNodes()
    {
        return tableNodes;
    }

    public Configuration getConfiguration()
    {
        return configuration;
    }

    public TableStructureRules getRules()
    {
        return rules;
    }

    public Set getTopLevelTableNodes()
    {
        return topLevelTableNodes;
    }

    public Map getTableCategories()
    {
        return tableCategories;
    }

    public TableCategory getTableCategory(final String id)
    {
        for(final Iterator i = tableCategories.keySet().iterator(); i.hasNext(); )
        {
            final TableCategory tableCategory = (TableCategory) i.next();
            if(tableCategory.getTableCategoryId().equalsIgnoreCase(id))
                return tableCategory;
        }

        return null;
    }

    public Map getTableToClassMap()
    {
        return tableToClassMap;
    }

    public PersistentClass getClassForTable(final Table table)
    {
        return (PersistentClass) tableToClassMap.get(table);
    }

    public Dialect getDialect()
    {
        return dialect;
    }

    public Mapping getMapping()
    {
        return mapping;
    }

    public TableStructureNode createNode(final PersistentClass mappedClass, final Table nodeForTable, final TableStructureNode parent, final int level)
    {
        final TableStructureNode existing = getNodeForTable(nodeForTable);
        if(existing == null)
        {
            final DefaultTableStructureNode newNode = new DefaultTableStructureNode(mappedClass, nodeForTable, this, parent, level);
            tableNodes.put(nodeForTable.getName().toUpperCase(), newNode);
            newNode.resolveDependencies();
            return newNode;
        }
        else
            return new LinkToOtherTableStructureNode(existing, parent, level);
    }
}

