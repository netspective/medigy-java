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
package com.medigy.tool.persist.loader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import com.medigy.persist.util.DelimitedValuesParser;
import com.medigy.persist.util.DelimitedValuesReader;
import com.medigy.tool.persist.loader.EntityLoader.EventHandler;

public class EntityLoaderTask extends Task
{
    private Class hibernateConfigClass;
    private String hibernateConfigFile;
    private String dialect;
    private String url;
    private String driver;
    private String userid;
    private String password;
    private boolean showSql;
    private char delimiter = DelimitedValuesParser.DEFAULT_SEP;
    private int commitRowsInterval = 250;
    private File file;
    private URL resource;
    private Class entity;
    private String[] propertyNames;
    private boolean ignoreBlankLines = true;
    private boolean validateFieldsCount = true;
    private boolean throwExceptionOnValidationError = false;
    private Class modifierClass;
    private int stopAfterErrors = 10;
    private String beforeLoadUpdateHQL;

    public void execute() throws BuildException
    {
        if(hibernateConfigClass == null)
            throw new BuildException("hibernateConfigClass was not provided.");

        if(url == null || driver == null || userid == null || password == null)
            throw new BuildException("url, driver, userid, and password are required attributes.");

        if(file == null && resource == null)
            throw new BuildException("Either a file or resource name should be provided.");

        final Configuration configuration;
        try
        {
            configuration = (Configuration) hibernateConfigClass.newInstance();
            if(hibernateConfigFile != null)
                configuration.configure(hibernateConfigFile);

            if(dialect != null)
                configuration.setProperty(Environment.DIALECT, dialect);

            configuration.setProperty(Environment.DRIVER, driver);
            configuration.setProperty(Environment.URL, url);
            configuration.setProperty(Environment.USER, userid);
            configuration.setProperty(Environment.PASS, password);

            if(showSql)
                configuration.setProperty(Environment.SHOW_SQL, "true");
        }
        catch (final Exception e)
        {
            throw new BuildException(e);
        }

        log("Using Hibernate Configuration " + configuration.getClass());
        log("Using Driver " + configuration.getProperty(Environment.DRIVER));
        log("Using Dialect " + configuration.getProperty(Environment.DIALECT));
        log("Using URL " + configuration.getProperty(Environment.URL));
        log("Using User ID " + configuration.getProperty(Environment.USER));
        log("Showing SQL " + configuration.getProperty(Environment.SHOW_SQL));

        final SessionFactory sessionFactory = configuration.buildSessionFactory();
        final Session session = sessionFactory.openSession();
        try
        {
            loadData(session);
        }
        catch(final Exception e)
        {
            throw new BuildException(e);
        }
        finally
        {
            session.close();
        }
    }

    protected void loadData(final Session session) throws IOException, IllegalAccessException, InstantiationException
    {
        final EventHandler eventHandler = new EventHandler()
        {
            public void log(final String message)
            {
                EntityLoaderTask.this.log(message);
            }

            public void onCommit(EntityLoader loader, DelimitedValuesReader dvReader)
            {
                log("Committed "+ getCommitRowsInterval() +" records at line " + dvReader.getReader().getLineNumber());
            }

            public int getCommitRowsInterval()
            {
                return commitRowsInterval;
            }
        };

        final EntityLoaderModifier modifier = modifierClass != null ? (EntityLoaderModifier) modifierClass.newInstance() : null;
        final EntityLoader loader = new EntityLoader(modifier, session, eventHandler, entity, propertyNames);
        final Reader source = file != null ? new FileReader(file) : new InputStreamReader(resource.openStream());
        final String readerId = file != null ? file.getAbsolutePath() : resource.toExternalForm();
        final DelimitedValuesParser dvp = new DelimitedValuesParser(delimiter);
        final int skipFirstRows = entity != null && propertyNames != null ? 0 : 1;

        log("Using source " + readerId + " with modifier " + modifier);
        log("Using loader " + loader + " with commit interval " + eventHandler.getCommitRowsInterval());
        log("Using " + skipFirstRows + " header rows.");

        final DelimitedValuesReader dvr = new DelimitedValuesReader(loader, source,
                readerId, dvp, skipFirstRows,
                ignoreBlankLines, null, null, validateFieldsCount, DelimitedValuesReader.AUTO_CALC_EXPECTED_FIELDS,
                throwExceptionOnValidationError, stopAfterErrors);

        try
        {
            if(beforeLoadUpdateHQL != null)
            {
                log("Executing session.createQuery(\""+ beforeLoadUpdateHQL + "\").executeUpdate()");
                session.createQuery(beforeLoadUpdateHQL).executeUpdate();
            }

            dvr.readAll();
        }
        finally
        {
            loader.getActiveTransaction().commit();
            log("Committed final set of records at line " + dvr.getReader().getLineNumber());
        }

        for(final String error : dvr.getErrors())
        {
            log(error);
        }
    }

    public void setHibernateConfigClass(final String cls) throws ClassNotFoundException
    {
        hibernateConfigClass = Class.forName(cls);
    }

    public void setDialect(final String dialect)
    {
        this.dialect = dialect;
    }

    public void setHibernateConfigFile(final String hibernateConfigFile)
    {
        this.hibernateConfigFile = hibernateConfigFile;
    }

    public void setPassword(final String password)
    {
        this.password = password;
    }

    public void setUrl(final String url)
    {
        this.url = url;
    }

    public void setDriver(final String driver)
    {
        this.driver = driver;
    }

    public void setUserid(final String userid)
    {
        this.userid = userid;
    }

    public void setShowSql(final boolean showSql)
    {
        this.showSql = showSql;
    }

    public void setDelimiter(final char delimiter)
    {
        this.delimiter = delimiter;
    }

    public void setCommitRowsInterval(final int commitRowsInterval)
    {
        this.commitRowsInterval = commitRowsInterval;
    }

    public void setFile(final File file)
    {
        this.file = file;
        if(! file.exists())
        {
            log("File " + file + " was not found.");
            this.file = null;
        }
    }

    public void setResource(final String resourceName)
    {
        this.resource = getClass().getResource(resourceName);
        if(this.resource == null)
            log("Resource " + resourceName + " was not found relative to " + getClass());
    }

    public void setEntity(final String entityName) throws ClassNotFoundException
    {
        this.entity = Class.forName(entityName);
    }

    public void setPropertyNames(final String propertyNames)
    {
        this.propertyNames = propertyNames.split(",");
    }

    public void setIgnoreBlankLines(boolean ignoreBlankLines)
    {
        this.ignoreBlankLines = ignoreBlankLines;
    }

    public void setValidateFieldsCount(boolean validateFieldsCount)
    {
        this.validateFieldsCount = validateFieldsCount;
    }

    public void setThrowExceptionOnValidationError(boolean throwExceptionOnValidationError)
    {
        this.throwExceptionOnValidationError = throwExceptionOnValidationError;
    }

    public void setModifierClass(final String modifierClass) throws ClassNotFoundException
    {
        this.modifierClass = Class.forName(modifierClass);
    }

    public void setStopAfterErrors(int stopAfterErrors)
    {
        this.stopAfterErrors = stopAfterErrors;
    }

    public void setBeforeLoadUpdateHQL(String beforeLoadUpdateHQL)
    {
        this.beforeLoadUpdateHQL = beforeLoadUpdateHQL;
    }
}
