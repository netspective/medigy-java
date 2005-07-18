/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.presentation.form.query;

import com.medigy.service.query.QueryDefinitionSearchService;
import com.medigy.persist.util.query.QueryDefinitionField;
import com.medigy.persist.util.query.SqlComparisonFactory;
import com.medigy.service.dto.query.QueryDefinitionSearchFormPopulateValues;
import com.medigy.service.dto.query.QueryDefinitionSearchCondition;
import com.medigy.wicket.form.FormMode;
import com.medigy.wicket.DefaultApplication;
import com.medigy.wicket.border.StandardPanelBorder;
import com.medigy.wicket.panel.DefaultPanel;
import com.medigy.presentation.model.common.ServiceSearchResultModel;
import com.medigy.presentation.model.common.ServiceCountAndListAction;
import com.medigy.presentation.model.query.QueryDefSearchResultModel;
import com.medigy.presentation.model.query.QueryDefServiceCountAndListAction;
import com.medigy.presentation.form.common.SearchForm;

import java.util.List;
import java.util.Collection;
import java.util.Map;
import java.util.ArrayList;
import java.io.Serializable;

import wicket.markup.html.form.ListMultipleChoice;
import wicket.markup.html.form.DropDownChoice;
import wicket.markup.html.form.TextField;
import wicket.markup.html.list.ListView;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.PageableListViewNavigator;
import wicket.markup.html.basic.Label;
import wicket.markup.html.panel.FeedbackPanel;
import wicket.markup.ComponentTag;
import wicket.IFeedback;
import wicket.model.CompoundPropertyModel;
import wicket.model.IModel;
import wicket.model.Model;

public class QueryDefinitionSearchFormPanel extends DefaultPanel
{
    private SearchResultsListView resultsListView;
    private SearchResultsHeaderView resultsHeaderView;
    private ServiceSearchResultModel searchResultModel;
    private QueryDefinitionSearchService service;
    private PageableListViewNavigator navPanel;
    private Class queryDefinitionClass;

    public QueryDefinitionSearchFormPanel(final String componentName, final FormMode formMode, final Class queryDefinitionClass)
    {
        super(componentName);
        this.queryDefinitionClass = queryDefinitionClass;
        this.service = (QueryDefinitionSearchService) ((DefaultApplication) getApplication()).getService(QueryDefinitionSearchService.class);
        // The service was looked up inside createForm which gets called in the super constructor.
        this.searchResultModel = createSearchResultModel(service, createCountAndListAction());

        final StandardPanelBorder border = new StandardPanelBorder("panel_border");
        add(border);

        // Create feedback panel and add to page
        final FeedbackPanel feedback = new FeedbackPanel("feedback");
        border.add(feedback);
        final SearchForm form = createForm("form", feedback, formMode);
        border.add(form);

        final int rowsPerPage = 10;
        resultsListView = createSearchResultsListView(searchResultModel, rowsPerPage);
        add(resultsListView);

        resultsHeaderView = new SearchResultsHeaderView("resultsHeader");
        // results yet
        add(resultsHeaderView);
        //add(new SearchResultTableNavigation("navigation", resultsListView));
        navPanel = new PageableListViewNavigator("navigation-panel", resultsListView);
        navPanel.setVisible(false);
        add(navPanel);
    }

    public ServiceCountAndListAction createCountAndListAction()
    {
        return new QueryDefServiceCountAndListAction(service, queryDefinitionClass);
    }


    /**
     * Override this method if you have your own model
     * @param service
     * @param action
     * @return
     */
    protected ServiceSearchResultModel createSearchResultModel(final QueryDefinitionSearchService service,
                                                               final ServiceCountAndListAction action)
    {
        return new QueryDefSearchResultModel(service, action);
    }

    /**
     * Override this method if you have your own custom list view to display the search results
     * @param searchResultModel
     * @param rowsPerPage
     * @return
     */
    protected SearchResultsListView createSearchResultsListView(final ServiceSearchResultModel searchResultModel, final int rowsPerPage)
    {
        return new SearchResultsListView("results", searchResultModel, rowsPerPage);
    }

    public void setCurrentResultPageToFirst()
    {
        resultsListView.setCurrentPage(0);
    }

    private int getNumberOfResults()
    {
        return ((List)resultsListView.getModelObject()).size();
    }

    protected SearchForm createForm(final String componentName, final IFeedback feedback, final FormMode formMode)
    {
        final QueryDefinitionSearchFormPopulateValues defaultValues = service.getAvailableSearchParameters(queryDefinitionClass);
        final QueryDefSearchFormModelObject formModelObject = new QueryDefSearchFormModelObject();
        formModelObject.setDefaultValues(defaultValues);

        final Collection<QueryDefinitionField> displayFields = defaultValues.getDisplayFields().values();
        for (QueryDefinitionField field : displayFields)
        {
            formModelObject.addConditionFieldList(new QueryDefinitionSearchCondition() {

            });
        }
        return new QueryDefinitionSearchForm(componentName, new CompoundPropertyModel(formModelObject),
                feedback);
    }


    protected class QueryDefinitionSearchForm extends SearchForm
    {
        public QueryDefinitionSearchForm(final String id, IModel model, final IFeedback feedback)
        {
            super(id, model, feedback);

            final QueryDefSearchFormModelObject searchFormModelObject = (QueryDefSearchFormModelObject) getModelObject();
            final QueryDefinitionSearchFormPopulateValues defaultValues = searchFormModelObject.getDefaultValues();
            add(new ConditionFieldListView("conditionFieldList", searchFormModelObject.getConditionFieldList(),
                    defaultValues));
            add(new ListMultipleChoice("displayFields", new QueryDefinitionFieldChoiceList(defaultValues.getDisplayFields().values())));
            add(new ListMultipleChoice("sortByFields", new QueryDefinitionFieldChoiceList(defaultValues.getSortByFields().values())));
        }

        public final void onSubmit()
        {
            super.onSubmit();
            final QueryDefSearchFormModelObject formModelObject = (QueryDefSearchFormModelObject) getModelObject();
            searchResultModel.setSearchParameters(formModelObject);
            this.getParent().setVisible(false);
            final QueryDefinitionSearchFormPopulateValues defaultValues = formModelObject.getDefaultValues();
            if (formModelObject.getDisplayFields().size() == 0)
            {
                formModelObject.setDisplayFields(new ArrayList<String>(defaultValues.getDisplayFields().keySet()));
            }
            navPanel.setVisible(true);
            resultsHeaderView.setModel(new Model((Serializable)formModelObject.getDisplayFields()));
            setCurrentResultPageToFirst();
            info(getNumberOfResults() + " results found.");
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

            System.out.println("CONNECTORS: " + defaultValues.getConnectors());
            item.add(new DropDownChoice("connector", defaultValues.getConnectors()));
        }
    }

    private class SearchResultsHeaderView extends ListView
    {
        public SearchResultsHeaderView(final String id)
        {
            super(id);
        }

        protected void populateItem(final ListItem item)
        {
            item.add(new Label("resultColumnName", item.getModelObjectAsString()));
        }
    }


}

