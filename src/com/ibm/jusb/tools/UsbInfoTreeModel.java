package com.ibm.jusb.tools;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import javax.swing.*;
import javax.swing.tree.*;

import javax.usb.*;

/**
 * Defines a subclass for displaying a tree of UbsInfo objects
 * @author E. Michael Maximilien
 * @version 0.0.1 (JDK 1.1.x)
 */
public class UsbInfoTreeModel extends DefaultTreeModel 
{
    /** 
     * Default 1-arg ctor
     * @param TreeNode the root TreeNode object
     */ 
    public UsbInfoTreeModel( DefaultMutableTreeNode rootNode ) 
    {
        super( rootNode );

        this.rootNode = rootNode;
    }

    //-------------------------------------------------------------------------
    // Public methods
    //

    //-------------------------------------------------------------------------
    // Private methods
    //

    //-------------------------------------------------------------------------
    // Private instance variables
    //

    private DefaultMutableTreeNode rootNode = null;
}
