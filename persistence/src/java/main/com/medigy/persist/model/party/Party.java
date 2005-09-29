package com.medigy.persist.model.party;

import com.medigy.persist.model.common.AbstractTopLevelEntity;
import com.medigy.persist.model.invoice.BillingAccountRole;
import com.medigy.persist.model.invoice.InvoiceRole;
import com.medigy.persist.reference.custom.GeographicBoundaryType;
import com.medigy.persist.reference.custom.party.CommunicationEventPurposeType;
import com.medigy.persist.reference.custom.party.CommunicationEventRoleType;
import com.medigy.persist.reference.custom.party.ContactMechanismPurposeType;
import com.medigy.persist.reference.custom.party.FacilityType;
import com.medigy.persist.reference.custom.party.PartyClassificationType;
import com.medigy.persist.reference.custom.party.PartyIdentifierType;
import com.medigy.persist.reference.type.ContactMechanismType;
import com.medigy.persist.reference.type.party.PartyType;

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
    // @Id(generator="system-uuid")
    // @GenericGenerator(name="system-uuid", strategy = "uuid")
    public Long getPartyId()
    {
        return partyId;
    }

    protected void setPartyId(final Long partyId)
    {
        this.partyId = partyId;
    }

    @Column(length = 100, nullable = false, name = "party_name")
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
        List<PartyContactMechanism> phoneList = new ArrayList<PartyContactMechanism>();
        for (PartyContactMechanism mech : partyContactMechanisms)
        {
            System.out.println(mech.getContactMechanism().getType().getLabel());
            if (mech.getContactMechanism().getType().equals(ContactMechanismType.Cache.PHONE.getEntity()))
                phoneList.add(mech);
        }
        return phoneList;
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
