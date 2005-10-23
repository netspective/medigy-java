package com.medigy.persist.model.party;

import com.medigy.persist.model.common.AbstractDateDurationEntity;
import com.medigy.persist.reference.custom.party.PartyClassificationType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * A relationship class between a party and its' classification. A classification is used to further categorize the
 * party (e.g. Hospital or Clinic to further categorize an Organization)
 */
@Entity
public class PartyClassification extends AbstractDateDurationEntity
{
    public static final String PK_COLUMN_NAME = "party_classification_id";

    private Long partyClassificationId;
    private Party party;
    private PartyClassificationType type;

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getPartyClassificationId()
    {
        return partyClassificationId;
    }

    protected void setPartyClassificationId(final Long partyClassificationId)
    {
        this.partyClassificationId = partyClassificationId;
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

    @ManyToOne
    @JoinColumn(name = PartyClassificationType.PK_COLUMN_NAME, nullable = false)
    public PartyClassificationType getType()
    {
        return type;
    }

    public void setType(PartyClassificationType type)
    {
        this.type = type;
    }
}
