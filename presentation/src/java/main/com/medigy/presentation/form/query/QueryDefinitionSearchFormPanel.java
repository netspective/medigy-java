/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.presentation.form.query;

import com.medigy.persist.util.query.QueryDefinition;
import com.medigy.persist.util.query.QueryDefinitionField;
import com.medigy.persist.util.query.SqlComparisonFactory;
import com.medigy.presentation.form.common.SearchCriteriaFormPanel;
import com.medigy.presentation.form.common.SearchForm;
import com.medigy.presentation.form.common.SearchResultPanel;
import com.medigy.service.dto.query.QueryDefinitionSearchFormPopulateValues;
import com.medigy.service.dto.query.SearchCondition;
import com.medigy.service.person.PatientSearchService;
import com.medigy.service.query.QueryDefinitionSearchService;
import com.medigy.wicket.form.FormMode;
import wicket.markup.ComponentTag;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.DropDownChoice;
import wicket.markup.html.form.ListMultipleChoice;
import wicket.markup.html.form.TextField;
import wicket.markup.html.form.IChoiceRenderer;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.ListView;
import wicket.model.CompoundPropertyModel;
import wicket.model.IModel;
import wicket.feedback.IFeedback;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

/**
 * Panel class for the query definition form
 */
public class QueryDefinitionSearchFormPanel extends SearchCriteriaFormPanel
{
    private Class queryDefinitionClass;

    public QueryDefinitionSearchFormPanel(final String componentName, final FormMode formMode,
                                          final Class<? extends QueryDefinition> queryDefinitionClass)
    {
        super(componentName, formMode, PatientSearchService.class);
        this.queryDefinitionClass = queryDefinitionClass;
        this.searchForm.initializeForm();
    }

    /**
     * Creates the query def form containing the conditons, order bys and display fields.
     * @param componentName
     * @param feedback
     * @param formMode
     * @return SearchForm
     */
    protected SearchForm createForm(final String componentName, final IFeedback feedback, final FormMode formMode)
    {
        return new QueryDefinitionSearchForm(componentName, feedback);
    }

    protected class QueryDefinitionSearchForm extends SearchForm
    {
        private QueryDefinitionSearchFormPopulateValues defaultValues;
        private ListMultipleChoice displayFieldsSelectList;

        public QueryDefinitionSearchForm(final String id, final IFeedback feedback)
        {
            super(id, feedback);
        }

        public void initializeForm()
        {
            final QueryDefinitionSearchService queryDefinitionSearchService = ((QueryDefinitionSearchService) service);
            defaultValues = queryDefinitionSearchService.getAvailableSearchParameters(queryDefinitionClass);

            final QueryDefSearchFormModelObject searchFormModelObject = new QueryDefSearchFormModelObject();
            searchFormModelObject.setDefaultValues(defaultValues);

            final Collection<QueryDefinitionField> displayFields = defaultValues.getDisplayFields().values();
            for (QueryDefinitionField field : displayFields)
            {
                searchFormModelObject.addConditionFieldList(new SearchCondition() {

                });
            }
            setModel(new CompoundPropertyModel(searchFormModelObject));
            add(new ConditionFieldListView("conditionFieldList", searchFormModelObject.getConditionFieldList(), defaultValues));

            displayFieldsSelectList = new QueryDefinitionFieldChoiceList("displayFields", new ArrayList((defaultValues.getDisplayFields().values())));
            add(displayFieldsSelectList);
            add(new QueryDefinitionFieldChoiceList("sortByFields", new ArrayList(defaultValues.getSortByFields().values())));
        }

        public final void onSubmit()
        {
            final QueryDefSearchFormModelObject formModelObject = (QueryDefSearchFormModelObject) getModelObject();
            final List<String> displayFields = formModelObject.getDisplayFields();
            if (displayFields.size() == 0)
            {
                final List choices = displayFieldsSelectList.getChoices();
                for (int i = 0; i < choices.size(); i++)
                {
                    displayFields.add(((QueryDefinitionField) choices.get(i)).getName());
                }
                formModelObject.setDisplayFields(displayFields);
            }
            ((SearchResultPanel) getPage().get("border:searchBorder:searchResultsPanel")).onSearchExecute(formModelObject);
        }
    }

    protected class ConditionFieldListView extends ListView
    {
        private QueryDefinitionSearchFormPopulateValues defaultValues;

        public ConditionFieldListView(final String id, final IModel model, final QueryDefinitionSearchFormPopulateValues values)
        {
            super(id, model);
            this.defaultValues = values;
        }

        public ConditionFieldListView(final String id, final List model, final QueryDefinitionSearchFormPopulateValues values)
        {
            super(id, model);
            this.defaultValues = values;
        }

        protected IModel getListItemModel(final IModel listViewModel, final int index)
        {
            return  new CompoundPropertyModel(super.getListItemModel(listViewModel, index));
        }

        protected void populateItem(final ListItem item)
        {
            // Right now no need to populate from the model
            final Map<String, QueryDefinitionField> conditionFieldsMap = defaultValues.getConditionFields();

            item.add(new Label("fieldLabel", "Field"));
            item.add(new ConditionFieldChoiceList("field", new ArrayList(defaultValues.getConditionFields().values())));
            item.add(new DropDownChoice("fieldComparison", SqlComparisonFactory.getInstance().listAllNames()));
            item.add(new TextField("fieldValue"));
            item.add(new DropDownChoice("connector", defaultValues.getConnectors()));
        }

        public class ConditionFieldChoiceList extends DropDownChoice
        {
            private class ConditionChoice implements IChoiceRenderer
            {
                private List<QueryDefinitionField> fieldList;

                public ConditionChoice(final List<QueryDefinitionField> conditionList)
                {
                    this.fieldList = conditionList;
                }

                public String getDisplayValue(Object object)
                {
                    for(QueryDefinitionField choice: fieldList)
                    {
                        if (choice.equals(object))
                            return choice.getCaption();
                    }
                    return null;
                }

                public String getIdValue(Object object, int index)
                {
                    return fieldList.get(index).getName();
                }
            }

            public ConditionFieldChoiceList(final String name, final List<QueryDefinitionField> conditions)
            {
                super(name);

                setChoices(conditions);
                this.setChoiceRenderer(new ConditionChoice(conditions));
            }
        }
    }

}
