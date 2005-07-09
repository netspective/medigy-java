/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.presentation.form.person.patient;

import com.medigy.service.dto.query.QueryDefinitionSearchCondition;
import com.medigy.service.person.PatientSearchQueryDefinition;
import com.medigy.service.query.QueryDefinition;
import com.medigy.service.query.QueryDefinitionFactory;
import com.medigy.service.query.QueryDefinitionField;
import com.medigy.service.query.QueryDefinitionSearchService;
import com.medigy.service.query.QueryDefnCondition;
import com.medigy.service.query.SqlComparison;
import com.medigy.service.query.SqlComparisonFactory;
import com.medigy.wicket.DefaultApplication;
import com.medigy.wicket.form.FormMode;
import com.medigy.wicket.panel.DefaultFormPanel;
import wicket.IFeedback;
import wicket.Component;
import wicket.markup.ComponentTag;
import wicket.markup.MarkupStream;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.DropDownChoice;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.ListMultipleChoice;
import wicket.markup.html.form.TextField;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.ListView;
import wicket.markup.html.list.PageableListView;
import wicket.markup.html.list.PageableListViewNavigation;
import wicket.markup.html.list.PageableListViewNavigationLink;
import wicket.markup.html.WebMarkupContainer;
import wicket.model.CompoundPropertyModel;
import wicket.model.IModel;
import wicket.model.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.io.Serializable;

public class PatientSearchFormPanel  extends DefaultFormPanel
{
    private SearchResultsListView resultsListView;
    private SearchResultsHeaderView resultsHeaderView;
    private PatientSearchModel searchModel;

    public PatientSearchFormPanel(final String componentName, final FormMode formMode)
    {
        super(componentName, formMode);

        final int rowsPerPage = 10;
        final QueryDefinitionSearchService service =
                (QueryDefinitionSearchService) ((DefaultApplication) getApplication()).getService(QueryDefinitionSearchService.class);
        searchModel = new PatientSearchModel(service);
        resultsListView = new SearchResultsListView("results", searchModel, rowsPerPage);
        add(resultsListView);

        resultsHeaderView = new SearchResultsHeaderView("resultsHeader");
        // results yet
        add(resultsHeaderView);
        add(new PatientTableNavigation("navigation", resultsListView));
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
        final QueryDefinition queryDefn = QueryDefinitionFactory.getInstance().getQueryDefinition(PatientSearchQueryDefinition.class);
        final PatientSearchModelObject modelObject = new PatientSearchModelObject();
        for (int i=0; i < queryDefn.getFieldNames().size(); i++)
        {
            modelObject.addConditionFieldList(new QueryDefinitionSearchCondition());
        }

        return new PatientSearchForm(componentName, new CompoundPropertyModel(modelObject),
                feedback, queryDefn);
    }


    protected class PatientSearchForm extends Form
    {
        private QueryDefinition queryDefn;

        public PatientSearchForm(final String id, IModel model, final IFeedback feedback, final QueryDefinition queryDefn)
        {
            super(id, model, feedback);
            this.queryDefn = queryDefn;

            add(new PatientSearchListView("conditionFieldList", ((PatientSearchModelObject) getModelObject()).getConditionFieldList(),
                    queryDefn));
            add(new ListMultipleChoice("displayFields", queryDefn.getFieldNames()));
            add(new ListMultipleChoice("sortByFields", queryDefn.getFieldNames()));
        }

        public final void onSubmit()
        {

            final PatientSearchModelObject modelObject = (PatientSearchModelObject) getModelObject();
            searchModel.setSearchParameters(modelObject);
            resultsHeaderView.setModel(new Model((Serializable)modelObject.getDisplayFields()));

            for (QueryDefinitionSearchCondition fc : modelObject.getConditionFieldList())
            {
                System.out.println("field: " + fc.getField()+ " value:" +  fc.getFieldValue());
            }
            System.out.println("display fields: ");
            for (String field : modelObject.getDisplayFields())
            {
                System.out.print(field + ", ");
            }
            setCurrentResultPageToFirst();
            info(getNumberOfResults() + " results found.");
        }
    }

    protected class PatientSearchListView extends ListView
    {
        private QueryDefinition queryDefn;

        public PatientSearchListView(final String id, final IModel model, final QueryDefinition queryDefn)
        {
            super(id, model);
            this.queryDefn = queryDefn;
        }

        public PatientSearchListView(final String id, final List model, final QueryDefinition queryDefn)
        {
            super(id, model);
            this.queryDefn = queryDefn;
        }


        protected IModel getListItemModel(final IModel listViewModel, final int index)
        {
            return  new CompoundPropertyModel(super.getListItemModel(listViewModel, index));
        }


        protected void populateItem(final ListItem item)
        {
            // Right now no need to populate from the model
            final Collection<QueryDefinitionField> queryDefinitionFields = queryDefn.getFields().values();
            final List<String> fieldNames = new ArrayList<String>();
            for (QueryDefinitionField field: queryDefinitionFields)
            {
                fieldNames.add(field.getName());
                final List<SqlComparison> comparisons = field.getValidSqlComparisons();
            }

            item.add(new Label("fieldLabel", "Field"));
            item.add(new DropDownChoice("field", fieldNames));
            item.add(new DropDownChoice("fieldComparison", SqlComparisonFactory.getInstance().listAllNames()));
            item.add(new TextField("fieldValue"));
            item.add(new DropDownChoice("connector", QueryDefnCondition.Connector.listNames()));
        }
    }

    public class PatientSearchModelObject
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

    private static class PatientTableNavigation extends PageableListViewNavigation
    {
        /**
         * Construct.
         *
         * @param id
         *            the name of the component
         * @param table
         *            the table
         */
        public PatientTableNavigation(String id, PageableListView table)
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

    private class SearchResultsListView extends PageableListView
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
}
