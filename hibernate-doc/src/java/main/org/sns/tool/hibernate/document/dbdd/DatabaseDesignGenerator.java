/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package org.sns.tool.hibernate.document.dbdd;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.hibernate.MappingException;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.Mapping;
import org.hibernate.mapping.Collection;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.ForeignKey;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.Table;
import org.hibernate.type.Type;

import org.sns.tool.graphviz.GraphvizDiagramEdge;
import org.sns.tool.graphviz.GraphvizDiagramGenerator;
import org.sns.tool.graphviz.GraphvizDiagramNode;
import org.sns.tool.hibernate.document.diagram.HibernateDiagramGeneratorFilter;
import org.sns.tool.hibernate.document.diagram.HibernateDiagramGeneratorException;
import org.sns.tool.hibernate.document.diagram.HibernateDiagramTableNodeGenerator;
import org.sns.tool.hibernate.struct.TableStructure;
import org.sns.tool.hibernate.struct.TableStructureNode;
import org.sns.tool.hibernate.struct.TableCategory;

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

    protected void document(final Writer writer, final TableStructureNode tableNode) throws IOException
    {
        for(int i = 0; i < tableNode.getLevel(); i++)
            writer.write("    ");

        writer.write(tableNode.getTable().getName());
        writer.write("\n");

        final List childNodes = tableNode.getChildNodes();
        for(int i = 0; i < childNodes.size(); i++)
        {
            final TableStructureNode childNode = (TableStructureNode) childNodes.get(i);
            document(writer, childNode);
        }
    }

    public void generate() throws IOException
    {
        final FileWriter writer = new FileWriter(generatorConfig.getIndexFile());

        for(final Iterator i = generatorConfig.getTableStructure().getTableCategories().entrySet().iterator(); i.hasNext(); )
        {
            final Map.Entry entry = (Map.Entry) i.next();
            final TableCategory category = (TableCategory) entry.getKey();
            final List nodes = (List) entry.getValue();

            writer.write(category.toString());
            writer.write(": ");
            writer.write(Integer.toString(nodes.size()));
            writer.write("\n");
        }
        writer.write("\n");

        final List topLevelTableNodes = generatorConfig.getTableStructure().getTopLevelTableNodes();
        for(int i = 0; i < topLevelTableNodes.size(); i++)
        {
            final TableStructureNode tableNode = (TableStructureNode) topLevelTableNodes.get(i);
            document(writer, tableNode);
        }

        writer.close();
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
