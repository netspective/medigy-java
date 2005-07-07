package com.medigy.wicket.form;

public enum FormMode
{
    INSERT("add", "Add perspective"),
    UPDATE("edit", "Edit perspective"),
    DELETE("delete", "Delete perspective"),
    CONFIRM("confirm", "Confirm perspective"),
    PRINT("print", "Print perspective");

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
