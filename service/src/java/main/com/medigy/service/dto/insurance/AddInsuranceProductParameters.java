/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.dto.insurance;

import com.medigy.service.dto.ServiceParameters;
import com.medigy.service.dto.party.PostalAddressParameters;
import com.medigy.service.dto.party.PhoneParameters;

import java.io.Serializable;

public interface AddInsuranceProductParameters extends ServiceParameters
{
    public Serializable getInsuranceCarrierId();
    public String getProductName();
    public String getProductTypeCode();
    public String getFeeScheduleId();
    public String getMedigapId();

    public PostalAddressParameters getBillingAddress();
    public PhoneParameters getPhone();
    public PhoneParameters getFax();

    public String getRemittanceTypeCode();
    public String getERemittancePayerId();
    public String getRemittancePayerName();
}
