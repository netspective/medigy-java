/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service;

import com.medigy.persist.util.HibernateUtil;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.cfg.Environment;

import java.io.FileOutputStream;
import java.io.InputStream;

public abstract class DbUnitTestCase extends com.medigy.service.TestCase
{
    protected void setUp() throws Exception
    {
        // NOTE: This will disable the SYS GLOBAL party creation and creation of all the reference entities!!!
        initializeModelData = false;
        
        super.setUp();
        IDatabaseConnection dbUnitConn = null;
        dbUnitConn = getDbUnitConnection();
        DatabaseOperation.REFRESH.execute(dbUnitConn, getDataSet());
        dbUnitConn.getConnection().commit();
        dbUnitConn.close();
        dbUnitConn.getConnection().close();
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

    protected void tearDown() throws Exception
    {
        DatabaseOperation.NONE.execute(getDbUnitConnection(), getDataSet());
        super.tearDown();
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

    protected void extractFullDataset(final String datasetFileName) throws Exception
    {
       // database connection
        IDatabaseConnection connection = getDbUnitConnection();

        // full database export
        IDataSet fullDataSet = connection.createDataSet();
        FlatXmlDataSet.write(fullDataSet, new FileOutputStream(datasetFileName));
   }

    public abstract String getDataSetFile();
}
