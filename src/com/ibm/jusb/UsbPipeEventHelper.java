package com.ibm.jusb.os;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import java.util.Vector;

import javax.usb.*;
import javax.usb.event.*;
import com.ibm.jusb.util.*;


/**
 * Helper class to implement registration and event delivery for UsbPipeImplementation
 * @author E. Michael Maximilien
 * @version 0.0.1 (JDK 1.1.x)
 */
public class UsbPipeEventHelper extends Object
{
    //-------------------------------------------------------------------------
    // Ctor(s)
    //

    /**
	 * Creates a new helper object for the UsbPipe specified and with the specified TaskScheduler
     * @param usbPipe the services object using this helper
     * @param taskScheduler the TaskScheduler that will be used to fire events
     */
    UsbPipeEventHelper( UsbPipe usbPipe, TaskScheduler taskScheduler )
    { 
        this.usbPipe = usbPipe; 
        this.taskScheduler = taskScheduler;
    }

    //-------------------------------------------------------------------------
    // Public methods
    //

    /**
     * Called to fire a new UsbPipeEvent to all listeners
     * @param event the UsbPipeEvent to fire
     */
    public void fireUsbPipeErrorEvent( UsbPipeErrorEvent event )
    { taskScheduler.post( this.new UsbPipeErrorEventTask( event ) ); }

    /**
     * Called to fire a new UsbPipeEvent to all listeners
     * @param event the UsbPipeEvent to fire
     */
    public void fireUsbPipeDataEvent( UsbPipeDataEvent event )
    { taskScheduler.post( this.new UsbPipeDataEventTask( event ) ); }

    /**
     * Adds a new UsbPipeListener object
     * @param l the UsbPipeListener to register     
     */
    public synchronized void addUsbPipeListener( UsbPipeListener l )
    { listeners.addElement( l ); }

    /**
     * Removes the UsbPipeListener object
     * @param l the UsbPipeListener to deregister
     */
    public synchronized void removeUsbPipeListener( UsbPipeListener l )
    { listeners.removeElement( l ); }

    //-------------------------------------------------------------------------
    // Inner classes
    //

    /**
     * Simple Task implementation for delivering UsbPipeEvent to listners
     * @author E. Michael Maximilien
     */
    protected abstract class EventTask extends Object implements Task
    {
        /** 
         * One argument ctor
         * @param event the UsbPipeEvent object
         */
        public EventTask( UsbPipeEvent event ) { this.event = event; }

        //---------------------------------------------------------------------
        // Public methods
        //

        /** Called to execute the task */
        public void execute() 
        {
            for( int i = 0; i < listeners.size(); ++ i )
                fireUsbPipeEvent( (UsbPipeListener)listeners.elementAt( i ), event );

        }

        //---------------------------------------------------------------------
        // Protected abstract methods
        //

        /**
         * Fires a UsbPipeListener event to the listener
         * @param listener the UsbPipeListener object
         * @param e the UsbServicesEvent to fire
         */
        protected abstract void fireUsbPipeEvent( UsbPipeListener listener, UsbPipeEvent e );

        //---------------------------------------------------------------------
        // Instance variables
        //

        protected UsbPipeEvent event = null;
    }

    /**
     * Simple Task implementation for delivering 
     * UsbPipeListner.errorEventOccurred( UsbPipeErrorEvent ) to listeners
     * @author E. Michael Maximilien
     */
    protected class UsbPipeErrorEventTask extends EventTask implements Task
    {
        /** 
         * One argument ctor
         * @param event the UsbPipeErrorEvent object
         */
        public UsbPipeErrorEventTask( UsbPipeErrorEvent event ) { super( event ); }

        //---------------------------------------------------------------------
        // Protected abstract methods
        //

        /**
         * Fires a UsbPipeListner.errorEventOccurred event to the listener
         * @param listener the UsbPipeListner object
         * @param e the UsbPipeEvent to fire
         */
        protected void fireUsbPipeEvent( UsbPipeListener listener, UsbPipeEvent e )
        { listener.errorEventOccurred( (UsbPipeErrorEvent)e ); }
    }

    /**
     * Simple Task implementation for delivering 
     * UsbPipeListner.dataEventOccurred( UsbPipeDataEvent ) to listeners
     * @author E. Michael Maximilien
     */
    protected class UsbPipeDataEventTask extends EventTask implements Task
    {
        /** 
         * One argument ctor
         * @param event the UsbPipeDataEvent object
         */
        public UsbPipeDataEventTask( UsbPipeDataEvent event ) { super( event ); }

        //---------------------------------------------------------------------
        // Protected abstract methods
        //

        /**
         * Fires a UsbPipeListner.dataEventOccurred event to the listener
         * @param listener the UsbPipeListner object
         * @param e the UsbPipeEvent to fire
         */
        protected void fireUsbPipeEvent( UsbPipeListener listener, UsbPipeEvent e )
        { listener.dataEventOccurred( (UsbPipeDataEvent)e ); }
    }

    //-------------------------------------------------------------------------
    // Instance variables
    //

    private TaskScheduler taskScheduler = null;
    private UsbPipe usbPipe = null;

    private Vector listeners = new Vector();
}                                                                             
