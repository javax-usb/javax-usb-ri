package com.ibm.jusb;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import javax.usb.*;

/**
 * Default implementation of Descriptor Factory
 * simple imeplementation.  Flyweight pattern not used; when device disappears so should descriptor.
 * @author Dan Streetman
 * @version 0.0.1 (JDK 1.1.x)
 */
public class DefaultDescriptorFactory implements DescriptorFactory {

	/** Constructor */
	public DefaultDescriptorFactory() { }

    /** @return a DeviceDescriptor object */
    public DeviceDescriptor createDeviceDescriptor(
		byte length,
		byte type,
		byte deviceClass,
		byte deviceSubClass,
		byte deviceProtocol,
		byte maxPacketSize,
		byte manufacturerIndex,
		byte productIndex,
		byte serialNumberIndex,
		byte numConfigs,
		short vendorId,
		short productId,
		short bcdDevice,
		short bcdUsb )
	{
		DeviceDescriptorImp deviceDescriptorImp = new DeviceDescriptorImp();

		deviceDescriptorImp.setLength( length );
		deviceDescriptorImp.setType( type );
		deviceDescriptorImp.setDeviceClass( deviceClass );
		deviceDescriptorImp.setDeviceSubClass( deviceSubClass );
		deviceDescriptorImp.setDeviceProtocol( deviceProtocol );
		deviceDescriptorImp.setMaxPacketSize( maxPacketSize );
		deviceDescriptorImp.setManufacturerIndex( manufacturerIndex );
		deviceDescriptorImp.setProductIndex( productIndex );
		deviceDescriptorImp.setSerialNumberIndex( serialNumberIndex );
		deviceDescriptorImp.setNumConfigs( numConfigs );
		deviceDescriptorImp.setVendorId( vendorId );
		deviceDescriptorImp.setProductId( productId );
		deviceDescriptorImp.setBcdDevice( bcdDevice );
		deviceDescriptorImp.setBcdUsb( bcdUsb );

		return deviceDescriptorImp;
	}

    /** @return a ConfigDescriptor object */
    public ConfigDescriptor createConfigDescriptor(
		byte length,
		byte type,
		byte numInterfaces,
		byte configValue,
		byte configIndex,
		byte attributes,
		byte maxPower )
	{
		ConfigDescriptorImp configDescriptorImp = new ConfigDescriptorImp();

		configDescriptorImp.setLength( length );
		configDescriptorImp.setType( type );
		configDescriptorImp.setNumInterfaces( numInterfaces );
		configDescriptorImp.setConfigValue( configValue );
		configDescriptorImp.setConfigIndex( configIndex );
		configDescriptorImp.setAttributes( attributes );
		configDescriptorImp.setMaxPower( maxPower );

		return configDescriptorImp;
	}

    /** @return an InterfaceDescriptor object */
    public InterfaceDescriptor createInterfaceDescriptor(
		byte length,
		byte type,
		byte interfaceNumber,
		byte alternateSetting,
		byte numEndpoints,
		byte interfaceClass,
		byte interfaceSubClass,
		byte interfaceProtocol,
		byte interfaceIndex )
	{
		InterfaceDescriptorImp interfaceDescriptorImp = new InterfaceDescriptorImp();

		interfaceDescriptorImp.setLength( length );
		interfaceDescriptorImp.setType( type );
		interfaceDescriptorImp.setInterfaceNumber( interfaceNumber );
		interfaceDescriptorImp.setAlternateSetting( alternateSetting );
		interfaceDescriptorImp.setNumEndpoints( numEndpoints );
		interfaceDescriptorImp.setInterfaceClass( interfaceClass );
		interfaceDescriptorImp.setInterfaceSubClass( interfaceSubClass );
		interfaceDescriptorImp.setInterfaceProtocol( interfaceProtocol );
		interfaceDescriptorImp.setInterfaceIndex( interfaceIndex );

		return interfaceDescriptorImp;
	}

	/** @return an EndpointDescriptor object */
	public EndpointDescriptor createEndpointDescriptor(
		byte length,
		byte type,
		byte endpointAddress,
		byte attributes,
		byte interval,
		short maxPacketSize )
	{
		EndpointDescriptorImp endpointDescriptorImp = new EndpointDescriptorImp();

		endpointDescriptorImp.setLength( length );
		endpointDescriptorImp.setType( type );
		endpointDescriptorImp.setEndpointAddress( endpointAddress );
		endpointDescriptorImp.setAttributes( attributes );
		endpointDescriptorImp.setInterval( interval );
		endpointDescriptorImp.setMaxPacketSize( maxPacketSize );

		return endpointDescriptorImp;
	}

    /** @return a StringDescriptor object */
    public StringDescriptor createStringDescriptor(
		byte length,
		byte type,
		String string )
	{
		StringDescriptorImp stringDescriptorImp = new StringDescriptorImp();

		stringDescriptorImp.setLength( length );
		stringDescriptorImp.setType( type );
		stringDescriptorImp.setString( string );

		return stringDescriptorImp;
	}

}
