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
 */
package org.sns.tool.hibernate.analyzer;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.Mapping;
import org.hibernate.type.Type;
import org.hibernate.MappingException;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.Table;
import org.hibernate.mapping.ForeignKey;
import org.hibernate.mapping.Collection;

import org.sns.tool.hibernate.document.diagram.HibernateDiagramGeneratorException;

public class HibernateAnalysis
{
    /**
     * Map of tables and the persistent classes they represent.
     */
    private final Map<Table,PersistentClass> tableToClassMap = new HashMap<Table,PersistentClass>();

    /**
     * The hibernate configuration which contains the classes and tables we'll be documenting.
     */
    private final Configuration configuration;

    /**
     * The dialect we will use when showing the data types
     */
    private final Dialect dialect;

    /**
     * The mappings we're going to use
     */
    private final Mapping mapping;

    /**
     * The list of analysis rules that we're going to use to perform the actual analysis
     */
    private final List<HibernateAnalysisRule> analysisRules;

    /**
     * A list of issues generated by the analysis rules as they are processed
     */
    private final List<HiberateIssue> issues = new ArrayList<HiberateIssue>();

    public HibernateAnalysis(final Configuration configuration, final List<HibernateAnalysisRule> analyzers)
    {
        this.configuration = configuration;
        this.analysisRules = analyzers;

        // the following was copied from org.hibernate.cfg.Configuration.buildMapping() because buildMapping() was private
        this.mapping = new Mapping()
        {
            /**
             * Returns the identifier type of a mapped class
             */
            public Type getIdentifierType(String persistentClass) throws MappingException
            {
                final PersistentClass pc = configuration.getClassMapping(persistentClass);
                if (pc == null) throw new MappingException("persistent class not known: " + persistentClass);
                return pc.getIdentifier().getType();
            }

            public String getIdentifierPropertyName(String persistentClass) throws MappingException
            {
                final PersistentClass pc = configuration.getClassMapping(persistentClass);
                if (pc == null) throw new MappingException("persistent class not known: " + persistentClass);
                if (!pc.hasIdentifierProperty()) return null;
                return pc.getIdentifierProperty().getName();
            }

            public Type getPropertyType(String persistentClass, String propertyName) throws MappingException
            {
                final PersistentClass pc = configuration.getClassMapping(persistentClass);
                if (pc == null) throw new MappingException("persistent class not known: " + persistentClass);
                Property prop = pc.getProperty(propertyName);
                if (prop == null) throw new MappingException("property not known: " + persistentClass + '.' + propertyName);
                return prop.getType();
            }
        };

        String dialectName = configuration.getProperty(Environment.DIALECT);
        if (dialectName == null)
            dialectName = org.hibernate.dialect.GenericDialect.class.getName();

        try
        {
            final Class cls = Class.forName(dialectName);
            this.dialect = (Dialect) cls.newInstance();
        }
        catch (Exception e)
        {
            throw new HibernateDiagramGeneratorException(e);
        }

        for (final Iterator classes = configuration.getClassMappings(); classes.hasNext();)
        {
            final PersistentClass pclass = (PersistentClass) classes.next();
            final Table table = (Table) pclass.getTable();

            tableToClassMap.put(table, pclass);
        }
    }

    public Configuration getConfiguration()
    {
        return configuration;
    }

    public List<HibernateAnalysisRule> getAnalysisRules()
    {
        return analysisRules;
    }

    public List<HiberateIssue> getIssues()
    {
        return issues;
    }

    public Dialect getDialect()
    {
        return dialect;
    }

    public Mapping getMapping()
    {
        return mapping;
    }

    public Map<Table,PersistentClass> getTableToClassMap()
    {
        return tableToClassMap;
    }

    public PersistentClass getClassForTable(final Table table)
    {
        return (PersistentClass) tableToClassMap.get(table);
    }

    public void analyze()
    {
        for(final HibernateAnalysisRule analysisRule : analysisRules)
        {
            if(analysisRule instanceof HibernateGlobalAnalysisRule)
                ((HibernateGlobalAnalysisRule) analysisRule).analyze(this);
        }

        for (final Iterator classes = configuration.getClassMappings(); classes.hasNext();)
        {
            final PersistentClass pclass = (PersistentClass) classes.next();
            for(final HibernateAnalysisRule analysisRule : analysisRules)
            {
                if(analysisRule instanceof HibernateClassAnalysisRule)
                    ((HibernateClassAnalysisRule) analysisRule).analyze(this, pclass);
            }
        }

        for (final Iterator tables = configuration.getTableMappings(); tables.hasNext();)
        {
            final Table table = (Table) tables.next();
            for(final HibernateAnalysisRule analysisRule : analysisRules)
            {
                if(analysisRule instanceof HibernateTableAnalysisRule)
                    ((HibernateTableAnalysisRule) analysisRule).analyze(this, table);

                for (final Iterator fKeys = table.getForeignKeyIterator(); fKeys.hasNext();)
                {
                    final ForeignKey foreignKey = (ForeignKey) fKeys.next();
                    if(analysisRule instanceof HibernateForeignKeyAnalysisRule)
                        ((HibernateForeignKeyAnalysisRule) analysisRule).analyze(this, foreignKey);
                }
            }
        }
    }

    /**
     * Ascertain whether the referenced class in the foreign key relationship is a superclass of the source
     * class.
     *
     * @param foreignKey The foreign key relationship
     *
     * @return True if the source of the foreign key is a subclass of the referenced class
     */
    public boolean isSubclassRelationship(final ForeignKey foreignKey)
    {
        PersistentClass sourceClass = getClassForTable(foreignKey.getTable());
        PersistentClass refClass = getClassForTable(foreignKey.getReferencedTable());
        return refClass.getMappedClass().isAssignableFrom(sourceClass.getMappedClass());
    }

    public boolean isParentRelationship(final ForeignKey foreignKey)
    {
        for (Iterator colls = getConfiguration().getCollectionMappings(); colls.hasNext();)
        {
            final Collection coll = (Collection) colls.next();
            if (coll.isOneToMany())
            {
                if (foreignKey.getReferencedTable() == coll.getOwner().getTable() && foreignKey.getTable() == coll.getCollectionTable())
                    return true;
            }
        }

        return false;
    }
}
