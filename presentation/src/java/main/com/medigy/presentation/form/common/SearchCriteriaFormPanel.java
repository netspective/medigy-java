/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.presentation.form.common;

import com.medigy.persist.util.query.QueryDefnCondition;
import com.medigy.presentation.model.common.SearchFormModelObject;
import com.medigy.service.SearchService;
import com.medigy.wicket.DefaultApplication;
import com.medigy.wicket.form.FormMode;
import wicket.IFeedback;
import wicket.markup.html.form.DropDownChoice;
import wicket.markup.html.form.TextField;
import wicket.markup.html.form.model.IChoice;
import wicket.markup.html.form.model.IChoiceList;
import wicket.markup.html.panel.FeedbackPanel;
import wicket.markup.html.panel.Panel;
import wicket.model.CompoundPropertyModel;
import wicket.model.IModel;

import java.util.ArrayList;
import java.util.List;

public class SearchCriteriaFormPanel extends Panel
{
    protected SearchService service;
    protected SearchForm searchForm;

    public SearchCriteriaFormPanel(final String id, final FormMode formMode, final Class<? extends SearchService> serviceClass)
    {
        super(id);
        this.service = (SearchService) ((DefaultApplication) getApplication()).getService(serviceClass);
        // Create feedback panel and add to page
        final FeedbackPanel feedback = new FeedbackPanel("feedback");
        add(feedback);
        add(searchForm = createForm("form", feedback, formMode));
        //TODO: Add the record ADD panel 
    }

    protected SearchForm createForm(final String componentName, final IFeedback feedback, final FormMode formMode)
    {
        final Object modelObject = new CriteriaSearchFormModelObject();
        return new CriteriaSearchForm(componentName, new CompoundPropertyModel(modelObject), feedback);
    }

    protected class CriteriaSearchForm extends SearchForm
    {
        public CriteriaSearchForm(final String componentName, final IModel iModel, final IFeedback iFeedback)
        {
            super(componentName, iModel, iFeedback);
            initializeForm();
        }

        public void initializeForm()
        {
            final List<QueryDefnCondition> conditionList= service.getCriteriaList();
            add(new DropDownChoice("searchCriterias", new CriteriaFieldChoiceList(conditionList)));
            add(new TextField("searchCriteriaValue"));

        }

        public void onSubmit()
        {
            super.onSubmit();
            final SearchFormModelObject formModelObject = (SearchFormModelObject) getModelObject();
            ((SearchResultPanel) getPage().get("border.searchBorder.searchResultsPanel")).onSearchExecute(formModelObject);
        }

        public class CriteriaFieldChoiceList implements IChoiceList
        {
            private List<IChoice> choices = new ArrayList<IChoice>();

            private class CriteriaChoice implements IChoice
            {
                private QueryDefnCondition condition;

                public CriteriaChoice(final QueryDefnCondition condition)
                {
                    this.condition = condition;
                }

                public String getDisplayValue()
                {
                    return condition.getField().getCaption();
                }

                public String getId()
                {
                    return condition.getName();
                }

                public Object getObject()
                {
                    return condition;
                }
            }

            public CriteriaFieldChoiceList(final List<QueryDefnCondition> conditions)
            {
                for (QueryDefnCondition cond : conditions)
                    choices.add(new CriteriaChoice(cond));
            }

            public void attach()
            {

            }

            public IChoice choiceForId(String id)
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
    }
}
