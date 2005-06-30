package com.medigy.wicket.form;

public @interface SelectFieldStyle
{
    public enum Style { RADIO, COMBO, LIST, MULTICHECK, MULTILIST, MULTIDUAL, POPUP }

    Style style();
}
