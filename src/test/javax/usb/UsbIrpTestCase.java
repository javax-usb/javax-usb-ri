package javax.usb;

/** 
 * Copyright (c) IBM Corporation, 2000
 * This software and documentation is the confidential and proprietary
 * information of IBM, Corp. ("Confidential Information").
 * Raleigh, NC USA
 */

import junit.framework.*;

import javax.usb.util.*;

import javax.usb.UsbTestCase;

/**
 * A Test for UsbIrp
 * @author Dan Streetman
 */
public class UsbIrpTestCase extends UsbTestCase
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	/** 
	 * Constructor
	 * @param name the name of the test case
	 */
	public UsbIrpTestCase( String name ) { super( name ); }

	//-------------------------------------------------------------------------
	// Protected methods
	//

	/** Set up tests */
	protected void setUp() {}

	/** Tear down tests */
	protected void tearDown() {}

	//*************************************************************************
	// Public methods

	/** Default test since JUnit will complain if your test class executes w/o a testXyz method */
	public void testDefault() { emptyTest(); }
}
