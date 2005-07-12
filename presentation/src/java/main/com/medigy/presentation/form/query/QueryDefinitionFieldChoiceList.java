/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.presentation.form.query;

import wicket.markup.html.form.model.IChoiceList;
import wicket.markup.html.form.model.IChoice;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

import com.medigy.persist.util.query.QueryDefinitionField;

public class QueryDefinitionFieldChoiceList implements IChoiceList
{
    private List<IChoice> choices = new ArrayList<IChoice>();

    private class FieldChoice implements IChoice
    {
        private QueryDefinitionField field;

        public FieldChoice(final QueryDefinitionField field)
        {
            this.field = field;
        }

        public String getDisplayValue()
        {
            return field.getCaption();
        }

        public String getId()
        {
            return field.getColumnName();
        }

        public Object getObject()
        {
            return field.getName();
        }
    }

    public QueryDefinitionFieldChoiceList(final Collection<QueryDefinitionField> fieldList)
    {
        for (QueryDefinitionField field : fieldList)
            choices.add(new FieldChoice(field));
    }

    public void attach()
    {
    }

    public IChoice choiceForId(final String id)
    {
        for (IChoice choice: choices)
        {
            if (choice.getId().equals(id))
                return choice;
        }
        return null;
    }

    public IChoice choiceForObject(Object object)
    {
        for (IChoice choice: choices)
        {
            if (choice.getObject().equals(object))
                return choice;
        }
        return null;
    }

    public IChoice get(int index)
    {
        return choices.get(index);
    }

    public int size()
    {
        return this.choices.size();
    }

    public void detach()
    {
    }
}