package com.ibm.jusb.util;

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
 * the com.ibm.jusb.util package
 * @author E. Michael Maximilien
 */
public class AllTests extends TestSuite
{
	//-------------------------------------------------------------------------
	// Public class methods
	//

	public static TestSuite suite()
	{
		TestSuite suite = new TestSuite();

		suite.addTestSuite( DiGraphTestCase.class );
		suite.addTestSuite( FifoSchedulerTestCase.class );
		suite.addTestSuite( GraphTestCase.class );
        suite.addTestSuite( QueueTestCase.class );
		suite.addTestSuite( StateMachineTestCase.class );

		return suite;
	}
}
