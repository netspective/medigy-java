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
package com.medigy.persist.util;

import com.medigy.persist.util.DelimitedValuesReader.IgnoredLineHandler;
import com.medigy.persist.util.DelimitedValuesReader.LineHandler;
import junit.framework.TestCase;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class DelimitedValuesReaderTest extends TestCase
{
    public class TestLineHandler implements LineHandler, IgnoredLineHandler
    {
        private int bodyLinesCount = 0;
        private int ignoredHeaderLinesCount = 0;
        private int ignoredLinesCount = 0;

        public TestLineHandler()
        {
        }

        public void handleHeaderLine(final DelimitedValuesReader dvReader, final List<String> line)
        {
        }

        public void handleLine(final DelimitedValuesReader dvReader, final List<String> line)
        {
            bodyLinesCount++;
        }

        public void handleIgnoredHeaderLine(DelimitedValuesReader dvReader, String line)
        {
            ignoredHeaderLinesCount++;
        }

        public void handleIgnoredLine(DelimitedValuesReader dvReader, String line)
        {
            ignoredLinesCount++;
        }
    }

    /**
     * Tests a basic successful read of a CSV file. One header line is expected with 3 columns and 20 body lines are
     * to be found.
     */
    public void testSimpleDelimitedValuesReader() throws IOException
    {
        // the constructor will run the parse because a line handler is provided
        final TestLineHandler lineHandler = new TestLineHandler();
        final DelimitedValuesReader dvr = new DelimitedValuesReader(lineHandler, getClass().getResource("DelimitedValuesReaderTest_Simple.csv"), true);
        dvr.readAll();

        assertEquals(0, dvr.getErrors().size());
        assertEquals(1, dvr.getHeaderLines().size());
        assertEquals(20, lineHandler.bodyLinesCount);
        assertEquals(3, dvr.getExpectedFieldsPerlLineCount());
        assertEquals(0, lineHandler.ignoredHeaderLinesCount);
        assertEquals(0, lineHandler.ignoredLinesCount);
    }

    /**
     * Tests a bit more complex CSV file that has lines that can be ignored (blank lines and lines starting with #) and
     * it has two header lines instead of just a simple one line header.
     */
    public void testDelimitedValuesReaderWithIgnoredLines() throws IOException
    {
        final TestLineHandler lineHandler = new TestLineHandler();
        final DelimitedValuesReader dvr = new DelimitedValuesReader(lineHandler, new InputStreamReader(getClass().getResource("DelimitedValuesReaderTest_Ignorable.csv").openStream()),
                "DelimitedValuesReaderTest_Ignorable.csv", new DelimitedValuesParser(), 2, true, "^#.*", lineHandler,
                true, DelimitedValuesReader.AUTO_CALC_EXPECTED_FIELDS, false, 10);

        dvr.readAll();

        assertEquals(0, dvr.getErrors().size());
        assertEquals(2, dvr.getHeaderLines().size());
        assertEquals(20, lineHandler.bodyLinesCount);
        assertEquals(3, dvr.getExpectedFieldsPerlLineCount());
        assertEquals(2, lineHandler.ignoredHeaderLinesCount);
        assertEquals(7, lineHandler.ignoredLinesCount);
    }
}
