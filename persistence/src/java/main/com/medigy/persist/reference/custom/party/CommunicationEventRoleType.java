package com.medigy.persist.reference.custom.party;

import com.medigy.persist.reference.custom.AbstractCustomReferenceEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.Table;

 /**
 * For contact events that involve more than two parties, the COMMUNICATION EVENT ROLE
 * may define the parties and the roles they play with the event (facilitator, participant,
 * note taker, and so on)
 */
@Entity
@Table(name = "Comm_Event_Role_Type")
public class CommunicationEventRoleType extends AbstractCustomReferenceEntity
{

    public CommunicationEventRoleType()
    {
    }

    @Id(generate = GeneratorType.AUTO)
    @Column(name = "comm_event_role_type_id")
    public Long getCommunicationEventRoleTypeId()
    {
        return super.getSystemId();
    }

    protected void setCommunicationEventRoleTypeId(final Long id)
    {
        super.setSystemId(id);
    }
}
