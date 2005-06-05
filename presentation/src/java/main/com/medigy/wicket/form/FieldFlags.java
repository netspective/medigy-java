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
package com.medigy.wicket.form;

public interface FieldFlags
{
    public static final long DEFAULT_FLAGS = 0;

    // These constants MUST be kept identical to what is in theme.js FLDFLAG_* constants

    public static final int REQUIRED = 1;
    public static final int PRIMARY_KEY = REQUIRED * 2;
    public static final int PRIMARY_KEY_GENERATED = PRIMARY_KEY * 2;
    public static final int UNAVAILABLE = PRIMARY_KEY_GENERATED * 2;
    public static final int READ_ONLY = UNAVAILABLE * 2;
    public static final int INITIAL_FOCUS = READ_ONLY * 2;
    public static final int PERSIST = INITIAL_FOCUS * 2;
    public static final int CREATE_ADJACENT_AREA = PERSIST * 2;
    public static final int INPUT_HIDDEN = CREATE_ADJACENT_AREA * 2;
    public static final int BROWSER_READONLY = INPUT_HIDDEN * 2;
    public static final int DOUBLE_ENTRY = BROWSER_READONLY * 2;
    public static final int SCANNABLE = DOUBLE_ENTRY * 2;
    public static final int AUTO_BLUR = SCANNABLE * 2;
    public static final int SUBMIT_ONBLUR = AUTO_BLUR * 2;
    public static final int CREATE_ADJACENT_AREA_HIDDEN = SUBMIT_ONBLUR * 2;
    public static final int CLEAR_TEXT_ON_VALIDATE_ERROR = CREATE_ADJACENT_AREA_HIDDEN * 2;
    public static final int ALWAYS_SHOW_HINT = CLEAR_TEXT_ON_VALIDATE_ERROR * 2;
}
