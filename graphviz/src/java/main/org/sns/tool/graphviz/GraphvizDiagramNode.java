/*
 * Copyright (c) 2000-2004 Netspective Communications LLC. All rights reserved.
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
 *    used to endorse or appear in products derived from The Software without written consent of Netspective.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT,
 * ARE HEREBY DISCLAIMED.
 *
 * NETSPECTIVE AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE OR ANY THIRD PARTY AS A
 * RESULT OF USING OR DISTRIBUTING THE SOFTWARE. IN NO EVENT WILL NETSPECTIVE OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THE SOFTWARE, EVEN
 * IF IT HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 */
package org.sns.tool.graphviz;

import java.util.HashMap;
import java.util.Map;

public class GraphvizDiagramNode
{
    private static final String ATTRNAME_SHAPE = "shape";

    /**
     * The node identifier (arbitrary)
     */
    private String identifier;

    /**
     * The node attributes
     */
    private Map attributes = new HashMap();

    public GraphvizDiagramNode(GraphvizDiagramGenerator generator, String identifier)
    {
        this.identifier = identifier;
        this.attributes.putAll(generator.getDefaultNodeAttributes());

        if(this.identifier == null)
            throw new NullPointerException("Node identifier may not be null.");
    }

    public String getLabel()
    {
        return (String) attributes.get(GraphvizDiagramGenerator.ATTRNAME_LABEL);
    }

    public void setLabel(String label)
    {
        attributes.put(GraphvizDiagramGenerator.ATTRNAME_LABEL, label);
    }

    public String getShape()
    {
        return (String) attributes.get(ATTRNAME_SHAPE);
    }

    public void setShape(String shape)
    {
        attributes.put(ATTRNAME_SHAPE, shape);
    }

    public String getFontName()
    {
        return (String) attributes.get(GraphvizDiagramGenerator.ATTRNAME_FONTNAME);
    }

    public void setFontName(String fontName)
    {
        attributes.put(GraphvizDiagramGenerator.ATTRNAME_FONTNAME, fontName);
    }

    public String getFontSize()
    {
        return (String) attributes.get(GraphvizDiagramGenerator.ATTRNAME_FONTSIZE);
    }

    public void setFontSize(String fontName)
    {
        attributes.put(GraphvizDiagramGenerator.ATTRNAME_FONTSIZE, fontName);
    }

    public String getColor()
    {
        return (String) attributes.get(GraphvizDiagramGenerator.ATTRNAME_COLOR);
    }

    public void setColor(String fontName)
    {
        attributes.put(GraphvizDiagramGenerator.ATTRNAME_COLOR, fontName);
    }

    public String getIdentifier()
    {
        return identifier;
    }

    public Map getAttributes()
    {
        return attributes;
    }
}
