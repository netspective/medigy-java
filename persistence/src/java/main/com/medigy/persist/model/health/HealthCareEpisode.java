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
package com.medigy.persist.model.health;

import com.medigy.persist.model.common.AbstractTopLevelEntity;
import com.medigy.persist.model.person.Incident;
import com.medigy.persist.model.person.Person;
import com.medigy.persist.reference.custom.health.EpisodeType;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class HealthCareEpisode extends AbstractTopLevelEntity
{
    private Long healthCareEpisodeId;
    private Incident incident;
    private String description;
    private Date date; // create date
    private EpisodeType type;
    private Person person;

    private Set<HealthCareDelivery> healthCareDeliveries = new HashSet<HealthCareDelivery>();
    private Set<Diagnosis> diagnosises = new HashSet<Diagnosis>();
    private Set<EpisodeOutcome> outcomes = new HashSet<EpisodeOutcome>();
    private Set<Symptom> symptoms = new HashSet<Symptom>();

    /**
     * INCIDENTs such as car accident, epidemic, or other event lead to HEALTH CARE EPISODEs, such as
     * injuries or illnesses.
     */
    public HealthCareEpisode()
    {
    }

    @Id(generate = GeneratorType.AUTO)
    @Column(name = "episode_id")
    public Long getHealthCareEpisodeId()
    {
        return healthCareEpisodeId;
    }

    protected void setHealthCareEpisodeId(final Long healthCareEpisodeId)
    {
        this.healthCareEpisodeId = healthCareEpisodeId;
    }

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "party_id")        
    public Person getPerson()
    {
        return person;
    }

    public void setPerson(final Person person)
    {
        this.person = person;
    }

    @ManyToOne
    @JoinColumn(name = "incident_id")
    public Incident getIncident()
    {
        return incident;
    }

    public void setIncident(final Incident incident)
    {
        this.incident = incident;
    }

    @Column(length = 1000)
    public String getDescription()
    {
        return description;
    }

    public void setDescription(final String description)
    {
        this.description = description;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(final Date date)
    {
        this.date = date;
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "health_care_episode_id")
    public Set<HealthCareDelivery> getHealthCareDeliveries()
    {
        return healthCareDeliveries;
    }

    public void setHealthCareDeliveries(final Set<HealthCareDelivery> healthCareDeliveries)
    {
        this.healthCareDeliveries = healthCareDeliveries;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "healthCareEpisode")
    public Set<Diagnosis> getDiagnosises()
    {
        return diagnosises;
    }

    public void setDiagnosises(final Set<Diagnosis> diagnosises)
    {
        this.diagnosises = diagnosises;
    }

    @OneToMany(mappedBy = "healthCareEpisode")
    public Set<EpisodeOutcome> getOutcomes()
    {
        return outcomes;
    }

    public void setOutcomes(final Set<EpisodeOutcome> outcomes)
    {
        this.outcomes = outcomes;
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "episode_id")
    public Set<Symptom> getSymptoms()
    {
        return symptoms;
    }

    public void setSymptoms(final Set<Symptom> symptoms)
    {
        this.symptoms = symptoms;
    }

    @ManyToOne
    @JoinColumn(name = "episode_type_id")
    public EpisodeType getType()
    {
        return type;
    }

    public void setType(final EpisodeType type)
    {
        this.type = type;
    }
}
