package com.ibm.jusb.util;

/** 
 * Copyright (c) IBM Corporation, 2000
 * This software and documentation is the confidential and proprietary
 * information of IBM, Corp. ("Confidential Information").
 * Raleigh, NC USA
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
		TestSuite suite = new AllTests();

		suite.addTest( new TestSuite( FifoSchedulerTestCase.class ) );
		suite.addTest( new TestSuite( GraphTestCase.class ) );
		suite.addTest( new TestSuite( DiGraphTestCase.class ) );
        suite.addTest( new TestSuite( QueueTestCase.class ) );
		suite.addTest( new TestSuite( StateMachineTestCase.class ) );

		return suite;
	}
}
