package com.ibm.jusb.tools;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import java.awt.*;
import javax.swing.*;

import javax.usb.*;

/**
 * Display an icon and a string for each list object
 * @author E. Michael Maximilien
 * @version 0.2.0 (JDK 1.1.x with Swing 1.1.x)
 */
public class UsbInfoListCellRenderer extends JLabel implements ListCellRenderer 
{
    //-------------------------------------------------------------------------
    // ListCellRenderer method
    //

    /**
     * @return the Component to use to draw the list cell
     * @param list the JList
     * @param value a AbstractDevice object to display
     * @param index the cell index
     * @param isSelected the selected cell
     * @param cellHasFocus the list and the cell have the focus
     */
    public Component getListCellRendererComponent( JList list, Object value, int index, 
                                                   boolean isSelected, boolean cellHasFocus )
    {
        selected = isSelected;
        this.hasFocus = cellHasFocus;

        UsbInfo usbInfo = (UsbInfo)value;

        setText( usbInfo.getName() );
        
        usbInfo.accept( usbInfoImageIconV );

        setIcon( usbInfoImageIconV.getTreeImageIcon() );
      
        return this;
    } 

    /**
     * paint is subclassed to draw the background correctly.  JLabel
     * currently does not allow backgrounds other than white, and it
     * will also fill behind the icon.  Something that isn't desirable
     * @param g the Graphics object to paint with
     */
    public void paint( Graphics g )
    {
        Color bColor;
        Icon currentI = getIcon();
        int offset = -1;

        if( selected )
            bColor = SELECTED_BACKGROUND_COLOR;
        else
        if( getParent() != null )
            bColor = getParent().getBackground();
        else
            bColor = getBackground();

        g.setColor( bColor );

        if( currentI != null && getText() != null ) 
        {
            offset = ( currentI.getIconWidth() + getIconTextGap() );

            g.fillRect( offset, 0, getWidth() - offset, getHeight() - 1 );
        }
        else
            g.fillRect( 0, 0, getWidth() , getHeight() -1 );

        super.paint( g );
    }

    //-------------------------------------------------------------------------
    // Instance variables
    //

    protected boolean selected = false;
    private boolean hasFocus = false;

    private UsbInfoImageIconV usbInfoImageIconV = new UsbInfoImageIconV(); 

    //-------------------------------------------------------------------------
    // Class constants
    //

    public static final Color SELECTED_BACKGROUND_COLOR = Color.yellow;
}
