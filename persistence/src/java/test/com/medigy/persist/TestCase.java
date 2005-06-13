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
import org.dbunit.Assertion;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.CompositeTable;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.HibernateException;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.HSQLDialect;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.jmock.MockObjectTestCase;
import org.jmock.core.Constraint;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.LogManager;

public abstract class TestCase extends MockObjectTestCase
{
    private static final Log log = LogFactory.getLog(TestCase.class);

    public static final String BUILD_PROPERTY_PREFIX = "ant.build.";

    public static final String TEST_DB_PROPERTY_PREFIX = "module.test.database";
    public static final String TEST_DB_DIALECT_PROPERTY = TEST_DB_PROPERTY_PREFIX + ".dialectClass";
    public static final String TEST_DB_DRIVER_PROPERTY = TEST_DB_PROPERTY_PREFIX + ".driverClass";
    public static final String TEST_DB_URL_PROPERTY = TEST_DB_PROPERTY_PREFIX + ".url";
    public static final String TEST_DB_USER_PROPERTY = TEST_DB_PROPERTY_PREFIX + ".user";
    public static final String TEST_DB_PASSWD_PROPERTY = TEST_DB_PROPERTY_PREFIX + ".password";

    protected File databaseDirectory;
    protected boolean useExternalModelData = false;

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
        hibProperties.setProperty(Environment.HBM2DDL_AUTO, "create-drop");
        hibProperties.setProperty(Environment.SHOW_SQL, "false");
        return hibProperties;
    }

    protected Properties setupHsqldb()
    {
        final Properties hibProperties = new Properties();
        hibProperties.setProperty(Environment.DIALECT, HSQLDialect.class.getName());
        hibProperties.setProperty(Environment.CONNECTION_PREFIX + ".driver_class", "org.hsqldb.jdbcDriver");
        hibProperties.setProperty(Environment.CONNECTION_PREFIX + ".url", "jdbc:hsqldb:" + databaseDirectory + "/db");
        hibProperties.setProperty(Environment.CONNECTION_PREFIX + ".username", "sa");
        hibProperties.setProperty(Environment.CONNECTION_PREFIX + ".password", "");
        hibProperties.setProperty(Environment.HBM2DDL_AUTO, "create-drop");
        hibProperties.setProperty(Environment.SHOW_SQL, "false");
        return hibProperties;
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

    protected void setUp() throws Exception
    {
        super.setUp();
        if (getDataSetFile() != null)
            useExternalModelData = true;
        final HibernateConfiguration hibernateConfiguration = getHibernateConfiguration();
        HibernateUtil.setConfiguration(hibernateConfiguration);
        setupModelInitializer(hibernateConfiguration);
        generateSchemaDdl(hibernateConfiguration);
        setupSession();
        HibernateUtil.enableStatistics();
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

     /**
     * This method returns a DbUnit database connection
     * based on the schema name
     */
    protected IDatabaseConnection getDbUnitConnection() throws Exception
    {
        IDatabaseConnection connection = new DatabaseConnection(HibernateUtil.getNewConnection());
        return connection;
    }

    protected IDataSet getDataSet() throws Exception
    {
        InputStream stream = Environment.class.getResourceAsStream(getDataSetFile());
		if (stream == null)
            stream = Thread.currentThread().getContextClassLoader().getResourceAsStream( getDataSetFile() );
		if (stream == null)
			throw new RuntimeException(getDataSetFile() + " not found");

        return new FlatXmlDataSet(stream);
    }

    protected void setupModelInitializer(final HibernateConfiguration hibernateConfiguration) throws Exception
    {
        if (useExternalModelData)
        {
            IDatabaseConnection dbUnitConn = getDbUnitConnection();
            //DatabaseOperation.REFRESH.execute(dbUnitConn, getDataSet());
            DatabaseOperation.CLEAN_INSERT.execute(dbUnitConn, getDataSet());
            dbUnitConn.getConnection().commit();
            dbUnitConn.getConnection().close();
            dbUnitConn.close();
        }
        if (!useExternalModelData)
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
        HibernateUtil.logStatistics();
        if (useExternalModelData)
            DatabaseOperation.NONE.execute(getDbUnitConnection(), getDataSet());
        super.tearDown();

        SessionManager.getInstance().popActiveSession();
        HibernateUtil.closeSession();

        /*
        if (databaseDirectory != null)
        {
            try
            {

                final File[] dbFiles = databaseDirectory.listFiles();
                for (File file : dbFiles)
                    file.delete();
            }
            catch (Exception e)
            {
                log.error(ExceptionUtils.getStackTrace(e));
            }
        }
        */
    }

    /**
     * Overwrite this method if you want to use an outside dataset file instead of using the built-in reference
     * entities
     *
     * @return
     */
    public String getDataSetFile()
    {
        return null;
    }

    /**
     * Utility method to make sure that the XML data set file reflects whats currently in the database
     *
     * @param tableNames
     * @throws Exception
     */
    protected void assertDBAsExpected(String[] tableNames)
            throws Exception
    {
        // Fetch database data after executing your code
        IDatabaseConnection dbUnitConn = null;
        try
        {
            dbUnitConn = getDbUnitConnection();
            IDataSet actualDataSet = dbUnitConn.createDataSet(tableNames);
            // Load expected data from an XML dataset
            IDataSet expectedDataSet = getDataSet();
            for (int i = 0; i < tableNames.length; i++)
            {
                ITable expected = expectedDataSet.getTable(tableNames[i]);
                ITable actual = actualDataSet.getTable(tableNames[i]);
                // converts actual to use the same colums as expected
                ITable trimmedActual = new CompositeTable(expected.getTableMetaData(),
                        actual);
                Assertion.assertEquals(expected, trimmedActual);
            }
        }
        finally
        {
            if (null != dbUnitConn)
            {
                dbUnitConn.close();
            }
        }
    }

    /**
     * Utility method for extracting an existing database as an XML file
     *
     * @param datasetFileName
     * @throws Exception
     */
    protected void extractFullDataset(final String datasetFileName) throws Exception
    {
       // database connection
        IDatabaseConnection connection = getDbUnitConnection();

        // full database export
        IDataSet fullDataSet = connection.createDataSet();
        FlatXmlDataSet.write(fullDataSet, new FileOutputStream(datasetFileName));
   }

    /**
     * Gets the hibernate session
     * @return
     */
    protected org.hibernate.Session getSession()
    {
        return HibernateUtil.getSession();
    }
}
