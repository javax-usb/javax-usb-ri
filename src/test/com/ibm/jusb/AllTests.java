package com.ibm.jusb;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import junit.framework.*;

/**
 * A TestSuite that puts together all the test classes from
 * all com.ibm.jusb.* packages
 * @author E. Michael Maximilien
 * @author Dan Streetman
 */
public class AllTests extends TestSuite
{
	//-------------------------------------------------------------------------
	// Public class methods
	//

	public static TestSuite suite()
	{
		TestSuite suite = new TestSuite();

		/* This package test cases */
		suite.addTestSuite( UsbTestCase.class );

		/* Subpackage test cases */
		suite.addTest( com.ibm.jusb.os.AllTests.suite() );
		suite.addTest( com.ibm.jusb.test.AllTests.suite() );
		suite.addTest( com.ibm.jusb.util.AllTests.suite() );

		return suite;
	}
}
