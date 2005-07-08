/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.wicket.form;

import com.medigy.service.validator.ValidEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.validator.NotNull;
import wicket.markup.html.form.FormComponent;
import wicket.markup.html.form.validation.RequiredValidator;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ReflectedFormFieldDefn
{
    private final Log log = LogFactory.getLog(ReflectedFormFieldDefn.class);

    private final Class container;
    private final String name;
    private final Class dataType;
    private final Class customType;
    private final Class validEntity;
    private final Method method;
    private final boolean methodIsAccessor;
    private final Map<Class, Annotation> constraints = Collections.synchronizedMap(new HashMap<Class, Annotation>());
    private final com.medigy.wicket.form.FormFieldFactory.FieldCreator creator;

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

        if(method == null)
        {
            log.error("No accessor or mutator found for property '" + propertyName + "' in " + container);
            throw new RuntimeException("No accessor or mutator found for property '" + propertyName + "' in " + container);
        }

        this.method = method;
        this.methodIsAccessor = isAccessor;
        this.dataType = type;

        // go through each of the provided classes (the first one is considered the "primary" container
        // and collect all the annotations into a single set
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

            if(annotations != null)
            {
                for (final Annotation a : annotations)
                    constraints.put(a.annotationType(), a);
            }
        }

        this.validEntity = isAnnotationPresent(ValidEntity.class) ? ((ValidEntity) getAnnotation(ValidEntity.class)).entity() : null;
        this.customType = isAnnotationPresent(FieldCreator.class) ? ((FieldCreator) getAnnotation(FieldCreator.class)).creator() : null;
        this.creator = getFieldCreator(customType == null ? (validEntity == null ? dataType : ValidEntity.class) : customType);

        if(log.isInfoEnabled())
        {
            final StringBuilder containersText = new StringBuilder();
            for(int i = 0; i < containers.length; i++)
            {
                if(containersText.length() > 0)
                    containersText.append(", ");
                containersText.append(containers[i]);
            }

            log.info("Reflected form field '"+ name +"' of "+ container +" as " + dataType + " (ValidEntity " + validEntity + "). Containers: " + containersText);
        }
    }

    public FormComponent createField(final String componentId)
    {
        if(log.isInfoEnabled())
            log.info("Auto-creating form field for component '" + componentId + "' using" + creator);

        return creator.createField(componentId, this);
    }

    public void initializeField(final ReflectedFormFieldDefn reflectedFormFieldDefn, final FormComponent formComponent)
    {
        // at this point we have the originating class, the creator, and the newly created field so we can do
        // basic annotations processing first (all common annotations) and then let the field handle what it
        // needs to do
        if(this.isAnnotationPresent(NotNull.class))
            formComponent.add(RequiredValidator.getInstance());

        // TODO: find the BaseForms FormMode and check against annotation. Hide the formComponent's label.
        if(this.isAnnotationPresent(InvisibleWhen.class))
            formComponent.setVisible(false);

    }

    public com.medigy.wicket.form.FormFieldFactory.FieldCreator getFieldCreator(final Class dataTypeOrValidEntity)
    {
        final com.medigy.wicket.form.FormFieldFactory.FieldCreator result = FormFieldFactory.getInstance().getFieldCreator(dataTypeOrValidEntity);
        if(result == null)
        {
            log.error("Unable to find a field creator for " + dataTypeOrValidEntity);
            throw new RuntimeException("Unable to find a field creator for " + dataTypeOrValidEntity);
        }
        else
            return result;
    }

    public boolean isAnnotationPresent(final Class<?> annotation)
    {
        return constraints.containsKey(annotation);
    }

    public Annotation getAnnotation(final Class<?> annotation)
    {
        return constraints.get(annotation);
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

    public Map<Class, Annotation> getConstraints()
    {
        return constraints;
    }

    public com.medigy.wicket.form.FormFieldFactory.FieldCreator getCreator()
    {
        return creator;
    }
}