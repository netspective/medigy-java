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
package com.medigy.tool.persist.hibernate.ddl;

import java.io.File;
import java.util.Properties;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.hibernate.HibernateException;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.Dialect;
import org.hibernate.tool.hbm2ddl.SchemaExport;

public class GenerateDDLTask extends Task
{
    private Class hibernateConfigClass;
    private Class createSqlDataDefinitionFilterClass = DefaultCreateSqlDataDefinitionFilter.class;
    private Class cleanSqlDataDefinitionFilterClass = DefaultCleanSqlDataDefinitionFilter.class;
    private String hibernateConfigFile;
    private String sqlStmtDelimiter = ";";
    private File destDir;
    private String createPrefix = "create_";
    private String cleanPrefix = "clean_";
    private String destFileExtension = ".sql";

    public void execute() throws BuildException
    {
        if(hibernateConfigClass == null)
            throw new BuildException("hibernateConfigClass was not provided.");

        if(destDir == null)
            throw new BuildException("destDir was not provided.");

        try
        {
            final SqlDataDefinitionFilter createFilter = (SqlDataDefinitionFilter) createSqlDataDefinitionFilterClass.newInstance();
            log("Using create DDL filter " + createFilter.getClass().getName());

            final SqlDataDefinitionFilter cleanFilter = (SqlDataDefinitionFilter) cleanSqlDataDefinitionFilterClass.newInstance();
            log("Using clean DDL filter " + createFilter.getClass().getName());

            final Configuration configuration = (Configuration) hibernateConfigClass.newInstance();
            log("Using configuration " + configuration.getClass().getName());

            if(hibernateConfigFile != null)
            {
                configuration.configure(hibernateConfigFile);
                log("Using configuration file " + hibernateConfigFile);
            }

            final Class[] dialects = HibernateDialectsCatalog.getDialects();
            for (int i = 0; i < dialects.length; i++)
            {
                final Class dialectClass = dialects[i];
                final Dialect dialect = (Dialect) dialectClass.newInstance();
                final String dialectClassName = dialectClass.getName();
                final String dialectShortName = dialectClass.getName().substring(dialectClassName.lastIndexOf('.') + 1);
                final File dialectFile = new File(dialectShortName + destFileExtension);

                final Properties properties = new Properties();
                properties.put(Environment.DIALECT, dialectClass.getName());

                final File createFileFiltered = new File(destDir, createPrefix + dialectFile);
                final File createFileTmp = File.createTempFile(getClass().getName() + "-", "-" + createPrefix + dialectFile);
                createFileTmp.deleteOnExit();

                final File cleanFileFiltered = new File(destDir, cleanPrefix + dialectFile);
                final File cleanFileTmp = File.createTempFile(getClass().getName() + "-", "-" + cleanPrefix + dialectFile);
                cleanFileTmp.deleteOnExit();

                final SchemaExport exporter;
                try
                {
                    // Generates CREATE statements including, quite stupidly, DROP statements which we'll filter later
                    exporter = new SchemaExport(configuration, properties);
                    exporter.setDelimiter(sqlStmtDelimiter);
                    exporter.setOutputFile(createFileTmp.getAbsolutePath());
                    exporter.create(false, false);

                    // Generates DROP statements only
                    exporter.setOutputFile(cleanFileTmp.getAbsolutePath());
                    exporter.drop(false, false);
                }
                catch (HibernateException e)
                {
                    log("Error generating DDL for " + dialectClassName + ": " + e.getMessage());
                    continue;
                }

                final SqlDataDefinitionFilterProcessor createFilterProcessor =
                        new SqlDataDefinitionFilterProcessor(createFilter, configuration, dialect, createFileTmp, createFileFiltered, sqlStmtDelimiter);
                createFilterProcessor.execute();

                final SqlDataDefinitionFilterProcessor cleanFilterProcessor =
                        new SqlDataDefinitionFilterProcessor(cleanFilter, configuration, dialect, cleanFileTmp, cleanFileFiltered, sqlStmtDelimiter);
                cleanFilterProcessor.execute();

                log("Generated create " + dialectShortName + " DDL in " + createFileFiltered.getAbsolutePath() + " ("+ createFilterProcessor.getRemovedLines() +" lines removed, "+ createFilterProcessor.getReplacedLines() +" lines replaced)");
                log("Generated clean " + dialectShortName + " DDL in " + cleanFileFiltered.getAbsolutePath() + " ("+ cleanFilterProcessor.getRemovedLines() +" lines removed, "+ cleanFilterProcessor.getReplacedLines() +" lines replaced)");
            }
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

    public void setHibernateConfigClass(final String cls) throws ClassNotFoundException
    {
        hibernateConfigClass = Class.forName(cls);
    }

    public void setCreateSqlDataDefinitionFilterClass(final String cls) throws ClassNotFoundException
    {
        createSqlDataDefinitionFilterClass = Class.forName(cls);
    }

    public void setSqlStmtDelimiter(String sqlStmtDelimiter)
    {
        this.sqlStmtDelimiter = sqlStmtDelimiter;
    }

    public void setDestFileExtension(String destFileExtension)
    {
        this.destFileExtension = destFileExtension;
    }

    public void setDestDir(File destDir)
    {
        this.destDir = destDir;
    }

    public void setCreatePrefix(String createPrefix)
    {
        this.createPrefix = createPrefix;
    }

    public void setCleanPrefix(String cleanPrefix)
    {
        this.cleanPrefix = cleanPrefix;
    }
}
