package com.ibm.jusb;

/**
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
 * Implementation of UsbOperations.
 * @author E. Michael Maximilien
 * @author Dan Streetman
 */
public class UsbOperationsImp implements StandardOperations,VendorOperations,ClassOperations
{
	public UsbOperationsImp( UsbDeviceImp devImp )
	{ 
		usbDeviceImp = devImp;
	}

    //-------------------------------------------------------------------------
    // Public methods
    //

	/** @return the UsbDevice */
	public UsbDevice getUsbDevice() { return getUsbDeviceImp(); }

	/** @return The UsbDeviceImp */
	public UsbDeviceImp getUsbDeviceImp() { return usbDeviceImp; }

	/**
	 * Performs a synchronous standard operation by submitting the standard request object
	 * @param request the Request object that is used for this submit
	 * @exception javax.usb.RequestException if something goes wrong sumitting the request for this operation
	 */
	public synchronized void syncSubmit( Request request ) throws RequestException
	{
		getUsbDeviceImp().syncSubmit( request );
	}

	/**
	 * Performs a synchronous operation by submitting all the Request objects in the bundle.
	 * No other request submission can be overlapped.  This means that the Request object in the
	 * bundle are guaranteed to be sent w/o interruption.
	 * @param requestBundle the RequestBundle object that is used for this submit
	 * @exception javax.usb.RequestException if something goes wrong submitting the request for this operation
	 */
	public synchronized void syncSubmit( RequestBundle requestBundle ) throws RequestException
	{
		getUsbDeviceImp().syncSubmit( requestBundle );
	}

	/**
	 * Performs an asynchronous standard operation by submitting the standard request object
	 * @param request the Request object that is used for this submit
	 * @exception javax.usb.RequestException if something goes wrong sumitting the request for this operation
	 */
	public UsbOperations.SubmitResult asyncSubmit( Request request ) throws RequestException
	{
		return getUsbDeviceImp().asyncSubmit( request );
	}

	//**************************************************************************
	// Standard Operations

	/**
	 * Used to disable to clear or disable a specific feature
	 * See USB 1.1 spec section 9.4.1
	 * @param bmRequestType the request type bitmap
	 * @param wValue the word feature selector value
	 * @param wIndex Zero or Interface or Endpoint index
	 * @return a Request object that is created for this submission
	 * @exception javax.usb.RequestException if something goes wrong sumitting the request for this operation
	 */
	public Request clearFeature( byte bmRequestType, short wValue, short wIndex ) throws RequestException
	{
		Request request = getRequestFactory().createClearFeatureRequest( bmRequestType, wValue, wIndex );

		syncSubmit( request );

		return request;
	}

	/**
	 * Returns the current device configuration value
	 * See USB 1.1 spec section 9.4.2
	 * @param data a byte array of 1 to contain the configuration value
	 * @return a Request object that is created for this submission
	 * @exception javax.usb.RequestException if something goes wrong sumitting the request for this operation
	 */
	public Request getConfiguration( byte[] data ) throws RequestException
	{
		Request request = getRequestFactory().createGetConfigurationRequest( data );

		syncSubmit( request );

		return request;
	}

	/**
	 * Returns the specified descriptor if it exists
	 * See USB 1.1 spec section 9.4.3
	 * @param wValue the descriptor type and index
	 * @param data a byte array of the correct length to contain the descriptor data bytes
	 * @return a Request object that is created for this submission
	 * @exception javax.usb.RequestException if something goes wrong sumitting the request for this operation
	 */
	public Request getDescriptor( short wValue, short wIndex, byte[] data ) throws RequestException
	{
		Request request = getRequestFactory().createGetDescriptorRequest( wValue, wIndex, data );

		syncSubmit( request );

		return request;
	}

	/**
	 * Returns the alternate setting for the specified interface
	 * See USB 1.1 spec section 9.4.4
	 * @param wIndex the interface index
	 * @param data a byte array of size 1 to contain the alternate setting value
	 * @return a Request object that is created for this submission
	 * @exception javax.usb.RequestException if something goes wrong sumitting the request for this operation
	 */
	public Request getInterface( short wIndex, byte[] data ) throws RequestException
	{
		Request request = getRequestFactory().createGetInterfaceRequest( wIndex, data );

		syncSubmit( request );

		return request;
	}

	/**
	 * Returns the status for the specified recipient
	 * See USB 1.1 spec section 9.4.5
	 * @param bmRequestType the request type bitmap
	 * @param wIndex zero for device status request OR interface or endpoint index
	 * @param data a byte array of size 2 to contain the device, interface or endpoint status
	 * @return a Request object that is created for this submission
	 * @exception javax.usb.RequestException if something goes wrong sumitting the request for this operation
	 */
	public Request getStatus( byte bmRequestType, short wIndex, byte[] data ) throws RequestException
	{
		Request request = getRequestFactory().createGetStatusRequest( bmRequestType, wIndex, data ); 

		syncSubmit( request );

		return request;
	}

	/**
	 * Sets the device address for all future device accesses
	 * See USB 1.1 spec section 9.4.6
	 * @param wValue the device address
	 * @return a Request object that is created for this submission
	 * @exception javax.usb.RequestException if something goes wrong sumitting the request for this operation
	 */
	public Request setAddress( short wValue ) throws RequestException
	{
		Request request = getRequestFactory().createSetAddressRequest( wValue );

		syncSubmit( request );

		return request;
	}

	/**
	 * Sets the device configuration
	 * See USB 1.1 spec section 9.4.7
	 * @param wValue the configuration value
	 * @return a Request object that is created for this submission
	 * @exception javax.usb.RequestException if something goes wrong sumitting the request for this operation
	 */
	public Request setConfiguration( short wValue ) throws RequestException
	{
		Request request = getRequestFactory().createSetConfigurationRequest( wValue );

		syncSubmit( request );

		getUsbDeviceImp().setActiveUsbConfigNumber( (byte)wValue );

		return request;
	}

	/**
	 * Update existing descriptor or add new descriptor
	 * See USB 1.1 spec section 9.4.8
	 * @param wValue the descriptor type and index
	 * @param wIndex the language ID if the descriptor is a String descriptor or zero
	 * @return a Request object that is created for this submission
	 * @exception javax.usb.RequestException if something goes wrong sumitting the request for this operation
	 */
	public Request setDescriptor( short wValue, short wIndex, byte[] data ) throws RequestException
	{
		Request request = getRequestFactory().createSetDescriptorRequest( wValue, wIndex, data ); 

		syncSubmit( request );

		return request;
	}

	/**
	 * Sets or enable a specific feature
	 * See USB 1.1 spec section 9.4.8
	 * @param bmRequestType the request type bitmap
	 * @param wValue the feature selector value
	 * @param wIndex zero for device feature OR interface or endpoint feature value
	 * @return a Request object that is created for this submission
	 * @exception javax.usb.RequestException if something goes wrong sumitting the request for this operation
	 */
	public Request setFeature( byte bmRequestType, short wValue, short wIndex ) throws RequestException
	{
		Request request = getRequestFactory().createSetFeatureRequest( bmRequestType, wValue, wIndex );

		syncSubmit( request );

		return request;
	}

	/**
	 * Allows the host to select an alternate setting for the specified interface
 	 * See USB 1.1 spec section 9.4.9
	 * @param wIndex the interface number
	 * @param wValue the alternate setting value
	 * @return a Request object that is created for this submission
	 * @exception javax.usb.RequestException if something goes wrong sumitting the request for this operation
	 */
	public Request setInterface( short wIndex, short wValue ) throws RequestException
	{
		Request request = getRequestFactory().createSetInterfaceRequest( wIndex, wValue );

		syncSubmit( request );

		getUsbDeviceImp().getActiveUsbConfigImp().getUsbInterfaceImp( (byte)wIndex ).setActiveAlternateSettingNumber( (byte)wValue );

		return request;
	}

	/**
	 * Sets and then report an endpoint's synchronization frame
	 * See USB 1.1 spec section 9.4.10
	 * @param wIndex the endpoint index
	 * @param data a byte array of size 2 to contain the frame number
	 * @return a Request object that is created for this submission
	 * @exception javax.usb.RequestException if something goes wrong sumitting the request for this operation
	 */
	public Request synchFrame( short wIndex, byte[] data ) throws RequestException
	{
		Request request = getRequestFactory().createSynchFrameRequest( wIndex, data );

		syncSubmit( request );

		return request;
	}

	//**************************************************************************
	// VendorOperations

	/**
	 * Used to submit any vendor request.  Note that the bmRequestType field bits 6..5
	 * must be set to 0x02 for Vendor type according to the USB 1.1. specification
	 * @param bmRequestType the request type bitmap
	 * @param requestType the specific request type
	 * @param wValue the word feature selector value
	 * @param wIndex Zero or Interface or Endpoint index
	 * @param data a byte array for the request Data
	 * @return a Request object that is created for this submission
	 * @exception javax.usb.RequestException if something goes wrong submitting the request for this operation
	 */
	public Request vendorRequest( byte bmRequestType, byte requestType, short wValue, short wIndex, byte[] data ) throws RequestException
	{
		Request vendorRequest = getRequestFactory().createVendorRequest( bmRequestType, requestType, wValue, wIndex, data );

		syncSubmit( vendorRequest );

		return vendorRequest;
	}

	//**************************************************************************
	// Class Operations

	/**
	 * Used to submit any class request.  Note that the bmRequestType field bits 6..5
	 * must be set to 0x01 for Class type according to the USB 1.1. specification
	 * @param bmRequestType the request type bitmap
	 * @param requestType the specific request type
	 * @param wValue the word feature selector value
	 * @param wIndex Zero or Interface or Endpoint index
	 * @param data a byte array for the request Data
	 * @return a Request object that is created for this submission
	 * @exception javax.usb.RequestException if something goes wrong submitting the request for this operation
	 */
	public Request classRequest( byte bmRequestType, byte requestType, short wValue, short wIndex, byte[] data ) throws RequestException
	{
		Request classRequest = getRequestFactory().createClassRequest( bmRequestType, requestType, wValue, wIndex, data );

		syncSubmit( classRequest );

		return classRequest;
	}

	//**************************************************************************
	// HubClassOperations

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
		Request request = getRequestFactory().createClearFeatureRequest( bmRequestType, wValue, wIndex );

		syncSubmit( request );

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
		Request request = getRequestFactory().createGetStateRequest( wIndex, data );

		syncSubmit( request );

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
		Request request = getRequestFactory().createGetDescriptorRequest( wValue, wIndex, data );

		syncSubmit( request );

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
		Request request = getRequestFactory().createGetStatusRequest( bmRequestType, wIndex, data );

		syncSubmit( request );

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
		Request request = getRequestFactory().createSetDescriptorRequest( wValue, wIndex, data );

		syncSubmit( request );

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
		Request request = getRequestFactory().createSetFeatureRequest( bmRequestType, wValue, wIndex );

		syncSubmit( request );

		return request;
	}

	//**************************************************************************
	// Protected methods

	/** @return A RequestFactory */
	protected RequestFactory getRequestFactory() { return requestFactory; }

	//**************************************************************************
	// Instance variables

	protected UsbDeviceImp usbDeviceImp = null;
	protected RequestFactory requestFactory = new DefaultRequestFactory.java

}
