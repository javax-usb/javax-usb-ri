package com.ibm.jusb.tools;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import java.util.*;
import javax.swing.*;

import javax.usb.*;
import javax.usb.util.*;

/**
 * This is a simple ListModel implementing class
 * @author E. Michael Maximilien
 * @version 1.1.x (JDK 1.1.x)
 */
public class UsbInfoListModel extends AbstractListModel
{
    //-------------------------------------------------------------------------
    // Ctor
    //

    /** Default ctor */
    public UsbInfoListModel() {}

    //-------------------------------------------------------------------------
    // Public instance methods
    //

    /**
     * Sets the List Model data (all data in vector need to be ListModelElement objects)
     * @param usbInfoIterator an iterator of UsbInfo
     */
    public void setListModelData( UsbInfoIterator usbInfoIterator )
    {
        Vector vector = new Vector();

        while( usbInfoIterator.hasNext() )
            vector.addElement( usbInfoIterator.nextUsbInfo() );

        setListVector( vector );

        fireContentsChanged( this, 0, getListVector().size() );
    }
    
    /**
     * Adds the UsbInfo in this ListModel keeping it sorted
     * @param usbInfo the UsbInfo to add
     */
    public void addElement( UsbInfo usbInfo )
    {
        getListVector().addElement( usbInfo );
        fireContentsChanged( this, 0, getListVector().size() );
    }

    /**
     * Removes the UsbInfo from the List
     * @param usbInfo the UsbInfo to remove
     */
    public void removeElement( UsbInfo usbInfo ) 
    {
        int usbInfoIndex = getListVector().indexOf( usbInfo );
        
        getListVector().removeElement( usbInfo ); 
        fireIntervalRemoved( this, usbInfoIndex, getListVector().size() );
    }

    /** Removes all the element in this list */
    public void removeAllElements() 
    { 
        int size = getListVector().size();

        getListVector().removeAllElements(); 
        fireIntervalRemoved( this, 0, size );
    }

    /** @return an Enumeration of the UsbInfo for this model */
    public Enumeration elements() { return getListVector().elements(); }

    /** @return true if this ListModel is empty */
    public boolean isEmpty() { return getListVector().isEmpty(); }

    //-------------------------------------------------------------------------
    // ListModel interface methods
    //

    /** @return the current size of the ListModel */
    public int getSize() { return getListVector().size(); }

    /**
     * @return the element at the position indicated
     * @param i the int index
     */
    public Object getElementAt( int i ) { return getListVector().elementAt( i ); }

    //-------------------------------------------------------------------------
    // Private methods
    //

    /** @return the current Vector of UsbInfo */
    private Vector getListVector() { return listVector; }

    /**
     * Sets the list vector and sorts it
     * @param v the Vector instance
     */
    private void setListVector( Vector v ) { listVector = v; }

    //-------------------------------------------------------------------------
    // Instance variables
    //

    private Vector listVector = new Vector();
}
