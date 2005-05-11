package com.medigy.tool.persist.hibernate.diagram;

/**
 * This filter is useful for generating DOT and GraphViz output when the intended
 * audience is a group of software engineers that need class relationships and how
 * the tables are structured (a mix of ERD and UML) including column names.
 */
public class HibernateDiagramEngineerFilter extends HibernateDiagramFilter
{
    public HibernateDiagramEngineerFilter()
    {
        super(true, true, true, true);
    }
}
