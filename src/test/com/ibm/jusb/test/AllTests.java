package com.ibm.jusb.test;

/** 
 * Copyright (c) IBM Corporation, 2000
 * This software and documentation is the confidential and proprietary
 * information of IBM, Corp. ("Confidential Information").
 * Raleigh, NC USA
 */

import junit.framework.*;

/**
 * A TestSuite that puts together all the test classes from
 * the com.ibm.jusb.test package
 * @author E. Michael Maximilien
 * @author Kevin H. Bell
 */
public class AllTests extends TestSuite
{
	//-------------------------------------------------------------------------
	// Public class methods
	//

	public static TestSuite suite()
	{
		TestSuite suite = new AllTests();

            suite.addTest( new TestSuite( JUnitUtilityTestCase.class ) );

		return suite;
	}
}
