/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package org.sns.tool.hibernate.document.dbdd;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
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

import org.hibernate.MappingException;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.Mapping;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.ForeignKey;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.PrimaryKey;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.Table;
import org.hibernate.type.Type;
import org.sns.tool.hibernate.document.diagram.HibernateDiagramGeneratorException;
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

    /**
     * The dialect we will use when showing the data types
     */
    private final Dialect dialect;

    /**
     * The mappings we're going to use for getting the data types and related information.
     */
    private final Mapping mapping;

    public DatabaseDesignGenerator(final DatabaseDesignGeneratorConfig config) throws HibernateDiagramGeneratorException
    {
        this.generatorConfig = config;

        // the following was copied from org.hibernate.cfg.Configuration.buildMapping() because buildMapping() was private
        final Configuration configuration = config.getHibernateConfiguration();
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
    }

    public String getUniqueTableId(final Table table)
    {
        return Integer.toHexString(table.hashCode());
    }

    public String getUniqueTableId(final TableStructureNode tableStructNode)
    {
        return getUniqueTableId(tableStructNode.getTable());
    }

    public String getColumnDataType(final TableStructureNode tableStructNode, final Column column, final PrimaryKey partOfPrimaryKey, final ForeignKey partOfForeignKey)
    {
        final TableStructure tableStruct = tableStructNode.getOwner();
        final TableStructureRules rules = tableStruct.getRules();
        return rules.getTranslatedDataType(column.getSqlType(dialect, mapping), tableStructNode, column, partOfPrimaryKey, partOfForeignKey);
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
                                                final TableStructureNode tableStructNode,
                                                final Column column,
                                                final PrimaryKey partOfPrimaryKey,
                                                final ForeignKey partOfForeignKey)
    {
        final TableStructure tableStruct = tableStructNode.getOwner();
        final TableStructureRules rules = tableStruct.getRules();

        final boolean isPrimaryKey = partOfPrimaryKey != null;
        final boolean isForeignKey = partOfForeignKey != null;
        final boolean isChildKeyOfParent = isForeignKey && rules.isParentRelationship(tableStruct, partOfForeignKey);
        final boolean isRequired = !column.isNullable() && partOfPrimaryKey == null;

        String className = null;
        if(isPrimaryKey)
            className = "primary-key";
        if(isChildKeyOfParent)
            className = "child-key";
        else if(isForeignKey)
            className = "foreign-key";

        if(isRequired)
            className += className == null ? "required" : ("-required");

        final Element result = doc.createElement("row");
        if(className != null)
            result.appendChild(createDocBookClassNamePI(doc, className));

        final Element pkElem = (Element) result.appendChild(doc.createElement("entry"));
        pkElem.setAttribute("role", isPrimaryKey ? "column-pk" : "column-not-pk");
        if (isPrimaryKey)
            pkElem.appendChild(createImageElement(doc, "primary-key.gif"));

        final Element requiredElem = (Element) result.appendChild(doc.createElement("entry"));
        requiredElem.setAttribute("role", isRequired ? "column-not-nullable" : "column-nullable");
        if (isRequired || isPrimaryKey)
            requiredElem.appendChild(createImageElement(doc, "value-required.gif"));

        final Element formulaElem = (Element) result.appendChild(doc.createElement("entry"));
        formulaElem.setAttribute("role", isRequired ? "column-not-formula" : "column-formula");
        if (column.isFormula())
            formulaElem.appendChild(createImageElement(doc, "calculated-value.gif"));

        final Element nameElem = (Element) result.appendChild(doc.createElement("entry"));
        nameElem.setAttribute("role", "column-name");

        final Element dataTypeElem = (Element) result.appendChild(doc.createElement("entry"));
        dataTypeElem.setAttribute("role", "column-data-type");

        final Element referencesElem = (Element) result.appendChild(doc.createElement("entry"));
        referencesElem.setAttribute("role", "column-references");

        final Element remarksElem = (Element) result.appendChild(doc.createElement("entry"));
        remarksElem.setAttribute("role", "column-remarks");

        nameElem.appendChild(doc.createTextNode(column.getName()));
        dataTypeElem.appendChild(doc.createTextNode(getColumnDataType(tableStructNode, column, partOfPrimaryKey, partOfForeignKey)));

        if(isForeignKey)
        {
            if(isChildKeyOfParent)
                formulaElem.appendChild(createImageElement(doc, "parent-ref-key.gif"));
            else
                formulaElem.appendChild(createImageElement(doc, "foreign-key.gif"));

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

        final List primaryKeyRows = new ArrayList();
        final List childKeyRows = new ArrayList();
        final List columnRows = new ArrayList();

        final Table table = tableStructNode.getTable();
        final PrimaryKey primaryKeyColumns = table.getPrimaryKey();

        for (final Iterator columns = table.getColumnIterator(); columns.hasNext();)
        {
            final Column column = (Column) columns.next();

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
                primaryKeyRows.add(createColumnDocumentationRow(doc, tableStructNode, column, primaryKeyColumns, partOfForeignKey));
            else if(partOfForeignKey != null && rules.isParentRelationship(tableStruct, partOfForeignKey))
                childKeyRows.add(createColumnDocumentationRow(doc, tableStructNode, column, null, partOfForeignKey));
            else
                columnRows.add(createColumnDocumentationRow(doc, tableStructNode, column, null, partOfForeignKey));
        }

        final Element tableColumnsBodyElem = (Element) tableGroupElem.appendChild(doc.createElement("tbody"));
        for(final Iterator i = primaryKeyRows.iterator(); i.hasNext(); )
            tableColumnsBodyElem.appendChild((Node) i.next());
        for(final Iterator i = childKeyRows.iterator(); i.hasNext(); )
            tableColumnsBodyElem.appendChild((Node) i.next());
        for(final Iterator i = columnRows.iterator(); i.hasNext(); )
            tableColumnsBodyElem.appendChild((Node) i.next());

        return tableColumnsTableElem;
    }

    protected void document(final Element parentElement, final TableCategory category,  final TableStructureNode tableStructNode) throws IOException
    {
        final Document doc = parentElement.getOwnerDocument();

        final String tableName = tableStructNode.getTable().getName();
        final Element tableStructSectionElem = (Element) parentElement.appendChild(doc.createElement("section"));
        tableStructSectionElem.setAttribute("id", getUniqueTableId(tableStructNode) + "_sect");
        tableStructSectionElem.appendChild(createDocBookChunkFileNamePI(doc, category.getCategoryName() + " " + tableName));
        tableStructSectionElem.appendChild(doc.createElement("title")).appendChild(doc.createTextNode(tableName));

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

    public void generate() throws IOException, ParserConfigurationException, TransformerException
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
            categoryChapter.setAttribute("id", "category_" + category.getCategoryName());
            categoryChapter.appendChild(createDocBookChunkFileNamePI(doc, category.getCategoryName()));
            categoryChapter.appendChild(doc.createElement("title")).appendChild(doc.createTextNode(category.getCategoryName()));

            for(Iterator t = categoryNodes.iterator(); t.hasNext(); )
            {
                final TableStructureNode tableNode = (TableStructureNode) t.next();

                // we only want to do the "top level" tables since the rest will be done recursively
                if(tableNode.getLevel() > 0)
                    continue;

                document(categoryChapter, category, tableNode);
            }

            categoryNum++;
        }

        final TransformerFactory transformerFactory = TransformerFactory.newInstance();
        final Transformer serializer = transformerFactory.newTransformer();

        serializer.transform(new DOMSource(doc), new StreamResult(new FileOutputStream(generatorConfig.getDocBookFile())));
    }

    protected ProcessingInstruction createDocBookClassNamePI(final Document doc, final String className)
    {
        return doc.createProcessingInstruction("dbhtml", "class=\""+ className + "\"");
    }

    protected ProcessingInstruction createDocBookChunkFileNamePI(final Document doc, final String baseName)
    {
        return doc.createProcessingInstruction("dbhtml", "filename=\""+ baseName.replaceAll("[^A-Za-z0-9]+", "_").toLowerCase() + ".html\"");
    }

    /*-- Accessors and Mutators for access to private fields --------------------------------------------------------*/

    public Dialect getDialect()
    {
        return dialect;
    }

    public Mapping getMapping()
    {
        return mapping;
    }

    public DatabaseDesignGeneratorConfig getGeneratorConfig()
    {
        return generatorConfig;
    }
}
