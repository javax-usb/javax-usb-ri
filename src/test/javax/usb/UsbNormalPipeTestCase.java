package javax.usb;

/** 
 * Copyright (c) IBM Corporation, 2000
 * This software and documentation is the confidential and proprietary
 * information of IBM, Corp. ("Confidential Information").
 * Raleigh, NC USA
 */

import junit.framework.*;

/**
 * A Test for UsbNormalPipe
 * @author Dan Streetman
 */
public class UsbNormalPipeTestCase extends UsbTestCase
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	/** 
	 * Constructor
	 * @param name the name of the test case
	 */
	public UsbNormalPipeTestCase( String name ) { super( name ); }

	//-------------------------------------------------------------------------
	// Protected methods
	//

	/** Set up tests */
	protected void setUp() {}

	/** Tear down tests */
	protected void tearDown() {}

	//-------------------------------------------------------------------------
	// Public testXyz methods
	//

	/** Default test since JUnit will complain if your test class executes w/o a testXyz method */
	public void testDefault() { emptyTest(); }
}
