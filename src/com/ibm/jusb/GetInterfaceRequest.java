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
 * Implements a GET_INTERFACE Request
 * @author E. Michael Maximilien
 * @version 1.0.0
 */
public class GetInterfaceRequest extends StandardRequest
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	/**
	 * Creates a Request object with default fields value
	 * @param factory the RequestFactory object that created this request
	 */
	public GetInterfaceRequest( RequestFactory factory ) 
	{
		super( factory );
		init( GET_INTERFACE_BM_REQUEST_TYPE, (short)0, (short)0, new byte[ 1 ] );
	}

	//-------------------------------------------------------------------------
	// Abstract public methods
	//

	/** @return the Request code byte for this request */
	public byte getRequest() { return RequestConst.REQUEST_GET_INTERFACE; }

	/**
	 * Accepts a RequestVisitor object
	 * @param visitor the RequestVisitor object
	 */
	public void accept( RequestVisitor visitor ) { visitor.visitGetInterfaceRequest( this ); }

	/**
	 * @return the unique String name of this request.  This name should be the same
	 * for all instances of this Request
	 */
	public String getName() { return GET_INTERFACE_REQUEST_NAME; }

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
		if( b != GET_INTERFACE_BM_REQUEST_TYPE )
			throw new UsbRuntimeException( "Cannot set bmRequestType to anything other than " + GET_INTERFACE_BM_REQUEST_TYPE + " for a GetInterface request" );	

		bmRequestType = GET_INTERFACE_BM_REQUEST_TYPE;
	}

	/**
	 * Sets the wValue for this request 
	 * @param s the short argument
	 * @exception javax.usb.UsbRuntimeException if the value could not be set or the argument is incorrect
	 */
	public void setValue( short s ) throws UsbRuntimeException
	{
		if( s != (short)0 )
			throw new UsbRuntimeException( "Cannot set wValue to anything but 0 for a GetInterface request" );	

		wValue = s;
	}

	/** 
	 * Sets the data byte[] for this request
	 * @param byteArray the byte[] object
	 * @exception javax.usb.UsbRuntimeException if the value could not be set or the argument is incorrect
	 */
	public void setData( byte[] byteArray ) throws UsbRuntimeException
	{ 
		checkNull( byteArray );

		if( byteArray.length < 1 )
			throw new UsbRuntimeException( "The Data length of a GetInterface request should be 1" );	

		data = byteArray; 
	}

	//-------------------------------------------------------------------------
	// Class constants
	//

	public static final byte GET_INTERFACE_BM_REQUEST_TYPE = (byte)0x81;

	public static final String GET_INTERFACE_REQUEST_NAME = "GetInterfaceRequest";
}
