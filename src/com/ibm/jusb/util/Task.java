package com.ibm.jusb.util;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

/**
 * Defines an interface for task that will execute synchronous or
 * or asynchronously
 * @author E. Michael Maximilien
 */
public interface Task
{
    //-------------------------------------------------------------------------
    // Public methods
    //

    /** Called to execute the task */
    public void execute();
}
