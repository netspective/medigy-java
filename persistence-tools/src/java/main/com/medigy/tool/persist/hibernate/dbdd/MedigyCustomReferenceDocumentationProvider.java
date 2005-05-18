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
package com.medigy.tool.persist.hibernate.dbdd;

import java.util.Map;

import org.hibernate.mapping.PersistentClass;
import org.sns.tool.hibernate.dbdd.DatabaseDesignGeneratorConfig;
import org.sns.tool.hibernate.dbdd.MappedClassDocumentationProvider;
import org.sns.tool.hibernate.struct.TableStructureNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.medigy.persist.reference.custom.CachedCustomReferenceEntity;
import com.medigy.persist.util.HibernateConfiguration;

public class MedigyCustomReferenceDocumentationProvider implements MappedClassDocumentationProvider
{
    public void provideDocumentationFragments(final DatabaseDesignGeneratorConfig generator, final TableStructureNode node, final Element parentElement)
    {
        final PersistentClass persistentClass = node.getPersistentClass();

        final HibernateConfiguration hibernateConfiguration = (HibernateConfiguration) generator.getTableStructure().getConfiguration();
        final Map<Class, Class> refEntityAndCachesMap = hibernateConfiguration.getCustomReferenceEntitiesAndCachesMap();
        final Class refEntityCacheEnum = refEntityAndCachesMap.get(persistentClass.getMappedClass());
        final Document doc = parentElement.getOwnerDocument();

        if(refEntityCacheEnum == null)
        {
            final Element paraElem = (Element) parentElement.appendChild(doc.createElement("para"));
            paraElem.appendChild(doc.createTextNode("No domain values found for " + persistentClass.getMappedClass() + ". HibernateConfiguration does not seem to contain the enum."));
            return;
        }

        final Element tableColumnsTableElem = (Element) parentElement.appendChild(doc.createElement("table"));
        tableColumnsTableElem.setAttribute("tabstyle", "table-domain-values");
        tableColumnsTableElem.appendChild(doc.createElement("title")).appendChild(doc.createTextNode(node.getTable().getName() + " Domain Values"));

        final Element tableGroupElem = (Element) tableColumnsTableElem.appendChild(doc.createElement("tgroup"));
        final Element tableColumnsHeadElem = (Element) tableGroupElem.appendChild(doc.createElement("thead"));
        final Element tableColumnsHeadRowElem = (Element) tableColumnsHeadElem.appendChild(doc.createElement("row"));
        tableColumnsHeadRowElem.appendChild(doc.createElement("entry")).appendChild(doc.createTextNode("Java Constant"));
        tableColumnsHeadRowElem.appendChild(doc.createElement("entry")).appendChild(doc.createTextNode("Code"));
        tableColumnsHeadRowElem.appendChild(doc.createElement("entry")).appendChild(doc.createTextNode("Label"));
        tableGroupElem.setAttribute("cols", Integer.toString(tableColumnsHeadRowElem.getChildNodes().getLength()));

        final Element tableColumnsBodyElem = (Element) tableGroupElem.appendChild(doc.createElement("tbody"));

        final Object[] dataRows = refEntityCacheEnum.getEnumConstants();
        if (dataRows != null && dataRows.length > 0)
        {
            for (int i = 0; i < dataRows.length; i++)
            {
                final CachedCustomReferenceEntity row = (CachedCustomReferenceEntity) dataRows[i];

                final Element rowElem = (Element) tableColumnsBodyElem.appendChild(doc.createElement("row"));

                final Element constElem = (Element) rowElem.appendChild(doc.createElement("entry"));
                constElem.setAttribute("role", "domain-value-constant");
                constElem.appendChild(doc.createTextNode(row.toString()));

                final Element codeElem = (Element) rowElem.appendChild(doc.createElement("entry"));
                codeElem.setAttribute("role", "domain-value-code");
                codeElem.appendChild(doc.createTextNode(row.getCode()));

                final Element labelElem = (Element) rowElem.appendChild(doc.createElement("entry"));
                labelElem.setAttribute("role", "domain-value-label");
                labelElem.appendChild(doc.createTextNode(row.getLabel()));
            }
        }
    }


}
