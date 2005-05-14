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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * This module provides an interface to layout and image generation of directed and undirected graphs in a variety of
 * formats (PostScript, PNG, etc.) using the "dot", "neato" and "twopi" programs from the GraphViz project
 * (http://www.graphviz.org/ or http://www.research.att.com/sw/tools/graphviz/).
 */
public class GraphvizDiagramGenerator
{
    public static final String ATTRNAME_LABEL = "label";
    public static final String ATTRNAME_FONTNAME = "fontname";

    /**
     * The graph identifier
     */
    private String graphId;

    /**
     * Maintains the layout algorithm GraphViz will use; possible alues are 'dot' (the default GraphViz layout for
     * directed graph layouts), 'neato' (for undirected graph layouts - spring model) or 'twopi' (for undirected graph
     * layouts - circular)
     */
    private String layout = GraphvizLayoutType.DOT;

    /**
     * Determines whether we're generating directed (edges have arrows) graphs. Setting this to false produces
     * undirected graphs (edges do not have arrows).
     */
    private boolean directed = true;

    /**
     * Controls the direction the nodes are linked together. If true it will do left->right linking rather than the
     * default up-down linking.
     */
    private boolean rankLeftToRight = false;

    /**
     * Controls the size of the bounding box of the drawing in inches. This is more useful for PostScript output as
     * for raster graphic (such as PNG) the pixel dimensions can not be set, although there are generally 96 pixels per
     * inch.
     */
    private double drawingWidthInInches;
    private double drawingHeightInInches;

    /**
     * Control the PostScript pagination size in inches. That is, if the image is larger than the page then the
     * resulting PostScript image is a sequence of pages that can be tiled or assembled into a mosaic of the full image.
     * (This only works for PostScript output).
     */
    private double pageWidthInInches;
    private double pageHeightInInches;

    /**
     * Enables an edge merging technique to reduce clutter in dense layouts of directed graphs. The default is
     * not to merge edges.
     */
    private boolean concentrate = false;

    /**
     * For undirected graphs, the randomStart option requests an initial random placement for the graph, which may
     * give a better result. The default is not random.
     */
    private boolean randomStart = false;

    /**
     * For undirected graphs, the 'epsilon' attribute decides how long the graph solver tries before finding a graph
     * layout. Lower numbers allow the solver to fun longer and potentially give a better layout. Larger values can
     * decrease the running time but with a reduction in layout quality. The default is 0.1.
     */
    private double epsilon = 0.1;

    /**
     * Tells the graph solver to not overlap the nodes
     */
    private boolean preventOverlap = false;

    /**
     * Sets the background color. A color value may be "h,s,v" (hue, saturation, brightness) floating point numbers
     * between 0 and 1, or an X11 color name such as 'white', 'black', 'red', 'green', 'blue', 'yellow', 'magenta',
     * 'cyan', or 'burlywood'.
     */
    private String backgroundColor = "white";

    private Map defaultGraphAttributes = new HashMap();
    private Map defaultNodeAttributes = new HashMap();
    private Map defaultEdgeAttributes = new HashMap();

    private Map nodes = new HashMap();
    private List nodeList = new ArrayList();
    private List edgeList = new ArrayList();

    public GraphvizDiagramGenerator(final String graphId, final boolean directed, final String layout)
    {
        this.graphId = graphId;
        this.directed = directed;
        this.layout = layout;

        if(!(layout.equals(GraphvizLayoutType.DOT) ||
             layout.equals(GraphvizLayoutType.NEATO) || layout.equals(GraphvizLayoutType.TWOPI)))
            throw new RuntimeException("Invalid layout type '" + layout + "'.");
    }

    public void addNode(final GraphvizDiagramNode node)
    {
        nodeList.add(node);
        nodes.put(node.getIdentifier(), node);
    }

    public void addEdge(final GraphvizDiagramEdge edge)
    {
        edgeList.add(edge);
    }

    public String generateAttributes(final Map attributes) throws IOException
    {
        if(attributes.size() == 0)
            return new String();

        final StringBuffer result = new StringBuffer("[");
        boolean first = true;
        final Set sortedAttrNames = new TreeSet(attributes.keySet());
        for(Iterator i = sortedAttrNames.iterator(); i.hasNext();)
        {
            String attrName = (String) i.next();
            String attrValue = (String) attributes.get(attrName);

            if(!first)
                result.append(" ");
            else
                first = false;

            result.append(attrName);
            result.append("=");

            // labels can have HTML which has label=<XXX> instead of label="XXX"
            if(attrName.equalsIgnoreCase(ATTRNAME_LABEL) && attrValue.startsWith("<") && attrValue.endsWith(">"))
                result.append(attrValue);
            else
                result.append("\"" + attrValue + "\"");
        }

        result.append("]");
        return result.toString();
    }

    public void generateDOTSource(Writer writer) throws IOException
    {
        writer.write(isDirected() ? "digraph" : "graph");
        writer.write(" " + getGraphId());
        writer.write("\n{\n");

        for(int i = 0; i < nodeList.size(); i++)
        {
            GraphvizDiagramNode node = (GraphvizDiagramNode) nodeList.get(i);
            writer.write("    " + node.getIdentifier());
            writer.write(generateAttributes(node.getAttributes()));
            writer.write(";\n");
        }

        writer.write("\n");

        for(int i = 0; i < edgeList.size(); i++)
        {
            GraphvizDiagramEdge edge = (GraphvizDiagramEdge) edgeList.get(i);
            writer.write("    " + edge.getSource() + " -> " + edge.getDestintation());
            writer.write(generateAttributes(edge.getAttributes()));
            writer.write(";\n");
        }

        writer.write("\n}");
    }

    public void generateDOTSource(final File dest) throws IOException
    {
        FileWriter writer = new FileWriter(dest);
        generateDOTSource(writer);
        writer.close();
    }

    public interface ImageGenerationParams
    {
        /**
         * Ascertain the directory in which the image generation will occur.
         */
        public File getDestDir();

        /**
         * Ascertain the base name of the files generated. The DOT source will be destDir/basename.dot. The images
         * will be called destDir/basename[extn].
         */
        public String getBaseFileName();

        /**
         * Obtain the spec that will be used to run the graphViz dot command.
         */
        public String getGraphVizDotCommandSpec();

        /**
         * Provide the output types understood by graphviz dot command (gif, jpg, png, svg, etc).
         */
        public String[] getImageTypes();

        /**
         * Provide the extensions associated with the output image types provided by getImageTypes. If this is null,
         * then getImagesTypes() will be used to provide the extensions.
         */
        public String[] getImageExtensions();
    }

    /**
     * Generate images based on the input parameters.
     * @param params The parameters.
     * @return A list of files with the first item in the list as the DOT source file name and all others are the generated
     * graphics files.
     * @throws IOException
     */
    public File[] generateImages(final ImageGenerationParams params) throws IOException, InterruptedException
    {
        final List results = new ArrayList();
        final File src = new File(params.getDestDir(), params.getBaseFileName() + ".dot.txt");
        generateDOTSource(src);
        results.add(src);

        for(int i = 0; i < params.getImageTypes().length; i++)
        {
            final String imageType = params.getImageTypes()[i];
            final String fileExtn = params.getImageTypes() == null ? ("." + imageType) :params.getImageExtensions()[i];
            final File dest = new File(params.getDestDir(), params.getBaseFileName() + fileExtn);

            final String cmdLine =  params.getGraphVizDotCommandSpec() + " -T"+ imageType +" -o\""+ dest +"\" \""+ src +"\"";
            GraphvizDotExec.exec(cmdLine, System.out, System.err);
            results.add(dest);
        }

        return (File[]) results.toArray(new File[results.size()]);
    }

    /*-- Accessors and Mutators for access to private fields --------------------------------------------------------*/

    public String getGraphId()
    {
        return graphId;
    }

    public boolean isDirected()
    {
        return directed;
    }

    public String getLayout()
    {
        return layout;
    }

    public boolean isRankLeftToRight()
    {
        return rankLeftToRight;
    }

    public void setRankLeftToRight(boolean rankLeftToRight)
    {
        this.rankLeftToRight = rankLeftToRight;
    }

    public boolean isRankTopToBottom()
    {
        return !rankLeftToRight;
    }

    public void setRankTopToBottom(boolean rankTopToBottom)
    {
        this.rankLeftToRight = !rankTopToBottom;
    }

    public double getDrawingHeightInInches()
    {
        return drawingHeightInInches;
    }

    public void setDrawingHeightInInches(double drawingHeightInInches)
    {
        this.drawingHeightInInches = drawingHeightInInches;
    }

    public double getDrawingWidthInInches()
    {
        return drawingWidthInInches;
    }

    public void setDrawingWidthInInches(double drawingWidthInInches)
    {
        this.drawingWidthInInches = drawingWidthInInches;
    }

    public double getPageHeightInInches()
    {
        return pageHeightInInches;
    }

    public void setPageHeightInInches(double pageHeightInInches)
    {
        this.pageHeightInInches = pageHeightInInches;
    }

    public double getPageWidthInInches()
    {
        return pageWidthInInches;
    }

    public void setPageWidthInInches(double pageWidthInInches)
    {
        this.pageWidthInInches = pageWidthInInches;
    }

    public boolean isConcentrate()
    {
        return concentrate;
    }

    public void setConcentrate(boolean concentrate)
    {
        this.concentrate = concentrate;
    }

    public double getEpsilon()
    {
        return epsilon;
    }

    public void setEpsilon(double epsilon)
    {
        this.epsilon = epsilon;
    }

    public boolean isRandomStart()
    {
        return randomStart;
    }

    public void setRandomStart(boolean randomStart)
    {
        this.randomStart = randomStart;
    }

    public String getBackgroundColor()
    {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor)
    {
        this.backgroundColor = backgroundColor;
    }

    public boolean isPreventOverlap()
    {
        return preventOverlap;
    }

    public void setPreventOverlap(boolean preventOverlap)
    {
        this.preventOverlap = preventOverlap;
    }

    public Map getDefaultEdgeAttributes()
    {
        return defaultEdgeAttributes;
    }

    public Map getDefaultGraphAttributes()
    {
        return defaultGraphAttributes;
    }

    public Map getDefaultNodeAttributes()
    {
        return defaultNodeAttributes;
    }
}
