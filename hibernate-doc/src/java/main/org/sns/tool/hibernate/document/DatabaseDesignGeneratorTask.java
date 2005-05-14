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
package org.sns.tool.hibernate.document;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Constructor;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.sns.tool.hibernate.struct.TableStructure;
import org.sns.tool.hibernate.struct.TableStructureRules;
import org.sns.tool.hibernate.struct.impl.DefaultTableStructure;

public class DatabaseDesignGeneratorTask extends Task
{
    private Class structureClass;
    private Class hibernateConfigClass;
    private Class structureRulesClass;
    private Class databaseDiagramRendererClass;
    private String hibernateConfigFile;
    private String graphvizDotCmdSpec;
    private String graphvizDotOutputType = "gif";
    private String dialectClass;
    private File destDir;
    private File docBookFile;
    private File associatedJavaDocHome;
    private String documentTitle;
    private boolean logGraphvizOutput;

    public void execute() throws BuildException
    {
        if(hibernateConfigClass == null)
            throw new BuildException("hibernateConfigClass was not provided.");

        if(structureRulesClass == null)
            throw new BuildException("structureRulesClass was not provided.");

        if(docBookFile == null)
            throw new BuildException("indexFile was not provided.");

        try
        {
            final Configuration configuration = (Configuration) hibernateConfigClass.newInstance();
            if(hibernateConfigFile != null)
                configuration.configure(hibernateConfigFile);

            final TableStructure structure;
            if(structureClass == null)
            {
                final TableStructureRules rules = (TableStructureRules) structureRulesClass.newInstance();
                structure = new DefaultTableStructure(configuration, rules);
            }
            else
            {
                final Constructor ctr = structureClass.getConstructor(new Class[] { Configuration.class, TableStructureRules.class });
                final TableStructureRules rules = (TableStructureRules) structureRulesClass.newInstance();
                structure = (TableStructure) ctr.newInstance(new Object[] { configuration, rules});
            }

            if(dialectClass != null)
                configuration.setProperty(Environment.DIALECT, dialectClass);

            final DatabaseDiagramRenderer ddr = (DatabaseDiagramRenderer) databaseDiagramRendererClass.newInstance();

            log("Using Hibernate Configuration " + configuration.getClass());
            log("Using Structure " + structure.getClass());
            log("Using Structure Rules " + structure.getRules().getClass());
            log("Using Dialect " + configuration.getProperty(Environment.DIALECT));
            log("Using Renderer " + ddr.getClass());

            final ByteArrayOutputStream graphvizCmdOutputStreamBuffer = new ByteArrayOutputStream();
            final PrintStream graphvizCmdOutputStream = new PrintStream(graphvizCmdOutputStreamBuffer);

            final DatabaseDesignGeneratorConfig ddgConfig = new DatabaseDesignGeneratorConfig()
            {
                public File getImagesDirectory()
                {
                    return destDir == null ? docBookFile.getParentFile() : destDir;
                }

                public Configuration getHibernateConfiguration()
                {
                    return configuration;
                }

                public File getDocBookFile()
                {
                    return docBookFile;
                }

                public File getAssociatedJavaDocHome()
                {
                    return associatedJavaDocHome;
                }

                public String getDocumentTitle()
                {
                    return documentTitle == null ? "No documentTitle attribute provided." : documentTitle;
                }

                public TableStructure getTableStructure()
                {
                    return structure;
                }

                public DatabaseDiagramRenderer getDatabaseDiagramRenderer()
                {
                    return ddr;
                }

                public String getGraphvizDiagramOutputType()
                {
                    return graphvizDotOutputType;
                }

                public String getGraphVizDotCommandSpec()
                {
                    return graphvizDotCmdSpec;
                }

                public PrintStream getGraphVizDotLogOutputStream()
                {
                    return graphvizCmdOutputStream;
                }
            };

            final DatabaseDesignGenerator ddg = new DatabaseDesignGenerator(ddgConfig);
            ddg.generateDatabaseDesign();

            if(logGraphvizOutput)
                log(graphvizCmdOutputStreamBuffer.toString());
        }
        catch (Exception e)
        {
            throw new BuildException(e);
        }
    }

    public void setHibernateConfigFile(String hibernateConfigFile)
    {
        this.hibernateConfigFile = hibernateConfigFile;
    }

    public void setDialectClass(String dialectClass)
    {
        this.dialectClass = dialectClass;
    }

    public void setHibernateConfigClass(final String cls) throws ClassNotFoundException
    {
        hibernateConfigClass = Class.forName(cls);
    }

    public void setDestDir(File destDir)
    {
        this.destDir = destDir;
    }

    public void setDocBookFile(File docBookFile)
    {
        this.docBookFile = docBookFile;
    }

    public void setStructureClass(final String structureClass) throws ClassNotFoundException
    {
        this.structureClass = Class.forName(structureClass);
    }

    public void setStructureRulesClass(final String structureRulesClass) throws ClassNotFoundException
    {
        this.structureRulesClass = Class.forName(structureRulesClass);
    }

    public void setDatabaseDiagramRendererClass(final String databaseDiagramRendererClass) throws ClassNotFoundException
    {
        this.databaseDiagramRendererClass = Class.forName(databaseDiagramRendererClass);
    }

    public void setDocumentTitle(String documentTitle)
    {
        this.documentTitle = documentTitle;
    }

    public void setAssociatedJavaDocHome(File associatedJavaDocHome)
    {
        this.associatedJavaDocHome = associatedJavaDocHome;
    }

    public void setGraphvizDotCmdSpec(String graphvizDotCmdSpec)
    {
        this.graphvizDotCmdSpec = graphvizDotCmdSpec;
    }

    public void setGraphvizDotOutputType(String graphvizDotOutputType)
    {
        this.graphvizDotOutputType = graphvizDotOutputType;
    }

    public void setLogGraphvizOutput(boolean logGraphvizOutput)
    {
        this.logGraphvizOutput = logGraphvizOutput;
    }
}
