package com.ibm.jusb.tools.pipeview;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the IBM Public License.
 *
 */

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import javax.usb.*;
import javax.usb.os.*;
import javax.usb.event.*;
import javax.usb.util.*;
import com.ibm.jusb.tools.*;

/**
 * MainFrame of the PipeTest (copied from UsbViewApp)
 * @author Dan Streetman
 * @version 0.0.1 (JDK 1.1.x)
 */
public class MainFrame extends com.ibm.jusb.tools.usbview.MainFrame {

    /** Constructor */
    public MainFrame( String title ) {
        super( title );

        getUsbInfoTreePanel().getUsbInfoTree().addMouseListener( treePanelListener );
    }

    //*************************************************************************
    // Protected methods

    /**
     * Process a click on the treePanel
	 * Only Button 3 (right button) clicks on UsbDevice or UsbEndpoints are processed
     */
    protected void treePanelClicked( MouseEvent e ) {
        if ( 0 == (e.getModifiers() & BUTTON_MASK) ) return;

        UsbInfoTree tree = (UsbInfoTree)e.getComponent();
        TreePath path = tree.getPathForLocation( e.getX(), e.getY() );

        if ( null != path ) {
			Object item = path.getLastPathComponent();

			if ( item instanceof DefaultMutableTreeNode ) item = ((DefaultMutableTreeNode)item).getUserObject();
			if ( item instanceof UsbInfo ) ((UsbInfo)item).accept( nodeVisitor );
		}
    }

    //*************************************************************************
    // Private methods

	/** Open a panel to claim/release an interface */
	private void openUsbInterfacePanel( UsbInterface usbInterface )
	{
		String name = usbInterface.getUsbDevice().getName() + " " + usbInterface.getName();

		if (panels.containsKey( usbInterface ))
			bringJFrameToFront( usbInterface );
		else
			openJFrame( name, new UsbInterfacePanel( usbInterface ), usbInterface );
	}

	/** Open a panel for DCP */
	private void openRequestPanel( UsbDevice usbDevice )
	{
		String name = usbDevice.getName() + " Default Control Pipe";

		if (panels.containsKey( usbDevice ))
			bringJFrameToFront( usbDevice );
		else
			JOptionPane.showMessageDialog( this, "Default Control Pipe Requests not yet supported." );
	}

	/** Open a panel for a UsbPipe */
	private void openUsbPipePanel( UsbEndpoint usbEndpoint )
	{
		String name = usbEndpoint.getUsbDevice().getName() + " " + usbEndpoint.getName() + " UsbPipe";

		if (panels.containsKey( usbEndpoint ))
			bringJFrameToFront( usbEndpoint );
		else
			openJFrame( name, new UsbPipePanel( usbEndpoint ), usbEndpoint );
	}

	/**
	 * Open JFrame with specified contents.
	 * @param name the name to use.
	 * @param panel the JPanel to use as contents.
	 * @param key the key to use.
	 */
	private void openJFrame( String name, JPanel panel, Object key )
	{
		JFrame frame = new JFrame( name );

		panels.put( key, frame );

		frame.addWindowListener( getWindowListener( key ) );
		frame.getContentPane().add( panel );
		frame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		frame.setLocation( getRandomX(), getRandomY() );
		frame.pack();
		frame.setVisible( true );
	}

	/**
	 * Bring the already-open JFrame to the front.
	 * @param the key to use to find the JFrame.
	 */
	private void bringJFrameToFront( UsbInfo usbInfo )
	{
		((JFrame)panels.get( usbInfo )).setVisible( true );
		((JFrame)panels.get( usbInfo )).toFront();
	}

	/** @return a WindowListner to handle this JFrame closing */
	private WindowListener getWindowListener( final Object object )
	{
		final Hashtable wlTable = panels;

		return new WindowAdapter() {
			public void windowClosing( WindowEvent e )
			{
				wlTable.remove( object );
			}
		};
	}

	/* @return a random x-location in this Frame's space */
	private int getRandomX()
	{
		return getX() + (int)((double)getWidth() * Math.random());
	}

	/* @return a random y-location in this Frame's space */
	private int getRandomY()
	{
		return getY() + (int)((double)getHeight() * Math.random());
	}

    //*************************************************************************
    // Instance variables

    private MouseListener treePanelListener = new MouseAdapter() {
        public void mouseClicked( MouseEvent e )
        { MainFrame.this.treePanelClicked( e ); }
    };

	private Hashtable panels = new Hashtable();

	private UsbInfoVisitor nodeVisitor = new NodeVisitor();

    //*************************************************************************
    // Class constants

	private static final int BUTTON_MASK = MouseEvent.BUTTON3_MASK;

	//*************************************************************************
	// Inner classes

	private class NodeVisitor extends DefaultUsbInfoV implements UsbInfoVisitor
	{
		public void visitUsbDevice( UsbInfo usbInfo )
		{ MainFrame.this.openRequestPanel( (UsbDevice)usbInfo ); }
		public void visitUsbInterface( UsbInfo usbInfo )
		{ MainFrame.this.openUsbInterfacePanel( (UsbInterface)usbInfo ); }
		public void visitUsbEndpoint( UsbInfo usbInfo )
		{ MainFrame.this.openUsbPipePanel( (UsbEndpoint)usbInfo ); }
	}

}
