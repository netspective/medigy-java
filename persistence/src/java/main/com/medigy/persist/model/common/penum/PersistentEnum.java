/*
 * Copyright (c) 2000-2003 Netspective Communications LLC. All rights reserved.
 *
 * Netspective Communications LLC ("Netspective") permits redistribution, modification and use of this file in source
 * and binary form ("The Software") under the Netspective Source License ("NSL" or "The License"). The following
 * conditions are provided as a summary of the NSL but the NSL remains the canonical license and must be accepted
 * before using The Software. Any use of The Software indicates agreement with the NSL.
 *
 * 1. Each copy or derived work of The Software must preserve the copyright notice and this notice unmodified.
 *
 * 2. Redistribution of The Software is allowed in object code form only (as Java .class files or a .jar file
 *    containing the .class files) and only as part of an application that uses The Software as part of its primary
 *    functionality. No distribution of the package is allowed as part of a software development kit, other library,
 *    or development tool without written consent of Netspective. Any modified form of The Software is bound by these
 *    same restrictions.
 *
 * 3. Redistributions of The Software in any form must include an unmodified copy of The License, normally in a plain
 *    ASCII text file unless otherwise agreed to, in writing, by Netspective.
 *
 * 4. The names "Netspective", "Axiom", "Commons", "Junxion", and "Sparx" are trademarks of Netspective and may not be
 *    used to endorse products derived from The Software without without written consent of Netspective. "Netspective",
 *    "Axiom", "Commons", "Junxion", and "Sparx" may not appear in the names of products derived from The Software
 *    without written consent of Netspective.
 *
 * 5. Please attribute functionality where possible. We suggest using the "powered by Netspective" button or creating
 *    a "powered by Netspective(tm)" link to http://www.netspective.com for each application using The Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT,
 * ARE HEREBY DISCLAIMED.
 *
 * NETSPECTIVE AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE OR ANY THIRD PARTY AS A
 * RESULT OF USING OR DISTRIBUTING THE SOFTWARE. IN NO EVENT WILL NETSPECTIVE OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THE SOFTWARE, EVEN
 * IF HE HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * @author Shahid N. Shah
 */

/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.persist.model.common.penum;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.type.NullableType;
import org.hibernate.usertype.UserType;


/**
 * Provides a base class for implementations of persistable, type-safe,
 * comparable and serializable enums with a custom persisted representation.
 *
 * @author &Oslash;rjan Nygaard Austvold
 * @version $Revision: 1.1 $
 */
abstract class PersistentEnum implements Comparable, Serializable, UserType
{
    /**
     * <code>Map</code> where key is of class name, value is of <code>Map</code>.
     * where key is of enumCode and value is of enum instance.
     */
    private static final Map<Class,Map<Serializable,PersistentEnum>> enumClasses = new HashMap<Class, Map<Serializable,PersistentEnum>>();
    /**
     * The identifying enum code.
     */
    protected Serializable enumCode;
    /**
     * The name of the enumeration. Used as toString result.
     */
    protected String name;
    /**
     * The hashcode representation of the enum.
     */
    protected transient int hashCode;


    /**
     * Default constructor.  Hibernate need the default constructor
     * to retrieve an instance of the enum from a JDBC resultset.
     * The instance will be converted to the correct enum instance
     * in {@link #nullSafeGet(java.sql.ResultSet, java.lang.String[], java.lang.Object)}.
     */
    protected PersistentEnum()
    {
        // no-op -- instance will be tossed away once the equivalent enum is found.
    }


    /**
     * Constructs a new enumeration instance with the given name and persisted
     * representation of enumCode.
     *
     * @param name     name of the enum instance.
     * @param enumCode persisted enum representation.
     */
    protected PersistentEnum(String name, Serializable enumCode)
    {
        this.name = name;
        this.enumCode = enumCode;
        hashCode = 7 + returnedClass().hashCode() + 3 * enumCode.hashCode();

        Class enumClass = returnedClass();
        Map<Serializable,PersistentEnum> entries = enumClasses.get(enumClass);
        if (entries == null)
        {
            entries = new HashMap<Serializable,PersistentEnum>();
            enumClasses.put(enumClass, entries);
        }
        if (entries.containsKey(enumCode))
        {
            throw new IllegalArgumentException("The enum code must be unique, '"
                    + enumCode + "' has already been added");
        }
        entries.put(enumCode, this);
    }

    public abstract String getTableName();

    /**
     * @see java.lang.Object#hashCode()
     */
    public final int hashCode()
    {
        return hashCode;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public final boolean equals(Object other)
    {

        //@author J.Brown 31.08.2004
        //An instanceof check is probably wise..

        if (!(other instanceof PersistentEnum))
        {
            return false;
        }

        if (other == this)
        {
            return true;

        }
        else if (other == null)
        {
            return false;

        }
        else if (((PersistentEnum) other).returnedClass().getName().equals(returnedClass().getName()))
        {
            // different classloaders
            try
            {
                // try to avoid reflection
                return enumCode.equals(((PersistentEnum) other).enumCode);

            }
            catch (ClassCastException ex)
            {
                // use reflection
                try
                {
                    Method mth = other.getClass().getMethod("getEnumCode", (java.lang.Class[]) null);
                    Serializable enumCode = (Serializable) mth.invoke(other, (java.lang.Object[]) null);
                    return this.enumCode.equals(enumCode);
                }
                catch (Exception ignore)
                { // NoSuchMethod-, IllegalAccess-, InvocationTargetException
                }
                return false;
            }
        }
        else
        {
            return false;
        }
    }


    /**
     * Gets the persistable enum code of this enum.
     *
     * @return the enum code.
     */
    public final Serializable getEnumCode()
    {
        return enumCode;
    }


    /**
     * Resolves this enumeration into a already staticly instantiated enum.
     *
     * @return the type-safe enum equivalent to this enumeration.
     */
    protected Object readResolve()
    {
        Map entries = (Map) enumClasses.get(returnedClass());
        return (entries != null) ? entries.get(enumCode) : null;
    }


    /**
     * Gets the collection of enumeration instances of a given
     * enumeration class.
     *
     * @param enumClass enumeration class type.
     *
     * @return collection of enumerations of the given class.
     */
    protected static Collection getEnumCollection(Class enumClass)
    {
        Map entries = (Map) enumClasses.get(enumClass);
        return (entries != null)
                ? Collections.unmodifiableCollection(entries.values())
                : Collections.EMPTY_LIST;
    }


    /**
     * @see Comparable#compareTo(Object)
     */
    public abstract int compareTo(Object other);


    /**
     * Gets the Hibernate type of the persisted representation of the enum.
     *
     * @return the Nullable Hibernate type.
     */
    protected abstract NullableType getNullableType();


    /**
     * @see org.hibernate.usertype.UserType#sqlTypes()
     */
    public int[] sqlTypes()
    {
        return new int[]{getNullableType().sqlType()};
    }


    /**
     * Simply return the enums name.
     *
     * @return the string representation of this enum.
     */
    public String toString()
    {
        return name;
    }


    /**
     * @see org.hibernate.usertype.UserType#deepCopy(java.lang.Object)
     */
    public Object deepCopy(Object value) throws HibernateException
    {
        // Enums are immutable - nothing to be done to deeply clone it
        return value;
    }


    /**
     * @see org.hibernate.usertype.UserType#isMutable()
     */
    public boolean isMutable()
    {
        // Enums are immutable
        return false;
    }


    /**
     * @return
     *
     * @see org.hibernate.usertype.UserType#returnedClass()
     */
    public Class returnedClass()
    {
        return this.getClass();
    }


    /**
     * @see org.hibernate.usertype.UserType#equals(java.lang.Object, java.lang.Object)
     */
    public boolean equals(Object x, Object y) throws HibernateException
    {
        if (x == y)
        {
            return true;
        }
        else if (x == null || y == null)
        {
            return false;
        }
        else
        {
            return getNullableType().isEqual(x, y);
        }
    }


    /**
     * @see org.hibernate.usertype.UserType#nullSafeGet(java.sql.ResultSet, java.lang.String[], java.lang.Object)
     */
    public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException
    {
        Serializable enumCode = (Serializable) getNullableType().nullSafeGet(rs, names[0]);
        Map entries = (Map) enumClasses.get(returnedClass());
        return (PersistentEnum) ((entries != null)
                ? entries.get(enumCode)
                : null);
    }


    /**
     * @see org.hibernate.usertype.UserType#nullSafeSet(java.sql.PreparedStatement, java.lang.Object, int)
     */
    public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException
    {
        // make sure the received value is of the right type
        if ((value != null) && !returnedClass().isAssignableFrom(value.getClass()))
        {
            throw new IllegalArgumentException("Received value is not a [" +
                    returnedClass().getName() + "] but [" + value.getClass() + "]");
        }

        if (value == null)
        {
            st.setNull(index, getNullableType().sqlType());
        }
        else
        {
            // convert the enum into its persistence format
            Serializable enumCode = ((PersistentEnum) value).getEnumCode();

            // set the value into the resultset
            st.setObject(index, enumCode, getNullableType().sqlType());
        }
    }

    public Object assemble(Serializable cached, Object owner) throws HibernateException
    {
        return cached;
    }

    public Serializable disassemble(Object value) throws HibernateException
    {
        return (Serializable) value;
    }

    public int hashCode(Object x) throws HibernateException
    {
        return x.hashCode();
    }

    public Object replace(Object original, Object target, Object owner) throws HibernateException
    {
        return original;
    }
}