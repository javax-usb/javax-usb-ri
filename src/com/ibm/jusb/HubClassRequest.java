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
 * Abstract superclass for all HubClassRequest
 * @author E. Michael Maximilien
 * @version 1.0.0
 */
public abstract class HubClassRequest extends ClassRequest
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	/** 
	 * Creates a new Request object with empty fields
	 * @param factory the RequestFactory object that created this Request
	 * @param b the request type byte
	 */
	public HubClassRequest( RequestFactory factory, byte b ) { super( factory, b ); }

	//-------------------------------------------------------------------------
	// Public methods
	//

	/**
	 * Accepts a RequestVisitor object
	 * @param visitor the RequestVisitor object
	 */
	public void accept( RequestVisitor visitor ) { visitor.visitHubClassRequest( this ); }

	/**
	 * @return the unique String name of this request.  This name should be the same
	 * for all instances of this Request
	 */
	public String getName() { return HUB_CLASS_REQUEST_NAME; }

	//-------------------------------------------------------------------------
	// Class constants
	//

	public static final String HUB_CLASS_REQUEST_NAME = "HubClassRequest";
}
