package com.ibm.jusb.os;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the IBM Public License.
 *
 */

/**
 * Default UsbIrpImpVisitor implementation
 * @author Dan Streetman
 * @version 0.0.1 (JDK 1.1.x)
 */
public class DefaultUsbIrpImpV implements UsbIrpImpVisitor
{
	/**
	 * Visit a UsbIrpImp
	 * @param the UsbIrpImp being visited
	 */
	public void visitUsbIrpImp( UsbIrpImp irp ) { }

	/**
	 * Visit a UsbCompositeIrpImp
	 * @param the UsbCompositeIrpImp being visited
	 */
	public void visitUsbCompositeIrpImp( UsbCompositeIrpImp irp ) { }

}
