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
package com.medigy.persist.util;

import org.hibernate.cfg.NamingStrategy;

import java.io.Serializable;

public class HibernateNamingStrategy implements NamingStrategy, Serializable
{
    /**
     * The singleton instance
     */
    private static final NamingStrategy INSTANCE = new HibernateNamingStrategy();

    public static NamingStrategy getInstance()
    {
        return INSTANCE;
    }

    protected HibernateNamingStrategy() {}

    /**
     * Return the unqualified class name, mixed case converted to
     * underscores
     */
    public String classToTableName(final String className)
    {
        return fixupTableName(unqualify(className));
    }

    /**
     * Return the full property path with underscore seperators, mixed
     * case converted to underscores
     */
    public String propertyToColumnName(final String propertyName)
    {
        return fixupColumnName(propertyName);
    }

    /**
     * Convert mixed case to underscores
     */
    public String tableName(final String tableName)
    {
        return fixupTableName(tableName);
    }

    /**
     * Convert mixed case to underscores
     */
    public String columnName(final String columnName)
    {
        return fixupColumnName(columnName);
    }

    /**
     * Return the full property path prefixed by the unqualified class
     * name, with underscore seperators, mixed case converted to underscores
     */
    public String propertyToTableName(final String className, final String propertyName)
    {
        return classToTableName(className) + '_' + propertyToColumnName(propertyName);
    }

    private String unqualify(final String qualifiedName)
    {
        return qualifiedName.substring(qualifiedName.lastIndexOf(".") + 1);
    }

    /**
     * Take the name of a column and convert it from Java-style mixed case to SQL-style underscores. Table
     * names have mixed in the first letter and after each underscore.
     * @param name
     * @return
     */
    public String fixupTableName(final String name)
    {
        final StringBuffer buf = new StringBuffer(name.replace('.', '_'));
        for (int i = 1; i < buf.length() - 1; i++)
        {
            if ('_' != buf.charAt(i - 1) &&
                Character.isUpperCase(buf.charAt(i)) &&
                !Character.isUpperCase(buf.charAt(i + 1)))
            {
                buf.insert(i++, '_');
            }
        }
        buf.setCharAt(0, Character.toUpperCase(buf.charAt(0)));
        return buf.toString();
    }

    /**
     * Take the name of a column and convert it from Java-style mixed case to SQL-style underscores. Column
     * names are all forced to lowercase as well.
     * @param name
     * @return
     */
    public String fixupColumnName(final String name)
    {
        final StringBuffer buf = new StringBuffer(name.replace('.', '_'));
        for (int i = 1; i < buf.length() - 1; i++)
        {
            if ('_' != buf.charAt(i - 1) &&
                Character.isUpperCase(buf.charAt(i)) &&
                !Character.isUpperCase(buf.charAt(i + 1)))
            {
                buf.insert(i++, '_');
            }
        }
        return buf.toString().toLowerCase();
    }
}
