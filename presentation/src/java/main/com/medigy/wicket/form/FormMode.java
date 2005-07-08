package com.medigy.wicket.form;

public enum FormMode
{
    NONE("none", "No specific mode"),
    INSERT("insert", "Add mode"),
    UPDATE("update", "Edit mode"),
    DELETE("delete", "Delete mode"),
    CONFIRM("confirm", "Confirm mode"),
    PRINT("print", "Print mode");

    private final String label;
    private final String description;

    private FormMode(final String label, final String description)
    {
        this.label = label;
        this.description = description;
    }

    public String getLabel()
    {
        return label;
    }

    public String getDescription()
    {
        return description;
    }
}
