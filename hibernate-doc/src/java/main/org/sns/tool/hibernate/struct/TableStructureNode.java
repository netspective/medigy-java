package org.sns.tool.hibernate.struct;

import java.util.List;

import org.hibernate.mapping.Table;
import org.hibernate.mapping.PersistentClass;

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
     * Get all the children for this node -- each item in the list is a TableTreeNode
     */
    public List getChildNodes();

    /**
     * Get all the ancestors for this node -- each item in the list is a TableTreeNode
     */
    public List getAncestorNodes();

    /**
     * Returns true if this node has any children.
     */
    public boolean hasChildren();
}
