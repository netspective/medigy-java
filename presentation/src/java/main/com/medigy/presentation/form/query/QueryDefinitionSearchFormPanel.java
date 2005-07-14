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

import java.util.List;
import java.util.Collection;
import java.util.Map;
import java.util.ArrayList;
import java.io.Serializable;

import wicket.markup.html.form.Form;
import wicket.markup.html.form.ListMultipleChoice;
import wicket.markup.html.form.DropDownChoice;
import wicket.markup.html.form.TextField;
import wicket.markup.html.list.ListView;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.PageableListViewNavigation;
import wicket.markup.html.list.PageableListView;
import wicket.markup.html.list.PageableListViewNavigationLink;
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
    private QueryDefinitionSearchResultsListView resultsListView;
    private SearchResultsHeaderView resultsHeaderView;
    private QueryDefinitionSearchModel searchModel;
    private QueryDefinitionSearchService service;
    private PageableListViewNavigator navPanel;
    private Class queryDefinitionClass;

    public QueryDefinitionSearchFormPanel(final String componentName, final FormMode formMode, final Class queryDefinitionClass)
    {
        super(componentName);
        this.queryDefinitionClass = queryDefinitionClass;
        service = (QueryDefinitionSearchService) ((DefaultApplication) getApplication()).getService(QueryDefinitionSearchService.class);
        final StandardPanelBorder border = new StandardPanelBorder("panel_border");
        add(border);

        // Create feedback panel and add to page
        final FeedbackPanel feedback = new FeedbackPanel("feedback");
        border.add(feedback);
        border.add(createForm("form", feedback, formMode));

        final int rowsPerPage = 10;

        // The service was looked up inside createForm which gets called in the super constructor.
        searchModel = new QueryDefinitionSearchModel(service, queryDefinitionClass);
        resultsListView = createSearchResultsListView(searchModel, rowsPerPage);
        add(resultsListView);

        resultsHeaderView = new SearchResultsHeaderView("resultsHeader");
        // results yet
        add(resultsHeaderView);
        //add(new SearchResultTableNavigation("navigation", resultsListView));
        navPanel = new PageableListViewNavigator("navigation-panel", resultsListView);
        navPanel.setVisible(false);
        add(navPanel);
    }

    /**
     * Override this method if you have your own custom list view to display the search results
     * @param searchModel
     * @param rowsPerPage
     * @return
     */
    protected QueryDefinitionSearchResultsListView createSearchResultsListView(final QueryDefinitionSearchModel searchModel, final int rowsPerPage)
    {
        return new QueryDefinitionSearchResultsListView("results", searchModel, rowsPerPage);
    }

    public void setCurrentResultPageToFirst()
    {
        resultsListView.setCurrentPage(0);
    }

    private int getNumberOfResults()
    {
        return ((List)resultsListView.getModelObject()).size();
    }

    protected Form createForm(final String componentName, final IFeedback feedback, final FormMode formMode)
    {
        final QueryDefinitionSearchFormPopulateValues defaultValues = service.getAvailableSearchParameters(queryDefinitionClass);
        final QueryDefinitionSearchModelObject modelObject = new QueryDefinitionSearchModelObject();

        final Collection<QueryDefinitionField> displayFields = defaultValues.getDisplayFields().values();
        for (QueryDefinitionField field : displayFields)
        {
            modelObject.addConditionFieldList(new QueryDefinitionSearchCondition() {

            });
        }

        return new QueryDefinitionSearchForm(componentName, new CompoundPropertyModel(modelObject),
                feedback, defaultValues);
    }


    protected class QueryDefinitionSearchForm extends Form
    {
        private QueryDefinitionSearchFormPopulateValues defaultValues;

        public QueryDefinitionSearchForm(final String id, IModel model, final IFeedback feedback, final QueryDefinitionSearchFormPopulateValues values)
        {
            super(id, model, feedback);
            this.defaultValues = values;

            add(new ConditionFieldListView("conditionFieldList", ((QueryDefinitionSearchModelObject) getModelObject()).getConditionFieldList(),
                    defaultValues));
            add(new ListMultipleChoice("displayFields", new QueryDefinitionFieldChoiceList(defaultValues.getDisplayFields().values())));
            add(new ListMultipleChoice("sortByFields", new QueryDefinitionFieldChoiceList(defaultValues.getSortByFields().values())));
        }

        public final void onSubmit()
        {
            this.getParent().setVisible(false);
            final QueryDefinitionSearchModelObject modelObject = (QueryDefinitionSearchModelObject) getModelObject();
            if (modelObject.getDisplayFields().size() == 0)
            {
                modelObject.setDisplayFields(new ArrayList<String>(defaultValues.getDisplayFields().keySet()));
            }
            navPanel.setVisible(true);
            searchModel.setSearchParameters(modelObject);
            resultsHeaderView.setModel(new Model((Serializable)modelObject.getDisplayFields()));
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

    public class QueryDefinitionSearchModelObject
    {

        private List<QueryDefinitionSearchCondition> conditionFieldList = new ArrayList<QueryDefinitionSearchCondition>();

        private List<String> displayFields = new ArrayList<String>();
        private List<String> sortByFields = new ArrayList<String>();

        public List<QueryDefinitionSearchCondition> getConditionFieldList()
        {
            return conditionFieldList;
        }

        public void setConditionFieldList(final List<QueryDefinitionSearchCondition> conditionFieldList)
        {
            this.conditionFieldList = conditionFieldList;
        }

        public void addConditionFieldList(final QueryDefinitionSearchCondition condition)
        {
            this.conditionFieldList.add(condition);
        }

        public List<String> getDisplayFields()
        {
            return displayFields;
        }

        public void setDisplayFields(final List<String> displayFields)
        {
            this.displayFields = displayFields;
        }

        public List<String> getSortByFields()
        {
            return sortByFields;
        }

        public void setSortByFields(final List<String> sortByFields)
        {
            this.sortByFields = sortByFields;
        }
    }

    private static class SearchResultTableNavigation extends PageableListViewNavigation
    {
        /**
         * Construct.
         *
         * @param id
         *            the name of the component
         * @param table
         *            the table
         */
        public SearchResultTableNavigation(String id, PageableListView table)
        {
            super(id, table);
        }

        /**
         * @see wicket.markup.html.list.Loop#populateItem(wicket.markup.html.list.Loop.LoopItem)
         */
        protected void populateItem(final LoopItem iteration)
        {
            final PageableListViewNavigationLink link = new PageableListViewNavigationLink(
                    "pageLink", pageableListView, iteration.getIteration());

            if (iteration.getIteration() > 0)
            {
                iteration.add(new Label("separator", "|"));
            }
            else
            {
                iteration.add(new Label("separator", ""));
            }
            link.add(new Label("pageNumber", String.valueOf(iteration.getIteration() + 1)));
            link.add(new Label("pageLabel", "page"));
            iteration.add(link);
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

