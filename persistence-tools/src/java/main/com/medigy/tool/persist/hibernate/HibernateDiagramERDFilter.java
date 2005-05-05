package com.medigy.tool.persist.hibernate;

/**
 * This filter is useful for generating DOT and GraphViz output when the intended
 * audience is a group of DBAs looking for a pure ERD.
 */
public class HibernateDiagramERDFilter extends HibernateDiagramFilter
{
    public HibernateDiagramERDFilter()
    {
        super(true, false, false, false);
    }
}
