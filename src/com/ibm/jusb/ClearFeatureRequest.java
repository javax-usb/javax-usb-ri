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
 * Implements a CLEAR_FEATURE Request
 * @author E. Michael Maximilien
 * @version 1.0.0
 */
public class ClearFeatureRequest extends StandardRequest
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	/**
	 * Creates a Request object with default fields value 
	 * @param factory the RequestFactory object that created this request
	 */
	public ClearFeatureRequest( RequestFactory factory ) 
	{
		super( factory );
		init( CLEAR_FEATURE_BM_REQUEST_TYPE0, (short)0, (short)0, new byte[ 0 ] );
	}

	//-------------------------------------------------------------------------
	// Public methods
	//

	/** @return the Request code byte for this request */
	public byte getRequest() { return RequestConst.REQUEST_CLEAR_FEATURE; }

	/**
	 * Accepts a RequestVisitor object
	 * @param visitor the RequestVisitor object
	 */
	public void accept( RequestVisitor visitor ) { visitor.visitClearFeatureRequest( this ); }

	/**
	 * @return the unique String name of this request.  This name should be the same
	 * for all instances of this Request
	 */
	public String getName() { return CLEAR_FEATURE_REQUEST_NAME; }

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
		switch( b )
		{
			case CLEAR_FEATURE_BM_REQUEST_TYPE0:
			case CLEAR_FEATURE_BM_REQUEST_TYPE1:
			case CLEAR_FEATURE_BM_REQUEST_TYPE2:
				break;

			default:
				throw new UsbRuntimeException( "Invalid bmRequestType = " + bmRequestType + " argument in ClearFeature request" );
		}

		bmRequestType = b;
	}

	/** 
	 * Sets the data byte[] for this request
	 * @param byteArray the byte[] object
	 * @exception javax.usb.UsbRuntimeException if the value could not be set or the argument is incorrect
	 */
	public void setData( byte[] byteArray ) throws UsbRuntimeException
	{ throw new UsbRuntimeException( "Data field not used in a ClearFeature request" ); }

	//-------------------------------------------------------------------------
	// Class constants
	//

	private static final byte CLEAR_FEATURE_BM_REQUEST_TYPE0 = 0x00;
	private static final byte CLEAR_FEATURE_BM_REQUEST_TYPE1 = 0x01;
	private static final byte CLEAR_FEATURE_BM_REQUEST_TYPE2 = 0x02;

	public static final String CLEAR_FEATURE_REQUEST_NAME = "ClearFeatureRequest";
}
