package javax.usb;

/*
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import junit.framework.*;

import javax.usb.*;
import com.ibm.jusb.test.JUnitUtility;

/**
 * This test suite puts together TestCase classes for Request and Operations
 * @author E. Michael Maximilien
 */
public class RequestOpsTests extends TestSuite
{
	//-------------------------------------------------------------------------
	// Public class methods
	//

	public static TestSuite suite()
	{
		TestSuite suite = new AllTests();

		suite.addTestSuite( RequestBundleTestCase.class );
		suite.addTestSuite( RequestFactoryTestCase.class );

		suite.addTestSuite( StandardOperationsTestCase.class );
		suite.addTestSuite( ClassOperationsTestCase.class );
		suite.addTestSuite( VendorOperationsTestCase.class );

		return suite;
	}
}

