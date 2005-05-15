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
package com.medigy.persist;

import com.medigy.persist.model.session.ProcessSession;
import com.medigy.persist.model.session.Session;
import com.medigy.persist.model.session.SessionManager;
import com.medigy.persist.util.HibernateConfiguration;
import com.medigy.persist.util.HibernateUtil;
import com.medigy.persist.util.ModelInitializer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.cfg.Environment;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.jmock.MockObjectTestCase;
import org.jmock.core.Constraint;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.LogManager;

public abstract class TestCase extends MockObjectTestCase
{
    private static final Log log = LogFactory.getLog(TestCase.class);

    protected File databaseDirectory;
    protected boolean initializeModelData = true;

    protected void assertThat(Object something, Constraint matches)
    {
        if (!matches.eval(something))
        {
            StringBuffer message = new StringBuffer("\nExpected: ");
            matches.describeTo(message);
            message.append("\nbut got : ").append(something).append('\n');
            fail(message.toString());
        }
    }

    protected String getClassNameWithoutPackage()
    {
        final String pkgAndClassName = getClass().getName();
        final int classNameDelimPos = pkgAndClassName.lastIndexOf('.');
        return classNameDelimPos != -1 ? pkgAndClassName.substring(classNameDelimPos + 1) : pkgAndClassName;
    }

    protected HibernateConfiguration getHibernateConfiguration() throws HibernateException, FileNotFoundException, IOException
    {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final Properties logProperties = new Properties();
        logProperties.setProperty("handlers", "java.util.logging.ConsoleHandler");
        logProperties.setProperty("java.util.logging.ConsoleHandler.formatter", "java.util.logging.SimpleFormatter");
        logProperties.setProperty("org.hibernate.level", "WARNING");
        logProperties.store(out, "Generated by " + TestCase.class.getName());
        LogManager.getLogManager().readConfiguration(new ByteArrayInputStream(out.toByteArray()));

        final HibernateConfiguration config = new HibernateConfiguration();

        final Properties hibProperties = new Properties();
        //hibProperties.setProperty(Environment.DIALECT, HSQLDialect.class.getName());
        hibProperties.setProperty(Environment.CONNECTION_PREFIX + ".driver_class", "org.hsqldb.jdbcDriver");
        hibProperties.setProperty(Environment.CONNECTION_PREFIX + ".url", "jdbc:hsqldb:" + databaseDirectory + "/db");
        hibProperties.setProperty(Environment.CONNECTION_PREFIX + ".username", "sa");
        hibProperties.setProperty(Environment.CONNECTION_PREFIX + ".password", "");
        hibProperties.setProperty(Environment.HBM2DDL_AUTO, "create-drop");
        hibProperties.setProperty(Environment.SHOW_SQL, "false");
        config.addProperties(hibProperties);

        try
        {
            config.configure("com/medigy/persist/hibernate.cfg.xml");
            config.buildMappings();
        }
        catch (HibernateException e)
        {
            log.error(e);
            throw new RuntimeException(e);
        }

        return config;
    }




    protected void setUp() throws Exception
    {
        super.setUp();
        initializeModelData = true;
        setupDatabaseDirectory();
        final HibernateConfiguration hibernateConfiguration = getHibernateConfiguration();
        HibernateUtil.setConfiguration(hibernateConfiguration);
        setupModelInitializer(hibernateConfiguration);
        generateSchemaDdl(hibernateConfiguration);
        setupSession();
    }

    protected void setupDatabaseDirectory()
    {
        final String systemTempDir = System.getProperty("java.io.tmpdir");
        final String systemFileSep = System.getProperty("file.separator");
        final String testDbDir = System.getProperty("ant.build.module.artifacts.test.db.home", systemTempDir);
        databaseDirectory = new File(testDbDir + systemFileSep + getClassNameWithoutPackage());
        log.info("Test database directory: " + databaseDirectory.getAbsolutePath());
    }

    protected void generateSchemaDdl(final HibernateConfiguration hibernateConfiguration)
    {
        final String systemFileSep = System.getProperty("file.separator");
        // Generate the DDL into a file so we can review it
        SchemaExport se = new SchemaExport(hibernateConfiguration);
        final String dialectName = hibernateConfiguration.getProperties().getProperty(Environment.DIALECT);
        final String dialectShortName = dialectName.substring(dialectName.lastIndexOf('.') + 1);
        se.setOutputFile(databaseDirectory.getAbsolutePath() + systemFileSep + "medigy-" + dialectShortName + ".ddl");
        se.create(false, false);
    }

    protected void setupModelInitializer(final HibernateConfiguration hibernateConfiguration) throws Exception
    {
        System.out.println(initializeModelData);
        if (initializeModelData)
            new ModelInitializer(HibernateUtil.getSession(),
                             ModelInitializer.SeedDataPopulationType.AUTO,
                             hibernateConfiguration).initialize();
        else
            new ModelInitializer(HibernateUtil.getSession(),
                             ModelInitializer.SeedDataPopulationType.NO,
                             hibernateConfiguration).initialize();
    }

    protected void setupSession() throws Exception
    {
        // setup a person here so that we can add a contact information for him/her
        Session session = new ProcessSession();
        session.setProcessName(getClass().getName() + "." + getName());
        SessionManager.getInstance().pushActiveSession(session);
        HibernateUtil.getSession().save(session);
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
        SessionManager.getInstance().popActiveSession();
        HibernateUtil.closeSession();
    }


}
