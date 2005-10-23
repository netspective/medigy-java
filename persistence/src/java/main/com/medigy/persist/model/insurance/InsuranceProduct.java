package com.medigy.persist.model.insurance;

import com.medigy.persist.model.common.AbstractTopLevelEntity;
import com.medigy.persist.model.org.Organization;
import com.medigy.persist.reference.custom.insurance.InsuranceProductType;
import com.medigy.persist.reference.custom.invoice.BillRemittanceType;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.HashSet;
import java.util.Set;

/**
 * Class for Insurance Products, which are a broad classification of insurance plans offered by an insurance organization.
 */
@Entity
public class InsuranceProduct extends AbstractTopLevelEntity
{
    public static final String PK_COLUMN_NAME = "ins_product_id";

    private Long insuranceProductId;
    private String name;
    private InsuranceProductType type;
    private Organization organization;
    private BillRemittanceType remittanceType;
    private String remittanceTypeDescription;
    private String remittancePayerName;

    private Set<InsuranceProductCoverage> insuranceProductCoverages = new HashSet<InsuranceProductCoverage>();
    private Set<InsurancePlan> insurancePlans = new HashSet<InsurancePlan>();
    private Set<InsuranceProductCoverageLevel> coverageLevelRelationships = new HashSet<InsuranceProductCoverageLevel>();
    private Set<InsuranceProductContactMechanism> productContactMechanisms = new HashSet<InsuranceProductContactMechanism>();

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getInsuranceProductId()
    {
        return insuranceProductId;
    }

    public void setInsuranceProductId(final Long insuranceProductId)
    {
        this.insuranceProductId = insuranceProductId;
    }

    /**
     * Gets the product name
     * @return
     */
    @Column(length = 128)
    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    /**
     * Gets the owner organization
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
     * Gets the insurance product type
     * @return
     */
    @ManyToOne
    @JoinColumn(name = InsuranceProductType.PK_COLUMN_NAME)
    public InsuranceProductType getType()
    {
        return type;
    }

    public void setType(final InsuranceProductType type)
    {
        this.type = type;
    }

    /**
     * Gets the relationships for coverage levels defined at the product level
     * @return
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "insuranceProduct")
    public Set<InsuranceProductCoverageLevel> getCoverageLevelRelationships()
    {
        return coverageLevelRelationships;
    }

    public void setCoverageLevelRelationships(final Set<InsuranceProductCoverageLevel> coverageLevelRelationships)
    {
        this.coverageLevelRelationships = coverageLevelRelationships;
    }

    @Transient
    public void addCoverageLevelRelationships(final InsuranceProductCoverageLevel rel)
    {
        this.coverageLevelRelationships.add(rel);
    }

    /**
     * Gets all the insurance plans for this insurance product. The different Insurance Plans that are part of the same
     * Insurance Product can have differing specifications but will all conform to the broad guidelines laid down by
     * the Insurance Product.
     * @return   a set of insurance plans
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "insuranceProduct")
    public Set<InsurancePlan> getInsurancePlans()
    {
        return insurancePlans;
    }

    public void setInsurancePlans(final Set<InsurancePlan> insurancePlans)
    {
        this.insurancePlans = insurancePlans;
    }

    @Transient
    public void addInsurancePlan(final InsurancePlan policy)
    {
        getInsurancePlans().add(policy);
    }

    @Transient
    public InsurancePlan getInsurancePlan(final String planName)
    {
        for (InsurancePlan plan: insurancePlans)
        {
            if (plan.getName().equals(planName))
            return plan;
        }
        return null;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "insuranceProduct")
    public Set<InsuranceProductCoverage> getInsuranceProductCoverages()
    {
        return  insuranceProductCoverages;
    }

    public void setInsuranceProductCoverages(final Set<InsuranceProductCoverage> insuranceProductCoverages)
    {
        this.insuranceProductCoverages = insuranceProductCoverages;
    }

    @Transient
    public void addCoverageRelationship(final InsuranceProductCoverage rel)
    {
        this.insuranceProductCoverages.add(rel);
    }

    /**
     * Gets the product's contact mechanism relationships
     * @return
     */
    @OneToMany(mappedBy = "insuranceProduct", cascade = CascadeType.ALL)
    public Set<InsuranceProductContactMechanism> getProductContactMechanisms()
    {
        return productContactMechanisms;
    }

    public void setProductContactMechanisms(final Set<InsuranceProductContactMechanism> productContactMechanisms)
    {
        this.productContactMechanisms = productContactMechanisms;
    }

    @Transient
    public void addProductContactMechanism(final InsuranceProductContactMechanism rel)
    {
        rel.setInsuranceProduct(this);
        productContactMechanisms.add(rel);

    }

    @ManyToOne
    @JoinColumn(name = BillRemittanceType.PK_COLUMN_NAME)
    public BillRemittanceType getRemittanceType()
    {
        return remittanceType;
    }

    public void setRemittanceType(final BillRemittanceType remittanceType)
    {
        this.remittanceType = remittanceType;
    }

    /**
     * Gets the remittance type description. Used when the the getRemittanceType() returns OTHER.
     * @return
     */
    @Column(length = 128, name = "remittance_type_description")
    public String getRemittanceTypeDescription()
    {
        return remittanceTypeDescription;
    }

    public void setRemittanceTypeDescription(final String remittanceTypeDescription)
    {
        this.remittanceTypeDescription = remittanceTypeDescription;
    }

    @Column(length = 128, name = "remittance_payer_name")
    public String getRemittancePayerName()
    {
        return remittancePayerName;
    }

    public void setRemittancePayerName(final String remittancePayerName)
    {
        this.remittancePayerName = remittancePayerName;
    }
}
