/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.util;

import com.medigy.service.AbstractSpringTestCase;
import com.medigy.persist.reference.custom.person.EthnicityType;
import com.medigy.persist.reference.custom.CustomReferenceEntity;
import com.medigy.persist.model.party.Party;

public class TestReferenceEntityFacade extends AbstractSpringTestCase
{
    private ReferenceEntityFacade referenceEntityFacade;

    public void setReferenceEntityFacade(final ReferenceEntityFacade referenceEntityFacade)
    {
        this.referenceEntityFacade = referenceEntityFacade;
    }

    public void testReferenceEntityFacade()
    {
        final CustomReferenceEntity customRef = referenceEntityFacade.getCustomReferenceEntity(EthnicityType.class, "C");
        assertEquals("C", customRef.getCode());
        assertEquals("Caucasian", customRef.getLabel());
        assertEquals(Party.Cache.SYS_GLOBAL_PARTY.getEntity().getPartyId(), customRef.getParty().getPartyId());

    }
}
