/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.service.query;

import com.medigy.service.dto.ServiceParameters;
import com.medigy.service.query.exception.QueryDefinitionException;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class QueryDefinitionConditionValue
{
    private final Log log = LogFactory.getLog(QueryDefinitionConditionValue.class);

    private QueryDefnCondition condition;
    private Class serviceParameterClass;
    private String propertyName;
    private Method propertyReadMethod;

    private Object value;

    public QueryDefinitionConditionValue(final Object value)
    {
        this.value = value;
    }

    public QueryDefinitionConditionValue(final Class parameterClass, final String propertyName)
    {
        this.serviceParameterClass = parameterClass;
        this.propertyName = propertyName;
    }

    public QueryDefinitionConditionValue(final QueryDefnCondition condition, final Class parameterClass, final String propertyName)
    {
        this.condition = condition;
        this.serviceParameterClass = parameterClass;
        this.propertyName = propertyName;
    }

    public QueryDefnCondition getCondition()
    {
        return condition;
    }

    public String getPropertyName()
    {
        return propertyName;
    }

    private void initialize() throws QueryDefinitionException
    {
        try
        {
            final BeanInfo beanInfo = Introspector.getBeanInfo(serviceParameterClass);
            final PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor descriptor:  descriptors)
            {
                if (descriptor.getName().equals(propertyName))
                {
                    final Class propertyType = descriptor.getPropertyType();
                    this.propertyReadMethod = descriptor.getReadMethod();
                    break;
                }
            }
            // Failed to find the property in the service parameter

        }
        catch (Exception e)
        {
            log.error(e);
            throw new QueryDefinitionException(condition.getOwner(), "The Query Definition Condition" +
                    " value cannot be obtained from property '" + propertyName +
                    "' of the service parameter class: " + serviceParameterClass.getName());
        }
    }


    public Object getValue()
    {
        return this.value;
    }

    public Object getValue(final ServiceParameters params) throws QueryDefinitionException
    {

        if (!serviceParameterClass.isAssignableFrom(params.getClass()))
            throw new QueryDefinitionException(condition.getOwner(), "The service parameter object " +
                    " is not assignable to: " + serviceParameterClass.getClass());
        Object value = null;
        try
        {
            if (propertyReadMethod == null)
                initialize();
            value = propertyReadMethod.invoke(params);
        }
        catch (Exception e)
        {
            log.error(e);
            throw new QueryDefinitionException(condition.getOwner(), e);
        }
        return value;
    }
}
