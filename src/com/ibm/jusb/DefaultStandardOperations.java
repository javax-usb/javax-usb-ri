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
 * Default implementation the interface for all the standard USB operations to a device
 * @author E. Michael Maximilien
 * @version 0.0.1
 */
public class DefaultStandardOperations extends AbstractUsbOperations implements StandardOperations
{
    //-------------------------------------------------------------------------
    // Ctor(s)
    //

	/**
	 * 1-arg ctor
	 * @param usbDevice the UsbDevice for this operations class
	 * @param standardOpsImp the StandardOpsImp object
	 * @exception javax.usb.UsbException if the RequestFactory could not be found
	 */
	public DefaultStandardOperations( UsbDevice usbDevice, StandardOpsImp standardOpsImp  ) throws UsbException
	{ 
		super( usbDevice ); 
		this.standardOpsImp = standardOpsImp;
	}

    //-------------------------------------------------------------------------
    // Public request methods
    //

	/**
	 * Performs a synchronous standard operation by submitting the standard request object
	 * @param request the Request object that is used for this submit
	 * @exception javax.usb.RequestException if something goes wrong sumitting the request for this operation
	 */
	public synchronized void syncSubmit( Request request ) throws RequestException
	{
		getStandardOpsImp().syncSubmit( request );
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
		getStandardOpsImp().syncSubmit( requestBundle );
	}

	/**
	 * Performs an asynchronous standard operation by submitting the standard request object
	 * @param request the Request object that is used for this submit
	 * @exception javax.usb.RequestException if something goes wrong sumitting the request for this operation
	 */
	public UsbOperations.SubmitResult asyncSubmit( Request request ) throws RequestException
	{
		return getStandardOpsImp().asyncSubmit( request );
	}

    //-------------------------------------------------------------------------
    // Public operations methods
	// NOTE: all operations are simplified of all arguments that are not necessary or superfluous
    //

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

                /* Change the "active" flag */
		( (UsbInterfaceAbstraction)usbDevice.getActiveUsbConfig().getUsbInterface( (byte)wIndex ) )
			.setActiveAlternateSettingNumber( (byte)wValue );

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

	//-------------------------------------------------------------------------
	// Protected methods
	//

	/** @return the StandardOpsImp object */
	protected StandardOpsImp getStandardOpsImp() { return standardOpsImp; }

	//-------------------------------------------------------------------------
	// Instance variables
	//

	private StandardOpsImp standardOpsImp = null;
}
