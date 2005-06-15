/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.dto.party;

import org.hibernate.validator.NotNull;

public interface PostalAddressParameters
{
    @NotNull
    public String getStreet1();

    public String getStreet2();

    @NotNull
    public String getCity();

    @NotNull
    public String getState();

    public String getProvince();

    @NotNull
    public String getPostalCode();

    public String getCounty();

    @NotNull
    public String getCountry();

    public String getPurposeType();

    public String getPurposeDescription();
}
