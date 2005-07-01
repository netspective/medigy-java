/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.wicket.form;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.validator.NotNull;

import com.medigy.service.validator.ValidEntity;
import com.medigy.wicket.form.FormFieldFactory.FieldCreator;
import wicket.markup.html.form.FormComponent;
import wicket.markup.html.form.validation.RequiredValidator;

public class ReflectedFormFieldDefn
{
    private final Class container;
    private final String name;
    private final Class dataType;
    private final Class validEntity;
    private final Method method;
    private final boolean methodIsAccessor;
    private final Set<Annotation> constraints = new HashSet<Annotation>();
    private final FormFieldFactory.FieldCreator creator;

    public ReflectedFormFieldDefn(final String propertyName, final Class ... containers)
    {
        this.container = containers[0];
        this.name = propertyName;

        Method method = null;
        boolean isAccessor = false;
        Class type = null;
        try
        {
            final PropertyDescriptor[] descs = Introspector.getBeanInfo(container).getPropertyDescriptors();
            for(final PropertyDescriptor desc : descs)
            {
                if(propertyName.equalsIgnoreCase(desc.getName()))
                {
                    method = desc.getReadMethod();
                    if(method == null)
                    {
                        method = desc.getWriteMethod();
                        type = method.getParameterTypes()[0];
                        isAccessor = false;
                    }
                    else
                    {
                        type = method.getReturnType();
                        isAccessor = true;
                    }
                    break;
                }
            }
        }
        catch(IntrospectionException e)
        {
            method = null;
        }

        this.method = method;
        this.methodIsAccessor = isAccessor;
        this.dataType = type;
        this.validEntity = isAnnotationPresent(ValidEntity.class) ? ((ValidEntity) getAnnotation(ValidEntity.class)).entity() : null;
        this.creator = getFieldCreator(validEntity == null ? dataType : validEntity);

        for(int i = 0; i < containers.length; i++)
        {
            Annotation[] annotations = null;
            if(i == 0)
                annotations = method.getAnnotations();
            else
            {
                final Class otherContainer = containers[i];
                final PropertyDescriptor[] descs;
                try
                {
                    descs = Introspector.getBeanInfo(otherContainer).getPropertyDescriptors();
                }
                catch (IntrospectionException e)
                {
                    throw new RuntimeException(e);
                }
                for(final PropertyDescriptor desc : descs)
                {
                    if(propertyName.equalsIgnoreCase(desc.getName()))
                    {
                        Method otherMethod = desc.getReadMethod();
                        if(otherMethod == null)
                            otherMethod = desc.getWriteMethod();
                        annotations = otherMethod.getAnnotations();
                        break;
                    }
                }
            }

            for (final Annotation a : annotations)
                constraints.add(a);
        }
    }

    public FormComponent createField()
    {
        return creator.createField(this);
    }

    public void initializeField(final ReflectedFormFieldDefn reflectedFormFieldDefn, final FormComponent formComponent)
    {
        // at this point we have the originating class, the creator, and the newly created field so we can do
        // basic annotations processing first (all common annotations) and then let the field handle what it
        // needs to do
        if(this.isAnnotationPresent(NotNull.class));
        formComponent.add(RequiredValidator.getInstance());

    }

    public FormFieldFactory.FieldCreator getFieldCreator(final Class dataTypeOrValidEntity)
    {
        final FieldCreator result = FormFieldFactory.getInstance().getFieldCreator(dataTypeOrValidEntity);
        if(result == null)
            throw new RuntimeException("Unable to find a field creator for " + dataTypeOrValidEntity);
        else
            return result;
    }

    public boolean isAnnotationPresent(final Class<?> annotation)
    {
        return constraints.contains(annotation);
    }

    public Annotation getAnnotation(final Class<?> annotation)
    {
        for(final Annotation a : constraints)
            if(a.getClass() == annotation)
                return a;

        return null;
    }

    public Class getContainer()
    {
        return container;
    }

    public Method getMethod()
    {
        return method;
    }

    public boolean isMethodIsAccessor()
    {
        return methodIsAccessor;
    }

    public String getName()
    {
        return name;
    }

    public Class getDataType()
    {
        return dataType;
    }

    public Set<Annotation> getConstraints()
    {
        return constraints;
    }

    public FormFieldFactory.FieldCreator getCreator()
    {
        return creator;
    }
}