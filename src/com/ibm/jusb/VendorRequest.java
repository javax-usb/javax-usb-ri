package com.ibm.jusb;

/*
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import javax.usb.*;

/**
 * Defines all VendorRequest
 * @author E. Michael Maximilien
 * @version 1.0.0
 */
public class VendorRequest extends AbstractRequest
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	/** 
	 * Creates a new Request object with empty fields
	 * @param factory the RequestFactory object that created this Request
	 * @param b the request value for this Request (defined by vendor)
	 */
	public VendorRequest( RequestFactory factory, byte b ) 
	{  
		super( factory ); 

		request = b;
	}

	//-------------------------------------------------------------------------
	// Public methods
	//

	/** @return the Request code byte for this request */
	public byte getRequest() { return request; }

	/**
	 * Accepts a RequestVisitor object
	 * @param visitor the RequestVisitor object
	 */
	public void accept( RequestVisitor visitor ) { visitor.visitVendorRequest( this ); }

	/**
	 * @return the unique String name of this request.  This name should be the same
	 * for all instances of this Request
	 */
	public String getName() { return VENDOR_REQUEST_NAME; }

	//-------------------------------------------------------------------------
	// Instance variables
	//

	protected byte request = (byte)0x00;

	//-------------------------------------------------------------------------
	// Class constants
	//

	public static final String VENDOR_REQUEST_NAME = "VendorRequest";
}
