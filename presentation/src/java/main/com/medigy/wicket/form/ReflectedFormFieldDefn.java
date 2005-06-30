package com.medigy.wicket.form;

import wicket.markup.html.form.FormComponent;
import wicket.markup.html.form.validation.RequiredValidator;

import java.lang.reflect.Method;
import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.HashSet;
import java.beans.PropertyDescriptor;
import java.beans.Introspector;
import java.beans.IntrospectionException;

import org.hibernate.validator.NotNull;

public class ReflectedFormFieldDefn
{
    private final Class container;
    private final String name;
    private final Class dataType;
    private final Method method;
    private final boolean methodIsAccessor;
    private final Set<Annotation> constraints = new HashSet<Annotation>();
    private final FormFieldFactory.FieldCreator creator;

    public ReflectedFormFieldDefn(final Class container, final String propertyName)
    {
        this.container = container;
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
        this.creator = getFieldCreator(getDataType());

        if(creator == null)
            throw new RuntimeException("Unable to find a field creator for data type " + this.dataType);

        for (final Annotation a : method.getAnnotations())
            constraints.add(a);
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

    public FormFieldFactory.FieldCreator getFieldCreator(final Class dataType)
    {
        return FormFieldFactory.getInstance().getFieldCreator(dataType);
    }

    public boolean isAnnotationPresent(final Class<?> annotation)
    {
        return constraints.contains(annotation);
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