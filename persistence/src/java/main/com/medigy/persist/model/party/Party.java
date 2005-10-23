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

/**
 * Main entity class to represent a party such as a person or an organization. This class is useful for relating
 * other entities that might have relationships to either a person or an organization.
 *
 * @see com.medigy.persist.model.person.Person
 * @see com.medigy.persist.model.org.Organization
 */
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
    private Set<PartyFacility> partyFacilities = new HashSet<PartyFacility>();
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

    /**
     * Gets the name for the party
     * @return party name
     */
    @Column(length = 100, nullable = false, name = "party_name")
    public String getPartyName()
    {
        return this.partyName;
    }

    public void setPartyName(final String partyName)
    {
        this.partyName = partyName;
    }

    /**
     * Gets the party type
     * @return party type
     */
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

    /**
     * Checks to see if this party is actually a person
     * @return True if the party is a person
     */
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

    /**
     * Gets all the party contact mechanisms for this party such as phone numbers, emails, and addresses.
     * @return list of contact mechanisms
     */
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

    /**
     * Add a new contact mechanism for the party
     * @param cm    the contact mechanism
     * @param type  purpose of the contact mechanism
     */
    @Transient
    public void addPartyContactMechanism(final ContactMechanism cm, final ContactMechanismPurposeType type)
    {
        final PartyContactMechanism pcm = new PartyContactMechanism();
        pcm.setContactMechanism(cm);
        pcm.setParty(this);
        pcm.addPurpose(type);
        partyContactMechanisms.add(pcm);
    }

    /**
     * Gets the facility relationships for this party
     * @return party and facility relationships
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "party")
    public Set<PartyFacility> getPartyFacilities()
    {
        return partyFacilities;
    }

    public void setPartyFacilities(final Set<PartyFacility> partyFacilities)
    {
        this.partyFacilities = partyFacilities;
    }

    /**
     * Gets all the party identifier types defined specifically for this party
     * @return custom party identifier type
     */
    @OneToMany(cascade =  CascadeType.ALL, mappedBy = "party")
    public Set<PartyIdentifierType> getPartyIdentifierTypes()
    {
        return partyIdentifierTypes;
    }

    public void setPartyIdentifierTypes(final Set<PartyIdentifierType> partyIdentifierTypes)
    {
        this.partyIdentifierTypes = partyIdentifierTypes;
    }

    /**
     * Gets all the facility types specifically defined for this party
     * @return  custom facility types
     */
    @OneToMany(cascade =  CascadeType.ALL, mappedBy = "party")
    public Set<FacilityType> getFacilityTypes()
    {
        return facilityTypes;
    }

    public void setFacilityTypes(final Set<FacilityType> facilityTypes)
    {
        this.facilityTypes = facilityTypes;
    }

    /**
     * Gets all the custom communication event purpose types defined for this party
     * @return custom communication event purpose types
     */
    @OneToMany(cascade =  CascadeType.ALL, mappedBy = "party")
    public Set<CommunicationEventPurposeType> getCommunicationEventPurposeTypes()
    {
        return communicationEventPurposeTypes;
    }

    public void setCommunicationEventPurposeTypes(Set<CommunicationEventPurposeType> communicationEventPurposeTypes)
    {
        this.communicationEventPurposeTypes = communicationEventPurposeTypes;
    }

    /**
     * Gets all the custom communication event role types defined for this party
     * @return
     */
    @OneToMany(cascade =  CascadeType.ALL, mappedBy = "party")
    public Set<CommunicationEventRoleType> getCommunicationEventRoleTypes()
    {
        return communicationEventRoleTypes;
    }

    public void setCommunicationEventRoleTypes(final Set<CommunicationEventRoleType> communicationEventRoleTypes)
    {
        this.communicationEventRoleTypes = communicationEventRoleTypes;
    }

    /**
     *
     * @return
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "party")
    public Set<CommunicationEventRole> getCommunicationEventRoles()
    {
        return communicationEventRoles;
    }

    public void setCommunicationEventRoles(final Set<CommunicationEventRole> communicationEventRoles)
    {
        this.communicationEventRoles = communicationEventRoles;
    }

    /**
     * Gets all the billing account relationships that the party is ivolved with
     * @return
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "party")
    public Set<BillingAccountRole> getBillingAccountRoles()
    {
        return billingAccountRoles;
    }

    public void setBillingAccountRoles(final Set<BillingAccountRole> billingAccountRoles)
    {
        this.billingAccountRoles = billingAccountRoles;
    }

    /**
     * Gets all the invoice relationships that the party is involved with
     * @return
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "party")
    public Set<InvoiceRole> getInvoiceRoles()
    {
        return invoiceRoles;
    }

    public void setInvoiceRoles(final Set<InvoiceRole> invoiceRoles)
    {
        this.invoiceRoles = invoiceRoles;
    }

    // TODO: Need to see if this needs to b a PersonQualification
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
