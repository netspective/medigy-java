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
package com.medigy.service.contact;

import com.medigy.persist.model.contact.GeographicBoundary;
import com.medigy.persist.model.contact.State;
import com.medigy.persist.model.contact.Country;
import com.medigy.persist.model.contact.City;
import com.medigy.persist.reference.custom.GeographicBoundaryType;
import com.medigy.service.util.Facade;

import java.util.List;

/**
 * Interface class for geographic boundary related activities. Implementing classes
 * will be used by service layer classes to perform higher level functions.
 */
public interface GeographicBoundaryFacade extends Facade
{
    /**
     * Gets all states defined for the USA by default
     * @return list of states
     */
    public List<State> listStates();
    public List<State> listStates(Country country);

    public State getStateByAbbreviation(final String abbrev);

    /**
     * Lists cities belonging to a state
     * @param state
     * @return list of cities
     */
    public List<City> listCitiesByState(final State state);

    /**
     * Adds a new geographic boundary who has no parents
     * @param name
     * @param type
     * @return Unique ID for the new geographic boundary
     */
    public GeographicBoundary addGeographicBoundary(String name, GeographicBoundaryType type);

    /**
     * Add a new geographic boundary
     * @param name      Name of the geographic boundary
     * @param type      The geo boundary type
     * @param parents   The geo boundaries to which this new one belongs to
     */
    public GeographicBoundary addGeographicBoundary(String name, GeographicBoundaryType type, GeographicBoundary[] parents);

    /**
     * List all geographic boundaries of the same type
     * @param type
     * @return list of geographic bounaries
     */
    public List<GeographicBoundary> listGeographicBoundaries(GeographicBoundaryType type);

    /**
     * Gets a geographic boundary by its name and type. The name will be a case insensitive
     * exact match.
     *
     * @param name
     * @param type
     * @return      an existing geo boundary and Null if it doesn't exist
     */
    public GeographicBoundary getGeographicBoundary(String name, GeographicBoundaryType type);

    /**
     * Gets a geographic boundary by its name and type. The name will be a case insensitive
     * exact match. Also if one doesn't exist, a new one will be created and returned.
     * @param name
     * @param type
     * @param addIfNew
     * @return
     */
    public GeographicBoundary getGeographicBoundary(String name, GeographicBoundaryType type,
                                                    boolean addIfNew);

    
}
