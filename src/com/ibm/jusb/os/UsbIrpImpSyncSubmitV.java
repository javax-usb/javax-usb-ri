package com.ibm.jusb.os;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the IBM Public License.
 *
 */

import javax.usb.UsbException;

/**
 * UsbIrpImp visitor to perform syncSubmits
 * @author Dan Streetman
 * @version 0.0.1 (JDK 1.1.x)
 */
class UsbIrpImpSyncSubmitV extends UsbIrpImpPipeV implements UsbIrpImpVisitor
{
	/**
	 * Constructor
	 */
	public UsbIrpImpSyncSubmitV() { }

	/**
	 * Constructor
	 * @param pipe the UsbPipeImp to use
	 */
	public UsbIrpImpSyncSubmitV( UsbPipeImp pipe ) { setUsbPipeImp( pipe ); }

	//*************************************************************************
	// Public visitor methods

	/**
	 * Visit a UsbIrpImp
	 * @param the UsbIrpImp being visited
	 */
	public void visitUsbIrpImp( UsbIrpImp irp )
	{
		clearUsbException();

		try { getUsbPipeImp().syncSubmit( irp ); }
		catch ( UsbException uE ) { setUsbException( uE ); }
	}

	/**
	 * Visit a UsbCompositeIrpImp
	 * @param the UsbCompositeIrpImp being visited
	 */
	public void visitUsbCompositeIrpImp( UsbCompositeIrpImp irp )
	{
		clearUsbException();

		try { getUsbPipeImp().syncSubmit( irp ); }
		catch ( UsbException uE ) { setUsbException( uE ); }
	}

}
