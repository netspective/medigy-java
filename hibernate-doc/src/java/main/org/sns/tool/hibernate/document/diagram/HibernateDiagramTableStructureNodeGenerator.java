/*
 * Copyright (c) 2000-2004 Netspective Communications LLC. All rights reserved.
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
 *    used to endorse or appear in products derived from The Software without written consent of Netspective.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT,
 * ARE HEREBY DISCLAIMED.
 *
 * NETSPECTIVE AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE OR ANY THIRD PARTY AS A
 * RESULT OF USING OR DISTRIBUTING THE SOFTWARE. IN NO EVENT WILL NETSPECTIVE OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THE SOFTWARE, EVEN
 * IF IT HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 */
package org.sns.tool.hibernate.document.diagram;

import java.sql.SQLException;
import java.util.Iterator;

import javax.naming.NamingException;

import org.hibernate.mapping.Column;
import org.hibernate.mapping.ForeignKey;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.PrimaryKey;
import org.hibernate.mapping.Table;

import org.sns.tool.graphviz.GraphvizDiagramNode;

public class HibernateDiagramTableStructureNodeGenerator implements HibernateDiagramTableNodeGenerator
{
    private String name;
    private boolean showDataTypes = true;
    private boolean showConstraints = true;

    public HibernateDiagramTableStructureNodeGenerator(final String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public GraphvizDiagramNode generateTableNode(final HibernateDiagramGenerator generator,
                                                 final HibernateDiagramGeneratorFilter filter,
                                                 final PersistentClass pclass)
    {
        final Table table = pclass.getTable();
        final StringBuffer primaryKeyRows = new StringBuffer();
        final StringBuffer childKeyRows = new StringBuffer();
        final StringBuffer columnRows = new StringBuffer();

        final PrimaryKey primaryKeyColumns = table.getPrimaryKey();
        final String indent = "        ";
        int hidden = 0;

        for (final Iterator columns = table.getColumnIterator(); columns.hasNext();)
        {
            final Column column = (Column) columns.next();

            if (filter.includeColumnInDiagram(generator, column))
            {
                try
                {
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

                    if (primaryKeyColumns.containsColumn(column))
                        primaryKeyRows.append(filter.getColumnDefinitionHtml(generator, column, primaryKeyColumns, partOfForeignKey, showDataTypes, showConstraints, indent) + "\n");
                    else if(partOfForeignKey != null && generator.isParentRelationship(partOfForeignKey))
                        childKeyRows.append(filter.getColumnDefinitionHtml(generator, column, null, partOfForeignKey, showDataTypes, showConstraints, indent) + "\n");
                    else
                        columnRows.append(filter.getColumnDefinitionHtml(generator, column, null, partOfForeignKey, showDataTypes, showConstraints, indent) + "\n");
                }
                catch (SQLException e)
                {
                    throw new HibernateDiagramGeneratorException(e);
                }
                catch (NamingException e)
                {
                    throw new HibernateDiagramGeneratorException(e);
                }
            }
            else
                hidden++;
        }

        int colSpan = 1;
        if (showDataTypes) colSpan++;
        if (showConstraints) colSpan++;

        StringBuffer tableNodeLabel = new StringBuffer("<<TABLE " + filter.getEntityTableHtmlAttributes(generator, pclass) + ">\n");
        tableNodeLabel.append("        <TR><TD COLSPAN=\"" + colSpan + "\" " + filter.getTableNameCellHtmlAttributes(generator, pclass) + ">" + table.getName() + "</TD></TR>\n");
        if (primaryKeyRows.length() > 0)
            tableNodeLabel.append(primaryKeyRows);
        if (childKeyRows.length() > 0)
            tableNodeLabel.append(childKeyRows);
        tableNodeLabel.append(columnRows);
        if (hidden > 0)
            tableNodeLabel.append("        <TR><TD COLSPAN=\"" + colSpan + "\">(" + hidden + " columns not shown)</TD></TR>\n");
        tableNodeLabel.append("    </TABLE>>");

        GraphvizDiagramNode result = new GraphvizDiagramNode(generator.getGraphvizDiagramGenerator(), table.getName());
        result.setLabel(tableNodeLabel.toString());
        result.setShape("plaintext");
        result.setFontName("Helvetica");

        return result;
    }

    public String getEdgeSourceElementAndPort(final HibernateDiagramGenerator generator, final HibernateDiagramGeneratorFilter filter, final ForeignKey foreignKey)
    {
        // the subclass will point to the superclass and be formatted as a "back" reference to properly set the weight
        if (filter.isShowClassStructure(generator, foreignKey) && generator.isSubclassRelationship(foreignKey))
            return foreignKey.getReferencedTable().getName();

        // for parents, we put the crow arrow pointing to us (the source becomes the parent, not the child -- this way it will look like a tree)
        if (generator.isParentRelationship(foreignKey))
            return foreignKey.getReferencedTable().getName();

        return filter.isIncludeEdgePort(generator, foreignKey, true) ? (foreignKey.getTable().getName() + ":" + (showConstraints
                ? (foreignKey.getColumn(0).getName() + HibernateDiagramGeneratorFilter.COLUMN_PORT_NAME_CONSTRAINT_SUFFIX)
                : foreignKey.getColumn(0).getName())) : foreignKey.getTable().getName();

    }

    public String getEdgeDestElementAndPort(final HibernateDiagramGenerator generator, final HibernateDiagramGeneratorFilter filter, final ForeignKey foreignKey)
    {
        // the subclass will point to the superclass and be formatted as a "back" reference to properly set the weight
        if (filter.isShowClassStructure(generator, foreignKey) && generator.isSubclassRelationship(foreignKey))
            return foreignKey.getTable().getName();

        // for parents, we put the crow arrow pointing to us (the source becomes the parent, not the child -- this way it will look like a tree)
        if (generator.isParentRelationship(foreignKey))
            return foreignKey.getTable().getName();

        return filter.isIncludeEdgePort(generator, foreignKey, false) ?
                (foreignKey.getReferencedTable().getName() + ":" + foreignKey.getReferencedTable().getPrimaryKey().getColumn(0).getName()) :
                foreignKey.getReferencedTable().getName();
    }

    public boolean isShowConstraints()
    {
        return showConstraints;
    }

    public void setShowConstraints(final boolean showConstraints)
    {
        this.showConstraints = showConstraints;
    }

    public boolean isShowDataTypes()
    {
        return showDataTypes;
    }

    public void setShowDataTypes(final boolean showDataTypes)
    {
        this.showDataTypes = showDataTypes;
    }
}
