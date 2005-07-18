/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.presentation.model.query;

import com.medigy.presentation.model.common.ServiceSearchResultModel;
import com.medigy.presentation.model.common.ServiceCountAndListAction;
import com.medigy.presentation.model.common.SearchFormModelObject;
import com.medigy.presentation.form.query.QueryDefSearchFormModelObject;
import com.medigy.service.query.QueryDefinitionSearchService;

public class QueryDefSearchResultModel extends ServiceSearchResultModel
{
    private QueryDefSearchFormModelObject searchFormModelObject;

    public QueryDefSearchResultModel(final QueryDefinitionSearchService service, final ServiceCountAndListAction countAndListAction)
    {
        super(service, countAndListAction);
    }

    public final void setSearchParameters(final SearchFormModelObject params)
    {
        detach();
        this.searchFormModelObject = (QueryDefSearchFormModelObject) params;
    }

    public SearchFormModelObject getSearchParameters()
    {
        return searchFormModelObject;
    }
}
