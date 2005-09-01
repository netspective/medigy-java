package com.medigy.presentation.navigation.paging;

import wicket.markup.html.list.PageableListView;
import wicket.markup.html.navigation.paging.PagingNavigator;

public class PageableListViewNavigator extends PagingNavigator
{
    public PageableListViewNavigator(final String id, final PageableListView pageableListView)
    {
        super(id, pageableListView);
    }
}

