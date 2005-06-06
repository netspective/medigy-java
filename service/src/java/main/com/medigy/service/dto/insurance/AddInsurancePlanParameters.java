/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.dto.insurance;

import com.medigy.service.dto.ServiceParameters;
import com.medigy.service.dto.party.PostalAddressParameters;
import com.medigy.service.dto.party.PhoneParameters;

import java.io.Serializable;

public interface AddInsurancePlanParameters extends ServiceParameters
{
    public String getPlanName();
    public Serializable getInsuranceCarrierId();

    public String getFeeSchedule();
    public PostalAddressParameters getBillingAddress();
    public PhoneParameters getPhone();
    public PhoneParameters getFax();
    public String getBcbsPlanCode();

    // CHAMPUS/TRICARE (The Civilian Health and Medical Program of the Uniformed Services)
    public String getChampusBranch();
    public String getChampusStatus();
    public String getChampusGrade();

}
