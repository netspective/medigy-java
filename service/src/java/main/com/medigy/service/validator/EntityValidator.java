/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.validator;

import org.hibernate.mapping.Property;
import org.hibernate.validator.PropertyConstraint;
import org.hibernate.validator.Validator;

import com.medigy.persist.reference.custom.CustomReferenceEntity;
import com.medigy.service.util.ReferenceEntityFacade;
import com.medigy.service.util.FacadeManager;

/**
 * Validator for making sure that the entity being checked belongs to a reference entity type.
 */
public class EntityValidator implements Validator<ValidEntity>, PropertyConstraint
{
    private Class referenceEntityClass;

    public boolean isValid(final Object value)
    {
        if (value == null)
            return true;
        if (value instanceof String)
        {
            final String code = (String) value;
            final ReferenceEntityFacade  referenceEntityFacade = (ReferenceEntityFacade) FacadeManager.getInstance().getFacade(ReferenceEntityFacade.class);
            if (ValidEntity.class.isAssignableFrom(referenceEntityClass))
            {
                final com.medigy.persist.reference.ReferenceEntity entity =
                    referenceEntityFacade.getReferenceEntity(referenceEntityClass, code);
                if (entity != null)
                    return true;
            }
            else if (CustomReferenceEntity.class.isAssignableFrom(referenceEntityClass))
            {
                final CustomReferenceEntity entity =
                    referenceEntityFacade.getCustomReferenceEntity(referenceEntityClass, code);
                // TODO: handle custom reference entity using party ID
                if (entity != null)
                    return true;
            }

        }
        return false;
    }

    public void initialize(final ValidEntity validEntity)
    {
        this.referenceEntityClass = validEntity.entity();
    }

    public void apply(Property property)
    {

    }
}
