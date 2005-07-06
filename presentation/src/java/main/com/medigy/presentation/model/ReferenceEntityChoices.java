/*
 * Copyright (c) 2000-2003 Netspective Communications LLC. All rights reserved.
 *
 * Netspective Communications LLC ("Netspective") permits redistribution, modification and use of this file in source
 * and binary form ("The Software") under the Netspective Source License ("NSL" or "The License"). The following
 * conditions are provided as a summary of the NSL but the NSL remains the canonical license and must be accepted
 * before using The Software. Any use of The Software indicates agreement with the NSL.
 *
 * 1. Each copy or derived work of The Software must preserve the copyright notice and this notice unmodified.
 *
 * 2. Redistribution of The Software is allowed in object code form only (as Java .class files or a .jar file
 *    containing the .class files) and only as part of an application that uses The Software as part of its primary
 *    functionality. No distribution of the package is allowed as part of a software development kit, other library,
 *    or development tool without written consent of Netspective. Any modified form of The Software is bound by these
 *    same restrictions.
 *
 * 3. Redistributions of The Software in any form must include an unmodified copy of The License, normally in a plain
 *    ASCII text file unless otherwise agreed to, in writing, by Netspective.
 *
 * 4. The names "Netspective", "Axiom", "Commons", "Junxion", and "Sparx" are trademarks of Netspective and may not be
 *    used to endorse products derived from The Software without without written consent of Netspective. "Netspective",
 *    "Axiom", "Commons", "Junxion", and "Sparx" may not appear in the names of products derived from The Software
 *    without written consent of Netspective.
 *
 * 5. Please attribute functionality where possible. We suggest using the "powered by Netspective" button or creating
 *    a "powered by Netspective(tm)" link to http://www.netspective.com for each application using The Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT,
 * ARE HEREBY DISCLAIMED.
 *
 * NETSPECTIVE AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE OR ANY THIRD PARTY AS A
 * RESULT OF USING OR DISTRIBUTING THE SOFTWARE. IN NO EVENT WILL NETSPECTIVE OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THE SOFTWARE, EVEN
 * IF HE HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * @author Shahid N. Shah
 */

/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.presentation.model;

import com.medigy.persist.reference.CachedReferenceEntity;
import com.medigy.persist.reference.ReferenceEntity;
import wicket.markup.html.form.model.IChoice;
import wicket.markup.html.form.model.IChoiceList;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple choice list for providing choices in a static ReferenceEntity. This class implements
 * {@link wicket.markup.html.form.model.IChoiceList}so that it is easier to
 * create subclasses and anonymous implementations.
 */
public class ReferenceEntityChoices implements IChoiceList
{
	/** The reference entity that we're getting the enums from */
	private Class entity;
    private List<Choice> choices;

    /**
	 * Implementation of IChoice for simple objects.
	 */
	private class Choice implements IChoice
	{
		/** The index of the choice */
		private final int index;

		/** The choice model object */
		private final CachedReferenceEntity object;

		/**
		 * Constructor
		 *
		 * @param object
		 *            The object
		 * @param index
		 *            The index of the object in the choice list
		 */
		public Choice(final CachedReferenceEntity object, final int index)
		{
			this.object = object;
			this.index = index;
		}

		/**
		 * @see wicket.markup.html.form.model.IChoice#getDisplayValue()
		 */
		public String getDisplayValue()
		{
			return object.getLabel();
		}

		/**
		 * @see wicket.markup.html.form.model.IChoice#getId()
		 */
		public String getId()
		{
			return object.getCode();
		}

		/**
		 * @see wicket.markup.html.form.model.IChoice#getObject()
		 */
		public Object getObject()
		{
			return object.getCode();
		}
	}

	/**
	 * Constructor
	 * @param entity Choices to add to this list
	 */
	public ReferenceEntityChoices(final Class entity)
	{
        this.entity = entity;
        boolean foundCache = false;
        for (final Class ic : this.entity.getClasses())
        {
            if (CachedReferenceEntity.class.isAssignableFrom(ic))
            {
                if (ic.isEnum())
                {
                    int i = 0;
                    choices = new ArrayList<Choice>();
                    for(final CachedReferenceEntity c : (CachedReferenceEntity[]) ic.getEnumConstants())
                    {
                        choices.add(new Choice(c, i));
                        i++;
                    }
                    foundCache = true;
                }
                else
                    throw new RuntimeException(ic + " must be an enum since " + this.entity + " is a " + ReferenceEntity.class.getName());

                break;
            }

        }

        if (!foundCache)
            throw new RuntimeException(this.entity + " is marked as a ReferenceEntity but does not contain a ReferenceEntityCache enum.");
	}

    public Class getEntity()
    {
        return entity;
    }

	/**
	 * @see IChoiceList#attach()
	 */
	public void attach()
	{
	}

    /**
     * @see wicket.model.IDetachable#detach()
     */
    public void detach()
    {
    }

	/**
	 * @see wicket.markup.html.form.model.IChoiceList#choiceForId(java.lang.String)
	 */
	public IChoice choiceForId(String id)
	{
        for(Choice c : choices)
            if(c.getId().equals(id))
                return c;

        return null;
	}

	/**
	 * @see wicket.markup.html.form.model.IChoiceList#choiceForObject(java.lang.Object)
	 */
	public IChoice choiceForObject(final Object object)
	{
        for(Choice c : choices)
            if(c.getObject() == object)
                return c;

        return null;
	}

	/**
	 * @see wicket.markup.html.form.model.IChoiceList#get(int)
	 */
	public IChoice get(final int index)
	{
		attach();
		if (index != -1)
			return choices.get(index);

		return null;
	}

	/**
	 * @see wicket.markup.html.form.model.IChoiceList#size()
	 */
	public int size()
	{
		attach();
		return choices.size();
	}
}
