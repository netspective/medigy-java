package com.medigy.tool.persist.hibernate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.naming.NamingException;

import org.hibernate.mapping.Column;
import org.hibernate.mapping.ForeignKey;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.PrimaryKey;

import com.medigy.persist.model.party.Party;
import com.medigy.persist.reference.ReferenceEntity;
import com.medigy.persist.reference.custom.CustomReferenceEntity;
import com.medigy.persist.util.HibernateConfiguration;

import org.sns.tool.graphviz.GraphvizDiagramEdge;
import org.sns.tool.graphviz.GraphvizDiagramNode;
import org.sns.tool.hibernate.document.diagram.HibernateDiagramGenerator;
import org.sns.tool.hibernate.document.diagram.HibernateDiagramGeneratorFilter;
import org.sns.tool.hibernate.document.diagram.HibernateDiagramTableNameNodeGenerator;
import org.sns.tool.hibernate.document.diagram.HibernateDiagramTableNodeGenerator;
import org.sns.tool.hibernate.document.diagram.HibernateDiagramTableStructureNodeGenerator;

public class HibernateDiagramFilter implements HibernateDiagramGeneratorFilter
{
    private Set commonColumns = new HashSet();
    private boolean hideCommonColumns;
    private boolean showColumns;
    private boolean showReferenceData;
    private boolean showClassStructure;
    private final HibernateDiagramTableNodeGenerator tableDataNodeGenerator = new HibernateDiagramReferenceTableNodeGenerator("enum");
    private final HibernateDiagramTableNodeGenerator tableStructureNodeGenerator = new HibernateDiagramTableStructureNodeGenerator("app");
    private final HibernateDiagramTableNodeGenerator tableNameNodeGenerator = new HibernateDiagramTableNameNodeGenerator("app");

    public HibernateDiagramFilter(final boolean showColumns, final boolean hideCommonColumns, final boolean showReferenceData, final boolean showClassStructure)
    {
        this.showColumns = showColumns;
        this.hideCommonColumns = hideCommonColumns;
        this.showReferenceData = showReferenceData;
        this.showClassStructure = showClassStructure;

        commonColumns.add("version");
        commonColumns.add("create_timestamp");
        commonColumns.add("update_timestamp");
        commonColumns.add("record_status");
        commonColumns.add("create_session_id");
        commonColumns.add("update_session_id");
    }

    public Set getCommonColumns()
    {
        return commonColumns;
    }

    public void formatForeignKeyEdge(final HibernateDiagramGenerator generator, final ForeignKey foreignKey, final GraphvizDiagramEdge edge)
    {
        if (showReferenceData && (isReferenceRelationship(generator, foreignKey) || isCustomReferenceRelationship(generator, foreignKey)))
        {
            edge.setArrowHead("normal");
            edge.setStyle("dotted");
            return;
        }

        if (isShowClassStructure(generator, foreignKey) && generator.isSubclassRelationship(foreignKey))
        {
            // the subclass will point to the superclass and be formatted as a "back" reference to properly set the weight
            edge.getAttributes().put("dir", "back");
            edge.getAttributes().put("arrowtail", "onormal");
            return;
        }

        // for parents, we put the crow arrow pointing to us (the source becomes the parent, not the child -- this way it will look like a tree)
        if (generator.isParentRelationship(foreignKey))
        {
            if (showClassStructure)
            {
                edge.setArrowHead("normal");
                edge.getAttributes().put("arrowtail", "odiamond");
            }
            else
            {
                edge.setArrowSize("2");
                edge.setArrowHead("crow");
            }
            return;
        }
    }

    public boolean isReferenceClass(final HibernateDiagramGenerator generator, final PersistentClass pclass)
    {
        if (!showReferenceData)
            return false;

        return ReferenceEntity.class.isAssignableFrom(pclass.getMappedClass());
    }

    public boolean isReferenceRelationship(final HibernateDiagramGenerator generator, final ForeignKey foreignKey)
    {
        if (!showReferenceData)
            return false;

        return isReferenceClass(generator, generator.getClassForTable(foreignKey.getReferencedTable()));
    }

    public Class getReferenceCachedItems(final HibernateDiagramGenerator generator, final PersistentClass pclass)
    {
        return ((HibernateConfiguration) generator.getConfiguration()).getReferenceEntitiesAndCachesMap().get(pclass.getMappedClass());
    }

    public boolean isShowClassStructure(final HibernateDiagramGenerator generator, final ForeignKey foreignKey)
    {
        return showClassStructure;
    }

    public String getColumnDataType(HibernateDiagramGenerator generator, Column column, PrimaryKey partOfPrimaryKey, ForeignKey partOfForeignKey)
    {
        if (showReferenceData && partOfForeignKey != null && (isReferenceRelationship(generator, partOfForeignKey) || isCustomReferenceRelationship(generator, partOfForeignKey)))
            return partOfForeignKey.getReferencedTable().getName();
        else if(showClassStructure && partOfForeignKey != null && isCustomReferencePartyRelationship(generator, partOfForeignKey))
            return partOfForeignKey.getReferencedTable().getName();
        else
            return column.getSqlType(generator.getDialect(), generator.getMapping());
    }

    public boolean isCustomReferenceRelationship(final HibernateDiagramGenerator generator, final ForeignKey foreignKey)
    {
        return CustomReferenceEntity.class.isAssignableFrom(generator.getClassForTable(foreignKey.getReferencedTable()).getMappedClass());
    }

    public boolean isIncludeEdgePort(final HibernateDiagramGenerator generator, final ForeignKey foreignKey, boolean source)
    {
        if ((showReferenceData && (isReferenceRelationship(generator, foreignKey) || isCustomReferenceRelationship(generator, foreignKey))))
            return false;
        else
            return true;
    }

    public String getTableNameCellHtmlAttributes(final HibernateDiagramGenerator generator, final PersistentClass pclass)
    {
        if (CustomReferenceEntity.class.isAssignableFrom(pclass.getMappedClass()))
            return " BGCOLOR=\"rosybrown\"";
        else
            return " BGCOLOR=\"lightsteelblue\"";
    }

    public String getEntityTableHtmlAttributes(HibernateDiagramGenerator generator, PersistentClass pclass)
    {
        return "BORDER=\"1\" CELLSPACING=\"0\" CELLBORDER=\"0\"";
    }

    public void formatTableNode(final HibernateDiagramGenerator generator, final PersistentClass pclass, final GraphvizDiagramNode node)
    {
    }

    public String getName()
    {
        return "MEDIGY";
    }

    public HibernateDiagramTableNodeGenerator getTableNodeGenerator(final HibernateDiagramGenerator generator,
                                                                    final PersistentClass pclass)
    {
        if(! showColumns)
            return tableNameNodeGenerator;

        if (showReferenceData)
            return isReferenceClass(generator, pclass) ? tableDataNodeGenerator : tableStructureNodeGenerator;
        else
            return tableStructureNodeGenerator;
    }

    public boolean includeClassInDiagram(final HibernateDiagramGenerator generator, final PersistentClass pclass)
    {
        return true;
    }

    public boolean includeColumnInDiagram(final HibernateDiagramGenerator generator, final Column column)
    {
        if (!hideCommonColumns)
            return true;

        return !commonColumns.contains(column.getName());
    }

    public boolean includeForeignKeyEdgeInDiagram(final HibernateDiagramGenerator generator, final ForeignKey foreignKey)
    {
        if(showClassStructure && isCustomReferencePartyRelationship(generator, foreignKey))
            return false;
        else
            return true;
    }

    protected boolean isCustomReferencePartyRelationship(final HibernateDiagramGenerator generator, final ForeignKey foreignKey)
    {
        return CustomReferenceEntity.class.isAssignableFrom(generator.getClassForTable(foreignKey.getTable()).getMappedClass()) &&
                   Party.class.isAssignableFrom(generator.getClassForTable(foreignKey.getReferencedTable()).getMappedClass());
    }

    public String getColumnDefinitionHtml(final HibernateDiagramGenerator generator,
                                          final Column column,
                                          final PrimaryKey partOfPrimaryKey,
                                          final ForeignKey partOfForeignKey,
                                          final boolean showDataTypes,
                                          final boolean showConstraints,
                                          final String indent) throws SQLException, NamingException
    {
        String extraAttrs = "";
        if(partOfPrimaryKey != null)
        {
            if(partOfForeignKey != null)
                extraAttrs = " BGCOLOR=\"lightcyan\"";
            else
                extraAttrs = " BGCOLOR=\"gray90\"";
        }
        else if(partOfForeignKey != null && generator.isParentRelationship(partOfForeignKey))
            extraAttrs = " BGCOLOR=\"beige\"";

        final StringBuffer result = new StringBuffer(indent + "<TR>\n");
        result.append(indent + indent + "<TD ALIGN=\"LEFT\" PORT=\"" + column.getName() + "\"" + extraAttrs + ">" + column.getName() + "</TD>\n");

        if (showDataTypes)
            result.append(indent + indent + "<TD ALIGN=\"LEFT\"" + extraAttrs + ">" + getColumnDataType(generator, column, partOfPrimaryKey, partOfForeignKey) + "</TD>\n");

        if (showConstraints)
        {

            List constraints = new ArrayList();
            if (partOfPrimaryKey != null)
                constraints.add("PK");
            if (column.isUnique())
                constraints.add("U");
            if (column.isFormula())
                constraints.add("F");
            if (!column.isNullable() && partOfPrimaryKey == null)
                constraints.add("R");
            if (partOfForeignKey != null)
            {
                if (generator.isParentRelationship(partOfForeignKey))
                    constraints.add("CK");
                else
                    constraints.add("FK");
            }

            if (constraints.size() > 0)
                result.append(indent + indent + "<TD ALIGN=\"LEFT\" PORT=\"" + column.getName() + COLUMN_PORT_NAME_CONSTRAINT_SUFFIX + "\"" + extraAttrs + ">" + constraints + "</TD>\n");
            else
                result.append(indent + indent + "<TD ALIGN=\"LEFT\"" + extraAttrs + "> </TD>\n");
        }

        result.append(indent + "</TR>\n");
        return result.toString();
    }

}
