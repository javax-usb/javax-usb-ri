package com.ibm.jusb.os.linux;

/*
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import com.ibm.jusb.os.VendorOpsImp;

/**
 * Linux implementation of the VendorOpsImp interface
 * @author Dan Streetman
 * @author E. Michael Maximilien
 * @version 1.0.0
 */
class LinuxVendorOpsImp extends LinuxUsbOpsImp implements VendorOpsImp
{
	/**
	 * Constructor
	 * @param deviceImp the associated LinuxDeviceImp.
	 */
	public LinuxVendorOpsImp( LinuxDeviceImp deviceImp ) { super( deviceImp ); }
}
