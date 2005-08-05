/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.presentation.form.query;

import com.medigy.presentation.model.common.ServiceSearchResultModel;
import wicket.markup.ComponentTag;
import wicket.markup.MarkupStream;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.PageableListView;
import wicket.model.IModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

    public SearchResultsListView(String id, List list, int pageSizeInCells)
    {
        super(id, list, pageSizeInCells);
    }

    /**
     * @see wicket.Component#isVersioned()
     */
    public boolean isVersioned()
    {
        return true;
    }

    public DateFormat getDateFormat()
    {
        // TODO : retrieve date format from a centralized place common across the application
        return new SimpleDateFormat("MM/dd/yyyy");
    }

    protected ListItem newItem(final int index)
    {
        columnNames = ((ServiceSearchResultModel) getModel()).getResultColumnNames();
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
    }
}
