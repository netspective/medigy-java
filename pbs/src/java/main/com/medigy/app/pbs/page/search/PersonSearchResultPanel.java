/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.app.pbs.page.search;

import com.medigy.app.pbs.page.entity.person.AbstractPersonPage;
import com.medigy.app.pbs.page.entity.person.PatientProfile;
import com.medigy.presentation.form.common.CriteriaSearchFormModelObject;
import com.medigy.presentation.form.common.SearchResultPanel;
import com.medigy.presentation.model.common.ServiceCountAndListAction;
import com.medigy.presentation.model.common.ServiceSearchResultModel;
import com.medigy.service.ServiceVersion;
import com.medigy.service.dto.person.PersonSearchParameters;
import com.medigy.service.dto.person.PersonSearchReturnValues;
import com.medigy.service.dto.query.SearchCondition;
import com.medigy.service.person.PersonSearchService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wicket.PageParameters;
import wicket.RequestCycle;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.basic.Label;
import wicket.markup.html.link.Link;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.PageableListView;
import wicket.model.Model;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class PersonSearchResultPanel extends SearchResultPanel
{
    private final Log log = LogFactory.getLog(PersonSearchResultPanel.class);

    public PersonSearchResultPanel(final String id)
    {
        super(id, PersonSearchService.class);
    }

    public ServiceCountAndListAction createCountAndListAction()
    {
        return new ServiceCountAndListAction(this, service);
    }

    public Object invokeService(final Object queryObject)
    {
        final CriteriaSearchFormModelObject formModelObject = (CriteriaSearchFormModelObject) queryObject;
        if (formModelObject == null)
            return 0;
        PersonSearchService pss = (PersonSearchService) getService();
        final PersonSearchReturnValues retValues = pss.searchPerson(new PersonSearchParameters() {
            public List<SearchCondition> getConditions()
            {
                final SearchCondition searchCondition = new SearchCondition();
                searchCondition.setCondition(formModelObject.getSearchCriterias());
                searchCondition.setFieldValue(formModelObject.getSearchCriteriaValue());
                return Arrays.asList(searchCondition);
            }

            public List<String> getDisplayFields()
            {
                return null;
            }

            public List<String> getOrderBys()
            {
                return null;
            }

            public int getStartFromRow()
            {
                return 0;
            }

            public ServiceVersion getServiceVersion()
            {
                return null;
            }
        });
        if (retValues.getErrorMessage() != null)
        {
            log.error(retValues.getErrorMessage());
            return 0;
        }
        return retValues.getSearchResults().size();
    }

    public List invokeService(final Object queryObject, final int startFromRow, final int numberOfRows)
    {
        final CriteriaSearchFormModelObject formModelObject = (CriteriaSearchFormModelObject) queryObject;
        if (formModelObject == null)
            return null;
        PersonSearchService pss = (PersonSearchService) getService();

        final PersonSearchReturnValues retValues = pss.searchPerson(new PersonSearchParameters() {
            public List<SearchCondition> getConditions()
            {
                final SearchCondition searchCondition = new SearchCondition();
                searchCondition.setCondition(formModelObject.getSearchCriterias());
                searchCondition.setFieldValue(formModelObject.getSearchCriteriaValue());
                return Arrays.asList(searchCondition);
            }

            public List<String> getDisplayFields()
            {
                return null;
            }

            public List<String> getOrderBys()
            {
                return null;
            }

            public int getStartFromRow()
            {
                return startFromRow;
            }

            public ServiceVersion getServiceVersion()
            {
                return null;
            }
        });
        if (retValues.getErrorMessage() != null)
        {
            log.error(retValues.getErrorMessage());
            return null;
        }
        return retValues.getSearchResults();
    }

    /**
     * Override the header generation since it is statically defined in the hTML
     * @return
     */
    protected WebMarkupContainer createSearchResultHeader()
    {
        return new SearchResultHeader("resultsHeader");
    }

    protected PageableListView createSearchResultsListView(final ServiceSearchResultModel searchResultModel, final int rowsPerPage)
    {
        final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        return new PageableListView("results", searchResultModel, rowsPerPage)
        {
            public boolean isVersioned()
            {
                return true;
            }

            protected void populateItem(final ListItem item)
            {
                final PersonSearchReturnValues.Person person = (PersonSearchReturnValues.Person) item.getModelObject();
                if (person == null)
                    return;
                item.add(new ProfileLink("firstName", person.getPersonId()).add(new Label("firstName", person.getFirstName())));
                item.add(new ProfileLink("lastName", person.getPersonId()).add(new Label("lastName", person.getLastName())));
                item.add(new Label("gender", person.getGender()));
                item.add(new Label("birthdate", person.getBirthDate() != null ? sdf.format(person.getBirthDate()): ""));
                item.add(new Label("ssn", person.getSsn()));
                item.add(new Label("primaryRole", person.getPrimaryRole()));
            }
        };
    }

    private final class SearchResultHeader extends WebMarkupContainer
    {
        public SearchResultHeader(final String id)
        {
            super(id);
            // ADD LOCALIZED COLUMN NAMES HERE
            add(new Label("firstNameLabel", "First Name"));
            add(new Label("lastNameLabel", "Last Name"));
            add(new Label("genderLabel", "Gender"));
            add(new Label("dobLabel", "Date of Birth"));
            add(new Label("ssnLabel", "SSN"));
            add(new Label("primaryRoleLabel", "Primary Role"));
        }

         public boolean isVisible()
        {
            return searchResultModel.hasResults();
        }
    }

    private final class ProfileLink extends Link
    {
        public ProfileLink(final String name, Long id)
        {
            super(name, new Model(id));
        }

        public void onClick()
        {
            final RequestCycle requestCycle = getRequestCycle();
            final Long id = (Long)getModelObject();

            PageParameters params = new PageParameters();
            params.put(AbstractPersonPage.REQPARAMNAME_PERSON_ID, id);
            requestCycle.setResponsePage(new PatientProfile(PersonSearchResultPanel.this.getPage(), params));
        }
    }

}
