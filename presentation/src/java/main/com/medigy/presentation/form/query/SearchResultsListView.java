/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.presentation.form.query;

import wicket.markup.html.list.PageableListView;
import wicket.markup.html.list.ListItem;
import wicket.markup.MarkupStream;
import wicket.markup.ComponentTag;
import wicket.model.IModel;

import java.util.Map;
import java.util.Iterator;
import java.util.Collection;
import java.util.List;
import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

public class SearchResultsListView  extends PageableListView
{
    private List<String> columnNames;
    /**
     * Construct.
     *
     * @param id
     *            id of the component
     * @param model
     *            the model
     * @param pageSizeInCells
     *            page size
     */
    public SearchResultsListView(String id, IModel model, int pageSizeInCells)
    {
        super(id, model, pageSizeInCells);
    }

    /**
     * @see wicket.Component#isVersioned()
     */
    public boolean isVersioned()
    {
        return true;
    }

    public void setColumnNames(final List<String> names)
    {
        this.columnNames = names;
    }

    public DateFormat getDateFormat()
    {
        // TODO : retrieve date format from a centralized place common across the application
        return new SimpleDateFormat("MM/dd/yyyy");
    }

    protected ListItem newItem(final int index)
    {
        return new ListItem(index, getListItemModel(getModel(), index))
        {

            protected void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag)
            {
                StringBuffer buffer = new StringBuffer();
                final Map<String, Object> map = (Map<String, Object>) getModelObject();

                if (columnNames == null)
                    columnNames =  new ArrayList<String>(map.keySet());
                for (String column : columnNames)
                {
                    final Object value = map.get(column);
                    if (value != null && value instanceof Date)
                         buffer.append("<td>" + getDateFormat().format((Date)value) +"</td>");
                    else if (value != null)
                        buffer.append("<td>" + value +"</td>");
                    else
                        buffer.append("<td>&nbsp;</td>");
                }

                replaceComponentTagBody(markupStream, openTag, buffer.toString());
                super.onComponentTagBody(markupStream, openTag);
            }
        };
    }

    /**
     * @see PageableListView#populateItem(ListItem)
     * @param item
     */
    public void populateItem(final ListItem item)
    {
        final Map<String, Object> map = (Map<String, Object>) item.getModelObject();
        final Iterator<String> strings = map.keySet().iterator();

        /*
        final CD cd = (CD) item.getModelObject();
        final Long id = cd.getId();

        // add links to the details
        item.add(new DetailLink("title", id).add(new Label("title", cd.getTitle())));
        item.add(new DetailLink("performers", id).add(new Label("performers", cd
                .getPerformers())));
        item.add(new DetailLink("label", id).add(new Label("label", cd.getLabel())));
        item.add(new DetailLink("year", id).add(new Label("year", (cd.getYear() != null) ? cd
                .getYear().toString() : "")));

        // add a delete link for each found record
        DeleteLink deleteLink = new DeleteLink("delete", cd);
        item.add(deleteLink);
        */
    }
}
