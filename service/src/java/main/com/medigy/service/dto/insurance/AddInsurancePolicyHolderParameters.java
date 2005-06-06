/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.dto.insurance;

import com.medigy.service.dto.ServiceParameters;
import com.medigy.service.dto.person.PersonParameters;

import java.io.Serializable;

public interface AddInsurancePolicyHolderParameters  extends ServiceParameters
{
    public Serializable getPatientId();

    public String getPolicyHolderRoleCode();

    public PersonParameters getPolicyHolder();

}
