package com.ibm.jusb.os;

/** 
 * Copyright (c) IBM Corporation, 2000
 * This software and documentation is the confidential and proprietary
 * information of IBM, Corp. ("Confidential Information").
 * Raleigh, NC USA
 */

import java.util.*;

import junit.framework.*;

import javax.usb.*;
import com.ibm.jusb.test.JUnitUtility;

/**
 * A JUnit Test for RequestFactory interface and implementing class
 * @author E. Michael Maximilien
 */
public class RequestFactoryTestCase extends TestCase
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	public RequestFactoryTestCase( String name ) { super( name ); }

	//-------------------------------------------------------------------------
	// Protected overridden methods
	//

	protected void setUp() 
	{
		factory = new DefaultRequestFactory();
	}

	protected void tearDown()
	{
		factory = null;
	}

	//-------------------------------------------------------------------------
	// Public testXyz() methods
	//

	public void testCreateClearFeatureRequest() throws RequestException
	{
		Request clearFeatureRequest1 = null;

		clearFeatureRequest1 = factory.createClearFeatureRequest( (byte)0x00, (short)0x0000, (short)0x0000 );
		assertTrue( "Request object returned from factory should be != null",
				    clearFeatureRequest1 != null );

		assertTrue( "clearFeatureRequest1.getRequestType() NOT VALID",
				    JUnitUtility.isInList( new Byte( clearFeatureRequest1.getRequestType() ),
					  				       clearFeatureRequestBmRequestTypeList ) );

		assertTrue( "clearFeatureRequest1.getRequest() NOT VALID",
				    clearFeatureRequest1.getRequest() == RequestConst.REQUEST_CLEAR_FEATURE );

		assertTrue( "clearFeatureRequest1.getLength() must be 0",
				    clearFeatureRequest1.getLength() == 0 );
	}

	public void testCreateGetConfigurationRequest() throws RequestException 
	{
		Request getConfigurationRequest1 = null;

		getConfigurationRequest1 = factory.createGetConfigurationRequest( new byte[ 1 ] );
		assertTrue( "Request object returned from factory should be != null",
				    getConfigurationRequest1 != null );

		assertTrue( "getConfigurationRequest1.getRequestType() NOT VALID",
				    getConfigurationRequest1.getRequestType() == (byte)0x80 );

		assertTrue( "getConfigurationRequest1.getRequest() NOT VALID",
				    getConfigurationRequest1.getRequest() == RequestConst.REQUEST_GET_CONFIGURATION );

		assertTrue( "getConfigurationRequest1.getValue() must be 0",
				    getConfigurationRequest1.getValue() == 0 );

		assertTrue( "getConfigurationRequest1.getIndex() must be 0",
				    getConfigurationRequest1.getIndex() == 0 );

		assertTrue( "getConfigurationRequest1.getLength() must be 1",
				    getConfigurationRequest1.getLength() == 1 );
	}

	public void testCreateGetDescriptorRequest() throws RequestException 
	{
		Request getDescriptorRequest1 = null;

		getDescriptorRequest1 = factory.createGetDescriptorRequest( (short)0x0100, (short)0x0000, new byte[ 16 ] );
		assertTrue( "Request object returned from factory should be != null",
				    getDescriptorRequest1 != null );

		assertTrue( "getDescriptorRequest1.getRequestType() NOT VALID",
				    getDescriptorRequest1.getRequestType() == (byte)0x80 );

		assertTrue( "getDescriptorRequest1.getRequest() NOT VALID",
				    getDescriptorRequest1.getRequest() == RequestConst.REQUEST_GET_DESCRIPTOR );

		Byte descriptorTypeByte = new Byte( javax.usb.util.UsbUtil.highByte( getDescriptorRequest1.getValue() ) ); 
		assertTrue( "getDescriptorRequest1.getValue() must be VALID descriptor type",
				    JUnitUtility.isInList( descriptorTypeByte, descriptorTypesList ) );
	}

	public void testCreateGetInterfaceRequest() throws RequestException
	{
		Request getInterfaceRequest1 = null;

		getInterfaceRequest1 = factory.createGetInterfaceRequest( (short)0x0000, new byte[] { 0x00 } );
		assertTrue( "Request object returned from factory should be != null",
				    getInterfaceRequest1 != null );

		assertTrue( "getInterfaceRequest1.getRequestType() NOT VALID",
				    getInterfaceRequest1.getRequestType() == (byte)0x81 );

		assertTrue( "getInterfaceRequest1.getRequest() NOT VALID",
				    getInterfaceRequest1.getRequest() == RequestConst.REQUEST_GET_INTERFACE );

		assertTrue( "getInterfaceRequest1.getValue() must be 0",
				    getInterfaceRequest1.getValue() == 0 );

		assertTrue( "getInterfaceRequest1.getLength() must be 1",
				    getInterfaceRequest1.getLength() == 1 );
	}

	public void testCreateGetStatusRequest() throws RequestException
	{
		Request getStatusRequest1 = null;

		getStatusRequest1 = factory.createGetStatusRequest( (byte)0x80, (short)0x0000, new byte[ 2 ] );
		assertTrue( "Request object returned from factory should be != null",
				    getStatusRequest1 != null );

		Byte requestTypeByte = new Byte( getStatusRequest1.getRequestType() );
		assertTrue( "getStatusRequest1.getRequestType() NOT VALID",
				    JUnitUtility.isInList( requestTypeByte, getStatusRequestBmRequestTypeList ) );

		assertTrue( "getStatusRequest1.getRequest() NOT VALID",
				    getStatusRequest1.getRequest() == RequestConst.REQUEST_GET_STATUS );

		assertTrue( "getStatusRequest1.getValue() must be 0",
				getStatusRequest1.getValue() == 0 );

		assertTrue( "getStatusRequest1.getLength() must be 2",
				getStatusRequest1.getLength() == 2 );
	}

	public void testCreateSetAddressRequest() throws RequestException
	{
		Request setAddressRequest1 = null;

		setAddressRequest1 = factory.createSetAddressRequest( (byte)0x00 );
		assertTrue( "Request object returned from factory should be != null",
				setAddressRequest1 != null );

		assertTrue( "setAddressRequest1.getRequestType() NOT VALID",
				setAddressRequest1.getRequestType() == (byte)0x00 );

		assertTrue( "setAddressRequest1.getRequest() NOT VALID",
				setAddressRequest1.getRequest() == RequestConst.REQUEST_SET_ADDRESS );

		assertTrue( "setAddressRequest1.getValue() must be 0",
				setAddressRequest1.getValue() == 0 );

		assertTrue( "setAddressRequest1.getLength() must be 0",
				setAddressRequest1.getLength() == 0 );
	}

	public void testCreateSetConfigurationRequest() throws RequestException
	{
		Request setConfigurationRequest1 = null;

		setConfigurationRequest1 = factory.createSetConfigurationRequest( (byte)0x00 );
		assertTrue( "Request object returned from factory should be != null",
				setConfigurationRequest1 != null );

		assertTrue( "setConfigurationRequest1.getRequestType() NOT VALID",
				setConfigurationRequest1.getRequestType() == (byte)0x00 );

		assertTrue( "setConfigurationRequest1.getRequest() NOT VALID",
				setConfigurationRequest1.getRequest() == RequestConst.REQUEST_SET_CONFIGURATION );

		assertTrue( "setConfigurationRequest1.getValue() must be 0",
				setConfigurationRequest1.getValue() == 0 );

		assertTrue( "setConfigurationRequest1.getLength() must be 0",
				setConfigurationRequest1.getLength() == 0 );
	}

	public void testCreateSetDescriptorRequest() throws RequestException
	{
		Request setDescriptorRequest1 = null;

		setDescriptorRequest1 = factory.createSetDescriptorRequest( (short)0x0100, (short)0x0000, new byte[ 16 ] );
		assertTrue( "Request object returned from factory should be != null",
				setDescriptorRequest1 != null );

		assertTrue( "setDescriptorRequest1.getRequestType() NOT VALID",
				setDescriptorRequest1.getRequestType() == (byte)0x00 );

		assertTrue( "setDescriptorRequest1.getRequest() NOT VALID",
				setDescriptorRequest1.getRequest() == RequestConst.REQUEST_SET_DESCRIPTOR );

		Byte descriptorTypeByte = new Byte( javax.usb.util.UsbUtil.highByte( setDescriptorRequest1.getValue() ) );
		assertTrue( "setDescriptorRequest1.getValue() must be 0",
				JUnitUtility.isInList( descriptorTypeByte, descriptorTypesList ) );
	}

	public void testCreateSetFeatureRequest() throws RequestException
	{
		Request setFeatureRequest1 = null;

		setFeatureRequest1 = factory.createSetFeatureRequest( (byte)0x00, (short)0x0000, (short)0x0000 );
		assertTrue( "Request object returned from factory should be != null",
				setFeatureRequest1 != null );

		Byte requestTypeByte = new Byte( setFeatureRequest1.getRequestType() );
		assertTrue( "setFeatureRequest1.getRequestType() NOT VALID",
				JUnitUtility.isInList( requestTypeByte, setFeatureRequestBmRequestTypeList ) );

		assertTrue( "setFeatureRequest1.getRequest() NOT VALID",
				setFeatureRequest1.getRequest() == RequestConst.REQUEST_SET_FEATURE );

		assertTrue( "setFeatureRequest1.getValue() must be 0",
				setFeatureRequest1.getValue() == 0 );

		assertTrue( "setFeatureRequest1.getLength() must be 0",
				setFeatureRequest1.getLength() == 0 );
	}

	public void testCreateSetInterfaceRequest() throws RequestException
	{
		Request setInterfaceRequest1 = null;

		setInterfaceRequest1 = factory.createSetInterfaceRequest( (short)0x0000, (short)0x0001	 );
		assertTrue( "Request object returned from factory should be != null",
				setInterfaceRequest1 != null );

		assertTrue( "setInterfaceRequest1.getRequestType() NOT VALID",
				setInterfaceRequest1.getRequestType() == (byte)0x01 );

		assertTrue( "setInterfaceRequest1.getRequest() NOT VALID",
				setInterfaceRequest1.getRequest() == RequestConst.REQUEST_SET_INTERFACE );

		assertTrue( "setInterfaceRequest1.getLength() must be 0",
				setInterfaceRequest1.getLength() == 0 );
	}

	public void testCreateSynchFrameRequest() throws RequestException
	{
		Request synchFrameRequest1 = null;

		synchFrameRequest1 = factory.createSynchFrameRequest( (short)0x0000, new byte[ 2 ] );
		assertTrue( "Request object returned from factory should be != null",
				synchFrameRequest1 != null );

		assertTrue( "synchFrameRequest1.getRequestType() NOT VALID",
				synchFrameRequest1.getRequestType() == (byte)0x82 );

		assertTrue( "synchFrameRequest1.getRequest() NOT VALID",
				synchFrameRequest1.getRequest() == RequestConst.REQUEST_SYNCH_FRAME );

		assertTrue( "synchFrameRequest1.getValue() must be 0",
				synchFrameRequest1.getValue() == 0 );

		assertTrue( "synchFrameRequest1.getLength() must be 0",
				synchFrameRequest1.getLength() == 2 );
	}

	//-------------------------------------------------------------------------
	// Instance variables
	//

	private DefaultRequestFactory factory = null;

	//-------------------------------------------------------------------------
	// Class variables
	//

	private static List descriptorTypesList = new ArrayList();

	private static List clearFeatureRequestBmRequestTypeList = new ArrayList();
	private static List getStatusRequestBmRequestTypeList = new ArrayList();
	private static List setFeatureRequestBmRequestTypeList = new ArrayList();

	//-------------------------------------------------------------------------
	// Static initializer
	//

	static
	{
		descriptorTypesList.add( new Byte( (byte)0x01 ) );
		descriptorTypesList.add( new Byte( (byte)0x02 ) );
		descriptorTypesList.add( new Byte( (byte)0x03 ) );
		descriptorTypesList.add( new Byte( (byte)0x04 ) );
		descriptorTypesList.add( new Byte( (byte)0x05 ) );

		clearFeatureRequestBmRequestTypeList.add( new Byte( (byte)0x00 ) );
		clearFeatureRequestBmRequestTypeList.add( new Byte( (byte)0x01 ) );
		clearFeatureRequestBmRequestTypeList.add( new Byte( (byte)0x02 ) );

		getStatusRequestBmRequestTypeList.add( new Byte( (byte)0x80 ) );
		getStatusRequestBmRequestTypeList.add( new Byte( (byte)0x81 ) );
		getStatusRequestBmRequestTypeList.add( new Byte( (byte)0x82 ) );
		
		setFeatureRequestBmRequestTypeList.add( new Byte( (byte)0x00 ) );
		setFeatureRequestBmRequestTypeList.add( new Byte( (byte)0x01 ) );
		setFeatureRequestBmRequestTypeList.add( new Byte( (byte)0x02 ) );
	}
}
