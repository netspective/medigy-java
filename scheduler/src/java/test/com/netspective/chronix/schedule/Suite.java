package com.netspective.chronix.schedule;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * $Id: Suite.java,v 1.1 2004-04-10 18:04:53 shahid.shah Exp $
 */
public class Suite
{
	public static Test suite()
	{
		TestSuite suite = new TestSuite();

		suite.addTest(new TestSuite(com.netspective.chronix.CalendarUtilsTest.class));
		suite.addTest(new TestSuite(com.netspective.chronix.schedule.ScheduleManagerTest.class));

		return suite;
	}
}
