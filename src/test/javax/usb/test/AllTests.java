package javax.usb.test;

/** 
 * Copyright (c) IBM Corporation, 2000
 * This software and documentation is the confidential and proprietary
 * information of IBM, Corp. ("Confidential Information").
 * Raleigh, NC USA
 */

import junit.framework.*;

/**
 * A TestSuite that puts together all the test classes from
 * the javax.usb.test package
 * @author E. Michael Maximilien
 */
public class AllTests extends TestSuite
{
	//-------------------------------------------------------------------------
	// Public class methods
	//

	public static TestSuite suite()
	{
		TestSuite suite = new AllTests();

		suite.addTest( new JUnitUtilityTestCase( "testIsEqualsEnum" ) );
		suite.addTest( new JUnitUtilityTestCase( "testIsIdenticalEnum" ) );
		suite.addTest( new JUnitUtilityTestCase( "testIsEqualsVector" ) );

		return suite;
	}
}
