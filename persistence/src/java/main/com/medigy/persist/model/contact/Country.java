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

import com.medigy.persist.reference.custom.GeographicBoundaryType;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = "country_name") })
@Inheritance(strategy = InheritanceType.JOINED )
public class Country extends GeographicBoundary
{
    public static final String USA_ISO_TWO_LETTER_CODE = "US";

    private String countryName;
    private String isoTwoLetterCode;
    private String isoThreeLetterCode;
    private String isoThreeDigitCode;

    private Set<State> states = new HashSet<State>();
    private Set<Province> provinces = new HashSet<Province>();

    public Country()
    {
        setType(GeographicBoundaryType.Cache.COUNTRY.getEntity());
    }

    public Country(final String name, final String abbreviation)
    {
        setType(GeographicBoundaryType.Cache.COUNTRY.getEntity());
        setCountryName(name);
        setCountryAbbreviation(abbreviation);
    }

    @Transient
    public Long getCountryId()
    {
        return getGeoId();
    }

    protected void setCountryId(final Long id)
    {
        setGeoId(id);
    }

    @Column(length = 3, name = "iso_three_digit_code")
    public String getIsoThreeDigitCode()
    {
        return isoThreeDigitCode;
    }

    /**
     * Sets the ISO3166 3-digit code for the country. If the code is for some reason less than 3 digits long,
     * zeroes are prepended to make it valid.
     * @param code
     */
    public void setIsoThreeDigitCode(final String code)
    {
        this.isoThreeDigitCode = code;
        if (isoThreeDigitCode != null)
        {
            if (isoThreeDigitCode.length() == 2)
                isoThreeDigitCode = "0" + isoThreeDigitCode;
            else if (isoThreeDigitCode.length() == 1)
                isoThreeDigitCode = "00" + isoThreeDigitCode;
        }
    }

    @Column(length = 3, name = "iso_three_letter_code")
    public String getIsoThreeLetterCode()
    {
        return isoThreeLetterCode;
    }

    public void setIsoThreeLetterCode(final String isoThreeLetterCode)
    {
        this.isoThreeLetterCode = isoThreeLetterCode;
    }

    @Column(length = 2, name = "iso_two_letter_code")
    public String getIsoTwoLetterCode()
    {
        return isoTwoLetterCode;
    }

    public void setIsoTwoLetterCode(final String isoTwoLetterCode)
    {
        this.isoTwoLetterCode = isoTwoLetterCode;
    }

    @OneToMany(mappedBy = "parentCountry", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public Set<State> getStates()
    {
        return states;
    }

    public void setStates(final Set<State> states)
    {
        this.states = states;
    }

    @Transient
    public void addState(final String name, final String abbrev)
    {
        final State state = new State();
        state.setStateName(name);
        state.setStateAbbreviation(abbrev);
        this.states.add(state);
    }

    @Transient
    public void addState(final State state)
    {
        state.setParentCountry(this);
        this.states.add(state);
    }

    /**
     * Checks to see if the state already belongs to the country
     * @param testState
     * @return  True if the State belongs to this country
     */
    @Transient
    public boolean hasState(final State testState)
    {
        final Set<State> states = getStates();
        for (State state : states)
        {
            if (state.equals(testState))
                return true;
        }
        return false;
    }

    /**
     * Gets a child state belonging to the country by the state's name
     * @param stateName
     * @return state belonging to the country
     */
    @Transient
    public State getStateByName(final String stateName)
    {
        final Set<State> states = getStates();
        for (State state : states)
        {
            if (state.getStateName().equals(stateName))
                return state;
        }
        return null;
    }

    /**
     * Gets a child province belonging to the country by its name
     * @param provinceName
     * @return province belonging to the country
     */
    @Transient
    public Province getProvinceByName(final String provinceName)
    {
        for (Province province : provinces)
        {
            if (province.getProvinceName().equals(provinceName))
                return province;
        }
        return null;
    }

    @OneToMany(mappedBy = "parentCountry", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public Set<Province> getProvinces()
    {
        return provinces;
    }

    public void setProvinces(final Set<Province> provinces)
    {
        this.provinces = provinces;
    }

    @Transient
    public boolean hasProvince(final Province province)
    {
        for (Province p : provinces)
        {
            if (p.equals(province))
                return true;
        }
        return false;
    }

    @Column(nullable =  false, name = "country_name")
    public String getCountryName()
    {
        return countryName;
    }

    public void setCountryName(final String countryName)
    {
        this.countryName = countryName;
    }

    /**
     * @deprecated The ISO two letter code will now be used as the abbreviation
     * @return country abbreviation
     */
    @Transient
    public String getCountryAbbreviation()
    {
        return getIsoTwoLetterCode();
    }

    /**
     * @deprecated The ISO two letter code will now be used as the abbreviation
     * @param countryAbbreviation
     */
    public void setCountryAbbreviation(final String countryAbbreviation)
    {
        setIsoTwoLetterCode(countryAbbreviation);
    }
    
    @Transient
    public void addProvince(final Province province)
    {
        this.provinces.add(province);
    }

    @Transient
    public void addProvince(final String provinceName)
    {
        final Province province = new Province();
        province.setProvinceName(provinceName);
        province.setParentCountry(this);
        this.provinces.add(province);
    }

    @Transient
    public State getStateByCode(final String stateCode)
    {
        final Set<State> states = getStates();
        for (State state : states)
        {
            if (state.getStateAbbreviation().equals(stateCode))
                return state;
        }
        return null;
    }

    public String toString()
    {
        return super.toString() + "[name=" + getCountryName() + "]";
    }
}
