package org.sns.tool.hibernate.struct;

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
    public TableStructureNode[] getChildNodes();

    /**
     * Get the list of all the columns in this table.
     */
    public ColumnDetail[] getAllColumns();

    /**
     * Get the list of only those columns that belong in a particular category.
     */
    public ColumnDetail[] getColumnsInCategory(final ColumnCategory columnCategory);

    /**
     * Retrieve the column category named id.
     * @return Null if no table category named "id" exists, ColumnCategory instance otherwise.
     */
    public ColumnCategory getColumnCategory(final String id);

    /**
     * Get all the ancestors for this node -- each item in the set is a TableStructureNode
     */
    public TableStructureNode[] getAncestorNodes();

    /**
     * Get all children and descendents into a single list.
     */
    public TableStructureNode[] getDescendents();

    /**
     * Returns true if this node has any children.
     */
    public boolean hasChildren();

    /**
     * Ascertain whether or not this node is a link to another node or a physical one.
     * @return True if this node is a reference to another node.
     */
    public boolean isLinkedNode();

    /**
     * If this node is not a physical node but a link to another node, then provide the link.
     * @return null if this is not a link, TableStructureNode instance if it's a reference to another node
     */
    public TableStructureNode getLinkedNode();

    /**
     * Ascertain whether the given table node belongs in a special category (like "reference" or "application").
     * @return Always a non-null and non-zero length array of categories to which the table belongs
     */
    public TableCategory[] getTableCategories();

    /**
     * See whether this table belongs to the given category.
     */
    public boolean isInCategory(final TableCategory category);

    /**
     * See whether this table belongs to the given category.
     */
    public boolean isInCategory(final String categoryId);

    /**
     * Obtain the list of table nodes that reference this instance.
     * @return The list of nodes that have foreign keys that refer to this table.
     */
    public TableStructureNode[] getDependencies();
}
