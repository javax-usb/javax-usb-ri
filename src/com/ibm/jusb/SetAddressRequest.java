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
 * Implements a SET_ADDRESS Request
 * @author E. Michael Maximilien
 * @version 1.0.0
 */
public class SetAddressRequest extends StandardRequest
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	/**
	 * Creates a Request object with default fields value 
	 * @param factory the RequestFactory object that created this request
	 */
	public SetAddressRequest( RequestFactory factory ) 
	{
		super( factory );
		init( SET_ADDRESS_BM_REQUEST_TYPE, (short)0, (short)0, new byte[ 0 ] );
	}

	//-------------------------------------------------------------------------
	// Abstract public methods
	//

	/** @return the Request code byte for this request */
	public byte getRequest() { return RequestConst.REQUEST_SET_ADDRESS; }

	/**
	 * Accepts a RequestVisitor object
	 * @param visitor the RequestVisitor object
	 */
	public void accept( RequestVisitor visitor ) { visitor.visitSetAddressRequest( this ); }

	/**
	 * @return the unique String name of this request.  This name should be the same
	 * for all instances of this Request
	 */
	public String getName() { return SET_ADDRESS_REQUEST_NAME; }

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
		if( b != SET_ADDRESS_BM_REQUEST_TYPE )
			throw new UsbRuntimeException( "Cannot set bmRequestType to anything other than " + SET_ADDRESS_BM_REQUEST_TYPE + " for a GetDescriptor request" );	

		bmRequestType = SET_ADDRESS_BM_REQUEST_TYPE;
	}

	/**
	 * Sets the wValue for this request 
	 * @param s the short argument
	 * @exception javax.usb.UsbRuntimeException if the value could not be set or the argument is incorrect
	 */
	public void setValue( short s ) throws UsbRuntimeException
	{
		wValue = s;
	}

	/**
	 * Sets the wIndex for this request
	 * @param s the short argument
	 * @exception javax.usb.UsbRuntimeException if the value could not be set or the argument is incorrect
	 */
	public void setIndex( short s ) throws UsbRuntimeException
	{ 
		if( s != 0 )
			throw new UsbRuntimeException( "wIndex value for a SetAddress request must be 0" );

		wIndex = s; 
	}

	/** 
	 * Sets the data byte[] for this request
	 * @param byteArray the byte[] object
	 * @exception javax.usb.UsbRuntimeException if the value could not be set or the argument is incorrect
	 */
	public void setData( byte[] byteArray ) throws UsbRuntimeException
	{ throw new UsbRuntimeException( "Data field not used in a SetAddress request" ); }

	//-------------------------------------------------------------------------
	// Class constants
	//

	public static final byte SET_ADDRESS_BM_REQUEST_TYPE = (byte)0x00;

	public static final String SET_ADDRESS_REQUEST_NAME = "SetAddressRequest";
}
