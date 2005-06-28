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
package com.medigy.tool.persist.populate;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.sns.tool.data.DataGeneratorSources;
import org.sns.tool.data.DataGeneratorSources.City;
import org.sns.tool.data.PersonDataGenerator;
import org.sns.tool.data.PersonDataGenerator.Gender;
import org.sns.tool.data.RandomUtils;
import org.sns.tool.data.USAddressDataGenerator;

import com.medigy.persist.model.org.Organization;
import com.medigy.persist.model.person.Ethnicity;
import com.medigy.persist.model.person.Person;
import com.medigy.persist.reference.custom.person.EthnicityType.Cache;
import com.medigy.persist.reference.type.GenderType;
import com.medigy.persist.util.ModelInitializer;
import com.medigy.persist.util.ModelInitializer.SeedDataPopulationType;

public class DataPopulatorTask extends Task
{
    private Class hibernateConfigClass;
    private String hibernateConfigFile;
    private String dialect;
    private String url;
    private String driver;
    private String userid;
    private String password;
    private boolean showSql;
    private String orgNameTemplate = "Medigy Demo Billing Service {1}";
    private int generatePrimaryOrgsCount = 10;
    private int generatePatientsPerOrgCount = 10;
    private DataGeneratorSources dataGeneratorSources;

    public void execute() throws BuildException
    {
        if(hibernateConfigClass == null)
            throw new BuildException("hibernateConfigClass was not provided.");

        if(url == null || driver == null || userid == null || password == null)
            throw new BuildException("url, driver, userid, and password are required attributes.");

        final Configuration configuration;
        try
        {
            configuration = (Configuration) hibernateConfigClass.newInstance();
            if(hibernateConfigFile != null)
                configuration.configure(hibernateConfigFile);

            if(dialect != null)
                configuration.setProperty(Environment.DIALECT, dialect);

            configuration.setProperty(Environment.DRIVER, driver);
            configuration.setProperty(Environment.URL, url);
            configuration.setProperty(Environment.USER, userid);
            configuration.setProperty(Environment.PASS, password);

            if(showSql)
                configuration.setProperty(Environment.SHOW_SQL, "true");
        }
        catch (final Exception e)
        {
            throw new BuildException(e);
        }

        log("Using Hibernate Configuration " + configuration.getClass());
        log("Using Driver " + configuration.getProperty(Environment.DRIVER));
        log("Using Dialect " + configuration.getProperty(Environment.DIALECT));
        log("Using URL " + configuration.getProperty(Environment.URL));
        log("Using User ID " + configuration.getProperty(Environment.USER));
        log("Showing SQL " + configuration.getProperty(Environment.SHOW_SQL));

        final SessionFactory sessionFactory = configuration.buildSessionFactory();
        final Session session = sessionFactory.openSession();
        try
        {
            // initialize the model and make sure to popuplate seed data if it's necessary
            final Transaction tx = session.beginTransaction();
            try
            {
                final ModelInitializer mi = new ModelInitializer(session, SeedDataPopulationType.AUTO, configuration);
                mi.initialize();
                tx.commit();
                log("Committed model initialization (seed data).");
            }
            catch(Exception e)
            {
                tx.rollback();
                log("Rolled back model initialization (seed data).");
                throw new BuildException(e);
            }

            dataGeneratorSources = new DataGeneratorSources();
            populateData(session);
        }
        catch(final Exception e)
        {
            throw new BuildException(e);
        }
        finally
        {
            session.close();
        }
    }

    protected void populateData(final Session session)
    {
        for(int i = 1; i <= generatePrimaryOrgsCount; i++)
            populateOrg(session, i);
    }

    protected void populateOrg(final Session session, final int number)
    {
        final Transaction tx = session.beginTransaction();
        try
        {
            final Organization org = new Organization();
            org.setOrganizationName(MessageFormat.format(orgNameTemplate, number));
            session.save(org);

            for(int i = 1; i <= generatePatientsPerOrgCount; i++)
                populatePatient(session, org, i);

            tx.commit();
        }
        catch(final Exception e)
        {
            tx.rollback();
            throw new BuildException(e);
        }
    }

    protected void populatePatient(final Session session, final Organization org, final int number)
    {
        final PersonDataGenerator personDataGenerator = new PersonDataGenerator(dataGeneratorSources);
        final USAddressDataGenerator usAddressDataGenerator = new USAddressDataGenerator(dataGeneratorSources);

        final Gender gender = personDataGenerator.getRandomGender();
        final String address1 = usAddressDataGenerator.getRandomStreetAddress(1000, 9999);
        // TODO: add a "address2" line generator for things like "Suite 400" or "Apartment 76"

        final City city = usAddressDataGenerator.getRandomCity();
        final String phone = usAddressDataGenerator.getRandomPhoneNumber(city);

        final Person patient = new Person();
        if(gender == Gender.MALE)
            patient.addGender(GenderType.Cache.MALE.getEntity());
        else
            patient.addGender(GenderType.Cache.FEMALE.getEntity());
        patient.setFirstName(personDataGenerator.getRandomFirstName(gender));
        patient.setLastName(personDataGenerator.getRandomSurname());
        // TODO: add middle name generation for sample of population
        // TODO: add suffix for small sample of population
        patient.setSsn(personDataGenerator.getRandomSocialSecurityNumber());

        // if the patient is an odd patient make it an older patient and if even make it "general population"
        if(number % 2 == 0)
            patient.setBirthDate(RandomUtils.generateRandomDateBetweenYearsAgo(18, 60));
        else
            patient.setBirthDate(RandomUtils.generateRandomDateBetweenYearsAgo(45, 90));

        final Set<Ethnicity> ethnicities = new HashSet<Ethnicity>();
        final Ethnicity ethnicity = new Ethnicity();
        final Cache[] ethnicityTypes = Cache.values();
        ethnicity.setPerson(patient);
        ethnicity.setType(ethnicityTypes[RandomUtils.generateRandomNumberBetween(0, ethnicityTypes.length)].getEntity());
        ethnicities.add(ethnicity);
        patient.setEthnicities(ethnicities);

        // TODO: replace with a real drivers license number generator for various states
        patient.setDriversLicenseNumber(personDataGenerator.getRandomSocialSecurityNumber());

        session.save(patient);
    }

    public void setHibernateConfigClass(final String cls) throws ClassNotFoundException
    {
        hibernateConfigClass = Class.forName(cls);
    }

    public void setDialect(final String dialect)
    {
        this.dialect = dialect;
    }

    public void setHibernateConfigFile(final String hibernateConfigFile)
    {
        this.hibernateConfigFile = hibernateConfigFile;
    }

    public void setPassword(final String password)
    {
        this.password = password;
    }

    public void setUrl(final String url)
    {
        this.url = url;
    }

    public void setDriver(final String driver)
    {
        this.driver = driver;
    }

    public void setUserid(final String userid)
    {
        this.userid = userid;
    }

    public void setShowSql(final boolean showSql)
    {
        this.showSql = showSql;
    }
}
