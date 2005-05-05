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

import java.io.FileOutputStream;
import java.io.InputStream;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.cfg.Environment;

import com.medigy.persist.util.HibernateUtil;

public abstract class DbUnitTestCase extends TestCase
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
