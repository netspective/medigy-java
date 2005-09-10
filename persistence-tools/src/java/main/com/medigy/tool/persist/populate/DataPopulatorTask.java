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

import com.medigy.persist.model.claim.Claim;
import com.medigy.persist.model.claim.ClaimItem;
import com.medigy.persist.model.health.HealthCareDelivery;
import com.medigy.persist.model.health.HealthCareEncounter;
import com.medigy.persist.model.insurance.InsurancePlan;
import com.medigy.persist.model.insurance.InsurancePolicy;
import com.medigy.persist.model.insurance.ResponsiblePartySelection;
import com.medigy.persist.model.invoice.Invoice;
import com.medigy.persist.model.invoice.InvoiceItem;
import com.medigy.persist.model.org.Organization;
import com.medigy.persist.model.org.OrganizationsRelationship;
import com.medigy.persist.model.party.ElectronicAddress;
import com.medigy.persist.model.party.Facility;
import com.medigy.persist.model.party.PhoneNumber;
import com.medigy.persist.model.person.Ethnicity;
import com.medigy.persist.model.person.PeopleRelationship;
import com.medigy.persist.model.person.Person;
import com.medigy.persist.model.person.PersonAndOrgRelationship;
import com.medigy.persist.reference.custom.claim.ClaimType;
import com.medigy.persist.reference.custom.health.DiagnosisType;
import com.medigy.persist.reference.custom.health.HealthCareEncounterStatusType;
import com.medigy.persist.reference.custom.insurance.InsurancePolicyType;
import com.medigy.persist.reference.custom.invoice.InvoiceStatusType;
import com.medigy.persist.reference.custom.invoice.InvoiceType;
import com.medigy.persist.reference.custom.party.ContactMechanismPurposeType;
import com.medigy.persist.reference.custom.party.FacilityType;
import com.medigy.persist.reference.custom.party.OrganizationRoleType;
import com.medigy.persist.reference.custom.party.OrganizationsRelationshipType;
import com.medigy.persist.reference.custom.party.PeopleRelationshipType;
import com.medigy.persist.reference.custom.party.PersonOrgRelationshipType;
import com.medigy.persist.reference.custom.person.EthnicityType.Cache;
import com.medigy.persist.reference.custom.person.PatientType;
import com.medigy.persist.reference.custom.person.PersonRoleType;
import com.medigy.persist.reference.type.CurrencyType;
import com.medigy.persist.reference.type.GenderType;
import com.medigy.persist.reference.type.clincial.CPT;
import com.medigy.persist.reference.type.clincial.Icd9;
import com.medigy.persist.util.ModelInitializer;
import com.medigy.persist.util.ModelInitializer.SeedDataPopulationType;
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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

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
    private String billingOrgNameTemplate = "Medigy Demo Billing Service {0}";
    private String clinicOrgNameTemplate = "Medigy Demo Clinic {0}";
    private String carrierOrgNameTemplate = "Medigy Demo Insurance Carrier {0}";

    private int generateCarrierOrgsCount = 5;
    private int generateClinicOrgsCount = 10;
    private int generateBillingOrgsCount = 5;
    private int generatePatientsPerOrgCount = 10;
    private int generateEmployeesPerClinicCount = 5;

    final List<Organization> carrierList = new ArrayList<Organization>();

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
                ModelInitializer.getInstance().initialize(session, SeedDataPopulationType.AUTO, configuration);
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
        // TODO: Need to create relationships between orgs
        // for every 10 clinics, create one billing service
        for (int i = 1; i <= generateCarrierOrgsCount; i++)
            carrierList.add(populateCarrier(session, i));

        for(int i = 1; i <= generateBillingOrgsCount; i++)
            populateBillingService(session, 1);

    }

    protected Organization populateCarrier(final Session session, final int number)
    {
        final Transaction tx = session.beginTransaction();
        try
        {
            final Organization org = new Organization();
            final String organizationName = MessageFormat.format(carrierOrgNameTemplate, number);
            org.setOrganizationName(organizationName);
            org.addRole(OrganizationRoleType.Cache.INSURANCE_PROVIDER.getEntity());

            // create Insurance plans for the carrier
            final InsurancePlan plan = new InsurancePlan();
            plan.setOrganization(org);
            plan.setName("Medigy Demo Insurance Plan");
            org.addInsurancePlan(plan);
            session.save(org);

            log("Created insurance carrier: " + organizationName);
            tx.commit();
            return org;
        }
        catch(final Exception e)
        {
            tx.rollback();
            throw new BuildException(e);
        }
    }

    protected void populateClinic(final Session session, final Organization billingServiceOrg, final int number)
    {
        final Transaction tx = session.beginTransaction();
        try
        {
            final Organization org = new Organization();
            final String organizationName = MessageFormat.format(clinicOrgNameTemplate, number);
            org.setOrganizationName(organizationName);
            org.addRole(OrganizationRoleType.Cache.CLINIC.getEntity());
            session.save(org);

            final Facility orgFacility = new Facility();
            orgFacility.setOrganization(org);
            orgFacility.setType(FacilityType.Cache.CLINIC.getEntity());
            session.save(orgFacility);

            final OrganizationsRelationship rel = new OrganizationsRelationship();
            rel.setPrimaryOrgRole(billingServiceOrg.getRole(OrganizationRoleType.Cache.BILLING_PROVIDER.getEntity()));
            rel.setSecondaryOrgRole(org.getRole(OrganizationRoleType.Cache.CLINIC.getEntity()));
            rel.setType(OrganizationsRelationshipType.Cache.CUSTOMER.getEntity());
            session.save(rel);

            // create physicians for the clinic
            List<Person> physicians = new ArrayList<Person>();
            for(int i = 1; i <= generatePatientsPerOrgCount; i++)
                physicians.add(populatePhysician(session, org, i));

            for(int i = 1; i <= generatePatientsPerOrgCount; i++)
            {
                final Person physician = physicians.get(RandomUtils.generateRandomNumberBetween(0, physicians.size()));
                final Person patient = populatePatient(session, org, i);
                populatePatientAppointment(session, patient, physician, org, orgFacility, i);
            }

            log("Created " + generatePatientsPerOrgCount + " patients in clinic: " + organizationName);
            tx.commit();
        }
        catch(final Exception e)
        {
            tx.rollback();
            throw new BuildException(e);
        }
    }

    protected void populateBillingService(final Session session, final int number)
    {
        final Transaction tx = session.beginTransaction();
        try
        {
            final Organization org = new Organization();
            final String organizationName = MessageFormat.format(billingOrgNameTemplate, number);
            org.setOrganizationName(organizationName);
            org.addRole(OrganizationRoleType.Cache.BILLING_PROVIDER.getEntity());
            session.save(org);

            for(int i = 1; i <= generateEmployeesPerClinicCount; i++)
                populateEmployee(session, org, i);

            // for each billing service, create client clinics/hospitals and their corresponding patients
            for (int i = 1; i < generateClinicOrgsCount; i++)
                populateClinic(session, org, i);

            log("Created " + generateEmployeesPerClinicCount + " employees in org " + organizationName);
            tx.commit();
        }
        catch(final Exception e)
        {
            tx.rollback();
            throw new BuildException(e);
        }
    }

    protected void populateEmployee(final Session session, final Organization org, final int number)
    {
        final PersonDataGenerator personDataGenerator = new PersonDataGenerator(dataGeneratorSources);
        final Gender gender = personDataGenerator.getRandomGender();
        final Person employee = new Person();
        if(gender == Gender.MALE)
            employee.addGender(GenderType.Cache.MALE.getEntity());
        else
            employee.addGender(GenderType.Cache.FEMALE.getEntity());
        employee.setFirstName(personDataGenerator.getRandomFirstName(gender));
        employee.setLastName(personDataGenerator.getRandomSurname());
        employee.addRole(PersonRoleType.Cache.EMPLOYEE.getEntity(), true);
        session.save(employee);

        // now create the link between the patient and the clinic through their roles
        PersonAndOrgRelationship rel = new PersonAndOrgRelationship();
        rel.setOrganizationRole(org.getRole(OrganizationRoleType.Cache.BILLING_PROVIDER.getEntity()));
        rel.setPersonRole(employee.getRole(PersonRoleType.Cache.EMPLOYEE.getEntity()));
        rel.setType(PersonOrgRelationshipType.Cache.EMPLOYER.getEntity());
        rel.setFromDate(new Date());
        session.save(rel);
    }

    protected Person populatePhysician(final Session session, final Organization org, final int number)
    {
        final PersonDataGenerator personDataGenerator = new PersonDataGenerator(dataGeneratorSources);
        final Gender gender = personDataGenerator.getRandomGender();
        final Person physician = new Person();
        if(gender == Gender.MALE)
            physician.addGender(GenderType.Cache.MALE.getEntity());
        else
            physician.addGender(GenderType.Cache.FEMALE.getEntity());
        physician.setFirstName(personDataGenerator.getRandomFirstName(gender));
        physician.setLastName(personDataGenerator.getRandomSurname());
        physician.addRole(PersonRoleType.Cache.INDIVIDUAL_HEALTH_CARE_PRACTITIONER.getEntity(), true);
        session.save(physician);

        // now create the link between the patient and the clinic through their roles
        PersonAndOrgRelationship rel = new PersonAndOrgRelationship();
        rel.setOrganizationRole(org.getRole(OrganizationRoleType.Cache.CLINIC.getEntity()));
        rel.setPersonRole(physician.getRole(PersonRoleType.Cache.INDIVIDUAL_HEALTH_CARE_PRACTITIONER.getEntity()));
        rel.setType(PersonOrgRelationshipType.Cache.EMPLOYER.getEntity());
        rel.setFromDate(new Date());
        session.save(rel);

        return physician;
    }

    protected void populatePatientInsurancePolicy(final Session session, final Organization carrier, final Person patient)
    {
        final InsurancePolicy policy = new InsurancePolicy();
        policy.setPolicyNumber("12345");
        policy.setInsuredPerson(patient);
        policy.setType(InsurancePolicyType.Cache.GROUP_INSURANCE_POLICY.getEntity());
        policy.setContractHolderPerson(patient);
        policy.setInsurancePlan(carrier.getInsurancePlans().get(0));
        policy.setFromDate(new Date());
        session.save(policy);
    }

    protected Person populatePatient(final Session session, final Organization org, final int number)
    {
        final PersonDataGenerator personDataGenerator = new PersonDataGenerator(dataGeneratorSources);
        final USAddressDataGenerator usAddressDataGenerator = new USAddressDataGenerator(dataGeneratorSources);

        Gender gender = personDataGenerator.getRandomGender();
        final String address1 = usAddressDataGenerator.getRandomStreetAddress(1000, 9999);
        // TODO: add a "address2" line generator for things like "Suite 400" or "Apartment 76"

        final City city = usAddressDataGenerator.getRandomCity();
        final String phoneNumber = usAddressDataGenerator.getRandomPhoneNumber(city);
        final String areaCode = usAddressDataGenerator.getRandomPhoneAreaCode(city);

        final Person patient = Person.createNewPatient();
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
        patient.addRole(PersonRoleType.Cache.PATIENT.getEntity());

        final ElectronicAddress email = new ElectronicAddress();
        email.setElectronicAddress(patient.getLastName() + patient.getFirstName() + "@medigy.com");
        session.save(email);
        patient.addPartyContactMechanism(email, ContactMechanismPurposeType.Cache.HOME_PHONE.getEntity());

        final PhoneNumber phone = new PhoneNumber();
        phone.setNumberValue(phoneNumber);
        phone.setAreaCode(areaCode);
        session.save(phone);
        patient.addPartyContactMechanism(phone, ContactMechanismPurposeType.Cache.HOME_PHONE.getEntity());
        session.save(patient);

        // now create the link between the patient and the clinic through their roles
        PersonAndOrgRelationship rel = new PersonAndOrgRelationship();
        rel.setOrganizationRole(org.getRole(OrganizationRoleType.Cache.CLINIC.getEntity()));
        rel.setPersonRole(patient.getRole(PersonRoleType.Cache.PATIENT.getEntity()));
        rel.setType(PersonOrgRelationshipType.Cache.PATIENT_CLINIC.getEntity());
        rel.setFromDate(new Date());
        session.save(rel);

        // create the responsible person
        gender = personDataGenerator.getRandomGender();
        Person respPerson = new Person();

        PeopleRelationship pplRel = new PeopleRelationship();
        if(gender == Gender.MALE)
        {
            respPerson.addGender(GenderType.Cache.MALE.getEntity());
            respPerson.addRole(PersonRoleType.Cache.FATHER.getEntity());
            pplRel.setSecondaryPersonRole(respPerson.getRole(PersonRoleType.Cache.FATHER.getEntity()));

        }
        else
        {
            respPerson.addGender(GenderType.Cache.FEMALE.getEntity());
            respPerson.addRole(PersonRoleType.Cache.MOTHER.getEntity());
            pplRel.setSecondaryPersonRole(respPerson.getRole(PersonRoleType.Cache.MOTHER.getEntity()));
        }
        respPerson.setFirstName(personDataGenerator.getRandomFirstName(gender));
        respPerson.setLastName(personDataGenerator.getRandomSurname());
        session.save(respPerson);

        pplRel.setPrimaryPersonRole(patient.getRole(PersonRoleType.Cache.PATIENT.getEntity()));
        pplRel.setType(PeopleRelationshipType.Cache.PARENT_CHILD.getEntity());
        session.save(pplRel);

        final ResponsiblePartySelection selection = new ResponsiblePartySelection();
        selection.setPeopleRelationship(pplRel);
        selection.setDefaultSelection(true);
        selection.setPatient(patient);
        session.save(selection);

        populatePatientInsurancePolicy(session, carrierList.get(0), patient);

        return patient;
    }

    /**
     * Populate invoice information
     * @param session
     * @param patient
     * @param encounter
     */
    protected void populateInvoicePerEncounter(final Session session, Person patient, final HealthCareEncounter encounter)
    {
        final Invoice invoice = new Invoice();
        invoice.setVisit(encounter);
        invoice.setType(InvoiceType.Cache.SERVICES.getEntity());
        invoice.setDescription("Medigy DEMO invoice description");
        invoice.setInvoiceDate(encounter.getCheckoutTime());
        invoice.addInvoiceStatus(InvoiceStatusType.Cache.CREATED.getEntity(), encounter.getCheckoutTime());

        final Calendar instance = Calendar.getInstance();
        instance.setTime(encounter.getCheckoutTime());
        instance.add(Calendar.DAY_OF_MONTH, 14);
        invoice.addInvoiceStatus(InvoiceStatusType.Cache.SUBMITTED.getEntity(), instance.getTime());

        if ((new Random()).nextBoolean())
        {
            instance.add(Calendar.DAY_OF_MONTH, 5);
            invoice.addInvoiceStatus(InvoiceStatusType.Cache.CLOSED.getEntity(), instance.getTime());
        }

        // each invoice item will correspond to each Health Care Delivery that is associated with the
        // visit.
        final List<HealthCareDelivery> healthCareDeliveries = encounter.getHealthCareDeliveries();
        int invItemCount = healthCareDeliveries.size();
        for (int i=0; i < invItemCount; i++)
        {
            final InvoiceItem item = new InvoiceItem();
            item.setInvoice(invoice);
            // TODO: This invoice item amount should be coming from the Fee Schedule associated with the doctor/facility
            int amount = RandomUtils.generateRandomNumberBetween(100, 1000);
            item.setAmount(new Float(amount));
            item.setQuantity(new Long(1));
            item.setCurrencyType(CurrencyType.Cache.US.getEntity());
            item.setCpt(healthCareDeliveries.get(i).getCpt());
            invoice.addInvoiceItem(item);
        }
        invoice.setTotalAdjustments(new Float(RandomUtils.generateRandomNumberBetween(1, 1000)));
        session.save(invoice);
    }

    protected void populateClaimPerInvoice(final Session session, final Person patient, final Invoice invoice)
    {
        // TODO: Need to decipher how claims should get created out of an Invoice.
        final Claim claim = new Claim();
        claim.setInvoice(invoice);
        claim.setType(ClaimType.Cache.INSURANCE.getEntity());

        int claimItemCount = invoice.getInvoiceItems().size();
        for (int i=0; i < claimItemCount; i++)
        {
            final ClaimItem item1 = new ClaimItem();
            item1.setClaimAmount(invoice.getInvoiceItem(i).getAmount());
            item1.setClaim(claim);
            item1.addDiagnosticCode(DiagnosisType.Cache.ICD9_CODE.getEntity(), null);
            item1.addDiagnosticCode(DiagnosisType.Cache.ICD9_CODE.getEntity(), null);
        }
    }

    protected void  populatePatientAppointment(final Session session, final Person patient, final Person randomPhysician,
                                               final Organization org, final Facility facility, final int number)
    {
        // TODO:  1 and 5 appts between each hour from 8 to 5 for each randomPhysician
        final PatientType.Cache[] patientTypeCaches = PatientType.Cache.values();
        final PatientType patientType = patientTypeCaches[RandomUtils.generateRandomNumberBetween(0, patientTypeCaches.length)].getEntity();

        if (patientType.getPatientTypeId().equals(PatientType.Cache.ESTABLISHED.getEntity().getPatientTypeId()))
        {
            // create old appointments for these patients
            Calendar cal = new GregorianCalendar();
            int month = RandomUtils.generateRandomNumberBetween(1, 6);
            int day = RandomUtils.generateRandomNumberBetween(1, 10);
            int hour = RandomUtils.generateRandomNumberBetween(8, 18);
            // schedule the appointment 1 to 6 months before
            cal.add(Calendar.MONTH, 0 - month);
            cal.add(Calendar.DAY_OF_MONTH, day);
            cal.set(Calendar.HOUR_OF_DAY, hour);
            final Date scheduledTimestamp = cal.getTime();
            HealthCareEncounter encounter = new HealthCareEncounter();
            encounter.setScheduledTime(scheduledTimestamp);
            encounter.setStartTime(scheduledTimestamp);
            cal.add(Calendar.HOUR_OF_DAY, 2);
            final Date checkoutTime = cal.getTime();
            encounter.setCheckoutTime(checkoutTime);
            encounter.setPatient(patient);
            encounter.setRequestedPhysician(randomPhysician);
            encounter.setPatientType(patientType);
            // change the day so that the appointment was scheduled 3 days before
            cal.add(Calendar.DAY_OF_MONTH, -3);
            encounter.addStatus(HealthCareEncounterStatusType.Cache.SCHEDULED.getEntity(), cal.getTime());
            encounter.addStatus(HealthCareEncounterStatusType.Cache.INPROGRESS.getEntity(), scheduledTimestamp);
            encounter.addStatus(HealthCareEncounterStatusType.Cache.COMPLETE.getEntity(), checkoutTime);
            encounter.setFacility(facility);

            final List icdList = session.createCriteria(Icd9.class).list();
            final List cptList = session.createCriteria(CPT.class).list();
            CPT cpt = null;
            Icd9 icd9 = null;
            if (cptList.size() > 0 && icdList.size() > 0)
            {
                cpt = (CPT) cptList.get(RandomUtils.generateRandomNumberBetween(0, cptList.size()));
                icd9 = (Icd9) icdList.get(RandomUtils.generateRandomNumberBetween(0, icdList.size()));
                int totalDeliveries  = RandomUtils.generateRandomNumberBetween(0, 5);
                for (int m = 0; m < totalDeliveries; m++)
                {
                    HealthCareDelivery delivery = new HealthCareDelivery();
                    delivery.setCpt(cpt);
                    delivery.setHealthCareEncounter(encounter);
                    delivery.addDiagnosis(DiagnosisType.Cache.ICD9_CODE.getEntity(), icd9);
                    encounter.addHealthCareDelivery(delivery);
                }
            }
            session.save(encounter);

            populateInvoicePerEncounter(session, patient, encounter);
        }

        Date apptTime = new Date();
        final int date = RandomUtils.generateRandomNumberBetween(0, 3);
        final Calendar instance = Calendar.getInstance();
        instance.setTime(apptTime);
        instance.add(Calendar.DAY_OF_MONTH, date);

        final int hr = RandomUtils.generateRandomNumberBetween(8, 17);
        instance.set(Calendar.HOUR_OF_DAY, hr);
        final int minute = RandomUtils.generateRandomMintutes(); // in 15 increments
        instance.set(Calendar.MINUTE, minute);

        HealthCareEncounter encounter = new HealthCareEncounter();
        encounter.setScheduledTime(instance.getTime());
        encounter.setPatient(patient);
        encounter.setRequestedPhysician(randomPhysician);
        encounter.setPatientType(patientType);
        encounter.addStatus(HealthCareEncounterStatusType.Cache.SCHEDULED.getEntity());
        encounter.setFacility(facility);
        session.save(encounter);
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
