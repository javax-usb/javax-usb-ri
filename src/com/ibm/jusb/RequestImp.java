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
 * Request implementation.
 * @author Dan Streetman
 * @author E. Michael Maximilien
 */
public class RequestImp implements Request,UsbOperations.SubmitResult
{
	/** Constructor */
	public RequestImp( RequestFactoryImp factory ) { requestFactoryImp = factory; }

	/** @return the bmRequestType bitmap byte for this Request */
	public byte getRequestType() { return bmRequestType; }

	/** @param type the bmRequestType bitmap byte for this Request */
	public void setRequestType(byte type) { bmRequestType = type; }

	/** @return the Request code byte for this request */
	public byte getRequest() { return request; }

	/** @param r the Request code byte for this request */
	public void setRequest(byte r) { request = r; }

	/** @return the wValue for this request */
	public short getValue() { return wValue; }

	/** @param v the wValue for this request */
	public void setValue(short v) { wValue = v; }

	/** @return the wIndex for this request */
	public short getIndex() { return wIndex; }

	/** @param i the wIndex for this request */
	public void setIndex(short i) { wIndex = i; }

	/** @return the length of the <i>data</i> (not including setup bytes) for this request. */
	public short getLength() { return (short)data.length; }

	/** @return the length of valid data */
	public int getDataLength() { return dataLength; }

	/** @param len the length of valid data */
	public void setDataLength(int len) { dataLength = len; }

	/** @return the data byte[] for this request */
	public byte[] getData() { return data; }

	/** @param d the data byte[] for this request */
	public void setData(byte[] d) { data = d; }

	/** @return a formated byte[] representing this Request object */
	public byte[] toBytes()
	{
		int requestBytesSize = REQUEST_HEADER_LENGTH + getData().length;

		byte[] requestBytes = new byte[ requestBytesSize ];

		requestBytes[ 0 ] = getRequestType();
		requestBytes[ 1 ] = getRequest();

		requestBytes[ 2 ] = (byte)getValue();
		requestBytes[ 3 ] = (byte)( getValue() >> 8 );

		requestBytes[ 4 ] = (byte)getIndex();
		requestBytes[ 5 ] = (byte)( getIndex() >> 8 );

		short dataLength = (short)getData().length;

		requestBytes[ 6 ] = (byte)dataLength;
		requestBytes[ 7 ] = (byte)( dataLength >> 8 );

		byte[] dataBytes = getData();

		for( int i = 0; i < dataBytes.length; ++i )
			requestBytes[ REQUEST_HEADER_LENGTH + i ] = dataBytes[ i ]; 

		return requestBytes;
	}

	/** Clean this object */
	public void clean()
	{
		bmRequestType = 0x00;
		bRequest = 0x00;
		wValue = 0x0000;
		wIndex = 0x0000;
		data = new byte[0];
		dataLength = 0;
	}

	/** Recycle this object */
	public void recycle()
	{
		requestFactoryImp.recycle(this);
	}

	//**************************************************************************
	// Instance variables

	protected byte bmRequestType = 0x00;
	protected byte bRequest = 0x00;
	protected short wValue = 0x0000;
	protected short wIndex = 0x0000;
	protected byte[] data = new byte[ 0 ];
	protected int dataLength = 0;

	protected RequestFactoryImp requestFactoryImp = null;

	//**************************************************************************
	// Class constants

	public static final int REQUEST_HEADER_LENGTH = 8;
}
