package com.ibm.jusb.os;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import javax.usb.UsbException;

/**
 * UsbIrpImpVisitor with methods for UsbPipeImp management and UsbException handling.
 * @author Dan Streetman
 * @version 0.0.1 (JDK 1.1.x)
 */
public class UsbIrpImpPipeV extends DefaultUsbIrpImpV implements UsbIrpImpVisitor
{
	//*************************************************************************
	// Public methods

	/** @return the UsbPipeImp associated with this visitor */
	public UsbPipeImp getUsbPipeImp() { return usbPipeImp; }

	/** @param pipe the UsbPipeImp to associate with this visitor */
	public void setUsbPipeImp( UsbPipeImp pipe ) { usbPipeImp = pipe; }

	/** @return if a UsbException occurred during visitation */
	public boolean isInUsbException() { return ( null != getUsbException() ); }

	/** @return the UsbException that occured during visitation */
	public UsbException getUsbException() { return usbException; }

	/** Clear the UsbException */
	public void clearUsbException() { setUsbException( null ); }

	//*************************************************************************
	// Protected methods

	/** @param the new UsbException */
	protected void setUsbException( UsbException uE ) { usbException = uE; }

	//*************************************************************************
	// Instance variables

	private UsbPipeImp usbPipeImp = null;
	private UsbException usbException = null;

}
