package com.medigy.tool.persist.hibernate.dbdd;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.mapping.ForeignKey;
import org.sns.tool.graphviz.GraphvizDiagramEdge;
import org.sns.tool.graphviz.GraphvizDiagramNode;
import org.sns.tool.hibernate.dbdd.DatabaseDesignGeneratorConfig;
import org.sns.tool.hibernate.dbdd.DatabaseDiagramRenderer;
import org.sns.tool.hibernate.struct.ColumnCategory;
import org.sns.tool.hibernate.struct.ColumnDetail;
import org.sns.tool.hibernate.struct.TableStructure;
import org.sns.tool.hibernate.struct.TableStructureNode;
import org.sns.tool.hibernate.struct.TableStructureRules;

import com.medigy.persist.reference.ReferenceEntity;
import com.medigy.persist.reference.custom.CustomReferenceEntity;

public class MedigyDatabaseDiagramRenderer implements DatabaseDiagramRenderer
{
    public MedigyDatabaseDiagramRenderer()
    {
    }

    public GraphvizDiagramEdge formatForeignKeyEdge(final DatabaseDesignGeneratorConfig generator, final TableStructureNode node, final ForeignKey foreignKey, final GraphvizDiagramEdge edge, boolean focused)
    {
        // for parents, we put the crow arrow pointing to us (the source becomes the parent, not the child -- this way it will look like a tree)
        final TableStructureRules rules = generator.getTableStructure().getRules();
        if (rules.isParentRelationship(generator.getTableStructure(), foreignKey))
        {
            final String src = edge.getSource();
            final String dest = edge.getDestintation();
            final GraphvizDiagramEdge newEdge = new GraphvizDiagramEdge(edge.getGenerator(), dest, src);
            newEdge.setArrowHead("crow");
            return newEdge;
        }

        // if the fkey is not a parent/child relationship and the fkey is not part of the focused table then eliminate the edge
        // to remove clutter
        if(! focused)
            return null;

        return edge;
    }

    public String getTableNameCellHtmlAttributes(final DatabaseDesignGeneratorConfig generator, final TableStructureNode node, boolean isFocused)
    {
        if (isFocused)
            return " BGCOLOR=\"beige\"";
        else if (CustomReferenceEntity.class.isAssignableFrom(node.getPersistentClass().getMappedClass()) ||
                 ReferenceEntity.class.isAssignableFrom(node.getPersistentClass().getMappedClass()))
            return " BGCOLOR=\"rosybrown\"";
        else
            return " BGCOLOR=\"lightsteelblue\"";
    }

    public String getEntityTableHtmlAttributes(final DatabaseDesignGeneratorConfig generator, final TableStructureNode node, boolean isFocused)
    {
        return "BORDER=\"1\" CELLSPACING=\"0\" CELLBORDER=\"0\"";
    }

    public GraphvizDiagramNode formatTableNode(final DatabaseDesignGeneratorConfig generator, final TableStructureNode tableStructNode, final GraphvizDiagramNode gdNode, boolean isFocused)
    {
        if(isFocused)
            gdNode.setFontSize("9");
        return gdNode;
    }

    public String getColumnDefinitionHtml(final DatabaseDesignGeneratorConfig generator,
                                          final ColumnDetail columnDetail,
                                          final boolean showDataTypes,
                                          final boolean showConstraints,
                                          final String indent)
    {
        String extraAttrs = "";
        if(columnDetail.isPrimaryKey())
            extraAttrs = " BGCOLOR=\"yellow\"";

        final StringBuffer result = new StringBuffer(indent + "<TR>\n");
        result.append(indent + indent + "<TD ALIGN=\"LEFT\" PORT=\"" + columnDetail.getColumn().getName() + "\"" + extraAttrs + ">" + columnDetail.getColumn().getName() + "</TD>\n");

        if (showDataTypes)
            result.append(indent + indent + "<TD ALIGN=\"LEFT\"" + extraAttrs + ">" + columnDetail.getDataType() + "</TD>\n");

        if (showConstraints)
        {
            List constraints = new ArrayList();
            if (columnDetail.isPrimaryKey())
                constraints.add("PK");
            if (columnDetail.getColumn().isUnique())
                constraints.add("U");
            if (columnDetail.getColumn().isFormula())
                constraints.add("F");
            if (columnDetail.isRequired())
                constraints.add("R");
            if (columnDetail.isForeignKey())
            {
                if (columnDetail.isChildKeyOfParent())
                    constraints.add("CK");
                else
                    constraints.add("FK");
            }

            if (constraints.size() > 0)
                result.append(indent + indent + "<TD ALIGN=\"LEFT\" PORT=\"" + columnDetail.getColumn().getName() + COLUMN_PORT_NAME_CONSTRAINT_SUFFIX + "\"" + extraAttrs + ">" + constraints + "</TD>\n");
            else
                result.append(indent + indent + "<TD ALIGN=\"LEFT\"" + extraAttrs + "> </TD>\n");
        }

        result.append(indent + "</TR>\n");
        return result.toString();
    }

    public String getTableDefinitionHtml(DatabaseDesignGeneratorConfig generatorConfig, TableStructureNode tableNode, boolean focused)
    {
        StringBuffer html = new StringBuffer("<<TABLE " + getEntityTableHtmlAttributes(generatorConfig, tableNode, focused) + ">\n");
        if(focused)
        {
            html.append("        <TR><TD COLSPAN=\"2\" " + getTableNameCellHtmlAttributes(generatorConfig, tableNode, focused) + ">" + tableNode.getTable().getName() + "</TD></TR>\n");
            final TableStructure tableStruct = tableNode.getOwner();
            final ColumnCategory[] sortedCategories = tableStruct.getRules().getColumnCategoriesSortOrder(tableStruct);
            for(int sc = 0; sc < sortedCategories.length; sc++)
            {
                final ColumnCategory category = sortedCategories[sc];
                if(! category.isIncludeInDiagrams())
                    continue;

                final ColumnDetail[] columnDetails = tableNode.getColumnsInCategory(category);
                if(columnDetails != null)
                {
                    for(int cd = 0; cd < columnDetails.length; cd++)
                    {
                        final ColumnDetail columnDetail = columnDetails[cd];
                        html.append(getColumnDefinitionHtml(generatorConfig, columnDetail, true, false, "    "));
                    }
                }
            }

        }
        else
            html.append("        <TR><TD " + getTableNameCellHtmlAttributes(generatorConfig, tableNode, focused) + ">" + tableNode.getTable().getName() + "</TD></TR>\n");
        html.append("    </TABLE>>");

        return html.toString();
    }

}
