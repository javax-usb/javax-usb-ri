package javax.usb;

/** 
 * Copyright (c) IBM Corporation, 2000
 * This software and documentation is the confidential and proprietary
 * information of IBM, Corp. ("Confidential Information").
 * Raleigh, NC USA
 */

import junit.framework.*;

/**
 * Superclass for all javax.usb API TestCases
 * @author E. Mchael Maximilien
 */
public class UsbTestCase extends TestCase
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	/** 
	 * Constructor
	 * @param name the name of the test case
	 */
	public UsbTestCase( String name ) { super( name ); }

	//-------------------------------------------------------------------------
	// Protected methods
	//

	/** Set up tests */
	protected void setUp() {}

	/** Tear down tests */
	protected void tearDown() {}

	/** Call this to printout the an EMPTY test message */
	protected void emptyTest()
	{
		System.out.println( "---->" + EMPTY_TEST_MSG + " from class " + 
							getClass().getName() + "<----" );
	}

	//-------------------------------------------------------------------------
	// Class constants
	//

	public static final String EMPTY_TEST_MSG = "EMPTY TEST";
}
