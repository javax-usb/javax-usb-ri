package com.ibm.jusb.os;

/*
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import javax.usb.*;
import javax.usb.os.*;
import javax.usb.UsbException;
import javax.usb.util.UsbProperties;

import com.ibm.jusb.util.*;

/**
 * Defines a singleton utility class that is used to create objects that
 * are used accross all OS implementations
 * @author E. Michael Maximilien
 * @author Dan Streetman
 * @version 0.0.1 (JDK 1.1.x)
 */
public class UsbUtility extends Object
{
    //-------------------------------------------------------------------------
    // Ctor
    //

    /** Make default ctor protected to prevent construction */
    protected UsbUtility() {}

    //-------------------------------------------------------------------------
    // Public class methods
    //

    /**
     * @return the only instance of this class.  Creating it if necessary
     * NOTE: if necessary then use the UsbProperties class to dynamically
     * create an instance of a subclass instead of this class by default
     */
    static UsbUtility getInstance()
    {
        if( instance == null )
            instance = new UsbUtility();

        return instance;
    }

    //-------------------------------------------------------------------------
    // Private methods
    //

    /** @return a new UsbInfoFactory using the default class */
    private UsbInfoFactory createDefaultUsbInfoFactory() { return new DefaultUsbInfoFactory(); }

	/** @return a new DescriptorFactory using the default class */
	private DescriptorFactory createDefaultDescriptorFactory() { return new DefaultDescriptorFactory(); }

    /** @return a new UsbPipeFactory using the default class */
    private UsbPipeFactory createDefaultUsbPipeFactory() { return new DefaultUsbPipeFactory(); }

    /** @return a new StateMachineFactory using the default class */
    private StateMachineFactory createDefaultStateMachineFactory() { return DefaultStateMachineFactory.getInstance(); }

    /**
     * @return a new UsbInfoFactory object creating the default or dynamically using
     * the class specified in the UsbProperties
     */
    private UsbInfoFactory createUsbInfoFactory()
    {
        UsbProperties properties = UsbHostManager.getInstance().getUsbProperties();

        if( properties.isPropertyDefined( JUSB_USB_INFO_FACTORY_PROP_NAME ) )
        {
            String usbInfoFactoryClassName = properties.getPropertyString( JUSB_USB_INFO_FACTORY_PROP_NAME );

            try
            {
                Class usbInfoFactoryClass = Class.forName( usbInfoFactoryClassName );
                return (UsbInfoFactory)usbInfoFactoryClass.newInstance();
            }
            catch( Exception e ) { }
        }

        return createDefaultUsbInfoFactory();
    }

    /**
     * @return a new UsbInfoFactory object creating the default or dynamically using
     * the class specified in the UsbProperties
     */
    private DescriptorFactory createDescriptorFactory()
    {
        UsbProperties properties = UsbHostManager.getInstance().getUsbProperties();

        if( properties.isPropertyDefined( JUSB_USB_DESCRIPTOR_FACTORY_PROP_NAME ) )
        {
            String descriptorFactoryClassName = properties.getPropertyString( JUSB_USB_DESCRIPTOR_FACTORY_PROP_NAME );

            try
            {
                Class descriptorFactoryClass = Class.forName( descriptorFactoryClassName );
                return (DescriptorFactory)descriptorFactoryClass.newInstance();
            }
            catch( Exception e ) { }
        }

        return createDefaultDescriptorFactory();
    }

    /**
     * @return a new UsbPipeFactory object creating the default or dynamically using
     * the class specified in the UsbProperties
     */
    private UsbPipeFactory createUsbPipeFactory()
    {
        UsbProperties properties = UsbHostManager.getInstance().getUsbProperties();

        if( properties.isPropertyDefined( JUSB_USB_PIPE_FACTORY_PROP_NAME ) )
        {
            String usbPipeFactoryClassName = properties.getPropertyString( JUSB_USB_PIPE_FACTORY_PROP_NAME );

            try
            {
                Class usbPipeFactoryClass = Class.forName( usbPipeFactoryClassName );
                return (UsbPipeFactory)usbPipeFactoryClass.newInstance();
            }
            catch( Exception e ) { }
        }

        return createDefaultUsbPipeFactory();
    }

    /**
     * @return a new SateMachineFactory object creating the default or dynamically using
     * the class specified in the UsbProperties
     */
    private StateMachineFactory createStateMachineFactory()
    {
        UsbProperties properties = UsbHostManager.getInstance().getUsbProperties();

        if( properties.isPropertyDefined( UsbProperties.JUSB_STATE_MACHINE_FACTORY_PROP_NAME ) )
        {
            String sateMachineFactoryClassName = properties.getPropertyString( UsbProperties.JUSB_STATE_MACHINE_FACTORY_PROP_NAME );

            try
            {
                Class stateMachineFactoryClass = Class.forName( sateMachineFactoryClassName );
                return (StateMachineFactory)stateMachineFactoryClass.newInstance();
            }
            catch( Exception e ) { }
        }

        return createDefaultStateMachineFactory();
    }

    //-------------------------------------------------------------------------
    // Public methods
    //

    /** @return a Factory class to create UsbInfo objects */
    public UsbInfoFactory getUsbInfoFactory()
    {
        if( usbInfoFactory == null )
            usbInfoFactory = createUsbInfoFactory();

        return usbInfoFactory;
    }

	/** @return a Factory class to create Descriptor objects */
	public DescriptorFactory getDescriptorFactory()
	{
		if( descriptorFactory == null )
			descriptorFactory = createDescriptorFactory();

		return descriptorFactory;
	}

    /** @return a Factory class to create UsbPipe objects */
    public UsbPipeFactory getUsbPipeFactory()
    {
        if( usbPipeFactory == null )
            usbPipeFactory = createUsbPipeFactory();

        return usbPipeFactory;
    }

    /** @return a Factory class to create StateMachine objects */
    public StateMachineFactory getStateMachineFactory()
    {
        if( stateMachineFactory == null )
            stateMachineFactory = createStateMachineFactory();

        return stateMachineFactory;
    }

	/** @return a Factory class to create UsbIrpImp objects */
	public RecycleFactory getUsbIrpImpFactory()
	{
		if ( null == usbIrpImpFactory ) {
			usbIrpImpFactory = new RecycleFactory() {
				public Recyclable createRecyclable() { return new UsbIrpImp( UsbUtility.this.usbIrpImpFactory ); }
			};
		}

		return usbIrpImpFactory;
	}

	/** @return a Factory class to create UsbCompositeIrpImp objects */
	public RecycleFactory getUsbCompositeIrpImpFactory()
	{
		if ( null == usbCompositeIrpImpFactory ) {
			usbCompositeIrpImpFactory = new RecycleFactory() {
				public Recyclable createRecyclable() { return new UsbCompositeIrpImp( UsbUtility.this.usbCompositeIrpImpFactory ); }
			};
		}

		return usbCompositeIrpImpFactory;
	}

    //-------------------------------------------------------------------------
    // Instance variables
    //

    private UsbInfoFactory usbInfoFactory = null;
	private DescriptorFactory descriptorFactory = null;
    private UsbPipeFactory usbPipeFactory = null;
    private StateMachineFactory stateMachineFactory = null;
	private RecycleFactory usbIrpImpFactory = null;
	private RecycleFactory usbCompositeIrpImpFactory = null;

    //-------------------------------------------------------------------------
    // Class variables
    //

    private static UsbUtility instance = null;

	//-------------------------------------------------------------------------
	// Class constants
	//

	private static final String JUSB_USB_DESCRIPTOR_FACTORY_PROP_NAME = "com.ibm.jusb.os.DescriptorFactory";
    private static final String JUSB_USB_INFO_FACTORY_PROP_NAME = "com.ibm.jusb.os.usbInfoFactory";
    private static final String JUSB_USB_PIPE_FACTORY_PROP_NAME = "com.ibm.jusb.os.usbPipeFactory";

}
