package org.sns.tool.hibernate.struct;

import java.util.List;
import java.util.Set;

import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Table;

public interface TableStructureNode
{
    /**
     * Return the owner of this node;
     */
    public TableStructure getOwner();

    /**
     * Return the hierarchical level (0 is top level) of this node.
     */
    public int getLevel();

    /**
     * Return the table for which structural information was gathered
     */
    public Table getTable();

    /**
     * Return the class that represents this tables.
     */
    public PersistentClass getPersistentClass();

    /**
     * Return the parent node for this node
     */
    public TableStructureNode getParentNode();

    /**
     * Get all the children for this node -- each item in the set is a TableStructureNode
     */
    public Set getChildNodes();

    /**
     * Get all the ancestors for this node -- each item in the set is a TableStructureNode
     */
    public List getAncestorNodes();

    /**
     * Returns true if this node has any children.
     */
    public boolean hasChildren();
}
