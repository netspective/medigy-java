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

import org.hibernate.Hibernate;
import org.hibernate.type.NullableType;

/**
 * Provides a base class for persistable, type-safe, comparable,
 * and serializable enums persisted as characters.
 * <p/>
 * <p>Create a subclass of this class implementing the enumeration:
 * <pre>package com.foo;
 * <p/>
 * public final class Gender extends PersistentCharacterEnum {
 *  public static final Gender MALE = new Gender("male", 'M');
 *  public static final Gender FEMALE = new Gender("female", 'F');
 *  public static final Gender UNDETERMINED = new Gender("undetermined", 'U');
 * <p/>
 *  public Gender() {}
 * <p/>
 *  private Gender(String name, char persistentValue) {
 *   super(name, persistentValue);
 *  }
 * }
 * </pre>
 * Note that a no-op default constructor must be provided.</p>
 * <p/>
 * <p>Use this enumeration in your mapping file as:
 * <pre>&lt;property name="gender" type="com.foo.Gender"&gt;</pre></p>
 * <p/>
 * <p><code>
 * $Id: PersistentCharacterEnum.java,v 1.1 2005-05-05 19:30:15 shahid.shah Exp $
 * </pre></p>
 *
 * @author &Oslash;rjan Nygaard Austvold
 * @version $Revision: 1.1 $
 */
public abstract class PersistentCharacterEnum extends PersistentEnum
{
    /**
     * Default constructor.  Hibernate need the default constructor
     * to retrieve an instance of the enum from a JDBC resultset.
     * The instance will be converted to the correct enum instance
     * in {@link #nullSafeGet(java.sql.ResultSet, java.lang.String[], java.lang.Object)}.
     */
    protected PersistentCharacterEnum()
    {
        // no-op -- instance will be tossed away once the equivalent enum is found.
    }


    /**
     * Constructs an enum with the given name and persistent representation.
     *
     * @param name                name of enum.
     * @param persistentCharacter persistent representation of the enum.
     */
    protected PersistentCharacterEnum(String name, char persistentCharacter)
    {
        super(name, new Character(persistentCharacter));
    }


    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object other)
    {
        if (other == this)
        {
            return 0;
        }
        return ((Character) getEnumCode()).compareTo((Character) ((PersistentEnum) other).getEnumCode());
    }


    /**
     * @see PersistentEnum#getNullableType()
     */
    protected NullableType getNullableType()
    {
        return Hibernate.CHARACTER;
    }
}