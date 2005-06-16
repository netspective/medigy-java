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
 * @author Aye Thu
 */
package com.medigy.persist.model.party;

import com.medigy.persist.model.common.AbstractDateDurationEntity;
import com.medigy.persist.reference.custom.party.ContactMechanismPurposeType;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Party_Contact_Mech")
public class PartyContactMechanism extends AbstractDateDurationEntity
{
    private Long partyContactMechanismId;
    private String notes;
    private boolean nonSolicitation;
    private Party party;
    private ContactMechanism contactMechanism;

    private Set<PartyContactMechanismPurpose> purposes = new HashSet<PartyContactMechanismPurpose>();

    public PartyContactMechanism()
    {
    }

    @Id(generate = GeneratorType.AUTO)
    @Column(name = "party_contact_mech_id")
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
    @JoinColumn(name = "party_id", nullable = false)
    public Party getParty()
    {
        return party;
    }

    public void setParty(final Party party)
    {
        this.party = party;
    }

    @Column(name = "non_solicitation_ind")
    public boolean isNonSolicitation()
    {
        return nonSolicitation;
    }

    public void setNonSolicitation(boolean nonSolicitation)
    {
        this.nonSolicitation = nonSolicitation;
    }

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

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "party_contact_mech_id")        
    public Set<PartyContactMechanismPurpose> getPurposes()
    {
        return purposes;
    }

    public void setPurposes(final Set<PartyContactMechanismPurpose> purposes)
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
