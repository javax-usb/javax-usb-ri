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
 * <p>
 * The user must provide some fields:
 * <ul>
 * <li>{@link #getRequestType() bmRequestType} via its {@link #setRequestType(byte) setter}.</li>
 * <li>{@link #getRequestCode() bRequest} via its {@link #setRequestCode(byte) setter}.</li>
 * <li>{@link #getValue() wValue} via its {@link #setValue(short) setter}.</li>
 * <li>{@link #getIndex() wIndex} via its {@link #setIndex(short) setter}.</li>
 * <li>{@link #getData() actual data} via its {@link #setData(byte[]) setter}.</li>
 * </ul>
 * The implementation will first set the
 * {@link #getNumber() number} via its {@link #setNumber(int) setter} and
 * {@link #getUsbDeviceImp() UsbDeviceImp} via its {@link #setUsbDeviceImp(UsbDeviceImp) setter},
 * then process this and set fields depending on the result.
 * If the processing is sucessful, the {@link #getDataLength() data length}
 * is set via its {@link #setDataLength(int) setter}.
 * If the processing is not successful, the {@link #getUsbException() UsbException}
 * is set via its {@link #setUsbException(UsbException) setter}.
 * In either case this is finally {@link #isCompleted() completed}
 * via the {@link #setCompleted(boolean) setter}, which then wakes up all
 * {@link #waitUntilCompleted() waiting Threads}.
 * <p>
 * If the application has passed their own implementation of Request, the UsbDeviceImp will
 * 'wrap' their implementation with this by {@link #setRequest setting} this RequestImp's
 * {@link #getRequest() local Request}.  If the local Request is set when this is
 * {@link #setCompleted(boolean) completed}, this RequestImp's
 * {@link javax.usb.Request#setUsbException(UsbException) UsbException} and
 * {@link javax.usb.Request#setDataLength(int) data length} are copied
 * to the original Request, and the original Request is
 * {@link javax.usb.Request#setCompleted(boolean} completed}.
 * <p>
 * The UsbDeviceImp will handle {@link #setNumber(int) setting the number}.  The
 * UsbDeviceOsImp implementation only needs to set either the
 * {@link #setDataLength(int) data length} or
 * {@link #setUsbException(UsbException) UsbException}, then pass it to the UsbDeviceImp's
 * {@link com.ibm.jusb.UsbDeviceImp#requestImpCompleted(RequestImp) requestImpCompleted} method.
 * No other setting is required by the UsbDeviceOsImp implementation.
 * @author Dan Streetman
 * @author E. Michael Maximilien
 */
public class RequestImp implements Request,UsbOperations.SubmitResult
{
	/** Constructor */
	public RequestImp( RequestImpFactory factory ) { requestImpFactory = factory; }

	/** @return The UsbDevice */
	public UsbDevice getUsbDevice() { return getUsbDeviceImp(); }

	/** @return The UsbDeviceImp */
	public UsbDeviceImp getUsbDeviceImp() { return usbDeviceImp; }

	/** @param device The UsbDeviceImp */
	public void setUsbDeviceImp(UsbDeviceImp device) { usbDeviceImp = device; }

	/** @return the bmRequestType bitmap byte for this Request */
	public byte getRequestType() { return bmRequestType; }

	/** @param type the bmRequestType bitmap byte for this Request */
	public void setRequestType(byte type) { bmRequestType = type; }

	/** @return the Request code byte for this request */
	public byte getRequestCode() { return bRequest; }

	/** @param r the Request code byte for this request */
	public void setRequestCode(byte r) { bRequest = r; }

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
		requestBytes[ 1 ] = getRequestCode();

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
		request = null;
		number = 0;
		usbException = null;
		completed = false;

		/* Shouldn't be any waiters.  If so they lose! */
		waitLock = new Object();
		waitCount = 0;

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
		requestImpFactory.recycle(this);
	}

	/** @return The number */
	public long getNumber() { return number; }

	/** @param num The Number */
	public void setNumber(long num) { number = num; }

	/** @return The UsbException */
	public UsbException getUsbException() { return usbException; }

	/** @param uE The UsbException */
	public void setUsbException( UsbException uE ) { usbException = uE; }

	/** @return If in UsbException */
	public boolean isUsbException() { return null != usbException; }

	/** @return If completed */
	public boolean isCompleted() { return completed; }

	/** Wait (block) until this submission is completed */
	public void waitUntilCompleted() { waitUntilCompleted(0); }

	/** Wait (block) until this submission is completed */
	public void waitUntilCompleted( long msecs )
	{
		synchronized ( waitLock ) {
			waitCount++;
			while (!isCompleted()) {
				try { waitLock.wait( msecs ); }
				catch ( InterruptedException iE ) { }
			}
			waitCount--;
		}
	}

	/**
	 * Get the Request.
	 * <p>
	 * If the user passes a Request that is not a RequestImp, this will
	 * 'wrap' that object so that lower (platform) layers can handle
	 * a standardized object (the RequestImp) with setters instead of
	 * a user-supplied object without setters.  The platform layers always use a
	 * RequestImp, not Request.
	 * @return The Request.
	 */
	public Request getRequest() { return request; }

	/**
	 * Set this as completed.
	 * <p>
	 * Setting this to true performs all required completion activities, such as waking up
	 * {@link #waitUntilCompleted(long) waiting Threads} and (if needed) setting the
	 * {@link #getRequest() Request}'s params.
	 * @param c If completed
	 */
	public void setCompleted(boolean c)
	{
		completed = c;

		if (!isCompleted())
			return;

		Request request = getRequest();

		if (null != request) {
			request.setDataLength(getDataLength());
			request.setUsbException(getUsbException());
			request.setCompleted(true);
		}

		notifyCompleted();
	}

	//**************************************************************************
	// Protected methods

	/** Notify all Threads waiting for completion */
	protected void notifyCompleted()
	{
		if (0 < waitCount) {
			synchronized ( waitLock ) {
				waitLock.notifyAll();
			}
		}
	}

	/**
	 * Set the Request.
	 * @param r The Request.
	 */
	protected void setRequest(Request r) { request = r; }

	//**************************************************************************
	// Instance variables

	private Request request = null;

	private UsbDeviceImp usbDeviceImp = null;

	private byte bmRequestType = 0x00;
	private byte bRequest = 0x00;
	private short wValue = 0x0000;
	private short wIndex = 0x0000;
	private byte[] data = new byte[ 0 ];
	private int dataLength = 0;

	private RequestImpFactory requestImpFactory = null;

	private long number = 0;
	private UsbException usbException = null;
	private boolean completed = false;

	private Object waitLock = new Object();
	private int waitCount = 0;

	//**************************************************************************
	// Class constants

	public static final int REQUEST_HEADER_LENGTH = 8;
}
