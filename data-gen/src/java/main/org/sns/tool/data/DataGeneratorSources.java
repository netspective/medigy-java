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

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;

import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFCell;

/**
 * TODO: review http://www.datamasker.com/dmdatasets.htm that provides a good list of datasets that can be used in
 * demo/test data generation process. Also take a look at http://generatorblog.blogspot.com/.
 */
public class DataGeneratorSources
{
    private final int totalNationalPopulation;
    private final double malePopulationPercentage;
    private final double femalePopulationPercentage;
    private final List<NameAndCount> maleNamesAndCounts = new ArrayList<NameAndCount>();
    private final List<NameAndCount> femaleNamesAndCounts = new ArrayList<NameAndCount>();
    private final List<NameAndCount> surnamesAndCounts = new ArrayList<NameAndCount>();
    private final List<City> largestCities = new ArrayList<City>();
    private final List<String> popularStreetNames = new ArrayList<String>();
    private final List<String> commonStreetSuffixes = new ArrayList<String>();
    private final List<String> uspsStreetSuffixes = new ArrayList<String>();

    public DataGeneratorSources(final InputStream workbookStream) throws IOException
    {
        final POIFSFileSystem fileSystem = new POIFSFileSystem(workbookStream);
        final HSSFWorkbook workbook = new HSSFWorkbook(fileSystem);

        readNamesAndCounts(workbook.getSheet("Common Male Names"), maleNamesAndCounts);
        readNamesAndCounts(workbook.getSheet("Common Female Names"), femaleNamesAndCounts);
        readNamesAndCounts(workbook.getSheet("Common Surnames"), surnamesAndCounts);
        readCitiesAndPopulations(workbook.getSheet("Largest US Cities"));
        readSingleColumn(workbook.getSheet("Popular Street Names"), popularStreetNames);
        readSingleColumn(workbook.getSheet("Common Street Suffixes"), commonStreetSuffixes);
        readSingleColumn(workbook.getSheet("USPS Street Suffixes"), uspsStreetSuffixes);

        final HSSFSheet censusData = workbook.getSheet("Census Data");
        totalNationalPopulation = (int) censusData.getRow(1).getCell((short) 1).getNumericCellValue();
        malePopulationPercentage = censusData.getRow(4).getCell((short) 2).getNumericCellValue() / 100.0;
        femalePopulationPercentage = censusData.getRow(5).getCell((short) 2).getNumericCellValue() / 100.0;

        workbookStream.close();
    }

    public DataGeneratorSources() throws IOException
    {
        // the default Excel file is the same name as this class except ending in ".xls"
        this(DataGeneratorSources.class.getResourceAsStream(DataGeneratorSources.class.getName().substring(DataGeneratorSources.class.getName().lastIndexOf('.')+1) + ".xls"));
    }

    public List<NameAndCount> getFemaleNamesAndCounts()
    {
        return femaleNamesAndCounts;
    }

    public List<NameAndCount> getMaleNamesAndCounts()
    {
        return maleNamesAndCounts;
    }

    public List<NameAndCount> getSurnamesAndCounts()
    {
        return surnamesAndCounts;
    }

    public List<City> getLargestCities()
    {
        return largestCities;
    }

    public List<String> getPopularStreetNames()
    {
        return popularStreetNames;
    }

    public List<String> getCommonStreetSuffixes()
    {
        return commonStreetSuffixes;
    }

    public List<String> getUspsStreetSuffixes()
    {
        return uspsStreetSuffixes;
    }

    public double getMalePopulationPercentage()
    {
        return malePopulationPercentage;
    }

    public double getFemalePopulationPercentage()
    {
        return femalePopulationPercentage;
    }

    public int getTotalNationalPopulation()
    {
        return totalNationalPopulation;
    }

    protected void readNamesAndCounts(final HSSFSheet sheet, final List<NameAndCount> list)
    {
        int rowNum = 1;
        while(true)
        {
            final HSSFRow row = sheet.getRow(rowNum);
            if(row == null)
                break;

            final HSSFCell nameCell = row.getCell((short) 0);
            final String name = nameCell.getStringCellValue();
            if(name == null || name.trim().length() == 0)
                break;

            list.add(new NameAndCount(name, (int) row.getCell((short) 1).getNumericCellValue()));
            rowNum++;
        }
    }

    protected void readSingleColumn(final HSSFSheet sheet, final List<String> list)
    {
        int rowNum = 1;
        while(true)
        {
            final HSSFRow row = sheet.getRow(rowNum);
            if(row == null)
                break;

            final HSSFCell nameCell = row.getCell((short) 0);
            final String data = nameCell.getStringCellValue();
            if(data == null || data.trim().length() == 0)
                break;

            list.add(data);
            rowNum++;
        }
    }

    protected void readCitiesAndPopulations(final HSSFSheet sheet)
    {
        String currentState = "UNKNOWN";
        int rowNum = 1;
        while(true)
        {
            final HSSFRow row = sheet.getRow(rowNum);
            if(row == null)
                break;

            final HSSFCell nameCell = row.getCell((short) 0);
            final String name = nameCell.getStringCellValue();
            if(name == null || name.trim().length() == 0)
                break;

            final HSSFCell zipCodeCell = row.getCell ((short) 1);
            if(zipCodeCell == null || zipCodeCell.getCellType() == HSSFCell.CELL_TYPE_STRING)
            {
                // states are on a line by themselves so save the current state and move on
                currentState = name;
                rowNum++;
                continue;
            }

            final int zipCode = (int) zipCodeCell.getNumericCellValue();
            final int population = (int) row.getCell((short) 2).getNumericCellValue();
            final City city = new City(name, currentState, zipCode, population);

            largestCities.add(city);
            rowNum++;
        }
    }

    public String toString()
    {
        final StringBuffer sb = new StringBuffer();
        sb.append("   Total Population: " + getTotalNationalPopulation() + "\n");
        sb.append("              Males: " + getMalePopulationPercentage() * 100 + "%\n");
        sb.append("            Females: " + getFemalePopulationPercentage()  * 100+ "%\n");
        sb.append("    Common Surnames: " + getSurnamesAndCounts().size()+ "\n");
        sb.append("  Common Male Names: " + getMaleNamesAndCounts().size()+ "\n");
        sb.append("Common Female Names: " + getFemaleNamesAndCounts().size()+ "\n");
        sb.append("     Largest Cities: " + getLargestCities().size()+ "\n");
        sb.append("    Popular Streets: " + getPopularStreetNames().size()+ "\n");
        sb.append("   Streets Suffixes: " + getCommonStreetSuffixes().size()+ "\n");
        sb.append("      USPS Suffixes: " + getUspsStreetSuffixes().size()+ "\n");
        return sb.toString();
    }

    public class NameAndCount
    {
        private final String name;
        private final int count;

        public NameAndCount(final String name, final int count)
        {
            this.name = name;
            this.count = count;
        }

        public int getCount()
        {
            return count;
        }

        public String getName()
        {
            return name;
        }
    }

    public class City
    {
        private final String city;
        private final String state;
        private final int zipCode;
        private final int population;

        public City(final String city, final String state, final int zipCode, final int population)
        {
            this.city = city;
            this.state = state;
            this.zipCode = zipCode;
            this.population = population;
        }

        public String getCity()
        {
            return city;
        }

        public String getState()
        {
            return state;
        }

        public int getZipCode()
        {
            return zipCode;
        }

        public String getFormattedZipCode()
        {
            final String zc = Integer.toString(zipCode);
            if(zc.length() < 5)
            {
                final int shortBy = 5 - zc.length();
                final StringBuffer sb = new StringBuffer();
                while(zc.length() < shortBy)
                    sb.append('0');
                sb.append(zc);
                return sb.toString();
            }

            return zc;
        }

        public int getPopulation()
        {
            return population;
        }

        public String toString()
        {
            return getCity() + ", " + getZipCode() + " " + getFormattedZipCode();
        }
    }
}
