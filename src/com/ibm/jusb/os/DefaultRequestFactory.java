package com.ibm.jusb.os;

/*
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import java.util.*;

import javax.usb.*;
import javax.usb.util.DefaultRequestBundle;

import com.ibm.jusb.util.DefaultRequestV;

/**
 * Default implementation of the RequestFactory interface to create Request objects
 * @author E. Michael Maximilien
 * @version 1.0.0
 */
public class DefaultRequestFactory extends Object implements RequestFactory
{
    //-------------------------------------------------------------------------
    // Ctor(s)
    //

	DefaultRequestFactory() {}

    //-------------------------------------------------------------------------
    // Public methods - create a RequestBundle
    //

	/**
	 * @return a RequestBundle object that is used to aggregate and submit 
	 * vendor or class specific requests (standard request cannot be bundled)
	 */
	public RequestBundle createRequestBundle()
	{
		if( freeBundleList.isEmpty() == false )
			return (RequestBundle)freeBundleList.remove( 0 );

		return new DefaultRequestFactory.MyRequestBundle( this );
	}

	//-------------------------------------------------------------------------
	// Public methods
	//

	/**
	 * Indicates to the RequestFactory object that the Request object can be recycled
	 * That is can be reused again.
	 * @param request the Request object to recycle
	 */
	public void recycle( Request request )
	{
		request.clean();
		addToFreeMap( request );
	}

	/**
	 * Indicates to the RequestFactory object that the RequestBundle object can be recycled
	 * That is can be reused again.
	 * @param requestBundle the RequestBundle object to recycle
	 */
	public void recycle( RequestBundle requestBundle )
	{
		requestBundle.clean();
		freeBundleList.add( requestBundle );
	}

    //-------------------------------------------------------------------------
    // Public methods - create methods for Vendor Request
    //

	/**
	 * @return a Vendor Request
	 * @param bmRequestType the request type bitmap
	 * @param requestType the request type value
	 * @param wValue the word feature selector value
	 * @param wIndex the index value
	 * @param data the Data byte[]
	 * @exception javax.usb.RequestException if the value to create this Request object is not valid
	 */
	public Request createVendorRequest( byte bmRequestType, byte requestType,
										short wValue, short wIndex, byte[] data ) throws RequestException
	{
        VendorRequest request = null;

		if( isAvailableRequestInFreeMap( VendorRequest.VENDOR_REQUEST_NAME ) )
			request = (VendorRequest)getNextAvailableRequestInFreeMap( VendorRequest.VENDOR_REQUEST_NAME );
		else
            request = new VendorRequest( this, requestType );

		request.setRequestType( bmRequestType );
		request.setValue( wValue );
		request.setIndex( wIndex );
		request.setData( data );

		return request;
    }
	
	//-------------------------------------------------------------------------
    // Public methods - create methods for Class Request
    //

	/**
	 * @return a Class Request (this also includes hub class requests)
	 * @param bmRequestType the request type bitmap
	 * @param requestType the request type value
	 * @param wValue the word feature selector value
	 * @param wIndex the index value
	 * @param data the Data byte[]
	 * @exception javax.usb.RequestException if the value to create this Request object is not valid
	 */
	public Request createClassRequest( byte bmRequestType, byte requestType,
							     	   short wValue, short wIndex, byte[] data ) throws RequestException
	{
        ClassRequest request = null;

		if( isAvailableRequestInFreeMap( ClassRequest.CLASS_REQUEST_NAME ) )
			request = (ClassRequest)getNextAvailableRequestInFreeMap( ClassRequest.CLASS_REQUEST_NAME );
		else
            request = new ClassRequest( this, requestType );
        
		request.setRequestType( bmRequestType );
		request.setValue( wValue );
		request.setIndex( wIndex );
		request.setData( data );

		return request;
    }

    //-------------------------------------------------------------------------
    // Public methods - create methods for Standard Request
    //

	/**
	 * @return a CLEAR_FEATURE Request object
	 * Used to disable to clear or disable a specific feature
	 * See USB 1.1 spec section 9.4.1
	 * @param bmRequestType the request type bitmap
	 * @param wValue the word feature selector value
	 * @param wIndex Zero or Interface or Endpoint index
	 * @exception javax.usb.RequestException if the value to create this Request object is not valid
	 */
	public Request createClearFeatureRequest( byte bmRequestType, short wValue, short wIndex ) throws RequestException
	{
        ClearFeatureRequest request = null;

		if( isAvailableRequestInFreeMap( ClearFeatureRequest.CLEAR_FEATURE_REQUEST_NAME ) )
			request = (ClearFeatureRequest)getNextAvailableRequestInFreeMap( ClearFeatureRequest.CLEAR_FEATURE_REQUEST_NAME );
		else
            request = new ClearFeatureRequest( this );

		request.setRequestType( bmRequestType );
		request.setValue( wValue );
		request.setIndex( wIndex );

		return request;
	}

	/**
	 * @return a GET_CONFIGURATION Request object
	 * Returns the current device configuration value
	 * See USB 1.1 spec section 9.4.2
	 * @param data a byte array of 1 to contain the configuration value
	 * @exception javax.usb.RequestException if the value to create this Request object is not valid
	 */
	public Request createGetConfigurationRequest( byte[] data ) throws RequestException
	{
        GetConfigurationRequest request = null;

		if( isAvailableRequestInFreeMap( GetConfigurationRequest.GET_CONFIGURATION_REQUEST_NAME ) )
			request = (GetConfigurationRequest)getNextAvailableRequestInFreeMap( GetConfigurationRequest.GET_CONFIGURATION_REQUEST_NAME );
		else
            request = new GetConfigurationRequest( this );

		request.setData( data );

		return request;
    }

	/**
	 * @return a GET_DESCRIPTOR Request object
	 * Returns the specified descriptor if it exists
	 * See USB 1.1 spec section 9.4.3
	 * @param wValue the descriptor type and index
	 * @param data a byte array of the correct length to contain the descriptor data bytes
	 * @exception javax.usb.RequestException if the value to create this Request object is not valid
	 */
	public Request createGetDescriptorRequest( short wValue, short wIndex, byte[] data ) throws RequestException
	{
        GetDescriptorRequest request = null;

		if( isAvailableRequestInFreeMap( GetDescriptorRequest.GET_DESCRIPTOR_REQUEST_NAME ) )
			request = (GetDescriptorRequest)getNextAvailableRequestInFreeMap( GetDescriptorRequest.GET_DESCRIPTOR_REQUEST_NAME );
		else
            request = new GetDescriptorRequest( this );

		request.setValue( wValue );
		request.setIndex( wIndex );
		request.setData( data );

		return request;
	}

	/**
	 * @return a GET_INTERFACE Request object
	 * Returns the alternate setting for the specified interface
	 * See USB 1.1 spec section 9.4.4
	 * @param wIndex the interface index
	 * @param data a byte array of size 1 to contain the alternate setting value
	 * @exception javax.usb.RequestException if the value to create this Request object is not valid
	 */
	public Request createGetInterfaceRequest( short wIndex, byte[] data ) throws RequestException
	{
        GetInterfaceRequest request = null;

		if( isAvailableRequestInFreeMap( GetInterfaceRequest.GET_INTERFACE_REQUEST_NAME ) )
			request = (GetInterfaceRequest)getNextAvailableRequestInFreeMap( GetInterfaceRequest.GET_INTERFACE_REQUEST_NAME );
		else
            request = new GetInterfaceRequest( this );

		request.setIndex( wIndex );
		request.setData( data );

		return request;
	}

	/**
	 * @return a GET_STATUS Request object
	 * Returns the status for the specified recipient
	 * See USB 1.1 spec section 9.4.5
	 * @param bmRequestType the request type bitmap
	 * @param wIndex zero for device status request OR interface or endpoint index
	 * @param data a byte array of size 2 to contain the device, interface or endpoint status
	 * @exception javax.usb.RequestException if the value to create this Request object is not valid
	 */
	public Request createGetStatusRequest( byte bmRequestType, short wIndex, byte[] data ) throws RequestException
	{
        GetStatusRequest request = null;

		if( isAvailableRequestInFreeMap( GetStatusRequest.GET_STATUS_REQUEST_NAME ) )
			request = (GetStatusRequest)getNextAvailableRequestInFreeMap( GetStatusRequest.GET_STATUS_REQUEST_NAME );
		else
            request = new GetStatusRequest( this );

		request.setRequestType( bmRequestType );
		request.setIndex( wIndex );
		request.setData( data );

		return request;
    }

	/**
	 * @return a SET_ADDRESS Request object
	 * Sets the device address for all future device accesses
	 * See USB 1.1 spec section 9.4.6
	 * @param wValue the device address
	 * @exception javax.usb.RequestException if the value to create this Request object is not valid
	 */
	public Request createSetAddressRequest( short wValue ) throws RequestException
	{
        SetAddressRequest request = null;

		if( isAvailableRequestInFreeMap( SetAddressRequest.SET_ADDRESS_REQUEST_NAME ) )
			request = (SetAddressRequest)getNextAvailableRequestInFreeMap( SetAddressRequest.SET_ADDRESS_REQUEST_NAME );
		else
            request = new SetAddressRequest( this );

		request.setValue( wValue );

		return request;
	}

	/**
	 * @return a SET_CONFIGURATION Request object
	 * Sets the device configuration
	 * See USB 1.1 spec section 9.4.7
	 * @param wValue the configuration value
	 * @exception javax.usb.RequestException if the value to create this Request object is not valid
	 */
	public Request createSetConfigurationRequest( short wValue ) throws RequestException
	{
        SetConfigurationRequest request = null;

		if( isAvailableRequestInFreeMap( SetConfigurationRequest.SET_CONFIGURATION_REQUEST_NAME ) )
			request = (SetConfigurationRequest)getNextAvailableRequestInFreeMap( SetConfigurationRequest.SET_CONFIGURATION_REQUEST_NAME );
		else
            request = new SetConfigurationRequest( this );

		request.setValue( wValue );

		return request;
	}

	/**
	 * @return a SET_DESCRIPTOR Request object
	 * Update existing descriptor or add new descriptor
	 * See USB 1.1 spec section 9.4.8
	 * @param wValue the descriptor type and index
	 * @param wIndex the language ID if the descriptor is a String descriptor or zero
	 * @param data a byte array of the correct length to contain the descriptor data bytes
	 * @exception javax.usb.RequestException if the value to create this Request object is not valid
	 */
	public Request createSetDescriptorRequest( short wValue, short wIndex, byte[] data ) throws RequestException
	{
        SetDescriptorRequest request = null;

		if( isAvailableRequestInFreeMap( SetDescriptorRequest.SET_DESCRIPTOR_REQUEST_NAME ) )
			request = (SetDescriptorRequest)getNextAvailableRequestInFreeMap( SetDescriptorRequest.SET_DESCRIPTOR_REQUEST_NAME );
		else
            request = new SetDescriptorRequest( this );

		request.setValue( wValue );
		request.setIndex( wIndex );
		request.setData( data );

		return request;
	}

	/**
	 * @return a SET_FEATURE Request object
	 * Sets or enable a specific feature
	 * See USB 1.1 spec section 9.4.8
	 * @param bmRequestType the request type bitmap
	 * @param wValue the feature selector value
	 * @param wIndex zero for device feature OR interface or endpoint feature value
	 * @exception javax.usb.RequestException if the value to create this Request object is not valid
	 */
	public Request createSetFeatureRequest( byte bmRequestType, short wValue, short wIndex ) throws RequestException
	{
        SetFeatureRequest request = null;

		if( isAvailableRequestInFreeMap( SetFeatureRequest.SET_FEATURE_REQUEST_NAME ) )
			request = (SetFeatureRequest)getNextAvailableRequestInFreeMap( SetFeatureRequest.SET_FEATURE_REQUEST_NAME );
		else
            request = new SetFeatureRequest( this );

		request.setRequestType( bmRequestType );
		request.setValue( wValue );
		request.setIndex( wIndex );

		return request;
	}

	/**
	 * @return a SET_INTERFACE Request object
	 * Allows the host to select an alternate setting for the specified interface
 	 * See USB 1.1 spec section 9.4.9
	 * @param wIndex the interface number
	 * @param wValue the alternate setting value
	 * @exception javax.usb.RequestException if the value to create this Request object is not valid
	 */
	public Request createSetInterfaceRequest( short wIndex, short wValue ) throws RequestException
	{
        SetInterfaceRequest request = null;

		if( isAvailableRequestInFreeMap( SetInterfaceRequest.SET_INTERFACE_REQUEST_NAME ) )
			request = (SetInterfaceRequest)getNextAvailableRequestInFreeMap( SetInterfaceRequest.SET_INTERFACE_REQUEST_NAME );
		else
            request = new SetInterfaceRequest( this );

		request.setIndex( wIndex );
		request.setValue( wValue );

		return request;
	}

	/**
	 * @return a SYNCH_FRAME Request object
	 * Sets and then report an endpoint's synchronization frame
	 * See USB 1.1 spec section 9.4.10
	 * @param wIndex the endpoint number
	 * @param data a byte array of size 2 to contain the frame number
	 * @exception javax.usb.RequestException if the value to create this Request object is not valid
	 */
	public Request createSynchFrameRequest( short wIndex, byte[] data ) throws RequestException
	{
        SynchFrameRequest request = null;

		if( isAvailableRequestInFreeMap( SynchFrameRequest.SYNCH_FRAME_REQUEST_NAME ) )
			request = (SynchFrameRequest)getNextAvailableRequestInFreeMap( SynchFrameRequest.SYNCH_FRAME_REQUEST_NAME );
		else
            request = new SynchFrameRequest( this );

		request.setIndex( wIndex );
		request.setData( data );

		return request;
	}

	//-------------------------------------------------------------------------
    // Public methods - additional create methods for Hub class request
    //

	/**
	 * Querries and returns the state of the hub
	 * @return a GET_STATE Request object 
	 * @param wIndex
	 * @param data byte array of size 1 for the port bus state
	 * @exception javax.usb.RequestException if the value to create this Request object is not valid
	 */
	public Request createGetStateRequest( short wIndex, byte[] data ) throws RequestException
	{
        GetStateRequest request = null;

		if( isAvailableRequestInFreeMap( GetStateRequest.GET_STATE_REQUEST_NAME ) )
			request = (GetStateRequest)getNextAvailableRequestInFreeMap( GetStateRequest.GET_STATE_REQUEST_NAME );
		else
            request = new GetStateRequest( this );

		request.setIndex( wIndex );
		request.setData( data );

		return request;
	}

	//-------------------------------------------------------------------------
	// Protected methods
	//

	/**
	 * Adds the Request to the free map
	 * @param request the Request object
	 */
	protected void addToFreeMap( Request request )
	{
		if( freeRequestMap.containsKey( request.toString() ) )
		{
			List freeRequestList = (List)freeRequestMap.get( request.toString() );
			freeRequestList.add( request );
		}
		else
		{
			List freeRequestList = new ArrayList();
			freeRequestMap.put( request.toString(), freeRequestList );
			freeRequestList.add( request );
		}
	}

	/**
	 * @return true if there are any available free Request object from the
	 * freeRequestMap
	 * @param requestName the Request name (unique per Request type)
	 */
	protected boolean isAvailableRequestInFreeMap( String requestName )
	{
		if( freeRequestMap.containsKey( requestName ) )
			return ( (List)freeRequestMap.get( requestName ) ).size() > 0;

		return false;
	}

	/**
	 * @return the next available Request from the freeRequestMap
	 * @param requestName the Request name (unique per Request type)
	 */
	protected Request getNextAvailableRequestInFreeMap( String requestName )
	{
		List requestList = null;

		if( freeRequestMap.containsKey( requestName ) )
			requestList = (List)freeRequestMap.get( requestName );
		else
			throw new RuntimeException( "Could not get next available Request object!" );

		return (Request)requestList.get( 0 );
	}

	//-------------------------------------------------------------------------
	// Instance variables
	//

	private Map freeRequestMap = new HashMap();
	private List freeBundleList = new ArrayList();

	//-------------------------------------------------------------------------
	// Inner classes
	//

	/**
	 * Simple inner class overridding DefaultRequestBundle to allow clients
	 * to set the in submission flag
	 * @author E. Michael Maximilien
	 * @version 1.0.0
	 */
	public static class MyRequestBundle extends DefaultRequestBundle
	{
		//---------------------------------------------------------------------
		// Ctor
		//

		/**
		 * Takes the RequestFactory that created it
		 * @param factory the RequestFactory that created this bundle
		 */
		public MyRequestBundle( RequestFactory factory )
		{									  
			requestFactory = factory;
		}

		//---------------------------------------------------------------------
		// Public methods
		//

		/** 
		 * Recycles this bundle so it may be returned when clients call ask the
		 * RequestFactory to create a new bundle.  Should be called once client is
		 * done using this bundle.
		 * @see javax.usb.RequestFactory
		 */
		public synchronized void recycle() { requestFactory.recycle( this ); }

		/**
		 * Marks this RequestBundle as beeing in submission
		 * @param b the boolean parameter
		 */
		public synchronized void setInSubmission( boolean b ) { this.inSubmission = b; }

		//---------------------------------------------------------------------
		// Instance variables
		//

		private RequestFactory requestFactory = null;
	}
}
