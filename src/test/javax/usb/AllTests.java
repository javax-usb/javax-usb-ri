package javax.usb;

/** 
 * Copyright (c) IBM Corporation, 2000
 * This software and documentation is the confidential and proprietary
 * information of IBM, Corp. ("Confidential Information").
 * Raleigh, NC USA
 */

import junit.framework.*;

/**
 * A TestSuite that puts together all the test classes from
 * all javax.usb.* packages
 * @author Helen Li
 */
public class AllTests extends TestSuite
{
	//-------------------------------------------------------------------------
	// Public class methods
	//

	public static TestSuite suite()
	{
		TestSuite suite = new AllTests();

		suite.addTest( UsbDeviceTestCase.suite() );

/* No tests yet.
		suite.addTest( new TestSuite( StandardOperationsTestCase.class ) );
*/

		suite.addTest( javax.usb.util.AllTests.suite() );
		suite.addTest( javax.usb.test.AllTests.suite() );
		suite.addTest( javax.usb.event.AllTests.suite() );

		return suite;
	}
}

