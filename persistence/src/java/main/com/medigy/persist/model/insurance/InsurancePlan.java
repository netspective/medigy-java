package com.medigy.persist.model.insurance;

import com.medigy.persist.model.common.AbstractDateDurationEntity;
import com.medigy.persist.model.org.Organization;
import com.medigy.persist.model.party.ContactMechanism;
import com.medigy.persist.reference.custom.insurance.CoverageLevelType;
import com.medigy.persist.reference.custom.invoice.BillRemittanceType;
import org.hibernate.validator.NotNull;

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

/**
 * Class for insurance plan
 */
@Entity
@Table(name = "Ins_Plan")
public class InsurancePlan extends AbstractDateDurationEntity
{
    public static final String PK_COLUMN_NAME = "ins_plan_id";

    private Long insurancePlanId;
    private InsuranceProduct insuranceProduct;
    private Organization organization;
    private String name;
    private String remittancePayerId;
    private String remittancePayerName;
    private String medigapId;
    private BillRemittanceType billRemittanceType;

    private Set<InsurancePolicy> insurancePolicies = new HashSet<InsurancePolicy>();
    private Set<InsurancePlanContactMechanism> insurancePlanContactMechanisms = new HashSet<InsurancePlanContactMechanism>();
    private Set<InsurancePlanCoverageLevel> coverageLevelRelationships = new HashSet<InsurancePlanCoverageLevel>();

    private Set<InsurancePlanAttribute> insurancePlanAttributes = new HashSet<InsurancePlanAttribute>();

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getInsurancePlanId()
    {
        return insurancePlanId;
    }

    public void setInsurancePlanId(final Long insurancePlanId)
    {
        this.insurancePlanId = insurancePlanId;
    }

    /**
     * Gets all the coverage level relationships associated with the  insurance plan
     * @return
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "insurancePlan")
    public Set<InsurancePlanCoverageLevel> getCoverageLevelRelationships()
    {
        return coverageLevelRelationships;
    }

    public void setCoverageLevelRelationships(final Set<InsurancePlanCoverageLevel> coverageLevelRelationships)
    {
        this.coverageLevelRelationships = coverageLevelRelationships;
    }

    @Transient
    public void addCoverageLevelRelationship(final InsurancePlanCoverageLevel rel)
    {
        this.coverageLevelRelationships.add(rel);
    }

    @Transient
    public InsurancePlanCoverageLevel getCoverageLevelRelationship(final CoverageLevelType entity)
    {
        for (InsurancePlanCoverageLevel rel : coverageLevelRelationships)
        {
            if (rel.getCoverageLevel().getType().equals(entity))
                return rel;
        }
        return null;
    }

    /**
     * Gets the insurance product the plan is related to
     * @return
     */
    @ManyToOne
    @JoinColumn(name = InsuranceProduct.PK_COLUMN_NAME)
    public InsuranceProduct getInsuranceProduct()
    {
        return insuranceProduct;
    }

    public void setInsuranceProduct(final InsuranceProduct insuranceProduct)
    {
        this.insuranceProduct = insuranceProduct;
    }

    /**
     * Gets the bill remittance type.  (NEFS-SAMPLE-MEDIGY)
     * @return
     */
    @ManyToOne
    @JoinColumn(name = BillRemittanceType.PK_COLUMN_NAME)
    public BillRemittanceType getBillRemittanceType()
    {
        return billRemittanceType;
    }

    public void setBillRemittanceType(final BillRemittanceType billRemittanceType)
    {
        this.billRemittanceType = billRemittanceType;
    }

    /**
     * Gets the name of the insurance plan
     * @return
     */
    @Column(length = 100, nullable = false)
    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    /**
     * Gets the remittance payer name. The name given to the primary remittance id record (NEFS-SAMPLE-MEDIGY)
     * @return
     */
    @Column(length = 256, name = "remit_payer_name")
    public String getRemittancePayerName()
    {
        return remittancePayerName;
    }

    public void setRemittancePayerName(final String remittancePayerName)
    {
        this.remittancePayerName = remittancePayerName;
    }

    /**
     * Gets the medigap ID for the insurance plan
     * @return
     */
    @Column(length = 64, name = "medigap_id")
    public String getMedigapId()
    {
        return medigapId;
    }

    public void setMedigapId(final String medigapId)
    {
        this.medigapId = medigapId;
    }

    /**
     * Gets the remittance payer name. The id (if required) for this insurance plan/group for electronic remittance (NEFS-SAMPLE-MEDIGY)
     * @return
     */
    @Column(name = "remit_payer_id")
    public String getRemittancePayerId()
    {
        return remittancePayerId;
    }

    public void setRemittancePayerId(final String remittancePayerId)
    {
        this.remittancePayerId = remittancePayerId;
    }

    /**
     * Gets the owner organization
     * @return
     */
    @ManyToOne
    @JoinColumn(name = Organization.PK_COLUMN_NAME, nullable = false)
    @NotNull
    public Organization getOrganization()
    {
        return organization;
    }

    public void setOrganization(final Organization organization)
    {
        this.organization = organization;
    }

    /**
     * Gets the insurance policies that belong to the plan
     * @return
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "insurancePlan")
    public Set<InsurancePolicy> getInsurancePolicies()
    {
        return insurancePolicies;
    }

    public void setInsurancePolicies(final Set<InsurancePolicy> insurancePolicies)
    {
        this.insurancePolicies = insurancePolicies;
    }

    /**
     * Gets the contact mechanism relationships
     * @return
     */
    @OneToMany(mappedBy = "insurancePlan")
    public Set<InsurancePlanContactMechanism> getInsurancePlanContactMechanisms()
    {
        return insurancePlanContactMechanisms;
    }

    public void setInsurancePlanContactMechanisms(final Set<InsurancePlanContactMechanism> insurancePlanContactMechanisms)
    {
        this.insurancePlanContactMechanisms = insurancePlanContactMechanisms;
    }

    @Transient
    public void addInsurancePolicy(final InsurancePolicy insPolicy)
    {
        addInsurancePolicy(insPolicy, true);
    }

    @Transient
    public void addInsurancePolicy(final InsurancePolicy insPolicy, final boolean copyCoverageLevels)
    {
        insPolicy.setInsurancePlan(this);
        insurancePolicies.add(insPolicy);

        for (InsurancePlanCoverageLevel levelRelationship : coverageLevelRelationships)
        {
            final CoverageLevel coverageLevel = levelRelationship.getCoverageLevel();
            final InsurancePolicyCoverageLevel policyCoverageLevel = new InsurancePolicyCoverageLevel();
            coverageLevel.addInsurancePolicyCoverageLevel(policyCoverageLevel);
            insPolicy.addCoverageLevelRelationship(policyCoverageLevel);
        }
    }

    @Transient
    public void addContactMechanismRelationship(final InsurancePlanContactMechanism cm)
    {
        cm.setInsurancePlan(this);
        insurancePlanContactMechanisms.add(cm);
    }

    @Transient
    public void addContactMechanismRelationship(final ContactMechanism cm)
    {
        final InsurancePlanContactMechanism ipcm = new InsurancePlanContactMechanism();
        ipcm.setContactMechanism(cm);
        ipcm.setInsurancePlan(this);
        insurancePlanContactMechanisms.add(ipcm);
    }

    /**
     * Gets all the various attributes associated with the insurance plan. 
     * @return
     */
    @OneToMany(mappedBy = "insurancePlan", cascade = CascadeType.ALL)
    public Set<InsurancePlanAttribute> getInsurancePlanAttributes()
    {
        return insurancePlanAttributes;
    }

    public void setInsurancePlanAttributes(final Set<InsurancePlanAttribute> insurancePlanAttributes)
    {
        this.insurancePlanAttributes = insurancePlanAttributes;
    }

    @Transient
    public void addInsurancePlanAttribute(final InsurancePlanAttribute attr)
    {
        attr.setInsurancePlan(this);
        insurancePlanAttributes.add(attr);
    }
}
