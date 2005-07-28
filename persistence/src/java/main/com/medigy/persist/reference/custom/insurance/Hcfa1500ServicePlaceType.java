/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.reference.custom.insurance;

import com.medigy.persist.reference.custom.CachedCustomReferenceEntity;
import com.medigy.persist.reference.custom.CustomReferenceEntity;
import com.medigy.persist.reference.custom.party.FacilityType;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE, discriminatorValue = "HCFA 1500 Service Place")
public class Hcfa1500ServicePlaceType extends FacilityType
{   
    public enum Cache implements CachedCustomReferenceEntity
    {
        OFFICE("OFFICE", "Office"),
        HOME("HOME", "Home"),
        INPATIENT_HOSPITAL("IN_HOSP", "Inpatient Hospital"),
        OUTPATIENT_HOSPITAL("OUT_HOSP", "Outpatient Hospital"),
        EMERGENCY_HOSPITAL("EM_HOSP", "Emergency Room Hospital"),
        AMBULATORY_SURGICAL_CENTER("AM_SURG_CTR", "Ambulatory Surgical Center"),
        BIRTHING_CENTER("BIRTH_CTR", "Birthing Center"),
        MILITARY_TREATMENT_FACILITY("MIL_FAC", "Military Treatment Facility"),
        SKILLED_NURSING_FACILITY("SNURS_FAC", "Skilled Nursing Facility"),
        NURSING_FACILITY("NURS_FAC", "Nursing Facility"),
        CUSTODIAL_CARE_FACILITY("CUST_CARE", "Custodial Care Facility"),
        HOSPICE("HOSPICE", "Hospice"),
        AMBULANCE_LAND("AMB_LAND", "Ambulance - Land"),
        AMBULANCE_AIR_WATER("AMB_AIRWATER", "Ambulance - Air or Water"),
        INPATIENT_PSYCHIATRIC_FACILITY("IN_PSYC_FAC", "Inpatient Psychiatric Facility"),
        PSYCHIATRIC_FACILITY_PARTIAL_HOSPITALIZATION("PSYC_FAC", "Psychiatric Facility Partial Hospitalization"),
        COMMUNITY_MENTAL_HEALTH_CENTER("MENT_HEALTH", "Community Mental Health Center"),
        INTERMEDIATE_CARE_FACILITY_MENTALLY_RETARDED("INT_MENTAL", "Intermediate Care Facility/Mentally Retarded"),
        RESIDENTIAL_SUBSTANCE_ABUSE_TREATEMENT_FACILITY("SUB_ABUSE", "Residential Substance Abuse Treatement Facility"),
        PSYCHIATRIC_RESIDENTIAL_TREATMENT_CENTER("PSYC_RES", "Psychiatric Residential Treatment Center"),
        COMPREHENSIVE_INPATIENT_REHABILITATION_FACILITY("IN_REHAB", "Comprehensive Inpatient Rehabilitation Facility"),
        COMPREHENSIVE_OUTPATIENT_REHABILITATION_FACILITY("OUT_REHAB", "Comprehensive Outpatient Rehabilitation Facility"),
        END_STAGE_RENAL_DISEASE_TREATMENT_FACILITY("RENAL_TREAT", "End Stage Renal Disease Treatment Facility"),
        STATE_LOCAL_PUBLIC_HEALTH_CLINIC("PUB_HEALTH", "State or Local Public Health Clinic"),
        RURAL_HEALTH_CLINIC("RU_HEALTH", "Rural Health Clinic"),
        INDEPENDENT_LAB("IND_LAB", "Independent Laboratory"),
        OTHER("OTHER_FAC", "Other Unlisted Facility"),
        ;

        private String code;
        private String label;
        private Hcfa1500ServicePlaceType entity;

        Cache(final String code, final String label)
        {
            this.code = code;
            this.label = label;
        }

        public String getCode()
        {
            return code;
        }

        public Hcfa1500ServicePlaceType getEntity()
        {
            return entity;
        }

        public void setEntity(final CustomReferenceEntity entity)
        {
            this.entity = (Hcfa1500ServicePlaceType) entity;
        }

        public String getLabel()
        {
            return label;
        }
    }
}
