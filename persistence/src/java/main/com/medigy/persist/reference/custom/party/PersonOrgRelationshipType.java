/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.reference.custom.party;

import com.medigy.persist.reference.custom.AbstractCustomReferenceEntity;
import com.medigy.persist.reference.custom.CachedCustomReferenceEntity;
import com.medigy.persist.reference.custom.CustomReferenceEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratorType;
import javax.persistence.Column;

@Entity
@Table(name = "Person_Org_Rel_Type")
public class PersonOrgRelationshipType extends AbstractCustomReferenceEntity
{
    public static final String PK_COLUMN_NAME = "rel_type_id";

    public enum Cache implements CachedCustomReferenceEntity
    {
        FINANCIAL_RESPONSIBLE_PARTY("FIN_RESP", "Financial Responsible Party"), // org is responsible for the patient
        EMPLOYER("EMP", "Employer"), // Org is the employer of the person
        PATIENT_CLINIC("PC", "Patient-Clinic Relationship"),
        OTHER("OTH", "Other");

        private final String label;
        private final String code;
        private PersonOrgRelationshipType entity;

        Cache(final String code, final String label)
        {
            this.code = code;
            this.label = label;
        }

        public String getCode()
        {
            return code;
        }

        public PersonOrgRelationshipType getEntity()
        {
            return entity;
        }

        public void setEntity(final CustomReferenceEntity entity)
        {
            this.entity = (PersonOrgRelationshipType) entity;
        }

        public String getLabel()
        {
            return label;
        }
    }

    @Id(generate = GeneratorType.AUTO)
    @Column(name = PK_COLUMN_NAME)
    public Long getRelationshipTypeId()
    {
        return getSystemId();
    }

    public void setRelationshipTypeId(final Long id)
    {
        setSystemId(id);
    }

}
