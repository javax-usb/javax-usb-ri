package com.ibm.jusb.os;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import java.util.*;

import javax.usb.*;

import com.ibm.jusb.*;

/**
 * Interface for Platform-specific implementation of control-type UsbPipe.
 * <p>
 * This is identical to UsbPipeOsImp with added methods using ControlUsbPipeOsImp parameters.
 * @author Dan Streetman
 */
public interface ControlUsbPipeOsImp extends UsbPipeOsImp
{
	/**
	 * Synchronously submits this ControlUsbIrpImp to the platform implementation.
	 * <p>
	 * This is identical to
	 * {@link com.ibm.jusb.os.UsbPipeOsImp#syncSubmit(UsbIrpImp) UsbPipeOsImp.syncSubmit(UsbIrpImp)}
	 * except the parameter is a ControlUsbIrpImp.
	 * @param irp the ControlUsbIrpImp to use for this submission.
	 * @exception UsbException If the data transfer was unsuccessful.
	 */
	public void syncSubmit( ControlUsbIrpImp irp ) throws UsbException;

	/**
	 * Asynchronously submits this ControlUsbIrpImp to the platform implementation.
	 * This is identical to
	 * {@link com.ibm.jusb.os.UsbPipeOsImp#asyncSubmit(UsbIrpImp) UsbPipeOsImp.asyncSubmit(UsbIrpImp)}
	 * except the parameter is a ControlUsbIrpImp.
	 * <p>
	 * @param irp the ControlUsbIrpImp to use for this submission
	 * @exception UsbException If the ControlUsbIrpImp was not accepted by the implementation.
	 */
	public void asyncSubmit( ControlUsbIrpImp irp ) throws UsbException;
}
