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
 * Abstract superclass for all StandardRequest
 * @author E. Michael Maximilien
 * @version 1.0.0
 */
public abstract class StandardRequest extends AbstractRequest
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	/** 
	 * Creates a new Request object with empty fields
	 * @param factory the RequestFactory object that created this Request
	 */
	public StandardRequest( RequestFactory factory ) {  super( factory ); }

	//-------------------------------------------------------------------------
	// Public methods
	//

	/**
	 * Accepts a RequestVisitor object
	 * @param visitor the RequestVisitor object
	 */
	public void accept( RequestVisitor visitor ) { visitor.visitStandardRequest( this ); }

	/**
	 * @return the unique String name of this request.  This name should be the same
	 * for all instances of this Request
	 */
	public String getName() { return STANDARD_REQUEST_NAME; }

	//-------------------------------------------------------------------------
	// Class constants
	//

	public static final String STANDARD_REQUEST_NAME = "StandardRequest";
}
