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

public class SearchResultsListView  extends PageableListView
{
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

    protected ListItem newItem(final int index)
    {
        return new ListItem(index, getListItemModel(getModel(), index))
        {

            protected void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag)
            {
                StringBuffer buffer = new StringBuffer();
                final Map<String, Object> map = (Map<String, Object>) getModelObject();
                final Iterator<String> strings = map.keySet().iterator();
                while (strings.hasNext())
                {
                    final String label = strings.next();
                    final Object value = map.get(label);
                    if (value instanceof Collection)
                    {
                        // TODO: support this???
                    }
                    else
                    {
                        buffer.append("<td>" + value +"</td>");
                    }
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
