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
 * This class defines the interface for implementing the UsbOperations
 * @author E. Michael Maximilien
 */
public interface UsbOpsImp
{
	//-------------------------------------------------------------------------
	// Public methods
	//
	
	/**
	 * Performs a synchronous operation by submitting the Request object
	 * @param request the Request object that is used for this submit
	 * @exception javax.usb.RequestException if something goes wrong submitting the request for this operation
	 */
	public void syncSubmit( Request request ) throws RequestException;

	/**
	 * Performs a synchronous operation by submitting all the Request objects in the bundle.
	 * No other request submission can be overlapped.  This means that the Request object in the
	 * bundle are guaranteed to be sent w/o interruption.
	 * @param requestBundle the RequestBundle object that is used for this submit
	 * @exception javax.usb.RequestException if something goes wrong submitting the request for this operation
	 */
	public void syncSubmit( RequestBundle requestBundle ) throws RequestException;

	/**
	 * Performs a asynchronous operation by submitting the Request object
	 * @param request the Request object that is used for this submit
	 * @return a UsbOperations.SubmitResult object used to track the submission
	 * @exception javax.usb.RequestException if something goes wrong submitting the request for this operation
	 */
	public UsbOperations.SubmitResult asyncSubmit( Request request ) throws RequestException;
}
