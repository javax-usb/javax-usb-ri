package com.ibm.jusb;

/*
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import javax.usb.Request;

/**
 * Defines a simple Visitor interface for the Request hiearchy
 * @version 1.0.0
 */
public interface RequestVisitor
{
	//-------------------------------------------------------------------------
	// Public visit<Xyz> methods for VendorRequest objects
	//

	/**
	 * Visits a VendorRequest Request
	 * @param request the Request object
	 */
	public void visitVendorRequest( Request request );

	//-------------------------------------------------------------------------
	// Public visit<Xyz> methods for ClassRequest objects
	//

	/**
	 * Visits a ClassRequest Request
	 * @param request the Request object
	 */
	public void visitClassRequest( Request request );

	/**
	 * Visits a HubClassRequest Request
	 * @param request the Request object
	 */
	public void visitHubClassRequest( Request request );

	/**
	 * Visits a GetStateRequest Request
	 * @param request the Request object
	 */
	public void visitGetStateRequest( Request request );

	//-------------------------------------------------------------------------
	// Public visit<Xyz> methods for StandardRequest objects
	//

	/**
	 * Visits a StandardRequest Request
	 * @param request the Request object
	 */
	public void visitStandardRequest( Request request );

	/**
	 * Visits a ClearFeatureRequest Request
	 * @param request the Request object
	 */
	public void visitClearFeatureRequest( Request request );

	/**
	 * Visits a GetConfigurationRequest Request
	 * @param request the Request object
	 */
	public void visitGetConfigurationRequest( Request request );

	/**
	 * Visits a GetDescriptorRequest Request
	 * @param request the Request object
	 */
	public void visitGetDescriptorRequest( Request request );

	/**
	 * Visits a GetInterfaceRequest Request
	 * @param request the Request object
	 */
	public void visitGetInterfaceRequest( Request request );

	/**
	 * Visits a GetStatusRequest Request
	 * @param request the Request object
	 */
	public void visitGetStatusRequest( Request request );

	/**
	 * Visits a SetAddressRequest Request
	 * @param request the Request object
	 */
	public void visitSetAddressRequest( Request request );

	/**
	 * Visits a SetConfigurationRequest Request
	 * @param request the Request object
	 */
	public void visitSetConfigurationRequest( Request request );

	/**
	 * Visits a SetDescriptorRequest Request
	 * @param request the Request object
	 */
	public void visitSetDescriptorRequest( Request request );

	/**
	 * Visits a SetFeatureRequest Request
	 * @param request the Request object
	 */
	public void visitSetFeatureRequest( Request request );

	/**
	 * Visits a SetInterfaceRequest Request
	 * @param request the Request object
	 */
	public void visitSetInterfaceRequest( Request request );

	/**
	 * Visits a SynchFrameRequest Request
	 * @param request the Request object
	 */
	public void visitSynchFrameRequest( Request request );
}