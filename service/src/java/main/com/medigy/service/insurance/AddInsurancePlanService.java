/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.insurance;

import com.medigy.service.Service;
import com.medigy.service.dto.insurance.NewInsurancePlanValues;
import com.medigy.service.dto.insurance.AddInsurancePlanParameters;

public interface AddInsurancePlanService extends Service
{
    public NewInsurancePlanValues add(AddInsurancePlanParameters parameters);
}
