package com.medigy.tool.persist.hibernate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.mapping.ForeignKey;
import org.hibernate.mapping.PersistentClass;
import org.sns.tool.graphviz.GraphvizDiagramEdge;
import org.sns.tool.graphviz.GraphvizDiagramNode;
import org.sns.tool.hibernate.document.DatabaseDesignGeneratorConfig;
import org.sns.tool.hibernate.document.DatabaseDiagramRenderer;
import org.sns.tool.hibernate.struct.ColumnDetail;

import com.medigy.persist.reference.custom.CustomReferenceEntity;

public class HibernateDatabaseDiagramRenderer implements DatabaseDiagramRenderer
{
    public HibernateDatabaseDiagramRenderer()
    {
    }

    public GraphvizDiagramEdge formatForeignKeyEdge(final DatabaseDesignGeneratorConfig generator, final ForeignKey foreignKey, final GraphvizDiagramEdge edge)
    {
        // for parents, we put the crow arrow pointing to us (the source becomes the parent, not the child -- this way it will look like a tree)
        if (generator.getTableStructure().getRules().isParentRelationship(generator.getTableStructure(), foreignKey))
        {
            final String src = edge.getSource();
            final String dest = edge.getDestintation();
            final GraphvizDiagramEdge newEdge = new GraphvizDiagramEdge(edge.getGenerator(), dest, src);
            newEdge.setArrowHead("crow");
            return newEdge;
        }

        return edge;
    }

    public boolean isIncludeEdgePort(final DatabaseDesignGeneratorConfig generator, final ForeignKey foreignKey, boolean source)
    {
        return true;
    }

    public String getTableNameCellHtmlAttributes(final DatabaseDesignGeneratorConfig generator, final PersistentClass pclass)
    {
        if (CustomReferenceEntity.class.isAssignableFrom(pclass.getMappedClass()))
            return " BGCOLOR=\"rosybrown\"";
        else
            return " BGCOLOR=\"lightsteelblue\"";
    }

    public String getEntityTableHtmlAttributes(final DatabaseDesignGeneratorConfig generator, final PersistentClass pclass)
    {
        return "BORDER=\"1\" CELLSPACING=\"0\" CELLBORDER=\"0\"";
    }

    public void formatTableNode(final DatabaseDesignGeneratorConfig generator, final PersistentClass pclass, final GraphvizDiagramNode node)
    {
    }

    public boolean includeForeignKeyEdgeInDiagram(final DatabaseDesignGeneratorConfig generator, final ForeignKey foreignKey)
    {
        return true;
    }

    public String getColumnDefinitionHtml(final DatabaseDesignGeneratorConfig generator,
                                          final ColumnDetail columnDetail,
                                          final boolean showDataTypes,
                                          final boolean showConstraints,
                                          final String indent) throws SQLException, NamingException
    {
        String extraAttrs = "";
        if(columnDetail.isPrimaryKey())
        {
            if(columnDetail.isForeignKey())
                extraAttrs = " BGCOLOR=\"lightcyan\"";
            else
                extraAttrs = " BGCOLOR=\"gray90\"";
        }
        else if(columnDetail.isChildKeyOfParent())
            extraAttrs = " BGCOLOR=\"beige\"";

        final StringBuffer result = new StringBuffer(indent + "<TR>\n");
        result.append(indent + indent + "<TD ALIGN=\"LEFT\" PORT=\"" + columnDetail.getColumn().getName() + "\"" + extraAttrs + ">" + columnDetail.getColumn().getName() + "</TD>\n");

        if (showDataTypes)
            result.append(indent + indent + "<TD ALIGN=\"LEFT\"" + extraAttrs + ">" + columnDetail.getColumn() + "</TD>\n");

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

}
