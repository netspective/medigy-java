package com.medigy.persist.model.party;

import com.medigy.persist.model.common.AbstractTopLevelEntity;
import com.medigy.persist.reference.type.party.PartyFacilityRoleType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * A relationship class between a party and a facility. The facility is more of a physical location entity which is
 * related to a party.
 */
@Entity
public class PartyFacility extends AbstractTopLevelEntity
{
    public static final String PK_COLUMN_NAME = "party_fac_role_id";

    private Long partyFacilityRoleId;

    private Party party;
    private PartyFacilityRoleType type;
    private Facility facility;

    public PartyFacility()
    {

    }

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getPartyFacilityRoleId()
    {
        return partyFacilityRoleId;
    }

    public void setPartyFacilityRoleId(Long partyFacilityRoleId)
    {
        this.partyFacilityRoleId = partyFacilityRoleId;
    }

    @ManyToOne
    @JoinColumn(name = "party_id", nullable = false)
    public Party getParty()
    {
        return party;
    }

    public void setParty(final Party party)
    {
        this.party = party;
    }

    /**
     * Describes the relationship between the party and the facility
     * @return relationship type
     */
    @ManyToOne
    @JoinColumn(name = "party_fac_role_type_id")
    public PartyFacilityRoleType getType()
    {
        return type;
    }

    public void setType(final PartyFacilityRoleType type)
    {
        this.type = type;
    }


    @ManyToOne
    @JoinColumn(name = "facility_id", nullable = false)
    public Facility getFacility()
    {
        return facility;
    }

    public void setFacility(final Facility facility)
    {
        this.facility = facility;
    }
    
}
