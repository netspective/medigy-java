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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.Dialect;

public class SqlDataDefinitionFilterProcessor
{
    private final SqlDataDefinitionFilter filter;
    private final Configuration configuration;
    private final Dialect dialect;
    private final File originalFile;
    private final File destFile;
    private final String stmtDelim;
    private int removedLines = 0;
    private int replacedLines = 0;

    public SqlDataDefinitionFilterProcessor(final SqlDataDefinitionFilter filter, 
                                            final Configuration configuration, 
                                            final Dialect dialect, 
                                            final File originalFile, 
                                            final File destFile,
                                            final String stmtDelim)
    {
        this.filter = filter;
        this.configuration = configuration;
        this.dialect = dialect;
        this.originalFile = originalFile;
        this.destFile = destFile;
        this.stmtDelim = stmtDelim;
    }

    public void execute() throws IOException
    {
        // hibernate stupidly includes drop statements at the top of the create scripts so we need to filter them
        final BufferedReader reader = new BufferedReader(new FileReader(originalFile));
        final BufferedWriter writer = new BufferedWriter(new FileWriter(destFile));
        try
        {
            filter.prependStatements(writer, configuration, dialect, stmtDelim);
            
            String statement = null;
            while ((statement = reader.readLine()) != null)
            {
                if(filter.isRetainStatement(configuration, dialect, statement))
                {
                    final String translated = filter.getTranslatedStatement(configuration, dialect, statement);
                    writer.write(translated);
                    writer.newLine();

                    if(! translated.equals(statement))
                        replacedLines++;
                }
                else
                    removedLines++;
            }

            filter.appendStatements(writer, configuration, dialect, stmtDelim);
        }
        finally
        {
            reader.close();
            writer.close();
        }        
    }

    public Configuration getConfiguration()
    {
        return configuration;
    }

    public File getDestFile()
    {
        return destFile;
    }

    public Dialect getDialect()
    {
        return dialect;
    }

    public SqlDataDefinitionFilter getFilter()
    {
        return filter;
    }

    public File getOriginalFile()
    {
        return originalFile;
    }

    public int getRemovedLines()
    {
        return removedLines;
    }

    public int getReplacedLines()
    {
        return replacedLines;
    }
}
