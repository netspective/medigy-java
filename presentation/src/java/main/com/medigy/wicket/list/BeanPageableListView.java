/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.wicket.list;

import wicket.markup.ComponentTag;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.ListView;
import wicket.markup.html.list.PageableListView;
import wicket.markup.html.basic.Label;
import wicket.model.IModel;

import java.util.List;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;


/**
 *
 */
public class BeanPageableListView extends PageableListView
{

    public BeanPageableListView(final String id, final IModel model, final int rowsPerPage)
    {
        super(id, model, rowsPerPage);
    }

    public boolean isVersioned()
    {
        return true;
    }

    /**
     * Subclass Table's newCell() and return a ListItem which will add/modify its
     * class attribute and thus provide ListItems with alternating row colours.
     *
     * See wicket.markup.html.table.Table#newItem(int)
     * @param index Index of item
     * @return List item
     */
    protected ListItem newItem(final int index)
    {
        return new ListItem(index, getListItemModel(getModel(), index))
            {
                protected void onComponentTag(final ComponentTag tag)
                {
                    // add/modify the attribute controlling the CSS style
                    tag.put("class", (getIndex() % 2) == 0 ? "even" : "odd");

                    // continue with default behaviour
                    super.onComponentTag(tag);
                }
            };
    }

    public void populateItem(final ListItem listItem)
    {
        Object obj = listItem.getModelObject();

        try
        {
            final BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            final PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < descriptors.length; i++)
            {
                final PropertyDescriptor descriptor = descriptors[i];
                if (descriptor.getReadMethod() != null)
                {
                    // TODO: Use annotations to see if links or plain labels should be used
                    Object propertyValue = descriptor.getReadMethod().invoke(obj);
                    if (propertyValue != null)
                        listItem.add(new Label(descriptor.getDisplayName(), propertyValue.toString()));
                    else
                        listItem.add(new Label(descriptor.getDisplayName()));
                }
            }
        }
        catch (IntrospectionException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }


    }
}
