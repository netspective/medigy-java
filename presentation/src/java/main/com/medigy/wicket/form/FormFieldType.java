package com.medigy.wicket.form;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target(java.lang.annotation.ElementType.METHOD)
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface FormFieldType
{
    Class type();
    String message() default "must belong to the custom form field type";
}


