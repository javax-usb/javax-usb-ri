package com.ibm.jusb.tools;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the IBM Public License.
 *
 */

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import javax.usb.*;
import javax.usb.util.*;

/**
 * Defines a subclass for displaying a tree of UbsInfo objects
 * @author E. Michael Maximilien
 * @version 0.0.1 (JDK 1.1.x)
 */
public class UsbInfoTree extends JTree 
{
    /** Default ctor */
    public UsbInfoTree()
    {
        getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
        setShowsRootHandles( true );
        setModel( null );

        putClientProperty( "JTree.lineStyle", "Angled" );
    }

    /** 
     * Default 1-arg ctor
     * @param rootHub the root hub
     */ 
    public UsbInfoTree( UsbHub rootHub ) 
    {
        this();
        setRootHub( rootHub );
    }

    //-------------------------------------------------------------------------
    // Public methods
    //

    public void setRootHub( UsbHub usbHub )
    {
        setCellRenderer( new UsbInfoTreeCellRenderer() );
        model = new UsbInfoTreeModel( buildRootTreeNode( usbHub ) );
        setModel( model );
    }

    public void expandAll()
    {
        for( int i = 0; i < getRowCount(); ++i )
            expandRow( i );
    }

    //-------------------------------------------------------------------------
    // Private methods
    //

    /** 
     * @return a root TreeNode with the contents of the UsbRootHub info 
     * @param rootHub the UsbHub object
     */
    private DefaultMutableTreeNode buildRootTreeNode( UsbHub rootHub )
    {
        DefaultMutableTreeNode rootHubNode = new DefaultMutableTreeNode( rootHub );

        buildHubNodes( rootHubNode, rootHub );

        return rootHubNode;
    }

    private void buildHubNodes( DefaultMutableTreeNode hubNode, UsbHub usbHub )
    {
        UsbInfoIterator portIterator = usbHub.getUsbPorts();

        while( portIterator.hasNext() )
        {
            UsbPort usbPort = (UsbPort)portIterator.nextUsbInfo();

            if( usbPort.isUsbDeviceAttached() )
            {
                DefaultMutableTreeNode deviceNode = new DefaultMutableTreeNode( usbPort.getUsbDevice() );
                hubNode.add( deviceNode );

                if( usbPort.getUsbDevice().isUsbHub() )
                    buildHubNodes( deviceNode, (UsbHub)usbPort.getUsbDevice() );
                else           
                    buildDeviceNodes( deviceNode, usbPort.getUsbDevice() );
            }
            else
                hubNode.add( new DefaultMutableTreeNode( usbPort ) ); 
        }
    }

    private void buildDeviceNodes( DefaultMutableTreeNode deviceNode, UsbDevice usbDevice )
    {
        UsbInfoIterator configIterator = usbDevice.getUsbConfigs();

        while( configIterator.hasNext() )
        {
            UsbConfig usbConfig = (UsbConfig)configIterator.nextUsbInfo();

            if( usbConfig.getUsbInterfaces().size() > 0 )
            {
                DefaultMutableTreeNode configNode = new DefaultMutableTreeNode( usbConfig );
                deviceNode.add( configNode );

                buildConfigNodes( configNode, usbConfig );
            }
            else
                deviceNode.add( new DefaultMutableTreeNode( usbConfig ) ); 
        }
    }

    private void buildConfigNodes( DefaultMutableTreeNode configNode, UsbConfig usbConfig )
    {
        UsbInfoIterator interfaceIterator = usbConfig.getUsbInterfaces();

        while( interfaceIterator.hasNext() )
        {
            UsbInterface usbInterface = (UsbInterface)interfaceIterator.nextUsbInfo();

			UsbInfoIterator alternateIterator = usbInterface.getAlternateSettings();

			while( alternateIterator.hasNext() )
			{
				UsbInterface usbInterfaceSetting = (UsbInterface)alternateIterator.nextUsbInfo();

				if( usbInterfaceSetting.getUsbEndpoints().size() > 0 )
				{
					DefaultMutableTreeNode interfaceNode = new DefaultMutableTreeNode( usbInterfaceSetting );
					configNode.add( interfaceNode );

					buildInterfaceNodes( interfaceNode, usbInterfaceSetting );
				}
				else
					configNode.add( new DefaultMutableTreeNode( usbInterfaceSetting ) ); 
			}
        }
    }

    private void buildInterfaceNodes( DefaultMutableTreeNode interfaceNode, UsbInterface usbInterface )
    {
        UsbInfoIterator endpointIterator = usbInterface.getUsbEndpoints();

        while( endpointIterator.hasNext() )
            interfaceNode.add( new DefaultMutableTreeNode( endpointIterator.nextUsbInfo() ) ); 
    }

    //-------------------------------------------------------------------------
    // Private instance variables
    //

    private UsbInfoTreeModel model = null;
}
