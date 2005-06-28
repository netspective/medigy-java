/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.reference.type;

import com.medigy.persist.reference.AbstractReferenceEntity;
import com.medigy.persist.reference.CachedReferenceEntity;
import com.medigy.persist.reference.ReferenceEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Person_Prefix_Type")
public class PersonNamePrefixType extends AbstractReferenceEntity
{
    public enum Cache implements CachedReferenceEntity
    {
        MR("MR", "Mr.", ""),
        MRS("MRS", "Mrs.", ""),
        MS("MS", "Ms.", ""),
        DR("DR", "Dr.", "");

        private final String code;
        private final String label;
        private final String description;
        private PersonNamePrefixType entity;

        private Cache(final String code, final String label, final String description)
        {
            this.code = code;
            this.label = label;
            this.description = description;
        }

        public String getCode()
        {
            return code;
        }

        public String getLabel()
        {
            return label;
        }

        public String getDescription()
        {
            return description;
        }

        public PersonNamePrefixType getEntity()
        {
            return entity;
        }

        public void setEntity(final ReferenceEntity entity)
        {
            this.entity = (PersonNamePrefixType) entity;
        }
    }
}
