package com.ibm.jusb.os;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the IBM Public License.
 *
 */

import javax.usb.*;

/**
 * Defines a factory interface to create Descriptor objects
 * @author E. Michael Maximilien
 * @author Dan Streetman
 * @version 0.0.1 (JDK 1.1.x)
 */
public interface DescriptorFactory
{
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
		short bcdUsb );

    /** @return a ConfigDescriptor object */
    public ConfigDescriptor createConfigDescriptor(
		byte length,
		byte type,
		byte numInterfaces,
		byte configValue,
		byte configIndex,
		byte attributes,
		byte maxPower );

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
		byte interfaceIndex );

	/** @return an EndpointDescriptor object */
	public EndpointDescriptor createEndpointDescriptor(
		byte length,
		byte type,
		byte endpointAddress,
		byte attributes,
		byte interval,
		short maxPacketSize );

    /** @return a StringDescriptor object */
    public StringDescriptor createStringDescriptor(
		byte length,
		byte type,
		String string );

}
