package com.medigy.persist.model.party;

import com.medigy.persist.model.common.AbstractDateDurationEntity;
import com.medigy.persist.reference.type.ContactMechanismType;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * Class for communication events such as phone calls, face-to-face meetings.
 */
@Entity
@Table(name = "Comm_Event")
public class CommunicationEvent extends AbstractDateDurationEntity
{
    public static final String PK_COLUMN_NAME = "comm_event_id";

    private Long eventId;
    private String notes;
    private Set<CommunicationEventPurpose> eventPurposes = new HashSet<CommunicationEventPurpose>();
    private ContactMechanismType contactMechanismType;
    private Set<CommunicationEventRole> eventRoles = new HashSet<CommunicationEventRole>();
    private Set<CommunicationEventWork> eventWorks = new HashSet<CommunicationEventWork>();

    public CommunicationEvent()
    {
    }

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getEventId()
    {
        return this.eventId;
    }

    protected void setEventId(final Long id)
    {
        this.eventId = id;
    }

    /**
     * Gets the associated notes for this communication event
     * @return event notes
     */
    @Column(length = 1000)
    public String getNotes()
    {
        return notes;
    }

    protected void setNotes(final String notes)
    {
        this.notes = notes;
    }

    /**
     * Gets all the event purposes
     * @return purpose list
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "communicationEvent")
    public Set<CommunicationEventPurpose> getEventPurposes()
    {
        return eventPurposes;
    }

    public void setEventPurposes(final Set<CommunicationEventPurpose> eventPurposes)
    {
        this.eventPurposes = eventPurposes;
    }

    /**
     * Gets the contact mechanism associated with the communication event
     * @return contact mechanism type
     */
    @ManyToOne
    @JoinColumn(name = "contact_mech_type_id")
    public ContactMechanismType getContactMechanismType()
    {
        return contactMechanismType;
    }

    public void setContactMechanismType(final ContactMechanismType contactMechanismType)
    {
        this.contactMechanismType = contactMechanismType;
    }

    /**
     * Gets all the party relationships associated to this communication event
     * @return
     */
    @OneToMany(mappedBy = "communicationEvent", cascade = CascadeType.ALL)
    public Set<CommunicationEventRole> getEventRoles()
    {
        return eventRoles;
    }

    public void setEventRoles(final Set<CommunicationEventRole> eventRoles)
    {
        this.eventRoles = eventRoles;
    }

    /**
     * Gets all the work items resulting from the communication event
     * @return
     */
    @OneToMany(mappedBy = "communicationEvent", cascade = CascadeType.ALL)
    public Set<CommunicationEventWork> getEventWorks()
    {
        return eventWorks;
    }

    public void setEventWorks(final Set<CommunicationEventWork> eventWorks)
    {
        this.eventWorks = eventWorks;
    }
}
