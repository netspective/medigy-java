/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.util;

import com.medigy.persist.util.HibernateUtil;
import org.hibernate.Session;

import java.util.Collection;
import java.util.Iterator;

public class AbstractFacade
{
    public Session getSession()
    {
        return HibernateUtil.getSession();
    }

    public <T> void convert(Class<T> type, Collection raw, Collection<T> cooked)
    {
        for (Iterator i = raw.iterator(); i.hasNext();)
        {
            cooked.add(type.cast(i.next()));
        }
    }
}
