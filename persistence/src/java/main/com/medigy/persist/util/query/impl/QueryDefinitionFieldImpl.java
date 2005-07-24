/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.util.query.impl;

import com.medigy.persist.util.query.QueryDefinition;
import com.medigy.persist.util.query.QueryDefinitionField;
import com.medigy.persist.util.query.QueryDefinitionJoin;
import com.medigy.persist.util.query.SqlComparison;
import com.medigy.persist.util.query.exception.QueryDefinitionException;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Method;
import java.lang.annotation.Annotation;
import java.beans.PropertyDescriptor;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;

import javax.persistence.OneToMany;
import javax.persistence.ManyToOne;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

public class QueryDefinitionFieldImpl implements QueryDefinitionField
{
    private String name;

    private String caption; // Localizable name
    private String entityPropertyName;

    private String selectClauseExpr;
    private String selectClauseExprAndLabel;
    private String whereClauseExpr;
    private String orderByClauseExpr;
    private String hqlJoinExpr;

    private QueryDefinitionJoin join;
    private QueryDefinition parentQueryDefinition;
    private boolean displayAllowed = true;
    private boolean transientProperty = false;
    private boolean associationProperty = false;
    private Class propertyType;
    private List<SqlComparison> validSqlComparisons = new ArrayList<SqlComparison>();

    public QueryDefinitionFieldImpl(final String name, final String propertyName,
                                    final QueryDefinitionJoin join,  final QueryDefinition queryDefn)
    {
        this.name = name;
        this.entityPropertyName = propertyName;
        this.join = join;
        this.parentQueryDefinition = queryDefn;
        checkPropertyAnnotations();
    }

    protected void checkPropertyAnnotations()
    {
        try
        {
            final Class entityClass = getJoin().getEntityClass();
            final PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(entityClass.newInstance(),
                    entityPropertyName);
            if (propertyDescriptor == null)
                throw new RuntimeException("Failed to lookup entity property in " + entityClass + ": " + entityPropertyName);   
            propertyType = propertyDescriptor.getPropertyType();
            final Method readMethod = propertyDescriptor.getReadMethod();

            if (readMethod.isAnnotationPresent(OneToMany.class) || readMethod.isAnnotationPresent(ManyToOne.class) ||
                readMethod.isAnnotationPresent(ManyToMany.class))
            {
                // be default all  the association fetches are LAZY so we need to make them EAGER
                associationProperty = true;
            }
            else if (readMethod.isAnnotationPresent(Transient.class))
            {
                transientProperty = true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public String getName()
    {
        return name;
    }

    public String getCaption()
    {
        return caption != null ? caption : name;
    }

    public void setCaption(final String caption)
    {
        this.caption = caption;
    }

    /**
     * Checks to see if the requested property is a transient property. If it is a transient property, it cannot be
     * added in the SELECT clause of the generated HQL but the JOIN expression still needs to be added.
     *
     * @return   True if the property denoted as a transient method
     */
    public boolean isTransientProperty()
    {
        return transientProperty;
    }

    /**
     * Gets the property of the entity. THIS CAN BE A NESTED STRING. (e.g. address.stree1)
     * @return the nested property name
     */
    public String getEntityPropertyName()
    {
        return entityPropertyName;
    }

    public Class getEntityPropertyType() throws QueryDefinitionException
    {
        return propertyType;
    }

    /**
     * Checks to see if the requested property is an association relationship if so
     * then an EXPLICIT FECTH must be issued so that the associated collection or entity is
     * also automatically retrieved.
     * @return
     * @throws QueryDefinitionException
     */
    public boolean isAssociationProperty() throws QueryDefinitionException
    {
        return associationProperty;
    }

    public String getColumnLabel()
    {
        return caption == null ? name : caption;
    }

    public String getColumnExpr() throws QueryDefinitionException
    {
        return displayAllowed ? (selectClauseExpr != null ? selectClauseExpr : getQualifiedColName()) : null;
    }

    public String getQualifiedColName()
    {
        String tableAlias = getTableAlias();
        if (selectClauseExpr != null)
            return selectClauseExpr;
        else
            return tableAlias != null ? (tableAlias + "." + getEntityPropertyName()) : getEntityPropertyName();
    }

    public String getTableName()
    {
        QueryDefinitionJoin join = getJoin();
        return join != null ? join.getTable() : null;
    }

    public String getTableAlias()
    {
        return join != null ? join.getName() : null;
    }

    public String getSelectClauseExpr()
    {
        return selectClauseExpr;
    }

    public void setSelectClauseExpr(final String selectClauseExpr)
    {
        this.selectClauseExpr = selectClauseExpr;
    }

    public String getSelectClauseExprAndLabel() throws QueryDefinitionException
    {
        return displayAllowed
               ? (selectClauseExprAndLabel != null
                  ? selectClauseExprAndLabel : (getColumnExpr()))
               : null;
    }

    public void setSelectClauseExprAndLabel(final String selectClauseExprAndLabel)
    {
        this.selectClauseExprAndLabel = selectClauseExprAndLabel;
    }

    public String getWhereClauseExpr() throws QueryDefinitionException
    {
        return whereClauseExpr != null ? whereClauseExpr : getQualifiedColName();
    }

    public void setWhereClauseExpr(final String whereClauseExpr)
    {
        this.whereClauseExpr = whereClauseExpr;
    }

    public String getOrderByClauseExpr()
    {
        return orderByClauseExpr != null ? orderByClauseExpr : getQualifiedColName();
    }

    public void setOrderByClauseExpr(final String orderByClauseExpr)
    {
        this.orderByClauseExpr = orderByClauseExpr;
    }

    public QueryDefinitionJoin getJoin()
    {
        return join;
    }

    /**
     * Gets all the SQL comparisons allowed for this field
     * @return list of SqlComparison
     */
    public List<SqlComparison> getValidSqlComparisons()
    {
        if (validSqlComparisons.size() == 0)
        {

        }
        return validSqlComparisons;
    }

    public QueryDefinition getQueryDefinition()
    {
        return this.parentQueryDefinition;
    }

    public String getHqlJoinExpr()
    {
        return hqlJoinExpr;
    }

    public void setHqlJoinExpr(final String hqlJoinExpr)
    {
        this.hqlJoinExpr = hqlJoinExpr;
    }

    public void setValidSqlComparisons(final List<SqlComparison> validSqlComparisons)
    {
        this.validSqlComparisons = validSqlComparisons;
    }


    public boolean isDisplayAllowed()
    {
        return displayAllowed;
    }

    public void setDisplayAllowed(final boolean displayAllowed)
    {
        this.displayAllowed = displayAllowed;
    }
}
