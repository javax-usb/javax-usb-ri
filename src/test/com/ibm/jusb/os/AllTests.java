package com.ibm.jusb.os;

/** 
 * Copyright (c) IBM Corporation, 2000
 * This software and documentation is the confidential and proprietary
 * information of IBM, Corp. ("Confidential Information").
 * Raleigh, NC USA
 */

import junit.framework.*;

/**
 * A TestSuite that puts together all the test classes from
 * the com.ibm.jusb.os package
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

        suite.addTest( new TestSuite( RequestFactoryTestCase.class ) );
	suite.addTest( new TestSuite( com.ibm.jusb.os.linux.AllTests.class ) );

		return suite;
	}
}
