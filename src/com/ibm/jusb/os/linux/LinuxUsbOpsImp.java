package com.ibm.jusb.os.linux;

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

import com.ibm.jusb.os.UsbOpsImp;
import com.ibm.jusb.os.AbstractRequest;
import com.ibm.jusb.os.DefaultRequestFactory;
import com.ibm.jusb.util.DefaultRequestV;

/**
 * Abstract superclass for all UsbOpsImp interface and sub-interface implementation
 * @author E. Michael Maximilien
 * @author Dan Streetman
 * @version 1.0.0
 */
class LinuxUsbOpsImp extends Object implements UsbOpsImp
{
	/**
	 * Constructor
	 * @param deviceImp the associated LinuxDeviceImp.
	 */
	public LinuxUsbOpsImp( LinuxDeviceImp deviceImp ) 
	{
        linuxDeviceImp = deviceImp;
	}

	//-------------------------------------------------------------------------
	// Public methods
	//

	/**
	 * Performs a synchronous standard operation by submitting the standard request object
	 * @param request the Request object that is used for this submit
	 * @throws javax.usb.RequestException if something went wrong submitting the request
	 */
	public void syncSubmit( Request request ) throws RequestException
	{
		//<temp>Need to create a UsbRequest interface or something to make this cast cleaner</temp>
        ( (AbstractRequest)request ).accept( syncSubmitDcpRequestV );

		if( syncSubmitDcpRequestV.isInException() )
			throw syncSubmitDcpRequestV.getRequestException();
	}

	/**
	 * Performs a synchronous operation by submitting all the Request objects in the bundle.
	 * No other request submission can be overlapped.  This means that the Request object in the
	 * bundle are guaranteed to be sent w/o interruption.
	 * @param requestBundle the RequestBundle object that is used for this submit
	 * @exception javax.usb.RequestException if something goes wrong submitting the request for this operation
	 */
	public void syncSubmit( RequestBundle requestBundle ) throws RequestException
	{
		( (DefaultRequestFactory.MyRequestBundle)requestBundle ).setInSubmission( true );
		
		RequestIterator requestIterator = requestBundle.requestIterator();
		
		try
		{
			while( requestIterator.hasNext() )
				syncSubmit( requestIterator.nextRequest() );
		}
		catch( RequestException re ) { throw re; }
		finally{ ( (DefaultRequestFactory.MyRequestBundle)requestBundle ).setInSubmission( false ); }
	}

	/**
	 * Performs an asynchronous standard operation by submitting the standard request object
	 * @param request the Request object that is used for this submit
	 * @exception javax.usb.RequestException if something goes wrong sumitting the request for this operation
	 */
	public UsbOperations.SubmitResult asyncSubmit( Request request ) throws RequestException
	{
		//<temp>
		throw new RuntimeException( "Not yet implemented.  Will be in v1.0.0-beta3" );
		//</temp>
	}

	//-------------------------------------------------------------------------
	// Protected methods
	//

	/**
	 * Does a sync submission of a DCP request that is not a SetConfiguration or SetInterface
	 * request.  These need to be done in Linux with a different IOCTL and thus exposed
	 * as a separate LinuxSetConfigurationRequest and LinuxSetInterfaceRequest
	 * @param request the Request object to submit
	 * @exception javax.usb.RequestException if something went wrong while transmitting
	 */
	protected void syncSubmitDcpRequest( Request request ) throws RequestException
	{
		LinuxDcpRequest dcpRequest = (LinuxDcpRequest)getLinuxDcpRequestFactory().takeLinuxRequest();
		byte[] bytes = request.toBytes();
		byte[] requestData = request.getData();

		dcpRequest.setData( bytes );

		try
		{
			getLinuxDeviceImp().getLinuxDeviceProxy().submitRequest( dcpRequest );
		}
		catch( UsbException ue ) { throw new RequestException( "Error submitting request!", ue ); }

		dcpRequest.waitUntilRequestCompleted();

		if( dcpRequest.getCompletionStatus() < 0 ) 
		{
			int error = dcpRequest.getCompletionStatus();
			dcpRequest.recycle();
			throw new RequestException( JavaxUsb.nativeGetErrorMessage( error ), error );
		} 
		else 
		{
			int xferred = dcpRequest.getCompletionStatus() - 8; /* subtract 8 for setup packet */
			if (xferred > requestData.length) /* This should not happen; should something be done? */
				xferred = requestData.length;
			if ((0 < xferred))
				System.arraycopy(bytes, 8, requestData, 0, xferred);

//FIXME - abstraction layer should be passing down settable Request (something like AbstractRequest)
			((AbstractRequest)request).setDataLength(xferred);
//FIXME
		}

		dcpRequest.recycle();
	}

	/**
	 * Does a sync submission of a DCP request for a SetConfiguration
	 * @param request the Request object to submit
	 * @exception javax.usb.RequestException if something went wrong while transmitting
	 */
	protected void syncSubmitDcpSetConfigurationRequest( Request request ) throws RequestException
	{
		LinuxSetConfigurationRequest setConfigRequest = (LinuxSetConfigurationRequest)getLinuxSetConfigurationRequestFactory().takeLinuxRequest();

		setConfigRequest.setConfiguration( (byte)request.getValue() );

		try
		{
			getLinuxDeviceImp().getLinuxDeviceProxy().submitRequest( setConfigRequest );
		}
		catch( UsbException ue ) { throw new RequestException( "Error submitting request!", ue ); }

		setConfigRequest.waitUntilRequestCompleted();

		if( setConfigRequest.getCompletionStatus() < 0 ) 
		{
			int error = setConfigRequest.getCompletionStatus();
			setConfigRequest.recycle();
			throw new RequestException( JavaxUsb.nativeGetErrorMessage( error ), error );
		} 
	}

	/**
	 * Does a sync submission of a DCP request for a SetConfiguration
	 * @param request the Request object to submit
	 * @exception javax.usb.RequestException if something went wrong while transmitting
	 */
	protected void syncSubmitDcpSetInterfaceRequest( Request request ) throws RequestException
	{
		LinuxSetInterfaceRequest setInterfaceRequest = (LinuxSetInterfaceRequest)getLinuxSetInterfaceRequestFactory().takeLinuxRequest();

		setInterfaceRequest.setInterface( (byte)request.getIndex() );
		setInterfaceRequest.setSetting( (byte)request.getValue() );

		try
		{
			getLinuxDeviceImp().getLinuxDeviceProxy().submitRequest( setInterfaceRequest );
		}
		catch( UsbException ue ) { throw new RequestException( "Error submitting request!", ue ); }

		setInterfaceRequest.waitUntilRequestCompleted();

		if( setInterfaceRequest.getCompletionStatus() < 0 ) 
		{
			int error = setInterfaceRequest.getCompletionStatus();
			setInterfaceRequest.recycle();
			throw new RequestException( JavaxUsb.nativeGetErrorMessage( error ), error );
		} 
	}

	/**
	 * Get the LinuxDeviceImp
	 * @return the LinuxDeviceImp
	 */
	protected LinuxDeviceImp getLinuxDeviceImp() { return linuxDeviceImp; }

	/** Get a LinuxRequestFactory for LinuxDcpRequests */
	protected LinuxRequestFactory getLinuxDcpRequestFactory()
	{
		return LinuxUsbServices.getLinuxInstance().getLinuxHelper().getLinuxDcpRequestFactory();
	}

	/** Get a LinuxRequestFactory for LinuxSetInterfaceRequests */
	protected LinuxRequestFactory getLinuxSetInterfaceRequestFactory()
	{
		return LinuxUsbServices.getLinuxInstance().getLinuxHelper().getLinuxSetInterfaceRequestFactory();
	}

	/** Get a LinuxRequestFactory for LinuxSetConfigurationRequests */
	protected LinuxRequestFactory getLinuxSetConfigurationRequestFactory()
	{
		return LinuxUsbServices.getLinuxInstance().getLinuxHelper().getLinuxSetConfigurationRequestFactory();
	}

	//-------------------------------------------------------------------------
	// Instance variables
	//

	private LinuxDeviceImp linuxDeviceImp = null;

	private SyncSubmitDcpRequestV syncSubmitDcpRequestV = this.new SyncSubmitDcpRequestV();

	//-------------------------------------------------------------------------
	// Inner classes
	//

	/**
	 * Simple visitor to select the correct syncSubmitDcp method
	 * @author E. Michael Maximilien
	 */
	protected class SyncSubmitDcpRequestV extends DefaultRequestV
	{
		//---------------------------------------------------------------------
		// Public methods
		//

		/** @return the RequestException that occurred during submission */
		public RequestException getRequestException() { return requestException; }

		/** @return true if there was an exception while doing sync submission */
		public boolean isInException() { return ( requestException != null ); }

		//---------------------------------------------------------------------
		// Public visitXyz methods - Uses the default sync submit of LinuxDcpRequest
		//

		public void visitStandardRequest( Request request ) { visitRequest( request ); }

		public void visitVendorRequest( Request request ) { visitRequest( request ); }

		public void visitClassRequest( Request request ) { visitRequest( request ); }

		//---------------------------------------------------------------------
		// Public visitXyz methods - Needs to be done as a separate LinuxRequest
		//

        public void visitSetConfigurationRequest( Request request ) 
		{
			requestException = null;

			try{ LinuxUsbOpsImp.this.syncSubmitDcpSetConfigurationRequest( request ); }
			catch( RequestException e ) { requestException = e; }
		}

		public void visitSetInterfaceRequest( Request request ) 
		{
			requestException = null;

			try{ LinuxUsbOpsImp.this.syncSubmitDcpSetInterfaceRequest( request ); }
			catch( RequestException e ) { requestException = e; }
		}

		//---------------------------------------------------------------------
		// Protected methods
		//

		/**
		 * Default visitRequest method
		 * @param request the Request to visit
		 */
		protected void visitRequest( Request request )
		{
			requestException = null;

			try{ LinuxUsbOpsImp.this.syncSubmitDcpRequest( request ); }
			catch( RequestException e ) { requestException = e; }
		}
		
		//---------------------------------------------------------------------
		// Instance variables
		//

		private RequestException requestException = null;
	}
}
