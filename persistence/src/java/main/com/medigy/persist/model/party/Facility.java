package com.medigy.persist.model.party;

import com.medigy.persist.model.common.AbstractTopLevelEntity;
import com.medigy.persist.model.health.HealthCareEncounter;
import com.medigy.persist.model.insurance.FeeSchedule;
import com.medigy.persist.model.org.Organization;
import com.medigy.persist.reference.custom.party.FacilityType;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

/**
 * Class for representing a physical entity Facility.
 */
@Entity
public class Facility extends AbstractTopLevelEntity
{
    public static final String PK_COLUMN_NAME = "facility_id";

    private Long facilityId;
    private String description;
    private Float squareFootage;
    private Facility parentFacility;
    private FacilityType type;
    private Organization organization;

    // children childFacilities (e.g Rooms on a Floor, offices in a building)
    private Set<Facility> childFacilities = new HashSet<Facility>();
    private Set<PartyFacility> facilityRole = new HashSet<PartyFacility>();

    private Set<HealthCareEncounter> visits = new HashSet<HealthCareEncounter>();
    private Set<FeeSchedule> feeSchedules = new HashSet<FeeSchedule>();
    /**
     * Facilities are not children of any table and they are related to Parties only through the
     * {@link PartyFacility}. Parties can "own", "rent", or "lease" facilities.
     */
    public Facility()
    {
    }

    @Id(generate=GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getFacilityId()
    {
        return facilityId;
    }

    protected void setFacilityId(final Long facilityId)
    {
        this.facilityId = facilityId;
    }

    @Column(length = 100)
    public String getDescription()
    {
        return description;
    }

    public void setDescription(final String description)
    {
        this.description = description;
    }

    /**
     * Gets the square footage of the facility
     * @return
     */
    @Column(name = "square_footage")
    public Float getSquareFootage()
    {
        return squareFootage;
    }

    public void setSquareFootage(final Float squareFootage)
    {
        this.squareFootage = squareFootage;
    }

    /**
     * Gets the type of the facility
     * @return
     */
    @ManyToOne
    @JoinColumn(name = "facility_type_id")
    public FacilityType getType()
    {
        return type;
    }

    public void setType(final FacilityType type)
    {
        this.type = type;
    }

    /**
     * Gets child facilities
     * @return
     */
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "child_facility_id", referencedColumnName = PK_COLUMN_NAME)
    public Set<Facility> getChildFacilities()
    {
        return childFacilities;
    }

    public void setChildFacilities(final Set<Facility> childFacilities)
    {
        this.childFacilities = childFacilities;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "facility")
    public Set<PartyFacility> getFacilityRole()
    {
        return facilityRole;
    }

    public void setFacilityRole(final Set<PartyFacility> facilityRole)
    {
        this.facilityRole = facilityRole;
    }

    @ManyToOne
    @JoinColumn(name = "parent_facility_id", referencedColumnName = PK_COLUMN_NAME)
    public Facility getParentFacility()
    {
        return parentFacility;
    }

    public void setParentFacility(final Facility parentFacility)
    {
        this.parentFacility = parentFacility;
    }

    /**
     * Gets all the health care visits occurred at this facility
     * @return
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "facility")
    public Set<HealthCareEncounter> getVisits()
    {
        return visits;
    }

    public void setVisits(final Set<HealthCareEncounter> visits)
    {
        this.visits = visits;
    }

    /**
     * Gets the organization this facility is associated with
     * @return
     */
    @ManyToOne
    @JoinColumn(name = Organization.PK_COLUMN_NAME)
    public Organization getOrganization()
    {
        return organization;
    }

    public void setOrganization(final Organization organization)
    {
        this.organization = organization;
    }

    /**
     * Gets the fee schedules associated with this facility
     * @return
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "facility")
    public Set<FeeSchedule> getFeeSchedules()
    {
        return feeSchedules;
    }

    public void setFeeSchedules(final Set<FeeSchedule> feeSchedules)
    {
        this.feeSchedules = feeSchedules;
    }
}
