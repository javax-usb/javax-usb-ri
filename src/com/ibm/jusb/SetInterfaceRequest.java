package com.ibm.jusb.os;

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
 * Implements a SET_INTERFACE Request
 * @author E. Michael Maximilien
 * @version 1.0.0
 */
public class SetInterfaceRequest extends StandardRequest
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	/**
	 * Creates a Request object with default fields value
	 * @param factory the RequestFactory object that created this request
	 */
	public SetInterfaceRequest( RequestFactory factory ) 
	{
		super( factory );
		init( SET_INTERFACE_BM_REQUEST_TYPE, (short)0, (short)0, new byte[ 0 ] );
	}

	//-------------------------------------------------------------------------
	// Abstract public methods
	//

	/** @return the Request code byte for this request */
	public byte getRequest() { return RequestConst.REQUEST_SET_INTERFACE; }

	/**
	 * Accepts a RequestVisitor object
	 * @param visitor the RequestVisitor object
	 */
	public void accept( RequestVisitor visitor ) { visitor.visitSetInterfaceRequest( this ); }

	/**
	 * @return the unique String name of this request.  This name should be the same
	 * for all instances of this Request
	 */
	public String getName() { return SET_INTERFACE_REQUEST_NAME; }

	//-------------------------------------------------------------------------
	// Public setter methods
	//

	/**
	 * Sets the bmRequestType bitmap byte for this Request 
	 * @param b the byte argument
	 * @exception javax.usb.UsbRuntimeException if the value could not be set or the argument is incorrect
	 */
	public void setRequestType( byte b ) throws UsbRuntimeException
	{ 
		if( b != SET_INTERFACE_BM_REQUEST_TYPE )
			throw new UsbRuntimeException( "Cannot set bmRequestType to anything other than " + SET_INTERFACE_BM_REQUEST_TYPE + " for a SetInterface request" );

		bmRequestType = SET_INTERFACE_BM_REQUEST_TYPE;
	}

	/** 
	 * Sets the data byte[] for this request
	 * @param byteArray the byte[] object
	 * @exception javax.usb.UsbRuntimeException if the value could not be set or the argument is incorrect
	 */
	public void setData( byte[] byteArray ) throws UsbRuntimeException
	{ throw new UsbRuntimeException( "Data field not used in a SetInterface request" ); }


	//-------------------------------------------------------------------------
	// Class constants
	//

	public static final byte SET_INTERFACE_BM_REQUEST_TYPE = (byte)0x01;

	//-------------------------------------------------------------------------
	// Class constants
	//

	public static final String SET_INTERFACE_REQUEST_NAME = "SetInterfaceRequest";
}
