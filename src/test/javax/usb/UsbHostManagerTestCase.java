package javax.usb;

/** 
 * Copyright (c) IBM Corporation, 2000
 * This software and documentation is the confidential and proprietary
 * information of IBM, Corp. ("Confidential Information").
 * Raleigh, NC USA
 */

import junit.framework.*;

/**
 * A Test for the UsbHostManager
 * @author Dan Streetman
 */
public class UsbHostManagerTestCase extends UsbTestCase {

    /** Constructor */
    public UsbHostManagerTestCase( String name ) { super( name ); }

    //*************************************************************************
    // Protected methods

    /** Set up tests */
    protected void setUp() {
        hostManager = UsbHostManager.getInstance();
        }

    /** Tear down test */
    protected void tearDown() {
        services = null;
        properties = null;
        hostManager = null;
        }

    //*************************************************************************
    // Public test methods

    public void testUsbHostManager() {
        assertNotNull( "Couldn't get UsbHostManager", hostManager );
        assertSame( "UsbHostManager returned 2 different instances", hostManager, UsbHostManager.getInstance() );

        properties = hostManager.getUsbProperties();

        try { services = hostManager.getUsbServices(); }
        catch ( UsbException uE ) {
            fail( "Could not get UsbServices" );
            }
        }

    //*************************************************************************
    // Instance variables

    private UsbHostManager hostManager = null;
    private javax.usb.util.UsbProperties properties = null;
    private javax.usb.os.UsbServices services = null;

	//<temp>
	public static void main( String[] args )
	{
		System.out.println( "UsbHostManager.main" );


		try
		{
			UsbHostManager usbHostManager = UsbHostManager.getInstance();
			System.out.println( usbHostManager );
			
			javax.usb.os.UsbServices usbServices = usbHostManager.getUsbServices();
			System.out.println( usbServices );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
	//</temp>
}
