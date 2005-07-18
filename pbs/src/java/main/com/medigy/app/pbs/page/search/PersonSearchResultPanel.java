/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.app.pbs.page.search;

import com.medigy.presentation.form.common.SearchResultPanel;
import com.medigy.service.person.PersonSearchService;

public class PersonSearchResultPanel extends SearchResultPanel
{
    public PersonSearchResultPanel(final String id)
    {
        super(id, PersonSearchService.class);
    }
}
