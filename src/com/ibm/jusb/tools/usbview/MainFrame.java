package com.ibm.jusb.tools.usbview;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

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
 * MainFrame of the UsbViewApp
 * @author E. Michael Maximilien
 * @version 0.0.1
 */
public class MainFrame extends JFrame
{
	public MainFrame() { this( JAVAX_USB_VIEW_APP_TITLE_STRING ); }

    public MainFrame( String title )
    {
        super( title );
        init();
        initMenu();
        initListeners();
    }

    //-------------------------------------------------------------------------
    // Protected methods
	//

    protected StatusPanel getStatusPanel() { return statusPanel; }
    protected UsbInfoPanel getUsbInfoPanel() { return usbInfoPanel; }
    protected UsbInfoListPanel getUsbHubListPanel() { return usbHubListPanel; }
    protected UsbInfoListPanel getUsbDeviceListPanel() { return usbDeviceListPanel; }
    protected UsbInfoTreePanel getUsbInfoTreePanel() { return usbInfoTreePanel; }
    protected JSplitPane getJSplitPane() { return jSplitPane; } 
    protected JTabbedPane getJTabbedPane() { return jTabbedPane; }
    


    protected void init()
    {

        getContentPane().setLayout( new BorderLayout() );

        jTabbedPane = new JTabbedPane( JTabbedPane.TOP );

        initTabbedPane();

        initCardLayout();

        jSplitPane = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT); 
        jSplitPane.setLeftComponent( jTabbedPane );
        jSplitPane.setRightComponent( rightInfoPane );

        getContentPane().add( jSplitPane, BorderLayout.CENTER );

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout( new BoxLayout( bottomPanel, BoxLayout.Y_AXIS ) );

        JPanel buttonPanel = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
        buttonPanel.add( refreshButton );

        bottomPanel.add( buttonPanel );
        bottomPanel.add( statusPanel );

        getContentPane().add( bottomPanel, BorderLayout.SOUTH );

        setSize( 585, 530 );
        jSplitPane.setDividerLocation( 250 );
        jSplitPane.setOneTouchExpandable( true );
        centerFrame();
        refresh();
    }

    protected void initTabbedPane()
    {
        jTabbedPane.addTab( "Topology", usbInfoTreePanel );
        jTabbedPane.addTab( "Hub list", usbHubListPanel );
        jTabbedPane.addTab( "Device list", usbDeviceListPanel );
    }

    protected void initCardLayout()
    {
        rightInfoPane.add( USB_INFO_PANEL, usbInfoPanel );
    }

    protected void initListeners()
    {

        refreshButton.addActionListener(    new ActionListener()
                                            {
                                                public void actionPerformed( ActionEvent e ) 
                                                { refreshButtonClicked(); }
                                            }
                                       );




        usbHubListPanel.getUsbInfoList().addListSelectionListener(  new ListSelectionListener()
                                                                    {
                                                                        public void valueChanged( ListSelectionEvent e )
                                                                        { hubListValueChanged( e ); }
                                                                    }
                                                                 );

        usbDeviceListPanel.getUsbInfoList().addListSelectionListener(   new ListSelectionListener()
                                                                        {
                                                                            public void valueChanged( ListSelectionEvent e )
                                                                            { deviceListValueChanged( e ); }
                                                                        }
                                                                    );
        
        usbInfoTreePanel.getUsbInfoTree().addTreeSelectionListener( new TreeSelectionListener()
                                                                    {
                                                                        public void valueChanged( TreeSelectionEvent e ) 
                                                                        { treeValueChanged( e ); }
                                                                    }
                                                                  );

    }

    protected void centerFrame()
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        setLocation( ( screenSize.width / 2 ) - ( getSize().width / 2 ),
                     ( screenSize.height / 2 ) - ( getSize().height / 2 ) );
    }

    protected void setListPanels( UsbHub rootHub ) 
    {

        usbHubListPanel.setUsbInfos( UsbHubUtility.bfsUsbHubs( rootHub ) );
        usbDeviceListPanel.setUsbInfos( UsbHubUtility.bfsUsbDevices( rootHub ) );
    }

    protected void initMenu()
    {
       JMenuBar jMenuBar = new JMenuBar();
       JMenu fileMenu = new JMenu( "File" );
       JMenu editMenu = new JMenu( "Edit" );
       JMenu helpMenu = new JMenu( "Help" );

       fileMenu.setMnemonic( 'F' );
       editMenu.setMnemonic( 'E' );
       helpMenu.setMnemonic( 'H' );

       JMenuItem exitMenuItem = new JMenuItem( "Exit" );
       JMenuItem refreshMenuItem = new JMenuItem( "Refresh" );
       JMenuItem aboutMenuItem = new JMenuItem( "About" );

       exitMenuItem.setMnemonic( 'x' );
       refreshMenuItem.setMnemonic( 'R' );
       aboutMenuItem.setMnemonic( 'A' );

       fileMenu.add( exitMenuItem );
       editMenu.add( refreshMenuItem );
       helpMenu.add( aboutMenuItem );

       jMenuBar.add( fileMenu );
       jMenuBar.add( editMenu );
       jMenuBar.add( helpMenu );

       setJMenuBar( jMenuBar );


       exitMenuItem.addActionListener( new ActionListener()
                                       {
                                           public void actionPerformed( ActionEvent e )
                                           { exitMenuItemSelected(); }
                                       }
                                     );
       refreshMenuItem.addActionListener( new ActionListener()                            
                                          {                                               
                                              public void actionPerformed( ActionEvent e )
                                              { refreshMenuItemSelected(); }                 
                                          }                                               
                                        );                                                

       aboutMenuItem.addActionListener( new ActionListener()                            
                                        {                                               
                                            public void actionPerformed( ActionEvent e )
                                            { aboutMenuItemSelected(); }                 
                                        }                                               
                                      );                                                

    }

    //-------------------------------------------------------------------------
    // Private methods
    //

    private void windowClosing() { System.exit( 0 ); }

    private void refreshButtonClicked() { refresh(); }
    
    private void refreshMenuItemSelected() { refresh(); }
    
    private void exitMenuItemSelected() { windowClosing(); }

    private void aboutMenuItemSelected() {}

    private void hubListValueChanged( ListSelectionEvent lse )
    {
        int index = usbHubListPanel.getUsbInfoList().getSelectedIndex();

        UsbHub usbHub = (UsbHub)( (UsbInfoListModel)usbHubListPanel.getUsbInfoList().getModel() ).getElementAt( index );
        usbInfoPanel.setUsbInfo( usbHub );
        cardLayout.show( rightInfoPane, USB_INFO_PANEL );        
    }

    private void deviceListValueChanged( ListSelectionEvent lse )
    {
        int index = usbDeviceListPanel.getUsbInfoList().getSelectedIndex();

        UsbDevice usbDevice = (UsbDevice)( (UsbInfoListModel)usbDeviceListPanel.getUsbInfoList().getModel() ).getElementAt( index );
        usbInfoPanel.setUsbInfo( usbDevice );
        cardLayout.show( rightInfoPane, USB_INFO_PANEL );
    }

    private void treeValueChanged( TreeSelectionEvent e )
    {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)e.getPath().getLastPathComponent();
        usbInfoPanel.setUsbInfo( (UsbInfo)node.getUserObject() );
        cardLayout.show( rightInfoPane, USB_INFO_PANEL );        
    }

    //-------------------------------------------------------------------------
    // Instance variables
    //

    public void refresh()
    {
        Cursor currentCursor = getCursor();

        try
        {
            setCursor( new Cursor( Cursor.WAIT_CURSOR ) );

            UsbServices usbServices = UsbHostManager.getInstance().getUsbServices();

            UsbRootHub rootHub = usbServices.getUsbRootHub();

            setListPanels( rootHub );

            usbInfoTreePanel.setRootUsbHub( rootHub );
            usbInfoTreePanel.getUsbInfoTree().expandAll();

            if ( !topologyListening ) {
                usbServices.addUsbServicesListener( topologyListener );
                topologyListening = true;
                }
        }
        catch( UsbException e )
        {
            JOptionPane.showMessageDialog( this, e.getMessage(), ERROR_DIALOG_TITLE_STRING, JOptionPane.ERROR_MESSAGE );
        }
        finally 
        { setCursor( currentCursor ); }
    }

    //-------------------------------------------------------------------------
    // Instance variables
    //

    private JButton refreshButton = new JButton( "Refresh" );

    protected JSplitPane jSplitPane = null;
    protected JTabbedPane jTabbedPane = null;
    
    protected CardLayout cardLayout = new CardLayout();
    protected JPanel rightInfoPane = new JPanel( cardLayout );

    private StatusPanel statusPanel = new StatusPanel();
    protected UsbInfoPanel usbInfoPanel = new UsbInfoPanel();
    protected UsbInfoListPanel usbHubListPanel = new UsbInfoListPanel();
    protected UsbInfoListPanel usbDeviceListPanel = new UsbInfoListPanel();
    protected UsbInfoTreePanel usbInfoTreePanel = new UsbInfoTreePanel();

    private boolean topologyListening = false;
    private UsbServicesListener topologyListener = new UsbServicesListener() {
        public void usbDeviceAttached( UsbServicesEvent event )
            { MainFrame.this.refresh(); }
        public void usbDeviceDetached( UsbServicesEvent event )
            { MainFrame.this.refresh(); }
        };

    //-------------------------------------------------------------------------
    // Class constants
    //

	public static final String JAVAX_USB_VIEW_APP_TITLE_STRING = "javax.usb View Tool";
    public static final String ERROR_DIALOG_TITLE_STRING = "javax.usb View Tool Error";
    public static final String USB_INFO_PANEL = "UsbInfoPanel";
}
