package com.medigy.persist.model.party;

import com.medigy.persist.model.common.AbstractDateDurationEntity;
import com.medigy.persist.reference.custom.party.ContactMechanismPurposeType;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

/**
 * Relationship class for relating a party and a contach mechanism such as phone number, email.
 */
@Entity
@Table(name = "Party_Contact_Mech")
public class PartyContactMechanism extends AbstractDateDurationEntity
{
    public static final String PK_COLUMN_NAME = "party_contact_mech_id";

    private Long partyContactMechanismId;
    private String notes;
    private boolean nonSolicitation;
    private Party party;
    private ContactMechanism contactMechanism;

    private List<PartyContactMechanismPurpose> purposes = new ArrayList<PartyContactMechanismPurpose>();

    public PartyContactMechanism()
    {
    }

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getPartyContactMechanismId()
    {
        return partyContactMechanismId;
    }

    protected void setPartyContactMechanismId(final Long partyContactMechanismId)
    {
        this.partyContactMechanismId = partyContactMechanismId;
    }

    @Column(length = 1000)
    public String getNotes()
    {
        return notes;
    }

    public void setNotes(final String notes)
    {
        this.notes = notes;
    }

    @ManyToOne
    @JoinColumn(name = Party.PK_COLUMN_NAME, nullable = false)
    public Party getParty()
    {
        return party;
    }

    public void setParty(final Party party)
    {
        this.party = party;
    }

    /**
     * Gets the solicitation flag for the contact mechanism
     * @return True if the contact mechanism shouldn't be used for solicitation
     */
    @Column(name = "non_solicitation_ind")
    public boolean isNonSolicitation()
    {
        return nonSolicitation;
    }

    public void setNonSolicitation(boolean nonSolicitation)
    {
        this.nonSolicitation = nonSolicitation;
    }

    /**
     * Gets the associated contact mechanism
     * @return the contact mechanism
     */
    @ManyToOne
    @JoinColumn(name = ContactMechanism.PK_COLUMN_NAME, nullable = false)
    public ContactMechanism getContactMechanism()
    {
        return contactMechanism;
    }

    public void setContactMechanism(final ContactMechanism contactMechanism)
    {
        this.contactMechanism = contactMechanism;
    }

    /**
     * Gets the purpose(s) for the contact mechanism
     * @return list of purposes
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "party_contact_mech_id")
    @OrderBy("purposeId")
    public List<PartyContactMechanismPurpose> getPurposes()
    {
        return purposes;
    }

    public void setPurposes(final List<PartyContactMechanismPurpose> purposes)
    {
        this.purposes = purposes;
    }

    @Transient
    public void addPurpose(final ContactMechanismPurposeType type)
    {
        final PartyContactMechanismPurpose purpose = new PartyContactMechanismPurpose();
        purpose.setType(type);
        purpose.setPartyContactMechanism(this);
        purposes.add(purpose);
    }

    @Transient
    public void addPurpose(final ContactMechanismPurposeType type, final String description)
    {
        final PartyContactMechanismPurpose purpose = new PartyContactMechanismPurpose();
        purpose.setType(type);
        purpose.setPartyContactMechanism(this);
        purpose.setDescription(description);
        purposes.add(purpose);
    }

    @Transient
    public void addPurpose(final PartyContactMechanismPurpose purpose)
    {
        purpose.setPartyContactMechanism(this);
        purposes.add(purpose);
    }

    @Transient
    public PartyContactMechanismPurpose getPurpose(final ContactMechanismPurposeType type)
    {
        for (PartyContactMechanismPurpose pcm : purposes)
        {
            if (pcm.getType().equals(type))
                return pcm;
        }
        return null;
    }

    /**
     * Checks to see if the contact mechanism is for a particular purpose
     * (e.g. "Home Address" or "Mailing Address")
     * @param type
     * @return
     */
    @Transient
    public boolean hasPurpose(final ContactMechanismPurposeType type)
    {
        for (PartyContactMechanismPurpose pcm : purposes)
        {
            if (pcm.getType().equals(type))
                return true;
        }
        return false;
    }
}
