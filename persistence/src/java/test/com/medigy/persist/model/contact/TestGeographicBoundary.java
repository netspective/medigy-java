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
package com.medigy.persist.model.contact;

import com.medigy.persist.TestCase;
import com.medigy.persist.model.party.PostalAddress;
import com.medigy.persist.model.party.PostalAddressBoundary;
import com.medigy.persist.reference.custom.GeographicBoundaryType;
import com.medigy.persist.util.HibernateUtil;

import java.util.List;
import java.util.Set;

public class TestGeographicBoundary extends TestCase
{
    public void testGeographicBoundaryType()
    {
        final List geoTypeList = HibernateUtil.getSession().createCriteria(GeographicBoundaryType.class).list();
        assertEquals(8, GeographicBoundaryType.Cache.values().length);
        assertEquals(GeographicBoundaryType.Cache.COUNTRY.getEntity(), (GeographicBoundaryType) geoTypeList.toArray()[0]);
        assertEquals(GeographicBoundaryType.Cache.REGION.getEntity(), (GeographicBoundaryType) geoTypeList.toArray()[1]);
        assertEquals(GeographicBoundaryType.Cache.TERRITORY.getEntity(), (GeographicBoundaryType) geoTypeList.toArray()[2]);
        assertEquals(GeographicBoundaryType.Cache.PROVINCE.getEntity(), (GeographicBoundaryType) geoTypeList.toArray()[3]);
        assertEquals(GeographicBoundaryType.Cache.STATE.getEntity(), (GeographicBoundaryType) geoTypeList.toArray()[4]);
        assertEquals(GeographicBoundaryType.Cache.POSTAL_CODE.getEntity(), (GeographicBoundaryType) geoTypeList.toArray()[5]);
        assertEquals(GeographicBoundaryType.Cache.COUNTY.getEntity(), (GeographicBoundaryType) geoTypeList.toArray()[6]);
        assertEquals(GeographicBoundaryType.Cache.CITY.getEntity(), (GeographicBoundaryType) geoTypeList.toArray()[7]);

        assertEquals(GeographicBoundaryType.Cache.CITY.getParentEntity(), (GeographicBoundaryType) geoTypeList.toArray()[4]);
        assertEquals(GeographicBoundaryType.Cache.POSTAL_CODE.getParentEntity(), (GeographicBoundaryType) geoTypeList.toArray()[4]);
        assertEquals(GeographicBoundaryType.Cache.STATE.getParentEntity(), (GeographicBoundaryType) geoTypeList.toArray()[0]);
    }

    /**
     * Main purpose is to test the relationship between postal address and geo boundaries
     */
    public void testPostalAddress() throws Exception
    {
        HibernateUtil.beginTransaction();
        final PostalAddress address = new PostalAddress();
        address.setAddress1("123 Acme Street");
        address.setAddress2("Suite 100");

        final Country country = new Country();
        country.setCountryName("United States of America");
        country.setCountryAbbreviation("USA");

        final State virginia = new State();
        virginia.setStateName("Virginia");
        virginia.setStateAbbreviation("VA");
        country.addState(virginia);

        final County county = new County();
        county.setCountyName("Fairfax County");
        virginia.addCounty(county);

        address.setCountry(country);
        address.setState(virginia);
        address.setCounty(county);

        HibernateUtil.getSession().save(country);
        HibernateUtil.getSession().save(address);
        HibernateUtil.commitTransaction();

        final PostalAddress newAddress = (PostalAddress) HibernateUtil.getSession().load(PostalAddress.class, address.getPostalAddressId());
        assertThat(newAddress.getState(), eq(virginia));
        assertThat(newAddress.getCountry(), eq(country));
        assertThat(newAddress.getCounty(), eq(county));
        assertThat(newAddress.getProvince(), NULL);

        assertThat(HibernateUtil.getSession().createCriteria(PostalAddressBoundary.class).list().size(), eq(3));
        final List countrList = HibernateUtil.getSession().createCriteria(Country.class).list();
        assertThat(countrList.size(), eq(1));
    }

    /**
     * Main purpose is to test the various geo boundaries and the relationships between them
     */
    public void testCity()
    {
        HibernateUtil.beginTransaction();
        final Country country = new Country();
        country.setCountryName("United States of America");
        country.setCountryAbbreviation("USA");

        final Country countryB = new Country();
        countryB.setCountryName("Canada");
        countryB.setCountryAbbreviation("CAN");

        HibernateUtil.getSession().save(country);
        HibernateUtil.getSession().save(countryB);

        final State stateA = new State();
        stateA.setStateName("Virginia");
        stateA.setStateAbbreviation("VA");
        stateA.setParentCountry(country);
        country.addState(stateA);

        final State stateB = new State();
        stateB.setStateName("Maryland");
        stateB.setStateAbbreviation("MD");
        stateB.setParentCountry(country);
        country.addState(stateB);

        final Province province = new Province();
        province.setProvinceName("Alberta");
        province.setParentCountry(countryB);
        countryB.addProvince(province);

        final City city = new City();
        city.setCityName("Fairfax");
        city.setParentState(stateA);
        stateA.addCity(city);

        final City cityB = new City();
        cityB.setCityName("Edmonton");
        cityB.setParentProvince(province);
        province.addCity(cityB);

        final PostalCode code = new PostalCode();
        code.setCodeValue("12345");
        code.setParentState(stateA);
        stateA.addPostalCode(code);

        HibernateUtil.commitTransaction();
        List countryList = HibernateUtil.getSession().createCriteria(Country.class).list();
        assertEquals(2, countryList.size());

        final Country usa = (Country) HibernateUtil.getSession().load(Country.class, country.getCountryId());
        assertThat(usa.getCountryName(), eq("United States of America"));
        assertThat(usa.getCountryAbbreviation(), eq("USA"));
        assertThat(usa.getStates().size(), eq(2));
        assertThat(usa.hasState(stateA), eq(true));
        assertThat(usa.hasState(stateB), eq(true));
        assertEquals(usa.getStateByName("Virginia"), stateA);
        assertEquals(usa.getStateByName("Maryland"), stateB);

        final Country canada = (Country) HibernateUtil.getSession().load(Country.class, countryB.getCountryId());
        assertThat(canada.getCountryName(), eq("Canada"));
        assertThat(canada.getCountryAbbreviation(), eq("CAN"));
        assertThat(canada.getProvinces().size(), eq(1));
        assertThat(canada.hasProvince(province), eq(true));

        HibernateUtil.beginTransaction();
        final State ohio = new State("Ohio", "OH");
        ohio.setParentCountry(usa);
        usa.addState(ohio);
        HibernateUtil.commitTransaction();

        final Country updatedUsa = (Country) HibernateUtil.getSession().load(Country.class, usa.getCountryId());
        final Set<State> states = updatedUsa.getStates();
        assertThat(states.size(), eq(3));
        assertEquals(updatedUsa.getStateByName("Ohio"), ohio);

    }
}
