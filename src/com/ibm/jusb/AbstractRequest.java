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

import com.ibm.jusb.util.Recyclable;

/**
 * Abstract class implementing most of the Request interface
 * Use the RequestFactory from the HostManager to create Request objects
 * @author E. Michael Maximilien
 * @author Dan Streetman
 * @version 1.0.0
 */
public abstract class AbstractRequest extends Object implements Request, Recyclable
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	/** 
	 * Creates a new Request object with empty fields
	 * @param factory the RequestFactory object that created this Request
	 */
	public AbstractRequest( RequestFactory factory ) 
	{ 
		requestFactory = factory; 
	}

	//-------------------------------------------------------------------------
	// Abstract public methods
	//

	/** @return the Request code byte for this request */
	public abstract byte getRequest();

	/**
	 * Accepts a RequestVisitor object
	 * @param visitor the RequestVisitor object
	 */
	public abstract void accept( RequestVisitor visitor );

	/**
	 * @return the unique String name of this request.  This name should be the same
	 * for all instances of this Request
	 */
	public abstract String getName();

	//-------------------------------------------------------------------------
	// Public overridden methods
	//

	/**
	 * @return a String representation of this Request.  
	 * This String is the same for all Request object of this type
	 */
	public String toString() { return getName(); }

    //-------------------------------------------------------------------------
    // Public getter methods
	// NOTE: all getter methods come from the USB 1.1 spec definition for the 
	// different standard requests.  See chapter 9.
    //

	/** @return the bmRequestType bitmap byte for this Request */
	public byte getRequestType() { return bmRequestType; }

	/** @return the wValue for this request */
	public short getValue() { return wValue; }

	/** @return the wIndex for this request */
	public short getIndex() { return wIndex; }

	/**
	 * @return the length of the data for this request 
	 * NOTE: this length is calculated from the Data property length (i.e. this is not the total request length)
	 */
	public short getLength() { return (short)data.length; }

	/** @return the length of valid data */
	public int getDataLength() { return dataLength; }

	/** @return the data byte[] for this request */
	public byte[] getData() { return data; }

	//-------------------------------------------------------------------------
	// Public setter methods
	//

	/** 
	 * Sets the wValue for this Request object
	 * @param wValue the short value
	 * @throws javax.usb.UsbRuntimeException if data supplied is invalid for 
	 * the Request wValue
	 */
	public void setValue( short wValue ) throws UsbRuntimeException
	{
		//<temp need to do checks>
		this.wValue = wValue;
		//<temp need to do checks>
	}

	/** 
	 * Sets the wIdex for this Request object
	 * @param wIndex the short index
	 * @throws javax.usb.UsbRuntimeException if data supplied is invalid for 
	 * the Request wIndex
	 */
	public void setIndex( short wIndex ) throws UsbRuntimeException
	{
		//<temp need to do checks>
		this.wIndex = wIndex;
		//<temp need to do checks>
	}

	/**
	 * Sets the valid data length.
	 * @param length the valid data length.
	 */
	public void setDataLength( int length ) { dataLength = length; }

	/** 
	 * Sets the Data array for this Request object
	 * @param data the byte[] data value
	 * @throws javax.usb.UsbRuntimeException if data supplied is invalid for 
	 * the Request Data
	 */
	public void setData( byte[] data ) throws UsbRuntimeException
	{
		checkNull( data );
		//<temp need to do checks>
		this.data = data;
		//<temp need to do checks>
	}

	/**
	 * Sets the bmRequestType bitmap byte for this Request 
	 * @param b the byte argument
	 * @exception javax.usb.UsbRuntimeException if the value could not be set or the argument is incorrect
	 */
	public void setRequestType( byte b ) throws UsbRuntimeException
	{ 
		bmRequestType = b; 
	}

	//-------------------------------------------------------------------------
	// Protected checkXyz methods
	//

	/**
	 * Makes sure that the object passed is not null.  If null throw exception
	 * @exception javax.usb.UsbRuntimeException
	 */										
	protected final void checkNull( Object obj ) throws UsbRuntimeException
	{
		if( obj == null ) throw new UsbRuntimeException( "Null object passed as argument in javax.usb.Request object" );
	}

	/**
	 * Makes sure that the byte passed is a valid descriptor type value according to 
	 * USB 1.1 spec table 9.5
	 * @exception javax.usb.UsbRuntimeException if the value is an invalid decriptor type
	 */										
	protected final void checkDescriptorType( byte b ) throws UsbRuntimeException
	{
		switch( b )
		{
			case DescriptorConst.DESCRIPTOR_TYPE_DEVICE:
			case DescriptorConst.DESCRIPTOR_TYPE_CONFIG:
			case DescriptorConst.DESCRIPTOR_TYPE_STRING:
			case DescriptorConst.DESCRIPTOR_TYPE_INTERFACE:
			case DescriptorConst. DESCRIPTOR_TYPE_ENDPOINT:
				break;

			default:
				throw new UsbRuntimeException( "Invalid descriptor type byte: " + b + " in javax.usb.Request object" );
		}
	}

	/**
	 * Makes sure that the short value passed is a valid language ID according to the 
	 * USB 1.1 spec section 9.6.5
	 * @param s the short value
	 */
	protected final void checkLanguageId( short s )
	{
		//<todo/>
	}

	/**
	 * Initializes the fields for this Request with the passed arguments
	 * @param bmRequestType the bitmap request type
	 * @param wValue the short value request argument
	 * @param wIndex the short index request argument
	 * @param data the byte[] data argument
	 */
	protected void init( byte bmRequestType, short wValue, short wIndex, byte[] data )
	{
		this.bmRequestType = bmRequestType;
		this.wValue = wValue;
		this.wIndex = wIndex;
		this.data = data;
	}

	//-------------------------------------------------------------------------
	// Other public methods
	//

	/**
	 * @return a formated byte[] representing this Request object
	 * @exception javax.usb.UsbRuntimeException if this Request object is invalid
	 */
	public byte[] toBytes() throws UsbRuntimeException
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

	/**
	 * Explicitly tell this Request that it can be recycled.  Clients call this method
	 * to tell the RequestFactory that this request can be recycled
	 * NOTE: typicaly the Request object will indicate that fact to the
	 * RequestFactory so that the same Request object can be used
	 */
	public void recycle() { requestFactory.recycle( this ); }

	/**
	 * Clean this object.
	 * <p>
	 * Perform whatever actions are needed to 'clean' this object
	 * so it is ready to be reused.
	 */
	public void clean()
	{
		bmRequestType = 0x0;   
		wValue = 0x00;        
		wIndex = 0x00;        
		data = new byte[ 0 ];
	}

	//-------------------------------------------------------------------------
	// Instance variables
	//

	protected byte bmRequestType = 0x0;
	protected short wValue = 0x00;
	protected short wIndex = 0x00;					 
	protected byte[] data = new byte[ 0 ];
	protected int dataLength = 0;
	
	protected RequestFactory requestFactory = null;

	//-------------------------------------------------------------------------
	// Class constants
	//

	public static final int REQUEST_HEADER_LENGTH = 8;
}
