/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package org.sns.tool.hibernate.document;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.hibernate.mapping.Column;
import org.hibernate.mapping.ForeignKey;
import org.hibernate.mapping.Table;
import org.sns.tool.graphviz.GraphvizDiagramEdge;
import org.sns.tool.graphviz.GraphvizDiagramGenerator;
import org.sns.tool.graphviz.GraphvizDiagramGenerator.ImageGenerationParams;
import org.sns.tool.graphviz.GraphvizDiagramNode;
import org.sns.tool.graphviz.GraphvizLayoutType;
import org.sns.tool.hibernate.document.diagram.HibernateDiagramGeneratorException;
import org.sns.tool.hibernate.struct.ColumnCategory;
import org.sns.tool.hibernate.struct.ColumnDetail;
import org.sns.tool.hibernate.struct.TableCategory;
import org.sns.tool.hibernate.struct.TableStructure;
import org.sns.tool.hibernate.struct.TableStructureNode;
import org.sns.tool.hibernate.struct.TableStructureRules;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;

public class DatabaseDesignGenerator
{
    /**
     * The configuration we'll use to do the documentation.
     */
    private final DatabaseDesignGeneratorConfig generatorConfig;

    public DatabaseDesignGenerator(final DatabaseDesignGeneratorConfig config) throws HibernateDiagramGeneratorException
    {
        this.generatorConfig = config;
    }

    public String getUniqueTableId(final Table table)
    {
        return Integer.toHexString(table.hashCode());
    }

    public String getUniqueTableId(final TableStructureNode tableStructNode)
    {
        return getUniqueTableId(tableStructNode.getTable());
    }

    public Element createImageElement(final Document doc, final String imageFileRef, final String format)
    {
        final Element result = doc.createElement("mediaobject");
        final Element imageObjectElem = (Element) result.appendChild(doc.createElement("imageobject"));
        final Element imageDataElem = (Element) imageObjectElem.appendChild(doc.createElement("imagedata"));
        imageDataElem.setAttribute("format", format);
        imageDataElem.setAttribute("fileref", "resources/" + imageFileRef);
        return result;
    }

    public Element createImageElement(final Document doc, final String imageFileRef)
    {
        return createImageElement(doc, imageFileRef, imageFileRef.substring(imageFileRef.lastIndexOf('.')+1).toLowerCase());
    }

    public Element createColumnDocumentationRow(final Document doc,
                                                final ColumnDetail columnDetail)
    {
        final TableStructure tableStruct = columnDetail.getTableStructureNode().getOwner();
        final TableStructureRules rules = tableStruct.getRules();

        final Element result = doc.createElement("row");
        result.appendChild(createDocBookClassNamePI(doc, columnDetail.getColumnCategory().getColumnCategoryId()));

        final Element pkElem = (Element) result.appendChild(doc.createElement("entry"));
        pkElem.setAttribute("role", columnDetail.isPrimaryKey() ? "column-pk" : "column-not-pk");
        if (columnDetail.isPrimaryKey())
            pkElem.appendChild(createImageElement(doc, "primary-key.gif"));

        final Element requiredElem = (Element) result.appendChild(doc.createElement("entry"));
        requiredElem.setAttribute("role", columnDetail.isRequired() ? "column-not-nullable" : "column-nullable");
        if (columnDetail.isRequired() || columnDetail.isPrimaryKey())
            requiredElem.appendChild(createImageElement(doc, "value-required.gif"));

        final Element formulaElem = (Element) result.appendChild(doc.createElement("entry"));
        formulaElem.setAttribute("role", columnDetail.getColumn().isFormula() ? "column-formula" : "column-not-formula");
        if (columnDetail.getColumn().isFormula())
            formulaElem.appendChild(createImageElement(doc, "calculated-value.gif"));

        final Element nameElem = (Element) result.appendChild(doc.createElement("entry"));
        nameElem.setAttribute("role", "column-name");

        final Element dataTypeElem = (Element) result.appendChild(doc.createElement("entry"));
        dataTypeElem.setAttribute("role", "column-data-type");

        final Element referencesElem = (Element) result.appendChild(doc.createElement("entry"));
        referencesElem.setAttribute("role", "column-references");

        final Element remarksElem = (Element) result.appendChild(doc.createElement("entry"));
        remarksElem.setAttribute("role", "column-remarks");

        nameElem.appendChild(doc.createTextNode(columnDetail.getColumn().getName()));
        dataTypeElem.appendChild(doc.createTextNode(columnDetail.getDataType()));

        if(columnDetail.isForeignKey())
        {
            if(columnDetail.isChildKeyOfParent())
                formulaElem.appendChild(createImageElement(doc, "parent-ref-key.gif"));
            else
                formulaElem.appendChild(createImageElement(doc, "foreign-key.gif"));

            final ForeignKey partOfForeignKey = columnDetail.getForeignKey();
            final StringBuffer colNames = new StringBuffer();
            final List columns = partOfForeignKey.getColumns();
            for(int i = 0; i < columns.size(); i++)
            {
                if(i > 0) colNames.append(",");
                colNames.append(((Column) columns.get(i)).getName());
            }

            final StringBuffer sb = new StringBuffer();
            sb.append(partOfForeignKey.getReferencedTable().getName());
            sb.append(".");
            sb.append(colNames);

            final Element linkElem = doc.createElement("link");
            linkElem.setAttribute("linkend", getUniqueTableId(partOfForeignKey.getReferencedTable()));
            linkElem.appendChild(doc.createTextNode(sb.toString()));
            referencesElem.appendChild(linkElem);
        }

        return result;
    }

    public Element createColumnDocumentationTable(final Document doc,
                                                  final TableStructureNode tableStructNode)
    {
        final TableStructure tableStruct = tableStructNode.getOwner();
        final TableStructureRules rules = tableStruct.getRules();

        final Element tableColumnsTableElem = doc.createElement("table");
        tableColumnsTableElem.setAttribute("id", getUniqueTableId(tableStructNode));
        tableColumnsTableElem.setAttribute("tabstyle", "table_columns");
        tableColumnsTableElem.appendChild(doc.createElement("title")).appendChild(doc.createTextNode(tableStructNode.getTable().getName() + " Columns"));
        final Element tableGroupElem = (Element) tableColumnsTableElem.appendChild(doc.createElement("tgroup"));

        final Element tableColumnsHeadElem = (Element) tableGroupElem.appendChild(doc.createElement("thead"));
        final Element tableColumnsHeadRowElem = (Element) tableColumnsHeadElem.appendChild(doc.createElement("row"));
        tableColumnsHeadRowElem.appendChild(doc.createElement("entry")).appendChild(doc.createTextNode(""));
        tableColumnsHeadRowElem.appendChild(doc.createElement("entry")).appendChild(doc.createTextNode(""));
        tableColumnsHeadRowElem.appendChild(doc.createElement("entry")).appendChild(doc.createTextNode(""));
        tableColumnsHeadRowElem.appendChild(doc.createElement("entry")).appendChild(doc.createTextNode("Column"));
        tableColumnsHeadRowElem.appendChild(doc.createElement("entry")).appendChild(doc.createTextNode("Data type"));
        tableColumnsHeadRowElem.appendChild(doc.createElement("entry")).appendChild(doc.createTextNode("References"));
        tableColumnsHeadRowElem.appendChild(doc.createElement("entry")).appendChild(doc.createTextNode("Description"));

        tableGroupElem.setAttribute("cols", Integer.toString(tableColumnsHeadRowElem.getChildNodes().getLength()));

        final List rowsByColumnCategory = new ArrayList();
        final ColumnCategory[] sortedCategories = rules.getColumnCategoriesSortOrder(tableStruct);
        for(int sc = 0; sc < sortedCategories.length; sc++)
        {
            final List rowsInCategory = new ArrayList();
            final ColumnCategory category = sortedCategories[sc];
            final ColumnDetail[] columnDetails = tableStructNode.getColumnsInCategory(category);

            if(columnDetails != null)
            {
                for(int cd = 0; cd < columnDetails.length; cd++)
                {
                    final ColumnDetail columnDetail = columnDetails[cd];
                    rowsInCategory.add(createColumnDocumentationRow(doc, columnDetail));
                }
            }

            rowsByColumnCategory.add(rowsInCategory);
        }

        final Element tableColumnsBodyElem = (Element) tableGroupElem.appendChild(doc.createElement("tbody"));
        for(final Iterator i = rowsByColumnCategory.iterator(); i.hasNext(); )
        {
            final List rowsInCategory = (List) i.next();
            for(final Iterator j = rowsInCategory.iterator(); j.hasNext(); )
                tableColumnsBodyElem.appendChild((Node) j.next());
        }

        return tableColumnsTableElem;
    }

    protected void document(final Element parentElement, final TableCategory category,  final TableStructureNode tableStructNode) throws IOException
    {
        final Document doc = parentElement.getOwnerDocument();

        final String tableName = tableStructNode.getTable().getName();
        final Element tableStructSectionElem = (Element) parentElement.appendChild(doc.createElement("section"));
        tableStructSectionElem.setAttribute("id", getUniqueTableId(tableStructNode) + "_sect");
        tableStructSectionElem.appendChild(createDocBookChunkFileNamePI(doc, category.getTableCategoryId() + " " + tableName));
        tableStructSectionElem.appendChild(doc.createElement("title")).appendChild(doc.createTextNode(tableName));

        final Element overview = (Element) tableStructSectionElem.appendChild(doc.createElement("para"));
        overview.appendChild(doc.createTextNode("The " + tableStructNode.getTable().getName() + " table is represented in the domain model by the class "));

        final File associatedJavaDocHome = generatorConfig.getAssociatedJavaDocHome();
        final Class mappedClass = tableStructNode.getPersistentClass().getMappedClass();
        if(associatedJavaDocHome != null)
        {
            final Element linkElem = doc.createElement("ulink");
            linkElem.setAttribute("role", "table-mapped-class-javadoc");
            linkElem.setAttribute("url", associatedJavaDocHome + "/" + mappedClass.getName().replace('.', '/') + ".html");
            linkElem.appendChild(doc.createTextNode(mappedClass.getName()));
            overview.appendChild(linkElem);
        }
        else
            overview.appendChild(doc.createTextNode(mappedClass.getName()));
        overview.appendChild(doc.createTextNode("."));

        tableStructSectionElem.appendChild(createColumnDocumentationTable(doc, tableStructNode));
        for(Iterator t = tableStructNode.getChildNodes().iterator(); t.hasNext(); )
        {
            final TableStructureNode childNode = (TableStructureNode) t.next();
            if(childNode.isLinkedNode())
            {
                // TODO: handle this properly
            }
            else
                document(tableStructSectionElem, category, childNode);
        }
    }

    public void generateDatabaseDesign() throws IOException, ParserConfigurationException, TransformerException
    {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final Document doc = factory.newDocumentBuilder().newDocument();

        doc.setXmlVersion("1.0");
        doc.setStrictErrorChecking(true);

        final Element rootElem = (Element) doc.appendChild(doc.createElement("book"));
        rootElem.appendChild(doc.createElement("title")).appendChild(doc.createTextNode(generatorConfig.getDocumentTitle()));

        final TableStructure tableStructure = generatorConfig.getTableStructure();
        int categoryNum = 0;
        for(final Iterator i = tableStructure.getTableCategories().entrySet().iterator(); i.hasNext(); )
        {
            final Map.Entry entry = (Map.Entry) i.next();
            final TableCategory category = (TableCategory) entry.getKey();
            final Set categoryNodes = (Set) entry.getValue();

            final Element categoryChapter = (Element) rootElem.appendChild(doc.createElement("chapter"));
            categoryChapter.setAttribute("name", "category_" + categoryNum);
            categoryChapter.setAttribute("id", "category_" + category.getTableCategoryId());
            categoryChapter.appendChild(createDocBookChunkFileNamePI(doc, category.getTableCategoryId()));
            categoryChapter.appendChild(doc.createElement("title")).appendChild(doc.createTextNode(category.getTableCategoryLabel()));

            for(Iterator t = categoryNodes.iterator(); t.hasNext(); )
            {
                final TableStructureNode tableNode = (TableStructureNode) t.next();

                // we only want to do the "top level" tables since the rest will be done recursively
                if(tableNode.getLevel() > 0)
                    continue;

                document(categoryChapter, category, tableNode);
            }

            if(category.isGenerateDiagrams())
                generateDatabaseDiagrams((Set) tableStructure.getTableCategories().get(category), category.getTableCategoryId() + "-erd");

            categoryNum++;
        }

        final TransformerFactory transformerFactory = TransformerFactory.newInstance();
        final Transformer serializer = transformerFactory.newTransformer();

        serializer.transform(new DOMSource(doc), new StreamResult(new FileOutputStream(generatorConfig.getDocBookFile())));
    }

    public void generateDatabaseDiagrams(final Set tableStructNodes, final String baseName) throws IOException
    {
        final GraphvizDiagramGenerator gdg = new GraphvizDiagramGenerator("erd", true, GraphvizLayoutType.DOT);
        final DatabaseDiagramRenderer ddr = generatorConfig.getDatabaseDiagramRenderer();

        final Set tablesIncluded = new HashSet();
        for(final Iterator i = tableStructNodes.iterator(); i.hasNext(); )
        {
            final TableStructureNode tableNode = (TableStructureNode) i.next();
            tablesIncluded.add(tableNode.getTable());

            StringBuffer tableNodeLabel = new StringBuffer("<<TABLE " + ddr.getEntityTableHtmlAttributes(generatorConfig, tableNode.getPersistentClass()) + ">\n");
            tableNodeLabel.append("        <TR><TD " + ddr.getTableNameCellHtmlAttributes(generatorConfig, tableNode.getPersistentClass()) + ">" + tableNode.getTable().getName() + "</TD></TR>\n");
            tableNodeLabel.append("    </TABLE>>");

            final GraphvizDiagramNode gdn = new GraphvizDiagramNode(gdg, tableNode.getTable().getName());
            gdn.setLabel(tableNodeLabel.toString());
            gdn.setShape("plaintext");
            gdn.setFontName("Helvetica");

            gdg.addNode(gdn);
        }


        for(final Iterator i = tableStructNodes.iterator(); i.hasNext(); )
        {
            final TableStructureNode tableNode = (TableStructureNode) i.next();
            for (final Iterator fKeys = tableNode.getTable().getForeignKeyIterator(); fKeys.hasNext();)
            {
                final ForeignKey foreignKey = (ForeignKey) fKeys.next();

                if (tablesIncluded.contains(foreignKey.getReferencedTable()) && ddr.includeForeignKeyEdgeInDiagram(generatorConfig, foreignKey))
                {
                    final GraphvizDiagramEdge edge = new GraphvizDiagramEdge(gdg, foreignKey.getTable().getName(), foreignKey.getReferencedTable().getName());
                    gdg.addEdge(ddr.formatForeignKeyEdge(generatorConfig, foreignKey, edge));
                }
            }
        }

        try
        {
            gdg.generateImages(new ImageGenerationParams()
            {
                public String getBaseFileName()
                {
                    return baseName;
                }

                public File getDestDir()
                {
                    return generatorConfig.getImagesDirectory();
                }

                public String getGraphVizDotCommandSpec()
                {
                    return generatorConfig.getGraphVizDotCommandSpec();
                }

                public String[] getImageExtensions()
                {
                    return new String[] { "." + generatorConfig.getGraphvizDiagramOutputType() };
                }

                public String[] getImageTypes()
                {
                    return new String[] { generatorConfig.getGraphvizDiagramOutputType() };
                }

                public PrintStream getLogOutputStream()
                {
                    return null;  //To change body of implemented methods use File | Settings | File Templates.
                }
            });
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }


    protected ProcessingInstruction createDocBookClassNamePI(final Document doc, final String className)
    {
        return doc.createProcessingInstruction("dbhtml", "class=\""+ className + "\"");
    }

    protected ProcessingInstruction createDocBookChunkFileNamePI(final Document doc, final String baseName)
    {
        return doc.createProcessingInstruction("dbhtml", "filename=\""+ baseName.replaceAll("[^A-Za-z0-9]+", "_").toLowerCase() + ".html\"");
    }

    public DatabaseDesignGeneratorConfig getGeneratorConfig()
    {
        return generatorConfig;
    }
}
