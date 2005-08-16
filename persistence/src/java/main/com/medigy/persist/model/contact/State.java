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

import com.medigy.persist.model.health.HealthCareLicense;
import com.medigy.persist.reference.custom.GeographicBoundaryType;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class State extends GeographicBoundary
{
    public enum Cache
    {
        ALABAMA("ALABAMA", "AL"),
        ALASKA("ALASKA", "AK"),
        AMERICAN_SAMOA("AMERICAN SAMOA", "AS"),
        ARIZONA("ARIZONA", "AZ"),
        ARKANSAS("ARKANSAS", "AR"),
        CALIFORNIA("CALIFORNIA", "CA"),
        COLORADO("COLORADO", "CO"),
        CONNECTICUT("CONNECTICUT", "CT"),
        DELAWARE("DELAWARE", "DE"),
        DC("DISTRICT OF COLUMBIA", "DC"),
        MICRONESIA("FEDERATED STATES OF MICRONESIA", "FM"),
        FLORIDA("FLORIDA", "FL"),
        GEORGIA("GEORGIA", "GA"),
        GUAM("GUAM", "GU"),
        HAWAII("HAWAII",  "HI"),
        IDAHO("IDAHO", "ID"),
        ILLINOIS("ILLINOIS", "IL"),
        INDIANA("INDIANA", "IN"),
        IOWA("IOWA", "IA"),
        KANSAS("KANSAS", "KS"),
        KENTUCKY("KENTUCKY", "KY"),
        LOUISIANA("LOUISIANA", "LA"),
        MAINE("MAINE", "ME"),
        MARSHALL_ISLANDS("MARSHALL ISLANDS", "MH"),
        MARYLAND("MARYLAND", "MD"),
        MASSACHUSETTS("MASSACHUSETTS", "MA"),
        MICHIGAN("MICHIGAN", "MI"),
        MINNESOTA("MINNESOTA", "MN"),
        MISSISSIPPI("MISSISSIPPI", "MS"),
        MISSOURI("MISSOURI", "MO"),
        MONTANA("MONTANA", "MT"),
        NEBRASKA("NEBRASKA", "NE"),
        NEVADA("NEVADA", "NV"),
        NEW_HAMPSHIRE("NEW HAMPSHIRE", "NH"),
        NEW_JERSEY("NEW JERSEY", "NJ"),
        NEW_MEXICO("NEW MEXICO", "NM"),
        NEW_YORK("NEW YORK",  "NY"),
        NORTH_CAROLINA("NORTH CAROLINA",  "NC"),
        NORTH_DAKOTA("NORTH DAKOTA", "ND"),
        NORTHERN_MARIANA_ISLANDS("NORTHERN MARIANA ISLANDS", "MP"),
        OHIO("OHIO", "OH"),
        OKLAHOMA("OKLAHOMA", "OK"),
        OREGON("OREGON", "OR"),
        PALAU("PALAU", "PW"),
        PENNSYLVANIA("PENNSYLVANIA", "PA"),
        PUERTO_RICO("PUERTO RICO", "PR"),
        RHODE_ISLAND("RHODE ISLAND", "RI"),
        SOUTH_CAROLINA("SOUTH CAROLINA", "SC"),
        SOUTH_DAKOTA("SOUTH DAKOTA", "SD"),
        TENNESSEE("TENNESSEE", "TN"),
        TEXAS("TEXAS", "TX"),
        UTAH("UTAH", "UT"),
        VERMONT("VERMONT", "VT"),
        VIRGIN_ISLANDS("VIRGIN ISLANDS", "VI"),
        VIRGINIA("VIRGINIA", "VA"),
        WASHINGTON("WASHINGTON", "WA"),
        WEST_VIRGINIA("WEST VIRGINIA", "WV"),
        WISCONSIN("WISCONSIN", "WI"),
        WYOMING("WYOMING", "WY");

        private String stateName;
        private String stateAbbreviation;
        private State entity;

        Cache(final String name, final String abbrev)
        {
            this.stateName = name;
            this.stateAbbreviation = abbrev;
        }

        public String getStateName()
        {
            return this.stateName;
        }

        public String getStateAbbreviation()
        {
            return this.stateAbbreviation;
        }

        public State getEntity()
        {
            if (entity == null)
                throw new RuntimeException(getClass() + " "+ name() + " has not been initialized");
            return entity;
        }

        public void setEntity(final State entity)
        {
            this.entity = entity;
        }
    }

    private String fips;
    private String stateName;
    private String stateAbbreviation;

    private Country parentCountry;
    private Set<City> cities = new HashSet<City>();
    private Set<County> counties = new HashSet<County>();
    private Set<PostalCode> postalCodes = new HashSet<PostalCode>();

    private Set<HealthCareLicense> healthCareLicenses = new HashSet<HealthCareLicense>();

    public State()
    {
        setType(GeographicBoundaryType.Cache.STATE.getEntity());
    }

    public State(final String name, final String abbreviation)
    {
        setType(GeographicBoundaryType.Cache.STATE.getEntity());
        setStateName(name);
        setStateAbbreviation(abbreviation);
    }

    @Transient
    public Long getStateId()
    {
        return getGeoId();
    }

    public void setStateId(final Long id)
    {
        setGeoId(id);
    }

    @Column(length = 5)
    public String getFips()
    {
        return fips;
    }

    public void setFips(final String fips)
    {
        this.fips = fips;
    }

    @OneToMany(mappedBy = "parentState", cascade = CascadeType.ALL)
    public Set<City> getCities()
    {
        return cities;
    }

    public void setCities(final Set<City> cities)
    {
        this.cities = cities;
    }

    @Transient
    public void addCity(final String cityName)
    {
        final City city = new City(cityName);
        city.setParentState(this);
        cities.add(city);
    }

    @Transient
    public void addCity(final City city)
    {
        city.setParentState(this);
        cities.add(city);
    }

    @OneToMany(mappedBy = "parentState", cascade = CascadeType.ALL)
    public Set<County> getCounties()
    {
        return counties;
    }

    public void setCounties(final Set<County> counties)
    {
        this.counties = counties;
    }

    @ManyToOne
    @JoinColumn(name = "country_id", referencedColumnName = "geo_id", nullable = false)
    public Country getParentCountry()
    {
        return parentCountry;
    }

    public void setParentCountry(final Country parentCountry)
    {
        this.parentCountry = parentCountry;
    }

    public String getStateName()
    {
        return stateName;
    }

    public void setStateName(final String stateName)
    {
        this.stateName = stateName;
    }

    public String getStateAbbreviation()
    {
        return stateAbbreviation;
    }

    public void setStateAbbreviation(final String stateAbbreviation)
    {
        this.stateAbbreviation = stateAbbreviation;
    }

    @OneToMany(mappedBy = "parentState", cascade =  CascadeType.ALL)
    public Set<PostalCode> getPostalCodes()
    {
        return postalCodes;
    }

    public void setPostalCodes(final Set<PostalCode> postalCodes)
    {
        this.postalCodes = postalCodes;
    }

    @Transient
    public void addPostalCode(final String code)
    {
        final PostalCode pc = new PostalCode();
        pc.setCodeValue(code);
        pc.setParentState(this);
        this.postalCodes.add(pc);
    }

    @Transient
    public void addPostalCode(final PostalCode code)
    {
        code.setParentState(this);
        this.postalCodes.add(code);
    }

    public boolean equals(Object obj)
    {
        if (obj == null)
            return false;
        final State compareState = (State) obj;
        if (compareState.getStateId() != null && this.getStateId() != null)
        {
            if (getStateId().longValue() == compareState.getStateId().longValue())
                return true;
            else
                return false;
        }
        else
        {
            if (getStateName().equals(compareState.getStateName()) &&
                getStateAbbreviation().equals(compareState.getStateAbbreviation()) &&
                (getParentCountry() != null && getParentCountry().equals(compareState.getParentCountry())))
                return true;
            else
                return false;
        }
    }

    /**
     * Gets the city belonging to this state by the city's name
     * @param cityName
     * @return Null if no city with the exact stateName is found
     */
    @Transient
    public City getCityByName(final String cityName)
    {
        for (City city : cities)
        {
            if (city.getCityName().equals(cityName))
                return city;
        }
        return null;
    }

    /**
     * Gets a county belonging to this state by the county's name
     * @param countyName
     * @return
     */
    @Transient
    public County getCountyByName(final String countyName)
    {
        for (County county : counties)
        {
            if (county.getCountyName().equals(countyName))
                return county;
        }
        return null;
    }

    @Transient
    public void addCounty(final String countyName)
    {
        final County county = new County();
        county.setCountyName(countyName);
        county.setParentState(this);
        getCounties().add(county);
    }
    
    @Transient
    public void addCounty(final County county)
    {
        county.setParentState(this);
        getCounties().add(county);
    }

    @Transient
    public PostalCode getPostalCodeByValue(final String postalCodeName)
    {
        for (PostalCode pc : postalCodes)
        {
            if (pc.getCodeValue().equals(postalCodeName))
            return pc;
        }
        return null;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "state", fetch = FetchType.LAZY)
    public Set<HealthCareLicense> getHealthCareLicenses()
    {
        return healthCareLicenses;
    }

    public void setHealthCareLicenses(final Set<HealthCareLicense> healthCareLicenses)
    {
        this.healthCareLicenses = healthCareLicenses;
    }
}
