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
 */
package com.medigy.service.impl.contact;

import com.medigy.persist.model.contact.GeographicBoundary;
import com.medigy.persist.model.contact.State;
import com.medigy.persist.model.contact.Country;
import com.medigy.persist.model.contact.City;
import com.medigy.persist.reference.custom.GeographicBoundaryType;
import com.medigy.service.contact.GeographicBoundaryFacade;
import com.medigy.service.util.AbstractFacade;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import java.util.ArrayList;

public class GeographicBoundaryFacadeImpl extends AbstractFacade implements GeographicBoundaryFacade
{
    public GeographicBoundaryFacadeImpl(final SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }

    /**
     * Adds a new geographic boundary who has no parents
     *
     * @param boundaryName
     * @param type
     */
    public GeographicBoundary addGeographicBoundary(String boundaryName, GeographicBoundaryType type)
    {
        return addGeographicBoundary(boundaryName, type, null);
    }

    /**
     * Add a new geographic boundary
     *
     * @param boundaryName    Name of the geographic boundary
     * @param type    The geo boundary type
     * @param parentBoundaries The geo boundaries to which this new one belongs to
     */
    public GeographicBoundary addGeographicBoundary(String boundaryName, GeographicBoundaryType type,
                                                    GeographicBoundary[] parentBoundaries)
    {
        if (boundaryName == null || boundaryName.length() == 0)
            return null;
        // check to see if this geographic boundary already exists
        GeographicBoundary geo = getGeographicBoundary(boundaryName, type);

        /*
        if (geo == null)
        {
            // this is a new geo boundary then
            geo = new GeographicBoundary();
            geo.setName(boundaryName);
            geo.setPartyType(type);
            HibernateUtil.getSession().save(geo);

            // need to asscoiate it with its parents
            if (parentBoundaries != null && parentBoundaries.length > 0)
            {
                for (GeographicBoundary parentGeo: parentBoundaries)
                    addGeographicBoundaryAssociation(geo, parentGeo);
            }
        }
        */
        return geo;

    }

    /**
     * List all geographic boundaries of the same type
     *
     * @param type
     * @return list of geo boundaries
     */
    public List<GeographicBoundary> listGeographicBoundaries(GeographicBoundaryType type)
    {
        Criteria criteria = getSession().createCriteria(GeographicBoundary.class);
        List list = criteria.createAlias("type", "type").add(Restrictions.eq("type.code", type.getCode())).list();
        List<GeographicBoundary> geoList = new ArrayList<GeographicBoundary>();

        convert(GeographicBoundary.class, list, geoList);
        return geoList;
    }

    /**
     * Gets a geographic boundary by its name and type. The name will be a case insensitive
     * exact match.
     *
     * @param name
     * @param type
     * @return the new geo boundary
     */
    public GeographicBoundary getGeographicBoundary(String name, GeographicBoundaryType type)
    {
        return getGeographicBoundary(name, type, false);
    }

    public GeographicBoundary getGeographicBoundary(String name, GeographicBoundaryType type, boolean addIfNew)
    {
        Criteria criteria =
                getSession().createCriteria(GeographicBoundary.class).add(Restrictions.eq("name", name).ignoreCase());
        criteria.createAlias("type", "type").add(Restrictions.eq("type.code", type.getCode()));
        GeographicBoundary boundary = (GeographicBoundary) criteria.uniqueResult();

        if (boundary == null)
            boundary = addGeographicBoundary(name, type);
        return boundary;
    }

    public List<State> listStates()
    {
        List stateList = getSession().createQuery("from State state where state.parentCountry.isoTwoLetterCode = ?").setString(0, Country.USA_ISO_TWO_LETTER_CODE).list();
        List<State> newList = new ArrayList<State>();
        convert(State.class, stateList, newList);
        return newList;
    }

    public List<State> listStates(Country country)
    {
        List stateList = getSession().createQuery("from State state where state.parentCountry.id = ?").setLong(0, country.getGeoId()).list();
        List<State> newList = new ArrayList<State>();
        convert(State.class, stateList, newList);
        return newList;
    }

    public State getStateByAbbreviation(final String abbrev)
    {
        final State state = (State) getSession().createCriteria(State.class).add(Restrictions.eq("stateAbbreviation", abbrev).ignoreCase()).uniqueResult();
        return state;
    }

    public List<City> listCitiesByState(final State state)
    {
        List cityList = getSession().createQuery("from City city where city.parentState.id = ?").setLong(0, state.getGeoId()).list();
        List<City> newList = new ArrayList<City>();
        convert(City.class, cityList, newList);
        return newList;
    }

}
