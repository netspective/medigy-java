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
 * @author Shahid N. Shah
 */

package com.medigy.persist.model.party;

import com.medigy.persist.model.common.AbstractTopLevelEntity;
import com.medigy.persist.model.invoice.BillingAccountRole;
import com.medigy.persist.model.invoice.InvoiceRole;
import com.medigy.persist.reference.custom.GeographicBoundaryType;
import com.medigy.persist.reference.custom.party.CommunicationEventPurposeType;
import com.medigy.persist.reference.custom.party.CommunicationEventRoleType;
import com.medigy.persist.reference.custom.party.FacilityType;
import com.medigy.persist.reference.custom.party.PartyClassificationType;
import com.medigy.persist.reference.custom.party.PartyIdentifierType;
import com.medigy.persist.reference.custom.party.ContactMechanismPurposeType;
import com.medigy.persist.reference.type.party.PartyType;
import com.medigy.persist.reference.type.ContactMechanismType;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity()
@Inheritance(strategy=InheritanceType.JOINED)
@Table(name = "Party")
public class Party extends AbstractTopLevelEntity
{
    public static final String PK_COLUMN_NAME = "party_id";
    public static final String SYS_GLOBAL_PARTY_NAME = "SYS_GLOBAL_PARTY";

    public enum Cache
    {
        SYS_GLOBAL_PARTY();

        private Party entity;

        public Party getEntity()
        {
            if(entity == null)
                throw new RuntimeException(getClass() + " " + name() + " has not been initialized.");

            return entity;
        }

        public void setEntity(Party entity)
        {
            this.entity = entity;
        }
    }

    private Long partyId;
    protected String partyName;
    protected PartyType partyType;

    private Set<PartyClassification> partyClassifications = new HashSet<PartyClassification>();
    private Set<PartyQualification> partyQualifications = new HashSet<PartyQualification>();

    protected List<PartyContactMechanism> partyContactMechanisms = new ArrayList<PartyContactMechanism>();
    private Set<PartyFacilityRole> partyFacilityRoles = new HashSet<PartyFacilityRole>();
    private Set<CommunicationEventRole> communicationEventRoles = new HashSet<CommunicationEventRole>();
    private Set<BillingAccountRole> billingAccountRoles = new HashSet<BillingAccountRole>();
    private Set<InvoiceRole> invoiceRoles = new HashSet<InvoiceRole>();


    // All the custom reference entity types
    private Set<PartyIdentifierType> partyIdentifierTypes = new HashSet<PartyIdentifierType>();
    private Set<FacilityType> facilityTypes = new HashSet<FacilityType>();
    private Set<CommunicationEventPurposeType> communicationEventPurposeTypes = new HashSet<CommunicationEventPurposeType>();
    private Set<CommunicationEventRoleType> communicationEventRoleTypes = new HashSet<CommunicationEventRoleType>();
    private Set<GeographicBoundaryType> geographicBoundaryTypes = new HashSet<GeographicBoundaryType>();

    public Party()
    {
    }

    public Party(final String partyName)
    {
        this.partyName = partyName;
    }

    @Id(generate=GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getPartyId()
    {
        return partyId;
    }

    protected void setPartyId(final Long partyId)
    {
        this.partyId = partyId;
    }

    @Column(length = 100, nullable = false)
    public String getPartyName()
    {
        return this.partyName;
    }

    public void setPartyName(final String partyName)
    {
        this.partyName = partyName;
    }

    @ManyToOne
    @JoinColumn(name= "party_type_id", referencedColumnName = PartyType.PK_COLUMN_NAME)
    public PartyType getPartyType()
    {
        return partyType;
    }

    public void setPartyType(final PartyType partyType)
    {
        this.partyType = partyType;
    }

    @Transient
    public boolean isPerson()
    {
        return partyType.equals(PartyType.Cache.PERSON.getEntity());
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "party")
    public Set<PartyClassification> getPartyClassifications()
    {
        return partyClassifications;
    }

    public void setPartyClassifications(final Set<PartyClassification> partyClassifications)
    {
        this.partyClassifications = partyClassifications;
    }

    @Transient
    public void addPartyClassification(final PartyClassificationType type)
    {
        final PartyClassification classification = new PartyClassification();
        classification.setType(type);
        classification.setParty(this);
        getPartyClassifications().add(classification);
    }

    @Transient
    public PartyClassification getPartyClassification(final PartyClassificationType type)
    {
        for (PartyClassification pc : getPartyClassifications())
        {
            if (pc.getType().equals(type))
            return pc;
        }
        return null;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "party")
    public List<PartyContactMechanism> getPartyContactMechanisms()
    {
        return partyContactMechanisms;
    }

    protected void setPartyContactMechanisms(final List<PartyContactMechanism> partyContactMechanisms)
    {
        this.partyContactMechanisms = partyContactMechanisms;
    }

    @Transient
    public List<PartyContactMechanism> getAddresses()
    {
        List<PartyContactMechanism> addresses = new ArrayList<PartyContactMechanism>();
        for (PartyContactMechanism mech : partyContactMechanisms)
        {
            if (mech.getContactMechanism() instanceof  PostalAddress)
                addresses.add(mech);
        }
        return addresses;
    }

    @Transient
    public List<PartyContactMechanism> getPhoneNumbers()
    {
        List<PartyContactMechanism> addresses = new ArrayList<PartyContactMechanism>();
        for (PartyContactMechanism mech : partyContactMechanisms)
        {
            if (mech.getContactMechanism().getType().equals(ContactMechanismType.Cache.PHONE.getEntity()))
                addresses.add(mech);
        }
        return addresses;
    }

    @Transient
    public void addPartyContactMechanism(final ContactMechanism number, final ContactMechanismPurposeType type)
    {
        final PartyContactMechanism pcm = new PartyContactMechanism();
        pcm.setContactMechanism(number);
        pcm.setParty(this);
        pcm.addPurpose(type);
        partyContactMechanisms.add(pcm);
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "party")
    public Set<PartyFacilityRole> getPartyFacilityRoles()
    {
        return partyFacilityRoles;
    }

    public void setPartyFacilityRoles(final Set<PartyFacilityRole> partyFacilityRoles)
    {
        this.partyFacilityRoles = partyFacilityRoles;
    }

    @OneToMany(cascade =  CascadeType.ALL, mappedBy = "party")
    public Set<PartyIdentifierType> getPartyIdentifierTypes()
    {
        return partyIdentifierTypes;
    }

    public void setPartyIdentifierTypes(final Set<PartyIdentifierType> partyIdentifierTypes)
    {
        this.partyIdentifierTypes = partyIdentifierTypes;
    }

    @OneToMany(cascade =  CascadeType.ALL, mappedBy = "party")
    public Set<FacilityType> getFacilityTypes()
    {
        return facilityTypes;
    }

    public void setFacilityTypes(final Set<FacilityType> facilityTypes)
    {
        this.facilityTypes = facilityTypes;
    }

    @OneToMany(cascade =  CascadeType.ALL, mappedBy = "party")
    public Set<CommunicationEventPurposeType> getCommunicationEventPurposeTypes()
    {
        return communicationEventPurposeTypes;
    }

    public void setCommunicationEventPurposeTypes(Set<CommunicationEventPurposeType> communicationEventPurposeTypes)
    {
        this.communicationEventPurposeTypes = communicationEventPurposeTypes;
    }

    @OneToMany(cascade =  CascadeType.ALL, mappedBy = "party")
    public Set<CommunicationEventRoleType> getCommunicationEventRoleTypes()
    {
        return communicationEventRoleTypes;
    }

    public void setCommunicationEventRoleTypes(final Set<CommunicationEventRoleType> communicationEventRoleTypes)
    {
        this.communicationEventRoleTypes = communicationEventRoleTypes;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "party")
    public Set<CommunicationEventRole> getCommunicationEventRoles()
    {
        return communicationEventRoles;
    }

    public void setCommunicationEventRoles(final Set<CommunicationEventRole> communicationEventRoles)
    {
        this.communicationEventRoles = communicationEventRoles;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "party")
    public Set<BillingAccountRole> getBillingAccountRoles()
    {
        return billingAccountRoles;
    }

    public void setBillingAccountRoles(final Set<BillingAccountRole> billingAccountRoles)
    {
        this.billingAccountRoles = billingAccountRoles;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "party")
    public Set<InvoiceRole> getInvoiceRoles()
    {
        return invoiceRoles;
    }

    public void setInvoiceRoles(final Set<InvoiceRole> invoiceRoles)
    {
        this.invoiceRoles = invoiceRoles;
    }

    @OneToMany(mappedBy = "party")
    public Set<PartyQualification> getPartyQualifications()
    {
        return partyQualifications;
    }

    public void setPartyQualifications(final Set<PartyQualification> partyQualifications)
    {
        this.partyQualifications = partyQualifications;
    }

    @Transient
    public void addInvoiceRole(final InvoiceRole role)
    {
        invoiceRoles.add(role);
    }

    @OneToMany(mappedBy = "party")
    public Set<GeographicBoundaryType> getGeographicBoundaryTypes()
    {
        return geographicBoundaryTypes;
    }

    public void setGeographicBoundaryTypes(final Set<GeographicBoundaryType> geographicBoundaryTypes)
    {
        this.geographicBoundaryTypes = geographicBoundaryTypes;
    }
}
