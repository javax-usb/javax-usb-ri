package com.ibm.jusb.tools.usbview;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the IBM Public License.
 *
 */

import java.awt.*;
import javax.swing.*;

/**
 * Simple status panel for MainFrame
 * @author E. Michael Maximilien
 * @version 0.0.1 (JDK 1.1.x)
 */
class StatusPanel extends JPanel
{
    public StatusPanel()
    {
        setLayout( new GridLayout( 1, 1 ) );

        statusTextField.setEditable( false );

        add( statusTextField );
    }

    //-------------------------------------------------------------------------
    // Public methods
    //

    public void print( String s )
    { statusTextField.setText( s ); }

    public void append( String s )
    { statusTextField.setText( statusTextField.getText() + s ); }

    //-------------------------------------------------------------------------
    // Instance variables
    //

    private JTextField statusTextField = new JTextField( 25 );
}
