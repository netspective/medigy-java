/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import java.util.Collection;
import java.util.Iterator;

public abstract class AbstractFacade  implements Facade
{
    protected SessionFactory sessionFactory;

    public AbstractFacade(final SessionFactory sessionFactory)
    {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Gets the thread bound hibernate session. This will throw a runtime exception if a Session doesn't already
     * exist.
     *
     * @return
     */
    public Session getSession()
    {
        return SessionFactoryUtils.getSession(sessionFactory, false);
        //return HibernateUtil.getSession();
    }

    public <T> void convert(Class<T> type, Collection raw, Collection<T> cooked)
    {
        for (Iterator i = raw.iterator(); i.hasNext();)
        {
            cooked.add(type.cast(i.next()));
        }
    }
}
