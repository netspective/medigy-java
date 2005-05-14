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

public class GraphvizDiagramEdge
{
    public static final String ARROWHEADSTYLE_CROW = "crow";
    private static final String ATTRNAME_ARROWHEAD = "arrowhead";
    private static final String ATTRNAME_ARROWSIZE = "arrowsize";
    private static final String ATTRNAME_STYLE = "style";

    private GraphvizDiagramGenerator generator;

    /**
     * The node attributes
     */
    private Map attributes = new HashMap();

    /**
     * The source of the edge
     */
    private String source;

    /**
     * The destination of the edge
     */
    private String destintation;

    public GraphvizDiagramEdge(GraphvizDiagramGenerator generator, String source, String destintation)
    {
        this.generator = generator;
        this.attributes.putAll(generator.getDefaultEdgeAttributes());
        this.source = source;
        this.destintation = destintation;

        if (this.source == null)
            throw new NullPointerException("Edge source may not be NULL.");

        if (this.destintation == null)
            throw new NullPointerException("Edge destintation may not be NULL.");
    }

    public GraphvizDiagramGenerator getGenerator()
    {
        return generator;
    }

    public Map getAttributes()
    {
        return attributes;
    }

    public String getSource()
    {
        return source;
    }

    public String getDestintation()
    {
        return destintation;
    }

    public String getLabel()
    {
        return (String) attributes.get(GraphvizDiagramGenerator.ATTRNAME_LABEL);
    }

    public void setLabel(String label)
    {
        attributes.put(GraphvizDiagramGenerator.ATTRNAME_LABEL, label);
    }

    public String getArrowHead()
    {
        return (String) attributes.get(ATTRNAME_ARROWHEAD);
    }

    public void setArrowHead(String arrowHead)
    {
        attributes.put(ATTRNAME_ARROWHEAD, arrowHead);
    }

    public String getArrowSize()
    {
        return (String) attributes.get(ATTRNAME_ARROWSIZE);
    }

    public void setArrowSize(String arrowSize)
    {
        attributes.put(ATTRNAME_ARROWSIZE, arrowSize);
    }

    public String getStyle()
    {
        return (String) attributes.get(ATTRNAME_STYLE);
    }

    public void setStyle(String style)
    {
        attributes.put(ATTRNAME_STYLE, style);
    }
}
