package com.ibm.jusb.tools;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import java.awt.Component;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.UIManager;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;

import javax.usb.*;

/**
 * TreeCellRenderer for UsbInfoTree to build tree nodes with appropriate
 * icons and tool-tips from the UsbInfo objects
 * @author E. Michael Maximilien
 * @version 0.0.1 (JDK 1.1.x with Swing 1.1.x)
 */
public class UsbInfoTreeCellRenderer extends JLabel implements TreeCellRenderer
{
    //-------------------------------------------------------------------------
    // Public method implementing TreeCellRederer interface
    //

    /**
     * This is messaged from JTree whenever it needs to get the size
     * of the component or it wants to draw it
     * @param tree the JTree object that called this method
     * @param value the TreeNode object that is about to be redered in the tree
     * @param selected flag indicating that the node is selected
     * @param expanded flag indicating that the node is expanded
     * @param leaf flag indicating that the node is a leaf node
     * @param row indicates the row of this node
     * @param hasFocus flag indicating if this node has focus
     */
    public Component getTreeCellRendererComponent( JTree tree, Object value, boolean selected, boolean expanded,
                                                   boolean leaf, int row, boolean hasFocus ) 
    {
        UsbInfo usbInfo = (UsbInfo)( (DefaultMutableTreeNode)value ).getUserObject();

        this.hasFocus = hasFocus;
        this.selected = selected;

        setText( usbInfo.getName() );
      
        usbInfo.accept( imageIconV );
        
        setIcon( imageIconV.getTreeImageIcon() );

        return this;
    }

    //-------------------------------------------------------------------------
    // Overridden methods from JComponent
    //

    /**
     * paint is subclassed to draw the background correctly.  JLabel
     * currently does not allow backgrounds other than white, and it
     * will also fill behind the icon.  Something that isn't desirable
     * @param g the Graphics object to paint with
     */
    public void paint( Graphics g )
    {
        Color bColor;
        Icon currentIcon = getIcon();
        int offset = -1;

        if( selected )
            bColor = SELECTED_BACKGROUND_COLOR;
        else
            if( getParent() != null )
                bColor = getParent().getBackground();
            else
                bColor = getBackground();

        g.setColor( bColor );

        if( currentIcon != null && getText() != null ) 
        {
            offset = ( currentIcon.getIconWidth() + getIconTextGap() );

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

    private UsbInfoImageIconV imageIconV = new UsbInfoImageIconV();

    //-------------------------------------------------------------------------
    // Class constants
    //

    public static final Color SELECTED_BACKGROUND_COLOR = Color.yellow;
}
