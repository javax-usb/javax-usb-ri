package com.ibm.jusb.os;

/*
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import com.ibm.jusb.*;

/**
 * Interface for os implementation of UsbDevice.
 * @author Dan Streetman
 */
public interface UsbDeviceImp
{
	/**
	 * Get a UsbOpsImp for this UsbDevice.
	 * @return A UsbOpsImp.
	 */
	public UsbOpsImp getUsbOpsImp();
}
