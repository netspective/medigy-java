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
import wicket.IFeedback;
import wicket.markup.ComponentTag;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.DropDownChoice;
import wicket.markup.html.form.ListMultipleChoice;
import wicket.markup.html.form.TextField;
import wicket.markup.html.form.model.IChoiceList;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.ListView;
import wicket.model.CompoundPropertyModel;
import wicket.model.IModel;

import java.util.Collection;
import java.util.List;
import java.util.Map;

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
     * @return
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

        public QueryDefinitionSearchForm(final String id, IModel model, final IFeedback feedback)
        {
            super(id, model, feedback);
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
            add(new ConditionFieldListView("conditionFieldList", searchFormModelObject.getConditionFieldList(),
                    defaultValues));
            displayFieldsSelectList = new ListMultipleChoice("displayFields", new QueryDefinitionFieldChoiceList(defaultValues.getDisplayFields().values()));
            add(displayFieldsSelectList);
            add(new ListMultipleChoice("sortByFields", new QueryDefinitionFieldChoiceList(defaultValues.getSortByFields().values())));
        }

        public final void onSubmit()
        {
            final QueryDefSearchFormModelObject formModelObject = (QueryDefSearchFormModelObject) getModelObject();
            final List<String> displayFields = formModelObject.getDisplayFields();
            System.out.println(" =============== " + displayFields.size());
            if (displayFields.size() == 0)
            {
                final IChoiceList choices = displayFieldsSelectList.getChoices();
                for (int i = 0; i < choices.size(); i++)
                {
                    displayFields.add((String) choices.get(i).getObject());
                }
                formModelObject.setDisplayFields(displayFields);
            }
            ((SearchResultPanel) getPage().get("border.searchBorder.searchResultsPanel")).onSearchExecute(formModelObject);
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
            item.add(new DropDownChoice("field", new QueryDefinitionFieldChoiceList(defaultValues.getConditionFields().values())) {
                protected void onComponentTag(final ComponentTag tag)
                {
                    // TODO:  Populate the field Comparison list with the field specific comparisons
                    super.onComponentTag(tag);
                    //tag.put("onChange", "this.form.fieldComparison.");
                }
            });
            item.add(new DropDownChoice("fieldComparison", SqlComparisonFactory.getInstance().listAllNames()));
            item.add(new TextField("fieldValue"));
            item.add(new DropDownChoice("connector", defaultValues.getConnectors()));
        }
    }


}

