/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.presentation.form.query;

import wicket.markup.html.form.IChoiceRenderer;
import wicket.markup.html.form.ListMultipleChoice;

import java.util.List;
import java.util.ArrayList;

import com.medigy.persist.util.query.QueryDefinitionField;

public class QueryDefinitionFieldChoiceList extends ListMultipleChoice
{
    private class FieldChoice implements IChoiceRenderer
    {
        private List<QueryDefinitionField> conditionList = new ArrayList<QueryDefinitionField>();

        public FieldChoice(final List<QueryDefinitionField> conditionList)
        {
            this.conditionList = conditionList;
        }

        public String getDisplayValue(Object object)
        {
            for(QueryDefinitionField choice: conditionList)
            {
                if (choice.equals(object))
                    return choice.getCaption();
            }
            return null;
        }

        public String getIdValue(Object object, int index)
        {
            for(QueryDefinitionField choice: conditionList)
            {
                if(object instanceof QueryDefinitionField)
                {
                    QueryDefinitionField param = (QueryDefinitionField)object;
                    if (choice.getCaption().equals(param.getCaption()))
                        return choice.getName();
                }
                else if(object instanceof String)
                {
                    if (choice.getName().equals(object))
                        return choice.getName();
                }
            }
            return null;
        }
    }

    public QueryDefinitionFieldChoiceList(final String name, final List<QueryDefinitionField> fieldList)
    {
        super(name);

        this.setChoices(fieldList);
        this.setChoiceRenderer(new FieldChoice(fieldList));
    }
}