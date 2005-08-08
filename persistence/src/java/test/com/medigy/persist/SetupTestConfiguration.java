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
 */
package com.medigy.persist;

import com.medigy.persist.util.HibernateConfiguration;
import com.medigy.persist.util.ModelInitializer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.classic.Session;
import org.hibernate.dialect.HSQLDialect;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.LogManager;

public class SetupTestConfiguration
{
    private static final Log log = LogFactory.getLog(SetupTestConfiguration.class);

    public static final String BUILD_PROPERTY_PREFIX = "ant.build.";
    public static final String TEST_DB_PROPERTY_PREFIX = "module.test.database";
    public static final String TEST_DB_DIALECT_PROPERTY = TEST_DB_PROPERTY_PREFIX + ".dialectClass";
    public static final String TEST_DB_DRIVER_PROPERTY = TEST_DB_PROPERTY_PREFIX + ".driverClass";
    public static final String TEST_DB_URL_PROPERTY = TEST_DB_PROPERTY_PREFIX + ".url";
    public static final String TEST_DB_USER_PROPERTY = TEST_DB_PROPERTY_PREFIX + ".user";
    public static final String TEST_DB_PASSWD_PROPERTY = TEST_DB_PROPERTY_PREFIX + ".password";

    protected File databaseDirectory;

    private static EntitySaveListener postInsertEventListener;
    private static Configuration hibernateConfiguration;
    private static SessionFactory sessionFactory;

    private static SetupTestConfiguration testConfiguration = new SetupTestConfiguration();

    private SetupTestConfiguration()
    {
    }

    public static SetupTestConfiguration getInstance()
    {
        return testConfiguration;
    }

    protected static Configuration getConfiguration()
    {
        return hibernateConfiguration;
    }

    protected static void setConfiguration(Configuration cfg)
    {
		SetupTestConfiguration.hibernateConfiguration = cfg;
	}

    protected boolean recreateSchema()
    {
		return true;
	}

    protected String getClassNameWithoutPackage()
    {
       final String pkgAndClassName = getClass().getName();
       final int classNameDelimPos = pkgAndClassName.lastIndexOf('.');
       return classNameDelimPos != -1 ? pkgAndClassName.substring(classNameDelimPos + 1) : pkgAndClassName;
    }

    /**
     * Setup the hibernate configuration properties for a database. You must specify all the property parameters
     * explicitly.
     *
     * @param dialectClassName
     * @param jdbcDriverClassName
     * @param url
     * @param userName
     * @param password
     * @return Properties for hibernate config
     */
    protected Properties setupDatabaseProperties(final String dialectClassName,
                                                     final String jdbcDriverClassName,
                                                     final String url, final String userName,
                                                     final String password)
    {
        final Properties hibProperties = new Properties();
        hibProperties.setProperty(Environment.DIALECT, dialectClassName);
        hibProperties.setProperty(Environment.CONNECTION_PREFIX + ".driver_class", jdbcDriverClassName);
        hibProperties.setProperty(Environment.CONNECTION_PREFIX + ".url", url);
        hibProperties.setProperty(Environment.CONNECTION_PREFIX + ".username", userName);
        hibProperties.setProperty(Environment.CONNECTION_PREFIX + ".password", password);
        if  (recreateSchema())
            hibProperties.setProperty(Environment.HBM2DDL_AUTO, "create-drop");
        hibProperties.setProperty(Environment.SHOW_SQL, "true");
        hibProperties.setProperty(Environment.AUTOCOMMIT, "false");

        return hibProperties;
    }

    /**
     * Setup the hibernate configuration properties for Hypersonic SQL database. This defaults a lot of the
     * property parameters;
     *
     * @return Properties for hibernate config
     */
    protected Properties setupHsqldb()
    {
        final Properties hibProperties = new Properties();
        hibProperties.setProperty(Environment.DIALECT, HSQLDialect.class.getName());
        hibProperties.setProperty(Environment.CONNECTION_PREFIX + ".driver_class", "org.hsqldb.jdbcDriver");
        hibProperties.setProperty(Environment.CONNECTION_PREFIX + ".url", "jdbc:hsqldb:" + databaseDirectory + "/db");
        hibProperties.setProperty(Environment.CONNECTION_PREFIX + ".username", "sa");
        hibProperties.setProperty(Environment.CONNECTION_PREFIX + ".password", "");
        if (recreateSchema())
            hibProperties.setProperty(Environment.HBM2DDL_AUTO, "create-drop");
        hibProperties.setProperty(Environment.SHOW_SQL, "false");
        hibProperties.setProperty(Environment.AUTOCOMMIT, "false");
        return hibProperties;
    }


    protected HibernateConfiguration createHibernateConfiguration() throws HibernateException, FileNotFoundException, IOException
    {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final Properties logProperties = new Properties();
        logProperties.setProperty("handlers", "java.util.logging.ConsoleHandler");
        logProperties.setProperty("java.util.logging.ConsoleHandler.formatter", "java.util.logging.SimpleFormatter");
        logProperties.setProperty("org.hibernate.level", "WARNING");
        logProperties.store(out, "Generated by " + TestCase.class.getName());
        LogManager.getLogManager().readConfiguration(new ByteArrayInputStream(out.toByteArray()));

        setupDatabaseDirectory();
        final HibernateConfiguration config = new HibernateConfiguration();

        if (System.getProperty(TEST_DB_DIALECT_PROPERTY) != null)
        {
            final String dialectName = System.getProperty(TEST_DB_DIALECT_PROPERTY);
            final String databaseUrl = System.getProperty(TEST_DB_URL_PROPERTY);
            final String databaseUserName = System.getProperty(TEST_DB_USER_PROPERTY);
            final String databasePassword = System.getProperty(TEST_DB_PASSWD_PROPERTY);
            final String databaseDriver = System.getProperty(TEST_DB_DRIVER_PROPERTY);
            config.addProperties(setupDatabaseProperties(dialectName, databaseDriver, databaseUrl, databaseUserName,
                databasePassword));
        }
        else if (System.getProperty(BUILD_PROPERTY_PREFIX + TEST_DB_DIALECT_PROPERTY) != null)
        {
            final String dialectName = System.getProperty(BUILD_PROPERTY_PREFIX + TEST_DB_DIALECT_PROPERTY);
            final String databaseUrl = System.getProperty(BUILD_PROPERTY_PREFIX + TEST_DB_URL_PROPERTY);
            final String databaseUserName = System.getProperty(BUILD_PROPERTY_PREFIX + TEST_DB_USER_PROPERTY);
            final String databasePassword = System.getProperty(BUILD_PROPERTY_PREFIX + TEST_DB_PASSWD_PROPERTY);
            final String databaseDriver = System.getProperty(BUILD_PROPERTY_PREFIX + TEST_DB_DRIVER_PROPERTY);
            config.addProperties(setupDatabaseProperties(dialectName, databaseDriver, databaseUrl, databaseUserName,
                databasePassword));
        }
        else
        {
            // assume HSQLDB
            config.addProperties(setupHsqldb());
        }

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


    private void buildSessionFactory() throws Exception
    {
        if (sessionFactory != null)
            sessionFactory.close();

        if (getConfiguration() == null)
        {
            hibernateConfiguration = createHibernateConfiguration();
            postInsertEventListener = new EntitySaveListener();
            getConfiguration().getSessionEventListenerConfig().setPostInsertEventListener(postInsertEventListener);
            //generateSchemaDdl(hibernateConfiguration);
        }
       sessionFactory = getConfiguration().buildSessionFactory();
    }

    /**
     * Sets up the temporary database directory for the Hypersonic SQL database to use
     */
    protected void setupDatabaseDirectory()
    {
        final String systemTempDir = System.getProperty("java.io.tmpdir");
        final String systemFileSep = System.getProperty("file.separator");
        final String testDbDir = System.getProperty("ant.build.module.artifacts.test.db.home", systemTempDir);
        databaseDirectory = new File(testDbDir + systemFileSep + getClassNameWithoutPackage());
        log.info("Test database directory: " + databaseDirectory.getAbsolutePath());
    }

    /**
     * Generates the schema DDL based on the hibernate config
     * @param hibernateConfiguration
     */
    protected void generateSchemaDdl(final Configuration hibernateConfiguration)
    {
        if (databaseDirectory == null)
            throw new RuntimeException("The database directory has not been setup yet.");
        final String systemFileSep = System.getProperty("file.separator");
        // Generate the DDL into a file so we can review it
        SchemaExport se = new SchemaExport(hibernateConfiguration);
        final String dialectName = hibernateConfiguration.getProperties().getProperty(Environment.DIALECT);
        final String dialectShortName = dialectName.substring(dialectName.lastIndexOf('.') + 1);
        se.setOutputFile(databaseDirectory.getAbsolutePath() + systemFileSep + "medigy-" + dialectShortName + ".ddl");
        se.create(false, false);
    }

    public SessionFactory getSessionFactory() throws Exception
    {
        if (sessionFactory == null)
            buildSessionFactory();

        final Session session = sessionFactory.openSession();
        final Transaction transaction = session.beginTransaction();
        try
        {
            if (!ModelInitializer.getInstance().isInitialized())
                ModelInitializer.getInstance().initialize(session, ModelInitializer.SeedDataPopulationType.AUTO, hibernateConfiguration);
            transaction.commit();
        }
        catch (Exception e)
        {
            transaction.rollback();
            log.error(e);
            throw new RuntimeException(e);
        }
        finally
        {
            session.close();
        }
        return sessionFactory;
    }

    public static EntitySaveListener getPostInsertEventListener()
    {
        return postInsertEventListener;
    }
}
