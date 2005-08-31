/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.dto.person;

import com.medigy.service.dto.ServiceReturnValues;

import java.util.Date;
import java.util.List;

public interface PersonSearchReturnValues extends ServiceReturnValues
{
    public class Person
    {
        private String lastName;
        private String firstName;
        private String gender;
        private String ssn;
        private Long personId;
        private Date birthDate;

        public Date getBirthDate()
        {
            return birthDate;
        }

        public void setBirthDate(final Date birthDate)
        {
            this.birthDate = birthDate;
        }

        public String getFirstName()
        {
            return firstName;
        }

        public void setFirstName(final String firstName)
        {
            this.firstName = firstName;
        }

        public String getGender()
        {
            return gender;
        }

        public void setGender(final String gender)
        {
            this.gender = gender;
        }

        public String getLastName()
        {
            return lastName;
        }

        public void setLastName(final String lastName)
        {
            this.lastName = lastName;
        }

        public Long getPersonId()
        {
            return personId;
        }

        public void setPersonId(final Long personId)
        {
            this.personId = personId;
        }

        public String getSsn()
        {
            return ssn;
        }

        public void setSsn(final String ssn)
        {
            this.ssn = ssn;
        }
    }

    public List<Person> getSearchResults();
}
