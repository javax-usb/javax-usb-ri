package com.ibm.jusb;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

/**
 * Visitor for UsbIrpImp objects
 * @author Dan Streetman
 * @version 0.0.1 (JDK 1.1.x)
 */
public interface UsbIrpImpVisitor
{
	/**
	 * Visit a UsbIrpImp
	 * @param the UsbIrpImp being visited
	 */
	public void visitUsbIrpImp( UsbIrpImp irp );

	/**
	 * Visit a UsbCompositeIrpImp
	 * @param the UsbCompositeIrpImp being visited
	 */
	public void visitUsbCompositeIrpImp( UsbCompositeIrpImp irp );
}
