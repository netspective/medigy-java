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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.medigy.persist.model.common.AbstractDateDurationEntity;
import com.medigy.persist.reference.custom.party.PartyRelationshipType;
import com.medigy.persist.reference.type.PriorityType;

@Entity
@Table(name = "Party_Relationship")
public class PartyRelationship extends AbstractDateDurationEntity
{
    public static final String PK_COLUMN_NAME = "party_rel_id";

    private Long partyRelationshipId;
    private String comment;

    private Party partyFrom;
    private Party partyTo;

    private PartyRole partyRoleFrom;
    private PartyRole partyRoleTo;
    private PartyRelationshipType type;
    private PriorityType priorityType;

    private Set<CommunicationEvent> communicationEvents = new HashSet<CommunicationEvent>();
    //private Set<Agreement> agreements = new HashSet<Agreement>();

    public PartyRelationship()
    {
    }

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PartyRelationship.PK_COLUMN_NAME)
    public Long getPartyRelationshipId()
    {
        return partyRelationshipId;
    }

    protected void setPartyRelationshipId(final Long partyRelationshipId)
    {
        this.partyRelationshipId = partyRelationshipId;
    }

    @ManyToOne
    @JoinColumn(name = "party_rel_type_id", nullable = false)
    public PartyRelationshipType getType()
    {
        return type;
    }

    public void setType(final PartyRelationshipType type)
    {
        this.type = type;
    }

    @ManyToOne
    @JoinColumn(name = "party_id_from", referencedColumnName = "party_id", nullable = false)
    public Party getPartyFrom()
    {
        return partyFrom;
    }

    public void setPartyFrom(final Party partyFrom)
    {
        this.partyFrom = partyFrom;
    }

    @ManyToOne
    @JoinColumn(name = "party_id_to", referencedColumnName = "party_id", nullable = false)
    public Party getPartyTo()
    {
        return partyTo;
    }

    public void setPartyTo(final Party partyTo)
    {
        this.partyTo = partyTo;
    }

    @ManyToOne
    @JoinColumn(name = "party_role_id_from", referencedColumnName = "party_role_id", nullable = false)
    public PartyRole getPartyRoleFrom()
    {
        return partyRoleFrom;
    }

    public void setPartyRoleFrom(final PartyRole partyRoleFrom)
    {
        this.partyRoleFrom = partyRoleFrom;
    }

    @ManyToOne
    @JoinColumn(name = "party_role_id_to", referencedColumnName = "party_role_id", nullable = false)
    public PartyRole getPartyRoleTo()
    {
        return partyRoleTo;
    }

    public void setPartyRoleTo(final PartyRole partyRoleTo)
    {
        this.partyRoleTo = partyRoleTo;
    }


    @Column(length = 256)
    public String getComment()
    {
        return comment;
    }

    protected void setComment(final String comment)
    {
        this.comment = comment;
    }

    @ManyToOne
    @JoinColumn(name = "priority_id")
    public PriorityType getPriority()
    {
        return priorityType;
    }

    protected void setPriority(final PriorityType priorityType)
    {
        this.priorityType = priorityType;
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "party_rel_id")
    public Set<CommunicationEvent> getCommunicationEvents()
    {
        return communicationEvents;
    }

    public void setCommunicationEvents(final Set<CommunicationEvent> communicationEvents)
    {
        this.communicationEvents = communicationEvents;
    }

    /*
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "party_relationship_id")
    public Set<Agreement> getAgreements()
    {
        return agreements;
    }

    public void setAgreements(final Set<Agreement> agreements)
    {
        this.agreements = agreements;
    }
    */
}
