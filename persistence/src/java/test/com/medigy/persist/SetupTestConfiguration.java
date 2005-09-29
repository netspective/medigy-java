package com.medigy.persist;

import com.medigy.persist.util.Ejb3ModelInitializer;
import com.medigy.persist.util.HibernateConfiguration;
import com.medigy.persist.util.HibernateModelInitializer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.classic.Session;
import org.hibernate.dialect.HSQLDialect;
import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.event.EventListeners;
import org.hibernate.event.PostInsertEventListener;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
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
    public static final String TEST_DB_RECREATE_SCHEMA_PROPERTY = TEST_DB_PROPERTY_PREFIX + ".recreateSchema";
    public static final String TEST_DB_DIALECT_PROPERTY = TEST_DB_PROPERTY_PREFIX + ".dialectClass";
    public static final String TEST_DB_DRIVER_PROPERTY = TEST_DB_PROPERTY_PREFIX + ".driverClass";
    public static final String TEST_DB_URL_PROPERTY = TEST_DB_PROPERTY_PREFIX + ".url";
    public static final String TEST_DB_USER_PROPERTY = TEST_DB_PROPERTY_PREFIX + ".user";
    public static final String TEST_DB_PASSWD_PROPERTY = TEST_DB_PROPERTY_PREFIX + ".password";

    protected File databaseDirectory;

    private static EntitySaveListener postInsertEventListener;
    private static Configuration hibernateConfiguration;
    private static SessionFactory sessionFactory;

    private static EntityManagerFactory entityManagerFactory;
    private static Ejb3Configuration ejb3Configuration;

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

    protected static Ejb3Configuration getEjb3Configuration()
    {
        return ejb3Configuration;
    }

    protected static void setConfiguration(Configuration cfg)
    {
		SetupTestConfiguration.hibernateConfiguration = cfg;
	}

    protected boolean recreateSchema()
    {
        if (System.getProperty(TEST_DB_RECREATE_SCHEMA_PROPERTY) != null)
        {
            return Boolean.parseBoolean(System.getProperty(TEST_DB_RECREATE_SCHEMA_PROPERTY));
        }
        else if (System.getProperty(BUILD_PROPERTY_PREFIX + TEST_DB_RECREATE_SCHEMA_PROPERTY) != null)
        {
            return Boolean.parseBoolean(System.getProperty(BUILD_PROPERTY_PREFIX + TEST_DB_RECREATE_SCHEMA_PROPERTY));
        }
        else
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
     * @return Properties for  config
     */
    protected Properties setupDatabaseProperties()
    {
        String dialectClassName;
        String driverName;
        String url;
        String userName;
        String password;

        if (System.getProperty(TEST_DB_DIALECT_PROPERTY) != null)
        {
            dialectClassName = System.getProperty(TEST_DB_DIALECT_PROPERTY);
            url = System.getProperty(TEST_DB_URL_PROPERTY);
            userName = System.getProperty(TEST_DB_USER_PROPERTY);
            password = System.getProperty(TEST_DB_PASSWD_PROPERTY);
            driverName = System.getProperty(TEST_DB_DRIVER_PROPERTY);
        }
        else if (System.getProperty(BUILD_PROPERTY_PREFIX + TEST_DB_DIALECT_PROPERTY) != null)
        {
            dialectClassName = System.getProperty(BUILD_PROPERTY_PREFIX + TEST_DB_DIALECT_PROPERTY);
            url = System.getProperty(BUILD_PROPERTY_PREFIX + TEST_DB_URL_PROPERTY);
            userName = System.getProperty(BUILD_PROPERTY_PREFIX + TEST_DB_USER_PROPERTY);
            password = System.getProperty(BUILD_PROPERTY_PREFIX + TEST_DB_PASSWD_PROPERTY);
            driverName = System.getProperty(BUILD_PROPERTY_PREFIX + TEST_DB_DRIVER_PROPERTY);
        }
        else
        {
            // assume HSQLDB
            dialectClassName = HSQLDialect.class.getName();
            url = "jdbc:hsqldb:" + databaseDirectory + "/db";
            userName = "sa";
            password = "";
            driverName = "org.hsqldb.jdbcDriver";
        }

        final Properties hibProperties = new Properties();
        hibProperties.setProperty(Environment.DIALECT, dialectClassName);
        hibProperties.setProperty(Environment.CONNECTION_PREFIX + ".driver_class", driverName);
        hibProperties.setProperty(Environment.CONNECTION_PREFIX + ".url", url);
        hibProperties.setProperty(Environment.CONNECTION_PREFIX + ".username", userName);
        hibProperties.setProperty(Environment.CONNECTION_PREFIX + ".password", password);
        if  (recreateSchema())
            hibProperties.setProperty(Environment.HBM2DDL_AUTO, "create-drop");
        hibProperties.setProperty(Environment.SHOW_SQL, "true");
        hibProperties.setProperty(Environment.AUTOCOMMIT, "false");
        return hibProperties;
    }

    protected Ejb3Configuration createEjb3Configuration()
    {
        final Ejb3Configuration ejb3Configuration = new Ejb3Configuration();
        ejb3Configuration.configure("com/medigy/persist/hibernate.cfg.xml");
        ejb3Configuration.addProperties(setupDatabaseProperties());

        return ejb3Configuration;
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
        config.addProperties(setupDatabaseProperties());

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
            getConfiguration().getEventListeners().setPostInsertEventListeners(new PostInsertEventListener[] { postInsertEventListener });
            //generateSchemaDdl(hibernateConfiguration);
        }
       sessionFactory = getConfiguration().buildSessionFactory();
    }

    private void buildEntityManagerFactory()
    {
        if (entityManagerFactory != null)
            entityManagerFactory.close();

        if (getEjb3Configuration() == null)
        {
            ejb3Configuration = createEjb3Configuration();
            postInsertEventListener = new EntitySaveListener();
            //getEjb3Configuration().getSessionEventListenerConfig().setPostInsertEventListener(postInsertEventListener);
            final EventListeners eventListeners = getEjb3Configuration().getEventListeners();
            eventListeners.setPostInsertEventListeners(new PostInsertEventListener[] { postInsertEventListener });
        }
        entityManagerFactory = getEjb3Configuration().createEntityManagerFactory();
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

    public EntityManagerFactory getEntityManagerFactory()
    {
        if (entityManagerFactory == null)
            buildEntityManagerFactory();

        final EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = null;
        try
        {
            transaction = em.getTransaction();
            transaction.begin();
            if (!Ejb3ModelInitializer.getInstance().isInitialized())
            {
                if (recreateSchema())
                    Ejb3ModelInitializer.getInstance().initialize(em, Ejb3ModelInitializer.SeedDataPopulationType.AUTO, ejb3Configuration);
                else
                    Ejb3ModelInitializer.getInstance().initialize(em, Ejb3ModelInitializer.SeedDataPopulationType.NO, ejb3Configuration);
            }
            transaction.commit();
        }
        catch (Exception e)
        {
            if (transaction != null)
                transaction.rollback();
            log.error(e);
            throw new RuntimeException(e);
        }
        finally
        {
            em.close();
        }
        return entityManagerFactory;
    }

    public SessionFactory getSessionFactory() throws Exception
    {
        if (sessionFactory == null)
            buildSessionFactory();

        final Session session = sessionFactory.openSession();
        final Transaction transaction = session.beginTransaction();
        try
        {
            if (!HibernateModelInitializer.getInstance().isInitialized())
            {
                if (recreateSchema())
                    HibernateModelInitializer.getInstance().initialize(session, HibernateModelInitializer.SeedDataPopulationType.AUTO, hibernateConfiguration);
                else
                    HibernateModelInitializer.getInstance().initialize(session, HibernateModelInitializer.SeedDataPopulationType.NO, hibernateConfiguration);
            }
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
