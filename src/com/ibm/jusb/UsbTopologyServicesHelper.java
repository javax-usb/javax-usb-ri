package com.ibm.jusb;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import java.util.Vector;

import javax.usb.event.UsbServicesEvent;
import javax.usb.event.UsbServicesListener;
import javax.usb.os.UsbTopologyServices;

import com.ibm.jusb.util.Task;
import com.ibm.jusb.util.TaskScheduler;

/**
 * Helper class to implement registration and event delivery for classes
 * implementing the UsbTopologyServices interface.  Can use this class to 
 * implement better event delivery like using a queue or separate threads 
 * for each registered listener.
 * @author E. Michael Maximilien
 * @version 0.0.1 (JDK 1.1.x)
 */
public class UsbTopologyServicesHelper extends Object
{
    //-------------------------------------------------------------------------
    // Ctor(s)
    //

    /**
     * Default one-arg ctor
     * @param usbTopologyServices the services object using this helper
     * @param taskScheduler the TaskScheduler that will be used to fire events
     */
    public UsbTopologyServicesHelper( UsbTopologyServices usbTopologyServices, TaskScheduler taskScheduler )
    { 
        this.usbTopologyServices = usbTopologyServices; 
        this.taskScheduler = taskScheduler;
    }

    //-------------------------------------------------------------------------
    // Public methods
    //

    /**
     * Called to fire a new UsbServicesEvent to all listeners
     * @param event the UsbServicesEvent to fire
     */
    public void fireUsbDeviceAttachedEvent( UsbServicesEvent event )
    { taskScheduler.post( this.new DeviceAttachedEventTask( event ) ); }

    /**
     * Called to fire a new UsbServicesEvent to all listeners
     * @param event the UsbServicesEvent to fire
     */
    public void fireUsbDeviceDetachedEvent( UsbServicesEvent event )
    { taskScheduler.post( this.new DeviceDetachedEventTask( event ) ); }

    /**
     * Adds a new UsbServicesListener object to receive events when the USB host
     * has changes.  For instance a new device is plugged in or unplugged.
     * @param l the UsbServicesListener to register     
     */
    public synchronized void addUsbServicesListener( UsbServicesListener l )
    { listeners.addElement( l ); }

    /**
     * Adds a new UsbServicesListener object to receive events when the USB host
     * has changes.  For instance a new device is plugged in or unplugged.
     * @param l the UsbServicesListener to register     
     */
    public synchronized void removeUsbServicesListener( UsbServicesListener l )
    { listeners.removeElement( l ); }

    //-------------------------------------------------------------------------
    // Inner classes
    //

    /**
     * Simple Task implementation for delivering UsbServicesEvent to listners
     * @author E. Michael Maximilien
     */
    protected abstract class EventTask extends Object implements Task
    {
        /** 
         * One argument ctor
         * @param event the UsbServicesEvent object
         */
        public EventTask( UsbServicesEvent event ) { this.event = event; }

        //---------------------------------------------------------------------
        // Public methods
        //

        /** Called to execute the task */
        public void execute() 
        {
            for( int i = 0; i < listeners.size(); ++ i )
                fireUsbServicesEvent( (UsbServicesListener)listeners.elementAt( i ), event );

        }

        //---------------------------------------------------------------------
        // Protected abstract methods
        //

        /**
         * Fires a UsbServicesListner event to the listener
         * @param listener the UsbServicesListner object
         * @param e the UsbServicesEvent to fire
         */
        protected abstract void fireUsbServicesEvent( UsbServicesListener listener, UsbServicesEvent e );

        //---------------------------------------------------------------------
        // Instance variables
        //

        protected UsbServicesEvent event = null;
    }

    /**
     * Simple Task implementation for delivering 
     * UsbServicesListner.usbDeviceAttached( UsbServicesEvent ) to listeners
     * @author E. Michael Maximilien
     */
    protected class DeviceAttachedEventTask extends EventTask implements Task
    {
        /** 
         * One argument ctor
         * @param event the UsbServicesEvent object
         */
        public DeviceAttachedEventTask( UsbServicesEvent event ) { super( event ); }

        //---------------------------------------------------------------------
        // Protected abstract methods
        //

        /**
         * Fires a UsbServicesListner.usbDeviceAttached event to the listener
         * @param listener the UsbServicesListner object
         * @param e the UsbServicesEvent to fire
         */
        protected void fireUsbServicesEvent( UsbServicesListener listener, UsbServicesEvent e )
        { listener.usbDeviceAttached( e ); }
    }

    /**
     * Simple Task implementation for delivering 
     * UsbServicesListner.usbDeviceDettached( UsbServicesEvent ) to listeners
     * @author E. Michael Maximilien
     */
    protected class DeviceDetachedEventTask extends EventTask implements Task
    {
        /** 
         * One argument ctor
         * @param event the UsbServicesEvent object
         */
        public DeviceDetachedEventTask( UsbServicesEvent event ) { super( event ); }

        //---------------------------------------------------------------------
        // Protected abstract methods
        //

        /**
         * Fires a UsbServicesListner.usbDeviceAttached event to the listener
         * @param listener the UsbServicesListner object
         * @param e the UsbServicesEvent to fire
         */
        protected void fireUsbServicesEvent( UsbServicesListener listener, UsbServicesEvent e )
        { listener.usbDeviceDetached( e ); }
    }

    //-------------------------------------------------------------------------
    // Instance variables
    //

    private TaskScheduler taskScheduler = null;
    private UsbTopologyServices usbTopologyServices = null;

    private Vector listeners = new Vector();
}                                                                             
