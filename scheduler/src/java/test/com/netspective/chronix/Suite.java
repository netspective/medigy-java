package com.netspective.chronix;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * $Id: Suite.java,v 1.1 2004-04-10 18:04:52 shahid.shah Exp $
 */
public class Suite
{
	public static Test suite()
	{
		TestSuite suite = new TestSuite();

		suite.addTest(new TestSuite(CalendarUtilsTest.class));
        suite.addTest(com.netspective.chronix.set.Suite.suite());
		suite.addTest(com.netspective.chronix.schedule.Suite.suite());

		return suite;
	}
}
