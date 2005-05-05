package com.medigy.tool.persist.hibernate;

/**
 * This filter is useful for generating DOT and GraphViz output when the intended
 * audience is a group of software engineers that need only class relationships and how
 * the tables are structured (a mix of ERD and UML) but without column names. 
 */
public class HibernateDiagramEngineerRelationshipsOnlyFilter extends HibernateDiagramFilter
{
    public HibernateDiagramEngineerRelationshipsOnlyFilter()
    {
        super(false, true, true, true);
    }
}
