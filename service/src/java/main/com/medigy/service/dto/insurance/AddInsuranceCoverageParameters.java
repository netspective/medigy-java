/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.dto.insurance;

import com.medigy.service.dto.ServiceParameters;
import org.hibernate.validator.NotNull;

import java.io.Serializable;

public interface AddInsuranceCoverageParameters extends ServiceParameters
{
    @NotNull
    public Serializable getPatientId();

    @NotNull
    public InsuranceCoverageParameters getInsuranceCoverage();
}
