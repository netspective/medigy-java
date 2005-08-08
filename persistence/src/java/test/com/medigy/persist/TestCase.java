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

import com.medigy.persist.model.session.ProcessSession;
import com.medigy.persist.model.session.Session;
import com.medigy.persist.model.session.SessionManager;
import com.medigy.persist.util.HibernateUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dbunit.Assertion;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.CompositeTable;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Environment;
import org.jmock.MockObjectTestCase;
import org.jmock.core.Constraint;

import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Test case based on hibernate test case
 */
public abstract class TestCase extends MockObjectTestCase
{
    private static final Log log = LogFactory.getLog(TestCase.class);

    protected boolean useExternalModelData = false;

    // this is only used to keep track of currently open/used hibernate session
    private org.hibernate.classic.Session session;
    private SessionFactory sessionFactory;

    public org.hibernate.classic.Session openSession() throws HibernateException
    {
        session = sessionFactory.openSession();
        return session;
    }

    protected boolean dropAfterFailure()
    {
        return true;
    }

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

    /*
    protected void runTest() throws Throwable
    {
        final boolean stats = ((SessionFactoryImplementor) sessionFactory).getStatistics().isStatisticsEnabled();
        try
        {
            if (stats) sessionFactory.getStatistics().clear();

            super.runTest();

            if (stats) sessionFactory.getStatistics().logSummary();

            org.hibernate.Session session = openSession();
            Transaction t = session.beginTransaction();
            final List entityList = SetupTestConfiguration.getInstance().getPostInsertEventListener().getEntityList();
            for (Object entity : entityList)
            {
                System.out.println("Deleteing: " + entity.getClass().getName());
                session.refresh(entity);
                session.delete(entity);
            }
            t.commit();
            session.close();
            SessionManager.getInstance().popActiveSession();

            if (session != null && session.isOpen())
            {
                if (session.isConnected()) session.connection().rollback();
                session.close();
                session = null;
                fail("unclosed session");
            }
            else
            {
                session = null;
            }
        }
        catch (Throwable e)
        {
            try
            {
                if (session != null && session.isOpen())
                {
                    if (session.isConnected()) session.connection().rollback();
                    session.close();
                }
            }
            catch (Exception ignore)
            {
            }
            try
            {
                if (dropAfterFailure() && sessionFactory != null)
                {
                    sessionFactory.close();
                    sessionFactory = null;
                }
            }
            catch (Exception ignore)
            {
            }
            throw e;
        }
    }
    */

    protected void setUp() throws Exception
    {
        sessionFactory = SetupTestConfiguration.getInstance().getSessionFactory();
        //setupApplicationSession();
        SetupTestConfiguration.getInstance().getPostInsertEventListener().newEntityList();
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

    protected void setupApplicationSession() throws Exception
    {
        // setup a person here so that we can add a contact information for him/her
        Session processSession = new ProcessSession();
        processSession.setProcessName(getClass().getName() + "." + getName());
        SessionManager.getInstance().pushActiveSession(processSession);

        // TODO: Need a commit?
        final org.hibernate.classic.Session session = openSession();
        session.save(processSession);
        session.close();
    }

    protected void tearDown() throws Exception
    {
        org.hibernate.Session session = openSession();
        Transaction t = session.beginTransaction();
        SetupTestConfiguration.getInstance().getPostInsertEventListener().deleteEntityList(session);
        t.commit();
        session.close();
        SessionManager.getInstance().popActiveSession();
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
}
