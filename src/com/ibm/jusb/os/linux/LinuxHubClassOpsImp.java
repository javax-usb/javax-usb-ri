package com.ibm.jusb.os.linux;

/*
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import com.ibm.jusb.os.ClassOpsImp;
import com.ibm.jusb.os.HubClassOpsImp;

/**
 * Linux implementation of the HubClassOpsImp interface
 * @author Dan Streetman
 * @author E. Michael Maximilien
 * @version 1.0.0
 */
class LinuxHubClassOpsImp extends LinuxClassOpsImp implements HubClassOpsImp
{
	/**
	 * Constructor
	 * @param deviceImp the associated LinuxDeviceImp.
	 */
	public LinuxHubClassOpsImp( LinuxDeviceImp deviceImp ) { super( deviceImp ); }
}
