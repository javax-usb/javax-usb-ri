package com.ibm.jusb.os;

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
 * the com.ibm.jusb.os package
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
        suite.addTestSuite( DefaultStdOpsTestCase.class );
        suite.addTestSuite( DefaultUsbIrpFactoryTestCase.class );
        suite.addTestSuite( DefaultUsbPipeFactoryTestCase.class );
        suite.addTestSuite( RequestFactoryTestCase.class );
        suite.addTestSuite( UsbConfigImpTestCase.class );
        suite.addTestSuite( UsbDeviceImpTestCase.class );
        suite.addTestSuite( UsbEndpointImpTestCase.class );
        suite.addTestSuite( UsbHubImpTestCase.class );
        suite.addTestSuite( UsbInterfaceImpTestCase.class );
        suite.addTestSuite( UsbIrpImpTestCase.class );
        suite.addTestSuite( UsbPipeAbstractionTestCase.class );
        suite.addTestSuite( UsbPipeEventHelperTestCase.class );
        suite.addTestSuite( UsbPortImpTestCase.class );
        suite.addTestSuite( UsbRootHubImpTestCase.class );
        suite.addTestSuite( UsbTopologyServicesHelperTestCase.class );
        suite.addTestSuite( UsbUtilityTestCase.class );

		/* Subclass test cases */
		suite.addTest( com.ibm.jusb.os.linux.AllTests.suite() );

		return suite;
	}
}
