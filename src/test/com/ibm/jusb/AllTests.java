package com.ibm.jusb;

/** 
 * Copyright (c) IBM Corporation, 2000
 * This software and documentation is the confidential and proprietary
 * information of IBM, Corp. ("Confidential Information").
 * Raleigh, NC USA
 */

import junit.framework.*;

/**
 * A TestSuite that puts together all the test classes from
 * all com.ibm.jusb.* packages
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

		suite.addTest( com.ibm.jusb.util.AllTests.suite() );
		suite.addTest( com.ibm.jusb.os.AllTests.suite() );

		return suite;
	}
}
