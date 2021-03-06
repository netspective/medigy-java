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
package org.sns.tool.data;

import java.util.Random;
import java.util.List;

import org.sns.tool.data.DataGeneratorSources.City;

public class USAddressDataGenerator
{
    private final DataGeneratorSources sources;
    private final Random random = new Random();
    private final List<String> usedStreetNames = new LimitedArrayList<String>(100);
    private final List<City> usedCities = new LimitedArrayList<City>(100);

    public USAddressDataGenerator(final DataGeneratorSources sources)
    {
        this.sources = sources;
    }

    public String getRandomStreetAddress(int lowBuildingNumber, int highBuildingNumber)
    {
        final List<String> streetNames = sources.getPopularStreetNames();
        String streetName = null;
        for(int i = 0; i < 15; i++)
        {
            final String name = streetNames.get(random.nextInt(streetNames.size()));
            if(! usedStreetNames.contains(name))
            {
                usedStreetNames.add(name);
                streetName = name;
                break;
            }
        }

        // if we couldn't find a unique street name after 15 tries just go ahead and use a potentially duplicate one
        if(streetName == null)
            streetName = streetNames.get(random.nextInt(streetNames.size()));

        final int randomBldngNumber = RandomUtils.generateRandomNumberBetween(lowBuildingNumber, highBuildingNumber);
        final List<String> suffixes = sources.getCommonStreetSuffixes();
        final String suffix = suffixes.get(random.nextInt(suffixes.size()));

        return randomBldngNumber + " " + streetName + " " + suffix;
    }

    public City getRandomCity()
    {
        final List<City> cities = sources.getLargestCities();
        for(int i = 0; i < 15; i++)
        {
            final City city = cities.get(random.nextInt(cities.size()));
            if(! usedCities.contains(city))
            {
                usedCities.add(city);
                return city;
            }
        }

        // just return a potentially duplicate random city
        return cities.get(random.nextInt(cities.size()));
    }

    public String getRandomPhoneNumber(final City city)
    {
        // TODO: try to generate proper exchanges and prefixs; right now we're just creating truly random ones
        return RandomUtils.generateRandomNumberBetween(100, 999) + "-" +
               RandomUtils.generateRandomNumberBetween(1000, 9999);
    }

    public String getRandomPhoneAreaCode(final City city)
    {
        return "" + RandomUtils.generateRandomNumberBetween(100, 999);
    }
}
