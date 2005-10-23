package com.medigy.persist.model.party;

import com.medigy.persist.model.common.AbstractTopLevelEntity;
import com.medigy.persist.reference.custom.party.CommunicationEventPurposeType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Class for recording the purpose of a communication event such as a phone conversation.
 */
@Entity
@Table(name = "Comm_Event_Purpose")        
public class CommunicationEventPurpose extends AbstractTopLevelEntity
{
    private Long purposeId;
    private String description;
    private CommunicationEvent communicationEvent;
    private CommunicationEventPurposeType type;

    public CommunicationEventPurpose()
    {
    }

    @Id(generate = GeneratorType.AUTO)
    @Column(name = "comm_event_purpose_id")
    public Long getPurposeId()
    {
        return purposeId;
    }

    public void setPurposeId(Long purposeId)
    {
        this.purposeId = purposeId;
    }

    /**
     * Gets the description text of the event
     * @return description of the event
     */
    @Column(length = 256)
    public String getDescription()
    {
        return description;
    }

    public void setDescription(final String description)
    {
        this.description = description;
    }

    /**
     * Gets the associated communication event
     * @return communication event
     */
    @ManyToOne
    @JoinColumn(name = "comm_event_id", nullable = false)
    public CommunicationEvent getCommunicationEvent()
    {
        return communicationEvent;
    }

    public void setCommunicationEvent(CommunicationEvent communicationEvent)
    {
        this.communicationEvent = communicationEvent;
    }

    /**
     * Gets the purpose type
     * @return
     */
    @ManyToOne
    @JoinColumn(name = "comm_event_purpose_type_id")
    public CommunicationEventPurposeType getType()
    {
        return type;
    }

    public void setType(final CommunicationEventPurposeType type)
    {
        this.type = type;
    }

}
