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
import javax.usb.util.*;
import javax.usb.os.*;

/**
 * Default implementation for the HubClassOperations interface
 * @author E. Michael Maximilien
 * @version 1.0.0
 */
public class DefaultHubClassOperations extends DefaultClassOperations implements HubClassOperations
{
    //-------------------------------------------------------------------------
    // Ctor(s)
    //

	/**
	 * 1-arg ctor
	 * @param usbDevice the UsbDevice for this operations class
	 * @param classOpsImp the ClassOpsImp object
	 * @exception javax.usb.UsbException if the RequestFactory could not be found
	 */
	public DefaultHubClassOperations( UsbHub usbHub, UsbOpsImp ops ) throws UsbException
	{ 
		super( usbHub, ops ); 
	}

	//-------------------------------------------------------------------------
	// Public methods
	//

	/** @return the UsbHub associated with this HubClassOperations */
	public UsbHub getUsbHub() { return (UsbHub)getUsbDevice(); }

    //-------------------------------------------------------------------------
    // Public operations methods
    //

	/**
	 * Used to disable to clear or disable a specific feature
	 * @param bmRequestType the request type bitmap
	 * @param wValue the feature value selector
	 * @param wIndex the port number (1 based)
	 * @return a Request object that is created for this submission
	 * @exception javax.usb.RequestException if something goes wrong submitting the request for this operation
	 */
	public Request clearFeature( byte bmRequestType, short wValue, short wIndex ) throws RequestException
	{
		Request request = getRequestFactory().
						  createClearFeatureRequest( bmRequestType, wValue, wIndex );

		getOpsImp().syncSubmit( request );

		return request;
	}

	/**
	 * Returns the state of the hub
	 * @param wIndex the port number (1 based)
	 * @param data byte array of size 1 for the port bus state
	 * @return a Request object that is created for this submission
	 * @exception javax.usb.RequestException if something goes wrong submitting the request for this operation
	 */
	public Request getState( short wIndex, byte[] data ) throws RequestException
	{
		Request request = getRequestFactory().
						  createGetStateRequest( wIndex, data );

		getOpsImp().syncSubmit( request );

		return request;
	}

	/**
	 * Returns the specified descriptor if it exists
	 * @param wValue the descriptor type and index
	 * @param wIndex zero or the language ID for String descriptor
	 * @param data a byte array of the correct length to contain the descriptor data bytes
	 * @return a Request object that is created for this submission
	 * @exception javax.usb.RequestException if something goes wrong submitting the request for this operation
	 */
	public Request getDescriptor( short wValue, short wIndex, byte[] data ) throws RequestException
	{
		Request request = getRequestFactory().
						  createGetDescriptorRequest( wValue, wIndex, data );

		getOpsImp().syncSubmit( request );

		return request;
	}

	/**
	 * Returns the status for the specified recipient
	 * @param bmRequestType the request type bitmap
	 * @param wIndex 0 for GetHubStatus and the port number for GetPortStatus
	 * @param data a byte[] of size 4 to contain the hub status
	 * @return a Request object that is created for this submission
	 * @exception javax.usb.RequestException if something goes wrong submitting the request for this operation
	 */
	public Request getStatus( byte bmRequestType, short wIndex, byte[] data ) throws RequestException
	{
		Request request = getRequestFactory().
						  createGetStatusRequest( bmRequestType, wIndex, data );

		getOpsImp().syncSubmit( request );

		return request;
	}

	/**
	 * Update existing descriptor or add new descriptor
	 * @param wValue the descriptor type and index
	 * @param wIndex the language ID if the descriptor is a String descriptor or zero
	 * @return a Request object that is created for this submission
	 * @exception javax.usb.RequestException if something goes wrong submitting the request for this operation
	 */
	public Request setDescriptor( short wValue, short wIndex, byte[] data ) throws RequestException
	{
		Request request = getRequestFactory().
						  createSetDescriptorRequest( wValue, wIndex, data );

		getOpsImp().syncSubmit( request );

		return request;
	}

	/**
	 * Sets or enable a specific feature
	 * @param bmRequestType the request type bitmap
	 * @param wValue the feature selector value
	 * @param wIndex zero or port number (1 based)
	 * @return a Request object that is created for this submission
	 * @exception javax.usb.RequestException if something goes wrong submitting the request for this operation
	 */
	public Request setFeature( byte bmRequestType, short wValue, short wIndex ) throws RequestException
	{
		Request request = getRequestFactory().
						  createSetFeatureRequest( bmRequestType, wValue, wIndex );

		getOpsImp().syncSubmit( request );

		return request;
	}
}
