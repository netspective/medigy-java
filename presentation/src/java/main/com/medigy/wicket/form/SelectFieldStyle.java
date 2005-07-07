package com.medigy.wicket.form;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target(java.lang.annotation.ElementType.METHOD)
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface SelectFieldStyle
{
    // make sure these are called the exact same names as in the JavaScript side minus the SELECTSTYLE_ prefix
    public enum Style { RADIO, COMBO, LIST, MULTICHECK, MULTILIST, MULTIDUAL, POPUP }

    Style style();
}
